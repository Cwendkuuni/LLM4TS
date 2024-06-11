package net.sf.javaml.core.test;

import junit.framework.Assert;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.EuclideanDistance;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultDatasetTest {

 @org.junit.Test
 public void testConstructorWithCollection() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 Assert.assertEquals(3, ds.size());
 }

 @org.junit.Test
 public void testConstructorWithEmptyCollection() {
 DefaultDataset ds = new DefaultDataset();
 Assert.assertEquals(0, ds.size());
 }

 @org.junit.Test
 public void testAddAll() {
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(getInstances());
 Assert.assertEquals(3, ds.size());
 }

 @org.junit.Test
 public void testAddAllWithInvalidInstance() {
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(Arrays.asList(new Object[] { new Object(), null }));
 Assert.assertEquals(3, ds.size());
 }

 @org.junit.Test
 public void testClear() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 ds.clear();
 Assert.assertEquals(0, ds.size());
 }

 @org.junit.Test
 public void testAdd() {
 DefaultDataset ds = new DefaultDataset();
 ds.add(getInstance(1));
 Assert.assertEquals(1, ds.size());
 }

 @org.junit.Test
 public void testAddWithInvalidInstance() {
 DefaultDataset ds = new DefaultDataset();
 ds.add(new Object());
 Assert.assertEquals(1, ds.size());
 }

 @org.junit.Test
 public void testInstance() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 Assert.assertEquals(getInstance(0), ds.instance(0));
 }

 @org.junit.Test
 public void testClasses() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 Set<Object> classes = ds.classes();
 Assert.assertEquals(2, classes.size());
 Assert.assertTrue(classes.contains(getClassValue(0)));
 Assert.assertTrue(classes.contains(getClassValue(1)));
 }

 @org.junit.Test
 public void testKNearestWithValidInstance() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 Instance testInstance = getInstance(2);
 List<Instance> result = ds.kNearest(2, testInstance, new EuclideanDistance());
 Assert.assertEquals(2, result.size());
 }

 @org.junit.Test
 public void testKNearestWithInvalidInstance() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 Instance testInstance = null;
 try {
 ds.kNearest(2, testInstance, new EuclideanDistance());
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testFolds() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 Dataset[] folds = ds.folds(5, new Random());
 Assert.assertArrayEquals(new int[] { 0, 2, 1, 2, 0 }, getFoldIndices(folds[0]));
 Assert.assertArrayEquals(new int[] { 2, 0, 1, 0, 2 }, getFoldIndices(folds[1]));
 }

 @org.junit.Test
 public void testNoAttributes() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 Assert.assertEquals(4, ds.noAttributes());
 }

 @org.junit.Test
 public void testClassIndex() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 Assert.assertEquals(1, ds.classIndex(getClassValue(1)));
 }

 @org.junit.Test
 public void testClassValue() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 Assert.assertEquals(getClassValue(0), ds.classValue(0));
 }

 @org.junit.Test
 public void testCopy() {
 DefaultDataset ds = new DefaultDataset(getInstances());
 DefaultDataset copy = ds.copy();
 Assert.assertEquals(ds.size(), copy.size());
 }

 private List<Instance> getInstances() {
 List<Instance> instances = new ArrayList<Instance>();
 instances.add(getInstance(0));
 instances.add(getInstance(1));
 instances.add(getInstance(2));
 return instances;
 }

 private Instance getInstance(int index) {
 Instance instance = new DenseInstance(4.0);
 instance.setDatasetIndex(index);
 instance.setClassValue(getClassValue(index));
 return instance;
 }

 private Object getClassValue(int index) {
 return index % 2 == 0 ? "ClassA" : "ClassB";
 }

 private int[] getFoldIndices(Dataset fold) {
 return ((Fold) fold).indices();
 }
}