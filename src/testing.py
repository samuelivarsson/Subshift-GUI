"""File for testing stuff."""

import sys
import os

print(sys.version)
print(os.path.dirname(sys.executable), 'tkdnd2.8')
print(os.path.join(os.path.dirname(sys.executable), 'tkdnd2.8'))
print(os.path.dirname(os.path.abspath(__file__)))

with open("test.srt") as file1:
    with open("test_original.srt") as file2:
        print(file1.readlines() == file2.readlines())
