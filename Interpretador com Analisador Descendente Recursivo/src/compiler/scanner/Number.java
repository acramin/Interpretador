package compiler.scanner;

public class Number extends Token {

    public final double value;

    public Number(double v, int line, int column) {
        super(Tag.NUMBER, line, column);
        value = v;
    }

    @Override
    public String toString() {
        return "<" + this.tag + "," + this.value + ">";
    }
}
