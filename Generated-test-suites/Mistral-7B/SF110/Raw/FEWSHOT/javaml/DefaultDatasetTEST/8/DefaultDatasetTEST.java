package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import net.sf.javaml.distance.*;
import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1;
 private Instance instance2;
 private Instance instance3;
 private DistanceMeasure distanceMock;

 @Before
 public void setup() {
 // Initialize mocks
 distanceMock = mock(DistanceMeasure.class);
 // Initialize instances
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 3.0);
 instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 3.0);
 instance3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, 4.0);
 // Initialize dataset
 dataset = new DefaultDataset();
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 }

 @Test
 public void testAddAll() {
 Collection<Instance> instances = Arrays.asList(new Instance[]{instance1, instance2, instance3});
 dataset.addAll(instances);
 assertEquals(3, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNullCollection() {
 dataset.addAll(null);
 }

 @Test
 public void testAdd() {
 dataset.add(instance1);
 assertEquals(2, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 dataset.add(null);
 }

 @Test
 public void testClasses() {
 assertEquals(3, dataset.classes().size());
 }

 @Test
 public void testClassesNull() {
 assertEquals(0, dataset.classes(null).size());
 }

 @Test
 public void testKNearest() {
 List<Instance> nearest = dataset.kNearest(2, instance1, distanceMock);
 assertEquals(2, nearest.size());
 verify(distanceMock).measure(instance1, instance2);
 verify(distanceMock).measure(instance1, instance3);
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> instancesWithDistances = new HashMap<>();
 instancesWithDistances.put(instance1, 1.0);
 instancesWithDistances.put(instance2, 2.0);
 instancesWithDistances.put(instance3, 3.0);
 double maxDistance = dataset.removeFarthest(instancesWithDistances);
 assertEquals(instance3, instancesWithDistances.get(instance3));
 assertEquals(3.0, maxDistance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 dataset.clear();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 dataset.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetGreaterThanSizeIndex() {
 dataset.get(3);
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 Dataset[] folds = dataset.folds(numFolds, mock(Random.class));
 assertEquals(numFolds, folds.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsZeroFolds() {
 dataset.folds(0, mock(Random.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsNullRandom() {
 dataset.folds(3, null);
 }
}