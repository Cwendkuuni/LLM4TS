import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDatasetTest {
    
    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));

        DefaultDataset dataset = new DefaultDataset(instances);

        assertEquals(2, dataset.size());
        assertEquals(3, dataset.maxAttributes());
        assertSame(Collections.singleton(new Double(0.0)), dataset.classes());

        Instance instance1 = dataset.instance(0);
        Instance instance2 = dataset.instance(1);

        assertEquals(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), instance1);
        assertEquals(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)), instance2);
    }
    
}