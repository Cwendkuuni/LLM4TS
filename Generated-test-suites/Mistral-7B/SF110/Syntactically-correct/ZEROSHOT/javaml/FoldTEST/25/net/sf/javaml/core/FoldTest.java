package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

 @Test
 public void testFoldConstruction() {
 Dataset parentDataset = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parentDataset, indices);
 assertEquals(parentDataset, fold.getParent());
 assertArrayEquals(indices, fold.getIndices());
 assertFalse(fold.add(new DenseInstance(1.0, 2.0, "Class1")));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.add(new DenseInstance(1.0, 2.0, "Class1"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0, 2.0, "Class1"), new DenseInstance(2.0, 3.0, "Class2"));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesWithIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0, 2.0, "Class1"), new DenseInstance(2.0, 3.0, "Class2"));
 fold.addAll(1, instances);
 }

 @Test
 public void testClasses() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(1.0, 2.0, "Class1"));
 parentDataset.add(new DenseInstance(2.0, 3.0, "Class2"));
 Fold fold = new Fold(parentDataset, new int[] { 1 });
 assertEquals(parentDataset.classes(), fold.classes());
 }

 @Test
 public void testFolds() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(1.0, 2.0, "Class1"));
 parentDataset.add(new DenseInstance(2.0, 3.0, "Class2"));
 Fold fold = new Fold(parentDataset, new int[] { 0, 1 });
 assertNotNull(fold.folds(3, new Random()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldsMethodNotImplemented() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.folds(3, new Random());
 }

 @Test
 public void testInstance() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(1.0, 2.0, "Class1"));
 parentDataset.add(new DenseInstance(2.0, 3.0, "Class2"));
 Fold fold = new Fold(parentDataset, new int[] { 1 });
 Instance instance = fold.instance(0);
 assertEquals(parentDataset.instance(1), instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearestNotImplemented() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.kNearest(3, new DenseInstance(1.0, 2.0, "Class1"), new EuclideanDistance());
 }

 @Test
 public void testSize() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(1.0, 2.0, "Class1"));
 parentDataset.add(new DenseInstance(2.0, 3.0, "Class2"));
 Fold fold = new Fold(parentDataset, new int[] { 1 });
 assertEquals(2, parentDataset.size());
 assertEquals(2, fold.size());
 }

 @Test
 public void testToString() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(1.0, 2.0, "Class1"));
 parentDataset.add(new DenseInstance(2.0, 3.0, "Class2"));
 Fold fold = new Fold(parentDataset, new int[] { 1 });
 String expected = "Fold[parent=DefaultDataset, indices=[1]]";
 assertEquals(expected, fold.toString());
 }
}