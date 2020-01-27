"""SubShift - a simple script to apply offsets to subtitles."""

# Copyright(C) 2020 - Samuel Ivarsson. All rights reserved.

import os.path
import re

SUB_PATTERN = r"\d{2}:\d{2}:\d{2},\d{3} --> \d{2}:\d{2}:\d{2},\d{3}"


class Subtitle:
    """Class for shifting a subtitle."""

    def __init__(self, line):
        """Subtitle constructor."""
        begin_time_array = re.split(r"\D", line[:12])
        end_time = re.split(r"\D", line[17:-1])
        self.begin_time = array_to_ms([int(i) for i in begin_time_array])
        self.end_time = array_to_ms([int(i) for i in end_time])

    def print_times(self):
        """Print start and end time."""
        print(self.begin_time)
        print(self.end_time)

    def shift_line(self, milli_seconds):
        """Shifting a single line in a srt-file."""
        self.begin_time += milli_seconds
        self.end_time += milli_seconds

        if self.begin_time < 0:
            self.begin_time = 0
        if self.end_time < 0:
            self.end_time = 0

        new_begin_hour = str(
            int((self.begin_time / (1000 * 60 * 60)) % 99)).zfill(2)
        new_begin_minute = str(
            int((self.begin_time / (1000 * 60)) % 60)).zfill(2)
        new_begin_second = str(int((self.begin_time / 1000) % 60)).zfill(2)
        new_begin_millisecond = str(int(self.begin_time % 1000)).zfill(3)
        new_end_hour = str(
            int((self.end_time / (1000 * 60 * 60)) % 99)).zfill(2)
        new_end_minute = str(int((self.end_time / (1000 * 60)) % 60)).zfill(2)
        new_end_second = str(int((self.end_time / 1000) % 60)).zfill(2)
        new_end_millisecond = str(int(self.end_time % 1000)).zfill(3)

        return (new_begin_hour + ":" + new_begin_minute + ":"
                + new_begin_second + "," + new_begin_millisecond + " --> "
                + new_end_hour + ":" + new_end_minute + ":" + new_end_second
                + "," + new_end_millisecond)


class SubtitleFile:
    """Class for handling srt file."""

    def __init__(self, path):
        """Subtitlefile constructor."""
        if path[-4:] != ".srt":
            exception_message = "You didn't pass a srt-file to the program!"
            print(exception_message)
            raise TypeError()

        self.path = path

    def shift_subtitles(self, milli_seconds):
        """Shifting subtitle file."""
        new_file_path = self.path

        # If the input file is not an original srt file
        if "_original.srt" not in self.path:
            copy_path = self.path[:-4] + "_original.srt"
            # If no original srt file already exists
            if not os.path.exists(copy_path):
                # Create an original file (copy of the origianl):
                with open(self.path) as input_file:
                    copy_file = open(copy_path, "w")
                    copy_file.writelines(input_file)
                    copy_file.close()
        else:
            # If the input file is an "_original" file
            new_file_path = new_file_path[:-13] + ".srt"

        new_lines = self.shift_lines(milli_seconds)
        try:
            with open(new_file_path, "w") as new_file:
                new_file.writelines(new_lines)
        except EnvironmentError:
            print("Couldn't create shifted file!")

    def shift_lines(self, milli_seconds):
        """Shift all lines in an array of subtitle-lines."""
        new_lines = []
        with open(self.path) as input_file:
            for line in input_file.readlines():
                new_line = line

                if re.match(SUB_PATTERN, line):
                    subtitle = Subtitle(line)
                    new_line = subtitle.shift_line(milli_seconds) + "\n"

                new_lines.append(new_line)

        return new_lines


def array_to_ms(array):
    """Turn array to milliseconds."""
    hours = array[0] * 1000 * 60 * 60
    minutes = array[1] * 1000 * 60
    seconds = array[2] * 1000
    milli_seconds = array[3]

    return hours + minutes + seconds + milli_seconds
