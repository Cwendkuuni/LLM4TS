package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

 @Test
 public void testFoldWithEmptyDataset() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 assertEquals(Collections.emptyList(), Arrays.asList(fold.classes()));
 assertEquals(0, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstanceToFold() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 Instance instance = new DenseInstance(new double[1]);
 fold.add(instance);
 }

 @Test
 public void testFoldWithValidParentDataset() {
 Dataset parentDataset = new DefaultDataset(); // Assuming it is an instance of Dataset class
 parentDataset.add(new DenseInstance(new double[1]));
 parentDataset.add(new DenseInstance(new double[1]));

 int[] indices = {0, 1};
 Fold fold = new Fold(parentDataset, indices);

 assertEquals(parentDataset.classes(), fold.classes());
 assertEquals(parentDataset.size(), fold.size());

 for (int i = 0; i < fold.size(); i++) {
 Instance instance = fold.instance(i);
 assertEquals(parentDataset.instance(indices[i]), instance);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldFoldsMethod() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 fold.folds(10, new Random());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldKNearestMethod() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 fold.kNearest(1, null, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldAddAllInstanceCollection() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[1]));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldAddAllInstanceCollectionAtIndex() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[1]));
 fold.addAll(1, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldClear() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldRemove() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldRemoveAt() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldRetainAll() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[1]));
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldSet() {
 Fold fold = new Fold(Collections.emptyDataset(), new int[0]);
 Instance instance = new DenseInstance(new double[1]);
 fold.set(0, instance);
 }
}