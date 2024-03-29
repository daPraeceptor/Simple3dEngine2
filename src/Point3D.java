import java.util.Objects;

public class Point3D {
    public double x, y, z;

    public Point3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(Point3D p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point3D = (Point3D) o;
        return Double.compare(x, point3D.x) == 0 && Double.compare(y, point3D.y) == 0 && Double.compare(z, point3D.z) == 0;
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + z; //super.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public void setTo (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void setTo (Point3D p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public void add (Point3D p) {
        this.x += p.x;
        this.y += p.y;
        this.z += p.z;
    }
    public Point3D subtract (Point3D p) {
        return new Point3D (this.x - p.x, this.y - p.y, this.z - p.z);
    }

    public void add (double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }
    public Point3D subtract (double x, double y, double z) {
        return new Point3D (this.x - x, this.y - y, this.z - z);
    }

    public Point3D getScaled (double factor) {
        return new Point3D (this.x * factor, this.y * factor, this.z * factor);

    }

    public Point3D crossProduct (Point3D p) {
        return new Point3D (this.y * p.z - this.z * p.y,
                this.z * p.x - this.x * p.z,
                this.x * p.y - this.y * p.x);
    }

    public double dotProduct (Point3D p) {
        return (this.x * p.x) + (this.y * p.y) + (this.z * p.z);
    }
    public double length () {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z );
    }

    public double distanceTo (Point3D p) {
        // p - this
        Point3D d = new Point3D(p.x - this.x, p.y - this.y, p.z - this.z );
        return Math.sqrt(d.x * d.x + d.y * d.y + d.z * d.z );
    }
    public Point3D getRotated (Point3D rotation) {
        if (rotation == null)
            return null;
        return this.getRotated (rotation.x, rotation.y, rotation.z);
    }
    public Point3D getRotated (double x, double y, double z) {
        // rotate round y-axis
        Point3D rP = new Point3D(Math.cos (y) * this.x + Math.sin (y) * this.z,
                this.y,
                Math.cos (y) * this.z - Math.sin (y) * this.x);

        // rotate round x-axis
        rP = new Point3D(rP.x,
                        Math.cos (x) * rP.y - Math.sin (x) * rP.z,
                        Math.cos (x) * rP.z + Math.sin (x) * rP.y);


        // rotate round z-axis
        return new Point3D(Math.cos (z) * rP.x - Math.sin (z) * rP.y,
                Math.cos (z) * rP.y + Math.sin (z) * rP.x,
                    rP.z);

    }
}
