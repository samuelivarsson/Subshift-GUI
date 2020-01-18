"SubShift - a simple script to apply offsets to subtitles"

# Copyright(C) 2020 - Samuel Ivarsson. All rights reserved.

import subtitle


MAX_WIDTH = 750
MAX_HEIGHT = 500

LINE = str("05:13:44,123 --> 05:13:46,144")
SUB = subtitle.Subtitle(LINE)
SUB.print_array()

FILE = subtitle.SubtitleFile("test.srt")
print(FILE.lines)
NEW_FILE = open("test2.srt", "w")
NEW_FILE.writelines(FILE.lines)
