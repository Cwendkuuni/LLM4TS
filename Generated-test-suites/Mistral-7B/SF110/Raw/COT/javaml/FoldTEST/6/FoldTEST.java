package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

import org.junit.Test;

public class FoldTest {

 private static final double DELTA = 0.001;

 @Test
 public void testFoldConstructor() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parent, indices);
 assertEquals(parent, fold.getParent());
 assertArrayEquals(indices, fold.getIndices());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 Instance instance = new DefaultInstance(1);
 fold.add(instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DefaultInstance(1));
 fold.addAll(instances);
 }

 @Test
 public void testClasses() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 assertEquals(fold.classes(), fold.getParent().classes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 int numFolds = 5;
 Random rg = new Random();
 fold.folds(numFolds, rg);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 int index = 0;
 fold.instance(index);
 }

 @Test
 public void testGetInstanceWithValidIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 Instance instance = fold.instance(1);
 assertEquals(fold.getParent().instance(0), instance);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInstanceWithInvalidIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 int index = 3;
 fold.instance(index);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 int k = 5;
 Instance inst = new DefaultInstance(1);
 DistanceMeasure dm = new EuclideanDistance();
 fold.kNearest(k, inst, dm);
 }

 @Test
 public void testSize() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 assertEquals(fold.size(), 3);
 }

 @Test
 public void testContainsInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 Instance instance = new DefaultInstance(1);
 assertFalse(fold.contains(instance));

 Instance foldInstance = fold.instance(0);
 assertTrue(fold.contains(foldInstance));
 }

 @Test
 public void testToArray() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 Instance[] expected = new Instance[] { fold.instance(0), fold.instance(1), fold.instance(2) };
 Object[] actual = fold.toArray();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testToArrayWithCustomArray() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 Instance[] expected = new Instance[] { fold.instance(0), fold.instance(1), fold.instance(2) };
 Dataset dataset = new DefaultDataset();
 Instance[] actual = fold.toArray(expected);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstanceAt() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 Instance instance = new DefaultInstance(1);
 fold.add(1, instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAt() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DefaultInstance(1));
 fold.addAll(1, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 fold.clear();
 }

 @Test
 public void testNoAttributes() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 assertEquals(fold.noAttributes(), fold.getParent().noAttributes());
 }

 @Test
 public void testClassIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 Object clazz = "class1";
 assertEquals(fold.classIndex(clazz), fold.getParent().classIndex(clazz));
 }

 @Test
 public void testClassValue() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 int index = 1;
 assertEquals(fold.classValue(index), fold.getParent().classValue(fold.getIndices()[index]));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCopy() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 Dataset copy = fold.copy();
 }

 @Test
 public void testIterator() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 Instance[] instances = new Instance[fold.size()];
 List<Instance> result = new ArrayList<>();
 for (Instance instance : fold) {
 instances[fold.indexOf(instance)] = instance;
 result.add(instance);
 }
 Collections.sort(result, new EuclideanDistance());
 assertArrayEquals(instances, result.toArray());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 int index = 1;
 fold.remove(index);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceAt() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 int index = 1;
 fold.removeAt(index);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 Collection<Instance> instances = new ArrayList<>();
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
 int index = 1;
 Instance instance = new DefaultInstance(1);
 fold.set(index, instance);
 }
}