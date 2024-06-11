package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import net.sf.javaml.distance.*;
import net.sf.javaml.instance.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instanceMock;
 private DistanceMeasure distanceMeasureMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataset = new DefaultDataset();
 instanceMock = mock(Instance.class);
 distanceMeasureMock = mock(DistanceMeasure.class);
 }

 @After
 public void tearDown() {
 dataset = null;
 instanceMock = null;
 distanceMeasureMock = null;
 }

 @Test
 public void testAddAll() {
 // Given
 ArrayList<Instance> instances = new ArrayList<>();
 instances.add(instanceMock);

 // When
 dataset.addAll(instances);

 // Then
 verify(dataset, times(instanceMock.getClass().getDeclaredMethods().length)).add(instanceMock);
 }

 @Test
 public void testAddAllFolds() {
 // Given
 int numFolds = 3;
 Random randomMock = mock(Random.class);
 when(randomMock.nextInt(anyInt())).thenAnswer(i -> i.intValue() % numFolds);
 DefaultDataset dataset = new DefaultDataset();
 Instance instance1 = mock(Instance.class);
 Instance instance2 = mock(Instance.class);
 Instance instance3 = mock(Instance.class);

 // When
 Dataset[] result = dataset.folds(numFolds, randomMock);

 // Then
 assertEquals(numFolds, result.length);
 for (Dataset datasetFold : result) {
 assertTrue(datasetFold instanceof Fold);
 }
 }

 @Test
 public void testClear() {
 // Given
 Instance instance1 = mock(Instance.class);
 Instance instance2 = mock(Instance.class);
 dataset.add(instance1);
 dataset.add(instance2);

 // When
 dataset.clear();

 // Then
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testInstance() {
 // Given
 Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"A", "B", "C"});
 dataset.add(instance);

 // When
 Instance result = dataset.instance(0);

 // Then
 assertEquals(instance, result);
 }

 @Test
 public void testClasses() {
 // Given
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"A"});
 Instance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"B"});
 dataset.add(instance1);
 dataset.add(instance2);

 // When
 SortedSet<Object> classes = dataset.classes();

 // Then
 assertEquals(2, classes.size());
 assertTrue(classes.contains("A"));
 assertTrue(classes.contains("B"));
 }

 @Test
 public void testKNearest() {
 // Given
 int k = 3;
 Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"Class1"});
 Instance instance1 = new DenseInstance(new double[]{1.2, 2.1, 3.1}, new String[]{"Class1"});
 Instance instance2 = new DenseInstance(new double[]{1.3, 2.2, 3.2}, new String[]{"Class2"});
 Instance instance3 = new DenseInstance(new double[]{1.4, 2.3, 3.3}, new String[]{"Class1"});
 Instance instance4 = new DenseInstance(new double[]{1.5, 2.4, 3.4}, new String[]{"Class2"});
 dataset.add(instance);
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 dataset.add(instance4);

 // When
 Instance[] result = dataset.kNearest(k, instance, distanceMeasureMock).toArray(new Instance[0]);

 // Then
 assertEquals(k, result.length);
 assertArrayEquals(new Object[]{instance1, instance3, instance}, result, new ComparatorInstanceValue());
 }
}