package draw_it.data.message;

import java.util.List;

public class DrawMessage extends Message {

    private List<Point> points;

    public DrawMessage() {
        super();
    }

    public DrawMessage(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DrawMessage other = (DrawMessage) obj;
        if (points == null) {
            if (other.points != null)
                return false;
        } else if (!points.equals(other.points))
            return false;

        return true;
    }

}
