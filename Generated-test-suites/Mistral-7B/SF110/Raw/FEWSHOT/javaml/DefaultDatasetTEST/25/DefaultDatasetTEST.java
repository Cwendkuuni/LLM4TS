package net.sf.javaml.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure mockDistanceMeasure;

 @Before
 public void setUp() {
 instance1 = mock(Instance.class);
 instance2 = mock(Instance.class);
 instance3 = mock(Instance.class);
 mockDistanceMeasure = mock(DistanceMeasure.class);
 dataset = new DefaultDataset();
 }

 @Test
 public void testDefaultDatasetNoArguments() {
 dataset = new DefaultDataset();
 assertEquals(0, dataset.size());
 assertEquals(0, dataset.maxAttributes());
 assertNull(dataset.classes());
 }

 @Test
 public void testDefaultDatasetWithCollectionOfInstances() {
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 dataset = new DefaultDataset(instances);
 assertEquals(instances.size(), dataset.size());
 assertEquals(instance1.noAttributes(), dataset.maxAttributes());
 assertSame(instances, dataset);
 }

 @Test
 public void testAddAllMethod() {
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 dataset.addAll(instances);
 assertEquals(instances.size(), dataset.size());
 assertEquals(instance1.noAttributes(), dataset.maxAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllWithNullCollectionOfInstances() {
 dataset.addAll(null);
 }

 @Test
 public void testCheckMethodForNullInstance() {
 dataset.check(null);
 assertEquals(0, dataset.size());
 }

 @Test
 public void testCheckMethodForValidInstance() {
 dataset.check(instance1);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testClassesMethod() {
 Set<Object> classes = new TreeSet<>();
 classes.add("Class1");
 classes.add("Class2");
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 dataset.classes().addAll(classes);
 assertEquals("Class1", dataset.classValue(0));
 assertEquals("Class2", dataset.classValue(1));
 }

 @Test
 public void testClassesMethodWithEmptyCollectionOfInstances() {
 dataset = new DefaultDataset();
 assertNull(dataset.classes());
 }

 @Test
 public void testFoldsMethod() {
 int numFolds = 3;
 Random random = mock(Random.class);
 Dataset[] expectedFolds = new Dataset[numFolds];
 for (int i = 0; i < numFolds; i++) {
 expectedFolds[i] = new Fold(dataset, new int[dataset.size()]);
 }
 when(random.nextInt(dataset.size())).thenReturn(0);
 when(random.nextInt(dataset.size())).thenReturn(1);
 when(random.nextInt(dataset.size())).thenReturn(2);
 when(random.nextInt(dataset.size())).thenThrow(new IndexOutOfBoundsException());
 Dataset[] actualFolds = dataset.folds(numFolds, random);
 assertArrayEquals(expectedFolds, actualFolds);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsMethodWithNegativeNumFolds() {
 dataset.folds(-1, mock(Random.class));
 }

 @Test(expected = NullPointerException.class)
 public void testFoldsMethodWithNullRandom() {
 dataset.folds(3, null);
 }

 @Test
 public void testGetInstanceMethod() {
 dataset.addAll(Arrays.asList(instance1, instance2, instance3));
 assertEquals(instance1, dataset.getInstance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInstanceMethodWithInvalidIndex() {
 dataset.getInstance(3);
 }

 @Test
 public void testInstancesMethod() {
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 dataset.addAll(instances);
 List<Instance> actualInstances = new ArrayList<>(dataset.instances());
 List<Instance> expectedInstances = new ArrayList<>(instances);
 assertEquals(expectedInstances, actualInstances);
 }

 @Test
 public void testKNearestMethod() {
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 dataset.addAll(instances);
 when(mockDistanceMeasure.measure(instance1, instance2)).thenReturn(1.0);
 when(mockDistanceMeasure.measure(instance1, instance3)).thenReturn(0.5);
 when(mockDistanceMeasure.measure(instance2, instance3)).thenReturn(1.5);
 Set<Instance> expectedInstances = new TreeSet<>();
 expectedInstances.add(instance1);
 expectedInstances.add(instance3);
 Set<Instance> actualInstances = dataset.kNearest(2, instance1, mockDistanceMeasure);
 assertEquals(expectedInstances, actualInstances);
 }

 @Test
 public void testKNearestMethodWithInvalidk() {
 dataset.kNearest(-1, instance1, mockDistanceMeasure);
 }

 @Test
 public void testKNearestMethodWithNullInstance() {
 dataset.kNearest(2, null, mockDistanceMeasure);
 }

 @Test
 public void testKNearestMethodWithNullDistanceMeasure() {
 dataset.kNearest(2, instance1, null);
 }

 @Test
 public void testNoAttributesMethod() {
 Instance instance = mock(Instance.class);
 when(instance.noAttributes()).thenReturn(5);
 dataset.add(instance);
 assertEquals(5, dataset.noAttributes());
 }

 @Test
 public void testSizeMethod() {
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 dataset.addAll(instances);
 assertEquals(instances.size(), dataset.size());
 }

 @Test
 public void testClearMethod() {
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 dataset.addAll(instances);
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAddMethod() {
 Instance instance = mock(Instance.class);
 dataset.add(instance);
 assertEquals(1, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMethodWithNullInstance() {
 dataset.add(null);
 }

 @Test
 public void testAddAtMethod() {
 Instance instance = mock(Instance.class);
 dataset.add(1, instance);
 assertEquals(instance, dataset.instance(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddAtMethodWithInvalidIndex() {
 dataset.addAt(1, instance1);
 }

 @Test
 public void testSetAtMethod() {
 Instance instance = mock(Instance.class);
 dataset.setAt(1, instance);
 assertEquals(instance, dataset.instance(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetAtMethodWithInvalidIndex() {
 dataset.setAt(1, instance1);
 }

 @Test
 public void testRemoveMethod() {
 Instance instance = mock(Instance.class);
 dataset.add(instance);
 dataset.remove(instance);
 assertEquals(0, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveMethodWithNullInstance() {
 dataset.remove(null);
 }

 @Test
 public void testRemoveAtMethod() {
 Instance instance = mock(Instance.class);
 dataset.add(instance);
 dataset.removeAt(0);
 assertEquals(instance, dataset.instance(0));
 assertEquals(0, dataset.size() - 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtMethodWithInvalidIndex() {
 dataset.removeAt(1);
 }

 @Test
 public void testCopyMethod() {
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.addAll(Arrays.asList(instance1, instance2, instance3));
 Dataset actualDataset = dataset.copy();
 assertEquals(expectedDataset, actualDataset);
 }

 @Test
 public void testClassIndexMethod() {
 Set<Object> classes = new TreeSet<>();
 classes.add("Class1");
 classes.add("Class2");
 dataset.addAll(Arrays.asList(instance1, instance2, instance3));
 dataset.classes().addAll(classes);
 assertEquals(0, dataset.classIndex("Class1"));
 assertEquals(1, dataset.classIndex("Class2"));
 assertEquals(-1, dataset.classIndex(null));
 }

 @Test
 public void testClassValueMethod() {
 Set<Object> classes = new TreeSet<>();
 classes.add("Class1");
 classes.add("Class2");
 dataset.addAll(Arrays.asList(instance1, instance2, instance3));
 dataset.classes().addAll(classes);
 assertEquals("Class1", dataset.classValue(0));
 assertEquals("Class2", dataset.classValue(1));
 assertEquals(null, dataset.classValue(2));
 }

 @Test
 public void testRemoveFarthestMethod() {
 Map<Instance, Double> closestInstances = new HashMap<>();
 closestInstances.put(instance1, 2.0);
 closestInstances.put(instance2, 1.0);
 closestInstances.put(instance3, 3.0);
 assertEquals(3.0, dataset.removeFarthest(closestInstances));
 }

 @Test
 public void testRemoveFarthestMethodWithEmptyMap() {
 Map<Instance, Double> closestInstances = new HashMap<>();
 assertEquals(0.0, dataset.removeFarthest(closestInstances));
 }

 @Test
 public void testRemoveFarthestMethodWithNullMap() {
 assertThrows(NullPointerException.class, () -> dataset.removeFarthest(null));
 }

}