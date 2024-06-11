import static org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;

public class FoldTest {
    
    @Test
    public void testInstance() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DefaultInstance(new double[]{1.0, 2.0, 3.0}, 1);
        parentDataset.add(instance1);
        int[] indices = {0};
        Fold fold = new Fold(parentDataset, indices);

        Instance expectedInstance = instance1;
        Instance actualInstance = fold.instance(0);

        assertEquals(expectedInstance, actualInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInstanceWithNegativeIndex() {
        Dataset parentDataset = new DefaultDataset();
        Fold fold = new Fold(parentDataset, new int[0]);

        try {
            fold.instance(-1);
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInstanceWithIndexOutOfBounds() {
        Dataset parentDataset = new DefaultDataset();
        Fold fold = new Fold(parentDataset, new int[]{0, 1});

        try {
            fold.instance(2);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 2, Size: 2", e.getMessage());
        }
    }
    
}