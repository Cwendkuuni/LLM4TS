package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.distance.*;
import org.junit.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private DefaultDataset emptyDataset;
 private Instance instance1;
 private Instance instance2;
 private Instance instance3;
 private Instance instance4;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 // Create a DefaultDataset with 4 instances
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
 instance2 = new DenseInstance(new double[]{2.0, 2.0, 3.0}, 2.0);
 instance3 = new DenseInstance(new double[]{3.0, 2.0, 3.0}, 3.0);
 instance4 = new DenseInstance(new double[]{4.0, 2.0, 3.0}, 1.0);

 ArrayList<Instance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 instances.add(instance4);

 dataset = new DefaultDataset(instances);

 // Create an empty DefaultDataset
 emptyDataset = new DefaultDataset();
 }

 @After
 public void tearDown() {
 // Clear the datasets to avoid memory leaks
 dataset.clear();
 emptyDataset.clear();
 }

 @Test
 public void testDefaultDatasetConstructor() {
 // Test the empty constructor
 assertTrue(emptyDataset.isEmpty());
 assertEquals(0, emptyDataset.size());
 assertEquals(0, emptyDataset.maxAttributes());
 assertNull(emptyDataset.classes());

 // Test the constructor with a collection of instances
 assertEquals(4, dataset.size());
 assertEquals(3, dataset.maxAttributes());
 assertNotNull(dataset.classes());
 }

 @Test
 public void testCheck() {
 // Test the private check method when adding a collection of instances
 Collection<Instance> instancesToAdd = new ArrayList<>();
 instancesToAdd.add(new DenseInstance(new double[]{5.0, 6.0, 7.0}, 4.0));
 instancesToAdd.add(new DenseInstance(new double[]{5.1, 6.1, 7.1}, 4.1));

 dataset.check(instancesToAdd);
 assertTrue(dataset.addAll(instancesToAdd));
 assertEquals(6, dataset.size());
 assertEquals(3, dataset.maxAttributes());

 // Test the private check method when adding an instance
 Instance instanceToCheck = new DenseInstance(new double[]{8.0, 9.0, 10.0}, 5.0);

 dataset.check(instanceToCheck);
 assertTrue(dataset.add(instanceToCheck));
 assertEquals(7, dataset.size());
 assertEquals(3, dataset.maxAttributes());
 }

 @Test
 public void testAddAll() {
 // Test adding a collection of instances at the beginning of the dataset
 Collection<Instance> instancesToAdd = new ArrayList<>();
 instancesToAdd.add(instance1);
 instancesToAdd.add(instance2);

 assertTrue(dataset.addAll(instancesToAdd));
 assertEquals(6, dataset.size());
 assertEquals(3, dataset.maxAttributes());

 // Test adding a collection of instances at a specific index
 int index = 3;

 assertTrue(dataset.addAll(index, instancesToAdd));
 assertEquals(7, dataset.size());
 assertEquals(3, dataset.maxAttributes());

 Instance instanceAtIndex = dataset.instance(index);
 assertEquals(instance2.classValue(), instanceAtIndex.classValue());
 }

 @Test
 public void testClear() {
 // Test clearing the dataset
 dataset.clear();

 assertEquals(0, dataset.size());
 assertEquals(0, dataset.maxAttributes());
 assertNull(dataset.classes());
 }

 @Test
 public void testAdd() {
 // Test adding an instance
 Instance newInstance = new DenseInstance(new double[]{11.0, 12.0, 13.0}, 6.0);

 assertTrue(dataset.add(newInstance));
 assertEquals(8, dataset.size());
 assertEquals(3, dataset.maxAttributes());

 // Test adding an instance with the same attributes and class value
 Instance identicalInstance = new DenseInstance(new double[]{11.0, 12.0, 13.0}, 6.0);

 assertTrue(dataset.add(identicalInstance));
 assertEquals(8, dataset.size());
 }

 @Test
 public void testAddElement() {
 // Test adding an element
 Instance newElement = new DenseInstance(new double[]{14.0, 15.0, 16.0}, 7.0);

 assertTrue(dataset.addElement(newElement));
 assertEquals(9, dataset.size());
 assertEquals(3, dataset.maxAttributes());
 }

 @Test
 public void testInsertElementAt() {
 // Test inserting an element at a specific index
 int indexToInsert = 4;

 assertTrue(dataset.insertElementAt(instance3, indexToInsert));
 assertEquals(9, dataset.size());
 assertEquals(3, dataset.maxAttributes());

 Instance instanceAtIndex = dataset.instance(indexToInsert);
 assertEquals(instance3.classValue(), instanceAtIndex.classValue());
 }

 @Test
 public void testSetElementAt() {
 // Test setting an element at a specific index
 Instance newElement = new DenseInstance(new double[]{17.0, 18.0, 19.0}, 8.0);

 assertTrue(dataset.setElementAt(newElement, 5));
 assertEquals(9, dataset.size());
 assertEquals(3, dataset.maxAttributes());

 Instance instanceAtIndex = dataset.instance(5);
 assertEquals(newElement.classValue(), instanceAtIndex.classValue());
 }

 @Test
 public void testInstance() {
 // Test getting an instance by index
 Instance instance = dataset.instance(2);
 assertEquals(instance2.classValue(), instance.classValue());
 }

 @Test
 public void testClasses() {
 // Test getting the classes
 assertEquals(3, dataset.classes().size());
 assertTrue(dataset.classes().contains(instance1.classValue()));
 assertTrue(dataset.classes().contains(instance2.classValue()));
 assertTrue(dataset.classes().contains(instance3.classValue()));
 }

 @Test
 public void testKNearest() {
 // Test the kNearest method with a small dataset
 distanceMeasure = new EuclideanDistance();

 Set<Instance> kNearestInstances = dataset.kNearest(3, instance1, distanceMeasure);

 assertEquals(3, kNearestInstances.size());

 Instance nearestInstance = kNearestInstances.iterator().next();
 assertEquals(instance1.classValue(), nearestInstance.classValue());

 // Test the kNearest method with a larger dataset
 dataset.addAll(Arrays.asList(instance4));

 kNearestInstances = dataset.kNearest(3, instance1, distanceMeasure);

 assertEquals(3, kNearestInstances.size());

 nearestInstance = kNearestInstances.iterator().next();
 assertNotEquals(instance1.classValue(), nearestInstance.classValue());
 }

 @Test
 public void testFolds() {
 // Test the folds method with a small dataset
 int numFolds = 2;

 Dataset[] folds = dataset.folds(numFolds, new Random());

 assertEquals(numFolds, folds.length);

 for (Dataset fold : folds) {
 assertEquals(2, fold.size());
 }

 // Test the folds method with a larger dataset
 dataset.addAll(Arrays.asList(instance4));

 folds = dataset.folds(numFolds, new Random());

 assertEquals(numFolds, folds.length);

 for (Dataset fold : folds) {
 assertEquals(3, fold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(1, dataset.classIndex(instance1.classValue()));
 assertEquals(-1, dataset.classIndex(null));
 }

 @Test
 public void testClassValue() {
 assertEquals(instance1.classValue(), dataset.classValue(0));
 }

 @Test
 public void testCopy() {
 // Test creating a copy of the dataset
 DefaultDataset copy = dataset.copy();

 assertEquals(7, copy.size());
 assertEquals(3, copy.maxAttributes());

 for (int i = 0; i < copy.size(); ++i) {
 Instance instanceCopy = copy.instance(i);
 Instance instanceOriginal = dataset.instance(i);

 assertEquals(instanceOriginal.classValue(), instanceCopy.classValue());

 for (int j = 0; j < instanceOriginal.noAttributes(); ++j) {
 assertEquals(instanceOriginal.value(j), instanceCopy.value(j), 0.001);
 }
 }
 }
}