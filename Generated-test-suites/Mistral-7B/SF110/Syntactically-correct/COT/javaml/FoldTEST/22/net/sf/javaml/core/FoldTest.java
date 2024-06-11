package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class FoldTest {

 private Dataset dataset;
 private Instance instance1, instance2;
 private int[] indices1 = {0, 2, 1};
 private int[] indices2 = {2, 1, 0};

 @Before
 public void setup() {
 dataset = new DefaultDataset();
 instance1 = new DefaultInstance(new double[] {1.0, 2.0});
 instance2 = new DefaultInstance(new double[] {3.0, 4.0});
 dataset.add(instance1);
 dataset.add(instance2);
 }

 @Test
 public void testConstructor() {
 Fold fold1 = new Fold(dataset, indices1);
 assertNotNull(fold1.parent);
 assertNotNull(fold1.indices);
 assertEquals(indices1.length, fold1.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 Fold fold = new Fold(dataset, indices1);
 fold.add(new DefaultInstance(new double[] {5.0, 6.0}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Fold fold = new Fold(dataset, indices1);
 fold.folds(5, new Random());
 }

 @Test
 public void testInstance() {
 Fold fold = new Fold(dataset, indices1);
 Instance expected = instance1;
 Instance result = fold.instance(0);
 assertSame(expected, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceNegativeIndex() {
 Fold fold = new Fold(dataset, indices1);
 fold.instance(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceIndexOutOfBounds() {
 Fold fold = new Fold(dataset, indices1);
 fold.instance(indices1.length);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 Fold fold = new Fold(dataset, indices1);
 fold.set(0, new DefaultInstance(new double[] {5.0, 6.0}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 Fold fold = new Fold(dataset, indices1);
 List<Instance> instances = Arrays.asList(new DefaultInstance(new double[] {5.0, 6.0}), new DefaultInstance(new double[] {7.0, 8.0}));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllIndex() {
 Fold fold = new Fold(dataset, indices1);
 List<Instance> instances = Arrays.asList(new DefaultInstance(new double[] {5.0, 6.0}), new DefaultInstance(new double[] {7.0, 8.0}));
 fold.addAll(1, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 Fold fold = new Fold(dataset, indices1);
 fold.remove(instance1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 Fold fold = new Fold(dataset, indices1);
 List<Instance> instances = Arrays.asList(instance2);
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Fold fold = new Fold(dataset, indices1);
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllCollection() {
 Fold fold = new Fold(dataset, indices1);
 List<Instance> instances = Arrays.asList(instance1, instance2);
 fold.addAll(instances);
 }

 @Test
 public void testClasses() {
 Fold fold = new Fold(dataset, indices1);
 Set<Object> classes = fold.classes();
 assertSame(dataset.classes(), classes);
 }

 @Test
 public void testSize() {
 Fold fold1 = new Fold(dataset, indices1);
 Fold fold2 = new Fold(dataset, indices2);
 assertEquals(indices1.length, fold1.size());
 assertEquals(indices2.length, fold2.size());
 }

 @Test
 public void testIterator() {
 Fold fold = new Fold(dataset, indices1);
 List<Instance> expected = Collections.nCopies(indices1.length, instance1);
 List<Instance> result = new ArrayList<>();
 for (Instance instance : fold) {
 result.add(instance);
 }
 assertEquals(expected, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAll() {
 Fold fold = new Fold(dataset, indices1);
 List<Instance> instances = Arrays.asList(instance1);
 fold.removeAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveIf() {
 Fold fold = new Fold(dataset, indices1);
 fold.removeIf(i -> true);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllCollection() {
 Fold fold = new Fold(dataset, indices1);
 List<Instance> instances = Arrays.asList(instance2);
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIterator() {
 Fold fold = new Fold(dataset, indices1);
 ListIterator<Instance> iterator = fold.listIterator();
 iterator.add(new DefaultInstance(new double[] {5.0, 6.0}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIteratorNegativeIndex() {
 Fold fold = new Fold(dataset, indices1);
 ListIterator<Instance> iterator = fold.listIterator(-1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIteratorIndexOutOfBounds() {
 Fold fold = new Fold(dataset, indices1);
 ListIterator<Instance> iterator = fold.listIterator(indices1.length);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIteratorRemove() {
 Fold fold = new Fold(dataset, indices1);
 ListIterator<Instance> iterator = fold.listIterator();
 iterator.remove();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIteratorAdd() {
 Fold fold = new Fold(dataset, indices1);
 ListIterator<Instance> iterator = fold.listIterator();
 iterator.add(new DefaultInstance(new double[] {5.0, 6.0}));
 }

 @Test
 public void testNoAttributes() {
 Fold fold = new Fold(dataset, indices1);
 assertEquals(dataset.noAttributes(), fold.noAttributes());
 }

 @Test
 public void testClassIndex() {
 Fold fold = new Fold(dataset, indices1);
 assertEquals(dataset.classIndex("TestClass"), fold.classIndex("TestClass"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCopy() {
 Fold fold = new Fold(dataset, indices1);
 Fold copy = fold.copy();
 }
}