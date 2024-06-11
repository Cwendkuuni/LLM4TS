package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setup() {
// create instance objects
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 3);
 instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 3);
 instance3 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 3);

// create DefaultDataset object
 dataset = new DefaultDataset();

// add instances to DefaultDataset
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);

// create DistanceMeasure object
 distanceMeasure = new EuclideanDistance();
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertTrue(emptyDataset.isEmpty());
 }

 @Test
 public void testConstructorWithCollection() {
 Collection<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 DefaultDataset datasetWithInstances = new DefaultDataset(instances);
 assertEquals(3, datasetWithInstances.size());
 }

 @Test
 public void testAddAll() {
 Collection<Instance> instancesToAdd = new ArrayList<>();
 instancesToAdd.add(instance1);
 instancesToAdd.add(instance2);
 assertTrue(dataset.addAll(instancesToAdd));
 assertEquals(5, dataset.size());
 }

 @Test
 public void testAddAllWithDuplicateInstances() {
 Collection<Instance> instancesToAdd = new ArrayList<>();
 instancesToAdd.add(instance1);
 instancesToAdd.add(instance1);
 assertFalse(dataset.addAll(instancesToAdd));
 assertEquals(5, dataset.size());
 }

 @Test
 public void testAdd() {
 Instance newInstance = new DenseInstance(new double[]{7.0, 8.0, 9.0}, 3);
 assertTrue(dataset.add(newInstance));
 assertEquals(6, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 Instance nullInstance = null;
 assertFalse(dataset.add(nullInstance));
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testSize() {
 assertEquals(5, dataset.size());
 }

 @Test
 public void testIsEmpty() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertTrue(emptyDataset.isEmpty());
 }

 @Test
 public void testGet() {
 assertEquals(instance1, dataset.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 dataset.get(5);
 }

 @Test
 public void testClasses() {
 Set<Object> expectedClasses = new TreeSet<>();
 expectedClasses.add(instance1.classValue());
 expectedClasses.add(instance2.classValue());
 expectedClasses.add(instance3.classValue());
 assertEquals(expectedClasses, dataset.classes());
 }

 @Test
 public void testClassesWithNullInstance() {
 Instance nullInstance = null;
 Instance instanceWithNullClass = new DenseInstance(new double[]{1.0}, 0);
 instanceWithNullClass.setClassValue(null);
 dataset.add(instanceWithNullClass);
 Set<Object> expectedClasses = new TreeSet<>();
 expectedClasses.add(instance1.classValue());
 expectedClasses.add(instance2.classValue());
 expectedClasses.add(null);
 assertEquals(expectedClasses, dataset.classes());
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, instance1.noAttributes());
 assertEquals(3, dataset.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 Instance instanceToRemove = instance1;
 dataset.remove(instanceToRemove);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveOutOfBounds() {
 dataset.remove(5);
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> closestInstances = new HashMap<>();
 closestInstances.put(instance2, 5.0);
 closestInstances.put(instance1, 4.0);
 closestInstances.put(instance3, 3.0);
 double farthestDistance = dataset.removeFarthest(closestInstances);
 assertEquals(instance2, dataset.getInstance(0));
 assertEquals(5.0, farthestDistance, 0.0);
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 Random random = new Random();
 Dataset[] expectedFolds = new Dataset[numFolds];
 for (int i = 0; i < numFolds; i++) {
 int[] indices = new int[instance1.noAttributes()];
 Dataset fold = new Fold(dataset, indices);
 expectedFolds[i] = fold;
 }
 Dataset[] actualFolds = dataset.folds(numFolds, random);
 assertArrayEquals(expectedFolds, actualFolds);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsWithNegativeNumFolds() {
 int numFolds = -1;
 Random random = new Random();
 assertNull(dataset.folds(numFolds, random));
 }

 @Test(expected = NullPointerException.class)
 public void testFoldsWithNullRandom() {
 int numFolds = 3;
 assertNull(dataset.folds(numFolds, null));
 }

 @Test(expected = ArrayStoreException.class)
 public void testRemoveInstanceFromFold() {
 Dataset[] folds = dataset.folds(3, new Random());
 for (Dataset fold : folds) {
 Instance instanceToRemove = instance1;
 fold.remove(instanceToRemove);
 fail("Should not be able to remove an instance from a fold");
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testInstanceOutsideFold() {
 Dataset[] folds = dataset.folds(3, new Random());
 Instance instanceOutsideFold = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 3);
 assertFalse(folds[0].contains(instanceOutsideFold));
 }

 @Test
 public void testKNearest() {
 int k = 2;
 Instance queryInstance = new DenseInstance(new double[]{0.5, 0.5, 0.5}, 3);
 Set<Instance> expectedNearestInstances = new TreeSet<>();
 expectedNearestInstances.add(instance1);
 expectedNearestInstances.add(instance2);
 Set<Instance> actualNearestInstances = dataset.kNearest(k, queryInstance, distanceMeasure);
 assertEquals(expectedNearestInstances, actualNearestInstances);
 }

 @Test
 public void testInstanceIsInNearestInstances() {
 Instance queryInstance = new DenseInstance(new double[]{0.5, 0.5, 0.5}, 3);
 Set<Instance> nearestInstances = dataset.kNearest(2, queryInstance, distanceMeasure);
 assertTrue(nearestInstances.contains(instance1));
 assertTrue(nearestInstances.contains(instance2));
 }

 @Test
 public void testInstanceIsNotInNearestInstances() {
 Instance queryInstance = new DenseInstance(new double[]{10.0, 10.0, 10.0}, 3);
 Set<Instance> nearestInstances = dataset.kNearest(2, queryInstance, distanceMeasure);
 assertFalse(nearestInstances.contains(instance3));
 }

 @Test
 public void testDistanceMeasureIsNull() {
 Instance queryInstance = new DenseInstance(new double[]{0.5, 0.5, 0.5}, 3);
 Set<Instance> nearestInstances = dataset.kNearest(2, queryInstance, null);
 fail("Should throw an IllegalArgumentException when the distance measure is null");
 }

 @Test
 public void testKGreaterThanSize() {
 Instance queryInstance = new DenseInstance(new double[]{0.5, 0.5, 0.5}, 3);
 int k = 10;
 Set<Instance> nearestInstances = dataset.kNearest(k, queryInstance, distanceMeasure);
 assertEquals(5, nearestInstances.size());
 }
}