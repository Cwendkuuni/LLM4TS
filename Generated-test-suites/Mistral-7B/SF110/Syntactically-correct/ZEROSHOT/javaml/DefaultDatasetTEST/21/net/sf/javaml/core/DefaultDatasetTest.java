package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.sf.javaml.distance.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private List<Instance> instances;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 instances = new LinkedList<>();
 instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, new Double[]{4.0});
 instance2 = new DenseInstance(new Double[]{2.0, 3.0, 4.0}, new Double[]{5.0});
 instance3 = new DenseInstance(new Double[]{3.0, 4.0, 5.0}, new Double[]{6.0});
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
 assertEquals(0, emptyDataset.size());
 }

 @Test
 public void testAddAll() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(instances);
 assertEquals(instances.size(), dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Collections.singleton(null));
 fail("IllegalArgumentException expected, but not thrown");
 }

 @Test
 public void testAdd() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);
 assertEquals(1, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(null);
 fail("IllegalArgumentException expected, but not thrown");
 }

 @Test
 public void testClasses() {
 SortedSet<Object> classes = dataset.classes();
 assertEquals(1, classes.size());
 assertEquals(instance1.classValue(), classes.first());
 }

 @Test
 public void testKNearest() {
 List<Instance> nearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, nearestInstances.size());
 assertTrue(nearestInstances.contains(instance1));
 assertTrue(nearestInstances.contains(instance2));
 }

 @Test
 public void testFolds() {
 Dataset[] folds = dataset.folds(3, new Random());
 assertEquals(3, folds.length);
 for (Dataset fold : folds) {
 assertTrue(fold instanceof Fold);
 assertEquals(dataset.size(), fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(1, dataset.classIndex(instance1.classValue()));
 }

 @Test
 public void testClassValue() {
 assertEquals(instance1.classValue(), dataset.classValue(0));
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(dataset.size(), copiedDataset.size());
 for (int i = 0; i < dataset.size(); ++i) {
 Instance datasetInstance = dataset.instance(i);
 Instance copiedInstance = copiedDataset.instance(i);
 assertArrayEquals(datasetInstance.toArray(), copiedInstance.toArray());
 }
 }
}