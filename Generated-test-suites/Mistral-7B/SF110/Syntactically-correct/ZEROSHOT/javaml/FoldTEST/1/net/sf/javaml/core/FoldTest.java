package net.sf.javaml.core;

import static org.junit.Assert.*;

import net.sf.javaml.distance.*;
import org.junit.Test;

import java.util.*;

public class FoldTest {

 @Test
 public void testFoldConstructor() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 assertEquals(5, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.add(new DenseInstance(new double[]{1.0, 2.0}));
 }

 @Test
 public void testClasses() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 Set<Object> classes = fold.classes();
 assertEquals(classes.size(), fold.parent.classes().size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.folds(5, new Random());
 }

 @Test
 public void testInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 Instance instance = fold.instance(0);
 assertNotNull(instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 Set<Instance> instances = fold.kNearest(5, new DenseInstance(new double[]{1.0, 2.0}), new EuclideanDistance());
 }

 @Test
 public void testAdd() {
 try {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.add(0, new DenseInstance(new double[]{1.0, 2.0}));
 fail("Adding an instance to a fold should not be possible.");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testAddAll() {
 try {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}));
 fold.addAll(instances);
 fail("Adding a collection of instances to a fold should not be possible.");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testAddAllStartIndex() {
 try {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 2.0}));
 fold.addAll(1, instances);
 fail("Adding a collection of instances to a fold at a specific index should not be possible.");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testClear() {
 try {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.clear();
 fail("Clearing a fold should not be possible.");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testContains() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 Instance instance = fold.instance(0);
 assertFalse(fold.contains(instance.classValue()));
 assertTrue(fold.contains(instance));
 }

 @Test
 public void testContainsAll() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 List<Instance> instances = new ArrayList<>(fold);
 assertTrue(fold.containsAll(instances));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGet() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.get(0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOf() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fail(fold.indexOf(null));
 }

 @Test
 public void testIsEmpty() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{});
 assertTrue(fold.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterator() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 ListIterator<Instance> iterator = fold.iterator();
 iterator.next();
 fail("Iterating over a fold should not be possible.");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testListIterator() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 ListIterator<Instance> iterator = fold.listIterator();
 iterator.next();
 fail("Iterating over a fold from the beginning should not be possible.");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testListIteratorStartIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 ListIterator<Instance> iterator = fold.listIterator(5);
 iterator.next();
 fail("Iterating over a fold from a specific index should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 Instance instance = fold.instance(0);
 fold.remove(instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAll() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 List<Instance> instances = new ArrayList<>();
 instances.add(fold.instance(0));
 fold.removeAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 List<Instance> instances = new ArrayList<>();
 instances.add(fold.instance(0));
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 Instance instance = fold.instance(0);
 fold.set(0, new DenseInstance(new double[]{1.0, 2.0}));
 }
}