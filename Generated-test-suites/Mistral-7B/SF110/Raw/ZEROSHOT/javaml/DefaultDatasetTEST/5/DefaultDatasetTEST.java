package net.sf.javaml.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(Parameterized.class)
public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private List<Instance> instances;

 public DefaultDatasetTest(List<Instance> instances) {
 this.instances = instances;
 }

 @Parameterized.Parameters(name = "{index}: Instances = {0}")
 public static List<Object> data() {
 return Arrays.asList(new Object[][]{
 // Test case 1
 new Object[]{ Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.1, 3.1}, 1.0),
 new DenseInstance(new double[]{1.0, 2.2, 3.2}, 1.0),
 new DenseInstance(new double[]{1.0, 2.3, 3.3}, 2.0),
 new DenseInstance(new double[]{1.1, 2.1, 3.1}, 1.0),
 new DenseInstance(new double[]{1.1, 2.2, 3.2}, 2.0),
 new DenseInstance(new double[]{1.1, 2.3, 3.3}, 1.0)
 )},
 // Test case 2
 new Object[]{ Arrays.asList(
 new DenseInstance(new double[]{1.0, 2.0}, 1.0),
 new DenseInstance(new double[]{1.0, 2.0}, 1.0),
 new DenseInstance(new double[]{1.0, 2.0}, 2.0),
 new DenseInstance(new double[]{1.1, 2.1}, 1.0),
 new DenseInstance(new double[]{1.1, 2.1}, 2.0),
 new DenseInstance(new double[]{1.1, 2.1}, 1.0)
 )}
 });
 }

 @Test
 public void testDefaultDataset() {
 dataset = new DefaultDataset();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testDefaultDatasetWithInstances() {
 dataset = new DefaultDataset(instances);
 assertEquals(instances.size(), dataset.size());
 }

 @Test
 public void testAddAll() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);
 assertEquals(instances.size(), dataset.size());
 }

 @Test
 public void testAddAll_WithCheck() {
 final DefaultDataset dataset1 = new DefaultDataset();
 dataset1.addAll(instances);
 final DefaultDataset dataset2 = new DefaultDataset();
 dataset2.addAll(instances);
 assertArrayEquals(dataset1.classes().toArray(), dataset2.classes().toArray());
 }

 @Test
 public void testClear() {
 dataset = new DefaultDataset(instances);
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAdd() {
 dataset = new DefaultDataset();
 final Instance instance = new DenseInstance(new double[]{1.0, 2.1, 3.1}, 1.0);
 dataset.add(instance);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testAdd_WithCheck() {
 final DefaultDataset dataset1 = new DefaultDataset();
 dataset1.add(new DenseInstance(new double[]{1.0, 2.1, 3.1}, 1.0));
 final DefaultDataset dataset2 = new DefaultDataset();
 dataset2.add(new DenseInstance(new double[]{1.0, 2.1, 3.1}, 1.0));
 assertArrayEquals(dataset1.classes().toArray(), dataset2.classes().toArray());
 }

 @Test
 public void testClasses() {
 dataset = new DefaultDataset(instances);
 Set<Object> classes = new HashSet<>();
 for (Instance instance : dataset) {
 classes.add(instance.classValue());
 }
 assertEquals(classes.size(), dataset.classes().size());
 }

 @Test
 public void testKNearest() {
 dataset = new DefaultDataset(instances);
 final int k = 2;
 final Instance testInstance = new DenseInstance(new double[]{1.5, 2.5}, null);
 final DistanceMeasure distanceMeasure = new EuclideanDistance();
 Set<Instance> nearestInstances = dataset.kNearest(k, testInstance, distanceMeasure);
 assertEquals(k, nearestInstances.size());

 List<Instance> expectedInstances = Arrays.asList(
 // Replace with the expected instances based on your test case
 );
 assertEquals(expectedInstances, new ArrayList<>(nearestInstances));
 }

 @Test
 public void testFolds() {
 dataset = new DefaultDataset(instances);
 final int numFolds = 3;
 final Random rg = new Random();
 Dataset[] folds = dataset.folds(numFolds, rg);
 assertEquals(numFolds, folds.length);
 }

 @Test
 public void testNoAttributes() {
 dataset = new DefaultDataset(instances);
 assertEquals(instances.get(0).noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 dataset = new DefaultDataset(instances);
 assertEquals(1, dataset.classIndex(1.0));
 }

 @Test
 public void testClassValue() {
 dataset = new DefaultDataset(instances);
 assertEquals(1.0, dataset.classValue(0).getClass().getDeclaredField("value").get(dataset.classValue(0)));
 }

 @Test
 public void testCopy() {
 dataset = new DefaultDataset(instances);
 DefaultDataset copy = dataset.copy();
 assertEquals(instances.size(), copy.size());
 }
}