import static org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;

public class FoldTest {
    @Test
    public void testInstanceMethod() {
        Dataset parentData = new DefaultDataset();
        Instance expectedInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        parentData.add(expectedInstance);
        int index = 0;

        Fold fold = new Fold(parentData, new int[]{0, 1, 2});
        Instance actualInstance = fold.instance(index);

        assertEquals(expectedInstance, actualInstance);
    }
}