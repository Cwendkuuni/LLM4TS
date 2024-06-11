package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FoldTest {

 @Test
 public void testConstructor() {
 Dataset parent = new DefaultDataset();
 int[] indices = {0, 1, 2, 3};
 Fold fold = new Fold(parent, indices);
 assertEquals(parent, fold.getParent());
 assertArrayEquals(indices, fold.getIndices());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 try {
 fold.add(new DenseInstance(new double[0], new Object[0]));
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[0], new Object[0]));
 try {
 fold.addAll(instances);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[0], new Object[0]));
 try {
 fold.addAll(0, instances);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 try {
 fold.clear();
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 try {
 fold.remove(new DenseInstance(new double[0], new Object[0]));
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceByIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 try {
 fold.remove(0);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[0], new Object[0]));
 try {
 fold.retainAll(instances);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 Instance instance = new DenseInstance(new double[0], new Object[0]);
 try {
 fold.set(0, instance);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testInstance() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0});
 Instance instance = new DenseInstance(new double[0], new Object[0]);
 parent.add(instance);
 Instance testInstance = fold.instance(0);
 assertEquals(instance, testInstance);
 }

 @Test
 public void testClasses() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 assertEquals(parent.classes(), fold.classes());
 }

 @Test
 public void testSize() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0, 1, 2, 3});
 assertEquals(4, fold.size());
 }

 @Test
 public void testGet() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0, 1, 2, 3});
 Instance instance = new DenseInstance(new double[0], new Object[0]);
 parent.add(instance);
 Instance testInstance = fold.get(0);
 assertEquals(instance, testInstance);
 }

 @Test
 public void testAddFolds() {
 Dataset parent = new DefaultDataset();
 Fold fold1 = new Fold(parent, new int[] {0, 1, 2});
 Fold fold2 = new Fold(parent, new int[] {3, 4, 5});
 Dataset[] folds = fold1.folds(3, new Random());
 assertEquals(3, folds.length);
 assertEquals(fold1, folds[0]);
 assertEquals(fold2, folds[1]);
 assertNotNull(folds[2]);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithNullParent() {
 try {
 new Fold(null, new int[0]);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNullIndices() {
 try {
 new Fold(new DefaultDataset(), null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithEmptyIndices() {
 try {
 new Fold(new DefaultDataset(), new int[0]);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceWithNegativeIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0, 1, 2});
 try {
 fold.instance(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceWithOutOfBoundsIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0, 1, 2});
 try {
 fold.instance(3);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithNegativeIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0, 1, 2});
 try {
 fold.get(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithOutOfBoundsIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0, 1, 2});
 try {
 fold.get(3);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithIndexFromParentOutOfBounds() {
 Dataset parent = new DefaultDataset();
 parent.add(new DenseInstance(new double[0], new Object[0]));
 Fold fold = new Fold(parent, new int[] {-1});
 try {
 fold.get(0);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIterator() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0, 1, 2});
 List<Instance> instances = new ArrayList<>();
 for (Instance instance : fold) {
 instances.add(instance);
 }
 assertEquals(3, instances.size());
 }

 @Test
 public void testListIterator() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0, 1, 2});
 ListIterator<Instance> iterator = fold.listIterator();
 List<Instance> instances = new ArrayList<>();
 while (iterator.hasNext()) {
 instances.add(iterator.next());
 }
 assertEquals(3, instances.size());
 }

 @Test
 public void testListIteratorWithIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0, 1, 2});
 ListIterator<Instance> iterator = fold.listIterator(1);
 List<Instance> instances = new ArrayList<>();
 while (iterator.hasNext()) {
 instances.add(iterator.next());
 }
 assertEquals(2, instances.size());
 }

 @Test(expected = NoSuchElementException.class)
 public void testListIteratorWithNegativeIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[] {0, 1, 2});
 ListIterator<Instance> iterator = fold.listIterator(-1);
 try {
 iterator.next();
 fail("Expected NoSuchElementException");
 } catch (NoSuchElementException e) {
 // expected
 }
 }
}