package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

 private Dataset dataset;
 private Fold fold;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 // Assuming a Dataset implementation with the required methods
 dataset = new DefaultDataset();
 // Add some instances to the dataset for testing
 for (int i = 0; i < 10; i++) {
 dataset.add(new DenseInstance(new double[] { i, Math.random(), Math.random() }));
 }

 fold = new Fold(dataset, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 });
 }

 @Test
 public void testConstructor() {
 assertNotNull(fold);
 assertEquals(dataset, fold.parent);
 assertArrayEquals(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 }, fold.indices);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(new DenseInstance(new double[] { 11, 11, 11 }));
 }

 @Test
 public void testClasses() {
 Set<Object> expectedClasses = dataset.classes();
 Set<Object> actualClasses = fold.classes();
 assertEquals(expectedClasses, actualClasses);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 fold.folds(3, new Random());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceNegativeIndex() {
 fold.instance(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceIndexOutOfBounds() {
 fold.instance(10);
 }

 @Test
 public void testInstance() {
 Instance expectedInstance = dataset.instance(0);
 Instance actualInstance = fold.instance(0);
 assertEquals(expectedInstance, actualInstance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 fold.kNearest(3, new DenseInstance(new double[] { 1, 1, 1 }), new EuclideanDistance());
 }

 @Test
 public void testSize() {
 assertEquals(fold.size(), 10);
 }

 @Test
 public void testNoAttributes() {
 assertEquals(dataset.noAttributes(), fold.noAttributes());
 }

 @Test
 public void testClassIndex() {
 Object expectedClass = dataset.classValue(0);
 Object actualClass = fold.classValue(0);
 assertEquals(expectedClass, actualClass);
 }

 @Test
 public void testCopy() {
 Dataset expectedCopy = dataset.copy();
 Dataset actualCopy = fold.copy();
 assertEquals(expectedCopy, actualCopy);
 }

 @Test
 public void testIterator() {
 Iterator<Instance> iterator = fold.iterator();
 int index = 0;
 while (iterator.hasNext()) {
 Instance instance = iterator.next();
 assertEquals(dataset.instance(fold.indices[index]), instance);
 ++index;
 }
 }
}