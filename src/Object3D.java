import java.awt.*;
import java.util.ArrayList;

public class Object3D {
    // Core attributes
    public Point3D pos;
    public Point3D rot;

    // Movement
    public Point3D dir;
    public Point3D rotadd;

    // Points and surfaces
    public ArrayList <Point3D> pointList = new ArrayList<Point3D>();
    public ArrayList <Point3D> rotatedPointList = new ArrayList<Point3D>();
    public ArrayList <Surface> surfaces = new ArrayList<Surface>();

    // appearance
    public Color color = new Color(100, 100, 200);

    public Object3D() {
        this.pos = new Point3D(0, 0, 0);
        this.rot = new Point3D(0, 0, 0);
        this.dir = new Point3D(0, 0, 0);
        this.rotadd = new Point3D(0, 0, 0);
    }

    public Object3D(Point3D pos) {
        this.pos = pos;
        this.rot = new Point3D(0, 0, 0);
        this.dir = new Point3D(0, 0, 0);
        this.rotadd = new Point3D(0, 0, 0);
    }

    public void makeThisRectangle (Point3D size) {
        for (int i = 0; i < 8; i ++) {
            pointList.add(new Point3D(
                    ((i % 2) * -1 +       ((i + 1) % 2)) * size.x,
                    (((i / 2) % 2) * -1 + (((i / 2) + 1) % 2)) * size.y,
                    (((i / 4) % 2) * -1 + (((i / 4) + 1) % 2)) * size.z
            ));
            //System.out.println(pointList.get(i));
        }
        surfaces.add (new Surface(new int[] {0, 1, 3, 2})); // Frontside
        surfaces.add (new Surface(new int[] {0, 4, 5, 1})); // Top
        surfaces.add (new Surface(new int[] {1, 5, 7, 3})); // right
        surfaces.add (new Surface(new int[] {5, 4, 6, 7})); // Backside
        surfaces.add (new Surface(new int[] {2, 3, 7, 6})); // Bottom
        surfaces.add (new Surface(new int[] {0, 2, 6, 4})); // left
    }
    public void makeThisTetrahedron (Point3D size) {
        pointList.add(new Point3D(0  * size.x,
                                    size.y,
                                    0 * size.z));
        pointList.add(new Point3D(
                Math.cos (Math.PI * 2 / 3) * size.x,
                Math.cos (Math.PI * 2 / 3) * size.y,
                Math.cos (Math.PI * 2 / 3) * size.z
        ));
        pointList.add(new Point3D(
                Math.cos (Math.PI * 2 / 3) * -size.x,
                Math.cos (Math.PI * 2 / 3) * size.y,
                Math.cos (Math.PI * 2 / 3) * size.z
        ));
        pointList.add(new Point3D(
                0  * size.x,
                Math.cos (Math.PI * 2 / 3) * size.y,
                Math.cos (Math.PI * 2 / 3) * -size.z
        ));

        surfaces.add (new Surface(new int[] {0, 2, 1})); // Frontside
        surfaces.add (new Surface(new int[] {0, 3, 2})); // Top
        surfaces.add (new Surface(new int[] {0, 1, 3})); // right
        surfaces.add (new Surface(new int[] {1, 2, 3})); // Backside
    }

    public boolean inView (Camera camera) {
        final double  maxObjectSize = 2; // TODO: set this to actual object max distans from center to point most far

        Point3D camCorditates = new Point3D(this.pos).subtract(camera.pos);
        Point3D camRotated = camCorditates.getRotated(new Point3D(camera.rot).getScaled(-1));
        double ratioX = (camera.screenWidth + maxObjectSize)/ 2 / camera.screenDistance;
        double ratioY = (camera.screenHeight + maxObjectSize) / 2 / camera.screenDistance;
        if (camRotated.z < 0.)
            return false;
        if ((camRotated.x ) / camRotated.z  > ratioX )
            return false;
        if ((camRotated.x ) / camRotated.z  < -ratioX )
            return false;
        if ((camRotated.y ) / camRotated.z  > ratioY )
            return false;
        if ((camRotated.y ) / camRotated.z < -ratioY )
            return false;

        return true;
    }

    public void move () {
        rot.add (rotadd);
        pos.add (dir);
    }

}
