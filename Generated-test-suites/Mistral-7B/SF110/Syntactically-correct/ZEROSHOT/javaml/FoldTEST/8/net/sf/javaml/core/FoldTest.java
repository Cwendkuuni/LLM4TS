package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.distance.PearsonCorrelationDistance;

import org.junit.Test;

class FoldTest {

 private static Dataset createDataset(int size) {
 final Dataset dataset = new DefaultDataset();
 for (int i = 0; i < size; i++) {
 final double[] attributes = new double[] { i, Math.sin(i), Math.cos(i), i };
 dataset.add(new DenseInstance(1, attributes));
 }
 return dataset;
 }

 @Test
 public void testAdd() {
 try {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.add(new DenseInstance(1, new double[] { 1.0, 1.0, 1.0, 1.0 }));
 fail("Exception expected.");
 } catch (final UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClasses() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.classes();
 }

 @Test
 public void testFolds() {
 final Dataset dataset = createDataset(10);
 final Random rg = new Random();
 final Fold fold = new Fold(dataset, new int[10]);
 final Dataset[] folds = fold.folds(5, rg);
 assertEquals(5, folds.length);
 for (final Dataset foldSet : folds) {
 assertTrue(foldSet instanceof Fold);
 assertEquals(2, foldSet.size());
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGet() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.get(0);
 }

 @Test
 public void testInstance() {
 final Dataset dataset = createDataset(5);
 final int[] indices = new int[] { 1, 3, 0, 4 };
 final Fold fold = new Fold(dataset, indices);
 for (int i = 0; i < indices.length; ++i) {
 final Instance instance = fold.instance(i);
 assertEquals(3, instance.size());
 final double[] attributes = instance.toDoubleArray();
 final int index = indices[i];
 for (int j = 0; j < 4; ++j) {
 assertEquals(dataset.instance(index).value(j), attributes[j], 0.001);
 }
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 final int k = 5;
 final Instance inst = new DenseInstance(1, new double[] { 1.0, 1.0, 1.0, 1.0 });
 final DistanceMeasure dm = new EuclideanDistance();
 fold.kNearest(k, inst, dm);
 }

 @Test
 public void testSize() {
 final Dataset dataset = createDataset(5);
 final int[] indices = new int[] { 1, 3, 0, 4 };
 final Fold fold = new Fold(dataset, indices);
 assertEquals(4, fold.size());
 }

 @Test
 public void testNoAttributes() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 assertEquals(0, fold.noAttributes());
 }

 @Test
 public void testClassIndex() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 final int classIndex = fold.classIndex("StringClass");
 assertEquals(-1, classIndex);
 }

 @Test
 public void testClassValue() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 final int index = 0;
 final int classValue = fold.classValue(index);
 fail("Exception expected.");
 }

 @Test
 public void testCopy() {
 final Dataset dataset = createDataset(5);
 final int[] indices = new int[] { 1, 3, 0, 4 };
 final Fold fold = new Fold(dataset, indices);
 final Dataset copy = fold.copy();
 assertEquals(4, copy.size());
 for (int i = 0; i < indices.length; ++i) {
 final Instance instance = fold.instance(i);
 final Instance copyInstance = copy.instance(i);
 assertEquals(instance, copyInstance);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.clear();
 }

 @Test
 public void testAddAll() {
 try {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 final Instance instance = new DenseInstance(1, new double[] { 1.0, 1.0, 1.0, 1.0 });
 final List<Instance> list = Arrays.asList(instance);
 fold.addAll(list);
 fail("Exception expected.");
 } catch (final UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.remove(new DenseInstance(1, new double[] { 1.0, 1.0, 1.0, 1.0 }));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAll() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 final List<Instance> list = Collections.singletonList(new DenseInstance(1, new double[] { 1.0, 1.0, 1.0, 1.0 }));
 fold.removeAll(list);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 final List<Instance> list = Collections.singletonList(new DenseInstance(1, new double[] { 1.0, 1.0, 1.0, 1.0 }));
 fold.retainAll(Collections.emptySet());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 final int index = 0;
 final Instance instance = new DenseInstance(1, new double[] { 1.0, 1.0, 1.0, 1.0 });
 fold.set(index, instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterator() {
 final Fold fold = new Fold(new DefaultDataset(), new int[0]);
 final Iterator<Instance> iterator = fold.iterator();
 iterator.next();
 }
}