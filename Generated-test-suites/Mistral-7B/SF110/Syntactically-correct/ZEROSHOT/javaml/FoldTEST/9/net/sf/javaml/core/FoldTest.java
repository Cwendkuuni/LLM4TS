package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FoldTest {

 private static final Dataset parentDataset = new DefaultDataset();

 static {
 for (int i = 0; i < 10; i++) {
 Instance instance = new DenseInstance(11);
 instance.setValue(0, i);
 parentDataset.add(instance);
 }
 }

 @Test
 public void testSize() {
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 assertEquals(10, fold.size());
 }

 @Test
 public void testGetInstance() {
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 for (int i = 0; i < fold.size(); i++) {
 Instance instance = fold.getInstance(i);
 assertEquals(i, instance.value(0));
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstanceUnsupported() {
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 fold.add(new DenseInstance(11));
 fail("Adding an instance to a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllUnsupported() {
 List<Instance> instances = new ArrayList<>();
 for (int i = 0; i < 5; i++) {
 instances.add(new DenseInstance(11));
 }
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 fold.addAll(instances);
 fail("Adding a collection of instances to a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClearUnsupported() {
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 fold.clear();
 fail("Clearing a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceUnsupported() {
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 fold.remove(new DenseInstance(11));
 fail("Removing an instance from a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveUnsupported() {
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 fold.remove(1);
 fail("Removing an element from a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllUnsupported() {
 List<Instance> instances = new ArrayList<>(Arrays.asList(parentDataset.getInstance(1), parentDataset.getInstance(3), parentDataset.getInstance(5)));
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 fold.removeAll(instances);
 fail("Removing a collection of instances from a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllUnsupported() {
 List<Instance> instances = new ArrayList<>(Arrays.asList(parentDataset.getInstance(1), parentDataset.getInstance(3), parentDataset.getInstance(5)));
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 fold.retainAll(Collections.emptyList());
 fail("Retaining a collection of instances in a fold should not be possible.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetUnsupported() {
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 fold.set(0, new DenseInstance(11));
 fail("Setting an instance in a fold should not be possible.");
 }

 @Test
 public void testIterator() {
 Fold fold = new Fold(parentDataset, new int[] { 1, 3, 5, 7, 8, 9, 2, 4, 6, 10 });
 List<Integer> indexValues = new ArrayList<>();
 for (Instance instance : fold) {
 indexValues.add((Integer)instance.value(0));
 }
 List<Integer> expectedIndexValues = Arrays.asList(new Integer[] { 3, 5, 7, 8, 9, 2, 4, 6, 1, 10 });
 assertEquals(expectedIndexValues, indexValues);
 }

}