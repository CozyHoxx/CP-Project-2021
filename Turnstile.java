package cp.assignment.gui;

public class Turnstile {
    boolean available;

    public Turnstile() {
        available = true;
    }

    public void open() {
        this.available = true;
    }

    public void close() {
        this.available = false;
    }

    public boolean getState() {
        return this.available;
    }
}
