"""SubShift - a simple script to apply offsets to subtitles"""

# Copyright(C) 2020 - Samuel Ivarsson
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY
# without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see

import subtitle


MAX_WIDTH = 750
MAX_HEIGHT = 500

LINE = str("05:13:44,123 --> 05:13:46,144")
SUB = subtitle.SubtitleClass(LINE)
SUB.print_array()
