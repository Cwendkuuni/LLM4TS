import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.sf.javaml.core.*;
import org.junit.Test;

public class FoldTest {

    @Test
    public void testConstructor() {
        Dataset parentDataSet = new DefaultDataset();
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(parentDataSet, indices);
        assertNotNull(fold);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyIndices() {
        Dataset parentDataSet = new DefaultDataset();
        int[] indices = new int[0];
        Fold fold = new Fold(parentDataSet, indices);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParent() {
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(null, indices);
    }

    @Test
    public void testClasses() {
        Dataset parentDataSet = new DefaultDataset();
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(parentDataSet, indices);
        assertEquals(parentDataSet.classes(), fold.classes());
    }

    @Test
    public void testInstance() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.0}));
        parentDataSet.add(new DenseInstance(new double[]{2.0, 2.0, 2.0}, new double[]{2.0, 2.0}));
        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parentDataSet, indices);
        Instance expectedInstance = parentDataSet.instance(0);
        Instance actualInstance = fold.instance(0);
        assertEquals(expectedInstance, actualInstance);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInstanceWithOutOfBoundsIndex() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.0}));
        parentDataSet.add(new DenseInstance(new double[]{2.0, 2.0, 2.0}, new double[]{2.0, 2.0}));
        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parentDataSet, indices);
        fold.instance(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset parentDataSet = new DefaultDataset();
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(parentDataSet, indices);
        fold.folds(3, new Random());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldsEmptyDataSet() {
        Dataset parentDataSet = new DefaultDataset();
        Fold fold = new Fold(parentDataSet, new int[0]);
        fold.folds(3, new Random());
    }
}