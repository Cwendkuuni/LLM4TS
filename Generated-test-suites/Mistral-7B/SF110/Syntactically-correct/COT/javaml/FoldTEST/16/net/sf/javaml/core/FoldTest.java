package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

 private static final Dataset parentDataset = new DefaultDataset();

 @Test
 public void testFoldConstructor() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 assertEquals(3, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.add(new DenseInstance(1.0));
 }

 @Test
 public void testClasses() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 assertEquals(fold.parent.classes(), fold.classes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.folds(5, new Random());
 }

 @Test
 public void testInstance() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Instance instance = fold.instance(0);
 assertEquals(parentDataset.instance(0), instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.kNearest(3, new DenseInstance(0.0), new EuclideanDistance());
 }

 @Test
 public void testAdd() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.add(0, new DenseInstance(3.0));
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testAddAll() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.addAll(Arrays.asList(new DenseInstance(3.0), new DenseInstance(4.0)));
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testAddAllStartIndex() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.addAll(1, Arrays.asList(new DenseInstance(3.0), new DenseInstance(4.0)));
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testClear() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.clear();
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testContains() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Instance instance = parentDataset.instance(0);
 assertTrue(fold.contains(instance));
 }

 @Test
 public void testContainsAll() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 List<Instance> instances = Arrays.asList(parentDataset.instance(0), parentDataset.instance(1));
 assertTrue(fold.containsAll(instances));
 }

 @Test
 public void testGet() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Instance instance = fold.get(0);
 assertEquals(parentDataset.instance(0), instance);
 }

 @Test
 public void testIndexOf() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Instance instance = parentDataset.instance(1);
 int index = fold.indexOf(instance);
 assertEquals(1, index);
 }

 @Test
 public void testIsEmpty() {
 Fold fold = new Fold(parentDataset, new int[] {});
 assertTrue(fold.isEmpty());
 }

 @Test
 public void testIterator() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Iterator<Instance> iterator = fold.iterator();
 List<Instance> instances = new ArrayList<>();
 while (iterator.hasNext()) {
 instances.add(iterator.next());
 }
 assertEquals(parentDataset.size(), instances.size());
 for (Instance instance : instances) {
 assertEquals(parentDataset.instance(fold.getIndex(instance)), instance);
 }
 }

 @Test
 public void testLastIndexOf() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 1, 1, 2 });
 Instance instance = parentDataset.instance(1);
 int index = fold.lastIndexOf(instance);
 assertEquals(3, index);
 }

 @Test
 public void testListIterator() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 ListIterator<Instance> listIterator = fold.listIterator();
 List<Instance> instances = new ArrayList<>();
 while (listIterator.hasNext()) {
 instances.add(listIterator.next());
 }
 assertEquals(parentDataset.size(), instances.size());
 for (Instance instance : instances) {
 assertEquals(parentDataset.instance(fold.getIndex(instance)), instance);
 }
 }

 @Test
 public void testListIteratorStartIndex() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 ListIterator<Instance> listIterator = fold.listIterator(1);
 List<Instance> instances = new ArrayList<>();
 while (listIterator.hasNext()) {
 instances.add(listIterator.next());
 }
 assertEquals(parentDataset.size() - 1, instances.size());
 for (Instance instance : instances) {
 assertEquals(parentDataset.instance(fold.getIndex(instance) + 1), instance);
 }
 }

 @Test
 public void testRemove() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.remove(parentDataset.instance(0));
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testRemoveInstance() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Instance instance = parentDataset.instance(1);
 fold.remove(instance);
 assertEquals(2, fold.size());
 assertFalse(fold.contains(instance));
 }

 @Test
 public void testRemoveAll() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.removeAll(Collections.singleton(parentDataset.instance(0)));
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testRetainAll() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.retainAll(Collections.emptySet());
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testSet() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.set(1, new DenseInstance(3.0));
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testSize() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 assertEquals(parentDataset.size(), fold.size());
 }

 @Test
 public void testSubList() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 fold.subList(0, 5);
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testToArray() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Object[] array = fold.toArray();
 assertEquals(parentDataset.size(), array.length);
 for (int i = 0; i < array.length; ++i) {
 assertEquals(parentDataset.instance(i), array[i]);
 }
 }

 @Test
 public void testToArrayWithArray() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 DenseInstance[] array = (DenseInstance[]) fold.toArray(new DenseInstance[fold.size()]);
 assertEquals(parentDataset.size(), array.length);
 for (int i = 0; i < array.length; ++i) {
 assertEquals(parentDataset.instance(i), array[i]);
 }
 }

 @Test
 public void testNoAttributes() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 assertEquals(parentDataset.noAttributes(), fold.noAttributes());
 }

 @Test
 public void testClassIndex() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Object clazz = parentDataset.instance(0).classValue();
 int index = fold.classIndex(clazz);
 assertEquals(0, index);
 }

 @Test
 public void testClassValue() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 int index = fold.getIndex(parentDataset.instance(0));
 assertEquals(parentDataset.classValue(index), parentDataset.instance(0).classValue());
 }

 @Test
 public void testCopy() {
 Dataset copy = new Fold(parentDataset, new int[] { 0, 1, 2 }).copy();
 assertEquals(parentDataset, copy);
 }

 @Test
 public void testFoldIteratorHasNext() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Fold.FoldIterator iterator = fold.iterator();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testFoldIteratorNext() {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Fold.FoldIterator iterator = fold.iterator();
 Instance next = iterator.next();
 assertEquals(parentDataset.instance(0), next);
 }

 @Test
 public void testFoldIteratorRemove() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Fold.FoldIterator iterator = fold.iterator();
 iterator.remove();
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testFoldIteratorAdd() {
 try {
 Fold fold = new Fold(parentDataset, new int[] { 0, 1, 2 });
 Fold.FoldIterator iterator = fold.iterator();
 iterator.add(new DenseInstance(3.0));
 fail("Should have thrown UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }
}