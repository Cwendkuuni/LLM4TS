package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.distance.*;
import org.junit.Test;

public class DefaultDatasetTest {

 DefaultDataset dataset = new DefaultDataset();
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
 Instance instance2 = new DenseInstance(new double[]{3.0, 2.0, 1.0}, 2.0);
 Instance instance3 = new DenseInstance(new double[]{2.0, 1.0, 1.0}, 3.0);

 @Test
 public void testDefaultConstructor() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertEquals(0, emptyDataset.size());
 }

 @Test
 public void testCollectionConstructor() {
 ArrayList<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 DefaultDataset datasetWithInstances = new DefaultDataset(instances);
 assertEquals(3, datasetWithInstances.size());
 }

 @Test
 public void testAddAll() {
 ArrayList<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 DefaultDataset datasetWithInstances = new DefaultDataset();
 datasetWithInstances.addAll(instances);
 assertEquals(3, datasetWithInstances.size());
 }

 @Test
 public void testClear() {
 DefaultDataset datasetWithInstances = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
 datasetWithInstances.clear();
 assertEquals(0, datasetWithInstances.size());
 }

 @Test
 public void testAdd() {
 DefaultDataset datasetWithInstances = new DefaultDataset();
 datasetWithInstances.add(instance1);
 assertEquals(1, datasetWithInstances.size());
 }

 @Test
 public void testAdd_index() {
 DefaultDataset datasetWithInstances = new DefaultDataset();
 datasetWithInstances.add(instance1);
 datasetWithInstances.add(3, instance2);
 assertEquals(2, datasetWithInstances.size());
 }

 @Test
 public void testElementAt() {
 DefaultDataset datasetWithInstances = new DefaultDataset();
 datasetWithInstances.add(instance1);
 datasetWithInstances.add(instance2);
 assertEquals(instance2, datasetWithInstances.elementAt(1));
 }

 @Test
 public void testGet() {
 DefaultDataset datasetWithInstances = new DefaultDataset();
 datasetWithInstances.add(instance1);
 datasetWithInstances.add(instance2);
 assertEquals(instance2, datasetWithInstances.get(1));
 }

 @Test
 public void testClasses() {
 DefaultDataset datasetWithInstances = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
 Set<Object> classes = datasetWithInstances.classes();
 assertEquals(3, classes.size());
 }

 @Test
 public void testKNearest() {
 DefaultDataset datasetWithInstances = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 Instance testInstance = new DenseInstance(new double[]{2.5, 2.5, 2.5});
 Set<Instance> expectedNearestInstances = new HashSet<>(Arrays.asList(instance1, instance3));
 Set<Instance> actualNearestInstances = datasetWithInstances.kNearest(2, testInstance, distanceMeasure);
 assertEquals(expectedNearestInstances, actualNearestInstances);
 }

 @Test
 public void testFolds() {
 DefaultDataset datasetWithInstances = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
 Random random = new Random();
 Dataset[] expectedFolds = new Dataset[4];
 expectedFolds[0] = new DefaultDataset(Arrays.asList(instance2, instance3));
 expectedFolds[1] = new DefaultDataset(Arrays.asList(instance1, instance3));
 expectedFolds[2] = new DefaultDataset(Arrays.asList(instance1, instance2));
 expectedFolds[3] = new DefaultDataset(Arrays.asList(instance3, instance1));
 Dataset[] actualFolds = datasetWithInstances.folds(4, random);
 assertArrayEquals(expectedFolds, actualFolds);
 }

 @Test
 public void testNoAttributes() {
 Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
 DefaultDataset datasetWithInstance = new DefaultDataset();
 datasetWithInstance.add(instance);
 assertEquals(instance.noAttributes(), datasetWithInstance.noAttributes());
 }

 @Test
 public void testClassIndex() {
 DefaultDataset datasetWithInstances = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
 assertEquals(1, datasetWithInstances.classIndex(instance2.classValue()));
 }

 @Test
 public void testClassValue() {
 DefaultDataset datasetWithInstances = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
 assertEquals(instance2.classValue(), datasetWithInstances.classValue(1));
 }

 @Test
 public void testCopy() {
 DefaultDataset datasetWithInstances = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
 DefaultDataset copy = datasetWithInstances.copy();
 assertEquals(3, copy.size());
 }
}