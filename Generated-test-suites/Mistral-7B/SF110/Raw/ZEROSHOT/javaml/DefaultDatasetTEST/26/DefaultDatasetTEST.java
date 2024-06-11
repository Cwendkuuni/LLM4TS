package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceFunction;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.distance.SquareFormDistance;

public class DefaultDatasetTest {

 private static final DistanceMeasure EUCLIDEAN = new EuclideanDistance();
 private static final DistanceMeasure SQUARE_FORM = new SquareFormDistance();

 @org.junit.Test
 public void testDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset();
 assertTrue(dataset.isEmpty());
 assertEquals(0, dataset.size());
 assertEquals(0, dataset.noAttributes());
 assertNull(dataset.classes());
 }

 @org.junit.Test
 public void testDefaultDatasetWithInstances() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 instances.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));

 DefaultDataset dataset = new DefaultDataset(instances);
 assertTrue(dataset.size() == 3);
 assertEquals(3, dataset.noAttributes());
 SortedSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.containsAll(Arrays.asList("class1", "class2")));
 }

 @org.junit.Test
 public void testAddAll() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 instances.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(3, dataset.size());
 assertEquals(3, dataset.noAttributes());
 SortedSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.containsAll(Arrays.asList("class1", "class2")));
 }

 @org.junit.Test
 public void testAdd() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(instances);
 Instance newInstance = new DenseInstance(new double[]{10.0, 20.0, 30.0}, "class3");
 dataset.add(newInstance);

 assertEquals(4, dataset.size());
 assertEquals(3, dataset.noAttributes());
 SortedSet<Object> classes = dataset.classes();
 assertEquals(3, classes.size());
 assertTrue(classes.containsAll(Arrays.asList("class1", "class2", "class3")));
 }

 @org.junit.Test
 public void testClear() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 instances.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));
 DefaultDataset dataset = new DefaultDataset(instances);
 dataset.clear();

 assertTrue(dataset.isEmpty());
 assertEquals(0, dataset.size());
 assertEquals(0, dataset.noAttributes());
 assertNull(dataset.classes());
 }

 @org.junit.Test
 public void testAddAllWithDuplicateInstances() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(3, dataset.size());
 assertEquals(3, dataset.noAttributes());
 SortedSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.containsAll(Arrays.asList("class1", "class2")));
 }

 @org.junit.Test
 public void testAddAllWithNullInstance() {
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"), null);
 DefaultDataset dataset = new DefaultDataset();
 assertFalse(dataset.addAll(instances));
 assertEquals(0, dataset.size());
 }

 @org.junit.Test
 public void testAddAllWithNullClass() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, null));
 DefaultDataset dataset = new DefaultDataset();
 assertFalse(dataset.addAll(instances));
 assertEquals(1, dataset.size());
 }

 @org.junit.Test
 public void testAddAllWithInstanceWithNullClassValue() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, null));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(instances);
 assertNotNull(dataset.classes());
 assertEquals(1, dataset.classes().size());
 assertEquals("class2", dataset.classes().first());
 }

 @org.junit.Test
 public void testContainsInstance() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 assertTrue(dataset.contains(instances.get(0)));
 assertFalse(dataset.contains(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class3")));
 assertFalse(dataset.contains(new DenseInstance(new double[]{5.0, 5.0, 5.0}, "class2")));
 }

 @org.junit.Test
 public void testContainsInstanceWithNullClassValue() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, null));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 assertFalse(dataset.contains(new DenseInstance(new double[]{1.0, 2.0, 3.0}, null)));
 assertTrue(dataset.contains(instances.get(0)));
 }

 @org.junit.Test
 public void testSetElementAt() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 Instance newInstance = new DenseInstance(new double[]{10.0, 20.0, 30.0}, "class3");
 dataset.setElementAt(newInstance, 0);

 assertEquals(1, dataset.size());
 assertEquals("class3", dataset.instance(0).classValue());
 }

 @org.junit.Test
 public void testGetInstance() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 Instance instance = dataset.getInstance(0);
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(1.0, instance.value(1), 0.001);
 assertEquals(1.0, instance.value(2), 0.001);
 assertEquals("class1", instance.classValue());
 }

 @org.junit.Test
 public void testClasses() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 assertEquals(2, dataset.classes().size());
 assertTrue(dataset.classes().contains("class1"));
 assertTrue(dataset.classes().contains("class2"));
 }

 @org.junit.Test
 public void testKNearestWithEuclideanDistance() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 instances.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));
 instances.add(new DenseInstance(new double[]{10.0, 11.0, 12.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 Instance testInstance = new DenseInstance(new double[]{6.0, 6.0, 6.0}, "class1");
 List<Instance> kNearest = dataset.kNearest(2, testInstance, EUCLIDEAN);

 assertEquals(2, kNearest.size());
 assertTrue(kNearest.contains(instances.get(0)));
 assertTrue(kNearest.contains(instances.get(2)));
 }

 @org.junit.Test
 public void testKNearestWithSquareFormDistance() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 instances.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));
 instances.add(new DenseInstance(new double[]{10.0, 11.0, 12.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 Instance testInstance = new DenseInstance(new double[]{6.0, 6.0, 6.0}, "class1");
 List<Instance> kNearest = dataset.kNearest(2, testInstance, SQUARE_FORM);

 assertEquals(2, kNearest.size());
 assertTrue(kNearest.contains(instances.get(0)));
 assertTrue(kNearest.contains(instances.get(2)));
 }

 @org.junit.Test
 public void testFolds() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 instances.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));
 instances.add(new DenseInstance(new double[]{10.0, 11.0, 12.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 int numFolds = 3;
 Dataset[] folds = dataset.folds(numFolds, new Random());

 assertEquals(numFolds, folds.length);
 for (Dataset fold : folds) {
 assertEquals(instances.size(), fold.size());
 }
 }

 @org.junit.Test
 public void testNoAttributes() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 assertEquals(3, dataset.noAttributes());
 }

 @org.junit.Test
 public void testClassIndex() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 assertEquals(0, dataset.classIndex("class1"));
 assertEquals(1, dataset.classIndex("class2"));
 assertEquals(-1, dataset.classIndex(null));
 }

 @org.junit.Test
 public void testClassValue() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 assertEquals("class1", dataset.classValue(0));
 assertEquals("class2", dataset.classValue(1));
 assertEquals(null, dataset.classValue(2));
 }

 @org.junit.Test
 public void testCopy() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 DefaultDataset dataset = new DefaultDataset(instances);

 DefaultDataset copy = dataset.copy();

 assertEquals(2, copy.size());
 assertEquals(3, copy.noAttributes());
 assertTrue(copy.classes().containsAll(Arrays.asList("class1", "class2")));
 }
}