import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {
    // 

    @Test
    public void testDefaultDatasetWithValidCollection() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(1, dataset.size());
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultDatasetWithNullCollection() {
        DefaultDataset dataset = new DefaultDataset(null);
    }

    // 

    // ... other test cases for other methods
}