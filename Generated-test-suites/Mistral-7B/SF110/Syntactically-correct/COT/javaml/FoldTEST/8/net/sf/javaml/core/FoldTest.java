package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.FoldIterator;
import net.sf.javaml.distance.DistanceMeasure;

public class FoldTest {

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstanceUnsupported() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.add(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllUnsupported() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.addAll(Arrays.asList(new Object[0]));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllFromIndexUnsupported() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.addAll(1, Arrays.asList(new Object[0]));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClearUnsupported() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveUnsupported() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.remove(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveUnsupportedIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllUnsupported() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.removeAll(Arrays.asList(new Object[0]));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllUnsupported() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.retainAll(Arrays.asList(new Object[0]));
 }

 @Test
 public void testSizeIsCorrect() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[5]);
 assertEquals(5, fold.size());
 }

 @Test
 public void testGetInstance() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[5]);
 Instance instance = new DefaultInstance(new double[10]);
 parent.add(instance);
 for (int i = 0; i < fold.size(); ++i) {
 assertEquals(instance, fold.instance(i));
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInstanceOutOfBounds() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[5]);
 assertEquals(null, fold.instance(-1));
 }

 @Test
 public void testFoldIteratorHasNext() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[5]);
 Instance instance = new DefaultInstance(new double[10]);
 parent.add(instance);
 FoldIterator iterator = fold.iterator();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testFoldIteratorNext() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[5]);
 Instance instance = new DefaultInstance(new double[10]);
 parent.add(instance);
 FoldIterator iterator = fold.iterator();
 assertEquals(instance, iterator.next());
 }

 @Test
 public void testFoldIteratorHasPrevious() {
 Dataset parent = new DefaultDataset();Fold fold = new Fold(parent, new int[5]);
 Instance instance = new DefaultInstance(new double[10]);
 parent.add(instance);
 FoldIterator iterator = fold.iterator();
 iterator.next();
 assertTrue(iterator.hasPrevious());
 }

 @Test
 public void testFoldIteratorPrevious() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[5]);
 Instance instance = new DefaultInstance(new double[10]);
 parent.add(instance);
 FoldIterator iterator = fold.iterator();
 iterator.next();
 iterator.next();
 assertEquals(instance, iterator.previous());
 }

 @Test
 public void testFoldIteratorHasNextAfterLastIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[5]);
 FoldIterator iterator = fold.iterator();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testFoldIteratorNoElements() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 FoldIterator iterator = fold.iterator();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testFoldConstructedWithNullParent() {
 try {
 new Fold(null, new int[0]);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testFoldConstructedWithNullIndices() {
 try {
 new Fold(new DefaultDataset(), null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testFoldConstructedWithInvalidParentType() {
 try {
 new Fold(new Object(), new int[0]);
 fail("Should throw ClassCastException");
 } catch (ClassCastException e) {
 // expected
 }
 }

 @Test
 public void testFoldConstructedWithInvalidIndicesType() {
 try {
 new Fold(new DefaultDataset(), new Object()[0]);
 fail("Should throw ClassCastException");
 } catch (ClassCastException e) {
 // expected
 }
 }

 @Test
 public void testFoldClasses() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[5]);
 assertEquals(parent.classes(), fold.classes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldFolds() {
 Fold fold = new Fold(new DefaultDataset(), new int[5]);
 fold.folds(5, new Random());
 }

 @Test
 public void testFoldKNearest() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[5]);
 Instance instance = new DefaultInstance(new double[10]);
 parent.add(instance);
 assertNull(fold.kNearest(5, instance, null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldKNearestWithDistanceMeasure() {
 Fold fold = new Fold(new DefaultDataset(), new int[5]);
 fold.kNearest(5, null, new EuclideanDistance());
 }
}