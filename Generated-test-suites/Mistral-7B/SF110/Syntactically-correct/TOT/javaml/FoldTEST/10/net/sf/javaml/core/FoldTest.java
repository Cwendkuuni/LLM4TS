package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

 // Test for constructor and get parent Dataset
 @Test
 public void testFoldConstruction() {
 Dataset dataset = new DefaultDataset();
 int[] indices = new int[]{0, 1, 2, 3, 4};
 Fold fold = new Fold(dataset, indices);
 assertSame(dataset, fold.getParent());
 }

 // Test instance retrieval from fold
 @Test
 public void testInstanceRetrieval() {
 Dataset dataset = new DefaultDataset();
 int[] indices = new int[]{0, 1, 2, 3, 4};
 Fold fold = new Fold(dataset, indices);
 Instance expectedInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.1, 1.2});
 Instance actualInstance = fold.instance(0);
 assertEquals(expectedInstance, actualInstance);
 }

 // Test unsupported operations
 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Dataset dataset = new DefaultDataset();
 int[] indices = new int[]{0, 1, 2, 3, 4};
 Fold fold = new Fold(dataset, indices);
 fold.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.1, 1.2}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldsMethod() {
 Dataset dataset = new DefaultDataset();
 int[] indices = new int[]{0, 1, 2, 3, 4};
 Fold fold = new Fold(dataset, indices);
 fold.folds(5, new Random());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetMethod() {
 Dataset dataset = new DefaultDataset();
 int[] indices = new int[]{0, 1, 2, 3, 4};
 Fold fold = new Fold(dataset, indices);
 fold.set(0, new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.1, 1.2}));
 }

 // Test empty fold
 @Test
 public void testEmptyFold() {
 Dataset dataset = new DefaultDataset();
 Fold fold = new Fold(dataset, new int[0]);
 assertTrue(fold.isEmpty());
 }

 // Test size of fold
 @Test
 public void testSize() {
 Dataset dataset = new DefaultDataset();
 int[] indices = new int[]{0, 1, 2, 3, 4};
 Fold fold = new Fold(dataset, indices);
 assertEquals(5, fold.size());
 }

 // Test iterator
 @Test
 public void testIterator() {
 Dataset dataset = new DefaultDataset();
 int[] indices = new int[]{0, 1, 2, 3, 4};
 Fold fold = new Fold(dataset, indices);
 List<Instance> instances = new ArrayList<>();
 for (Instance instance : fold) {
 instances.add(instance);
 }
 assertEquals(5, instances.size());
 }
}