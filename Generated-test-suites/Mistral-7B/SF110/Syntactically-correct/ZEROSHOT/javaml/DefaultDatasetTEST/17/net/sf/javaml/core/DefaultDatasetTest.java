package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.distance.*;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private DefaultDataset emptyDataset;
 private Instance instance1;
 private Instance instance2;
 private Instance instance3;
 private Instance instance4;
 private Collection<Instance> instances;
 private Instance[] testInstances;
 private DistanceMeasure distanceMeasure;
 private Random random;

 @Before
 public void setUp() {
 dataset = new DefaultDataset();
 emptyDataset = new DefaultDataset();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0));
 instance2 = new DenseInstance(new double[]{2.1, 3.1, 4.1}, new Double(1.0));
 instance3 = new DenseInstance(new double[]{3.2, 4.2, 5.2}, new Double(0.0));
 instance4 = new DenseInstance(new double[]{4.3, 5.3, 6.3}, new Double(1.0));
 instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 instances.add(instance4);
 testInstances = new Instance[]{instance1, instance2, instance3, instance4};
 distanceMeasure = new EuclideanDistance();
 random = new Random();
 }

 @Test
 public void testDefaultDatasetEmpty() {
 assertNotNull(emptyDataset);
 assertFalse(emptyDataset.isEmpty());
 assertEquals(0, emptyDataset.size());
 assertTrue(emptyDataset.classes().isEmpty());
 }

 @Test
 public void testDefaultDatasetWithInstances() {
 assertNotNull(dataset);
 assertFalse(dataset.isEmpty());
 assertEquals(0, dataset.size());
 assertTrue(dataset.classes().isEmpty());

 dataset.addAll(instances);

 assertEquals(instances.size(), dataset.size());
 assertFalse(dataset.isEmpty());

 Set<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains(new Double(0.0)));
 assertTrue(classes.contains(new Double(1.0)));

 assertEquals(instance1, dataset.instance(0));
 assertEquals(instance2, dataset.instance(1));
 assertEquals(instance3, dataset.instance(2));
 assertEquals(instance4, dataset.instance(3));
 }

 @Test
 public void testAddAll() {
 dataset = new DefaultDataset();

 dataset.addAll(instances);

 assertEquals(instances.size(), dataset.size());
 assertFalse(dataset.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNullInstances() {
 dataset = new DefaultDataset();

 try {
 dataset.addAll(null);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllDuplicateInstances() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 try {
 dataset.addAll(instances);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAddInstance() {
 dataset = new DefaultDataset();

 dataset.add(instance1);

 assertEquals(1, dataset.size());
 assertFalse(dataset.isEmpty());

 assertEquals(instance1, dataset.instance(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 dataset = new DefaultDataset();

 try {
 dataset.add(null);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testClear() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 dataset.clear();

 assertEquals(0, dataset.size());
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testRemoveInstance() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(4, dataset.size());
 assertFalse(dataset.isEmpty());

 dataset.remove(instance3);

 assertEquals(3, dataset.size());
 assertFalse(dataset.isEmpty());

 assertFalse(dataset.contains(instance3));
 }

 @Test
 public void testRemoveInstanceInvalidIndex() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(4, dataset.size());
 assertFalse(dataset.isEmpty());

 try {
 dataset.remove(Integer.MAX_VALUE);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveInstanceNegativeIndex() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(4, dataset.size());
 assertFalse(dataset.isEmpty());

 try {
 dataset.remove(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testGet() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(instance1, dataset.instance(0));
 assertEquals(instance2, dataset.instance(1));
 assertEquals(instance3, dataset.instance(2));
 assertEquals(instance4, dataset.instance(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInvalidIndex() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 try {
 dataset.instance(Integer.MAX_VALUE);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 try {
 dataset.instance(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testClasses() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 Set<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains(new Double(0.0)));
 assertTrue(classes.contains(new Double(1.0)));
 }

 @Test
 public void testKNearest() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 Set<Instance> kNearest = dataset.kNearest(2, instance1, distanceMeasure);
 assertEquals(2, kNearest.size());

 assertTrue(kNearest.contains(instance1));
 assertTrue(kNearest.contains(instance2));

 assertFalse(kNearest.contains(instance3));
 assertFalse(kNearest.contains(instance4));
 }

 @Test
 public void testFolds() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 Dataset[] folds = dataset.folds(4, random);
 assertEquals(4, folds.length);

 for (Dataset fold : folds) {
 assertTrue(fold instanceof Fold);
 }
 }

 @Test
 public void testNoAttributes() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(0, dataset.classIndex(new Double(0.0)));
 assertEquals(1, dataset.classIndex(new Double(1.0)));

 try {
 dataset.classIndex(null);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testClassValue() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 assertEquals(new Double(0.0), dataset.classValue(0));
 assertEquals(new Double(1.0), dataset.classValue(1));

 try {
 dataset.classValue(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testCopy() {
 dataset = new DefaultDataset();
 dataset.addAll(instances);

 Dataset copy = dataset.copy();

 assertEquals(dataset.size(), copy.size());
 assertFalse(dataset.isEmpty());
 assertFalse(copy.isEmpty());

 for (int i = 0; i < dataset.size(); i++) {
 assertEquals(dataset.instance(i), copy.instance(i));
 }
 }
}