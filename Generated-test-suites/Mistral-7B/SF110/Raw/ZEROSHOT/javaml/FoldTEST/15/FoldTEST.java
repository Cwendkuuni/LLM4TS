package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FoldTest {

 @Test
 public void testConstructor() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 assertEquals(4, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.add(new DenseInstance(1.0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAtIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
 fold.addAll(1, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.remove(new DenseInstance(1.0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAtIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.set(0, new DenseInstance(1.0));
 }

 @Test
 public void testGetInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 Instance instance = fold.instance(0);
 assertNotNull(instance);
 }

 @Test
 public void testGetInstanceByIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 Instance instance = fold.instance(1);
 assertNotNull(instance);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInstanceInvalidIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.instance(4);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesNullCollection() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.addAll(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSubList() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 List<Instance> subList = fold.subList(0, 4);
 }

 @Test
 public void testSize() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 assertEquals(4, fold.size());
 }

 @Test
 public void testIsEmpty() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 assertTrue(fold.isEmpty());
 }

 @Test
 public void testIterator() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 List<Instance> instances = new ArrayList<>();
 for (Instance instance : fold) {
 instances.add(instance);
 }
 assertEquals(4, instances.size());
 }

 @Test
 public void testFolds() {
 throw new UnsupportedOperationException("Method is not yet implemented");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClasses() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.classes();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 fold.kNearest(2, new DenseInstance(0.5), new EuclideanDistanceMeasure());
 }
}