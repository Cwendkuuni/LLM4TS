package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import net.sf.javaml.distance.*;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasureMock;

 @Test
 public void testAddAll() {
 dataset = new DefaultDataset();

 Collection<Instance> instances = Arrays.asList(instance1, instance2, instance3);
 dataset.addAll(instances);

 assertEquals(3, dataset.size());
 }

 @Test
 public void testAddAll_Duplicates() {
 dataset = new DefaultDataset();

 Collection<Instance> instances = Arrays.asList(instance1, instance1, instance2, instance3);
 dataset.addAll(instances);

 assertEquals(3, dataset.size());
 }

 @Test
 public void testAddAll_NullCollection() {
 dataset = new DefaultDataset();

 Collection<Instance> nullCollection = null;

 assertFalse(dataset.addAll(nullCollection));
 }

 @Test
 public void testAddAll_NullInstance() {
 dataset = new DefaultDataset();

 Instance nullInstance = null;

 Collection<Instance> instances = Arrays.asList(instance1, nullInstance, instance2, instance3);

 assertFalse(dataset.addAll(instances));
 }

 @Test
 public void testCheck() {
 dataset = new DefaultDataset();

 Instance instance = new DenseInstance(new double[5]);

 dataset.check(Arrays.asList(instance));

 assertEquals(5, dataset.maxAttributes);
 }

 @Test
 public void testClear() {
 dataset = new DefaultDataset();

 dataset.add(instance1);
 dataset.add(instance2);
 dataset.clear();

 assertEquals(0, dataset.size());
 }

 @Test
 public void testKNearest() {
 dataset = new DefaultDataset();

 instance1 = mock(Instance.class);
 instance2 = mock(Instance.class);
 instance3 = mock(Instance.class);

 when(instance1.noAttributes()).thenReturn(3);
 when(instance2.noAttributes()).thenReturn(3);
 when(instance3.noAttributes()).thenReturn(5);

 distanceMeasureMock = mock(DistanceMeasure.class);

 when(distanceMeasureMock.compare(anyDouble(), anyDouble())).then(i -> i.getFirst().doubleValue() > i.getSecond().doubleValue());

 when(instance1.distance(instance2, distanceMeasureMock)).thenReturn(1.5);
 when(instance1.distance(instance3, distanceMeasureMock)).thenReturn(2.5);
 when(instance2.distance(instance1, distanceMeasureMock)).thenReturn(1.6);
 when(instance2.distance(instance3, distanceMeasureMock)).thenReturn(1.9);
 when(instance3.distance(instance1, distanceMeasureMock)).thenReturn(2.1);
 when(instance3.distance(instance2, distanceMeasureMock)).thenReturn(1.8);

 List<Instance> expectedInstances = Arrays.asList(instance1, instance2);

 Dataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3));

 Set<Instance> actualInstances = dataset.kNearest(2, instance3, distanceMeasureMock);

 assertEquals(expectedInstances, actualInstances);
 }

 @Test
 public void testRemoveFarthest() {
 dataset = new DefaultDataset();

 Map<Instance, Double> map = new HashMap<>();
 map.put(instance1, 1.5);
 map.put(instance2, 1.6);
 map.put(instance3, 1.8);

 assertEquals(instance3, dataset.removeFarthest(map));
 }

 @Test
 public void testFolds() {
 dataset = new DefaultDataset();

 Random randomMock = mock(Random.class);

 when(randomMock.nextInt(anyInt())).thenReturn(0);

 Dataset[] expectedFolds = new Dataset[3];
 for (int i = 0; i < 3; i++) {
 expectedFolds[i] = mock(Dataset.class);
 }

 Dataset[] actualFolds = dataset.folds(3, randomMock);

 for (int i = 0; i < 3; i++) {
 assertEquals(expectedFolds[i], actualFolds[i]);
 }
 }

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 instance1 = new DenseInstance(new double[5]);
 instance2 = new DenseInstance(new double[5]);
 instance3 = new DenseInstance(new double[5]);
 }
}