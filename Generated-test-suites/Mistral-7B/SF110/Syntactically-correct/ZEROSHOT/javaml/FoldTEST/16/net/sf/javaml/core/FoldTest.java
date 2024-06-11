package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class FoldTest {

 @Test
 public void testFold() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 1, 2, 3, 4 });

 assertEquals("Size of fold should match the size of indices", 4, fold.size());

 for (int i = 0; i < fold.size(); i++) {
 Instance instance = fold.instance(i);
 assertEquals("Instance at index " + i + " should be the same as instance in parent dataset at index " + fold.indices[i], fold.parent.instance(fold.indices[i]), instance);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 fold.add(new DenseInstance(new double[]{}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 fold.addAll(Arrays.asList(new DenseInstance(new double[]{}), new DenseInstance(new double[]{})));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAtIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 fold.addAll(1, Arrays.asList(new DenseInstance(new double[]{}), new DenseInstance(new double[]{})));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 1, 2, 3, 4 });
 fold.remove(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 1, 2, 3, 4 });
 fold.removeAll(Arrays.asList(new DenseInstance(new double[]{}), new DenseInstance(new double[]{})));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 1, 2, 3, 4 });
 fold.retainAll(Collections.emptyList());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 1, 2, 3, 4 });
 fold.set(0, new DenseInstance(new double[]{}));
 }
}