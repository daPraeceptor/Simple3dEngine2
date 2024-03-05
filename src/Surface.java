import java.util.ArrayList;

public class Surface {
    public ArrayList<Integer> pointIndex = new ArrayList<Integer>();

    Surface (int[] pIndexses) {
        for (int i = 0; i < pIndexses.length; i ++) {
            pointIndex.add (pIndexses[i]);
        }
    }
    public int getPointIndex (int i) {
        return pointIndex.get (i);
    }
    public int nrOfPoints () {
        return pointIndex.size ();
    }
}
