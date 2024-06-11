package net.sf.javaml.core;

import static org.junit.Assert.*;
import net.sf.javaml.distance.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class FoldTest {

 private Dataset parentDataset;
 private Fold fold;

 @Before
 public void setUp() {
 // Assuming we have a Dataset implementation with some instances
 parentDataset = new DefaultDataset();
 // Create a Dataset with some instances
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0});
 Instance instance2 = new DenseInstance(new double[]{2.0, 3.0});
 Instance instance3 = new DenseInstance(new double[]{3.0, 4.0});

 parentDataset.add(instance1);
 parentDataset.add(instance2);
 parentDataset.add(instance3);

 int[] indices = {1, 2};
 fold = new Fold(parentDataset, indices);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(new DenseInstance(new double[]{1.0, 1.0}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 Collections.addAll(new ArrayList<>(), new Object[]{new DenseInstance(new double[]{1.0, 1.0})});
 fold.addAll(Collections.EMPTY_LIST);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithIndex() {
 fold.addAll(0, new ArrayList<>());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContains() {
 fold.contains(instance1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsAll() {
 fold.containsAll(new HashSet<>(Arrays.asList(instance1, instance2)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 fold.remove(instance1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 fold.remove(instance1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 HashSet<Instance> set = new HashSet<>(Arrays.asList(instance1, instance2));
 fold.retainAll(set);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 fold.set(instance1, new DenseInstance(new double[]{1.0, 1.1}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddFolds() {
 fold.folds(2, new Random());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 KNearestNeighbors knn = new KNearestNeighbors();
 fold.kNearest(2, instance1, knn);
 }

 @Test
 public void testGetInstance() {
 Instance instance = fold.instance(0);
 assertEquals(instance2, instance);
 }

 @Test
 public void testSize() {
 assertEquals(2, fold.size());
 }

 @Test
 public void testIterator() {
 List<Instance> instances = new ArrayList<>();
 for (Instance instance : fold) {
 instances.add(instance);
 }
 assertEquals(Arrays.asList(instance2, instance3), instances);
 }

 @Test
 public void testFoldIterator() {
 FoldIterator iterator = fold.iterator();
 Instance instance = iterator.next();
 assertEquals(instance2, instance);
 }

 @Test
 public void testFoldIteratorWithIndex() {
 FoldIterator iterator = fold.listIterator(1);
 Instance instance = iterator.next();
 assertEquals(instance3, instance);
 }
}