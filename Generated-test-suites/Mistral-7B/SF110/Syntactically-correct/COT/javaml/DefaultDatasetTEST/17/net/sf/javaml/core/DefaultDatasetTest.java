package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import net.sf.javaml.core.Attribute;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.InstanceFactory;
import net.sf.javaml.distance.EuclideanDistance;

public class DefaultDatasetTest {

 private static final double DELTA = 0.0001;

 @Test
 public void testDefaultDatasetConstructor() {
 DefaultDataset dataset = new DefaultDataset();
 assertEquals(0, dataset.size());
 assertTrue(dataset.classes().isEmpty());
 }

 @Test
 public void testDefaultDatasetCollectionConstructor() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new Attribute[]{}, new double[0]));
 instances.add(new DenseInstance(new Attribute[]{}, new double[0]));
 DefaultDataset dataset = new DefaultDataset(instances);
 assertEquals(2, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testCheckNullCollection() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.check(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckInstanceWithNullClassValue() {
 Instance instance = new DenseInstance(new Attribute[]{}, new double[0]);
 instance.setClassValue(null);
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckInstanceWithMoreAttributesThanMax() {
 Instance instance = new DenseInstance(new Attribute[]{new Attribute("a", Double.TYPE)}, new double[2]);
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance);
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 assertEquals(2, dataset.size());
 try {
 dataset.add(instance);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddAll() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new Attribute[]{}, new double[0]));
 instances.add(new DenseInstance(new Attribute[]{}, new double[0]));
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(instances);
 assertEquals(2, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testAddAllWithNullCollection() {
 DefaultDataset dataset = new DefaultDataset();
 try {
 dataset.addAll(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testAddAllWithCollectionContainingNullInstance() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new Attribute[]{}, new double[0]));
 instances.add(null);
 DefaultDataset dataset = new DefaultDataset();
 try {
 dataset.addAll(instances);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testClear() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 dataset.clear();
 assertEquals(0, dataset.size());
 assertTrue(dataset.classes().isEmpty());
 }

 @Test
 public void testAdd() {
 Instance instance = new DenseInstance(new Attribute[]{}, new double[0]);
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance);
 assertEquals(1, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testAddWithNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 try {
 dataset.add(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddAt() {
 Instance instance = new DenseInstance(new Attribute[]{}, new double[0]);
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 dataset.add(instance, 0);
 assertEquals(2, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 }

 @Test
 public void testAddAtWithIndexOutOfBounds() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 try {
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]), -1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testSetElementAt() {
 Instance instance1 = new DenseInstance(new Attribute[]{}, new double[0]);
 Instance instance2 = new DenseInstance(new Attribute[]{}, new double[0]);
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.setElementAt(instance2, 0);
 assertEquals(1, dataset.size());
 assertFalse(dataset.classes().isEmpty());
 assertNotSame(instance1, dataset.instance(0));
 }

 @Test
 public void testSetElementAtWithIndexOutOfBounds() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 try {
 dataset.setElementAt(new DenseInstance(new Attribute[]{}, new double[0]), -1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testGet() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 Instance instance = dataset.instance(0);
 assertEquals(instance, dataset.get(0));
 }

 @Test
 public void testGetWithIndexOutOfBounds() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 try {
 dataset.get(1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testClasses() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.0}, 1.0));
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.0}, 0.0));
 List<Object> classes = new ArrayList<>(dataset.classes());
 Collections.sort(classes);
 assertEquals(2, classes.size());
 assertEquals(0.0, classes.get(0));
 assertEquals(1.0, classes.get(1));
 }

 @Test
 public void testSize() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 assertEquals(1, dataset.size());
 }

 @Test
 public void testIsEmpty() {
 DefaultDataset dataset = new DefaultDataset();
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testContains() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 assertTrue(dataset.contains(dataset.instance(0)));
 }

 @Test
 public void testContainsWithNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 assertFalse(dataset.contains(null));
 }

 @Test
 public void testContainsWithInstanceFromDifferentDataset() {
 DefaultDataset dataset1 = new DefaultDataset();
 dataset1.add(new DenseInstance(new Attribute[]{}, new double[0]));
 DefaultDataset dataset2 = new DefaultDataset();
 dataset2.add(new DenseInstance(new Attribute[]{}, new double[0]));
 assertFalse(dataset1.contains(dataset2.instance(0)));
 }

 @Test
 public void testRemove() {
 DefaultDataset dataset = new DefaultDataset();
 Instance instance = new DenseInstance(new Attribute[]{}, new double[0]);
 dataset.add(instance);
 dataset.remove(instance);
 assertEquals(0, dataset.size());
 assertTrue(dataset.classes().isEmpty());
 }

 @Test
 public void testRemoveWithNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 Instance instance = new DenseInstance(new Attribute[]{}, new double[0]);
 dataset.add(instance);
 try {
 dataset.remove(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testRemoveWithInstanceNotInDataset() {
 DefaultDataset dataset = new DefaultDataset();
 Instance instance = new DenseInstance(new Attribute[]{}, new double[0]);
 try {
 dataset.remove(instance);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIterator() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 List<Instance> instances = new ArrayList<>(Arrays.asList(dataset.iterator()));
 assertEquals(2, instances.size());
 }

 @Test
 public void testRemoveFarthest() {
 DefaultDataset dataset = new DefaultDataset();
 Instance instance1 = new DenseInstance(new Attribute[]{}, new double[]{1.0});
 Instance instance2 = new DenseInstance(new Attribute[]{}, new double[]{2.0});
 dataset.add(instance1);
 dataset.add(instance2);
 double max = dataset.removeFarthest(new HashMap<>());
 assertEquals(1.0, max, DELTA);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testRemoveFarthestWithEmptyVector() {
 DefaultDataset dataset = new DefaultDataset();
 double max = dataset.removeFarthest(new HashMap<>());
 assertEquals(0.0, max, DELTA);
 assertEquals(0, dataset.size());
 }

 @Test
 public void testKNearest() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.1, 0.1}, 0.0));
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.2, 0.2}, 1.0));
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.3, 0.3}, 0.0));
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 Instance instance = new DenseInstance(new Attribute[]{}, new double[]{0.4, 0.4});
 List<Instance>expectedKNearest = Arrays.asList(
 dataset.instance(1),
 dataset.instance(0)
 );
 List<Instance> nearestInstances = dataset.kNearest(2, instance, distanceMeasure);
 assertEquals(expectedKNearest.size(), nearestInstances.size());
 for (int i = 0; i < expectedKNearest.size(); i++) {
 assertEquals(expectedKNearest.get(i), nearestInstances.get(i));
 }
 }

 @Test
 public void testFolds() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.1}, 0.0));
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.2}, 1.0));
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.3}, 0.0));
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.4}, 1.0));
 Dataset[] expectedFolds = new Dataset[]{
 new DefaultDataset(Arrays.asList(
 new DenseInstance(new Attribute[]{}, new double[]{0.2, 0.2}, 1.0),
 new DenseInstance(new Attribute[]{}, new double[]{0.3, 0.3}, 0.0),
 new DenseInstance(new Attribute[]{}, new double[]{0.4, 0.4}, 1.0)
 )),
 new DefaultDataset(Arrays.asList(
 new DenseInstance(new Attribute[]{}, new double[]{0.1, 0.1}, 0.0),
 new DenseInstance(new Attribute[]{}, new double[]{0.3, 0.3}, 0.0)
 ))
 };
 Dataset[] folds = dataset.folds(2, new Random());
 assertEquals(expectedFolds.length, folds.length);
 for (int i = 0; i < expectedFolds.length; i++) {
 assertEquals(expectedFolds[i].getClass(), folds[i].getClass());
 assertEquals(expectedFolds[i].size(), folds[i].size());
 for (int j = 0; j < expectedFolds[i].size(); j++) {
 Instance expectedInstance = expectedFolds[i].instance(j);
 Instance instance = folds[i].instance(j);
 assertEquals(expectedInstance.attributes(), instance.attributes());
 assertEquals(expectedInstance.classValue(), instance.classValue());
 }
 }
 }

 @Test
 public void testNoAttributes() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 assertEquals(0, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.0}, 1.0));
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.0}, 0.0));
 assertEquals(1, dataset.classIndex(1.0));
 assertEquals(-1, dataset.classIndex(null));
 }

 @Test
 public void testClassValue() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.0}, 1.0));
 dataset.add(new DenseInstance(new Attribute[]{}, new double[]{0.0}, 0.0));
 assertEquals(1.0, dataset.classValue(0), DELTA);
 assertEquals(null, dataset.classValue(1));
 }

 @Test
 public void testCopy() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new Attribute[]{}, new double[0]));
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(dataset.size(), copiedDataset.size());
 for (int i = 0; i < dataset.size(); i++) {
 Instance instance1 = dataset.instance(i);
 Instance instance2 = copiedDataset.instance(i);
 assertEquals(instance1.attributes(), instance2.attributes());
 assertEquals(instance1.classValue(), instance2.classValue());
 }
 }
}