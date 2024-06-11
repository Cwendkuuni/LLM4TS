package net.sf.javaml.core.test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.EuclideanDistance;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.*;

@RunWith(JUnit4.class)
public class DefaultDatasetTest {

 @Test
 public void testDefaultDatasetEmpty() {
 DefaultDataset ds = new DefaultDataset();
 Assert.assertEquals(0, ds.size());
 }

 @Test
 public void testDefaultDatasetWithInstances() {
 // Create some instances
 Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1");
 Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2");
 Instance inst3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1");

 // Add instances to the dataset
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(Arrays.asList(inst1, inst2, inst3));

 // Check the size and attributes of the dataset
 Assert.assertEquals(3, ds.size());
 Assert.assertEquals(3, ds.noAttributes());

 // Check the classes in the dataset
 SortedSet<Object> classes = ds.classes();
 Assert.assertEquals(2, classes.size());
 Assert.assertEquals("class1", classes.first());
 Assert.assertEquals("class2", classes.last());
 }

 @Test
 public void testAddAll() {
 // Create some instances
 Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1");
 Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2");
 Instance inst3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1");

 // Add instances to the dataset
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(Arrays.asList(inst1, inst2, inst3));

 // Add more instances and check the size
 ds.addAll(Arrays.asList(new DenseInstance(new double[]{11.0, 12.0, 13.0}, "class3")));
 Assert.assertEquals(4, ds.size());
 }

 @Test
 public void testClear() {
 // Create a dataset with some instances
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2")));

 // Clear the dataset and check the size
 ds.clear();
 Assert.assertEquals(0, ds.size());
 }

 @Test
 public void testAdd() {
 // Create some instances
 Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1");
 Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2");

 // Add instances to the dataset
 DefaultDataset ds = new DefaultDataset();
 ds.add(inst1);
 ds.add(inst2);

 // Check the size and attributes of the dataset
 Assert.assertEquals(2, ds.size());
 Assert.assertEquals(3, ds.noAttributes());
 }

 @Test
 public void testKNearest() {
 // Create some instances with Euclidean distance measure
 Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1");
 Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2");
 Instance inst3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1");
 Instance inst4 = new DenseInstance(new double[]{10.0, 11.0, 12.0}, "class3");

 // Add instances to the dataset
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(Arrays.asList(inst1, inst2, inst3, inst4));

 // Find the k nearest neighbors to inst1
 DistanceMeasure dm = new EuclideanDistance();
 Set<Instance> neighbors = ds.kNearest(3, inst1, dm);

 // Check the number of neighbors and their distances
 Assert.assertEquals(3, neighbors.size());
 Assert.assertTrue(dm.compare(dm.measure(inst1, inst1), ds.instance(ds.indexOf(inst1)).distance(inst1, dm)) <= 0);
 Assert.assertTrue(dm.compare(dm.measure(inst1, inst2), dm.measure(inst1, inst3)) <= 0);
 Assert.assertTrue(dm.compare(dm.measure(inst1, inst3), dm.measure(inst1, inst4)) <= 0);
 }

 @Test
 public void testFolds() {
 // Create some instances
 Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1");
 Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2");
 Instance inst3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1");
 Instance inst4 = new DenseInstance(new double[]{10.0, 11.0, 12.0}, "class3");

 // Add instances to the dataset
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(Arrays.asList(inst1, inst2, inst3, inst4));

 // Split the dataset into folds
 int numFolds = 2;
 Random rg = new Random();
 Dataset[] folds = ds.folds(numFolds, rg);

 // Check the number of instances in each fold
 for (Dataset fold : folds) {
 Assert.assertEquals(ds.size() / numFolds, fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 // Create a dataset with some instances
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(Arrays.asList(new DenseInstance(new double[]{1.0}, "class1"),
 new DenseInstance(new double[]{2.0}, "class2")));

 // Check the number of attributes
 Assert.assertEquals(1, ds.noAttributes());
 }

 @Test
 public void testClassIndex() {
 // Create a dataset with some instances
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"),
 new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1")));

 // Check the class index for each class
 Assert.assertEquals(0, ds.classIndex("class1"));
 Assert.assertEquals(1, ds.classIndex("class2"));
 }

 @Test
 public void testClassValue() {
 // Create a dataset with some instances
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"),
 new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1")));

 // Check the class value for each instance
 Assert.assertEquals("class1", ds.classValue(0));
 Assert.assertEquals("class2", ds.classValue(1));
 Assert.assertEquals("class1", ds.classValue(2));
 }

 @Test
 public void testCopy() {
 // Create a dataset with some instances
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"),
 new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"),
 new DenseInstance(new double[]{7.0, 8.0, 9.0}, "class1")));

 // Create a copy of the dataset
 DefaultDataset copy = ds.copy();

 // Check the size and attributes of the copied dataset
 Assert.assertEquals(ds.size(), copy.size());
 Assert.assertEquals(ds.noAttributes(), copy.noAttributes());

 // Check the classes in the copied dataset
 SortedSet<Object> classes = copy.classes();
 Assert.assertEquals(ds.classes().size(), classes.size());
 Assert.assertEquals(ds.classes().first(), classes.first());
 Assert.assertEquals(ds.classes().last(), classes.last());
 }
}