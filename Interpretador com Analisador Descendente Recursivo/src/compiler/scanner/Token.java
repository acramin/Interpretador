package compiler.scanner;

public class Token {

    public final Tag tag;
    public final int line;
    public final int column;

    public Token(Tag tag, int line, int column) {
        this.tag = tag;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return "<" + tag + ">";
    }
}
