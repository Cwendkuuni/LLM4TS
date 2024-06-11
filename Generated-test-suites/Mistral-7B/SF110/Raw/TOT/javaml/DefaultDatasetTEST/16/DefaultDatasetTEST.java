import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.EuclideanDistance;

public class DefaultDatasetTest {
    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        // Given: A collection of instances with different classes and attributes
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 3.0);
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 4.0);
        ArrayList<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // When: Creating a DefaultDataset with the collection
        DefaultDataset dataset = new DefaultDataset(instances);

        // Then: The dataset has the correct size and attributes
        assertEquals(instances.size(), dataset.size());
        Instance instance = dataset.instance(0);
        assertEquals(instance.noAttributes(), dataset.noAttributes());

        // Testing the getClass() method
        Object expectedClass1 = instance1.classValue();
        Object expectedClass2 = instance2.classValue();
        SortedSet<Object> classes = dataset.classes();
        assertTrue(classes.contains(expectedClass1));
        assertTrue(classes.contains(expectedClass2));

        // Testing the add() method with an instance already in the dataset
        dataset.add(instance1);
        assertEquals(dataset.size(), 2);

        // Testing the addAll() method with an empty collection
        ArrayList<Instance> emptyInstances = new ArrayList<>();
        dataset.addAll(emptyInstances);
        assertEquals(dataset.size(), 2);

        // Testing the method that measures the distance between two instances
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        double distance = distanceMeasure.measure(instance1, instance2);
        assertTrue(distance > 0.0);
    }
}