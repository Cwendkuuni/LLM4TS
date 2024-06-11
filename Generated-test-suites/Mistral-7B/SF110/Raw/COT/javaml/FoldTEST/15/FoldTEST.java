package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.instance.Instance;

public class FoldTest {

 private static Dataset parentDataset;
 private static int[] indices;
 private static Fold fold;

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() {
 // Assuming parentDataset is an instance of Dataset and contains some data
 parentDataset = new DefaultDataset();
 // Create indices array with some values
 indices = new int[] { 1, 2, 3 };
 // Create Fold object
 fold = new Fold(parentDataset, indices);
 }

 @Test
 public void testConstructor() {
 assertEquals(parentDataset, fold.parent);
 assertArrayEquals(indices, fold.indices);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(new DenseInstance(1.0));
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 try {
 fold.folds(2, new Random());
 fail("Should throw UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testGetInstance() {
 Instance expectedInstance = parentDataset.instance(indices[0]);
 Instance actualInstance = fold.instance(0);
 assertEquals(expectedInstance.classValue(0), actualInstance.classValue(0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 try {
 fold.set(0, new DenseInstance(1.0));
 fail("Should throw UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 try {
 fold.remove(0);
 fail("Should throw UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testSize() {
 assertEquals(indices.length, fold.size());
 }

 @Test
 public void testGet() {
 Instance expectedInstance = parentDataset.instance(indices[0]);
 Instance actualInstance = fold.get(0);
 assertEquals(expectedInstance, actualInstance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAll() {
 try {
 fold.removeAll(Collections.emptyList());
 fail("Should throw UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testIterator() {
 List<Instance> expectedInstances = Arrays.asList(parentDataset.instance(indices[0]),
 parentDataset.instance(indices[1]), parentDataset.instance(indices[2]));
 List<Instance> actualInstances = new ArrayList<>();
 for (Instance instance : fold) {
 actualInstances.add(instance);
 }
 assertEquals(expectedInstances, actualInstances);
 }

 @Test
 public void testListIterator() {
 List<Instance> expectedInstances = Arrays.asList(parentDataset.instance(indices[2]),
 parentDataset.instance(indices[1]), parentDataset.instance(indices[0]));
 List<Instance> actualInstances = new ArrayList<>();
 ListIterator<Instance> listIterator = fold.listIterator();
 while (listIterator.hasNext()) {
 actualInstances.add(listIterator.next());
 }
 assertEquals(expectedInstances, actualInstances);
 }

 @Test
 public void testListIteratorNegativeIndex() {
 List<Instance> expectedInstances = Arrays.asList(parentDataset.instance(indices[0]),
 parentDataset.instance(indices[1]), parentDataset.instance(indices[2]));
 List<Instance> actualInstances = new ArrayList<>();
 ListIterator<Instance> listIterator = fold.listIterator(-1);
 while (listIterator.hasPrevious()) {
 actualInstances.add(listIterator.previous());
 }
 assertEquals(expectedInstances, actualInstances);
 }

 @Test
 public void testDistance() {
Instance instance1 = parentDataset.instance(indices[0]);
Instance instance2 = parentDataset.instance(indices[1]);
DistanceMeasure euclidean = new EuclideanDistance();
double expectedDistance = euclidean.distance(instance1, instance2);
double actualDistance = euclidean.distance(fold.instance(0), fold.instance(1));
assertEquals(expectedDistance, actualDistance, 0.0);
}

}