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

    public boolean inView (Camera camera) {
        Point3D camCorditates = new Point3D(this.pos);
        camCorditates.subtract(camera.pos);
        Point3D camRotated = camCorditates.getRotated(camera.rot);

        if (camRotated.z < 1)
            return false;
//        if ((camRotated.x /* + objectMaxSize */ ) * camera.screenDistance / camRotated.z > camera.screenWidth / 2 )
//           return false;
//        if ((camRotated.y /* + objectMaxSize */ ) * camera.screenDistance / camRotated.z > camera.screenHeight / 2)
//            return false;
//        if ((camRotated.x /* + objectMaxSize */ ) * camera.screenDistance / camRotated.z < camera.screenWidth / 2)
//            return false;
//        if ((camRotated.y /* + objectMaxSize */ ) * camera.screenDistance / camRotated.z < camera.screenHeight / 2)
//            return false;

        return true;
    }

    public void move () {
        rot.add (rotadd);
        pos.add (dir);
    }

}
