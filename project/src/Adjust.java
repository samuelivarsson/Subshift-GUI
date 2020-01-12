import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Adjust implements Runnable {

    private String path;
    private String milliSeconds;

    public Adjust(String path, String milliSeconds) {
        this.path = path;
        this.milliSeconds = milliSeconds;
    }

    @Override
    public void run() {
        SubtitleFile subtitleFile = null;
        try {
            subtitleFile = new SubtitleFile(new File(path));
            long milli = Integer.parseInt(milliSeconds);
            try {
                subtitleFile.shiftSubtitles(milli);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't read file (33)");
        }
    }
}
