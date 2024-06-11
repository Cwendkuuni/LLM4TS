package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FoldTest {

 private Dataset parentDataset;
 private Fold fold;

 @Before
 public void setup() {
 // Create a mock parent Dataset
 Instance[] instances = new Instance[] { new DenseInstance(new double[] {1.0, 2.0, 3.0}, new Object[]{1}),
 new DenseInstance(new double[] {4.0, 5.0, 6.0}, new Object[]{2}) };
 parentDataset = new DefaultDataset(Arrays.asList(instances));

 // Create a Fold with indices [0, 1]
 int[] indices = new int[] {0, 1};
 fold = new Fold(parentDataset, indices);
 }

 @Test
 public void testSize() {
 assertEquals(2, fold.size());
 }

 @Test
 public void testGetInstance() {
 Instance instance1 = fold.instance(0);
 Instance instance2 = fold.instance(1);
 assertEquals(instance1.classValue(), instance1.classValue());
 assertEquals(instance1, instance1);
 assertEquals(instance2.classValue(), instance2.classValue());
 assertEquals(instance2, instance2);
 }

 @Test
 public void testGetClass() {
 Set<Object> classes = fold.classes();
 assertEquals(classes.size(), parentDataset.classes().size());
 assertTrue(classes.containsAll(parentDataset.classes()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(new DenseInstance(new double[] {7.0, 8.0, 9.0}, new Object[]{3}));
 fail("Should not be able to add an instance to a Fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 List<Instance> instancesToAdd = Arrays.asList(new DenseInstance(new double[] {1.1, 2.1, 3.1}, new Object[]{1}),
 new DenseInstance(new double[] {4.1, 5.1, 6.1}, new Object[]{2}));
 fold.addAll(instancesToAdd);
 fail("Should not be able to add all instances to a Fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 fold.remove(0);
 fail("Should not be able to remove an instance from a Fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAll() {
 List<Instance> instancesToRemove = Arrays.asList(fold.instance(0), fold.instance(1));
 fold.removeAll(instancesToRemove);
 fail("Should not be able to remove all instances from a Fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 fold.set(0, new DenseInstance(new double[] {7.0, 8.0, 9.0}, new Object[]{3}));
 fail("Should not be able to set an instance in a Fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 fold.folds(2, new Random());
 fail("Method is not yet implemented in Fold class");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 fold.kNearest(2, fold.instance(0), new EuclideanDistance());
 fail("Method is not yet implemented in Fold class");
 }
}