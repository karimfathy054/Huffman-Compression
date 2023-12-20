import java.io.Serializable;
import java.util.Objects;

public class Pair implements Serializable {
    long code;
    int len;

    public Pair(long code, int len) {
        this.code = code;
        this.len = len;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "code=" + code +
                ", len=" + len +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return code == pair.code && len == pair.len;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, len);
    }
}