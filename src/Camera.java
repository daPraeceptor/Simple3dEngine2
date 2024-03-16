public class Camera extends Object3D {
    public double screenDistance = 400;
    public int screenWidth = 0; // in pixels
    public int screenHeight = 0;

    public Point3D maxRot = new Point3D(1.1, 0, 0);
    public Point3D minRot = new Point3D(-1.1, 0, 0);
    Camera (Point3D p, double screenDistance, int screenWidth, int screenHeight) {
        super (p);
        this.screenDistance = screenDistance;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
    @Override
    public void move () {
        if (dir.x < - Math.PI /4)
            dir.x = - Math.PI /4;
        else if (dir.x > Math.PI /4)
            dir.x = Math.PI /4;
        super.move ();

    }
}
