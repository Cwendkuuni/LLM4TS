import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstructionAndAddInstances() {
        // Test default constructor
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());

        // Test constructor with collection
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
        Instance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new Object[]{2.0});
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        dataset = new DefaultDataset(instances);
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));

        // Test adding an instance
        Instance newInstance = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new Object[]{3.0});
        dataset.add(newInstance);
        assertEquals(3, dataset.size());
        assertEquals(newInstance, dataset.get(2));
    }
}