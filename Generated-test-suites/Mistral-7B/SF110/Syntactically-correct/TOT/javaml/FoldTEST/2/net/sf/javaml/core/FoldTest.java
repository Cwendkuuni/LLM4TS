package net.sf.javaml.core;

import junit.framework.TestCase;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.instance.Instance;
import net.sf.javaml.instance.InstanceFactory;
import net.sf.javaml.instance.InstanceList;
import net.sf.javaml.instance.InstanceLoader;
import net.sf.javaml.instance.InstanceVector;
import net.sf.javaml.instance.label.Label;

public class FoldTest extends TestCase {

 private static final int NUM_FOLDS = 5;
 private static final int NUM_INSTANCES = 10;

 @org.junit.BeforeClass
 public static void setUpBeforeClass() throws Exception {
 Instances data = InstanceLoader.loadInstances("iris.arff");
 Dataset originalDataset = data;
 Dataset foldedDataset = new Fold(originalDataset, createFoldIndices(originalDataset, NUM_FOLDS));
 }

 @org.junit.Test
 public void testSize() {
 assertEquals(NUM_INSTANCES, originalDataset.size());
 assertEquals(NUM_INSTANCES / NUM_FOLDS, foldedDataset.size());
 }

 @org.junit.Test
 public void testGetInstance() {
 Instance instanceExpected = originalDataset.instance(0);
 Instance instanceActual = foldedDataset.instance(0);
 assertEquals(instanceExpected, instanceActual);
 }

 @org.junit.Test
 public void testGetInstanceWithInvalidIndex() {
 try {
 foldedDataset.instance(-1);
 fail("Expected an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testClasses() {
 Set<Object> classes = originalDataset.classes();
 Set<Object> classesFold = foldedDataset.classes();
 assertEquals(classes, classesFold);
 }

 @org.junit.Test
 public void testAdd() {
 try {
 foldedDataset.add(new DenseInstance(new double[originalDataset.noAttributes()]));
 fail("Expected an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testFolds() {
 try {
 foldedDataset.folds(NUM_FOLDS, new Random());
 fail("Expected an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testKNearestWithValidArguments() {
 Instance testInstance = new InstanceVector(new double[]{1.0, 0.5, 3.5, 1.2});
 Label testLabel = new Label(3);
 Instance nearestInstance = originalDataset.kNearest(1, testInstance, new EuclideanDistance())
 .get(0);
 Instance nearestInstanceFold = foldedDataset.kNearest(1, testInstance, new EuclideanDistance())
 .get(0);
 assertEquals(nearestInstance, nearestInstanceFold);
 }

 @org.junit.Test
 public void testKNearestWithInvalidArguments() {
 try {
 foldedDataset.kNearest(-1, new InstanceVector(new double[originalDataset.noAttributes()]), new EuclideanDistance());
 fail("Expected an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 private static int[] createFoldIndices(Dataset dataset, int numFolds) {
 int size = dataset.size();
 int sliceSize = size / numFolds;
 int index = 0;
 int[] indices = new int[size];
 for (int i = 0; i < numFolds; i++) {
 for (int j = 0; j < sliceSize; j++) {
 indices[index++] = i * sliceSize + j;
 }
 }
 return indices;
 }
}