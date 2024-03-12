import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Main {

    private static final boolean drawObjectPoints = false;
    private static final boolean drawObjectSurfaces = true;

    public static ArrayList<Object3D> objects = new ArrayList<>();
    public static ArrayList<ProjectedPolygon> polygons = new ArrayList<>() ;
    public static Camera camera = null;

    public static Object3D light = new Object3D(new Point3D(-600, -1200, - 400));

    private static final double rotation_speed = Math.PI / 64;

    public static void main(String[] args) {

        // Create objects
        for (int i = 0; i < 24; i++) {
            objects.add(new Object3D(new Point3D(20. * (int) (i / 4), 0, 10 + 20 * (i % 4))));
            objects.get(i).makeThisRectangle(new Point3D(1, 1, 1));
            if (i == 0) {
                objects.get(i).color = new Color(40, 40, 240);
                //objects.get(i).rotadd = new Point3D (0.1, 0.0, 0.0);
            } else {
                objects.get(i).color = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
                objects.get(i).rotadd = new Point3D(0.1 * (i % 3), 0.1 * ((i + 1) % 3), 0.1 * ((i + 2) % 3));
            }
        }


        JFrame f = new JFrame("Simple 3D Engine Demo") {
            public void paint(Graphics g) {
                super.paint(g);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
                g.setColor(new Color(60, 220, 80));
                g.fillRect(0, (int) (-(getHeight() / 2) * camera.rot.x /*+ Math.PI / 2*/) + getHeight() / 2, getWidth(), getHeight());

                //camera.get
                for (Object3D o : objects) {
                    Point3D op = new Point3D(o.pos);

                    // subtract camera placement and rotation
                    op.subtract(camera.pos);
                    op = op.getRotated(camera.rot.x, camera.rot.y, camera.rot.z);

                    if (op.z < 0) {
                        continue;
                    }

                    o.rotatedPointList.clear();
                    for (Point3D p : o.pointList) {
                        Point3D rotated_cam = new Point3D(1, 1, 1).getRotated(camera.rot.x, camera.rot.y, camera.rot.z);
                        Point3D newPoint = p.getRotated(o.rot.x + rotated_cam.x, o.rot.y + rotated_cam.y, o.rot.z + rotated_cam.z);
                        newPoint.add(op);
                        o.rotatedPointList.add(newPoint);
                    }

                    if (drawObjectPoints) {
                        for (Point3D p : o.rotatedPointList) {
                            int size = (int) (1 * camera.screenDistance / (p.z));
                            int xx = (int) ((p.x) * camera.screenDistance / (p.z)) - size / 2 + getWidth() / 2;
                            int yy = (int) ((p.y) * camera.screenDistance / (p.z)) - size / 2 + getHeight() / 2;

                            //System.out.println ("Point " + i++ + ": " + p + " -> " + xx + ", " + yy);

                            if (xx + size < 0)
                                continue;
                            if (yy + size < 0)
                                continue;
                            if (xx - size >= getWidth())
                                continue;
                            if (yy - size >= getHeight())
                                continue;

                            g.setColor(o.color);
                            g.fillOval(xx, yy, size, size);
                        }
                    }
                    //System.out.println ("Object Pos: " + o.pos + " Cam:" + camera.pos);
                    if (drawObjectSurfaces) {
                        for (ProjectedPolygon polygon : polygons) {
                            g.setColor(polygon.color);
                            g.fillPolygon(polygon);
                        }
                    }
                }
            }
        };
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setBackground(new Color(120, 160, 220)); // Sky color
        f.add(panel);
        f.pack();
        f.setLayout(null);
        camera = new Camera(new Point3D(0, 0, 0), 200, f.getWidth(), f.getHeight());
        f.setVisible(true);
        f.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int deltaX = e.getX() - f.getWidth() / 2;
                int deltaY = e.getY() - f.getHeight() / 2;

                camera.rot.y -= (double) deltaX * 2. / f.getWidth();
                camera.rot.x += (double) deltaY * 2. / f.getHeight();

                Robot r;
                try {
                    r = new Robot();
                    r.mouseMove(f.getWidth() / 2, f.getHeight() / 2);
                } catch (AWTException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        f.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println (e.getKeyCode ());
                switch (e.getKeyCode()) {
                    case 37: // left arrow
                        camera.rot.y += rotation_speed;
                        break;
                    case 38: // down arrow
                        camera.dir.setTo(new Point3D(0, 0, 1).getRotated(camera.rot.x, -camera.rot.y, camera.rot.z));
                        break;
                    case 39: // right arrow
                        camera.rot.y -= rotation_speed;
                        break;
                    case 40: // up arrow
                        camera.dir.setTo(new Point3D(0, 0, -1).getRotated(camera.rot.x, -camera.rot.y, camera.rot.z));
                        //camera.dir.setTo (0, 0, -1);
                        break;

                    case KeyEvent.VK_W:
                        camera.dir.setTo(new Point3D(0, 0, 1).getRotated(-camera.rot.x, -camera.rot.y, -camera.rot.z));
                        break;
                    case KeyEvent.VK_S:
                        camera.dir.setTo(new Point3D(0, 0, -1).getRotated(-camera.rot.x, -camera.rot.y, -camera.rot.z));
                        break;
                    case KeyEvent.VK_A:
                        camera.dir.setTo(new Point3D(-1, 0, 0).getRotated(-camera.rot.x, -camera.rot.y, -camera.rot.z));
                        break;
                    case KeyEvent.VK_D:
                        camera.dir.setTo(new Point3D(1, 0, 0).getRotated(-camera.rot.x, -camera.rot.y, -camera.rot.z));
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                        camera.dir.setTo(0, 0, 0);
                        break;
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_D:
                        camera.dir.setTo(new Point3D(0, 0, 0));
                        break;
                }
            }
        });

        Timer timer = new Timer(20, (ActionEvent e) ->  {
                // Apply movement
                camera.move();
                for (Object3D o : objects) {
                    o.move();
                }

                // Determine objects to draw
                ArrayList<Object3D> objectsToView = new ArrayList<>();
                for (Object3D o : objects) {
                    if (o.inView(camera)) {
                        objectsToView.add(o);
                    }
                }
                // Create surfaces
                if (drawObjectSurfaces) {
                    polygons.clear();
                    for (Object3D o : objectsToView) {
                        for (Surface s : o.surfaces) {
                            // Calculate the crossProduct of surface
                            if (s.nrOfPoints() < 3) {
                                System.err.println("Not a surface!");
                                break; // Not a surface
                            }
                            Point3D p0 = o.rotatedPointList.get(s.getPointIndex(0));
                            Point3D p1 = new Point3D(o.rotatedPointList.get(s.getPointIndex(1)));
                            Point3D p2 = new Point3D(o.rotatedPointList.get(s.getPointIndex(2)));

                            p1.subtract(p0);
                            p2.subtract(p0);

                            Point3D crossProduct = p1.crossProduct(p2);
                            //System.out.println ("Cross: " + crossProduct + " p0:" + p0);

                            // Calculate angle to viewe
                            Point3D camPos = new Point3D();
                            camPos.subtract(p0);
                            double dotProduct = crossProduct.dotProduct(camPos);
                            double angle = Math.asin(dotProduct / (camPos.length() * crossProduct.length()));
                            //System.out.println(" |camPos| = " + camPos.length() + " |cross| = " + crossProduct.length());
                            //System.out.println("Angle = " + angle + " |cross| = " + crossProduct.length());

                            // Light angle
                            double dotProdLight = crossProduct.dotProduct(light.pos);
                            double light_angle = Math.asin(dotProdLight / (light.pos.length() * crossProduct.length()));
                            double lumination = light_angle / (Math.PI ) + 0.5;

                            //System.out.println ("Lumination: " + lumination + " Angle:" + angle);
                            if (angle > 0) {
                                ProjectedPolygon polygon = new ProjectedPolygon(new Color (
                                        (int) (o.color.getRed() * lumination),
                                        (int) (o.color.getGreen() * lumination),
                                        (int) (o.color.getBlue() * lumination )));
                                double avrage_z = 0.;
                                for (int i = 0; i < s.nrOfPoints(); i++) {
                                    Point3D p = o.rotatedPointList.get(s.getPointIndex(i));
                                    //if (p.z <= 0)
                                    //    continue;
                                    int xx = (int) ((p.x * camera.screenDistance) / (p.z)) + camera.screenWidth / 2;
                                    int yy = (int) ((p.y * camera.screenDistance) / (p.z)) + camera.screenHeight / 2;
                                    polygon.addPoint(xx, yy);
                                    avrage_z += p.z;
                                    //System.out.println ("Point " + s.getPoint(i) + ": " + p + " -> " + xx + ", " + yy);
                                }
                                // z sort value
                                polygon.z = avrage_z / s.nrOfPoints();
                                //
                                if (polygon.z <= 0)
                                    continue;
                                polygons.add (polygon);
                            }
                        } // surface
                    } // Objects
                    // z-sort
                    polygons.sort((ProjectedPolygon p1, ProjectedPolygon p2)-> (int) ((p2.z - p1.z)*100));
                } // if (drawSurfaces)
                // Paint surfaces
                f.repaint();

        });
        timer.start();

    }
}
