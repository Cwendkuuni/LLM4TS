import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import org.junit.*;
import org.junit.Assert.*;
import org.junit.Test;

public class FoldTest {

    private Dataset dataset;

    @BeforeClass
    public static void setup() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testFoldConstructor() {
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(dataset, indices);
        Assert.assertEquals(dataset, fold.parent);
        Assert.assertArrayEquals(indices, fold.indices);
    }

    @Test
    public void testSize() {
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(dataset, indices);
        Assert.assertEquals(3, fold.size());
    }

    @Test
    public void testInstance() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance(new double[]{1.0, 2.0}));
        parent.add(new Instance(new double[]{2.0, 3.0}));
        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parent, indices);
        Instance instance = fold.instance(0);
        Assert.assertEquals(parent.instance(0), instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Fold fold = new Fold(dataset, new int[]{});
        fold.add(new Instance(new double[]{1.0, 2.0}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstances() {
        Fold fold = new Fold(dataset, new int[]{});
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance(new double[]{1.0, 2.0}));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(dataset, indices);
        fold.remove(dataset.instance(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(dataset, indices);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(dataset, indices);
        List<Instance> instances = new ArrayList<>();
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(dataset, indices);
        List<Instance> instances = new ArrayList<>();
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(dataset, indices);
        Instance instance = new Instance(new double[]{1.0, 2.0});
        fold.set(0, instance);
    }

    @Test
    public void testEmptyDataset() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{});
        Assert.assertFalse(fold.isEmpty());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutOfBoundsIndex() {
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(dataset, indices);
        fold.instance(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDuplicateIndices() {
        int[] indices = new int[]{0, 0, 2};
        Fold fold = new Fold(dataset, indices);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDataset() {
        Fold fold = new Fold(null, new int[]{});
    }
}