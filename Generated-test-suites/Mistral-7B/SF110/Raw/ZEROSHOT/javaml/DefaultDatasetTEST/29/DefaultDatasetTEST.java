package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.distance.*;
import org.junit.*;

public class DefaultDatasetTest {

 @BeforeClass
 public static void setupClass() {
 // Initialize any static variables or setup any static resources here
 }

 @AfterClass
 public static void tearDownClass() {
 // Teardown any static resources here
 }

 @Before
 public void setUp() {
 // Initialize any instance variables here
 }

 @After
 public void tearDown() {
 // Teardown any instance resources here
 }

 @Test
 public void testDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset();
 // Test empty constructor
 assertEquals(0, dataset.size());
 assertEquals(0, dataset.maxAttributes);
 assertNotNull(dataset.classes());
 assertFalse(dataset.classes().isEmpty());
 // Test constructor with collection
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
 dataset = new DefaultDataset(instances);
 assertEquals(2, dataset.size());
 assertEquals(3, dataset.maxAttributes);
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testAddAll() {
 DefaultDataset dataset = new DefaultDataset();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
 dataset.addAll(instances);
 assertEquals(2, dataset.size());
 assertEquals(3, dataset.maxAttributes);
 assertFalse(dataset.classes().isEmpty());
}

 @Test
 public void testClear() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.clear();
 assertEquals(0, dataset.size());
 assertEquals(0, dataset.maxAttributes);
 assertFalse(dataset.classes().isEmpty());
}

 @Test
 public void testAdd() {
 DefaultDataset dataset = new DefaultDataset();
 Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0);
 dataset.add(instance);
 assertEquals(1, dataset.size());
 assertEquals(3, dataset.maxAttributes);
 assertFalse(dataset.classes().isEmpty());
}

 @Test
 public void testFolds() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1),
 new DenseInstance(new double[]{7.0, 8.0, 9.0}, 0),
 new DenseInstance(new double[]{10.0, 11.0, 12.0}, 1)
 ));
 int numFolds = 3;
 Random rg = new Random();
 Dataset[] folds = dataset.folds(numFolds, rg);
 assertEquals(numFolds, folds.length);
 for (Dataset fold : folds) {
 assertEquals(dataset.size() / numFolds, fold.size());
 }
}

 @Test
 public void testKNearest() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1),
 new DenseInstance(new double[]{7.0, 8.0, 9.0}, 0),
 new DenseInstance(new double[]{10.0, 11.0, 12.0}, 1)
 ));
 Instance testInstance = new DenseInstance(new double[]{5.5, 5.5, 5.5}, 1);
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 Set<Instance> kNearest = dataset.kNearest(2, testInstance, distanceMeasure);
 assertEquals(2, kNearest.size());
}

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeKNearest() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1),
 new DenseInstance(new double[]{7.0, 8.0, 9.0}, 0),
 new DenseInstance(new double[]{10.0, 11.0, 12.0}, 1)
 ));
 Instance testInstance = new DenseInstance(new double[]{5.5, 5.5, 5.5}, 1);
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 dataset.kNearest(-1, testInstance, distanceMeasure);
}

 @Test(expected = NullPointerException.class)
 public void testNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1),
 new DenseInstance(new double[]{7.0, 8.0, 9.0}, 0),
 new DenseInstance(new double[]{10.0, 11.0, 12.0}, 1)
 ));
 Instance testInstance = null;
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 dataset.kNearest(2, testInstance, distanceMeasure);
}

 @Test
 public void testRemoveFarthest() {
 DefaultDataset dataset = new DefaultDataset();
 Map<Instance, Double> closest = new HashMap<>();
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0);
 Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1);
 Instance instance3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, 0);
 Instance instance4 = new DenseInstance(new double[]{10.0, 11.0, 12.0}, 1);
 closest.put(instance1, 1.0);
 closest.put(instance2, 2.0);
 closest.put(instance3, 3.0);
 closest.put(instance4, 4.0);
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 double max = dataset.removeFarthest(closest);
 assertEquals(4.0, max, 0.001);
 assertEquals(3, closest.size());
}

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInstanceCollection() {
 DefaultDataset dataset = new DefaultDataset();
 Collection<Instance> instances = Collections.emptyList();
 dataset.addAll(instances);
}

 @Test
 public void testNoAttributes() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1)
 ));
 assertEquals(3, dataset.noAttributes());
}

 @Test
 public void testClassIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1)
 ));
 assertEquals(0, dataset.classIndex(0));
 assertEquals(1, dataset.classIndex(1));
 assertEquals(-1, dataset.classIndex(null));
}

 @Test
 public void testClassValue() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1)
 ));
 assertEquals(0, dataset.classValue(0).hashCode());
 assertEquals(1, dataset.classValue(1).hashCode());
 assertEquals(null, dataset.classValue(10));
}

 @Test
 public void testCopy() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1)
 ));
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(dataset.size(), copiedDataset.size());
 for (int i = 0; i < dataset.size(); i++) {
 assertEquals(dataset.instance(i), copiedDataset.instance(i));
 }
}
}