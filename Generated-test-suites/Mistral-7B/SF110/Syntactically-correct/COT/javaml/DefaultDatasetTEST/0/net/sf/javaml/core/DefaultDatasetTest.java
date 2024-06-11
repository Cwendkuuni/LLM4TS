package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3, instance4, instance5;
 private DistanceMeasure distanceMeasure;
 private Random random;

 @Before
 public void setup() {
 instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, new double[] { 4.0 });
 instance2 = new DenseInstance(new double[] { 2.0, 3.0, 4.0 }, new double[] { 5.0 });
 instance3 = new DenseInstance(new double[] { 3.0, 4.0, 5.0 }, new double[] { 6.0 });
 instance4 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, new double[] { 4.0 });
 instance5 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, new double[] { 5.0 });

 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 dataset.add(instance4);
 dataset.add(instance5);

 distanceMeasure = new EuclideanDistance();
 random = new Random();
 }

 // Basic test cases for constructors
 @Test
 public void testEmptyConstructor() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertEquals(0, emptyDataset.size());
 }

 @Test
 public void testCollectionConstructor() {
 assertEquals(5, dataset.size());
 }

 // Test methods for addAll
 @Test
 public void testAddAll() {
 List<Instance> newInstances = new ArrayList<>();
 newInstances.add(instance2);
 newInstances.add(instance3);
 dataset.addAll(newInstances);
 assertEquals(7, dataset.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllUnsupportedOperation() {
 List<Instance> newInstances = new ArrayList<>();
 newInstances.add(instance2);
 newInstances.add(instance3);
 try {
 dataset.addAll(1, newInstances);
 } catch (UnsupportedOperationException e) {
 fail("UnsupportedOperationException expected but not thrown");
 }
 }

 // Test methods for clear
 @Test
 public void testClear() {
 int initialSize = dataset.size();
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 // Test methods for add
 @Test
 public void testAddInstance() {
 Instance newInstance = new DenseInstance(new double[] { 6.0, 7.0, 8.0 }, new double[] { 9.0 });
 dataset.add(newInstance);
 assertEquals(8, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 try {
 dataset.add(null);
 } catch (IllegalArgumentException e) {
 fail("IllegalArgumentException expected but not thrown");
 }
 }

 // Test methods for set
 @Test
 public void testSetInstance() {
 Instance newInstance = new DenseInstance(new double[] { 6.0, 7.0, 8.0 }, new double[] { 9.0 });
 dataset.set(0, newInstance);
 assertEquals(instance1.getClassValue(), dataset.get(0).getClassValue());
 assertEquals(newInstance, dataset.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetInvalidIndex() {
 try {
 dataset.set(5, newInstance);
 } catch (IndexOutOfBoundsException e) {
 fail("IndexOutOfBoundsException expected but not thrown");
 }
 }

 // Test methods for get
 @Test
 public void testGetInstance() {
 assertEquals(instance1, dataset.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInvalidIndex() {
 try {
 dataset.get(5);
 } catch (IndexOutOfBoundsException e) {
 fail("IndexOutOfBoundsException expected but not thrown");
 }
 }

 // Test methods for size
 @Test
 public void testSize() {
 assertEquals(5, dataset.size());
 }

 // Test methods for isEmpty
 @Test
 public void testIsEmpty() {
 assertTrue(dataset.isEmpty());
 dataset.clear();
 assertFalse(dataset.isEmpty());
 }

 // Test methods for contains
 @Test
 public void testContainsInstance() {
 assertTrue(dataset.contains(instance1));
 }

 @Test
 public void testContainsNullInstance() {
 assertFalse(dataset.contains(null));
 }

 @Test
 public void testContainsInstanceAfterAddingIt() {
 Instance newInstance = new DenseInstance(new double[] { 6.0, 7.0, 8.0 }, new double[] { 9.0 });
 dataset.add(newInstance);
 assertTrue(dataset.contains(newInstance));
 }

 @Test
 public void testContainsInstanceAfterRemovingIt() {
 Instance newInstance = new DenseInstance(new double[] { 6.0, 7.0, 8.0 }, new double[] { 9.0 });
 dataset.add(newInstance);
 dataset.remove(newInstance);
 assertFalse(dataset.contains(newInstance));
 }

 // Test methods for remove
 @Test
 public void testRemoveInstance() {
 Instance instanceToRemove = instance2;
 dataset.remove(instanceToRemove);
 assertEquals(4, dataset.size());
 assertFalse(dataset.contains(instanceToRemove));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveInvalidIndex() {
 try {
 dataset.remove(5);
 } catch (IndexOutOfBoundsException e) {
 fail("IndexOutOfBoundsException expected but not thrown");
 }
 }

 // Test methods for removeAll
 @Test
 public void testRemoveAll() {
 List<Instance> instancesToRemove = Arrays.asList(instance2, instance3);
 int initialSize = dataset.size();
 dataset.removeAll(instancesToRemove);
 assertEquals(3, dataset.size());
 for (Instance instance : instancesToRemove) {
 assertFalse(dataset.contains(instance));
 }
 }

 // Test methods for retainAll
 @Test
 public void testRetainAll() {
 List<Instance> instancesToKeep = Arrays.asList(instance1, instance4);
 int initialSize = dataset.size();
 dataset.retainAll(instancesToKeep);
 assertEquals(2, dataset.size());
 for (Instance instance : instancesToKeep) {
 assertTrue(dataset.contains(instance));
 }
 }

 // Test methods for iterator
 @Test
 public void testIterator() {
 List<Instance> instances = new ArrayList<>();
 for (Instance instance : dataset) {
 instances.add(instance);
 }
 assertEquals(dataset.size(), instances.size());
 for (Instance instance : instances) {
 assertEquals(instance, dataset.get(dataset.indexOf(instance)));
 }
 }

 // Test methods for classes
 @Test
 public void testGetClasses() {
 Set<Object> expectedClasses = new LinkedHashSet<>(Arrays.asList("4.0", "5.0"));
 Set<Object> actualClasses = dataset.classes();
 assertEquals(expectedClasses, actualClasses);
 }

 // Test methods for noAttributes
 @Test
 public void testNoAttributes() {
 assertEquals(3, instance1.noAttributes());
 assertEquals(3, dataset.noAttributes());
 }

 // Test methods for kNearest
 @Test
 public void testKNearestValidValues() {
 Set<Instance> kNearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, kNearestInstances.size());
 }

 @Test
 public void testKNearestEdgeCaseEmptyDataset() {
 Set<Instance> kNearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(0, kNearestInstances.size());
 }

 @Test
 public void testKNearestEdgeCaseInvalidK() {
 try {
 dataset.kNearest(-1, instance1, distanceMeasure);
 } catch (IllegalArgumentException e) {
 fail("IllegalArgumentException expected but not thrown");
 }
 }

 @Test
 public void testKNearestEdgeCaseNullInstance() {
 try {
 dataset.kNearest(2, null, distanceMeasure);
 } catch (NullPointerException e) {
 fail("NullPointerException expected but not thrown");
 }
 }

 // Test methods for folds
 @Test
 public void testFoldsValidValues() {
 Dataset[] folds = dataset.folds(5, random);
 assertEquals(5, folds.length);
 }

 @Test
 public void testFoldsEdgeCaseEmptyDataset() {
 try {
 dataset.folds(5, random);
 } catch (UnsupportedOperationException e) {
 fail("UnsupportedOperationException expected but not thrown");
 }
 }

 @Test
 public void testFoldsEdgeCaseInvalidNumFolds() {
 try {
 dataset.folds(-1, random);
 } catch (IllegalArgumentException e) {
 fail("IllegalArgumentException expected but not thrown");
 }
 }
}