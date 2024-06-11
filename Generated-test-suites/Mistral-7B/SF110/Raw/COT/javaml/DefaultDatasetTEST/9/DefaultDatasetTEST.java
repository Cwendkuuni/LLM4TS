package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset createEmptyDefaultDataset() {
 return new DefaultDataset();
 }

 private DefaultDataset createDefaultDataset(Collection<Instance> instances) {
 return new DefaultDataset(instances);
 }

 @Test
 public void testDefaultDatasetConstructorEmpty() {
 DefaultDataset ds = createEmptyDefaultDataset();
 assertEquals(0, ds.size());
 assertEquals(0, ds.maxAttributes);
 assertNotNull(ds.classes());
 assertTrue(ds.classes().isEmpty());
 }

 @Test
 public void testDefaultDatasetConstructorWithInstances() {
 Collection<Instance> instances = new ArrayList<>();
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0});
 Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0});
 instances.add(instance1);
 instances.add(instance2);
 DefaultDataset ds = createDefaultDataset(instances);
 assertEquals(2, ds.size());
 assertEquals(3, ds.maxAttributes);
 assertNotNull(ds.classes());
 assertFalse(ds.classes().isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testAddAllNullCollection() {
 DefaultDataset ds = createEmptyDefaultDataset();
 ds.addAll(null);
 }

 @Test
 public void testAddAllValidCollection() {
 Collection<Instance> instances = new ArrayList<>();
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0});
 Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0});
 instances.add(instance1);
 instances.add(instance2);
 DefaultDataset ds = createEmptyDefaultDataset();
 ds.addAll(instances);
 assertEquals(2, ds.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 DefaultDataset ds = createEmptyDefaultDataset();
 ds.add(null);
 }

 @Test
 public void testAddValidInstance() {
 Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0});
 DefaultDataset ds = createEmptyDefaultDataset();
 ds.add(instance);
 assertEquals(1, ds.size());
 }

 @Test
 public void testClear() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 ds.clear();
 assertEquals(0, ds.size());
 }

 @Test(expected = ClassCastException.class)
 public void testKNearestNullInstance() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, null)));
 Instance inst = null;
 try {
 ds.kNearest(1, inst, new EuclideanDistance());
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Expected NullPointerException");
 }
 }

 @Test
 public void testKNearestValidInstance() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 Instance queryInstance = new DenseInstance(new double[]{3.5, 3.5, 3.5}, new double[]{0.0});
 Set<Instance> result = ds.kNearest(1, queryInstance, new EuclideanDistance());
 assertEquals(1, result.size());
 Instance nearestInstance = result.iterator().next();
 assertEquals(queryInstance.getClass(), nearestInstance.getClass());
 }

 @Test
 public void testFoldsValidArguments() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 Dataset[] folds = ds.folds(3, new Random());
 assertEquals(3, folds.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsInvalidNumberOfFolds() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 ds.folds(-1, new Random());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsNullRandom() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 ds.folds(3, null);
 }

 @Test
 public void testNoAttributes() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 assertEquals(3, ds.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassIndexOutOfBounds() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 ds.classIndex(-1);
 }

 @Test
 public void testClassIndexValidIndex() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 assertEquals(1, ds.classIndex(Instance.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClassValueNullIndex() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 ds.classValue(-1);
 }

 @Test
 public void testClassValueValidIndex() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 assertEquals(Instance.class, ds.classValue(0).getClass());
 }

 @Test
 public void testCopy() {
 DefaultDataset ds = createDefaultDataset(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0})));
 Dataset copiedDs = ds.copy();
 assertEquals(ds.size(), copiedDs.size());
 }
}