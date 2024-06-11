package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class FoldTest {

 private Dataset parentDataSet;
 private int[] foldIndices;
 private Fold fold;

 @Test
 public void testFoldConstructor() {
 parentDataSet = new DefaultDataset();
 foldIndices = new int[] {0, 1, 2};
 fold = new Fold(parentDataSet, foldIndices);
 assertEquals(3, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold = new Fold(parentDataSet, foldIndices);
 fold.add(new DenseInstance(1.0));
 fail("Adding an instance to a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 fold = new Fold(parentDataSet, foldIndices);
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(1.0));
 fold.addAll(instances);
 fail("Adding instances to a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldsMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 fold.folds(5, new Random());
 fail("The folds method is not yet implemented.");
 }

 @Test
 public void testInstanceMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 Instance instance = fold.instance(0);
 assertNotNull(instance);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceMethodWithInvalidIndex() {
 fold = new Fold(parentDataSet, foldIndices);
 fold.instance(fold.size());
 fail("Accessing an index out of bounds should throw an IndexOutOfBoundsException.");
 }

 @Test
 public void testClassesMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 Set<Object> classes = fold.classes();
 assertEquals(parentDataSet.classes().size(), classes.size());
 }

 @Test
 public void testSizeMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 assertEquals(foldIndices.length, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 fold = new Fold(parentDataSet, foldIndices);
 fold.remove(0);
 fail("Removing an instance from a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllInstances() {
 fold = new Fold(parentDataSet, foldIndices);
 fold.removeAll(parentDataSet);
 fail("Removing all instances from a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClearMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 fold.clear();
 fail("Clearing the instances in a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 List<Instance> instancesToRetain = new ArrayList<>();
 fold.retainAll(instancesToRetain);
 fail("Retaining all instances in a fold based on a collection should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 fold.set(0, new DenseInstance(1.0));
 fail("Setting an instance in a fold at a specific index should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSubListMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 List<Instance> subList = fold.subList(0, 1);
 fail("Getting a sublist of instances from a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testToArrayMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 Object[] array = fold.toArray();
 fail("Converting a fold to an array should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testToArrayWithCapacityMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 Object[] array = new Object[fold.size()];
 fold.toArray(array);
 fail("Converting a fold to an array with a given capacity should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNoAttributesMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 int noAttributes = fold.noAttributes();
 fail("Getting the number of attributes from a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClassIndexMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 int classIndex = fold.classIndex("classLabel");
 fail("Getting the class index from a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClassValueMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 Object classValue = fold.classValue(0);
 fail("Getting the class value from a fold at a specific index should not be possible.");
 }

 @Test
 public void testCopyMethod() {
 fold = new Fold(parentDataSet, foldIndices);
 Dataset copiedFold = fold.copy();
 assertTrue(copiedFold instanceof Fold);
 }

}