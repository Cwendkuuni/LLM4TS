package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import net.sf.javaml.distance.*;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Collection<Instance> instances;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasure;
 private Random random;

 @Before
 public void setUp() {
 instances = new ArrayList<>();
 instance1 = mock(Instance.class);
 instance2 = mock(Instance.class);
 instance3 = mock(Instance.class);
 when(instance1.noAttributes()).thenReturn(3);
 when(instance1.classValue()).thenReturn("class1");
 when(instance2.noAttributes()).thenReturn(3);
 when(instance2.classValue()).thenReturn("class2");
 when(instance3.noAttributes()).thenReturn(3);
 when(instance3.classValue()).thenReturn("class1");
 distanceMeasure = mock(DistanceMeasure.class);
 random = mock(Random.class);
 dataset = new DefaultDataset();
 }

 @Test
 public void testDefaultConstructor() {
 assertEquals(0, dataset.size());
 assertNull(dataset.classes());
 }

 @Test
 public void testConstructorWithCollection() {
 instances.add(instance1);
 instances.add(instance2);
 dataset = new DefaultDataset(instances);
 assertEquals(2, dataset.size());
 assertEquals(2, dataset.classes().size());
 assertTrue(dataset.classes().containsAll(Arrays.asList("class1", "class2")));
 }

 @Test
 public void testAddAll() {
 instances.add(instance1);
 instances.add(instance2);
 dataset.addAll(instances);
 assertEquals(2, dataset.size());
 assertEquals(2, dataset.classes().size());
 assertTrue(dataset.classes().containsAll(Arrays.asList("class1", "class2")));
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 assertNull(dataset.classes());
 }

 @Test
 public void testAdd() {
 dataset.add(instance1);
 assertEquals(1, dataset.size());
 assertEquals(1, dataset.classes().size());
 assertTrue(dataset.classes().contains("class1"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllNullCollection() {
 dataset.addAll(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllWithNullInstance() {
 instances.add(instance1);
 instances.add(null);
 dataset.addAll(instances);
 }

 @Test
 public void testInsertElementAt() {
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.insertElementAt(instance1, 0);
 assertEquals(2, dataset.size());
 assertEquals(2, dataset.classes().size());
 assertEquals(instance1, dataset.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetElementAtNullInstance() {
 dataset.add(instance1);
 dataset.setElementAt(null, 0);
 }

 @Test
 public void testSetElementAt() {
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.setElementAt(instance3, 0);
 assertEquals(2, dataset.size());
 assertEquals(2, dataset.classes().size());
 assertEquals(instance3, dataset.instance(0));
 }

 @Test
 public void testGet() {
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance1, dataset.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInvalidIndex() {
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.get(2);
 }

 @Test
 public void testClasses() {
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(2, dataset.classes().size());
 assertTrue(dataset.classes().containsAll(Arrays.asList("class1", "class2")));
 }

 @Test
 public void testKNearest() {
 when(distanceMeasure.measure(instance1, instance2)).thenReturn(2.0);
 when(distanceMeasure.measure(instance1, instance3)).thenReturn(1.0);
 when(distanceMeasure.measure(instance2, instance3)).thenReturn(3.0);
 dataset.addAll(Arrays.asList(instance1, instance2, instance3));
 assertEquals(2, dataset.size());
 List<Instance> result = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, result.size());
 assertTrue(result.containsAll(Arrays.asList(instance1, instance3)));
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> map = new HashMap<>();
 map.put(instance1, 1.0);
 map.put(instance2, 2.0);
 map.put(instance3, 3.0);
 double max = dataset.removeFarthest(map);
 assertEquals(3.0, max, 0.0);
 assertEquals(1, map.size());
 assertEquals("instance3", map.keySet().toString());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveFarthestNullMap() {
 dataset.removeFarthest(null);
 }

 @Test
 public void testFolds() {
 when(random.nextInt(anyInt())).thenAnswer(i -> i.intValue() % 3);
 dataset.addAll(Arrays.asList(instance1, instance2, instance3));
 Dataset[] result = dataset.folds(3, random);
 assertEquals(3, result.length);
 for (Dataset ds : result) {
 assertEquals(1, ds.size());
 }
 }

 @Test
 public void testNoAttributes() {
 dataset.add(instance1);
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(1, dataset.classIndex("class1"));
 assertEquals(-1, dataset.classIndex(null));
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = (DefaultDataset) dataset.copy();
 copiedDataset.add(instance1.copy());
 assertEquals(2, copiedDataset.size());
 assertEquals(2, copiedDataset.classes().size());
 assertEquals(instance1, copiedDataset.instance(0));
 }
}