package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasure;

 @Test
 public void testDefaultDatasetConstructorEmpty() {
 String className = DefaultDataset.class.getName();
 DefaultDataset emptyDataset = new DefaultDataset();
 assertEquals(0, emptyDataset.size());
 assertTrue(emptyDataset.classes().isEmpty());
 }

 @Test
 public void testDefaultDatasetConstructorCollection() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 DefaultDataset dataset = new DefaultDataset(instances);
 assertEquals(2, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testCheckCollection() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 DefaultDataset dataset = new DefaultDataset();
 dataset.check(instances);
 assertEquals(2, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testCheckInstance() {
 String className = DefaultDataset.class.getName();
 Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0));
 DefaultDataset dataset = new DefaultDataset();
 dataset.check(instance);
 assertEquals(1, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNullCollection() {
 String className = DefaultDataset.class.getName();
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(null);
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testAddAllCollection() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(instances);
 assertEquals(2, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 String className = DefaultDataset.class.getName();
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(null);
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testAddInstance() {
 String className = DefaultDataset.class.getName();
 Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0));
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance);
 assertEquals(1, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testClear() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 DefaultDataset dataset = new DefaultDataset(instances);
 dataset.clear();
 assertEquals(0, dataset.size());
 assertTrue(dataset.classes().isEmpty());
 }

 @Test
 public void testAddAllIndexNullCollection() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(1, null);
 assertEquals(1, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testAddAllIndexCollection() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(1, instances);
 assertEquals(3, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testAddElementNullInstance() {
 String className = DefaultDataset.class.getName();
 DefaultDataset dataset = new DefaultDataset();
 dataset.addElement(null);
 assertEquals(0, dataset.size());
 assertTrue(dataset.classes().isEmpty());
 }

 @Test
 public void testAddElementInstance() {
 String className = DefaultDataset.class.getName();
 Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0));
 DefaultDataset dataset = new DefaultDataset();
 dataset.addElement(instance);
 assertEquals(1, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testInsertElementAtNullInstance() {
 String className = DefaultDataset.class.getName();
 Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0));
 DefaultDataset dataset = new DefaultDataset();
 dataset.insertElementAt(null, 0);
 assertEquals(0, dataset.size());
 assertTrue(dataset.classes().isEmpty());
 }

 @Test
 public void testInsertElementAtInstance() {
 String className = DefaultDataset.class.getName();
 Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0));
 DefaultDataset dataset = new DefaultDataset();
 dataset.insertElementAt(instance, 0);
 assertEquals(1, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testSetElementAtNullInstance() {
 String className = DefaultDataset.class.getName();
 Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0));
 DefaultDataset dataset = new DefaultDataset();
 dataset.setElementAt(null, 0);
 assertEquals(0, dataset.size());
 assertTrue(dataset.classes().isEmpty());
 }

 @Test
 public void testSetElementAtInstance() {
 String className = DefaultDataset.class.getName();
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0));
 Instance instance2 = new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0));
 DefaultDataset dataset = new DefaultDataset();
 dataset.setElementAt(instance1, 0);
 assertEquals(1, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 dataset.setElementAt(instance2, 0);
 assertEquals(1, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testInstance() {
 String className = DefaultDataset.class.getName();
 DefaultDataset dataset = new DefaultDataset();
 Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0));
 dataset.add(instance);
 Instance returnedInstance = dataset.instance(0);
 assertSame(instance, returnedInstance);
 }

 @Test
 public void testClasses() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 DefaultDataset dataset = new DefaultDataset(instances);
 SortedSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains(1.0));
 assertTrue(classes.contains(2.0));
 }

 @Test
 public void testKNearest() {
 String className = DefaultDataset.class.getName();
 distanceMeasure = new EuclideanDistance();
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0));
 Instance instance2 = new DenseInstance(new double[]{1.5, 2.5}, new Double(1.5));
 Instance instance3 = new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0));
 Instance instance4 = new DenseInstance(new double[]{3.0, 4.0}, new Double(3.0));
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 Set<Instance> kNearest = dataset.kNearest(2, instance2, distanceMeasure);
 assertEquals(2, kNearest.size());
 assertFalse(kNearest.contains(instance1));
 assertFalse(kNearest.contains(instance4));
 assertTrue(kNearest.contains(instance2));
 assertTrue(kNearest.contains(instance3));
 }

 @Test
 public void testRemoveFarthest() {
 String className = DefaultDataset.class.getName();
 Map<Instance, Double> vector = new HashMap<>();
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0));
 Instance instance2 = new DenseInstance(new double[]{1.5, 2.5}, new Double(1.5));
 Instance instance3 = new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0));
 Instance instance4 = new DenseInstance(new double[]{3.0, 4.0}, new Double(3.0));
 vector.put(instance1, 1.0);
 vector.put(instance2, 2.0);
 vector.put(instance3, 3.0);
 vector.put(instance4, 4.0);
 double max = dataset.removeFarthest(vector);
 assertEquals(4.0, max, 0.0);
 assertEquals(3, vector.size());
 assertTrue(vector.containsKey(instance1));
 assertTrue(vector.containsKey(instance2));
 assertTrue(vector.containsKey(instance3));
 assertFalse(vector.containsKey(instance4));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsNullNumFolds() {
 String className = DefaultDataset.class.getName();
 DefaultDataset dataset = new DefaultDataset();
 try {
 dataset.folds(0, null);
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsNegativeNumFolds() {
 String className = DefaultDataset.class.getName();
 DefaultDataset dataset = new DefaultDataset();
 try {
 dataset.folds(-1, new Random());
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testFolds() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 instances.add(new DenseInstance(new double[]{3.0, 4.0}, new Double(3.0)));
 DefaultDataset dataset = new DefaultDataset(instances);
 Dataset[] folds = dataset.folds(3, new Random());
 assertEquals(3, folds.length);
 for (Dataset fold : folds) {
 assertEquals(instances.size(), fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 DefaultDataset dataset = new DefaultDataset(instances);
 assertEquals(2, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 instances.add(new DenseInstance(new double[]{3.0, 4.0}, new Double(3.0)));
 DefaultDataset dataset = new DefaultDataset(instances);
 assertEquals(3, dataset.classIndex(new Double(3.0)));
 assertEquals(-1, dataset.classIndex(new Double(5.0)));
 }

 @Test
 public void testClassValue() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0), new Double(0.1)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0), new Double(0.2)));
 instances.add(new DenseInstance(new double[]{3.0, 4.0}, new Double(3.0), new Double(0.3)));
 DefaultDataset dataset = new DefaultDataset(instances);
 assertEquals(0.1, dataset.classValue(0), 0.0);
 assertEquals(null, dataset.classValue(1));
 assertEquals(0.3, dataset.classValue(2), 0.0);
 }

 @Test
 public void testCopy() {
 String className = DefaultDataset.class.getName();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[]{2.0, 3.0}, new Double(2.0)));
 DefaultDataset dataset = new DefaultDataset(instances);
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(2, copiedDataset.size());
 assertFalse(copiedDataset.classes().isEmpty());
 }
}