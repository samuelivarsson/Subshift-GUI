import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Subtitle {

    private Date beginTime;
    private Date endTime;

    public Subtitle(String line) {
        long beginHourInMS = TimeUnit.HOURS.toMillis(Integer.parseInt(line.substring(0,2)));
        long beginMinuteInMS = TimeUnit.MINUTES.toMillis(Integer.parseInt(line.substring(3,5)));
        long beginSecondInMS = TimeUnit.SECONDS.toMillis(Integer.parseInt(line.substring(6,8)));
        long beginMilliSecond = Integer.parseInt(line.substring(9,12));
        long endHourInMS = TimeUnit.HOURS.toMillis(Integer.parseInt(line.substring(17,19)));
        long endMinuteInMS = TimeUnit.MINUTES.toMillis(Integer.parseInt(line.substring(20,22)));
        long endSecondInMS = TimeUnit.SECONDS.toMillis(Integer.parseInt(line.substring(23,25)));
        long endMilliSecond = Integer.parseInt(line.substring(26,29));

        this.beginTime = new Date();
        this.beginTime.setTime(beginHourInMS+beginMinuteInMS+beginSecondInMS+beginMilliSecond);
        this.endTime = new Date();
        this.endTime.setTime(endHourInMS+endMinuteInMS+endSecondInMS+endMilliSecond);
    }

    public String shiftLine(long milliSeconds) {
        shiftAttributes(milliSeconds);

        int beginMilliSecond = 0;
        int beginSecond = 0;
        int beginMinute = 0;
        int beginHour = 0;
        int endMilliSecond = 0;
        int endSecond = 0;
        int endMinute = 0;
        int endHour = 0;

        if (this.beginTime.getTime() >= 0) {
            beginMilliSecond = (int) this.beginTime.getTime() % 1000;
            beginSecond = (int) TimeUnit.MILLISECONDS.toSeconds(this.beginTime.getTime()) % 60;
            beginMinute = (int) TimeUnit.MILLISECONDS.toMinutes(this.beginTime.getTime()) % 60;
            beginHour = (int) TimeUnit.MILLISECONDS.toHours(this.beginTime.getTime()) % 99;
        }

        if (this.endTime.getTime() >= 0) {
            endMilliSecond = (int) this.endTime.getTime() % 1000;
            endSecond = (int) TimeUnit.MILLISECONDS.toSeconds(this.endTime.getTime()) % 60;
            endMinute = (int) TimeUnit.MILLISECONDS.toMinutes(this.endTime.getTime()) % 60;
            endHour = (int) TimeUnit.MILLISECONDS.toHours(this.endTime.getTime()) % 99;
        }

        String newBeginMilliSecond = String.format("%03d", beginMilliSecond);
        String newBeginSecond = String.format("%02d", beginSecond);
        String newBeginMinute = String.format("%02d", beginMinute);
        String newBeginHour = String.format("%02d", beginHour);
        String newEndMilliSecond = String.format("%03d", endMilliSecond);
        String newEndSecond = String.format("%02d", endSecond);
        String newEndMinute = String.format("%02d", endMinute);
        String newEndHour = String.format("%02d", endHour);

        return newBeginHour + ":" + newBeginMinute + ":" + newBeginSecond + "," + newBeginMilliSecond
                + " --> " + newEndHour + ":" + newEndMinute + ":" + newEndSecond + "," + newEndMilliSecond;
    }

    private void shiftAttributes(long milliSeconds) {
        this.beginTime = addMilliSecondsToDate(this.beginTime, milliSeconds);
        this.endTime = addMilliSecondsToDate(this.endTime, milliSeconds);
    }

    public Date addMilliSecondsToDate(Date date, long milliSeconds) {
        int milli = (int) milliSeconds;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, milli);
        return calendar.getTime();
    }

}
