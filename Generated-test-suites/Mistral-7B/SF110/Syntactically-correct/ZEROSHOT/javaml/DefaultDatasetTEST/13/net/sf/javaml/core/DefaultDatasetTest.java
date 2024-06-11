package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.distance.*;
import org.junit.Test;

public class DefaultDatasetTest {

 @Test
 public void testDefaultDatasetEmptyConstructor() {
 DefaultDataset dataset = new DefaultDataset();
 assertEquals(0, dataset.size());
 assertNull(dataset.classes());
 }

 @Test
 public void testDefaultDatasetCollectionConstructor() {
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
 DefaultDataset dataset = new DefaultDataset(instances);
 assertEquals(2, dataset.size());
 TreeSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains(0));
 assertTrue(classes.contains(1));
 }

 @Test
 public void testAddAll() {
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
 DefaultDataset dataset = new DefaultDataset();
 assertTrue(dataset.addAll(instances));
 assertEquals(2, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNullCollection() {
 DefaultDataset dataset = new DefaultDataset();
 assertFalse(dataset.addAll(null));
 }

 @Test
 public void testAdd() {
 Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0);
 DefaultDataset dataset = new DefaultDataset();
 assertTrue(dataset.add(instance));
 assertEquals(1, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 assertFalse(dataset.add(null));
 }

 @Test
 public void testClear() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 dataset.clear();
 assertEquals(0, dataset.size());
 assertNull(dataset.classes());
 }

 @Test
 public void testRemove() {
 DefaultDataset dataset = new DefaultDataset();
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0);
 Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1);
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.remove(instance1);
 assertEquals(1, dataset.size());
 assertFalse(dataset.contains(instance1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0);
 dataset.remove(null);
 }

 @Test
 public void testClasses() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
 TreeSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains(0));
 assertTrue(classes.contains(1));
 }

 @Test
 public void testKNearest() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 dataset.add(new DenseInstance(new double[]{1.5, 2.5, 3.5}, 0));
 dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
 Instance testInstance = new DenseInstance(new double[]{2.5, 3.5, 4.5}, 0);
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 Set<Instance> nearestInstances = dataset.kNearest(2, testInstance, distanceMeasure);
 assertEquals(2, nearestInstances.size());
 assertTrue(nearestInstances.contains(new DenseInstance(new double[]{1.5, 2.5, 3.5}, 0)));
 assertTrue(nearestInstances.contains(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0)));
 }

 @Test
 public void testFolds() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 dataset.add(new DenseInstance(new double[]{1.5, 2.5, 3.5}, 0));
 dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
 Dataset[] folds = dataset.folds(3, new Random());
 assertEquals(3, folds.length);
 for (Dataset fold : folds) {
 assertTrue(fold instanceof Fold);
 }
 }

 @Test
 public void testNoAttributes() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
 assertEquals(0, dataset.classIndex(0));
 assertEquals(1, dataset.classIndex(1));
 }

 @Test
 public void testClassValue() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
 assertEquals(0, dataset.classValue(0).hashCode());
 assertEquals(1, dataset.classValue(1).hashCode());
 }

 @Test
 public void testCopy() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(1, copiedDataset.size());
 }
}