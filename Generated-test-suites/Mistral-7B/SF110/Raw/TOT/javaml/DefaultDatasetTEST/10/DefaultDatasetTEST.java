import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDatasetTest {
    // 

    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));

        DefaultDataset dataset = new DefaultDataset(instances);

        assertEquals(2, dataset.size());
        assertEquals(3, dataset.maxAttributes());
        assertTrue(dataset.classes().contains(0));
        assertTrue(dataset.classes().contains(1));

        Instance instance = dataset.instance(0);
        assertEquals(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0), instance);

        // Test with an empty collection
        instances.clear();
        dataset = new DefaultDataset(instances);
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes());
        assertNotNull(dataset.classes());
        assertTrue(dataset.classes().isEmpty());
    }

    // 
}