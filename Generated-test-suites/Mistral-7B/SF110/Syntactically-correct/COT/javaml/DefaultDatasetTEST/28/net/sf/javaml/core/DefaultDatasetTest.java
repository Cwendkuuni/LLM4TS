package net.sf.javaml.core;

import static org.junit.Assert.*;
import net.sf.javaml.distance.*;
import org.junit.*;
import java.util.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 dataset = new DefaultDataset();
 distanceMeasure = new EuclideanDistance();

 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
 instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{2.0});
 instance3 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});

 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 }

 @Test
 public void testDefaultDataset() {
 assertNotNull(dataset);
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAddAll() {
 dataset.addAll(Arrays.asList(instance1, instance2, instance3));
 assertEquals(3, dataset.size());
 }

 @Test
 public void testAddInstance() {
 dataset.add(instance1);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testAddAllWithEmptyCollection() {
 dataset.addAll(new ArrayList<Instance>());
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAddAllWithNullCollection() {
 try {
 dataset.addAll(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddInstanceWithNullInstance() {
 try {
 dataset.add(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddInstanceWithAlreadyExistingInstance() {
 dataset.add(instance1);
 dataset.add(instance1);
 assertEquals(2, dataset.size());
 }

 @Test
 public void testClasses() {
 Set<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains(instance1.classValue()));
 assertTrue(classes.contains(instance2.classValue()));
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testGetInstance() {
 Instance instance = dataset.getInstance(0);
 assertEquals(instance1, instance);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInstanceWithNegativeIndex() {
 dataset.getInstance(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInstanceWithIndexGreaterThanSize() {
 dataset.getInstance(3);
 }

 @Test
 public void testGetInstanceWithNullIndex() {
 try {
 dataset.getInstance(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testInstance() {
 assertEquals(instance1, dataset.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceWithNegativeIndex() {
 dataset.instance(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceWithIndexGreaterThanSize() {
 dataset.instance(3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceWithNullIndex() {
 try {
 dataset.instance(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testKNearest() {
 Set<Instance> neighbors = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, neighbors.size());
 assertTrue(neighbors.contains(instance1));
 assertTrue(neighbors.contains(instance2));
 assertFalse(neighbors.contains(instance3));
 }

 @Test
 public void testKNearestWithLessInstancesThanK() {
 Set<Instance> neighbors = dataset.kNearest(4, instance1, distanceMeasure);
 assertEquals(3, neighbors.size());
 }

 @Test
 public void testKNearestWithNullInstance() {
 try {
 dataset.kNearest(1, null, distanceMeasure);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testKNearestWithNullDistanceMeasure() {
 try {
 dataset.kNearest(1, instance1, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> distances = new HashMap<>();
 distances.put(instance1, 3.0);
 distances.put(instance2, 1.0);
 distances.put(instance3, 2.0);
 double maxDistance = dataset.removeFarthest(distances);
 assertEquals(instance2.classValue(), dataset.instance(0).classValue());
 assertEquals(1.0, maxDistance, 0.0);
 }

 @Test
 public void testFolds() {
 Dataset[] folds = dataset.folds(3, new Random());
 assertEquals(3, folds.length);
 for (Dataset fold : folds) {
 assertTrue(fold instanceof Fold);
 assertEquals(1, fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(1, dataset.classIndex(instance1.classValue()));
 }

 @Test
 public void testClassValue() {
 assertEquals(instance1.classValue(), dataset.classValue(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueWithNegativeIndex() {
 dataset.classValue(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueWithIndexGreaterThanSize() {
 dataset.classValue(3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueWithNullIndex() {
 try {
 dataset.classValue(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = (DefaultDataset) dataset.copy();
 assertEquals(3, copiedDataset.size());
 }

}