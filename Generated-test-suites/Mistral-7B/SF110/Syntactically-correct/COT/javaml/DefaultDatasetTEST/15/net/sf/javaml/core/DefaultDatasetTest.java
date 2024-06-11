package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setup() {
 dataset = new DefaultDataset();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.1});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new double[]{1.0});
 instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new double[]{0.1});
 distanceMeasure = new EuclideanDistance();
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertTrue(emptyDataset.isEmpty());
 }

 @Test
 public void testCollectionConstructor() {
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 DefaultDataset datasetWithInstances = new DefaultDataset(instances);
 assertEquals(3, datasetWithInstances.size());
 }

 @Test
 public void testAddAll() {
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 dataset.addAll(instances);
 assertEquals(3, dataset.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllThrowsExceptionWhenModifyingImmutableCollection() {
 List<Instance> instances = new ArrayList<>(dataset);
 instances.add(instance3);
 dataset.addAll(instances);
 }

 @Test
 public void testAddInstance() {
 dataset.add(instance3);
 assertEquals(4, dataset.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullInstanceThrowsException() {
 dataset.add(null);
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testSize() {
 assertEquals(0, dataset.size());
 dataset.add(instance1);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testGet() {
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance1, dataset.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetThrowsExceptionWhenIndexOutOfBounds() {
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance1, dataset.get(0));
 assertEquals(null, dataset.get(1));
 }

 @Test
 public void testClasses() {
 assertEquals(0, dataset.classes().size());
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(1, dataset.classes().size());
 }

 @Test
 public void testClassesContainExpectedClasses() {
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance1.classValue(), dataset.classes().first());
 }

 @Test
 public void testKNearestMethod() {
 List<Instance> kNearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, kNearestInstances.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestThrowsExceptionWhenInvalidK() {
 dataset.kNearest(-1, instance1, distanceMeasure);
 }

 @Test(expected = NullPointerException.class)
 public void testKNearestThrowsExceptionWhenNullInstance() {
 dataset.kNearest(2, null, distanceMeasure);
 }

 @Test
 public void testFoldsMethod() {
 Dataset[] folds = dataset.folds(3, new Random());
 assertEquals(3, folds.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsThrowsExceptionWhenInvalidNumFolds() {
 dataset.folds(-1, new Random());
 }

 @Test(expected = NullPointerException.class)
 public void testFoldsThrowsExceptionWhenNullRandom() {
 dataset.folds(3, null);
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, instance1.noAttributes());
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(0, dataset.classIndex(instance1.classValue()));
 }

 @Test(expected = NullPointerException.class)
 public void testClassIndexThrowsExceptionWhenNullClass() {
 dataset.classIndex(null);
 }

 @Test
 public void testClassValue() {
 assertEquals(instance1.classValue(), dataset.classValue(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueThrowsExceptionWhenIndexOutOfBounds() {
 assertEquals(null, dataset.classValue(2));
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(dataset.size(), copiedDataset.size());
 }
}