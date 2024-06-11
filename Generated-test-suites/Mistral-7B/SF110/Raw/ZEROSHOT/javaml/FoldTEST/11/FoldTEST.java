package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.instance.InstanceFactory;
import net.sf.javaml.instance.InstanceList;
import net.sf.javaml.instance.Instances;
import net.sf.javaml.instance.Dataset;
import net.sf.javaml.instance.Attribute;
import net.sf.javaml.instance.AttributeFactory;
import net.sf.javaml.tools.data.DataFormat;

public class FoldTest {

 @Test
 public void testFoldClass() {
 Fold fold = new Fold(createTestDataset(), new int[] { 0, 1, 2, 3, 4 });
 assertTrue(fold instanceof Dataset);
 assertEquals(5, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(createTestDataset(), new int[] { 0, 1, 2, 3, 4 });
 fold.add(InstanceFactory.createDenseInstance(new double[] { 1.0, 2.0 }));
 }

 @Test
 public void testClasses() {
 Fold fold = new Fold(createTestDataset(), new int[] { 0, 1, 2, 3, 4 });
 Set<Object> classes = fold.classes();
 assertEquals(2, classes.size());
 }

 @Test
 public void testFolds() {
 Fold fold = new Fold(createTestDataset(), new int[] { 0, 1, 2, 3, 4 });
 Dataset[] folds = fold.folds(5, new Random());
 assertArrayEquals(5, folds.length);
 for (Dataset foldSet : folds) {
 assertEquals(2, foldSet.size());
 }
 }

 @Test
 public void testInstance() {
 Fold fold = new Fold(createTestDataset(), new int[] { 0, 1, 2, 3, 4 });
 Instance instance = fold.instance(0);
 assertNotNull(instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 Fold fold = new Fold(createTestDataset(), new int[] { 0, 1, 2, 3, 4 });
 fold.set(0, InstanceFactory.createDenseInstance(new double[] { 1.0, 2.0 }));
 }

 @Test
 public void testSize() {
 Fold fold = new Fold(createTestDataset(), new int[] { 0, 1, 2, 3, 4 });
 assertEquals(5, fold.size());
 }

 @Test
 public void testInstanceWithDistance() {
 Fold fold = new Fold(createTestDataset(), new int[] { 0, 1, 2, 3, 4 });
 Instance instance1 = fold.instance(0);
 Instance instance2 = fold.instance(1);
 double distance = new EuclideanDistance().distance(instance1, instance2);
 assertEquals(1.4142135625, distance, 0.01);
 }

 @Test
 public void testIterator() {
 Fold fold = new Fold(createTestDataset(), new int[] { 0, 1, 2, 3, 4 });
 List<Instance> instances = new ArrayList<Instance>();
 FoldIterator iterator = fold.iterator();
 while (iterator.hasNext()) {
 instances.add(iterator.next());
 }
 assertEquals(5, instances.size());
 }

 private static Dataset createTestDataset() {
 String[] names = { "A", "B" };
 Attribute[] attributes = new Attribute[2];
 for (int i = 0; i < 2; i++) {
 attributes[i] = AttributeFactory.nominalAttribute(names[i]);
 }
 Instances instances = new Instances(DataFormat.read("test.arff"), false);
 instances.setClassIndex(instances.numAttributes() - 1);
 return instances;
 }

}