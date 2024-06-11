package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {
 // Test case for the constructor
 @Test
 public void testFoldConstructor() {
 Dataset parentDataSet = new DefaultDataset();
 int[] indices = {0, 1, 2, 3, 4};
 Fold fold = new Fold(parentDataSet, indices);
 assertEquals("Expected parent dataset not null", parentDataSet, fold.parent);
 assertEquals("Expected indices length equals 5", 5, fold.size());
 }

 // Test case for the getInstance method
 @Test
 public void testGetInstance() {
 Dataset parentDataSet = new DefaultDataset();
 Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
 parentDataSet.add(inst1);
 parentDataSet.add(inst2);
 int[] indices = {0, 1};
 Fold fold = new Fold(parentDataSet, indices);
 Instance expectedInstance1 = inst1;
 Instance expectedInstance2 = inst2;
 Instance instance1 = fold.instance(0);
 Instance instance2 = fold.instance(1);
 assertEquals("Expected instance 0 equals expectedInstance1", expectedInstance1, instance1);
 assertEquals("Expected instance 1 equals expectedInstance2", expectedInstance2, instance2);
 }

 // Test case for the size method
 @Test
 public void testSize() {
 Dataset parentDataSet = new DefaultDataset();
 Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
 parentDataSet.add(inst1);
 parentDataSet.add(inst2);
 int[] indices = {0, 1};
 Fold fold = new Fold(parentDataSet, indices);
 assertEquals("Expected size 2", 2, fold.size());
 }

 // Test case for the add method
 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 Dataset parentDataSet = new DefaultDataset();
 int[] indices = {0, 1};
 Fold fold = new Fold(parentDataSet, indices);
 Instance testInstance = new DenseInstance(new double[]{7.0, 8.0, 9.0});
 fold.add(testInstance);
 fail("Expected UnsupportedOperationException to be thrown");
 }

 // Test case for the classes method
 @Test
 public void testClasses() {
 Dataset parentDataSet = new DefaultDataset();
 parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
 Fold fold = new Fold(parentDataSet, new int[]{0, 1});
 Set<Object> classes = fold.classes();
 assertEquals("Expected size 2 for classes", 2, classes.size());
 assertTrue("Expected class 0 in classes", classes.contains(0));
 assertTrue("Expected class 1 in classes", classes.contains(1));
 }
}