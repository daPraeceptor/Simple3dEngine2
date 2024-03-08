import java.awt.*;
import java.util.Comparator;

public class ProjectedPolygon extends Polygon implements Comparator<ProjectedPolygon> {
    Color color;
    public double z;
    ProjectedPolygon (Color c) {
        super ();
        this.color = c;
        this.z = 0;
    }
    @Override
    public int compare(ProjectedPolygon arg0, ProjectedPolygon arg1) {
        return (int) ((arg1.z - arg0.z) * 100);
    }

}
