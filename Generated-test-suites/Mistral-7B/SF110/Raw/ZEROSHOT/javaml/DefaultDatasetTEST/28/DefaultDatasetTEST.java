package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.distance.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1;
 private Instance instance2;
 private Instance instance3;
 private Collection<Instance> instances;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setup() {
 // Initialize instances
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{4.0, 5.0});
 instance1.setClassValue(1.0);

 instance2 = new DenseInstance(new double[]{2.0, 2.0, 3.0}, new double[]{4.0, 5.0});
 instance2.setClassValue(1.0);

 instance3 = new DenseInstance(new double[]{1.0, 2.0, 4.0}, new double[]{4.0, 5.0});
 instance3.setClassValue(0.0);

 // Initialize collection
 instances = new ArrayList<Instance>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);

 // Initialize distance measure
 distanceMeasure = new EuclideanDistance();

 // Initialize dataset
 dataset = new DefaultDataset();
 }

 @Test
 public void testDefaultDatasetEmpty() {
 // Test empty constructor
 DefaultDataset emptyDataset = new DefaultDataset();
 assertTrue(emptyDataset.isEmpty());
 assertEquals(0, emptyDataset.size());
 }

 @Test
 public void testDefaultDatasetWithCollection() {
 // Test constructor with collection
 dataset = new DefaultDataset(instances);
 assertFalse(dataset.isEmpty());
 assertEquals(3, dataset.size());

 // Test getClassValues
 SortedSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertEquals(1.0, classes.first());
 assertEquals(0.0, classes.last());

 // Test noAttributes
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testAddAll() {
 // Test addAll method
 dataset.clear();
 dataset.addAll(instances);
 assertFalse(dataset.isEmpty());
 assertEquals(3, dataset.size());

 // Test addAll with duplicate instances
 Collection<Instance> duplicates = new ArrayList<Instance>();
 duplicates.add(instance1);
 dataset.addAll(duplicates);
 assertEquals(3, dataset.size());
 }

 @Test
 public void testClear() {
 // Test clear method
 dataset.clear();
 assertTrue(dataset.isEmpty());
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAdd() {
 // Test add method
 dataset.clear();
 dataset.add(instance1);
 assertFalse(dataset.isEmpty());
 assertEquals(1, dataset.size());
 }

 @Test
 public void testAddAt() {
 // Test addAt method
 dataset.clear();
 dataset.add(instance1);
 dataset.add(instance2, 0);
 assertFalse(dataset.isEmpty());
 assertEquals(2, dataset.size());
 }

 @Test
 public void testSetElementAt() {
 // Test setElementAt method
 dataset.clear();
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.setElementAt(instance1, 0);
 assertFalse(dataset.isEmpty());
 assertEquals(2, dataset.size());
 assertEquals(instance1, dataset.instance(0));
 }

 @Test
 public void testGet() {
 // Test get method
 dataset.clear();
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance1, dataset.instance(0));
 assertEquals(instance2, dataset.instance(1));
 }

 @Test
 public void testClasses() {
 // Test classes method
 dataset.clear();
 dataset.addAll(instances);
 SortedSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertEquals(1.0, classes.first());
 assertEquals(0.0, classes.last());
 }

 @Test
 public void testKNearest() {
 // Test kNearest method
 // Initialize some more instances
 Instance instance4 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{4.0, 5.0});
 instance4.setClassValue(1.0);

 Instance instance5 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{4.0, 5.1});
 instance5.setClassValue(1.0);

 Instance instance6 = new DenseInstance(new double[]{1.0, 2.0, 4.0}, new double[]{4.1, 5.0});
 instance6.setClassValue(0.0);

 instances.add(instance4);
 instances.add(instance5);
 instances.add(instance6);
 dataset.clear();
 dataset.addAll(instances);

 // Test kNearest method with k = 2
 Set<Instance> neighbors = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, neighbors.size());
 assertTrue(neighbors.contains(instance1));
 assertTrue(neighbors.contains(instance2));
 }

 @Test
 public void testFolds() {
 // Test folds method
 dataset.clear();
 dataset.addAll(instances);
 Dataset[] folds = dataset.folds(5, new Random());
 assertEquals(5, folds.length);
 }

 @Test
 public void testNoAttributes() {
 // Test noAttributes method
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 // Test classIndex method
 assertEquals(1, dataset.classIndex(1.0));
 assertEquals(-1, dataset.classIndex(2.0));
 }

 @Test
 public void testClassValue() {
 // Test classValue method
 assertEquals(1.0, dataset.classValue(0));
 assertEquals(1.0, dataset.classValue(1));
 assertEquals(null, dataset.classValue(2));
 }

 @Test
 public void testCopy() {
 // Test copy method
 DefaultDataset copiedDataset = dataset.copy();
 assertFalse(copiedDataset.isEmpty());
 assertEquals(3, copiedDataset.size());
 }
}