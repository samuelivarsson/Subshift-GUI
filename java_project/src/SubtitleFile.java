import jdk.jshell.spi.ExecutionControl;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SubtitleFile {

    private Pattern timePattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2},\\d{3}");
    private File file;
    private ArrayList<String> lines;

    public SubtitleFile(File file) throws Exception {
        this.file = file;
        this.lines = createLines(file);
    }

    private ArrayList<String> createLines(File file) throws Exception {

        ArrayList<String> lines = new ArrayList<>();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("srt-files only", "srt");

        if (!file.isFile() || !filter.accept(file)) throw new ExecutionControl.UserException("Wrong file-type", "Subtitle", new StackTraceElement[]{});

        FileReader fr = new FileReader(file);   //reads the file
        BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
        String line;
        while((line=br.readLine())!=null)
        {
            lines.add(line);      //appends line to string buffer
            lines.add("\n");     //line feed
        }
        fr.close();    //closes the stream and release the resources

        return lines;
    }

    public void shiftSubtitles(long milliSeconds) throws Exception {
        String absPath = file.getAbsolutePath();
        String copyPath = absPath+"1";
        String extension = "";
        int i = absPath.lastIndexOf('.');
        if (i > 0) {
            extension = absPath.substring(i+1);
            copyPath = absPath.substring(0, i) + "_original." + extension;
        }

        File originalFile = new File(copyPath);
        if(!originalFile.exists() && originalFile.isDirectory()) {
            FileWriter fw = new FileWriter(originalFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String line: lines) {
                bw.write(line);
            }
            bw.close();
        }

        shiftLines(milliSeconds);

        File newFile = new File(absPath);
        fw = new FileWriter(newFile, false);
        bw = new BufferedWriter(fw);
        for (String line: lines) {
            bw.write(line);
        }
        bw.close();
    }

    private void shiftLines(long milliSeconds) {
        for (int i = 0; i < lines.size(); i++) {
            if (timePattern.matcher(lines.get(i)).matches()) {
                Subtitle subtitle = new Subtitle(lines.get(i));
                String newLine = subtitle.shiftLine(milliSeconds);
                lines.set(i, newLine);
            }
        }
    }

}
