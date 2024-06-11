package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.*;

import org.junit.Test;

public class DefaultDatasetTest {

 private Collection<Instance> createInstancesCollection() {
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[] {1.0, 2.0, 3.0}, new Double(1.0)));
 instances.add(new DenseInstance(new double[] {2.0, 3.0, 4.0}, new Double(2.0)));
 instances.add(new DenseInstance(new double[] {3.0, 4.0, 5.0}, new Double(3.0)));
 return instances;
 }

 @Test
 public void testDefaultDatasetConstructorWithCollection() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 assertEquals(3, dataset.size());
 }

 @Test
 public void testDefaultDatasetConstructorEmpty() {
 DefaultDataset dataset = new DefaultDataset();
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testAddAllMethod() {
 DefaultDataset dataset = new DefaultDataset();
 Collection<Instance> instances = createInstancesCollection();
 dataset.addAll(instances);
 assertEquals(3, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllMethodWithNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 Instance nullInstance = null;
 dataset.addAll(Arrays.asList(nullInstance));
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testAddAllMethodWithNullCollection() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(null);
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testCheckMethod() {
 Collection<Instance> instances = createInstancesCollection();
 DefaultDataset dataset = new DefaultDataset();
 dataset.check(instances);
 assertEquals(3, dataset.maxAttributes);
 }

 @Test
 public void testClassesMethod() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 SortedSet<Object> classes = dataset.classes();
 assertEquals(3, classes.size());
 assertTrue(classes.contains(1.0));
 assertTrue(classes.contains(2.0));
 assertTrue(classes.contains(3.0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClearMethod() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 dataset.clear();
 fail("UnsupportedOperationException expected");
 }

 @Test
 public void testAddMethod() {
 DefaultDataset dataset = new DefaultDataset();
 Instance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, new Double(1.0));
 dataset.add(instance);
 assertEquals(1, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMethodWithNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 Instance nullInstance = null;
 dataset.add(nullInstance);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testSetElementAtMethod() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 Instance newInstance = new DenseInstance(new double[] {4.0, 5.0, 6.0}, new Double(4.0));
 dataset.setElementAt(newInstance, 0);
 assertEquals(newInstance, dataset.getInstance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetElementAtMethodWithInvalidIndex() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 Instance newInstance = new DenseInstance(new double[] {4.0, 5.0, 6.0}, new Double(4.0));
 dataset.setElementAt(newInstance, 3);
 fail("IndexOutOfBoundsException expected");
 }

 @Test
 public void testInstanceMethod() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 Instance instance = dataset.instance(0);
 assertEquals(new DenseInstance(new double[] {1.0, 2.0, 3.0}, new Double(1.0)), instance);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceMethodWithInvalidIndex() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 Instance instance = dataset.instance(3);
 fail("IndexOutOfBoundsException expected");
 }

 @Test
 public void testKNearestMethod() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 Instance testInstance = new DenseInstance(new double[] {0.5, 0.5, 0.5}, new Double(1.0));
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 Set<Instance> nearestInstances = dataset.kNearest(3, testInstance, distanceMeasure);
 assertEquals(3, nearestInstances.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestMethodWithNullDistanceMeasure() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 Set<Instance> nearestInstances = dataset.kNearest(3, null, null);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestMethodWithNullTestInstance() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 Set<Instance> nearestInstances = dataset.kNearest(3, null, new EuclideanDistance());
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestMethodWithNegativeK() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 Set<Instance> nearestInstances = dataset.kNearest(-1, new DenseInstance(new double[] {0.5, 0.5, 0.5}, new Double(1.0)), new EuclideanDistance());
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testFoldsMethod() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 int numFolds = 3;
 Random rg = new Random();
 Dataset[] folds = dataset.folds(numFolds, rg);
 assertEquals(numFolds, folds.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsMethodWithNegativeNumFolds() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 int numFolds = -1;
 Random rg = new Random();
 Dataset[] folds = dataset.folds(numFolds, rg);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testNoAttributesMethod() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndexMethod() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 assertEquals(1, dataset.classIndex(1.0));
 }

 @Test
 public void testClassValueMethod() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 assertEquals(1.0, dataset.classValue(0).doubleValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueMethodWithInvalidIndex() {
 DefaultDataset dataset = new DefaultDataset(createInstancesCollection());
 assertEquals(null, dataset.classValue(3));
 }

 @Test
 public void testCopyMethod() {
 DefaultDataset originalDataset = new DefaultDataset(createInstancesCollection());
 DefaultDataset copiedDataset = originalDataset.copy();
 assertEquals(originalDataset.size(), copiedDataset.size());
 }
}