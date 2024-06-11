package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.EuclideanDistance;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private List<Instance> instances;
 private DistanceMeasure distanceMeasure;

 @BeforeClass
 public static void setupClass() {
 // Initialize the distance measure
 distanceMeasure = new EuclideanDistance();
 }

 @Before
 public void setup() {
 // Initialize the instances
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0);
 instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1);
 instance3 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0);

 // Initialize the instances list
 instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);

 // Initialize the dataset
 dataset = new DefaultDataset(instances);
 }

 @Test
 public void testDefaultDatasetWithEmptyCollection() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertTrue(emptyDataset.isEmpty());
 assertEquals(0, emptyDataset.size());
 }

 @Test
 public void testDefaultDatasetWithCollection() {
 // Test constructor with a collection
 DefaultDataset constructedDataset = new DefaultDataset(instances);
 assertEquals(instances.size(), constructedDataset.size());
 // Test addAll method
 DefaultDataset datasetWithAddAll = new DefaultDataset();
 datasetWithAddAll.addAll(instances);
 assertEquals(instances.size(), datasetWithAddAll.size());
 }

 @Test
 public void testCheckInstance() {
 // Test check method with a valid instance
 Instance validInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0);
 dataset.check(validInstance);

 // Test check method with a null class value instance
 Instance nullClassValueInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertThrows(IllegalArgumentException.class, () -> dataset.check(nullClassValueInstance));

 // Test check method with an instance having more attributes than the current max
 Instance moreAttributesInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, 0);
 assertThrows(IllegalArgumentException.class, () -> dataset.check(moreAttributesInstance));
 }

 @Test
 public void testAddAll() {
 // Test addAll method with a collection
 dataset.addAll(instances);
 assertEquals(instances.size(), dataset.size());

 // Test addAll method with an index
 dataset.add(0, instance1);
 dataset.addAll(0, instances);
 assertEquals(instances.size() + 1, dataset.size());
 }

 @Test
 public void testClear() {
 // Test clear method
 dataset.clear();
 assertTrue(dataset.isEmpty());
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAdd() {
 // Test add method with a valid instance
 dataset.add(instance1);
 assertEquals(1, dataset.size());

 // Test add method with a null class value instance
 assertThrows(IllegalArgumentException.class, () -> dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, null)));
 }

 @Test
 public void testAddElement() {
 // Test addElement method with a valid instance
 dataset.addElement(instance1);
 assertEquals(1, dataset.size());

 // Test addElement method with an index out of bounds
 assertThrows(IndexOutOfBoundsException.class, () -> dataset.addElement(-1, instance1));
 }

 @Test
 public void testInsertElementAt() {
 // Test insertElementAt method with a valid instance and index
 dataset.insertElementAt(instance1, 0);
 assertEquals(instance1, dataset.instance(0));
 assertEquals(1, dataset.size());

 // Test insertElementAt method with an index out of bounds and a valid instance
 assertThrows(IndexOutOfBoundsException.class, () -> dataset.insertElementAt(instance1, -1));
 }

 @Test
 public void testSetElementAt() {
 // Test setElementAt method with a valid instance and index
 dataset.setElementAt(instance1, 0);
 assertEquals(instance1, dataset.instance(0));
 assertEquals(1, dataset.size());

 // Test setElementAt method with an index out of bounds and a valid instance
 assertThrows(IndexOutOfBoundsException.class, () -> dataset.setElementAt(instance1, -1));
 }

 @Test
 public void testGetInstance() {
 // Test getInstance method with a valid index
 assertEquals(instance1, dataset.instance(0));

 // Test getInstance method with an index out of bounds
 assertNull(dataset.instance(instances.size()));
 }

 @Test
 public void testClasses() {
 // Test classes method with empty dataset
 assertTrue(dataset.classes().isEmpty());

 // Test classes method with dataset having instances with different classes
 dataset.addAll(instances);
 assertEquals(2, dataset.classes().size());
 }

 @Test
 public void testKNearest() {
 // Test kNearest method with k = 1
 List<Instance> kNearestInstances1 = dataset.kNearest(1, instance1, distanceMeasure);
 assertEquals(1, kNearestInstances1.size());
 assertEquals(instance1, kNearestInstances1.get(0));

 // Test kNearest method with k > size of the dataset
 List<Instance> kNearestInstances2 = dataset.kNearest(instances.size() + 1, instance1, distanceMeasure);
 assertEquals(instances.size(), kNearestInstances2.size());

 // Test kNearest method with k = size of the dataset
 List<Instance> kNearestInstances3 = dataset.kNearest(instances.size(), instance1, distanceMeasure);
 assertEquals(instances.size(), kNearestInstances3.size());

 // Test kNearest method with instances having same distance to the query instance
 dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
 List<Instance> kNearestInstances4 = dataset.kNearest(3, instance1, distanceMeasure);
 Instance instance4 = kNearestInstances4.get(0);
 Instance instance5 = kNearestInstances4.get(1);
 assertEquals(instance4.classValue(), instance5.classValue());
 }

 @Test
 public void testFolds() {
 // Test folds method with numFolds = 2 and a random seed of 1
 Dataset[] folds = dataset.folds(2, new Random(1));
 assertEquals(2, folds.length);

 // Test the size of each fold
 for (Dataset fold : folds) {
 assertEquals(instances.size() / 2, fold.size());
 }

 // Test folds method with numFolds = 3 and a random seed of 0
 Dataset[] folds2 = dataset.folds(3, new Random(0));
 assertEquals(3, folds2.length);

 // Test the size of each fold
 for (Dataset fold : folds2) {
 assertEquals(instances.size() / 3, fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 // Test noAttributes method with an empty dataset
 assertEquals(0, new DefaultDataset().noAttributes());

 // Test noAttributes method with a dataset having instances with different number of attributes
 dataset.addAll(instances);
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 // Test classIndex method with a null class
 assertNull(dataset.classIndex(null));

 // Test classIndex method with a valid class
 assertEquals(0, dataset.classIndex(instance1.classValue()));
 }

 @Test
 public void testClassValue() {
 // Test classValue method with a valid index
 assertEquals(instance1.classValue(), dataset.classValue(0));

 // Test classValue method with an index out of bounds
 assertNull(dataset.classValue(instances.size()));
 }

 @Test
 public void testCopy() {
 // Test copy method
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(instances.size(), copiedDataset.size());
 }
}