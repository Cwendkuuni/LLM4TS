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
 assertEquals(0, dataset.maxAttributes);
 assertTrue(dataset.classes.isEmpty());
 }

 @Test
 public void testCollectionConstructor() {
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0)));
 instances.add(new DenseInstance(new double[] { 2.0, 3.0 }, new Double(1.0)));

 DefaultDataset dataset = new DefaultDataset(instances);
 assertEquals(2, dataset.size());
 assertEquals(2, dataset.maxAttributes);
 assertTrue(dataset.classes.containsAll(Arrays.asList(new Double(0.0), new Double(1.0))));
 }

 @Test
 public void testAddAll() {
 DefaultDataset dataset = createEmptyDataset();
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0)));
 instances.add(new DenseInstance(new double[] { 2.0, 3.0 }, new Double(1.0)));

 boolean result = dataset.addAll(instances);
 assertEquals(2, dataset.size());
 assertTrue(result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithUnmodifiableCollection() {
 DefaultDataset dataset = createEmptyDataset();
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0)),
 new DenseInstance(new double[] { 2.0, 3.0 }, new Double(1.0)));

 try {
 dataset.addAll(instances);
 fail("Method should throw UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 DefaultDataset dataset = createEmptyDataset();
 Instance nullInstance = null;

 try {
 dataset.add(nullInstance);
 fail("Method should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testAddInstance() {
 DefaultDataset dataset = createEmptyDataset();
 Instance instance = new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0));

 boolean result = dataset.add(instance);
 assertEquals(1, dataset.size());
 assertTrue(result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstanceWithFullCapacity() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.maxAttributes = 1;
 Instance instance = new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0));

 try {
 dataset.add(instance);
 fail("Method should throw UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testClear() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0)),
 new DenseInstance(new double[] { 2.0, 3.0 }, new Double(1.0)));

 dataset.clear();
 assertEquals(0, dataset.size());
 assertTrue(dataset.classes.isEmpty());
 }

 @Test
 public void testKNearest() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 1.0 }, new Double(0.0)),
 new DenseInstance(new double[] { 2.0, 2.0 }, new Double(1.0)),
 new DenseInstance(new double[] { 3.0, 3.0 }, new Double(0.0)),
 new DenseInstance(new double[] { 4.0, 4.0 }, new Double(1.0)));

 Instance testInstance = new DenseInstance(new double[] { 2.5, 2.5 }, new Double(0.0));
 DistanceMeasure distanceMeasure = new EuclideanDistance();

 Set<Instance> kNearestInstances = dataset.kNearest(2, testInstance, distanceMeasure);
 assertEquals(2, kNearestInstances.size());

 Instance firstNearestInstance = kNearestInstances.iterator().next();
 assertNotNull(firstNearestInstance);
 assertEquals(2.0, firstNearestInstance.attribute(0), 0.001);
 assertEquals(2.0, firstNearestInstance.attribute(1), 0.001);

 Instance secondNearestInstance = kNearestInstances.iterator().next();
 assertNotSame(firstNearestInstance, secondNearestInstance);
}

 @Test
 public void testRemoveFarthest() {
 DefaultDataset dataset = new DefaultDataset();
 Map<Instance, Double> closest = new HashMap<>();
 Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0));
 Instance instance2 = new DenseInstance(new double[] { 2.0, 3.0 }, new Double(1.0));
 closest.put(instance1, 1.0);
 closest.put(instance2, 2.0);

 double max = dataset.removeFarthest(closest);
 assertEquals(1.0, max, 0.001);
 assertEquals(1, closest.size());
 assertFalse(closest.containsKey(instance2));
 }

 @Test
 public void testFolds() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 1.0 }, new Double(0.0)),
 new DenseInstance(new double[] { 2.0, 2.0 }, new Double(1.0)),
 new DenseInstance(new double[] { 3.0, 3.0 }, new Double(0.0)),
 new DenseInstance(new double[] { 4.0, 4.0 }, new Double(1.0)));

 int numFolds = 3;
 Random rg = new Random();
 Dataset[] folds = dataset.folds(numFolds, rg);

 assertEquals(numFolds, folds.length);
 for (Dataset fold : folds) {
 assertTrue(fold instanceof Fold);
 }
 }

 @Test
 public void testNoAttributes() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0)));
 assertEquals(2, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0)),
 new DenseInstance(new double[] { 2.0, 3.0 }, new Double(1.0)));

 assertEquals(0, dataset.classIndex(new Double(0.0)));
 assertEquals(1, dataset.classIndex(new Double(1.0)));
 }

 @Test
 public void testClassValue() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0)),
 new DenseInstance(new double[] { 2.0, 3.0 }, new Double(1.0)));

 assertEquals(new Double(0.0), dataset.classValue(0));
 assertEquals(new Double(1.0), dataset.classValue(1));
 }

 @Test
 public void testCopy() {
 DefaultDataset dataset = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0 }, new Double(0.0)),
 new DenseInstance(new double[] { 2.0, 3.0 }, new Double(1.0)));
 DefaultDataset copiedDataset = dataset.copy();

 assertEquals(2, copiedDataset.size());
 for (int i = 0; i < copiedDataset.size(); i++) {
 assertEquals(dataset.instance(i), copiedDataset.instance(i));
 }
 }
}