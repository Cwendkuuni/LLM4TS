package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.DistanceMeasure;
import net.sf.javaml.core.*;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset createEmptyDataset() {
 return new DefaultDataset();
 }

 private DefaultDataset createDatasetWithInstances(Instance... instances) {
 DefaultDataset dataset = new DefaultDataset();
 for (Instance instance : instances) {
 dataset.add(instance);
 }
 return dataset;
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset dataset = createEmptyDataset();
 assertEquals(0, dataset.size());
 assertEquals(0, dataset.maxAttributes);
 assertNotNull(dataset.classes);
 }

 @Test
 public void testConstructorWithCollection() {
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)));
 DefaultDataset dataset = new DefaultDataset(instances);
 assertEquals(1, dataset.size());
 assertEquals(instance(dataset, 0).noAttributes(), 3);
 }

 @Test
 public void testAddAll() {
 DefaultDataset dataset = createEmptyDataset();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 boolean added = dataset.addAll(instances);
 assertEquals(added, true);
 assertEquals(2, dataset.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithEmptyCollection() {
 DefaultDataset dataset = createEmptyDataset();
 boolean added = dataset.addAll(Collections.emptyList());
 }

 @Test
 public void testAdd() {
 DefaultDataset dataset = createEmptyDataset();
 Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0));
 boolean added = dataset.add(instance);
 assertEquals(added, true);
 assertEquals(1, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 DefaultDataset dataset = createEmptyDataset();
 Instance instance = null;
 boolean added = dataset.add(instance);
 }

 @Test
 public void testClear() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testSetElementAt() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 Instance newInstance = new DenseInstance(new double[]{7.0, 8.0, 9.0}, new Double(2.0));
 dataset.setElementAt(newInstance, 0);
 assertEquals(newInstance, dataset.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetElementAtWithNegativeIndex() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 dataset.setElementAt(null, -1);
 }

 @Test
 public void testInstance() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 assertEquals(dataset.instance(0), dataset.get(0));
 }

 @Test
 public void testClasses() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 SortedSet<Object> classes = dataset.classes();
 assertTrue(classes.containsAll(Arrays.asList(new Double(0.0), new Double(1.0))));
 }

 @Test
 public void testKNearestWithInvalidInstance() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 assertNull(dataset.kNearest(1, null, new EuclideanDistance()));
 }

 @Test
 public void testKNearestWithEmptyDataset() {
 DefaultDataset dataset = createEmptyDataset();
 assertNull(dataset.kNearest(1, new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new EuclideanDistance()));
 }

 @Test
 public void testKNearestWithLessKInstancesThanAvailable() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)), new DenseInstance(new double[]{7.0, 8.0, 9.0}, new Double(0.0)));
 Set<Instance> kNearest = dataset.kNearest(1, new DenseInstance(new double[]{10.0, 11.0, 12.0}, new Double(2.0)), new EuclideanDistance());
 assertEquals(kNearest.size(), 1);
 }

 @Test
 public void testKNearest() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)), new DenseInstance(new double[]{7.0, 8.0, 9.0}, new Double(0.0)), new DenseInstance(new double[]{10.0, 11.0, 12.0}, new Double(1.0)));
 Set<Instance> kNearest = dataset.kNearest(2, new DenseInstance(new double[]{5.5, 5.5, 5.5}, new Double(0.5)), new EuclideanDistance());
 assertTrue(kNearest.contains(dataset.instance(1)));
 assertTrue(kNearest.contains(dataset.instance(3)));
 }

 @Test
 public void testRemoveFarthestWithEmptyMap() {
 DefaultDataset dataset = createEmptyDataset();
 Map<Instance, Double> closest = new HashMap<>();
 double max = dataset.removeFarthest(closest);
 assertEquals(max, Double.POSITIVE_INFINITY);
 assertFalse(closest.isEmpty());
 }

 @Test
 public void testRemoveFarthest() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)), new DenseInstance(new double[]{7.0, 8.0, 9.0}, new Double(0.0)));
 Map<Instance, Double> closest = new HashMap<>();
 for (Instance instance : dataset) {
 closest.put(instance, new EuclideanDistance().measure(new DenseInstance(new double[]{5.5, 5.5, 5.5}, new Double(0.5)), instance));
 }
 double max = dataset.removeFarthest(closest);
 assertEquals(max, closest.get(dataset.instance(0)));
 }

 @Test
 public void testFoldsWithInvalidNumFolds() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 assertNull(dataset.folds(-1, new Random()));
 }

 @Test
 public void testFoldsWithValidNumFolds() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 Dataset[] folds = dataset.folds(2, new Random());
 assertEquals(folds.length, 2);
 }

 @Test
 public void testFoldsWithEmptyDataset() {
 DefaultDataset dataset = createEmptyDataset();
 Dataset[] folds = dataset.folds(2, new Random());
 assertEquals(folds.length, 0);
 }

 @Test
 public void testNoAttributes() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 assertEquals(dataset.noAttributes(), dataset.instance(0).noAttributes());
 }

 @Test
 public void testClassIndexWithNullClass() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 assertEquals(dataset.classIndex(null), -1);
 }

 @Test
 public void testClassIndexWithValidClass() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 assertEquals(dataset.classIndex(new Double(0.0)), 0);
 }

 @Test
 public void testClassValueWithValidIndex() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 assertEquals(dataset.classValue(0), new Double(0.0));
 }

 @Test
 public void testClassValueWithInvalidIndex() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 assertNull(dataset.classValue(1));
 }

 @Test
 public void testCopy() {
 DefaultDataset originalDataset = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0)));
 DefaultDataset copiedDataset = originalDataset.copy();
 assertEquals(copiedDataset.size(), originalDataset.size());
 }
}