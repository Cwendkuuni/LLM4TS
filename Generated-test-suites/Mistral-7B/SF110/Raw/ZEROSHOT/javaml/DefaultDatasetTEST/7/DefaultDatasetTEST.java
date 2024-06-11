package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.distance.*;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset createEmptyDataset() {
 return new DefaultDataset();
 }

 private DefaultDataset createDatasetWithInstances(Instance... instances) {
 Collection<Instance> coll = Arrays.asList(instances);
 return new DefaultDataset(coll);
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset ds = createEmptyDataset();
 assertEquals(0, ds.size());
 assertEquals(0, ds.maxAttributes);
 assertNotNull(ds.classes);
 assertTrue(ds.classes.isEmpty());
 }

 @Test
 public void testCollectionConstructor() {
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"});
 instance1.setClassValue("class1");
 Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"d", "e", "f"});
 instance2.setClassValue("class2");

 DefaultDataset ds = createDatasetWithInstances(instance1, instance2);
 assertEquals(2, ds.size());
 assertEquals(3, ds.maxAttributes);
 assertNotNull(ds.classes);
 assertEquals(2, ds.classes.size());
 assertTrue(ds.classes.containsAll(Arrays.asList("class1", "class2")));
 }

 @Test
 public void testAddAll() {
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"});
 instance1.setClassValue("class1");
 Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"d", "e", "f"});
 instance2.setClassValue("class2");

 DefaultDataset ds = createEmptyDataset();
 ds.addAll(Arrays.asList(instance1, instance2));
 assertEquals(2, ds.size());
 assertEquals(3, ds.maxAttributes);
 assertNotNull(ds.classes);
 assertEquals(2, ds.classes.size());
 assertTrue(ds.classes.containsAll(Arrays.asList("class1", "class2")));
 }

 @Test
 public void testAdd() {
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"});
 instance1.setClassValue("class1");
 DefaultDataset ds = createEmptyDataset();
 ds.add(instance1);
 assertEquals(1, ds.size());
 assertEquals(3, ds.maxAttributes);
 assertNotNull(ds.classes);
 assertEquals(1, ds.classes.size());
 assertTrue(ds.classes.contains("class1"));
 }

 @Test
 public void testRemove() {
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"});
 instance1.setClassValue("class1");
 DefaultDataset ds = createDatasetWithInstances(instance1);
 ds.remove(instance1);
 assertEquals(0, ds.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedOperationRemove() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"}));
 ds.remove(0);
 }

 @Test
 public void testClear() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"}));
 ds.clear();
 assertEquals(0, ds.size());
 assertNotNull(ds.classes);
 assertTrue(ds.classes.isEmpty());
 }

 @Test
 public void testClasses() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"d", "e", "f"}));
 assertEquals(2, ds.classes().size());
 assertTrue(ds.classes.containsAll(Arrays.asList("class1", "class2")));
 }

 @Test
 public void testClassIndex() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"d", "e", "f"}));
 assertEquals(0, ds.classIndex("class1"));
 assertEquals(1, ds.classIndex("class2"));
 assertEquals(-1, ds.classIndex(null));
 }

 @Test
 public void testClassValue() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"d", "e", "f"}));
 assertEquals("class1", ds.classValue(0));
 assertEquals("class2", ds.classValue(1));
 assertEquals(null, ds.classValue(2));
 }

 @Test
 public void testCopy() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"d", "e", "f"}));
 DefaultDataset copiedDs = ds.copy();
 assertEquals(2, copiedDs.size());
 assertEquals(3, copiedDs.maxAttributes);
 assertNotNull(copiedDs.classes);
 assertEquals(2, copiedDs.classes.size());
 assertTrue(copiedDs.classes.containsAll(Arrays.asList("class1", "class2")));
 }

 @Test
 public void testKNearest() {
 Instance instanceToFindNearest = new DenseInstance(new double[]{2.5, 2.5, 2.5}, new String[]{"a", "b", "c"});
 instanceToFindNearest.setClassValue("class1");
 DefaultDataset ds = createDatasetWithInstances(
 new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"}),
 new DenseInstance(new double[]{2.0, 2.1, 2.2}, new String[]{"a", "b", "c"}),
 new DenseInstance(new double[]{3.0, 3.1, 3.2}, new String[]{"d", "e", "f"}),
 new DenseInstance(new double[]{4.0, 4.1, 4.2}, new String[]{"d", "e", "f"}),
 new DenseInstance(new double[]{5.0, 5.1, 5.2}, new String[]{"d", "e", "f"})
 );
 List<Instance> nearestInstances = ds.kNearest(2, instanceToFindNearest, new EuclideanDistance());
 assertEquals(2, nearestInstances.size());
 assertTrue(nearestInstances.contains(ds.instance(1)));
 assertTrue(nearestInstances.contains(ds.instance(3)));
 }

 @Test
 public void testFolds() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"}), new DenseInstance(new double[]{4.0, 5.0, 6.0}, new String[]{"d", "e", "f"}));
 Dataset[] folds = ds.folds(3, new SecureRandom());
 assertEquals(3, folds.length);
 for (Dataset fold : folds) {
 assertTrue(fold instanceof Fold);
 }
 }

 @Test
 public void testNoAttributes() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new String[]{"a", "b", "c"}));
 assertEquals(3, ds.noAttributes());
 }

}