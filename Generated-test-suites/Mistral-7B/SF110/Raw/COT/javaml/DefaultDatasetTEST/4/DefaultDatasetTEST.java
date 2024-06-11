package net.sf.javaml.core.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.EuclideanDistance;
import java.util.*;

public class DefaultDatasetTest extends TestCase {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 dataset = new DefaultDataset();
 distanceMeasure = new EuclideanDistance();

 instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Object[]{1.0});
 instance2 = new DenseInstance(new double[]{2.0, 3.0}, new Object[]{2.0});
 instance3 = new DenseInstance(new double[]{3.0, 4.0}, new Object[]{3.0});

 dataset.add(instance1);
 dataset.add(instance2);
 dataset.add(instance3);
 }

 // Testing constructor with a collection
 public void testDefaultDatasetWithCollection() {
 DefaultDataset defaultDataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
 assertEquals(3, defaultDataset.size());
 }

 // Testing default constructor
 public void testDefaultDataset() {
 assertEquals(0, dataset.size());
 }

 // Testing addAll method
 public void testAddAll() {
 Collection<Instance> coll = new ArrayList<>();
 coll.add(instance1);
 coll.add(instance2);
 coll.add(instance3);
 dataset.addAll(coll);
 assertEquals(3, dataset.size());
 }

 // Testing addAll method with index
 public void testAddAllWithIndex() {
 Collection<Instance> coll = new ArrayList<>();
 coll.add(instance1);
 coll.add(instance2);
 coll.add(instance3);
 dataset.addAll(1, coll);
 assertEquals(3, dataset.size());
 }

 // Testing clear method
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 // Testing add method
 public void testAdd() {
 Instance newInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{4.0});
 dataset.add(newInstance);
 assertEquals(4, dataset.size());
 }

 // Testing add method with index
 public void testAddWithIndex() {
 Instance newInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{4.0});
 dataset.add(1, newInstance);
 assertEquals(4, dataset.size());
 }

 // Testing addElement method
 public void testAddElement() {
 Instance newInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{4.0});
 dataset.addElement(newInstance);
 assertEquals(4, dataset.size());
 }

 // Testing insertElementAt method
 public void testInsertElementAt() {
 Instance newInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{4.0});
 dataset.insertElementAt(newInstance, 1);
 assertEquals(4, dataset.size());
 }

 // Testing setElementAt method
 public void testSetElementAt() {
 Instance newInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{4.0});
 dataset.setElementAt(newInstance, 1);
 assertEquals(4, dataset.size());
 }

 // Testing instance method
 public void testInstance() {
 assertEquals(instance1, dataset.instance(0));
 }

 // Testing classes method
 public void testClasses() {
 Set<Object> classes = dataset.classes();
 assertEquals(3, classes.size());
 }

 // Testing kNearest method
 public void testKNearest() {
 Set<Instance> expected = new HashSet<>();
 expected.add(instance1);
 expected.add(instance2);
 Set<Instance> result = dataset.kNearest(2, instance3, distanceMeasure);
 assertEquals(expected, result);
 }

 // Testing removeFarthest method (private method)
 // You cannot test a private method directly in JUnit,
 // but you can test the method using the public API.
 // Replace this test method with tests on the public methods
 // that use removeFarthest internally.
 // public void testRemoveFarthest() {
 // fail("Not yet implemented");
 // }

 // Testing folds method
 public void testFolds() {
 int numFolds = 3;
 Dataset[] expected = new Dataset[numFolds];
 for (int i = 0; i < numFolds; i++) {
 expected[i] = new DefaultDataset();
 expected[i].addAll(Arrays.asList(instance1, instance2, instance3));
 }
 Dataset[] result = dataset.folds(numFolds, new Random());
 assertArrayEquals(expected, result);
 }

 // Testing noAttributes method
 public void testNoAttributes() {
 assertEquals(2, instance1.noAttributes());
 }

 // Testing classIndex method
 public void testClassIndex() {
 assertEquals(0, dataset.classIndex(instance1.classValue()));
 }

 // Testing classValue method
 public void testClassValue() {
 assertEquals(instance1.classValue(), dataset.classValue(0));
 }

 // Testing copy method
 public void testCopy() {
 DefaultDataset expected = new DefaultDataset();
 expected.addAll(Arrays.asList(instance1, instance2, instance3));
 DefaultDataset result = (DefaultDataset) dataset.copy();
 assertEquals(expected, result);
 }

 // Testing exception handling for addAll method with null collection
 public void testAddAllWithNullCollection() {
 try {
 dataset.addAll(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 // Testing exception handling for add method with null instance
 public void testAddWithNullInstance() {
 try {
 dataset.add(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 // Testing exception handling for addAll method with null instance
 public void testAddAllWithNullInstance() {
 Collection<Instance> coll = new ArrayList<>();
 coll.add(instance1);
 try {
 dataset.addAll(coll);
 dataset.add(null);
 fail("Expected ConcurrentModificationException");
 } catch (ConcurrentModificationException e) {
 // Expected exception
 }
 }

 // Testing exception handling for instance method with out-of-bounds index
 public void testInstanceWithOutOfBoundsIndex() {
 try {
 dataset.instance(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected exception
 }
 }

 // Testing exception handling for classes method with no instances
 public void testClassesWithNoInstances() {
 dataset.clear();
 try {
 dataset.classes();
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 }

 // Testing exception handling for kNearest method with negative k
 public void testKNearestWithNegativeK() {
 try {
 dataset.kNearest(-1, instance3, distanceMeasure);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 // Testing exception handling for kNearest method with null distance measure
 public void testKNearestWithNullDistanceMeasure() {
 try {
 dataset.kNearest(2, instance3, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 // Testing exception handling for folds method with negative numFolds
 public void testFoldsWithNegativeNumFolds() {
 try {
 dataset.folds(-1, new Random());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 // Testing exception handling for folds method with null random number generator
 public void testFoldsWithNullRandomNumberGenerator() {
 try {
 dataset.folds(3, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }
}