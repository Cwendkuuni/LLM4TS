package net.sf.javaml.core;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private DefaultDataset copiedDataset;
 private Instance instance1;
 private Instance instance2;
 private Instance instance3;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 // Initialize objects
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0));
 instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0));
 instance3 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0));

 dataset = new DefaultDataset();
 copiedDataset = new DefaultDataset();

 // Add instances to the dataset
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);

 // Initialize distance measure
 distanceMeasure = new EuclideanDistance();
 }

 @After
 public void tearDown() {
 // Clear instances and datasets
 dataset.clear();
 copiedDataset.clear();
 }

 @Test
 public void testDefaultDatasetConstructorWithEmptyCollection() {
 // Test DefaultDataset constructor with an empty collection
 DefaultDataset emptyDataset = new DefaultDataset();

 // Check if the dataset is empty
 Assert.assertEquals(0, emptyDataset.size());
 }

 @Test
 public void testDefaultDatasetConstructorWithCollection() {
 // Test DefaultDataset constructor with a collection
 Collection<Instance> instances = new ArrayList<Instance>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);

 DefaultDataset datasetWithInstances = new DefaultDataset(instances);

 // Check if the dataset contains the instances
 Assert.assertEquals(3, datasetWithInstances.size());
 Assert.assertTrue(datasetWithInstances.contains(instance1));
 Assert.assertTrue(datasetWithInstances.contains(instance2));
 Assert.assertTrue(datasetWithInstances.contains(instance3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInstanceWithNullInstance() {
 // Test adding a null instance
 DefaultDataset dataset = new DefaultDataset();

 // Try to add a null instance
 dataset.add(null);
 }

 @Test
 public void testAddAllInstances() {
 // Test adding multiple instances
 Collection<Instance> instancesToAdd = new ArrayList<Instance>();
 instancesToAdd.add(instance1);
 instancesToAdd.add(instance2);
 instancesToAdd.add(instance3);

 boolean added = dataset.addAll(instancesToAdd);

 // Check if the instances were added
 Assert.assertEquals(3, dataset.size());
 Assert.assertTrue(dataset.containsAll(instancesToAdd));
 Assert.assertTrue(added);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllInstancesWithNullCollection() {
 // Test adding instances with a null collection
 DefaultDataset dataset = new DefaultDataset();

 // Try to add instances with a null collection
 dataset.addAll(null);
 }

 @Test
 public void testClear() {
 // Test clearing the dataset
 dataset.clear();

 // Check if the dataset is empty
 Assert.assertEquals(0, dataset.size());
 }

 @Test
 public void testAddInstance() {
 // Test adding an instance
 boolean added = dataset.add(instance1);

 // Check if the instance was added
 Assert.assertEquals(2, dataset.size());
 Assert.assertTrue(dataset.contains(instance1));
 Assert.assertTrue(added);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddInstanceWithInvalidIndex() {
 // Test adding an instance with an invalid index
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);

 // Try to add an instance with an invalid index
 dataset.add(1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInstanceWithNullInstanceAtInvalidIndex() {
 // Test adding a null instance at an invalid index
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);

 // Try to add a null instance at an invalid index
 dataset.add(1, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetElementAtWithInvalidIndex() {
 // Test setting an element at an invalid index
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);

 // Try to set an element at an invalid index
 dataset.setElementAt(instance3, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetElementAtWithNullInstance() {
 // Test setting a null instance
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);

 // Try to set a null instance
 dataset.setElementAt(null, 0);
 }

 @Test
 public void testGetInstance() {
 // Test getting an instance by index
 Assert.assertEquals(instance1, dataset.getInstance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInstanceWithInvalidIndex() {
 // Test getting an instance with an invalid index
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);

 // Try to get an instance with an invalid index
 dataset.getInstance(1);
 }

 @Test
 public void testClasses() {
 // Test getting classes
 SortedSet<Object> classes = dataset.classes();

 // Check if the classes are correct
 Assert.assertEquals(1, classes.size());
 Assert.assertEquals(instance1.classValue(), classes.first());
 }

 @Test
 public void testKNearest() {
 // Test getting k nearest instances
 Set<Instance> kNearestInstances = dataset.kNearest(2, instance1, distanceMeasure);

 // Check if the number of nearest instances is correct
 Assert.assertEquals(2, kNearestInstances.size());

 // Check if the instances are in the result set
 Assert.assertTrue(kNearestInstances.contains(instance1));
 Assert.assertTrue(kNearestInstances.contains(instance2));

 // Check if the distances are correct
 Double expectedDistance1 = distanceMeasure.measure(instance1, instance1);
 Double expectedDistance2 = distanceMeasure.measure(instance1, instance2);

 for (Instance instance : kNearestInstances) {
 if (instance.equals(instance1)) {
 Assert.assertEquals(expectedDistance1, distanceMeasure.measure(instance1, instance));
 } else {
 Assert.assertEquals(expectedDistance2, distanceMeasure.measure(instance1, instance));
 }
 }
 }

 @Test(expected = NullPointerException.class)
 public void testKNearestWithNullDistanceMeasure() {
 // Test getting k nearest instances with a null distance measure
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);

 // Try to get k nearest instances with a null distance measure
 dataset.kNearest(2, null, null);
 }

 @Test
 public void testFolds() {
 // Test getting folds
 Dataset[] folds = dataset.folds(3, new Random());

 // Check if the number of folds is correct
 Assert.assertEquals(3, folds.length);

 // Check if each fold contains the correct number of instances
 for (Dataset fold : folds) {
 Assert.assertNotNull(fold);
 Assert.assertEquals(dataset.size() / 3, fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 // Test getting the number of attributes
 int noAttributes = dataset.noAttributes();

 // Check if the number of attributes is correct
 Assert.assertEquals(instance1.noAttributes(), noAttributes);
 }

 @Test
 public void testClassIndex() {
 // Test getting class index
 int classIndex = dataset.classIndex(instance1.classValue());

 // Check if the class index is correct
 Assert.assertEquals(0, classIndex);
 }

 @Test
 public void testClassValue() {
 // Test getting class value
 Object classValue = dataset.classValue(0);

 // Check if the class value is correct
 Assert.assertEquals(instance1.classValue(), classValue);
 }

 @Test
 public void testCopy() {
 // Test copying the dataset
 DefaultDataset copiedDataset = dataset.copy();

 // Check if the copied dataset contains the same instances as the original dataset
 Assert.assertEquals(dataset.size(), copiedDataset.size());
 Assert.assertTrue(copiedDataset.containsAll(dataset));
 }
}