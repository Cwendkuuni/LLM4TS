package net.sf.javaml.core.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import java.util.*;

public class DefaultDatasetTest extends TestCase {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3, instance4;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 dataset = new DefaultDataset();
 distanceMeasure = new EuclideanDistance();

 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.2, 0.5, 0.1});
 instance2 = new DenseInstance(new double[]{1.1, 1.1, 1.1}, new double[]{1.0, 1.0, 1.0});
 instance3 = new DenseInstance(new double[]{2.0, 2.0, 2.0}, new double[]{0.1, 0.4, 0.2});
 instance4 = new DenseInstance(new double[]{3.0, 3.0, 3.0}, new double[]{1.0, 1.0, 1.0});

 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 }

 // Test for constructor with Collection
 public void testDefaultDatasetWithCollection() {
 DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3, instance4));
 assertEquals(4, dataset.size());
 }

 // Test for empty constructor
 public void testEmptyDefaultDataset() {
 DefaultDataset dataset = new DefaultDataset();
 assertEquals(0, dataset.size());
 }

 // Test for addAll method
 public void testAddAll() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 assertEquals(4, dataset.size());
 }

 // Test for addAll with index
 public void testAddAllWithIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

 List<Instance> list = new ArrayList<>();
 list.add(instance2);
 list.add(instance3);
 dataset.addAll(1, list);

 assertEquals(5, dataset.size());
 }

 // Test for clear method
 public void testClear() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 // Test for add method
 public void testAdd() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);
 assertEquals(1, dataset.size());
 }

 // Test for add with index
 public void testAddWithIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 dataset.add(1, instance2);
 assertEquals(5, dataset.size());
 }

 // Test for addElement method
 public void testAddElement() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 dataset.addElement(instance2);
 assertEquals(5, dataset.size());
 }

 // Test for insertElementAt method
 public void testInsertElementAt() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 dataset.insertElementAt(instance3, 2);
 assertEquals(5, dataset.size());
 }

 // Test for setElementAt method
 public void testSetElementAt() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 dataset.setElementAt(instance4, 3);
 assertEquals(5, dataset.size());
 }

 // Test for get method
 public void testGet() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 assertEquals(instance3, dataset.getInstance(2));
 }

 // Test for classes method
 public void testClasses() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 SortedSet<Object> classes = dataset.classes();
 assertEquals(1, classes.size());
 assertEquals(instance1.classValue(), classes.first());
 }

 // Test for kNearest method
 public void testKNearest() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

 List<Instance> kNearestInstances = dataset.kNearest(2, instance2, distanceMeasure);
 assertEquals(2, kNearestInstances.size());
 }

 // Test for removeFarthest method
 public void testRemoveFarthest() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

 Map<Instance, Double> closestInstances = new HashMap<>();
 for (Instance instance : dataset) {
 closestInstances.put(instance, distanceMeasure.measure(instance2, instance));
 }
 double maxDistance = dataset.removeFarthest(closestInstances);
 assertEquals(Double.POSITIVE_INFINITY, maxDistance);
 assertEquals(3, dataset.size());
 }

 // Test for folds method
 public void testFolds() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 Dataset[] folds = dataset.folds(4, new Random());
 assertEquals(4, folds.length);
 }

 // Test for noAttributes method
 public void testNoAttributes() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 // Test for classIndex method
 public void testClassIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 assertEquals(instance1.classValue().getClass().getName(), dataset.classValue(instance1.getClassValueIndex()).getClass().getName());
 }

 // Test for classValue method
 public void testClassValue() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 assertEquals(instance1.classValue(), dataset.classValue(instance1.getClassValueIndex()));
 }

 // Test for copy method
 public void testCopy() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
 Dataset copiedDataset = dataset.copy();
 assertEquals(4, copiedDataset.size());
 }

 // Test for add method with null instance
 public void testAddNullInstance() {
 DefaultDataset dataset = new DefaultDataset();
 try {
 dataset.add(null);
 fail("Adding a null instance should throw an exception.");
 } catch (NullPointerException e) {
 // Pass
 }
 }

 // Test for addAll method with null collection
 public void testAddAllNullCollection() {
 DefaultDataset dataset = new DefaultDataset();
 try {
 dataset.addAll(null);
 fail("Adding a null collection should throw an exception.");
 } catch (NullPointerException e) {
 // Pass
 }
 }

 // Test for addAll method with null instance in collection
 public void testAddAllNullInstanceInCollection() {
 DefaultDataset dataset = new DefaultDataset();
 List<Instance> list = new ArrayList<>();
 list.add(instance1);
 list.add(null);
 try {
 dataset.addAll(list);
 fail("Adding a null instance to a collection and then adding the collection to the dataset should throw an exception.");
 } catch (NullPointerException e) {
 // Pass
 }
 }

 // Test for remove method with null index
 public void testRemoveNullIndex() {
 DefaultDataset dataset = new DefaultDataset();
 try {
 dataset.remove(null);
 fail("Removing with a null index should throw an exception.");
 } catch (NullPointerException e) {
 // Pass
 }
 }

 // Test for remove method with out-of-bounds index
 public void testRemoveOutOfBoundsIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);
 try {
 dataset.remove(0);
 fail("Removing with an out-of-bounds index should throw an exception.");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 // Test for get method with out-of-bounds index
 public void testGetOutOfBoundsIndex() {
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance1);
 try {
 dataset.getInstance(0);
 fail("Getting with an out-of-bounds index should throw an exception.");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 // Test for classes method with empty dataset
 public void testClassesEmptyDataset() {
 DefaultDataset dataset = new DefaultDataset();
 SortedSet<Object> classes = dataset.classes();
 assertEquals(0, classes.size());
 }

 // Test for kNearest method with empty dataset
 public void testKNearestEmptyDataset() {
 DefaultDataset dataset = new DefaultDataset();
 Set<Instance> kNearestInstances = dataset.kNearest(2, instance2, distanceMeasure);
 assertEquals(0, kNearestInstances.size());
 }

 // Test for folds method with zero numFolds
 public void testFoldsZeroNumFolds() {
 DefaultDataset dataset = new DefaultDataset();
 try {
 dataset.folds(0, new Random());
 fail("Folding with zero numFolds should throw an exception.");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 // Test for copy method with empty dataset
 public void testCopyEmptyDataset() {
 DefaultDataset dataset = new DefaultDataset();
 Dataset copiedDataset = dataset.copy();
 assertEquals(0, copiedDataset.size());
 }
}