public class Clock {
    private int HOUR = 8; // Starts clock at 8am
    private int MINUTE = 0;

    public Clock() {
    }
    public int getHOUR() {
        return HOUR;
    }

    public int getMINUTE() {
        return MINUTE;
    }

    public void updateClock() {
        MINUTE++;
        if (MINUTE == 60) {
            HOUR++;
            MINUTE = 0;
        }
    }

    public String getTime(){
        String str = this.pad(2, '0', Integer.toString(HOUR) + "");
        str+=":" + this.pad(2,'0',Integer.toString(MINUTE));
        return str;
    }

    public String pad(int fieldWidth, char padChar, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < fieldWidth; i++) {
            sb.append(padChar);
        }
        sb.append(s);
        return sb.toString();
    }
}