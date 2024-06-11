package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasureMock;

 @Before
 public void setup() {
 dataset = new DefaultDataset();
 distanceMeasureMock = mock(DistanceMeasure.class);
 instance1 = mock(Instance.class);
 instance2 = mock(Instance.class);
 instance3 = mock(Instance.class);
 }

 @Test
 public void testAddAll() {
 Collection<Instance> instances = Arrays.asList(instance1, instance2, instance3);
 dataset.addAll(instances);
 assertEquals(instances.size(), dataset.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllThrowsExceptionWhenFrozen() {
 dataset.freeze();
 try {
 dataset.addAll(Collections.singletonList(instance1));
 } catch (UnsupportedOperationException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test
 public void testAdd() {
 dataset.add(instance1);
 assertEquals(1, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 dataset.add(null);
 }

 @Test
 public void testClasses() {
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(2, dataset.classes().size());
 assertEquals(instance1.classValue(), dataset.classes().first());
 assertEquals(instance2.classValue(), dataset.classes().last());
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testGet() {
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance1, dataset.get(0));
 assertEquals(instance2, dataset.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 dataset.add(instance1);
 assertEquals(instance1, dataset.get(0));
 try {
 dataset.get(1);
 } catch (IndexOutOfBoundsException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test
 public void testInstance() {
 dataset.add(instance1);
 assertEquals(instance1, dataset.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceOutOfBounds() {
 dataset.add(instance1);
 try {
 dataset.instance(1);
 } catch (IndexOutOfBoundsException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test
 public void testKNearest() {
 List<Instance> nearestInstances = Arrays.asList(instance1, instance2, instance3);
 when(distanceMeasureMock.measure(instance1, instance2)).thenReturn(1.0);
 when(distanceMeasureMock.measure(instance1, instance3)).thenReturn(2.0);
 when(distanceMeasureMock.compare(1.0, 2.0)).thenReturn(-1);

 Instance expectedNearest = instance2;
 Instance[] result = dataset.kNearest(1, instance1, distanceMeasureMock).toArray(new Instance[0]);
 assertEquals(1, result.length);
 assertEquals(expectedNearest, result[0]);
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> instancesWithDistances = new HashMap<>();
 instancesWithDistances.put(instance1, 1.0);
 instancesWithDistances.put(instance2, 2.0);
 instancesWithDistances.put(instance3, 3.0);

 double farthestDistance = dataset.removeFarthest(instancesWithDistances);
 assertEquals(3.0, farthestDistance);
 assertEquals(2, instancesWithDistances.size());
 }

 @Test
 public void testFolds() {
 Random randomMock = mock(Random.class);
 int numFolds = 3;
 Dataset[] folds = dataset.folds(numFolds, randomMock);
 assertEquals(numFolds, folds.length);
 }

 @Test
 public void testNoAttributes() {
 Instance instanceWithThreeAttributes = mock(Instance.class);
 when(instanceWithThreeAttributes.noAttributes()).thenReturn(3);
 dataset.add(instanceWithThreeAttributes);
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(0, dataset.classIndex(instance1.classValue()));
 assertEquals(1, dataset.classIndex(instance2.classValue()));
 }

 @Test(expected = NullPointerException.class)
 public void testClassIndexNullClass() {
 dataset.add(instance1);
 dataset.add(instance2);
 try {
 dataset.classIndex(null);
 } catch (NullPointerException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(dataset.size(), copiedDataset.size());
 }
}