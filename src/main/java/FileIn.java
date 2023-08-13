public class FileIn {
    private final String name;
    private boolean available;
    private int cursor;
    private String curLine;
    private String prevLine;

    public FileIn(String name) {
        this.name = name;
        this.available = true; //перед первой попыткой чтения надеемся на доступность файла
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getCursor() {
        return cursor;
    }

    public void incCursor() {
        this.cursor++;
    }

    public String getCurLine() {
        return curLine;
    }

    public void setCurLine(String curLine) {
        this.curLine = curLine;
    }

     public void setPrevLine(String prevLine) {
        this.prevLine = prevLine;
    }
}
