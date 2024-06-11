package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset createEmptyDataset() {
 return new DefaultDataset();
 }

 private DefaultDataset createDatasetWithInstances(Instance... instances) {
 DefaultDataset dataset = new DefaultDataset();
 for (Instance instance : instances) {
 dataset.add(instance);
 }
 return dataset;
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset dataset = new DefaultDataset();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testCollectionConstructor() {
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[] {1.0, 2.0}, 3.0));
 instances.add(new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 DefaultDataset dataset = new DefaultDataset(instances);
 assertEquals(2, dataset.size());
 }

 @Test
 public void testAddAll() {
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[] {1.0, 2.0}, 3.0));
 instances.add(new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 DefaultDataset dataset = new DefaultDataset();
 boolean result = dataset.addAll(instances);
 assertEquals(true, result);
 assertEquals(2, dataset.size());
 }

 @Test
 public void testClear() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] {1.0, 2.0}, 3.0), new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAdd() {
 Instance instance = new DenseInstance(new double[] {1.0, 2.0}, 3.0);
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testSize() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] {1.0, 2.0}, 3.0), new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 assertEquals(2, dataset.size());
 }

 @Test
 public void testInstance() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] {1.0, 2.0}, 3.0), new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 Instance instance = dataset.instance(0);
 assertEquals(instance.classValue(), dataset.classValue(0));
 }

 @Test
 public void testClasses() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] {1.0, 2.0}, 3.0), new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 SortedSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertEquals(3.0, classes.first());
 assertEquals(2.0, classes.last());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 Instance nullInstance = null;
 dataset.add(nullInstance);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInstanceWithMoreAttributes() {
 DefaultDataset dataset = new DefaultDataset();
 Instance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 3.0);
 dataset.add(instance);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetElementAt() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] {1.0, 2.0}, 3.0), new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 Instance newInstance = new DenseInstance(new double[] {1.0, 2.0}, 1.0);
 dataset.setElementAt(newInstance, 0);
 fail("Should throw an UnsupportedOperationException");
 }

 @Test
 public void testRemove() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] {1.0, 2.0}, 3.0), new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 assertEquals(2, dataset.size());
 dataset.remove(1);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testNoAttributes() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] {1.0, 2.0}, 3.0), new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 assertEquals(2, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] {1.0, 2.0}, 3.0), new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 assertEquals(1, dataset.classIndex(3.0));
 assertEquals(-1, dataset.classIndex(null));
 }

 @Test
 public void testClassValue() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] {1.0, 2.0}, 3.0), new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 assertEquals(3.0, dataset.classValue(0));
 assertEquals(2.0, dataset.classValue(1));
 assertEquals(null, dataset.classValue(2));
 }

 @Test
 public void testCopy() {
 DefaultDataset originalDataset = createDatasetWithInstances(new DenseInstance(new double[] {1.0, 2.0}, 3.0), new DenseInstance(new double[] {4.0, 5.0}, 2.0));
 DefaultDataset copyDataset = originalDataset.copy();
 assertEquals(originalDataset.size(), copyDataset.size());
 for (int i = 0; i < originalDataset.size(); i++) {
 Instance originalInstance = originalDataset.instance(i);
 Instance copyInstance = copyDataset.instance(i);
 assertArrayEquals(originalInstance.toArray(), copyInstance.toArray());
 }
 }

 @Test
 public void testKNearestWithValidParameters() {
 DefaultDataset dataset = createDatasetWithInstances(
 new DenseInstance(new double[] {1.0, 1.0}, 3.0),
 new DenseInstance(new double[] {1.1, 1.1}, 3.0),
 new DenseInstance(new double[] {1.2, 1.2}, 2.0),
 new DenseInstance(new double[] {1.3, 1.3}, 1.0),
 new DenseInstance(new double[] {1.4, 1.4}, 3.0),
 new DenseInstance(new double[] {1.5, 1.5}, 3.0));
 Instance testInstance = new DenseInstance(new double[] {1.25, 1.25}, null);
 DistanceMeasure euclideanDistance = new EuclideanDistance();
 Set<Instance> nearestInstances = dataset.kNearest(3, testInstance, euclideanDistance);
 assertEquals(3, nearestInstances.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestWithNullTestInstance() {
 DefaultDataset dataset = createDatasetWithInstances(
 new DenseInstance(new double[] {1.0, 1.0}, 3.0),
 new DenseInstance(new double[] {1.1, 1.1}, 3.0),
 new DenseInstance(new double[] {1.2, 1.2}, 2.0),
 new DenseInstance(new double[] {1.3, 1.3}, 1.0),
 new DenseInstance(new double[] {1.4, 1.4}, 3.0),
 new DenseInstance(new double[] {1.5, 1.5}, 3.0));
 Instance nullTestInstance = null;
 Set<Instance> nearestInstances = dataset.kNearest(3, nullTestInstance, new EuclideanDistance());
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestWithInvalidK() {
 DefaultDataset dataset = createDatasetWithInstances(
 new DenseInstance(new double[] {1.0, 1.0}, 3.0),
 new DenseInstance(new double[] {1.1, 1.1}, 3.0),
 new DenseInstance(new double[] {1.2, 1.2}, 2.0),
 new DenseInstance(new double[] {1.3, 1.3}, 1.0),
 new DenseInstance(new double[] {1.4, 1.4}, 3.0),
 new DenseInstance(new double[] {1.5, 1.5}, 3.0));
 Instance testInstance = new DenseInstance(new double[] {1.25, 1.25}, null);
 Set<Instance> nearestInstances = dataset.kNearest(-1, testInstance, new EuclideanDistance());
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestWithNullDistanceMeasure() {
 DefaultDataset dataset = createDatasetWithInstances(
 new DenseInstance(new double[] {1.0, 1.0}, 3.0),
 new DenseInstance(new double[] {1.1, 1.1}, 3.0),
 new DenseInstance(new double[] {1.2, 1.2}, 2.0),
 new DenseInstance(new double[] {1.3, 1.3}, 1.0),
 new DenseInstance(new double[] {1.4, 1.4}, 3.0),
 new DenseInstance(new double[] {1.5, 1.5}, 3.0));
 Instance testInstance = new DenseInstance(new double[] {1.25, 1.25}, null);
 Set<Instance> nearestInstances = dataset.kNearest(3, testInstance, null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testFoldsWithValidParameters() {
 DefaultDataset dataset = createDatasetWithInstances(
 new DenseInstance(new double[] {1.0, 1.0}, 3.0),
 new DenseInstance(new double[] {1.1, 1.1}, 3.0),
 new DenseInstance(new double[] {1.2, 1.2}, 2.0),
 new DenseInstance(new double[] {1.3, 1.3}, 1.0),
 new DenseInstance(new double[] {1.4, 1.4}, 3.0),
 new DenseInstance(new double[] {1.5, 1.5}, 3.0));
 Dataset[] folds = dataset.folds(5, new Random());
 assertEquals(5, folds.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsWithInvalidNumberOfFolds() {
 DefaultDataset dataset = createDatasetWithInstances(
 new DenseInstance(new double[] {1.0, 1.0}, 3.0),
 new DenseInstance(new double[] {1.1, 1.1}, 3.0),
 new DenseInstance(new double[] {1.2, 1.2}, 2.0),
 new DenseInstance(new double[] {1.3, 1.3}, 1.0),
 new DenseInstance(new double[] {1.4, 1.4}, 3.0),
 new DenseInstance(new double[] {1.5, 1.5}, 3.0));
 Dataset[] folds = dataset.folds(0, new Random());
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testFoldsWithNullRandom() {
 DefaultDataset dataset = createDatasetWithInstances(
 new DenseInstance(new double[] {1.0, 1.0}, 3.0),
 new DenseInstance(new double[] {1.1, 1.1}, 3.0),
 new DenseInstance(new double[] {1.2, 1.2}, 2.0),
 new DenseInstance(new double[] {1.3, 1.3}, 1.0),
 new DenseInstance(new double[] {1.4, 1.4}, 3.0),
 new DenseInstance(new double[] {1.5, 1.5}, 3.0));
 Dataset[] folds = dataset.folds(5, null);
 fail("Should throw an IllegalArgumentException");
 }

}