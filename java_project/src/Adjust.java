import java.io.File;

public class Adjust implements Runnable {

    private String path;
    private long milliSeconds;

    public Adjust(String path, long milliSeconds) {
        this.path = path;
        this.milliSeconds = milliSeconds;
    }

    @Override
    public void run() {
        try {
            SubtitleFile subtitleFile = new SubtitleFile(new File(path));
            try {
                subtitleFile.shiftSubtitles(milliSeconds);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't read file (33)");
        }
    }
}
