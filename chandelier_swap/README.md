# Chandelier Swap Tool

A simple Streamlit app to replace a chandelier on a background photo with another chandelier image.

## Run

```bash
pip install -r chandelier_swap/requirements.txt
streamlit run chandelier_swap/app.py --server.headless true --server.port 8501
```

Upload:
- Background: the room/bathroom photo
- Foreground: the chandelier photo

Adjust scale, rotation, position and blending. Then click Download to save the result.