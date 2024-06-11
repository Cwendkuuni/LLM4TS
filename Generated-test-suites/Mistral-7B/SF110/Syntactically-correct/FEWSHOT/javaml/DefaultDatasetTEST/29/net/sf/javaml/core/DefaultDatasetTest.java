package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.distance.SquaredEuclideanDistance;
import net.sf.javaml.instance.*;
import org.junit.Test;

public class DefaultDatasetTest {

 Instance mockInstance() {
 Instances mockInstances = mock(Instances.class);
 when(mockInstances.instance(anyInt())).thenCallRealMethod();
 return mock(Instance.class, mockInstances);
 }

 @Test
 public void testDefaultDatasetEmpty() {
 DefaultDataset dataset = new DefaultDataset();
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testDefaultDatasetAddAll() {
 DefaultDataset dataset = new DefaultDataset();
 Instance inst1 = mockInstance();
 Instance inst2 = mockInstance();
 Instance inst3 = mockInstance();

 dataset.add(inst1);
 dataset.add(inst2);
 dataset.add(inst3);

 assertEquals(3, dataset.size());
 }

 @Test
 public void testDefaultDatasetAddAllNull() {
 DefaultDataset dataset = new DefaultDataset();

 assertFalse(dataset.addAll(null));
 }

 @Test
 public void testDefaultDatasetClear() {
 DefaultDataset dataset = new DefaultDataset();
 Instance inst1 = mockInstance();
 Instance inst2 = mockInstance();
 Instance inst3 = mockInstance();

 dataset.add(inst1);
 dataset.add(inst2);
 dataset.add(inst3);

 assertEquals(3, dataset.size());
 dataset.clear();

 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testDefaultDatasetAdd() {
 DefaultDataset dataset = new DefaultDataset();
 Instance inst1 = mockInstance();

 dataset.add(inst1);

 assertEquals(1, dataset.size());
 }

 @Test
 public void testDefaultDatasetAddNull() {
 DefaultDataset dataset = new DefaultDataset();

 assertFalse(dataset.add(null));
 }

 @Test
 public void testDefaultDatasetAddFold() {
 DefaultDataset dataset = new DefaultDataset();
 Instance inst1 = mockInstance();
 Instance inst2 = mockInstance();
 Instance inst3 = mockInstance();

 dataset.add(inst1);
 dataset.add(inst2);
 dataset.add(inst3);

 Dataset[] folds = dataset.folds(3, mock(Random.class));
 assertEquals(3, folds.length);

 for (Dataset fold : folds) {
 assertEquals(true, fold instanceof Fold);
 }
 }

 @Test
 public void testDefaultDatasetFoldsEmpty() {
 DefaultDataset dataset = new DefaultDataset();

 Dataset[] folds = dataset.folds(3, mock(Random.class));
 assertEquals(3, folds.length);

 for (Dataset fold : folds) {
 assertTrue(fold.isEmpty());
 }
 }

 @Test
 public void testDefaultDatasetRemoveFarthest() {
 DefaultDataset dataset = new DefaultDataset();
 Instance inst1 = mockInstance();
 Instance inst2 = mockInstance();
 Instance inst3 = mockInstance();
 Instance inst4 = mockInstance();
 Instance inst5 = mockInstance();

 dataset.add(inst1);
 dataset.add(inst2);
 dataset.add(inst3);
 dataset.add(inst4);
 dataset.add(inst5);

 Map<Instance, Double> closest = new HashMap<>();
 closest.put(inst1, 1.0);
 closest.put(inst2, 2.0);
 closest.put(inst3, 3.0);
 closest.put(inst4, 4.0);
 closest.put(inst5, 5.0);

 double max = dataset.removeFarthest(closest);
 assertEquals(5.0, max, 0.001);

 assertEquals(4, dataset.size());
 }

 @Test
 public void testDefaultDatasetRemoveFarthestEmpty() {
 DefaultDataset dataset = new DefaultDataset();

 Map<Instance, Double> closest = new HashMap<>();
 double max = dataset.removeFarthest(closest);

 assertEquals(0.0, max, 0.001);

 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testDefaultDatasetKNearest() {
 DefaultDataset dataset = new DefaultDataset();
 Instance inst1 = mockInstance();
 Instance inst2 = mockInstance();
 Instance inst3 = mockInstance();
 Instance inst4 = mockInstance();
 Instance inst5 = mockInstance();
 Instance inst6 = mockInstance();
Instance inst7 = mockInstance();

 dataset.add(inst1);
 dataset.add(inst2);
 dataset.add(inst3);
 dataset.add(inst4);
 dataset.add(inst5);
 dataset.add(inst6);
 dataset.add(inst7);

 DistanceMeasure euclidean = new EuclideanDistance();
 DistanceMeasure squaredEuclidean = new SquaredEuclideanDistance();

 Instance testInstance = mockInstance();

 List<Instance> result = dataset.kNearest(3, testInstance, euclidean);
 assertEquals(3, result.size());

 result = dataset.kNearest(3, testInstance, squaredEuclidean);
 assertEquals(3, result.size());
 }

 @Test
 public void testDefaultDatasetKNearestNull() {
 DefaultDataset dataset = new DefaultDataset();
 Instance testInstance = mockInstance();

 assertNull(dataset.kNearest(3, testInstance, null));
 }

 @Test
 public void testDefaultDatasetKNearestInvalidK() {
 DefaultDataset dataset = new DefaultDataset();
 Instance testInstance = mockInstance();

 assertNull(dataset.kNearest(-1, testInstance, new EuclideanDistance()));
 }

 @Test
 public void testDefaultDatasetKNearestEmpty() {
 DefaultDataset dataset = new DefaultDataset();
 Instance testInstance = mockInstance();

 List<Instance> result = dataset.kNearest(1, testInstance, new EuclideanDistance());
 assertTrue(result.isEmpty());
 }

 @Test
 public void testDefaultDatasetKNearestHasSameInstance() {
 DefaultDataset dataset = new DefaultDataset();
 Instance inst1 = mockInstance();
 Instance inst2 = mockInstance();
 Instance inst3 = mockInstance();
 Instance inst4 = mockInstance();
 Instance inst5 = mockInstance();
 Instance testInstance = mockInstance();

 dataset.add(inst1);
 dataset.add(inst2);
 dataset.add(inst3);
 dataset.add(inst4);
 dataset.add(inst5);
 dataset.add(testInstance);

 List<Instance> result = dataset.kNearest(1, testInstance, new EuclideanDistance());

 assertEquals(1, result.size());
 assertEquals(testInstance, result.get(0));
 }

 @Test
 public void testDefaultDatasetCopy() {
 DefaultDataset dataset = new DefaultDataset();
 Instance inst1 = mockInstance();
 Instance inst2 = mockInstance();
 Instance inst3 = mockInstance();

 dataset.add(inst1);
 dataset.add(inst2);
 dataset.add(inst3);

 DefaultDataset copy = dataset.copy();

 assertEquals(dataset.size(), copy.size());
 }
}