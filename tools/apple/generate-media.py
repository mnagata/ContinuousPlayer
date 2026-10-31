#!/usr/bin/env python3
"""Create non-personal media used by the Apple UI snapshot fixture server."""
import argparse
from pathlib import Path
import subprocess
import wave

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('output', type=Path)
args = parser.parse_args()
args.output.mkdir(parents=True, exist_ok=True)
subprocess.run(['ffmpeg', '-hide_banner', '-loglevel', 'error', '-y', '-f', 'lavfi', '-i',
    'testsrc2=size=640x360:rate=24', '-t', '90', '-c:v', 'libx264', '-preset', 'ultrafast',
    '-crf', '24', '-pix_fmt', 'yuv420p', '-movflags', '+faststart', str(args.output / 'video.mp4')], check=True)
for name, seconds in [('audio', 90), ('short', 2)]:
    wav = args.output / (name + '.wav')
    with wave.open(str(wav), 'wb') as stream:
        stream.setparams((1, 2, 48000, 0, 'NONE', 'not compressed'))
        stream.writeframes(b'\0\0' * 48000 * seconds)
    subprocess.run(['ffmpeg', '-hide_banner', '-loglevel', 'error', '-y', '-i', str(wav),
        '-c:a', 'aac', '-b:a', '128k', '-movflags', '+faststart', str(args.output / (name + '.mp4'))], check=True)
(args.output / 'corrupt.mp4').write_bytes(b'Intentionally invalid UI snapshot sample\n')
print(args.output)
