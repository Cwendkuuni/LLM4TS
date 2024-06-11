package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DefaultDatasetTest {

 private DefaultDataset dataset;

 @Test
 public void testConstructorWithCollection() {
 Instances instances = new Instances(new FileReader("path/to/instances.arff"));
 dataset = new DefaultDataset(instances.stream().collect(Collectors.toList()));
 Assert.assertEquals(instances.size(), dataset.size());
 }

 @Test
 public void testConstructorEmpty() {
 dataset = new DefaultDataset();
 Assert.assertEquals(0, dataset.size());
 }

 @Test
 public void testAddAll() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{0.0}));
 instances.add(new DenseInstance(new Double[]{2.0, 3.0}, new Double[]{1.0}));
 dataset = new DefaultDataset();
 dataset.addAll(instances);
 Assert.assertEquals(instances.size(), dataset.size());
 }

 @Test
 public void testClear() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{0.0}));
 instances.add(new DenseInstance(new Double[]{2.0, 3.0}, new Double[]{1.0}));
 dataset = new DefaultDataset(instances);
 dataset.clear();
 Assert.assertEquals(0, dataset.size());
 }

 @Test
 public void testAdd() {
 Instance instance = new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{0.0});
 dataset = new DefaultDataset();
 dataset.add(instance);
 Assert.assertEquals(1, dataset.size());
 }

 @Test
 public void testNoAttributes() {
 Instance instance = new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{0.0});
 dataset = new DefaultDataset(Collections.singletonList(instance));
 Assert.assertEquals(instance.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 dataset = new DefaultDataset(Collections.singletonList(new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{1.0})));
 Assert.assertEquals(0, dataset.classIndex(0.0));
 }

 @Test
 public void testClassValue() {
 dataset = new DefaultDataset(Collections.singletonList(new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{1.0})));
 Assert.assertEquals(1.0, dataset.classValue(0));
 }

 @Test
 public void testCopy() {
 Instance instance = new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{0.0});
 dataset = new DefaultDataset(Collections.singletonList(instance));
 DefaultDataset copiedDataset = (DefaultDataset) dataset.copy();
 Assert.assertEquals(instance, copiedDataset.instance(0));
 }

 @Test
 public void testKNearestWithEmptyDataset() {
 dataset = new DefaultDataset();
 Assert.assertNull(dataset.kNearest(1, null, null).iterator().next());
 }

 @Test
 public void testKNearestWithOneInstance() {
 Instance instance = new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{0.0});
 dataset = new DefaultDataset(Collections.singletonList(instance));
 Instance nearest = dataset.kNearest(1, null, null).iterator().next();
 Assert.assertEquals(instance, nearest);
 }

 @Test
 public void testKNearestWithMultipleInstances() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{0.0}));
 instances.add(new DenseInstance(new Double[]{2.0, 3.0}, new Double[]{1.0}));
 dataset = new DefaultDataset(instances);
 List<Instance> nearest = dataset.kNearest(1, null, null).keySet().stream().collect(Collectors.toList());
 Assert.assertEquals(1, nearest.size());
 Assert.assertEquals(instances.get(0), nearest.get(0));
 }

 @Test
 public void testFoldsWithEmptyDataset() {
 dataset = new DefaultDataset();
 Assert.assertNull(dataset.folds(5, new Random()));
 }

 @Test
 public void testFoldsWithOneInstance() {
 Instance instance = new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{0.0});
 dataset = new DefaultDataset(Collections.singletonList(instance));
 Dataset[] folds = dataset.folds(5, new Random());
 Assert.assertArrayEquals(new Dataset[5], folds);
 }

 @Test
 public void testFoldsWithMultipleInstances() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{0.0}));
 instances.add(new DenseInstance(new Double[]{2.0, 3.0}, new Double[]{1.0}));
 dataset = new DefaultDataset(instances);
 Dataset[] folds = dataset.folds(5, new Random());
 Assert.assertArrayEquals(new Dataset[5], folds);
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> closest = new HashMap<>();
 closest.put(new DenseInstance(new Double[]{1.0, 2.0}, new Double[]{0.0}), 1.0);
 closest.put(new DenseInstance(new Double[]{2.0, 3.0}, new Double[]{1.0}), 2.0);
 closest.put(new DenseInstance(new Double[]{3.0, 4.0}, new Double[]{0.0}), 3.0);
 Assert.assertEquals(3.0, dataset.removeFarthest(closest));
 Assert.assertEquals(1, closest.size());
 }

 @Test
 public void testRemoveFarthestWithEmptyMap() {
 Map<Instance, Double> closest = new HashMap<>();
 Assert.assertEquals(0.0, dataset.removeFarthest(closest));
 Assert.assertEquals(0, closest.size());
 }
}