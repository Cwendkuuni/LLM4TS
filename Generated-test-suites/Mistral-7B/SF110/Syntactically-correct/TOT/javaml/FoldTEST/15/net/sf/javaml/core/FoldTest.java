package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

public class FoldTest {

 @Test
 public void testInstance() {
 Fold fold = new Fold(createDummyDataset(), new int[] { 0, 1, 2 });
 Instance instance = fold.instance(0);
 assertNotNull(instance);
 assertEquals(fold.parent.instance(0), instance);
 }

 @Test
 public void testSize() {
 Fold fold = new Fold(createDummyDataset(), new int[] { 0, 1, 2 });
 assertEquals(3, fold.size());
 }

 @Test
 public void testIterator() {
 Fold fold = new Fold(createDummyDataset(), new int[] { 0, 1, 2 });
 List<Instance> instances = new ArrayList<>();
 for (Instance i : fold) {
 instances.add(i);
 }
 assertEquals(3, instances.size());
 assertArrayEquals(new int[] { 0, 1, 2 }, fold.indices);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 Fold fold = new Fold(createDummyDataset(), new int[] { 0, 1, 2 });
 fold.add(new DenseInstance(new double[] { 1.0 }));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Fold fold = new Fold(createDummyDataset(), new int[] { 0, 1, 2 });
 fold.folds(5, new Random());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 Fold fold = new Fold(createDummyDataset(), new int[] { 0, 1, 2 });
 fold.set(1, new DenseInstance(new double[] { 2.0 }));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Fold fold = new Fold(createDummyDataset(), new int[] { 0, 1, 2 });
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 Fold fold = new Fold(createDummyDataset(), new int[] { 0, 1, 2 });
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 Fold fold = new Fold(createDummyDataset(), new int[] { 0, 1, 2 });
 fold.addAll(Arrays.asList(new DenseInstance(new double[] { 1.0 })));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAll() {
 Fold fold = new Fold(createDummyDataset(), new int[] { 0, 1, 2 });
 fold.retainAll(Arrays.asList(new DenseInstance(new double[] { 1.0 })));
 }

 private Dataset createDummyDataset() {
 // Create a dataset with at least 4 instances for testing purposes
 // ...
 }

}