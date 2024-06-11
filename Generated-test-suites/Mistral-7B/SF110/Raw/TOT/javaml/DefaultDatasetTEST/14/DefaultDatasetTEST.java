import org.junit.Test;
import static org.junit.Assert.*; // for assertions
import net.sf.javaml.core.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        // Testing the constructor that accepts a Collection<Instance>
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 1.0));
        instances.add(new DenseInstance(new double[]{2.0, 2.0}, 2.0));
        DefaultDataset dataset = new DefaultDataset(instances);

        assertEquals(2, dataset.size());
        assertEquals(2, dataset.maxAttributes());
        assertSame(1.0, dataset.classValue(0));
        assertSame(2.0, dataset.classValue(1));
    }

    @Test
    public void testDefaultDatasetConstructorEmpty() {
        // Testing the constructor that accepts no arguments
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes());
        assertNull(dataset.classes().size());
    }

    // Add other test methods here...

    // ... or in separate test methods files, if needed
}