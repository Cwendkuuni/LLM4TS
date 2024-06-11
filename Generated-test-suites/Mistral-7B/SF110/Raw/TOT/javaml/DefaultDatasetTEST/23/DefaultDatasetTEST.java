import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.*;
import static net.sf.javaml.distance.EuclideanDistance.*;

public class DefaultDatasetTest {
    @Test
    public void testAddAll() {
        // Test with empty collections
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> coll1 = new ArrayList<>();
        Collection<Instance> coll2 = new ArrayList<>();
        dataset.addAll(coll1);
        dataset.addAll(coll2);
        Assert.assertEquals(0, dataset.size());
        
        // Test with non-empty collections
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1.0);
        coll1.add(inst1);
        coll2.add(inst2);
        dataset.addAll(coll1);
        dataset.addAll(coll2);
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(inst1, dataset.instance(0));
        Assert.assertEquals(inst2, dataset.instance(1));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        dataset.add(inst1);
        dataset.clear();
        Assert.assertEquals(0, dataset.size());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        dataset.add(inst1);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1.0);
        dataset.add(inst2);
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(inst1, dataset.instance(0));
        Assert.assertEquals(inst2, dataset.instance(1));
    }

    @Test
    public void testAddAllWithNullInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> coll = new ArrayList<>();
        Instance nullInstance = null;
        coll.add(nullInstance);
        try {
            dataset.addAll(coll);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1.0);
        Instance inst3 = new DenseInstance(new double[]{2.5, 2.5, 2.5}, 0.0);
        dataset.add(inst1);
        dataset.add(inst2);
        dataset.add(inst3);

        Instance testInstance = new DenseInstance(new double[]{3.0, 3.0, 3.0}, 0.0);
        SortedSet<Object> expected = new TreeSet<>();
        expected.add(inst1);
        expected.add(inst3);
        SortedSet<Object> actual = dataset.kNearest(2, testInstance, this);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1.0);
        Instance inst3 = new DenseInstance(new double[]{2.5, 2.5, 2.5}, 0.0);
        dataset.add(inst1);
        dataset.add(inst2);
        dataset.add(inst3);

        Dataset[] actual = dataset.folds(3, new Random());
        Assert.assertEquals(3, actual.length);
        for (Dataset ds : actual) {
            Assert.assertTrue(ds instanceof Fold);
        }
    }
}