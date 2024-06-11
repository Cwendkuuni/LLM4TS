import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {
    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        // Given: A Collection of Instances
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0}));

        // When: A DefaultDataset is created using the given Collection
        DefaultDataset dataset = new DefaultDataset(instances);

        // Then: The dataset should have the correct size and attributes
        assertEquals(2, dataset.size());
        assertEquals(3, dataset.noAttributes());

        // And: The dataset should contain the instances from the Collection
        Instance instance1 = dataset.instance(0);
        Instance instance2 = dataset.instance(1);
        assertEquals(instance1.getClass(), DenseInstance.class);
        assertEquals(instance1.noAttributes(), 3);
        assertEquals(instance1.classValue(), 0.0);
        assertEquals(instance2.getClass(), DenseInstance.class);
        assertEquals(instance2.noAttributes(), 3);
        assertEquals(instance2.classValue(), 1.0);
    }
}