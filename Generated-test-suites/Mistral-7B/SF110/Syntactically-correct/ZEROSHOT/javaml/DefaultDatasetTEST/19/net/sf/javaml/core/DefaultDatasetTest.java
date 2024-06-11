package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import net.sf.javaml.distance.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private DefaultDataset emptyDataset;
 private DefaultDataset copiedDataset;
 private Instance instance1;
 private Instance instance2;
 private Instance instance3;
 private Instance instance4;
 private Collection<Instance> instances;
 private DistanceMeasure distanceMeasure;

 @Test
 public void testDefaultDataset() {
 dataset = new DefaultDataset();
 assertEquals(0, dataset.size());
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testDefaultDatasetWithInstances() {
 instances = new ArrayList<Instance>();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"class1"});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new String[]{"class2"});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new String[]{"class1"});
 instance4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"class2"});
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 instances.add(instance4);

 dataset = new DefaultDataset(instances);
 assertEquals(4, dataset.size());
 assertFalse(dataset.isEmpty());
 }

 @Test
 public void testDefaultDatasetAddAll() {
 instances = new ArrayList<Instance>();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"class1"});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new String[]{"class2"});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new String[]{"class1"});
 instance4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"class2"});
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 instances.add(instance4);

 dataset = new DefaultDataset();
 assertTrue(dataset.addAll(instances));
 assertEquals(4, dataset.size());
 assertFalse(dataset.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDefaultDatasetAddAllThrowsException() {
 instances = new ArrayList<Instance>();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"class1"});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new String[]{"class2"});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new String[]{"class1"});
 instance4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"class2"});
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 instances.add(instance4);

 dataset = new DefaultDataset();
 try {
 dataset.addAll(instances);
 } catch (UnsupportedOperationException e) {
 throw new AssertionError("Should not reach this point.", e);
 }
 }

 @Test
 public void testDefaultDatasetAdd() {
 dataset = new DefaultDataset();
 assertTrue(dataset.add(instance1));
 assertEquals(1, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultDatasetAddThrowsException() {
 dataset = new DefaultDataset();
 try {
 dataset.add(null);
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Should not reach this point.", e);
 }
 }

 @Test
 public void testDefaultDatasetClear() {
 dataset = new DefaultDataset();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"class1"});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new String[]{"class2"});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new String[]{"class1"});
 instance4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"class2"});
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 dataset.add(instance4);
 dataset.clear();
 assertEquals(0, dataset.size());
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testDefaultDatasetClasses() {
 instances = new ArrayList<Instance>();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"class1"});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new String[]{"class2"});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new String[]{"class1"});
 instance4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"class2"});
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 instances.add(instance4);

 dataset = new DefaultDataset(instances);
 SortedSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains("class1"));
 assertTrue(classes.contains("class2"));
 }

 @Test
 public void testDefaultDatasetCopy() {
 dataset = new DefaultDataset();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"class1"});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new String[]{"class2"});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new String[]{"class1"});
 instance4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"class2"});
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 dataset.add(instance4);

 copiedDataset = (DefaultDataset) dataset.copy();
 assertEquals(4, copiedDataset.size());
 assertFalse(copiedDataset.isEmpty());
 }

 @Test
 public void testDefaultDatasetFolds() {
 instances = new ArrayList<Instance>();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"class1"});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new String[]{"class2"});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new String[]{"class1"});
 instance4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"class2"});
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 instances.add(instance4);

 dataset = new DefaultDataset(instances);
 Dataset[] folds = dataset.folds(5, new Random());
 assertEquals(5, folds.length);
 for (Dataset fold : folds) {
 assertEquals(dataset.size() / 5, fold.size());
 }
 }

 @Test
 public void testDefaultDatasetInstance() {
 dataset = new DefaultDataset();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"class1"});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new String[]{"class2"});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new String[]{"class1"});
 instance4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"class2"});
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 dataset.add(instance4);

 assertEquals(instance1, dataset.instance(0));
 assertEquals(instance2, dataset.instance(1));
 assertEquals(instance3, dataset.instance(2));
 assertEquals(instance4, dataset.instance(3));
 }

 @Test
 public void testDefaultDatasetKNearest() {
 distanceMeasure = new EuclideanDistance();
 instances = new ArrayList<Instance>();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"class1"});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new String[]{"class2"});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new String[]{"class1"});
 instance4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"class2"});
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 instances.add(instance4);

 dataset = new DefaultDataset(instances);
 Instance testInstance = new DenseInstance(new double[]{2.5, 3.5, 4.5}, new String[]{"class1"});
 Set<Instance> expectedNearestInstances = new HashSet<Instance>();
 expectedNearestInstances.add(instance1);
 expectedNearestInstances.add(instance3);
 Set<Instance> actualNearestInstances = dataset.kNearest(2, testInstance, distanceMeasure);
 assertEquals(expectedNearestInstances, actualNearestInstances);
 }

 @Test
 public void testDefaultDatasetMaxAttributes() {
 instances = new ArrayList<Instance>();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"class1"});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new String[]{"class2"});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new String[]{"class1"});
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);

 dataset = new DefaultDataset(instances);
 assertEquals(4, dataset.maxAttributes());
 }

 @Test
 public void testDefaultDatasetNoAttributes() {
 dataset = new DefaultDataset();
 assertEquals(0, dataset.noAttributes());
 }

 @Test
 public void testDefaultDatasetSize() {
 dataset = new DefaultDataset();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testDefaultDatasetAddAllWithEmptyCollection() {
 dataset = new DefaultDataset();
 assertTrue(dataset.addAll(Collections.emptyList()));
 assertEquals(0, dataset.size());
 }

 @Test
 public void testDefaultDatasetAddWithNullInstance() {
 dataset = new DefaultDataset();
 try {
 dataset.add(null);
 } catch (Exception e) {
 fail("Expected no exception");
 }
 }

 @Test
 public void testDefaultDatasetAddWithInstanceWithoutClassValue() {
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 dataset = new DefaultDataset();
 try {
 dataset.add(instance1);
 } catch (IllegalArgumentException e) {
 assertEquals("Instance must have a class value", e.getMessage());
 }
 }
}