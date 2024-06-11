import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import org.junit.Test;

public class DefaultDatasetTest {
    // 

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        Instance instance3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, new Double(3.0));

        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        assertEquals(3, dataset.size());
        for (Instance instance : dataset) {
            assertEquals(instance1, instance); // or instance2, instance3
            break;
        }
    }

    // 
}