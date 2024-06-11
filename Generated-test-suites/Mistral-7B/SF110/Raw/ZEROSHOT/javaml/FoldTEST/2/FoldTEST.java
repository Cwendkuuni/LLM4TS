package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

 @Test
 public void testSize() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 assertEquals(4, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.add(new DenseInstance(1.0));
 }

 @Test
 public void testGetInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 Instance inst = new DenseInstance(1.0);
 fold.parent.add(inst);
 Instance testInstance = fold.instance(0);
 assertEquals(inst, testInstance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.folds(5, new Random());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.kNearest(2, new DenseInstance(0.0), new EuclideanDistanceMeasure());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.remove(new DenseInstance(1.0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAll() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
 fold.removeAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
 fold.retainAll(Collections.emptyList());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 Instance inst = new DenseInstance(1.0);
 fold.set(0, inst);
 }

 @Test
 public void testClasses() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 Set<Object> classes = fold.classes();
 assertEquals(fold.parent.classes().size(), classes.size());
 }

 @Test
 public void testIterator() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 List<Instance> instances = new ArrayList<>();
 for (Instance inst : fold) {
 instances.add(inst);
 }
 assertEquals(fold.size(), instances.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceByIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.remove(1);
 }
}