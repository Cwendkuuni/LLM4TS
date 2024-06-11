package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

 @Test
 public void testFoldConstructor() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 assertNotNull(fold);
 assertEquals(5, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 Instance instance = new DenseInstance(new double[1]);
 fold.add(instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[1]));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 fold.remove(new DenseInstance(new double[1]));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllInstances() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 Collection<Instance> instances = new ArrayList<>();
 fold.removeAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllInstances() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 Collection<Instance> instances = new ArrayList<>();
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 Instance instance = new DenseInstance(new double[1]);
 fold.set(0, instance);
 }

 @Test
 public void testClasses() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 Set<Object> classes = fold.classes();
 assertEquals(dataset.classes().size(), classes.size());
 assertTrue(classes.containsAll(Arrays.asList(dataset.classes().toArray())));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 Dataset[] folds = fold.folds(5, new Random());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testInstance() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 fold.instance(5);
 }

 @Test
 public void testInstanceWithIndex() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 Instance instance = fold.instance(0);
 assertNotNull(instance);
 assertEquals(dataset.instance(indices[0]), instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Dataset dataset = new DefaultDataset();
 int[] indices = { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(dataset, indices);
 fold.kNearest(5, new DenseInstance(new double[1]), new EuclideanDistance());
 }
}