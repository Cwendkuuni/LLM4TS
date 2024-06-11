package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 // Create instances
 instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 4.0);
 instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 5.0);
 instance3 = new DenseInstance(new double[] { 2.0, 2.0, 2.0 }, 4.0);

 // Create distance measure
 distanceMeasure = new EuclideanDistance();

 // Create dataset
 dataset = new DefaultDataset();
 }

 @Test
 public void testDefaultDatasetEmpty() {
 // Test empty constructor
 DefaultDataset emptyDataset = new DefaultDataset();
 assertTrue(emptyDataset.isEmpty());
 }

 @Test
 public void testAddInstance() {
 // Test addInstance with single instance
 dataset.add(instance1);
 assertEquals(1, dataset.size());

 // Test addInstance with multiple instances
 dataset.addAll(Arrays.asList(instance2, instance3));
 assertEquals(3, dataset.size());
 }

 @Test
 public void testAddAllInstances() {
 Collection<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);

 dataset.addAll(instances);
 assertEquals(3, dataset.size());
 }

 @Test
 public void testAddInstanceWithInvalidClass() {
 // Test adding an instance with a null class
 try {
 dataset.add(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, null));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test adding an instance with an invalid class
 try {
 dataset.add(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, new Object() {}));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testClasses() {
 // Test empty classes
 assertEquals(0, dataset.classes().size());

 // Test adding instances with different classes
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(2, dataset.classes().size());
 assertEquals(Instance.classValue(0), dataset.classes().first());
 assertEquals(Instance.classValue(1), dataset.classes().last());
 }

 @Test
 public void testAddInstanceWithSameClass() {
 // Test adding two instances with the same class
 dataset.add(instance1);
 dataset.add(instance1);
 assertEquals(2, dataset.size());
 assertEquals(Instance.classValue(0), dataset.classes().first());
 }

 @Test
 public void testNoAttributes() {
 // Test noAttributes on empty dataset
 assertEquals(0, dataset.noAttributes());

 // Test noAttributes on a dataset with instances
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testMaxAttributes() {
 // Test maxAttributes on empty dataset
 assertEquals(0, dataset.maxAttributes());

 // Test maxAttributes on a dataset with instances having different number of attributes
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance2.noAttributes(), dataset.maxAttributes());
 }

 @Test
 public void testClear() {
 // Test clearing dataset
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testRemoveInstance() {
 // Test removing an instance by index
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(2, dataset.size());
 dataset.remove(1);
 assertEquals(1, dataset.size());
 assertEquals(instance1, dataset.get(0));

 // Test removing an instance by class value
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(2, dataset.size());
 assertEquals(1, dataset.classes().size());
 dataset.removeInstance(Instance.classValue(0));
 assertEquals(1, dataset.size());
 assertFalse(dataset.contains(instance1));
 assertFalse(dataset.contains(instance2));
 }

 @Test
 public void testRemoveInstanceByIndexWhenEmpty() {
 // Test removing an instance by index when dataset is empty
 dataset.clear();
 try {
 dataset.remove(0);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveInstanceByIndexWhenOutOfBounds() {
 // Test removing an instance by index when index is out of bounds
 dataset.add(instance1);
 dataset.add(instance2);
 try {
 dataset.remove(3);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testKNearest() {
 // Test kNearest with k = 1 and an empty dataset
 assertEquals(Collections.emptyList(), dataset.kNearest(1, instance1, distanceMeasure));

 // Test kNearest with k = 1 and a dataset with one instance
 dataset.add(instance1);
 assertEquals(Collections.singletonList(instance1), dataset.kNearest(1, instance1, distanceMeasure));

 // Test kNearest with k = 2 and a dataset with two instances
 dataset.add(instance2);
 List<Instance> nearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, nearestInstances.size());
 assertTrue(nearestInstances.contains(instance1));
 assertTrue(nearestInstances.contains(instance2));

 // Test kNearest with k = 2 and an instance that is identical to another instance in the dataset
 dataset.add(instance3);
 nearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, nearestInstances.size());
 assertTrue(nearestInstances.contains(instance1));
 assertTrue(nearestInstances.contains(instance1)); // Should be the same instance
 }

 @Test
 public void testFolds() {
 // Test folds with 2 folds and a dataset with 6 instances
 int numFolds = 2;
 int size = 6;
 DefaultDataset datasetWithSixInstances = new DefaultDataset();
 for (int i = 0; i < size; ++i) {
 datasetWithSixInstances.add(new DenseInstance(new double[] { i + 1.0, i + 2.0, i + 3.0 }, (Object) i % 2 + ""));
 }
 Dataset[] folds = datasetWithSixInstances.folds(numFolds, new Random());

 // Test that each fold has the expected size
 for (Dataset fold : folds) {
 assertEquals(size / numFolds, fold.size());
 }

 // Test that instances are evenly distributed among folds
 Set<Instance> instancesInFold0 = new HashSet<>();
 Set<Instance> instancesInFold1 = new HashSet<>();
 for (Instance instance : datasetWithSixInstances) {
 instancesInFold0.add(instance);
 instancesInFold1.add(instance);
 Dataset fold = folds[instance.getClassValue().toString().charAt(0) - '0'];
 if (fold.size() > instancesInFold0.size()) {
 instancesInFold0 = (Set<Instance>) fold;
 }
 if (fold.size() > instancesInFold1.size()) {
 instancesInFold1 = (Set<Instance>) fold;
 }
 }
 assertEquals(size / numFolds, instancesInFold0.size());
 assertEquals(size / numFolds, instancesInFold1.size());
 assertFalse(instancesInFold0.isEmpty());
 assertFalse(instancesInFold1.isEmpty());
 assertFalse(instancesInFold0.containsAll(instancesInFold1));
 assertFalse(instancesInFold1.containsAll(instancesInFold0));
 }
}