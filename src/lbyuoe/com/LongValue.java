package lbyuoe.com;

public class LongValue {
    public long value;

    public LongValue(long i) {
        value = i;
    }

    public long minus(LongValue a) {
        return (this.value - a.value);
    }
}
