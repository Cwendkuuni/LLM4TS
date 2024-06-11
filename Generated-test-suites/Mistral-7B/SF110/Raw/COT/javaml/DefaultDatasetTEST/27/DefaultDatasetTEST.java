package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.distance.*;
import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 // Create instances and dataset
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 3.0);
 instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 5.0);
 instance3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, 7.0);

 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);

 dataset = new DefaultDataset(instances);
 distanceMeasure = new EuclideanDistance();
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertTrue(emptyDataset.isEmpty());
 }

 @Test
 public void testAddAll() {
 // Test adding instances to an empty dataset
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);

 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(3, dataset.size());

 Instance expectedInstance = instance1;
 Instance actualInstance = dataset.instance(0);
 assertTrue(expectedInstance.equals(actualInstance));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllWithNullInstances() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllWithNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Collections.singletonList(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllWithDuplicateInstances() {
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance1);

 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(1, dataset.size());
 }

 @Test
 public void testAdd() {
 // Test adding an instance to an empty dataset
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);

 assertEquals(1, dataset.size());
 Instance expectedInstance = instance1;
 Instance actualInstance = dataset.instance(0);
 assertTrue(expectedInstance.equals(actualInstance));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(null);
 }

 @Test
 public void testClasses() {
 Set<Object> expectedClasses = new TreeSet<>();
 expectedClasses.add(instance1.classValue());
 expectedClasses.add(instance2.classValue());
 expectedClasses.add(instance3.classValue());

 Set<Object> actualClasses = dataset.classes();
 assertEquals(expectedClasses, actualClasses);
 }

 @Test
 public void testClear() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Collections.singletonList(instance1));

 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 Random random = new Random();
 Dataset[] expectedFolds = new Dataset[numFolds];
 Dataset fold;

 for (int i = 0; i < numFolds; ++i) {
 fold = dataset.folds(numFolds, random);
 expectedFolds[i] = fold;
 }

 Dataset[] actualFolds = dataset.folds(numFolds, random);
 assertArrayEquals(expectedFolds, actualFolds);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsWithNegativeNumFolds() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.folds(-1, new Random());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsWithNullRandom() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.folds(3, null);
 }

 @Test
 public void testGet() {
 int index = 0;
 Instance expectedInstance = instance1;
 Instance actualInstance = dataset.instance(index);
 assertTrue(expectedInstance.equals(actualInstance));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithNegativeIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Collections.singletonList(instance1));

 dataset.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithSizeIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Collections.singletonList(instance1));

 dataset.get(dataset.size());
 }

 @Test
 public void testInstance() {
 assertEquals(instance1, dataset.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceWithNegativeIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Collections.singletonList(instance1));

 dataset.instance(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceWithSizeIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Collections.singletonList(instance1));

 dataset.instance(dataset.size());
 }

 @Test
 public void testKNearest() {
 int k = 2;
 Set<Instance> expectedNearestInstances = new TreeSet<>();
 expectedNearestInstances.add(instance1);
 expectedNearestInstances.add(instance2);

 Set<Instance> actualNearestInstances = dataset.kNearest(k, instance3, distanceMeasure);
 assertEquals(expectedNearestInstances, actualNearestInstances);
 }

 @Test(expected = NullPointerException.class)
 public void testKNearestWithNullInstance() {
 Set<Instance> nearestInstances = dataset.kNearest(1, null, distanceMeasure);
 }

 @Test(expected = NullPointerException.class)
 public void testKNearestWithNullDistanceMeasure() {
 Set<Instance> nearestInstances = dataset.kNearest(1, instance3, null);
 }

 @Test
 public void testNoAttributes() {
 Instance instance = instance1;
 int expectedNumberOfAttributes = instance.noAttributes();
 int actualNumberOfAttributes = dataset.noAttributes();
 assertEquals(expectedNumberOfAttributes, actualNumberOfAttributes);
 }

 @Test
 public void testSize() {
 int expectedSize = 3;
 int actualSize = dataset.size();
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testClassIndex() {
 Object expectedClass = instance1.classValue();
 int actualClassIndex = dataset.classIndex(expectedClass);
 assertEquals(0, actualClassIndex);
 }

 @Test(expected = NullPointerException.class)
 public void testClassIndexWithNullClass() {
 int classIndex = dataset.classIndex(null);
 }

 @Test
 public void testCopy() {
 DefaultDataset expectedCopy = new DefaultDataset();
 expectedCopy.addAll(Collections.singletonList(instance1.copy()));
 expectedCopy.addAll(Collections.singletonList(instance2.copy()));
 expectedCopy.addAll(Collections.singletonList(instance3.copy()));

 DefaultDataset actualCopy = dataset.copy();
 assertEquals(expectedCopy, actualCopy);
 }
}