ffmpeg -r 1/5.6 -pattern_type glob -i '*.jpg' -c:v libx264 -r 30 -pix_fmt yuv420p -vf scale=400:200 out.mp4


