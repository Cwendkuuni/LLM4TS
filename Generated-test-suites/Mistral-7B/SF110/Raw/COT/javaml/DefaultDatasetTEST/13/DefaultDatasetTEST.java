package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private DefaultDataset emptyDataset;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 dataset = new DefaultDataset();
 Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, new Object[] { "Class1" });
 Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, new Object[] { "Class2" });
 Instance instance3 = new DenseInstance(new double[] { 7.0, 8.0, 9.0 }, new Object[] { "Class1" });
 dataset.addAll(Arrays.asList(instance1, instance2, instance3));

 emptyDataset = new DefaultDataset();
 }

 @Test
 public void testDefaultDataset() {
 DefaultDataset defaultDataset = new DefaultDataset();
 assertEquals(0, defaultDataset.size());
 }

 @Test
 public void testDefaultDatasetConstructorEmpty() {
 DefaultDataset defaultDataset = new DefaultDataset();
 assertEquals(0, defaultDataset.size());
 }

 @Test
 public void testDefaultDatasetConstructorCollection() {
 assertEquals(3, dataset.size());
 }

 @Test
 public void testAddAll() {
 Instance instance4 = new DenseInstance(new double[] { 10.0, 11.0, 12.0 }, new Object[] { "Class2" });
 dataset.addAll(Arrays.asList(instance4));
 assertEquals(4, dataset.size());
 }

 @Test
 public void testAddAllIndex() {
 Instance instance4 = new DenseInstance(new double[] { 10.0, 11.0, 12.0 }, new Object[] { "Class2" });
 dataset.addAll(1, Arrays.asList(instance4));
 assertEquals(4, dataset.size());
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAdd() {
 Instance instance4 = new DenseInstance(new double[] { 10.0, 11.0, 12.0 }, new Object[] { "Class3" });
 dataset.add(instance4);
 assertEquals(5, dataset.size());
 }

 @Test
 public void testAddIndex() {
 Instance instance4 = new DenseInstance(new double[] { 10.0, 11.0, 12.0 }, new Object[] { "Class3" });
 dataset.add(1, instance4);
 assertEquals(5, dataset.size());
 }

 @Test
 public void testAddElement() {
 Instance instance4 = new DenseInstance(new double[] { 10.0, 11.0, 12.0 }, new Object[] { "Class3" });
 dataset.addElement(instance4);
 assertEquals(5, dataset.size());
 }

 @Test
 public void testInsertElementAt() {
 Instance instance4 = new DenseInstance(new double[] { 10.0, 11.0, 12.0 }, new Object[] { "Class3" });
 dataset.insertElementAt(instance4, 1);
 assertEquals(5, dataset.size());
 }

 @Test
 public void testSetElementAt() {
 Instance instance4 = new DenseInstance(new double[] { 10.0, 11.0, 12.0 }, new Object[] { "Class3" });
 dataset.setElementAt(instance4, 1);
 assertEquals(5, dataset.size());
 }

 @Test
 public void testInstance() {
 assertEquals(instance1, dataset.instance(0));
 }

 @Test
 public void testClasses() {
 SortedSet<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains("Class1"));
 assertTrue(classes.contains("Class2"));
 }

 @Test
 public void testKNearest() {
 Instance testInstance = new DenseInstance(new double[] { 2.5, 3.5, 4.5 }, new Object[] { "Class1" });
 DistanceMeasure euclideanDistance = new EuclideanDistance();
 Set<Instance> nearestInstances = dataset.kNearest(2, testInstance, euclideanDistance);
 assertEquals(2, nearestInstances.size());
 assertTrue(nearestInstances.contains(instance1));
 assertTrue(nearestInstances.contains(instance3));
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> closestInstances = new HashMap<>();
 closestInstances.put(instance2, 2.2);
 closestInstances.put(instance1, 3.5);
 closestInstances.put(instance3, 1.2);
 double maxDistance = dataset.removeFarthest(closestInstances);
 assertEquals(instance2.distance(testInstance), maxDistance, 0.001);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldsNullRandom() {
 dataset.folds(5, null);
 }

 @Test
 public void testFolds() {
 Random random = new Random();
 Dataset[] folds = dataset.folds(5, random);
 assertEquals(5, folds.length);
 for (Dataset fold : folds) {
 assertEquals(dataset.size() / 5, fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(1, dataset.classIndex("Class1"));
 }

 @Test
 public void testClassValue() {
 assertEquals("Class1", dataset.classValue(0));
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(dataset.size(), copiedDataset.size());
 for (int i = 0; i < copiedDataset.size(); i++) {
 assertEquals(dataset.instance(i), copiedDataset.instance(i));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testCheckNullCollection() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.check(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.check(new Instance() {});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckInstanceWithMoreAttributes() {
 DefaultDataset dataset = new DefaultDataset();
 Instance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 }, new Object[] { "Class1" });
 dataset.check(instance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetIndexOutOfBounds() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, new Object[] { "Class1" }));
 try {
 dataset.instance(100);
 } catch (IndexOutOfBoundsException e) {
 assertEquals(1, e.getExpectedSize());
 }
 }

 @Test(expected = ClassCastException.class)
 public void testClassValueClassCastException() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, new String[] { "Class1" }));
 try {
 dataset.classValue(0);
 } catch (ClassCastException e) {
 // Expected
 }
 }
}