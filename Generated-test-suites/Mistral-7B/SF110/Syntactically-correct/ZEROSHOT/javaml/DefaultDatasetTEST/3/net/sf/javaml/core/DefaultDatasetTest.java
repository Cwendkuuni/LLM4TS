package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import net.sf.javaml.distance.*;
import static net.sf.javaml.distance.DistanceMeasure.*;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private DefaultDataset emptyDataset;
 private Instance instance1, instance2, instance3;
 private Instance instance4;
 private Collection<Instance> instancesCollection;
 private Set<Object> classesSet;
 private SortedSet<Object> classesSortedSet;
 private Dataset[] foldedDatasets;

 @Before
 public void setUp() {
 instance1 = new DenseInstance(new double[]{1.1, 2.2, 3.3}, new Object[]{1.0});
 instance2 = new DenseInstance(new double[]{2.1, 2.2, 3.3}, new Object[]{0.0});
 instance3 = new DenseInstance(new double[]{3.1, 2.2, 3.3}, new Object[]{1.0});
 instance4 = new DenseInstance(new double[]{4.1, 2.2, 3.3}, null);
 instancesCollection = new ArrayList<>();
 instancesCollection.add(instance1);
 instancesCollection.add(instance2);
 instancesCollection.add(instance3);
 classesSet = new TreeSet<>();
 classesSortedSet = new TreeSet<>();
 dataset = new DefaultDataset(instancesCollection);
 emptyDataset = new DefaultDataset();
 }

 @Test
 public void testConstructorWithCollection() {
 assertEquals(3, dataset.size());
 assertEquals(3, dataset.maxAttributes());
 assertEquals(1, dataset.classes().size());
 assertEquals(1.0, dataset.classValue(0));
 }

 @Test
 public void testConstructorEmpty() {
 assertEquals(0, emptyDataset.size());
 assertEquals(0, emptyDataset.maxAttributes());
 assertEquals(0, emptyDataset.classes().size());
 }

 @Test
 public void testAddAll() {
 instancesCollection.add(instance4);
 dataset.addAll(instancesCollection);
 assertEquals(4, dataset.size());
 assertEquals(3, dataset.maxAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithEmptyCollection() {
 dataset.addAll(Collections.emptyList());
 }

 @Test
 public void testAddInstance() {
 dataset.add(instance4);
 assertEquals(5, dataset.size());
 assertEquals(3, dataset.maxAttributes());
 assertNotNull(dataset.classValue(4));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInstanceWithNullInstance() {
 dataset.add(null);
 }

 @Test
 public void testAddAllAtIndex() {
 instancesCollection.clear();
 instancesCollection.add(instance3);
 instancesCollection.add(instance1);
 instancesCollection.add(instance2);
 dataset.addAll(0, instancesCollection);
 assertEquals(3, dataset.size());
 assertEquals(instance3, dataset.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddAllAtIndexWithNegativeIndex() {
 dataset.addAll(-1, instancesCollection);
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testCheck() {
 instancesCollection.clear();
 instancesCollection.add(instance1);
 DefaultDataset testDataset = new DefaultDataset();
 testDataset.check(instancesCollection);
 assertEquals(1, testDataset.size());
 assertEquals(3, testDataset.maxAttributes());
 }

 @Test
 public void testClasses() {
 assertEquals(1, dataset.classes().size());
 assertEquals(1.0, dataset.classes().headSet(new Object[]{1.0}).first());
 }

 @Test
 public void testKNearest() {
 Instance queryInstance = new DenseInstance(new double[]{1.5, 2.5, 3.5});
 DistanceMeasure euclideanDistance = new EuclideanDistance();
 Set<Instance> kNearestInstances = dataset.kNearest(2, queryInstance, euclideanDistance);
 assertEquals(2, kNearestInstances.size());
 assertTrue(kNearestInstances.contains(instance1));
 assertTrue(kNearestInstances.contains(instance2));
 }

 @Test
 public void testRemoveFarthest() {
 Map<Instance, Double> closestInstances = new HashMap<>();
 closestInstances.put(instance1, 1.0);
 closestInstances.put(instance2, 2.0);
 closestInstances.put(instance3, 3.0);
 double maxDistance = dataset.removeFarthest(closestInstances);
 assertEquals(instance1.classValue(), dataset.classes().headSet(instance3.classValue()).first());
 assertEquals(instance3, closestInstances.remove(instance3));
 assertEquals(2.0, maxDistance);
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 Random random = new Random();
 Dataset[] foldedDatasets = dataset.folds(numFolds, random);
 assertEquals(numFolds, foldedDatasets.length);
 for (Dataset datasetFold : foldedDatasets) {
 assertEquals(dataset.size(), datasetFold.size());
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(1, dataset.classIndex(instance1.classValue()));
 }

 @Test(expected = NullPointerException.class)
 public void testClassIndexWithNullClass() {
 dataset.classIndex(null);
 }

 @Test
 public void testClassValue() {
 assertEquals(1.0, dataset.classValue(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueWithInvalidIndex() {
 dataset.classValue(5);
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = dataset.copy();
 assertEquals(dataset.size(), copiedDataset.size());
 }
}