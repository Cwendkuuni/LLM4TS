package net.sf.javaml.core;

import net.sf.javaml.distance.*;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private ArrayList<Instance> instances;
 private Instance instance1, instance2, instance3;
 private Instance instance4;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setup() {
 instances = new ArrayList<>();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{5.0});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 1.0}, new double[]{5.0});
 instance3 = new DenseInstance(new double[]{3.0, 1.0, 2.0}, new double[]{4.0});
 instance4 = new DenseInstance(new double[]{3.0, 2.0, 1.0}, new double[]{4.0});

 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 instances.add(instance4);

 dataset = new DefaultDataset(instances);
 distanceMeasure = new EuclideanDistance();
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertTrue(emptyDataset.size() == 0);
 }

 @Test
 public void testAddAllCollection() {
 DefaultDataset datasetCopy = new DefaultDataset();
 datasetCopy.addAll(instances);

 assertEquals(instances.size(), datasetCopy.size());
 for (Instance instance : instances) {
 assertTrue(datasetCopy.contains(instance));
 }
 }

 @Test
 public void testAddAllCollectionWithDuplicates() {
 DefaultDataset datasetCopy = new DefaultDataset();
 datasetCopy.addAll(instances);
 datasetCopy.addAll(instances);

 assertEquals(instances.size() * 2, datasetCopy.size());
 for (Instance instance : instances) {
 assertTrue(datasetCopy.contains(instance));
 }
 }

 @Test
 public void testAddAllCollectionWithNullInstance() {
 DefaultDataset datasetCopy = new DefaultDataset();
 ArrayList<Instance> invalidInstance = new ArrayList<>();
 invalidInstance.add(null);

 assertFalse(datasetCopy.addAll(invalidInstance));
 assertEquals(datasetCopy.size(), 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllCollectionWithInvalidInstance() {
 DefaultDataset datasetCopy = new DefaultDataset();
 Instance invalidInstance = new Instance() {
 @Override
 public int noAttributes() {
 throw new UnsupportedOperationException();
 }
 };
 ArrayList<Instance> invalidInstances = new ArrayList<>();
 invalidInstances.add(invalidInstance);

 datasetCopy.addAll(invalidInstances);
 }

 @Test
 public void testAddInstance() {
 DefaultDataset datasetCopy = new DefaultDataset();
 datasetCopy.add(instance1);

 assertEquals(1, datasetCopy.size());
 assertTrue(datasetCopy.contains(instance1));
 }

 @Test
 public void testAddInstanceWithDuplicate() {
 DefaultDataset datasetCopy = new DefaultDataset();
 datasetCopy.add(instance1);
 datasetCopy.add(instance1);

 assertEquals(2, datasetCopy.size());
 assertTrue(datasetCopy.contains(instance1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInstanceWithInvalidInstance() {
 DefaultDataset datasetCopy = new DefaultDataset();
 Instance invalidInstance = new Instance() {
 @Override
 public int noAttributes() {
 throw new UnsupportedOperationException();
 }
 };

 datasetCopy.add(invalidInstance);
 }

 @Test
 public void testClear() {
 DefaultDataset datasetCopy = new DefaultDataset(instances);
 datasetCopy.clear();

 assertEquals(0, datasetCopy.size());
 }

 @Test
 public void testClasses() {
 Set<Object> classes = dataset.classes();
 assertEquals(1, classes.size());
 assertEquals(instance1.classValue(), classes.first());
 }

 @Test
 public void testClassesWithMultipleClasses() {
 Instance instance5 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{1.0});
 instances.add(instance5);
 DefaultDataset datasetCopy = new DefaultDataset(instances);
 Set<Object> classes = datasetCopy.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains(instance1.classValue()));
 assertTrue(classes.contains(instance5.classValue()));
 }

 @Test
 public void testInstance() {
 Instance instance = dataset.instance(0);
 assertEquals(instance1, instance);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceWithInvalidIndex() {
 Instance instance = dataset.instance(3);
 }

 @Test
 public void testKNearestWithSingleInstance() {
 List<Instance> neighbors = dataset.kNearest(1, instance1, distanceMeasure);
 assertEquals(1, neighbors.size());
 assertEquals(instance1, neighbors.get(0));
 }

 @Test
 public void testKNearestWithMultipleNeighbors() {
 int k = 2;
 List<Instance> neighbors = dataset.kNearest(k, instance1, distanceMeasure);
 assertEquals(k, neighbors.size());
 assertTrue(neighbors.contains(instance1));
 assertTrue(neighbors.contains(instance2));
 }

 @Test
 public void testKNearestWithCustomComparator() {
 DistanceMeasure customDistanceMeasure = new ManhattanDistance();
 List<Instance> neighbors = dataset.kNearest(2, instance1, customDistanceMeasure);
 Instance nearestNeighbor = neighbors.get(0);
 Instance secondNearestNeighbor = neighbors.get(1);

 double distanceToNearestNeighbor = customDistanceMeasure.measure(instance1, nearestNeighbor);
 double distanceToSecondNearestNeighbor = customDistanceMeasure.measure(instance1, secondNearestNeighbor);

 assertTrue(distanceToNearestNeighbor < distanceToSecondNearestNeighbor);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestWithInvalidNumberOfNeighbors() {
 List<Instance> neighbors = dataset.kNearest(-1, instance1, distanceMeasure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestWithNullInstance() {
 List<Instance> neighbors = dataset.kNearest(1, null, distanceMeasure);
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> closestInstances = new HashMap<>();
 closestInstances.put(instance2, 3.0);
 closestInstances.put(instance3, 2.0);
 closestInstances.put(instance1, 1.0);
 closestInstances.put(instance4, 4.0);

 double maxDistance = dataset.removeFarthest(closestInstances);
 Instance removedInstance = closestInstances.remove(maxDistance);

 assertEquals(instance4, removedInstance);
 assertEquals(3.0, maxDistance);
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveFarthestWithEmptyMap() {
 Map<Instance, Double> emptyMap = new HashMap<>();
 dataset.removeFarthest(emptyMap);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveFarthestWithNullInstance() {
 Map<Instance, Double> closestInstances = new HashMap<>();
 closestInstances.put(instance2, 3.0);
 closestInstances.put(instance3, 2.0);
 closestInstances.put(null, 1.0);

 dataset.removeFarthest(closestInstances);
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 Dataset[] folds = dataset.folds(numFolds, new SecureRandom());

 assertEquals(numFolds, folds.length);
 for (Dataset fold : folds) {
 assertTrue(fold instanceof Fold);
 assertEquals(dataset.size() / numFolds, fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(0, dataset.classIndex(instance1.classValue()));
 }

 @Test(expected = NullPointerException.class)
 public void testClassIndexWithNullClass() {
 dataset.classIndex(null);
 }

 @Test
 public void testClassValue() {
 assertEquals(instance1.classValue(), dataset.classValue(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueWithInvalidIndex() {
 dataset.classValue(3);
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(dataset.size(), copiedDataset.size());
 for (Instance instance : dataset) {
 assertTrue(copiedDataset.contains(instance.copy()));
 }
 }
}