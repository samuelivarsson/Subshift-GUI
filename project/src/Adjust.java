import java.io.BufferedReader;
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
        Process p;
        try {
            String commands = "\"srt -i shift " + milliSeconds + " \"" + path + "\"\"";
            String[] totCommand = {"cmd", "/c", commands};

            p = Runtime.getRuntime().exec(totCommand);

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
