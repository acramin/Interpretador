package compiler.scanner;

public class Id extends Token {

    public final String lexeme;

    public Id(String s, int line, int column) {
        super(Tag.ID, line, column);
        lexeme = s;
    }

    @Override
    public String toString() {
        return "<" + this.tag + ",\"" + this.lexeme + "\">";
    }

}
