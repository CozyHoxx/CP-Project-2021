public class Visitor {
    int groupNumber;
    String enterTime;
    String exitTime;

    int duration;
    int enterHour;
    int enterMinute;

    int exitHour;
    int exitMinute;
    int ticketNumber;

    public Visitor(String enterTime,  int groupNumber, int ticketNumber) {
        this.enterTime = enterTime;
        this.groupNumber = groupNumber;
        this.ticketNumber = ticketNumber;
        getEnterTimeInt();
    }

    private void calculateExitTime() {
        this.exitHour = duration / 60 + enterHour;
        this.exitMinute = duration % 60 + enterMinute;

        if (exitMinute > 60) {
            exitHour++;
            exitMinute -= 60;
        }
    }

    private void getEnterTimeInt() {
        String[] timeSplit = enterTime.split(":");
        this.enterHour = Integer.parseInt(timeSplit[0]);
        this.enterMinute = Integer.parseInt(timeSplit[1]);
    }

    public String getExitTime(){
        String str = this.pad(2, '0', Integer.toString(exitHour) + "");
        str+=":" + this.pad(2,'0',Integer.toString(exitMinute));
        return str;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void addDuration(int duration){
        this.duration = duration;
        calculateExitTime();
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