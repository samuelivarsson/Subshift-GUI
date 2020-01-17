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

import re
import time


class SubtitleClass:
    """Class for shifting a subtitle."""

    def __init__(self, line):
        begin_time_array = re.split(r"\D", line[:12])
        end_time = re.split(r"\D", line[17:])
        self.begin_time = array_to_ms([int(i) for i in begin_time_array])
        self.end_time = array_to_ms([int(i) for i in end_time])

    def print_array(self):
        """Method for printing"""

        print(self.begin_time)
        print(self.end_time)

    def myfunc(self):
        """Do stuff."""

        print(self.begin_time)


def array_to_ms(array):
    """Function to turn array to milliseconds."""

    hours = array[0]*1000*60*60
    minutes = array[1]*1000*60
    seconds = array[2]*1000
    milli_seconds = array[3]

    return hours+minutes+seconds+milli_seconds
