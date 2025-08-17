import io
from typing import Tuple

import numpy as np
from PIL import Image, ImageEnhance, ImageOps, ImageFilter
import streamlit as st
from rembg import remove


# ----- Utility functions -----

def ensure_rgba(pil_image: Image.Image) -> Image.Image:
	"""Return a copy of the image in RGBA mode."""
	if pil_image.mode != "RGBA":
		return pil_image.convert("RGBA")
	return pil_image


def remove_background(pil_image: Image.Image) -> Image.Image:
	"""Use rembg to remove background, returning an RGBA image.

	Removes background even if rembg returns bytes by converting back to PIL.
	"""
	pil_image = ensure_rgba(pil_image)
	try:
		result = remove(pil_image)
		if isinstance(result, Image.Image):
			return ensure_rgba(result)
		if isinstance(result, (bytes, bytearray)):
			return Image.open(io.BytesIO(result)).convert("RGBA")
		# Fallback when result is a numpy array
		return Image.fromarray(result).convert("RGBA")
	except Exception:
		array = np.array(pil_image)
		result_array = remove(array)
		return Image.fromarray(result_array).convert("RGBA")


def apply_enhancements(image_rgba: Image.Image, brightness: float, contrast: float, saturation: float) -> Image.Image:
	"""Apply brightness, contrast and saturation adjustments."""
	rgb = image_rgba.convert("RGB")
	rgb = ImageEnhance.Brightness(rgb).enhance(brightness)
	rgb = ImageEnhance.Contrast(rgb).enhance(contrast)
	rgb = ImageEnhance.Color(rgb).enhance(saturation)
	return Image.merge("RGBA", (*rgb.split(), image_rgba.split()[-1]))


def feather_alpha(image_rgba: Image.Image, radius: float, opacity: float) -> Image.Image:
	"""Soften edges of alpha channel and apply global opacity using Pillow only."""
	if radius <= 0 and np.isclose(opacity, 1.0):
		return image_rgba
	# Split to manipulate alpha channel
	r, g, b, a = image_rgba.split()
	if radius > 0:
		a = a.filter(ImageFilter.GaussianBlur(radius=radius))
	if not np.isclose(opacity, 1.0):
		alpha_np = (np.array(a, dtype=np.float32) * float(opacity)).clip(0, 255).astype(np.uint8)
		a = Image.fromarray(alpha_np, mode="L")
	return Image.merge("RGBA", (r, g, b, a))


def transform_overlay(
	image_rgba: Image.Image,
	scale: float,
	rotation_deg: float,
	flip_horizontal: bool,
) -> Image.Image:
	"""Scale, rotate, and optionally mirror the overlay image."""
	w = max(1, int(image_rgba.width * scale))
	h = max(1, int(image_rgba.height * scale))
	img = image_rgba.resize((w, h), Image.LANCZOS)
	if flip_horizontal:
		img = ImageOps.mirror(img)
	img = img.rotate(rotation_deg, resample=Image.BICUBIC, expand=True)
	return img


def composite_images(
	background_rgba: Image.Image,
	overlay_rgba: Image.Image,
	offset_xy: Tuple[int, int],
) -> Image.Image:
	"""Paste overlay onto background using its alpha channel."""
	canvas = Image.new("RGBA", background_rgba.size, (0, 0, 0, 0))
	canvas.paste(overlay_rgba, (int(offset_xy[0]), int(offset_xy[1])), overlay_rgba)
	return Image.alpha_composite(background_rgba, canvas)


# ----- UI -----

st.set_page_config(page_title="Chandelier Swap", layout="wide")

st.title("Chandelier Swap")

with st.sidebar:
	st.markdown("**1. Upload photos**")
	background_file = st.file_uploader("Background photo (the room)", type=["jpg", "jpeg", "png"])
	chandelier_file = st.file_uploader("Chandelier photo", type=["jpg", "jpeg", "png"])

	st.markdown("**2. Foreground processing**")
	remove_bg = st.checkbox("Auto remove chandelier background", value=True)
	brightness = st.slider("Brightness", 0.2, 2.0, 1.0, 0.01)
	contrast = st.slider("Contrast", 0.2, 2.0, 1.0, 0.01)
	saturation = st.slider("Saturation", 0.0, 2.0, 1.0, 0.01)
	feather = st.slider("Edge feather (px)", 0.0, 40.0, 6.0, 0.5)
	opacity = st.slider("Opacity", 0.0, 1.0, 1.0, 0.01)

	st.markdown("**3. Transform**")
	scale = st.slider("Scale", 0.05, 3.0, 0.7, 0.01)
	rotation = st.slider("Rotation (deg)", -180.0, 180.0, 0.0, 0.1)
	flip_h = st.checkbox("Flip horizontally", value=False)


col_left, col_right = st.columns([1, 1])

if background_file is not None:
	background_img = Image.open(background_file).convert("RGBA")
	with col_left:
		st.subheader("Background")
		st.image(background_img, use_column_width=True)
else:
	st.info("Upload a background photo to begin.")
	st.stop()

if chandelier_file is None:
	st.info("Upload a chandelier photo.")
	st.stop()

# Prepare chandelier
chandelier_img = Image.open(chandelier_file).convert("RGBA")
if remove_bg:
	chandelier_img = remove_background(chandelier_img)

chandelier_img = apply_enhancements(chandelier_img, brightness, contrast, saturation)
chandelier_img = transform_overlay(chandelier_img, scale, rotation, flip_h)
chandelier_img = feather_alpha(chandelier_img, feather, opacity)

# Dynamic position sliders based on background size
max_x = max(0, background_img.width - 1)
max_y = max(0, background_img.height - 1)

# Default to roughly centered horizontally and above middle vertically
default_x = int((background_img.width - chandelier_img.width) / 2)
default_y = int(background_img.height * 0.15)

with st.sidebar:
	x_pos = st.slider("Offset X (px)", -background_img.width, background_img.width, default_x, 1)
	y_pos = st.slider("Offset Y (px)", -background_img.height, background_img.height, default_y, 1)

# Composite
result = composite_images(background_img, chandelier_img, (x_pos, y_pos))

with col_right:
	st.subheader("Result")
	st.image(result, use_column_width=True)

# Download
buffer = io.BytesIO()
result.save(buffer, format="PNG")
result_bytes = buffer.getvalue()

st.download_button("Download PNG", data=result_bytes, file_name="chandelier_swapped.png", mime="image/png")