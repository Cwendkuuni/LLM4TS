package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

 @Test
 public void testFold() {
 Dataset parentDataset = new DefaultDataset();
 Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 });
 parentDataset.add(instance1);
 parentDataset.add(instance2);

 int[] indices = { 0, 1 };
 Fold fold = new Fold(parentDataset, indices);

 assertEquals("Parent dataset should be the same for the fold", parentDataset, fold.getParent());
 assertEquals("Number of folds should be equal to the length of indices", 2, fold.size());

 for (int i = 0; i < fold.size(); i++) {
 Instance instance = fold.instance(i);
 int indexOfParent = Arrays.binarySearch(parentDataset.toArray(), instance);
 assertEquals("Instance should be present in the parent dataset", indexOfParent >= 0, true);
 assertEquals("Instance index in the parent dataset should match the index in the fold", parentDataset.indexOf(instance), i);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.add(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }), new DenseInstance(new double[] { 4.0, 5.0, 6.0 }));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }), new DenseInstance(new double[] { 4.0, 5.0, 6.0 }));
 fold.addAll(1, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1 });
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1 });
 fold.remove(instance1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1 });
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }), new DenseInstance(new double[] { 4.0, 5.0, 6.0 }));
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1 });
 Instance instanceToSet = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 fold.set(0, instanceToSet);
 }

 @Test
 public void testClasses() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1 });
 Set<Object> expectedClasses = new HashSet<>(parentDataset.classes());
 assertEquals("Classes should be the same as the parent dataset", expectedClasses, fold.classes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 Dataset[] expectedFolds = new Dataset[0];
 fold.folds(5, new Random());
 }

 @Test
 public void testIterator() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1 });
 List<Instance> expectedInstances = Arrays.asList(instance1, instance2);
 List<Instance> instances = new ArrayList<>();
 for (Instance instance : fold) {
 instances.add(instance);
 }
 assertEquals("Iterator should return all instances in the correct order", expectedInstances, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIterator() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1 });
 ListIterator<Instance> listIterator = fold.listIterator();
 listIterator.next();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIteratorWithIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1 });
 ListIterator<Instance> listIterator = fold.listIterator(1);
 listIterator.next();
 }
}