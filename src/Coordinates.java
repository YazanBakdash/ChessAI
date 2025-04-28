import java.util.Objects;

public class Coordinates {
    int x;
    int y;
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates coords = (Coordinates) o;
        return x == coords.x && y == coords.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
