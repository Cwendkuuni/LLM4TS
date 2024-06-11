package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1;
 private Instance instance2;
 private Instance instance3;
 private Collection<Instance> coll;

 @Test
 public void testDefaultDataset() {
 // Test empty constructor
 dataset = new DefaultDataset();
 assertNotNull(dataset);
 assertTrue(dataset.isEmpty());
 assertTrue(dataset.classes().isEmpty());

 // Test constructor with collection
 coll = new ArrayList<>();
 instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 4.0);
 instance2 = new DenseInstance(new double[] { 2.0, 3.0, 4.0 }, 5.0);
 coll.add(instance1);
 coll.add(instance2);
 dataset = new DefaultDataset(coll);
 assertEquals(2, dataset.size());
 assertNotNull(dataset.classes());
 assertEquals(2, dataset.classes().size());

 // Test constructor without collection
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(2, dataset.size());
 assertNotNull(dataset.classes());
 assertEquals(2, dataset.classes().size());
 }

 @Test
 public void testAddAll() {
 // Test adding instances to an empty dataset
 coll = new ArrayList<>();
 coll.add(instance1);
 dataset = new DefaultDataset();
 dataset.addAll(coll);
 assertEquals(1, dataset.size());

 // Test adding instances to a non-empty dataset
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.addAll(coll);
 assertEquals(3, dataset.size());

 // Test adding duplicated instances
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance1);
 assertEquals(2, dataset.size());
 }

 @Test
 public void testAdd() {
 // Test adding an instance to an empty dataset
 dataset = new DefaultDataset();
 dataset.add(instance1);
 assertEquals(1, dataset.size());

 // Test adding an instance to a non-empty dataset
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 assertEquals(3, dataset.size());
 }

 @Test
 public void testClear() {
 // Test clearing an empty dataset
 dataset = new DefaultDataset();
 dataset.clear();
 assertTrue(dataset.isEmpty());

 // Test clearing a non-empty dataset
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.clear();
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testCheck() {
 // Test checking a collection of instances
 coll = new ArrayList<>();
 coll.add(instance1);
 dataset = new DefaultDataset();
 dataset.check(coll);
 assertEquals(1, dataset.size());

 // Test checking an instance
 dataset = new DefaultDataset();
 dataset.check(instance1);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testInstance() {
 // Test getting an instance by index
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance1, dataset.instance(0));

 // Test getting an instance that does not exist
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 assertNull(dataset.instance(2));
 }

 @Test
 public void testClasses() {
 // Test getting classes from an empty dataset
 dataset = new DefaultDataset();
 assertTrue(dataset.classes().isEmpty());

 // Test getting classes from a dataset with instances of different classes
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(2, dataset.classes().size());
 assertEquals(4.0, dataset.classes().first(), 0.001);
 assertEquals(5.0, dataset.classes().last(), 0.001);
 }

 @Test
 public void testKNearest() {
 // Test getting k nearest neighbors
 dataset = new DefaultDataset();
 instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 4.0);
 instance2 = new DenseInstance(new double[] { 2.0, 3.0, 4.0 }, 5.0);
 instance3 = new DenseInstance(new double[] { 1.0, 2.0, 5.0 }, 4.0);
 instance4 = new DenseInstance(new double[] { 3.0, 4.0, 5.0 }, 5.0);
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 DistanceMeasure dm = new EuclideanDistance();
 assertEquals(3, dataset.kNearest(3, instance3, dm).size());
 }

 @Test
 public void testFolds() {
 // Test creating folds for a dataset with 10 instances
 dataset = new DefaultDataset();
 instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 4.0);
 instance2 = new DenseInstance(new double[] { 2.0, 3.0, 4.0 }, 5.0);
 instance3 = new DenseInstance(new double[] { 1.0, 2.0, 5.0 }, 4.0);
 instance4 = new DenseInstance(new double[] { 3.0, 4.0, 5.0 }, 5.0);
 instance5 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 5.0);
 instance6 = new DenseInstance(new double[] { 5.0, 6.0, 7.0 }, 4.0);
 instance7 = new DenseInstance(new double[] { 6.0, 7.0, 8.0 }, 5.0);
 instance8 = new DenseInstance(new double[] { 7.0, 8.0, 9.0 }, 5.0);
 instance9 = new DenseInstance(new double[] { 8.0, 9.0, 10.0 }, 4.0);
 instance10 = new DenseInstance(new double[] { 9.0, 10.0, 11.0 }, 5.0);
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4, instance5, instance6, instance7, instance8, instance9, instance10));
 Random rg = new Random();
 Dataset[] folds = dataset.folds(5, rg);
 assertEquals(5, folds.length);
 for (Dataset fold : folds) {
 assertEquals(2, fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 // Test getting the number of attributes for an empty dataset
 dataset = new DefaultDataset();
 assertEquals(0, dataset.noAttributes());

 // Test getting the number of attributes for a dataset with instances of different number of attributes
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 // Test getting the class index for an instance with a known class
 dataset = new DefaultDataset();
 dataset.add(instance1);
 assertEquals(0, dataset.classIndex(instance1.classValue()));

 // Test getting the class index for an instance with an unknown class
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 assertNull(dataset.classIndex(null));
 }

 @Test
 public void testClassValue() {
 // Test getting the class value for an instance in a dataset with known classes
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance1.classValue(), dataset.classValue(0));

 // Test getting the class value for an instance in a dataset with an unknown class index
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 assertNull(dataset.classValue(1));
 }

 @Test
 public void testCopy() {
 // Test copying an empty dataset
 dataset = new DefaultDataset();
 Dataset copiedDataset = dataset.copy();
 assertTrue(copiedDataset.isEmpty());

 // Test copying a non-empty dataset
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 Dataset copiedDataset = dataset.copy();
 assertEquals(2, copiedDataset.size());
 }
}