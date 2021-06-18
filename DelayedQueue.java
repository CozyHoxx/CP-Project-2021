package cp.assignment.gui;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class DelayedQueue implements Delayed {
    private long duration;
    private int ticketValue;

    public DelayedQueue(long duration, int ticketValue) {
        this.duration = System.currentTimeMillis() + duration;
        this.ticketValue = ticketValue;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getTicketValue() {
        return ticketValue;
    }

    public void setTicketValue(int ticketValue) {
        this.ticketValue = ticketValue;
    }

    @Override
    public int compareTo(Delayed otherDelayed) {
        if (this.duration < ((DelayedQueue) otherDelayed).getDuration()) {
            return -1;
        }
        if (this.duration > ((DelayedQueue) otherDelayed).getDuration()) {
            return +1;
        }
        return 0; // equal
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(duration - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return Integer.toString(ticketValue);
    }

}
