package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class FoldTest {

 private Dataset dataset;
 private Fold fold;

 @Before
 public void setup() {
 // Create a mock dataset for testing
 Instance instance1 = new DensityBasedInstance(new double[]{1.0, 2.0}, 1);
 Instance instance2 = new DensityBasedInstance(new double[]{3.0, 4.0}, 2);
 Instance instance3 = new DensityBasedInstance(new double[]{5.0, 6.0}, 1);
 List<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 dataset = new DefaultDataset(instances);

 // Create a fold with indices [1, 0, 2]
 int[] indices = {1, 0, 2};
 fold = new Fold(dataset, indices);
 }

 @Test
 public void testSize() {
 assertEquals("Fold size should be equal to the number of indices", 3, fold.size());
 }

 @Test
 public void testClasses() {
 Set<Object> expectedClasses = dataset.classes();
 Set<Object> actualClasses = fold.classes();
 assertEquals("The classes of the fold should be the same as the classes of the parent dataset", expectedClasses, actualClasses);
 }

 @Test
 public void testInstance() {
 Instance expectedInstance = dataset.instance(0); // The original dataset has an instance at index 0
 Instance actualInstance = fold.instance(1); // The fold has an instance at index 1, which corresponds to index 0 in the original dataset
 assertEquals("The instance in the fold should be the same as the instance in the parent dataset", expectedInstance, actualInstance);
 }

 @Test
 public void testFolds() {
 try {
 fold.folds(2, new Random());
 fail("Method folds should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected result
 }
 }

 @Test
 public void testAdd() {
 try {
 fold.add(new DensityBasedInstance(new double[]{7.0, 8.0}, 3));
 fail("Method add should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected result
 }
 }

 @Test
 public void testSubList() {
 try {
 fold.subList(0, 10);
 fail("Method subList should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected result
 }
 }

 @Test
 public void testToArray() {
 Object[] expectedArray = new Object[3];
 for (int i = 0; i < 3; i++) {
 expectedArray[i] = dataset.instance(fold.indices[i]);
 }
 Object[] actualArray = fold.toArray();
 assertArrayEquals("The array returned by toArray should contain the instances in the fold", expectedArray, actualArray);
 }

 @Test
 public void testToArrayWithCapacity() {
 Object[] expectedArray = new Object[3];
 for (int i = 0; i < 3; i++) {
 expectedArray[i] = dataset.instance(fold.indices[i]);
 }
 Object[] actualArray = new Object[3];
 fold.toArray(actualArray);
 assertArrayEquals("The array returned by toArray should contain the instances in the fold", expectedArray, actualArray);
 }

 @Test
 public void testCopy() {
 Dataset expectedDataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
 Dataset actualDataset = fold.copy();
 assertEquals("The copy of the fold should be a new dataset with the same instances as the original fold", expectedDataset, actualDataset);
 }

 @Test
 public void testNoAttributes() {
 assertEquals("A fold should have the same number of attributes as the parent dataset", dataset.noAttributes(), fold.noAttributes());
 }

 @Test
 public void testClassIndex() {
 Object expectedClass = dataset.classValue(0);
 int actualClassIndex = fold.classIndex(expectedClass);
 assertEquals("The class index of a class in the fold should be the same as the class index of the same class in the parent dataset", fold.parent.classIndex(expectedClass), actualClassIndex);
 }

 @Test
 public void testClassValue() {
 Object expectedClass = dataset.classValue(0);
 Object actualClass = fold.classValue(0);
 assertEquals("The class value of a fold should be the same as the class value of the corresponding index in the parent dataset", expectedClass, actualClass);
 }
}