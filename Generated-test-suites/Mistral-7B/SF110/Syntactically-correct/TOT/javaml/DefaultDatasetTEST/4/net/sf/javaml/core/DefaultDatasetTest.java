import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        // Given: An empty DefaultDataset and a list of Instances
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));

        // When: Adding the instances to the dataset
        dataset.addAll(instances);

        // Then: The dataset should have the correct number of instances and attributes
        assertEquals(2, dataset.size());
        Instance instance1 = dataset.instance(0);
        Instance instance2 = dataset.instance(1);
        assertEquals(3, instance1.noAttributes());
        assertEquals(3, instance2.noAttributes());
    }

    @Test
    public void testConstructorWithCollection() {
        // Given: A list of Instances
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));

        // When: Creating a DefaultDataset from the list
        DefaultDataset dataset = new DefaultDataset(instances);

        // Then: The dataset should have the correct number of instances and attributes
        assertEquals(2, dataset.size());
        Instance instance1 = dataset.instance(0);
        Instance instance2 = dataset.instance(1);
        assertEquals(3, instance1.noAttributes());
        assertEquals(3, instance2.noAttributes());
    }

    // Add a test case for the noAttributes() method here

    // 
    // Add more test cases for other methods here
    // 
}