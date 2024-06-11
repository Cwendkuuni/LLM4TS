package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;

 @Before
 public void setup() {
 collection().add(instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
 collection().add(instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, 2.0));
 collection().add(instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, 3.0));
 dataset = new DefaultDataset(collection());
 }

 @Test
 public void testDefaultDatasetWithCollection() {
 assertEquals(3, dataset.size());
 assertEquals(3, dataset.maxAttributes());
 }

 @Test
 public void testDefaultDatasetEmpty() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertEquals(0, emptyDataset.size());
 assertEquals(0, emptyDataset.maxAttributes());
 }

 @Test
 public void testAddAllInstanceCollection() {
 List<Instance> collection = new ArrayList<>();
 collection.add(instance1);
 collection.add(instance2);
 collection.add(instance3);
 DefaultDataset datasetWithCollection = new DefaultDataset(collection);
 dataset.addAll(instance1);
 assertEquals(4, dataset.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllThrowExceptionWhenModifyingCollection() {
 List<Instance> collection = new ArrayList<>(Arrays.asList(instance1, instance2, instance3));
 dataset.addAll(collection); // Success
 collection.remove(instance1); // Fail
 dataset.addAll(collection); // Exception
 }

 @Test
 public void testAddInstance() {
 Instance newInstance = new DenseInstance(new double[]{6.0, 7.0, 8.0}, 4.0);
 dataset.add(newInstance);
 assertEquals(4, dataset.size());
 }

 @Test
 public void testKNearestDistance() {
 double[] expectedDistances = {2.2361, 2.7459, 3.2649};
 Instance testInstance = new DenseInstance(new double[]{4.0, 4.0, 4.0}, 1.0);
 SortedSet<Instance> kNearestInstances = dataset.kNearest(3, testInstance, new EuclideanDistance());
 Instance[] instances = new Instance[3];
 for (int i = 0; i < 3; i++) {
 instances[i] = dataset.instance(i);
 }
 for (int i = 0; i < 3; i++) {
 assertEquals(instances[i], kNearestInstances.iterator().next());
 assertEquals(expectedDistances[i], testInstance.distance(instances[i]));
 kNearestInstances.remove(instances[i]);
 }
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 Random random = new Random();
 Dataset[] folds = dataset.folds(numFolds, random);
 for (Dataset fold : folds) {
 assertEquals(dataset.size(), fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(1, dataset.classIndex("1.0"));
 assertEquals(-1, dataset.classIndex(null));
 }

 @Test
 public void testClassValue() {
 assertEquals("1.0", dataset.classValue(0).classValue().toString());
 assertEquals(null, dataset.classValue(2));
 }

 @Test
 public void testCopy() {
 Dataset copiedDataset = dataset.copy();
 assertEquals(dataset, copiedDataset);
 }

 private List<Instance> collection() {
 return new ArrayList<>(Arrays.asList(instance1, instance2, instance3));
 }
}