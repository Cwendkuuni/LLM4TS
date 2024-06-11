package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.distance.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure measure;

 @Before
 public void setup() {
 // Create instances
 instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, new Double(0.0));
 instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, new Double(1.0));
 instance3 = new DenseInstance(new double[] { 1.0, 2.0, 7.0 }, new Double(0.0));

 // Create distance measure
 measure = new EuclideanDistance();

 // Create dataset
 dataset = new DefaultDataset();
 }

 @Test
 public void testDefaultDataset() {
 // Test default constructor
 dataset = new DefaultDataset();
 assertEquals(0, dataset.size());
 assertNull(dataset.classes());
 }

 @Test
 public void testDefaultDatasetAddAll() {
 // Test addAll method
 ArrayList<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 dataset.addAll(instances);
 assertEquals(2, dataset.size());
 assertEquals(instance1.classValue(), dataset.classes().first());
 assertEquals(instance2.classValue(), dataset.classes().last());
 }

 @Test
 public void testDefaultDatasetAdd() {
 // Test add method
 dataset.add(instance3);
 assertEquals(3, dataset.size());
 assertEquals(instance3.classValue(), dataset.classes().last());
 }

 @Test
 public void testDefaultDatasetClear() {
 // Test clear method
 dataset.clear();
 assertEquals(0, dataset.size());
 assertNull(dataset.classes());
 }

 @Test
 public void testDefaultDatasetRemoveFarthest() {
 // Test removeFarthest method
 Map<Instance, Double> closest = new HashMap<>();
 double max = Double.POSITIVE_INFINITY;
 closest.put(instance1, measure.measure(instance2, instance1));
 closest.put(instance2, measure.measure(instance1, instance2));
 closest.put(instance3, measure.measure(instance1, instance3));
 assertEquals(instance3, dataset.removeFarthest(closest));
 assertEquals(2, dataset.size());
 }

 @Test
 public void testDefaultDatasetKNearest() {
 // Test kNearest method
 Set<Instance> kNearest = dataset.kNearest(2, instance1, measure);
 assertEquals(2, kNearest.size());
 }

 @Test
 public void testDefaultDatasetFolds() {
 // Test folds method
 Dataset[] folds = dataset.folds(3, new Random());
 assertEquals(3, folds.length);
 }

 @Test
 public void testDefaultDatasetNoAttributes() {
 // Test noAttributes method
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testDefaultDatasetClassIndex() {
 // Test classIndex method
 assertEquals(0, dataset.classIndex(instance1.classValue()));
 }

 @Test
 public void testDefaultDatasetClassValue() {
 // Test classValue method
 assertEquals(instance1.classValue(), dataset.classValue(0));
 }

 @Test
 public void testDefaultDatasetCopy() {
 // Test copy method
 DefaultDataset copiedDataset = (DefaultDataset) dataset.copy();
 assertEquals(dataset.size(), copiedDataset.size());
 }
}