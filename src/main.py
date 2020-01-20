"SubShift - a simple script to apply offsets to subtitles"

# Copyright(C) 2020 - Samuel Ivarsson. All rights reserved.

import os
import platform
import tkinter as tk
import traceback
import TkinterDnD2 as tkdnd
from subtitle import SubtitleFile


MAX_WIDTH = 750
MAX_HEIGHT = 500
BG_COLOR = "#21252B"
BG_COLOR_LIGHT = "#2C313C"
TEXT_COLOR = "#9FB2BF"
BORDER_COLOR = "#181A1F"


def adjust(path, milli_seconds):
    "Method for adjusting a srt file."
    file = SubtitleFile(path)
    file.shift_subtitles(milli_seconds)


def drop(event):
    "Function for handling drop event."
    DROPBOX_SV.set(event.data)


def on_enter(event):
    "Function to handle go button hover."
    GO_BTN["background"] = "#2F333D"


def on_leave(event):
    "Function to handle go button hover."
    GO_BTN["background"] = BG_COLOR


def go_btn_pressed():
    "Function to handle go button press."
    adjust(DROPBOX_SV.get(), 1000)


def show_error(*args):
    "Function for showing errors in tkinter window."
    err = traceback.format_exception(args[0], args[1], args[2])
    tk.messagebox.showerror('Exception', err)


def get_window_geometry(root, window_width, window_height):
    """
    Function to get window geometry.

    Used for centering program window.
    Returns:
        geometry (str): [width]x[height]+[left]+[top]
    """
    screen_width = root.winfo_screenwidth()
    screen_height = root.winfo_screenheight()
    offset = "+{}+{}".format(int(screen_width/2-window_width/2),
                             int(screen_height/2-window_height/2))

    return str(window_width) + "x" + str(window_height) + offset


# Main
if __name__ == "__main__":
    tk.Tk.report_callback_exception = show_error
    ROOT = tkdnd.TkinterDnD.Tk()
    ROOT.title("Subshift")
    ROOT_GEO = get_window_geometry(ROOT, MAX_WIDTH, MAX_HEIGHT)
    ROOT.geometry(ROOT_GEO)
    ROOT.configure(bg="YELLOW")

    # Big Frame
    BIG_FRAME = tk.Frame(ROOT)
    BIG_FRAME.configure(bg="RED")
    BF_MARGIN = 20
    BIG_FRAME.place(width=MAX_WIDTH-BF_MARGIN*2, height=MAX_HEIGHT-BF_MARGIN*2,
                    x=BF_MARGIN, y=BF_MARGIN)

    # Dropbox for drag and drop functionality
    DROPBOX_SV = tk.StringVar(value="Drag a srt-file here!")
    DROPBOX_FRAME = tk.Frame(BIG_FRAME, bd=1, bg=BORDER_COLOR,
                             width=400, height=300)
    DROPBOX_FRAME["width"] = 400
    DROPBOX_FRAME["height"] = 300
    DROPBOX_FRAME.grid(row=0, column=0, sticky="nesw")
    DROPBOX_FRAME.grid_propagate(0)
    DROPBOX = tk.Label(DROPBOX_FRAME, textvar=DROPBOX_SV, bg=BG_COLOR_LIGHT,
                       fg=TEXT_COLOR, wraplength=380)
    DROPBOX.pack(fill=tk.BOTH, expand=True)
    DROPBOX.drop_target_register(tkdnd.DND_FILES)
    DROPBOX.dnd_bind('<<Drop>>', drop)

    # Go Button
    GO_BTN = tk.Button(BIG_FRAME, text="Go!", command=go_btn_pressed,
                       bg=BG_COLOR, fg=TEXT_COLOR, bd=0,
                       activebackground=BG_COLOR_LIGHT,
                       activeforeground=TEXT_COLOR,
                       highlightbackground=BG_COLOR)
    GO_BTN.grid(row=1, column=0, sticky="nesw")
    if platform.system() != 'Darwin':
        # Mac doesn't let you tamper with button appearance
        GO_BTN.bind("<Enter>", on_enter)
        GO_BTN.bind("<Leave>", on_leave)

    # Make tkinter window come to the front on Mac
    if platform.system() == 'Darwin':
        os.system('''/usr/bin/osascript -e 'tell app "Finder"\
                    to set frontmost of process "Python" to true' ''')

    # Run
    ROOT.mainloop()
