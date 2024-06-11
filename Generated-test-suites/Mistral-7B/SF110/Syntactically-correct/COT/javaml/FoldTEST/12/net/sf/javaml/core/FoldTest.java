package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class FoldTest {

 private Dataset parentDataSet;
 private int[] indices;
 private Fold fold;

 @Test
 public void testFoldConstructor() {
 parentDataSet = new DefaultDataset();
 indices = new int[5];
 fold = new Fold(parentDataSet, indices);
 assertEquals(parentDataSet, fold.getParent());
 assertArrayEquals(indices, fold.getIndices());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstanceMethod() {
 fold = new Fold(parentDataSet, indices);
 fold.add(new DenseInstance(new double[0]));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesMethod() {
 fold = new Fold(parentDataSet, indices);
 fold.addAll(new ArrayList<Instance>());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAtIndexMethod() {
 fold = new Fold(parentDataSet, indices);
 fold.addAll(new ArrayList<Instance>(), 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClearMethod() {
 fold = new Fold(parentDataSet, indices);
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceMethod() {
 fold = new Fold(parentDataSet, indices);
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceAtIndexMethod() {
 fold = new Fold(parentDataSet, indices);
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllMethod() {
 fold = new Fold(parentDataSet, indices);
 fold.retainAll(new ArrayList<Instance>());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstanceMethod() {
 fold = new Fold(parentDataSet, indices);
 fold.set(0, new DenseInstance(new double[0]));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstancesFoldsMethod() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, indices);
 assertThrows(UnsupportedOperationException.class, () -> fold.folds(2, new Random()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearestMethod() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, indices);
 assertThrows(UnsupportedOperationException.class, () -> fold.kNearest(2, new DenseInstance(new double[0]), new EuclideanDistanceMeasure()));
 }

 @Test
 public void testInstanceMethod() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, indices);
 Instance instance = fold.instance(0);
 assertNotNull(instance);
 }

 @Test
 public void testSizeMethod() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, indices);
 assertEquals(2, fold.size());
 }

 @Test
 public void testNoAttributesMethod() {
 parentDataSet = new DefaultDataset();
 fold = new Fold(parentDataSet, new int[0]);
 assertEquals(0, fold.noAttributes());
 }

 @Test
 public void testGetParentMethod() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, new int[0]);
 assertEquals(parentDataSet, fold.getParent());
 }

 @Test
 public void testGetClassesMethod() {
 parentDataSet.add(new DenseInstance(new double[0], "Class1"));
 parentDataSet.add(new DenseInstance(new double[0], "Class2"));
 parentDataSet.add(new DenseInstance(new double[0], "Class3"));
 fold = new Fold(parentDataSet, new int[0]);
 assertEquals(parentDataSet.classes(), fold.classes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstanceAtIndexMethod() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, indices);
 fold.add(1, new DenseInstance(new double[0]));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorOutOfBounds() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, new int[0]);
 Iterator<Instance> iterator = fold.iterator();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next(); // index out of bounds
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIteratorConstructorWithIndexOutOfBounds() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, new int[0]);
 ListIterator<Instance> listIterator = new FoldIterator(fold, -1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testListIteratorNextWithIndexOutOfBounds() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, new int[0]);
 ListIterator<Instance> listIterator = fold.listIterator();
 listIterator.next();
 listIterator.next();
 listIterator.next();
 listIterator.next(); // index out of bounds
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIteratorRemove() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, new int[0]);
 ListIterator<Instance> listIterator = fold.listIterator();
 listIterator.next();
 listIterator.remove();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIteratorAdd() {
 parentDataSet.add(new DenseInstance(new double[0]));
 parentDataSet.add(new DenseInstance(new double[0]));
 fold = new Fold(parentDataSet, new int[0]);
 ListIterator<Instance> listIterator = fold.listIterator();
 listIterator.next();
 listIterator.add(new DenseInstance(new double[0]));
 }
}