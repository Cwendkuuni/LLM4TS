package net.sf.javaml.core.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.EuclideanDistance;

public class FoldTest extends TestCase {

 @SuppressWarnings("unchecked")
 @Override
 protected void setUp() throws Exception {
 super.setUp();

 // Create a sample dataset
 Object[][] data = new Object[][]{{1.0, 2.0, "Class1"}, {2.0, 3.0, "Class1"}, {3.0, 4.0, "Class2"}, {4.0, 5.0, "Class2"}};
 Dataset dataset = new DefaultDataset(data, 3);

 // Create a fold of the dataset with 2 folds
 int[] indices = new int[]{1, 2, 3, 0}; // The order of indices does not matter as long as each fold contains the same number of instances
 Fold fold = new Fold(dataset, indices);
 }

 // Test for the constructor
 public void testFoldConstructor() {
 // Test that the parent dataset is correctly set
 Dataset parentDataset = fold.parent;
 assertEquals("The parent dataset is not the expected one", dataset, parentDataset);

 // Test that the indices are correctly set
 int[] expectedIndices = new int[]{1, 2, 3, 0};
 Assert.assertArrayEquals("The indices are not the expected ones", expectedIndices, fold.indices);
 }

 // Test for the instance method
 public void testInstanceMethod() {
 // Test retrieving instances from the fold
 Instance instance1 = fold.instance(0);
 Instance instance2 = fold.instance(1);
 Instance instance3 = fold.instance(2);

 // Test that the instances are the expected ones from the parent dataset
 Instance expectedInstance1 = dataset.instance(1);
 Instance expectedInstance2 = dataset.instance(2);
 Instance expectedInstance3 = dataset.instance(3);

 Assert.assertEquals("The first instance is not the expected one", expectedInstance1, instance1);
 Assert.assertEquals("The second instance is not the expected one", expectedInstance2, instance2);
 Assert.assertEquals("The third instance is not the expected one", expectedInstance3, instance3);
 }

 // Test for the classes method
 public void testClassesMethod() {
 // Test that the classes returned by the fold and the parent dataset are the same
 SortedSet<Object> foldClasses = fold.classes();
 SortedSet<Object> parentClasses = dataset.classes();

 Assert.assertEquals("The classes are not the same", parentClasses, foldClasses);
 }

 // Test for the kNearest method (unsupported but test it to verify the error message)
 public void testKNearestMethod() {
 try {
 Set<Instance> kNearestInstances = fold.kNearest(3, null, new EuclideanDistance());
 fail("Expected an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }
}