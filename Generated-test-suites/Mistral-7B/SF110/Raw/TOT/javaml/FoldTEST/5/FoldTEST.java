package net.sf.javaml.core.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.EuclideanDistance;

public class FoldTest extends TestCase {

 /**
 * Test constructing a Fold with valid inputs
 */
 public void testValidFoldConstruction() {
 Dataset parentDataset = new DefaultDataset();
 int[] indices = {0, 1, 2};
 Fold fold = new Fold(parentDataset, indices);
 assertEquals("Incorrect number of instances in fold", 3, fold.size());
 }

 /**
 * Test constructing a Fold with null parent dataset
 */
 public void testNullParentDataset() {
 try {
 new Fold(null, new int[0]);
 fail("Should have thrown an exception");
 } catch (NullPointerException e) {
 // expected
 }
 }

 /**
 * Test constructing a Fold with null indices
 */
 public void testNullIndices() {
 try {
 new Fold(new DefaultDataset(), null);
 fail("Should have thrown an exception");
 } catch (NullPointerException e) {
 // expected
 }
 }

 /**
 * Test getting an instance from a fold by index
 */
 public void testGetInstanceFromFold() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 parentDataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 parentDataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));

 int[] indices = {1, 2};
 Fold fold = new Fold(parentDataset, indices);
 Instance instance = fold.instance(0);
 Instance expectedInstance = parentDataset.instance(1);

 assertEquals("Incorrect instance returned from fold", expectedInstance, instance);
 }

 /**
 * Test getting an invalid index from a fold
 */
 public void testGetInvalidIndexFromFold() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 parentDataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 parentDataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));

 int[] indices = {1, 2};
 Fold fold = new Fold(parentDataset, indices);

 try {
 fold.instance(-1);
 fail("Should have thrown an exception");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 /**
 * Test getting an instance from a fold with empty fold
 */
 public void testGetInstanceFromEmptyFold() {
 Dataset parentDataset = new DefaultDataset();
 Fold fold = new Fold(parentDataset, new int[0]);

 try {
 fold.instance(0);
 fail("Should have thrown an exception");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 /**
 * Test getting the classes from a fold
 */
 public void testGetClassesFromFold() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 parentDataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 parentDataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));

 int[] indices = {1, 2};
 Fold fold = new Fold(parentDataset, indices);
 SortedSet<Object> classes = fold.classes();

 assertEquals("Incorrect number of classes in fold", 2, classes.size());
 assertTrue("class1 not in classes", classes.contains("class1"));
 assertTrue("class2 not in classes", classes.contains("class2"));
 }

 /**
 * Test getting the classes from an empty fold
 */
 public void testGetClassesFromEmptyFold() {
 Dataset parentDataset = new DefaultDataset();
 Fold fold = new Fold(parentDataset, new int[0]);
 SortedSet<Object> classes = fold.classes();

 assertEquals("Incorrect number of classes in fold", 0, classes.size());
 }

 /**
 * Test trying to add an instance to a fold
 */
 public void testAddInstanceToFold() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 parentDataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 parentDataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));
 int[] indices = {1, 2};
 Fold fold = new Fold(parentDataset, indices);

 try {
 fold.add(new DenseInstance(new double[]{0.0, 0.0, 0.0}, "class3"));
 fail("Should have thrown an exception");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 /**
 * Test getting k-nearest neighbors from a fold (not yet implemented)
 */
 public void testGetKNearestNeighborsFromFold() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
 parentDataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
 parentDataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1"));
 int[] indices = {1, 2};
 Fold fold = new Fold(parentDataset, indices);
 Instance testInstance = new DenseInstance(new double[]{5.0, 5.0, 5.0}, "class3");
 DistanceMeasure distanceMeasure = new EuclideanDistance();

 try {
 Set<Instance> neighbors = fold.kNearest(2, testInstance, distanceMeasure);
 fail("Should have thrown an exception");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }
}