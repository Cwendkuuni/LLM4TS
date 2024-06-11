import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetWithCollection() {
        // Given
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 0.0));

        // When
        DefaultDataset dataset = new DefaultDataset(instances);

        // Then
        assertEquals(2, dataset.size());
        assertTrue(dataset.classes().contains(1.0));
        assertTrue(dataset.classes().contains(0.0));
    }

    // ... other test cases
}