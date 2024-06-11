package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import net.sf.javaml.distance.*;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance testInstance;
 private DistanceMeasure mockDistanceMeasure;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mockDistanceMeasure = mock(DistanceMeasure.class);
 testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, new Object[] {"class1"});
 dataset = new DefaultDataset();
 dataset.add(testInstance);
 }

 @Test
 public void testAddAll() {
 Collection<Instance> instances = Arrays.asList(new DenseInstance(new double[] {1.0, 2.0, 3.0}, new Object[] {"class1"}),
 new DenseInstance(new double[] {2.0, 2.0, 2.0}, new Object[] {"class2"}));
 dataset.addAll(instances);
 assertEquals(3, dataset.size());
 }

 @Test
 public void testAddAll_emptyCollection() {
 dataset.addAll(Collections.emptyList());
 assertEquals(1, dataset.size());
 }

 @Test
 public void testAddAll_nullCollection() {
 dataset.addAll(null);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAdd() {
 Instance newInstance = new DenseInstance(new double[] {3.0, 3.0, 3.0}, new Object[] {"class3"});
 dataset.add(newInstance);
 assertEquals(2, dataset.size());
 }

 @Test
 public void testAdd_sameInstance() {
 dataset.add(testInstance);
 dataset.add(testInstance);
 assertEquals(2, dataset.size());
 }

 @Test
 public void testAdd_nullInstance() {
 dataset.add(null);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testCheck() {
 Collection<Instance> instances = Arrays.asList(new DenseInstance(new double[] {1.0, 2.0, 3.0}, new Object[] {"class1"}),
 new DenseInstance(new double[] {2.0, 2.0, 2.0}, new Object[] {"class2"}));
 dataset.addAll(instances);
 dataset.check(instances);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheck_instanceWithMoreAttributes() {
 Instance instanceWithMoreAttributes = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0}, new Object[] {"class1"});
 dataset.add(instanceWithMoreAttributes);
 }

 @Test
 public void testClasses() {
 assertEquals(1, dataset.classes().size());
 assertEquals("class1", dataset.classes().first());
 }

 @Test
 public void testInstance() {
 assertEquals(testInstance, dataset.instance(0));
 }

 @Test
 public void testKNearest() {
 Set<Instance> expectedNearestInstances = new HashSet<>();
 expectedNearestInstances.add(testInstance);
 Set<Instance> actualNearestInstances = dataset.kNearest(1, testInstance, mockDistanceMeasure);
 assertEquals(expectedNearestInstances, actualNearestInstances);
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> instancesAndDistances = new HashMap<>();
 instancesAndDistances.put(testInstance, 1.0);
 double maxDistance = dataset.removeFarthest(instancesAndDistances);
 assertEquals(1.0, maxDistance);
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 Random mockRandom = mock(Random.class);
 Dataset[] actualFolds = dataset.folds(numFolds, mockRandom);
 assertEquals(numFolds, actualFolds.length);
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(0, dataset.classIndex("class1"));
 }

 @Test
 public void testClassValue() {
 assertEquals("class1", dataset.classValue(0));
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(1, copiedDataset.size());
 }
}