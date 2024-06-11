package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private List<Instance> instanceList;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setup() {
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 3.0);
 instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 4.0);
 instance3 = new DenseInstance(new double[]{1.0, 1.0, 1.0}, 3.0);
 instanceList = new ArrayList<>();
 instanceList.add(instance1);
 instanceList.add(instance2);
 instanceList.add(instance3);
 distanceMeasure = new EuclideanDistance();
 dataset = new DefaultDataset(instanceList);
 }

 @Test
 public void testAddAll() {
 Collection<Instance> collection = new ArrayList<>();
 collection.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, 5.0));
 dataset.addAll(collection);
 assertEquals(5, dataset.size());
 }

 @Test
 public void testAddAllWithEmptyCollection() {
 dataset.addAll(new ArrayList<>());
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAddAllWithNullInstance() {
 Collection<Instance> nullInstanceCollection = new ArrayList<>();
 nullInstanceCollection.add(null);
 try {
 dataset.addAll(nullInstanceCollection);
 fail("Adding null instance to dataset should throw an exception.");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testAdd() {
 Instance newInstance = new DenseInstance(new double[]{0.0, 0.0, 0.0}, 3.0);
 dataset.add(newInstance);
 assertEquals(4, dataset.size());
 }

 @Test
 public void testAddWithDuplicateInstance() {
 dataset.add(instance1);
 dataset.add(instance1);
 assertEquals(2, dataset.classes().size()); // Class count should not change
 assertEquals(2, dataset.size());
 }

 @Test
 public void testClasses() {
 assertEquals(2, dataset.classes().size());
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testInstance() {
 assertEquals(instance1, dataset.instance(0));
 }

 @Test
 public void testInstanceOutOfBounds() {
 try {
 dataset.instance(dataset.size());
 fail("Accessing instance out of bounds should throw an exception.");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testkNearest() {
 Set<Instance> kNearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
 assertTrue(kNearestInstances.contains(instance1));
 assertTrue(kNearestInstances.contains(instance2));
 }

 @Test
 public void testkNearestWithSameDistance() {
 Set<Instance> kNearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, kNearestInstances.size());
 Set<Instance> kNearestInstances2 = dataset.kNearest(2, instance3, distanceMeasure);
 assertEquals(2, kNearestInstances2.size());
 assertTrue(kNearestInstances.containsAll(kNearestInstances2));
 }

 @Test
 public void testkNearestWithEmptyDataset() {
 Set<Instance> kNearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(0, kNearestInstances.size());
 }

 @Test
 public void testkNearestWithNullInstance() {
 try {
 dataset.kNearest(2, null, distanceMeasure);
 fail("Passing null instance to kNearest should throw an exception.");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testkNearestWithNullDistanceMeasure() {
 try {
 dataset.kNearest(2, instance1, null);
 fail("Passing null distance measure to kNearest should throw an exception.");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testFolds() {
 Dataset[] folds = dataset.folds(3, new Random());
 assertEquals(3, folds.length);
 for (Dataset fold : folds) {
 assertTrue(fold instanceof Fold);
 }
 }

 @Test
 public void noAttributes() {
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void classIndex() {
 assertEquals(3, dataset.classIndex(instance1.classValue()));
 }

 @Test
 public void classValue() {
 assertEquals(3.0, dataset.classValue(0).classValue(), 0.01);
 }

 @Test
 public void copy() {
 DefaultDataset copiedDataset = (DefaultDataset) dataset.copy();
 assertEquals(dataset.size(), copiedDataset.size());
 }

 @Test
 public void copyWithEmptyDataset() {
 DefaultDataset copiedDataset = (DefaultDataset) dataset.copy();
 assertEquals(0, copiedDataset.size());
 }

}