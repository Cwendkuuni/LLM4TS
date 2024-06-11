
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {
    @Test
    public void testAddAll() {
        // Given: An empty DefaultDataset and a Collection of Instances
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0));

        // When: Adding the Collection to the DefaultDataset
        dataset.addAll(instances);

        // Then: The DefaultDataset should have the correct size and instances
        assertEquals(instances.size(), dataset.size());
        for (int i = 0; i < instances.size(); i++) {
            Instance instance = instances.get(i);
            Instance datasetInstance = dataset.instance(i);
            assertEquals(instance, datasetInstance);
        }
    }

    @Test
    public void testAdd() {
        // Given: An empty DefaultDataset
        DefaultDataset dataset = new DefaultDataset();

        // Given: A new Instance
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);

        // When: Adding the Instance to the DefaultDataset
        dataset.add(instance);

        // Then: The DefaultDataset should have the correct size and instance
        assertEquals(1, dataset.size());
        Instance datasetInstance = dataset.instance(0);
        assertEquals(instance, datasetInstance);
    }

    @Test
    public void testClear() {
        // Given: A DefaultDataset with instances
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0));

        // When: Clearing the DefaultDataset
        dataset.clear();

        // Then: The DefaultDataset should be empty
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testFolds() {
        // Given: A DefaultDataset with instances
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0));
        dataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, 3.0));

        // When: Folding the DefaultDataset with 3 folds
        Dataset[] folds = dataset.folds(3, new Random());

        // Then: Each fold should have the correct size and instances
        for (Dataset fold : folds) {
            assertEquals(dataset.size() / 3, fold.size());
            for (int i = 0; i < fold.size(); i++) {
                Instance instance = fold.instance(i);
                assertTrue(dataset.contains(instance));
            }
        }
    }

    @Test
    public void testKNearest() {
        // Given: A DefaultDataset with instances
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0));
        dataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, 3.0));
        dataset.add(new DenseInstance(new double[]{10.0, 11.0, 12.0}, 1.0));
        dataset.add(new DenseInstance(new double[]{13.0, 14.0, 15.0}, 2.0));
        dataset.add(new DenseInstance(new double[]{16.0, 17.0, 18.0}, 3.0));

        // Given: A new Instance and a DistanceMeasure
        Instance testInstance = new DenseInstance(new double[]{5.0, 5.0, 5.0}, null);
        DistanceMeasure dm = new EuclideanDistance();

        // When: Finding the k nearest neighbors
        Set<Instance> kNearest = dataset.kNearest(3, testInstance, dm);

        // Then: The set should contain the correct neighbors
        assertEquals(3, kNearest.size());
        assertTrue(kNearest.contains(dataset.instance(0)));
        assertTrue(kNearest.contains(dataset.instance(1)));
        assertTrue(kNearest.contains(dataset.instance(2)));
    }

    @Test
    public void testNoAttributes() {
        // Given: A DefaultDataset with instances
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0));

        // When: Getting the number of attributes
        int noAttributes = dataset.noAttributes();

        // Then: The number of attributes should be the number of attributes of the first instance
        assertEquals(dataset.instance(0).noAttributes(), noAttributes);
    }

    @Test
    public void testClassIndex() {
        // Given: A DefaultDataset with instances
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0));

        // Given: A class value
        Object clazz = 1.0;

        // When: Getting the class index
        int classIndex = dataset.classIndex(clazz);

        // Then: The class index should be the position of the first instance with that class value
        assertEquals(0, classIndex);
    }

    @Test
    public void testClassValue() {
        // Given: A DefaultDataset with instances
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0));

        // Given: An index
        int index = 0;

        // When: Getting the class value
        Object classValue = dataset.classValue(index);

        // Then: The class value should be the class value of the instance at that index
        assertEquals(1.0, classValue);
    }

    @Test
    public void testCopy() {
        // Given: A DefaultDataset with instances
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0));

        // When: Making a copy of the DefaultDataset
        Dataset copy = dataset.copy();

        // Then: The copy should have the same instances and size as the original DefaultDataset
        assertEquals(dataset.size(), copy.size());
        for (int i = 0; i < dataset.size(); i++) {
            Instance instance = dataset.instance(i);
            Instance copyInstance = copy.instance(i);
            assertEquals(instance, copyInstance);
        }
    }
}