
package net.sf.javaml.core;

import net.sf.javaml.distance.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {
    @BeforeClass
    public static void setUpClass() {
        // Initialize static final long serialVersionUID
    }

    @AfterClass
    public static void tearDownClass() {
        // Perform any necessary cleanup here
    }

    @Before
    public void setUp() {
        // Perform any necessary setup here
    }

    @After
    public void tearDown() {
        // Perform any necessary cleanup here
    }

    @Test
    public void testConstructorAndAddAll() {
        // Test constructor with empty collection
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
        assertTrue(dataset.classes().isEmpty());

        // Test constructor with collection of instances
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 1));
        DefaultDataset dataset1 = new DefaultDataset(instances);
        assertEquals(2, dataset1.size());
        assertEquals(2, dataset1.classes().size());
        assertTrue(dataset1.classes().contains(0));
        assertTrue(dataset1.classes().contains(1));

        // Test addAll method
        DefaultDataset dataset2 = new DefaultDataset();
        dataset2.addAll(instances);
        assertEquals(2, dataset2.size());
        assertEquals(2, dataset2.classes().size());
        assertEquals(dataset1.classes(), dataset2.classes());
    }

    @Test
    public void testBasicMethods() {
        // Test classes() method
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 1));
        dataset.addAll(instances);
        SortedSet<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains(0));
        assertTrue(classes.contains(1));

        // Test noAttributes() method
        assertEquals(2, instances.get(0).noAttributes());
        assertEquals(2, instances.get(1).noAttributes());
        assertEquals(2, dataset.noAttributes());

        // Test classIndex() method
        assertEquals(0, dataset.classIndex(0));
        assertEquals(1, dataset.classIndex(1));

        // Test classValue() method
        assertEquals(0, dataset.classValue(0).hashCode());
        assertEquals(1, dataset.classValue(1).hashCode());

        // Test copy() method
        Dataset copiedDataset = dataset.copy();
        assertEquals(2, copiedDataset.size());
        assertEquals(2, copiedDataset.classes().size());
        assertEquals(dataset.classes(), copiedDataset.classes());
    }

    @Test
    public void testAdvancedMethods() {
        // Test addAll method with instances of the same class
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 0));
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 0));
        dataset.addAll(instances);
        assertEquals(2, dataset.size());

        // Test addAll method with instances of different classes
        instances.clear();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 1));
        dataset.addAll(instances);
        assertEquals(3, dataset.size());
        assertEquals(2, dataset.classes().size());

        // Test clear method
        dataset.clear();
        assertEquals(0, dataset.size());
        assertTrue(dataset.classes().isEmpty());

        // Test kNearest method
        DefaultDataset testDataset = new DefaultDataset();
        Instance testInstance = new DenseInstance(new double[]{1.0, 1.0}, 0);
        Instance nearestInstance1 = new DenseInstance(new double[]{1.1, 1.1}, 0);
        Instance nearestInstance2 = new DenseInstance(new double[]{1.2, 1.2}, 0);
        Instance farthestInstance = new DenseInstance(new double[]{10.0, 10.0}, 1);
        testDataset.addAll(Arrays.asList(testInstance, nearestInstance1, nearestInstance2, farthestInstance));
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        SortedSet<Instance> kNearestInstances = testDataset.kNearest(2, testInstance, distanceMeasure);
        assertEquals(2, kNearestInstances.size());
        assertTrue(kNearestInstances.contains(nearestInstance1));
        assertTrue(kNearestInstances.contains(nearestInstance2));
        assertFalse(kNearestInstances.contains(farthestInstance));
    }

    @Test
    public void testFolds() {
        // Test folds method with different input scenarios and edge cases
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 1));
        dataset.addAll(instances);

        Dataset[] folds = dataset.folds(3, new Random());
        assertEquals(3, folds.length);
        for (Dataset fold : folds) {
            assertEquals(instances.size() / 3, fold.size());
            for (int i = 0; i < fold.size(); i++) {
                Instance inst = fold.instance(i);
                assertTrue(instances.contains(inst));
            }
        }

        // Test folds method with empty dataset
        dataset.clear();
        assertEquals(0, dataset.size());
        assertNull(dataset.folds(3, new Random()));

        // Test folds method with less than required number of instances
        dataset.addAll(instances);
        assertEquals(6, dataset.size());
        assertNull(dataset.folds(7, new Random()));
    }
}
