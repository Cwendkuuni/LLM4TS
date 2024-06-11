package net.sf.javaml.core.test;

import junit.framework.TestCase;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

public class FoldTest extends TestCase {

 private Dataset originalDataSet;
 private Instance instance1, instance2, instance3;
 private int[] indices;

 @Before
 public void setUp() throws Exception {
 originalDataSet = new DefaultDataset();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
 instance3 = new DenseInstance(new double[]{7.0, 8.0, 9.0});
 originalDataSet.add(instance1);
 originalDataSet.add(instance2);
 originalDataSet.add(instance3);
 indices = new int[]{1, 2};
 }

 @Test
 public void testGetInstance() {
 Fold fold = new Fold(originalDataSet, indices);
 Instance testInstance = fold.instance(0);
 assertEquals("Instance retrieved is incorrect.", instance2, testInstance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(originalDataSet, indices);
 fold.add(new DenseInstance(new double[]{10.0, 11.0, 12.0}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 Fold fold = new Fold(originalDataSet, indices);
 Dataset newDataSet = new DefaultDataset();
 newDataSet.add(instance1);
 newDataSet.add(instance2);
 fold.addAll(newDataSet);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Fold fold = new Fold(originalDataSet, indices);
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 Fold fold = new Fold(originalDataSet, indices);
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllInstances() {
 Fold fold = new Fold(originalDataSet, indices);
 fold.removeAll(originalDataSet);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllInstances() {
 Fold fold = new Fold(originalDataSet, indices);
 Dataset newDataSet = new DefaultDataset();
 fold.retainAll(newDataSet);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 Fold fold = new Fold(originalDataSet, indices);
 Instance newInstance = new DenseInstance(new double[]{10.0, 11.0, 12.0});
 fold.set(0, newInstance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAtIndex() {
 Fold fold = new Fold(originalDataSet, indices);
 Dataset newDataSet = new DefaultDataset();
 fold.addAll(newDataSet, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAtIndex() {
 Fold fold = new Fold(originalDataSet, indices);
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsInstance() {
 Fold fold = new Fold(originalDataSet, indices);
 assertTrue(fold.contains(instance1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsAllInstances() {
 Fold fold = new Fold(originalDataSet, indices);
 assertTrue(fold.containsAll(originalDataSet));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsEmpty() {
 Fold fold = new Fold(originalDataSet, indices);
 assertFalse(fold.isEmpty());
 }

 @Test
 public void testSize() {
 Fold fold = new Fold(originalDataSet, indices);
 assertEquals("Size of fold is incorrect.", 2, fold.size());
 }

 @Test
 public void testClasses() {
 Fold fold = new Fold(originalDataSet, indices);
 Set<Object> classes = fold.classes();
 assertEquals("Number of classes in fold is incorrect.", 2, classes.size());
 }

 @Test
 public void testKNearest() {
 Fold fold = new Fold(originalDataSet, indices);
 Instance instanceToFind = new DenseInstance(new double[]{5.0, 5.0, 5.0});
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 Set<Instance> kNearestInstances = fold.kNearest(3, instanceToFind, distanceMeasure);
 assertEquals("Number of nearest instances retrieved is incorrect.", 3, kNearestInstances.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Fold fold = new Fold(originalDataSet, indices);
 Dataset[] folds = fold.folds(3, new Random());
 }
}