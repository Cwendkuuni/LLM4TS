import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetEmptyConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testDefaultDatasetCollectionConstructor() {
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance);
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testCheckInstanceNullClassValue() {
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, null);
        DefaultDataset dataset = new DefaultDataset();
        dataset.check(instance);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testCheckInstanceNoAttributesGreaterThanMax() {
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, new Object[]{1.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.check(instance);
        assertEquals(5, dataset.maxAttributes);
    }

    @Test
    public void testAddAllWithNullCollection() {
        DefaultDataset dataset = new DefaultDataset();
        assertFalse(dataset.addAll(null));
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddAllWithEmptyCollection() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.addAll(Collections.emptyList()));
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddAllWithInstanceCollection() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
        Instance instance2 = new DenseInstance(new double[]{3.0, 2.0, 1.0}, new Object[]{2.0});
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2)));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testClearEmptyDataset() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testClearNonEmptyDataset() {
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddingInstancesWithSameClassValue() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
        Instance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(2, dataset.size());
        assertEquals(1.0, dataset.classes.first());
    }

    @Test
    public void testAddingInstancesWithDifferentClassValues() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
        Instance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{2.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(2, dataset.size());
        assertEquals(2, dataset.classes.size());
    }

    @Test
    public void testAddingInstanceWithDifferentNumberOfAttributes() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
        Instance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, new Object[]{1.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(2, dataset.size());
        assertEquals(5, dataset.maxAttributes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullInstance() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingInstanceWithNullAttributes() {
        Instance instance = new DenseInstance(null, new Object[]{1.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
    }
}