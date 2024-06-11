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
 private DistanceMeasure distanceMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 distanceMock = mock(DistanceMeasure.class);
 instance1 = mock(Instance.class);
 instance2 = mock(Instance.class);
 instance3 = mock(Instance.class);
 dataset = new DefaultDataset();
 }

 @Test
 public void testAddAll() {
 Collection<Instance> instances = Arrays.asList(instance1, instance2, instance3);
 dataset.addAll(instances);
 assertEquals(3, dataset.size());
 }

 @Test
 public void testAddAll_ThrowsException() {
 Collection<Instance> instances = Arrays.asList(instance1, instance2, instance3);
 try {
 dataset.addAll(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 dataset.addAll(Collections.emptyList());
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAdd() {
 dataset.add(instance1);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testAdd_ThrowsException() {
 try {
 dataset.add(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 dataset.add(instance1);
 dataset.add(instance1);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testAddElement() {
 dataset.addElement(instance1);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testInsertElementAt() {
 dataset.add(instance1);
 dataset.insertElementAt(instance2, 0);
 assertEquals(2, dataset.size());
 }

 @Test
 public void testSetElementAt() {
 dataset.add(instance1);
 dataset.setElementAt(instance2, 0);
 assertEquals(instance2, dataset.getInstance(0));
 }

 @Test
 public void testInstance() {
 dataset.add(instance1);
 assertEquals(instance1, dataset.getInstance(0));
 }

 @Test
 public void testClasses() {
 dataset.add(instance1);
 assertEquals(instance1.getClassValue(), dataset.classes().first());
 }

 @Test
 public void testKNearest_NullInstance() {
 assertNull(dataset.kNearest(1, null, distanceMock));
 }

 @Test
 public void testKNearest_EmptyDataset() {
 assertNull(dataset.kNearest(1, instance1, distanceMock));
 }

 @Test
 public void testKNearest() {
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 Instance[] result = dataset.kNearest(2, instance1, distanceMock).toArray(new Instance[0]);
 assertArrayEquals(new Instance[] { instance1, instance2 }, result);
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 Dataset[] result = dataset.folds(numFolds, mock(Random.class));
 assertEquals(numFolds, result.length);
 for (Dataset ds : result) {
 assertNotNull(ds);
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 dataset.add(instance1);
 dataset.add(instance2);
 assertEquals(instance1.getClassValue().getClass(), dataset.classes().headSet(instance1.getClassValue()).getClass());
 assertEquals(instance2.getClassValue().getClass(), dataset.classes().headSet(instance2.getClassValue()).getClass());
 assertEquals(-1, dataset.classIndex(null));
 }

 @Test
 public void testClassValue() {
 dataset.add(instance1);
 assertEquals(instance1.getClassValue(), dataset.classValue(0));
 assertEquals(null, dataset.classValue(1));
 }

 @Test
 public void testCopy() {
 DefaultDataset copy = dataset.copy();
 assertEquals(dataset.size(), copy.size());
 }
}