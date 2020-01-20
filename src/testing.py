"""File for testing stuff."""

with open("test.srt") as file1:
    with open("test_original.srt") as file2:
        print(file1.readlines() == file2.readlines())
