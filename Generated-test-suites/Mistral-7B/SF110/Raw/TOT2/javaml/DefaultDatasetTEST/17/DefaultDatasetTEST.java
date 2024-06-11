import org.junit.Assert.*;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import java.util.ArrayList;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstruction() {
        // Test DefaultDataset(Collection<Instance> coll) constructor
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0)));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(1, dataset.size());
        assertEquals(3, dataset.noAttributes());
        
        // Test DefaultDataset() constructor
        DefaultDataset emptyDataset = new DefaultDataset();
        assertEquals(0, emptyDataset.size());
    }
}