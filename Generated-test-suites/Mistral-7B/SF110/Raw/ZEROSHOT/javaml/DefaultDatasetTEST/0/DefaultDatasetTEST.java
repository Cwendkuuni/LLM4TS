package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private Collection<Instance> instances;

 @Before
 public void setup() {
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 4.0);
 instance2 = new DenseInstance(new double[]{2.1, 3.1, 4.1}, 5.0);
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, 6.0);
 instances = new ArrayList<Instance>() {{
 add(instance1);
 add(instance2);
 add(instance3);
 }};
 dataset = new DefaultDataset(instances);
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertEquals(0, emptyDataset.size());
 assertEquals(0, emptyDataset.noAttributes());
 }

 @Test
 public void testAddAll() {
 DefaultDataset datasetCopy = new DefaultDataset();
 assertTrue(datasetCopy.addAll(instances));
 assertEquals(instances.size(), datasetCopy.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNullInstances() {
 DefaultDataset dataset = new DefaultDataset();
 assertFalse(dataset.addAll(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 assertFalse(dataset.addAll(Collections.singletonList(null)));
 }

 @Test
 public void testAddAllDifferentSizes() {
 DefaultDataset dataset = new DefaultDataset();
 Collection<Instance> biggerInstances = new ArrayList<Instance>() {{
 add(instance1);
 add(instance1);
 }};
 assertFalse(dataset.addAll(biggerInstances));
 }

 @Test
 public void testAdd() {
 DefaultDataset datasetCopy = new DefaultDataset();
 assertTrue(datasetCopy.add(instance1));
 assertEquals(1, datasetCopy.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 assertFalse(dataset.add(null));
 }

 @Test
 public void testClasses() {
 Set<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains(instance1.classValue()));
 assertTrue(classes.contains(instance2.classValue()));
 }

 @Test
 public void testClear() {
 DefaultDataset datasetCopy = new DefaultDataset(instances);
 datasetCopy.clear();
 assertEquals(0, datasetCopy.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testRemove() {
 DefaultDataset datasetCopy = new DefaultDataset(instances);
 datasetCopy.remove(instance1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 DefaultDataset datasetCopy = new DefaultDataset(instances);
 datasetCopy.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBiggerIndex() {
 DefaultDataset datasetCopy = new DefaultDataset(instances);
 datasetCopy.get(instances.size());
 }

 @Test
 public void testKNearest() {
 DefaultDataset dataset = new DefaultDataset(instances);
 Instance testInstance = new DenseInstance(new double[]{1.5, 2.5, 3.5}, 4.0);
 DistanceMeasure euclideanDistance = new EuclideanDistance();
 Set<Instance> result = dataset.kNearest(2, testInstance, euclideanDistance);
 assertTrue(result.size() == 2);
 assertTrue(result.contains(instance1));
 assertTrue(result.contains(instance2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestNullInstance() {
 DefaultDataset dataset = new DefaultDataset(instances);
 assertNull(dataset.kNearest(0, null, new EuclideanDistance()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestNullDistanceMeasure() {
 DefaultDataset dataset = new DefaultDataset(instances);
 assertNull(dataset.kNearest(0, instance1, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestInvalidK() {
 DefaultDataset dataset = new DefaultDataset(instances);
 assertNull(dataset.kNearest(-1, instance1, new EuclideanDistance()));
 }

 @Test
 public void testFolds() {
 DefaultDataset dataset = new DefaultDataset(instances);
 int numFolds = 3;
 Random random = new Random();
 Dataset[] folds = dataset.folds(numFolds, random);
 assertEquals(numFolds, folds.length);
 for (Dataset fold : folds) {
 assertEquals(dataset.size(), fold.size());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsInvalidNumberOfFolds() {
 DefaultDataset dataset = new DefaultDataset(instances);
 int numFolds = -1;
 assertNull(dataset.folds(numFolds, new Random()));
 }

 @Test(expected = NullPointerException.class)
 public void testFoldsNullRandom() {
 DefaultDataset dataset = new DefaultDataset(instances);
 int numFolds = 3;
 assertNull(dataset.folds(numFolds, null));
 }

 @Test
 public void testNoAttributes() {
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(dataset.classIndex(instance1.classValue()), 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClassIndexNullObject() {
 DefaultDataset dataset = new DefaultDataset(instances);
 assertNull(dataset.classValue(0));
 }

 @Test
 public void testCopy() {
 DefaultDataset originalDataset = new DefaultDataset(instances);
 DefaultDataset copiedDataset = originalDataset.copy();
 assertEquals(originalDataset.size(), copiedDataset.size());
 for (int i = 0; i < originalDataset.size(); i++) {
 Instance originalInstance = originalDataset.instance(i);
 Instance copiedInstance = copiedDataset.instance(i);
 assertArrayEquals(originalInstance.toArray(), copiedInstance.toArray());
 }
 }
}