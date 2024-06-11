package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset defaultDataset;
 private Instance instance1, instance2;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 Collection<Instance> instances = new ArrayList<>();
 instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, new Object[] { "Class1" });
 instance2 = new DenseInstance(new double[] { 2.0, 2.0, 3.0 }, new Object[] { "Class2" });
 instances.add(instance1);
 instances.add(instance2);
 defaultDataset = new DefaultDataset(instances);
 distanceMeasure = new EuclideanDistance();
 }

 @Test
 public void testDefaultDatasetConstructorWithCollection() {
 // Test the constructor that takes a Collection of Instances
 DefaultDataset dataset = new DefaultDataset(defaultDataset);
 assertEquals(2, dataset.size());
 }

 @Test
 public void testDefaultDatasetConstructorEmpty() {
 // Test the constructor that takes no arguments
 DefaultDataset emptyDataset = new DefaultDataset();
 assertEquals(0, emptyDataset.size());
 }

 @Test
 public void testAddAll() {
 // Test adding a collection of Instances
 Collection<Instance> instancesToAdd = new ArrayList<>();
 instancesToAdd.add(new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, new Object[] { "Class3" }));
 defaultDataset.addAll(instancesToAdd);
 assertEquals(4, defaultDataset.size());
 }

 @Test
 public void testAddAllWithDuplicates() {
 // Test adding Instances with duplicates
 defaultDataset.add(instance1);
 defaultDataset.add(instance1);
 assertEquals(2, defaultDataset.size());
 }

 @Test
 public void testClear() {
 // Test clearing the dataset
 defaultDataset.clear();
 assertEquals(0, defaultDataset.size());
 }

 @Test
 public void testAdd() {
 // Test adding an Instance
 Instance newInstance = new DenseInstance(new double[] { 7.0, 8.0, 9.0 }, new Object[] { "Class4" });
 defaultDataset.add(newInstance);
 assertEquals(5, defaultDataset.size());
 }

 @Test
 public void testSetElementAt() {
 // Test setting an element at a specific index
 Instance newInstance = new DenseInstance(new double[] { 10.0, 11.0, 12.0 }, new Object[] { "Class5" });
 defaultDataset.setElementAt(newInstance, 2);
 assertEquals(instance1.getClass(), defaultDataset.get(0).getClass());
 assertEquals(instance2.getClass(), defaultDataset.get(1).getClass());
 assertEquals(newInstance.getClass(), defaultDataset.get(2).getClass());
 }

 @Test
 public void testInstance() {
 // Test getting an Instance by index
 Instance instanceFromDataset = defaultDataset.instance(2);
 assertEquals(newInstance.getClass(), instanceFromDataset.getClass());
 }

 @Test
 public void testClasses() {
 // Test getting the classes
 List<Object> classes = new ArrayList<>(defaultDataset.classes());
 assertEquals(5, classes.size());
 assertTrue(classes.containsAll(Arrays.asList("Class1", "Class2", "Class3", "Class4", "Class5")));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearestWithInvalidInput() {
 // Test kNearest() with invalid input (k < 1)
 Set<Instance> kNearestInstances = defaultDataset.kNearest(0, instance1, distanceMeasure);
 fail("Expected UnsupportedOperationException");
 }

 @Test
 public void testKNearest() {
 // Test kNearest() with a valid input
 Set<Instance> kNearestInstances = defaultDataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, kNearestInstances.size());
 }

 @Test
 public void testRemoveFarthest() {
 // Test removeFarthest() method
 Map<Instance, Double> vector = new HashMap<>();
 vector.put(instance1, 1.0);
 vector.put(instance2, 2.0);
 double max = defaultDataset.removeFarthest(vector);
 assertEquals(1.0, max);
 }

 @Test
 public void testFolds() {
 // Test folds() method with valid input
 Dataset[] folds = defaultDataset.folds(5, new Random());
 assertEquals(5, folds.length);
 }

 @Test
 public void testNoAttributes() {
 // Test noAttributes() method
 assertEquals(instance1.noAttributes(), defaultDataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 // Test classIndex() method
 assertEquals(1, defaultDataset.classIndex("Class1"));
 }

 @Test
 public void testClassValue() {
 // Test classValue() method
 Object classValue = defaultDataset.classValue(1);
 assertEquals("Class1", classValue);
 }

 @Test
 public void testCopy() {
 // Test copy() method
 Dataset copiedDataset = defaultDataset.copy();
 assertEquals(defaultDataset.size(), copiedDataset.size());
 }
}