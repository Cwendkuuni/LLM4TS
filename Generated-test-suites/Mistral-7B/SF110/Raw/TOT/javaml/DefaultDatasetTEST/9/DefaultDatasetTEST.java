import static org.junit.Assert.*;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.InstanceFactory;
import net.sf.javaml.distance.EuclideanDistance;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        // Create an empty DefaultDataset
        DefaultDataset dataset = new DefaultDataset();

        // Create instances with different classes and attributes
        InstanceFactory instanceFactory = new InstanceFactory();
        Instance instance1 = instanceFactory.createInstance(new double[]{1.0, 2.0}, 2.0);
        Instance instance2 = instanceFactory.createInstance(new double[]{3.0, 4.0}, 2.0);
        Instance instance3 = instanceFactory.createInstance(new double[]{5.0, 6.0}, 3.0);

        // Add instances with the same class to the dataset
        Instance class1Instance = instance1;
        dataset.addAll(Arrays.asList(instance1, instance2));
        assertEquals(2, dataset.size());
        assertEquals(class1Instance.classValue(), dataset.instance(0).classValue());
        assertEquals(class1Instance.classValue(), dataset.instance(1).classValue());

        // Add an instance with a different class
        Instance class2Instance = instance3;
        dataset.addAll(Arrays.asList(instance3));
        assertEquals(3, dataset.size());
        assertNotEquals(class1Instance.classValue(), dataset.instance(2).classValue());
        assertEquals(class2Instance.classValue(), dataset.instance(2).classValue());

        // Check that maxAttributes is updated
        assertEquals(3, dataset.maxAttributes());
    }
}