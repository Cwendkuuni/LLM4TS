import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testEmptyConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
        assertNull(dataset.classes());
    }

    @Test
    public void testCollectionConstructor() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1, 2, 3}, new Double(1.0)));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(1, dataset.size());
        assertEquals(new Double(1.0), dataset.classes().first());
    }

    @Test
    public void testAddAll() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1, 2, 3}, new Double(1.0)));
        instances.add(new DenseInstance(new double[]{4, 5, 6}, new Double(2.0)));
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(instances);
        assertEquals(2, dataset.size());
        assertTrue(dataset.classes().containsAll(Arrays.asList(new Double(1.0), new Double(2.0))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullInstance() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(null);
    }

    @Test
    public void testRemoveFarthest() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1, 2, 3}, new Double(1.0)));
        instances.add(new DenseInstance(new double[]{4, 5, 6}, new Double(2.0)));
        DefaultDataset dataset = new DefaultDataset(instances);
        Map<Instance, Double> closest = new HashMap<>();
        closest.put(instances.get(0), 3.0);
        closest.put(instances.get(1), 2.0);
        assertEquals(1.0, dataset.removeFarthest(closest), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFoldsWithNegativeNumFolds() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.folds(-1, new Random());
    }

    @Test
    public void testFolds() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1, 2, 3}, new Double(1.0)));
        instances.add(new DenseInstance(new double[]{4, 5, 6}, new Double(2.0)));
        instances.add(new DenseInstance(new double[]{7, 8, 9}, new Double(3.0)));
        DefaultDataset dataset = new DefaultDataset(instances);
        Dataset[] folds = dataset.folds(3, new Random());
        assertEquals(3, folds.length);
        for (Dataset fold : folds) {
            assertTrue(fold instanceof Fold);
        }
    }

    @Test
    public void testCopy() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1, 2, 3}, new Double(1.0)));
        instances.add(new DenseInstance(new double[]{4, 5, 6}, new Double(2.0)));
        DefaultDataset dataset = new DefaultDataset(instances);
        DefaultDataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        for (int i = 0; i < dataset.size(); i++) {
            Instance instance1 = dataset.instance(i);
            Instance instance2 = copy.instance(i);
            assertEquals(instance1.noAttributes(), instance2.noAttributes());
            assertArrayEquals(instance1.toArray(), instance2.toArray());
            assertEquals(instance1.classValue(), instance2.classValue());
        }
    }
}