package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;

public class DefaultDatasetTest {

 private static final Instance TEST_INSTANCE_1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
 private static final Instance TEST_INSTANCE_2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1.0);
 private static final Instance TEST_INSTANCE_3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, 0.0);
 private static final Instance TEST_INSTANCE_4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1.0);
 private static final Instance TEST_INSTANCE_5 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);

 private static final DistanceMeasure EUCLIDEAN_DISTANCE = new EuclideanDistance();
 private static final Random RANDOM = new Random();

 @org.junit.Test
 public void testEmptyDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset();
 assertTrue(dataset.isEmpty());
 assertEquals(0, dataset.size());
 assertEquals(0, dataset.noAttributes());
 assertNull(dataset.classes());
 }

 @org.junit.Test
 public void testDefaultDatasetWithInstances() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 assertFalse(dataset.isEmpty());
 assertEquals(3, dataset.size());
 assertEquals(3, dataset.noAttributes());
 Set<Object> expectedClasses = new HashSet<>(Arrays.asList("0.0", "1.0"));
 Set<Object> actualClasses = dataset.classes();
 assertEquals(expectedClasses, actualClasses);
 }

 @org.junit.Test
 public void testAddAllInstancesToDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 assertFalse(dataset.isEmpty());
 assertEquals(3, dataset.size());
 assertEquals(3, dataset.noAttributes());
 Set<Object> expectedClasses = new HashSet<>(Arrays.asList("0.0", "1.0"));
 Set<Object> actualClasses = dataset.classes();
 assertEquals(expectedClasses, actualClasses);
 }

 @org.junit.Test
 public void testAddInstanceToEmptyDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(TEST_INSTANCE_1);
 assertFalse(dataset.isEmpty());
 assertEquals(1, dataset.size());
 assertEquals(3, dataset.noAttributes());
 Set<Object> expectedClasses = new HashSet<>(Arrays.asList("0.0"));
 Set<Object> actualClasses = dataset.classes();
 assertEquals(expectedClasses, actualClasses);
 }

 @org.junit.Test
 public void testAddInstanceToDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 dataset.add(TEST_INSTANCE_4);
 assertEquals(4, dataset.size());
 assertEquals(3, dataset.noAttributes());
 Set<Object> expectedClasses = new HashSet<>(Arrays.asList("0.0", "1.0"));
 Set<Object> actualClasses = dataset.classes();
 assertEquals(expectedClasses, actualClasses);
 }

 @org.junit.Test
 public void testClearDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 dataset.clear();
 assertTrue(dataset.isEmpty());
 assertEquals(0, dataset.size());
 assertEquals(0, dataset.noAttributes());
 assertNull(dataset.classes());
 }

 @org.junit.Test
 public void testkNearestWithOneInstance() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1));
 Set<Instance> kNearestInstances = dataset.kNearest(1, TEST_INSTANCE_1, EUCLIDEAN_DISTANCE);
 assertEquals(1, kNearestInstances.size());
 assertEquals(TEST_INSTANCE_1, kNearestInstances.iterator().next());
 }

 @org.junit.Test
 public void testkNearestWithTwoInstances() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2));
 Set<Instance> kNearestInstances = dataset.kNearest(1, TEST_INSTANCE_1, EUCLIDEAN_DISTANCE);
 assertEquals(1, kNearestInstances.size());
 assertEquals(TEST_INSTANCE_1, kNearestInstances.iterator().next());

 Set<Instance> kNearestInstances2 = dataset.kNearest(2, TEST_INSTANCE_1, EUCLIDEAN_DISTANCE);
 assertEquals(2, kNearestInstances2.size());
 assertTrue(kNearestInstances2.contains(TEST_INSTANCE_1));
 assertTrue(kNearestInstances2.contains(TEST_INSTANCE_2));
 }

 @org.junit.Test
 public void testkNearestWithThreeInstances() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 Set<Instance> kNearestInstances = dataset.kNearest(1, TEST_INSTANCE_1, EUCLIDEAN_DISTANCE);
 assertEquals(1, kNearestInstances.size());
 assertEquals(TEST_INSTANCE_1, kNearestInstances.iterator().next());

 Set<Instance> kNearestInstances2 = dataset.kNearest(2, TEST_INSTANCE_1, EUCLIDEAN_DISTANCE);
 assertEquals(2, kNearestInstances2.size());
 assertTrue(kNearestInstances2.contains(TEST_INSTANCE_1));
 assertTrue(kNearestInstances2.contains(TEST_INSTANCE_2));

 Set<Instance> kNearestInstances3 = dataset.kNearest(3, TEST_INSTANCE_1, EUCLIDEAN_DISTANCE);
 assertEquals(3, kNearestInstances3.size());
 assertTrue(kNearestInstances3.contains(TEST_INSTANCE_1));
 assertTrue(kNearestInstances3.contains(TEST_INSTANCE_2));
 assertTrue(kNearestInstances3.contains(TEST_INSTANCE_3));
 }

 @org.junit.Test
 public void testFoldsWithTwoInstancesAndThreeFolds() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2));
 Dataset[] folds = dataset.folds(3, RANDOM);
 assertEquals(3, folds.length);
 for (Dataset fold : folds) {
 assertEquals(1, fold.size());
 }
 }

 @org.junit.Test
 public void testFoldsWithThreeInstancesAndFourFolds() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 Dataset[] folds = dataset.folds(4, RANDOM);
 assertEquals(4, folds.length);
 for (Dataset fold : folds) {
 assertTrue(fold.size() > 1);
 }
 }

 @org.junit.Test
 public void testNoAttributesDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset();
 assertEquals(0, dataset.noAttributes());
 }

 @org.junit.Test
 public void testNoAttributesWithInstancesDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 assertEquals(3, dataset.noAttributes());
 }

 @org.junit.Test
 public void testClassIndexWithClass() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 assertEquals(0, dataset.classIndex("0.0"));
 assertEquals(1, dataset.classIndex("1.0"));
 }

 @org.junit.Test
 public void testClassIndexWithNull() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 assertEquals(-1, dataset.classIndex(null));
 }

 @org.junit.Test
 public void testClassValueWithIndex() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 assertEquals("0.0", dataset.classValue(0));
 assertEquals("1.0", dataset.classValue(1));
 }

 @org.junit.Test
 public void testClassValueWithInvalidIndex() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 assertNull(dataset.classValue(-1));
 }

 @org.junit.Test
 public void testCopyDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(TEST_INSTANCE_1, TEST_INSTANCE_2, TEST_INSTANCE_3));
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(3, copiedDataset.size());
 assertEquals(3, copiedDataset.noAttributes());
 Set<Object> expectedClasses = new HashSet<>(Arrays.asList("0.0", "1.0"));
 Set<Object> actualClasses = copiedDataset.classes();
 assertEquals(expectedClasses, actualClasses);
 }
}