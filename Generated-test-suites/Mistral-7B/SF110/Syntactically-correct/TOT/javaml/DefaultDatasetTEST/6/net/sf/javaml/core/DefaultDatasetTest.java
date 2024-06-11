import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetEmptyConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
        assertTrue(dataset.classes().isEmpty());
        assertFalse(dataset.hasNoClassLabels());
    }

    @Test
    public void testDefaultDatasetCollectionConstructor() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        Collection<Instance> instances = new ArrayList<Instance>() {{
            add(instance1);
            add(instance2);
        }};
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(2, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(2, dataset.classes().size());
        assertTrue(dataset.hasNoClassLabels());
    }

    @Test
    public void testAddAllMethod() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        Collection<Instance> instances = new ArrayList<Instance>() {{
            add(instance1);
            add(instance2);
        }};
        dataset.addAll(instances);
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsAll(instances));
        assertEquals(2, dataset.classes().size());
        assertFalse(dataset.hasNoClassLabels());
    }

    @Test
    public void testClearMethod() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        dataset.addAll(Arrays.asList(instance1, instance2));
        dataset.clear();
        assertEquals(0, dataset.size());
        assertTrue(dataset.classes().isEmpty());
        assertFalse(dataset.hasNoClassLabels());
    }

    @Test
    public void testClassIndexMethod() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        dataset.addAll(Arrays.asList(instance1, instance2));
        assertEquals(1, dataset.classIndex(instance1.classValue()));
        assertEquals(2, dataset.classIndex(instance2.classValue()));
        assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testInstanceMethod() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        dataset.addAll(Arrays.asList(instance1, instance2));
        Instance retrievedInstance1 = dataset.instance(0);
        Instance retrievedInstance2 = dataset.instance(1);
        assertEquals(instance1, retrievedInstance1);
        assertEquals(instance2, retrievedInstance2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        dataset.add(instance);
        dataset.instance(-1);
    }

    @Test
    public void testKNearestNeighborsMethod() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 1.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{1.0, 3.0, 1.0}, new Double(1.0));
        Instance instance3 = new DenseInstance(new double[]{3.0, 2.0, 2.0}, new Double(2.0));
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        SortedSet<Object> neighbors = dataset.kNearest(2, instance1, distanceMeasure);
        assertTrue(neighbors.containsAll(Arrays.asList(instance1, instance2)));
        assertEquals(2, neighbors.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfNeighbors() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        dataset.kNearest(-1, instance, distanceMeasure);
    }

    @Test
    public void testCopyMethod() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 1.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{1.0, 3.0, 1.0}, new Double(1.0));
        dataset.addAll(Arrays.asList(instance1, instance2));
        DefaultDataset copiedDataset = dataset.copy();
        assertEquals(2, copiedDataset.size());
        assertTrue(copiedDataset.containsAll(dataset));
        assertEquals(copiedDataset.hashCode(), dataset.hashCode());
    }
}

