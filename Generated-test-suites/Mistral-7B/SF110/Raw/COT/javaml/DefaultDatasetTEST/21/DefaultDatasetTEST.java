package net.sf.javaml.core; // make sure to import necessary packages

import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {

 private DefaultDataset defaultDataset;
 private Instance instance;
 private Collection<Instance> instances;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 defaultDataset = new DefaultDataset();
 instance = new DummyInstance(new double[5]); // assuming DummyInstance is a valid Instance implementation
 instances = new ArrayList<>();
 instances.add(instance);
 defaultDataset.addAll(instances);
 distanceMeasure = new EuclideanDistance();
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset dataset = new DefaultDataset();
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testConstructorWithCollection() {
 DefaultDataset dataset = new DefaultDataset(instances);
 assertEquals(instances.size(), dataset.size());
 }

 @Test
 public void testAddAll() {
 Collection<Instance> newInstances = new ArrayList<>();
 newInstances.add(new DummyInstance(new double[5]));
 defaultDataset.addAll(newInstances);
 assertEquals(instances.size() + newInstances.size(), defaultDataset.size());
 }

 @Test
 public void testClear() {
 defaultDataset.clear();
 assertTrue(defaultDataset.isEmpty());
 }

 @Test
 public void testAdd() {
 Instance newInstance = new DummyInstance(new double[5]);
 defaultDataset.add(newInstance);
 assertEquals(instances.size() + 1, defaultDataset.size());
 }

 @Test
 public void testAddElementAt() {
 Instance newInstance = new DummyInstance(new double[5]);
 defaultDataset.addElementAt(newInstance, 0);
 assertEquals(newInstance.noAttributes(), defaultDataset.maxAttributes());
 }

 @Test
 public void testSetElementAt() {
 Instance newInstance = new DummyInstance(new double[5]);
 Instance oldInstance = defaultDataset.get(0);
 defaultDataset.setElementAt(newInstance, 0);
 assertEquals(newInstance, defaultDataset.get(0));
 }

 @Test
 public void testInstance() {
 assertEquals(instance, defaultDataset.instance(0));
 }

 @Test
 public void testClasses() {
 Set<Object> classes = defaultDataset.classes();
 assertTrue(classes.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 defaultDataset.remove(instance);
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 try {
 defaultDataset.get(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testKNearest() {
 Set<Instance> kNearest = defaultDataset.kNearest(1, instance, distanceMeasure);
 assertEquals(1, kNearest.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestWithNegativeK() {
 defaultDataset.kNearest(-1, instance, distanceMeasure);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testKNearestWithNullInstance() {
 defaultDataset.kNearest(1, null, distanceMeasure);
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testKNearestWithNullDistanceMeasure() {
 defaultDataset.kNearest(1, instance, null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testFolds() {
 Dataset[] folds = defaultDataset.folds(3, new Random());
 assertEquals(3, folds.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsWithInvalidNumFolds() {
 defaultDataset.folds(-1, new Random());
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testNoAttributes() {
 assertEquals(instance.noAttributes(), defaultDataset.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 defaultDataset.remove(instance);
 fail("Expected UnsupportedOperationException");
 }

 @Test
 public void testClassIndex() {
 assertEquals(0, defaultDataset.classIndex(null));
 }

 @Test
 public void testClassValue() {
 Object clazz = defaultDataset.getClassValue(0);
 assertEquals(defaultDataset.getClassValue(0), defaultDataset.classValue(defaultDataset.size() - 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueWithInvalidIndex() {
 try {
 defaultDataset.classValue(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = (DefaultDataset) defaultDataset.copy();
 assertEquals(defaultDataset.size(), copiedDataset.size());
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> closest = new HashMap<>();
 closest.put(instance, 0.1);
 double max = defaultDataset.removeFarthest(closest);
 assertEquals(0.1, max, 0.0);
 }

 @Test
 public void testAddAllWithNullCollection() {
 Collection<Instance> nullCollection = null;
 boolean result = defaultDataset.addAll(nullCollection);
 assertTrue(result);
 assertTrue(defaultDataset.isEmpty());
 }

 @Test
 public void testAddAllWithEmptyCollection() {
 Collection<Instance> emptyCollection = Collections.emptyList();
 boolean result = defaultDataset.addAll(emptyCollection);
 assertTrue(result);
 assertTrue(defaultDataset.isEmpty());
 }

 @Test
 public void testAddAllWithWrongInstanceType() {
 Collection<String> stringCollection = new ArrayList<>();
 stringCollection.add("test");
 boolean result = defaultDataset.addAll(stringCollection);
 assertFalse(result);
 }

 @Test
 public void testAddWithNullInstance() {
 Instance nullInstance = null;
 boolean result = defaultDataset.add(nullInstance);
 assertFalse(result);
 }

 @Test
 public void testAddWithWrongInstanceType() {
 Object wrongInstance = new Object();
 boolean result = defaultDataset.add(wrongInstance);
 assertFalse(result);
 }

 @Test
 public void testAddElementAtWithInvalidIndex() {
 try {
 defaultDataset.addElementAt(instance, -1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testSetElementAtWithNullInstance() {
 Instance nullInstance = null;
 defaultDataset.setElementAt(nullInstance, 0);
 assertEquals(defaultDataset.get(0), instance); // it should not change anything
 }

 @Test
 public void testSetElementAtWithWrongInstanceType() {
 Object wrongInstance = new Object();
 defaultDataset.setElementAt(wrongInstance, 0);
 assertEquals(defaultDataset.get(0), instance); // it should not change anything
 }

}