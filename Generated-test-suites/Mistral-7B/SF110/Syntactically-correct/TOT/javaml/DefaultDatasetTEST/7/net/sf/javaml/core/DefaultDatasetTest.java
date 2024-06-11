package net.sf.javaml.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.instance.Instance;
import net.sf.javaml.instance.InstanceFactory;
import net.sf.javaml.instance.vector.ArrayInstance;
import net.sf.javaml.instance.vector.VectorInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DefaultDatasetTest extends TestCase {

 private DefaultDataset dataset;
 private DefaultDataset emptyDataset;

 @Before
 public void setUp() {
     InstanceFactory instanceFactory = new InstanceFactory();
     Instance instance1 = instanceFactory.createInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.1, 0.2, 0.3});
     Instance instance2 = instanceFactory.createInstance(new double[]{4.0, 5.0, 6.0}, new double[]{1.0, 1.0, 1.0});
     Instance instance3 = instanceFactory.createInstance(new double[]{7.0, 8.0, 9.0}, new double[]{0.1, 0.2, 0.4});

     List<Instance> instances = new ArrayList<>(Arrays.asList(instance1, instance2, instance3));
     dataset = new DefaultDataset(instances);

     emptyDataset = new DefaultDataset();
 }

 @Test
 public void testAddAll() {
     List<Instance> instancesToAdd = new ArrayList<>(Arrays.asList(instance1, instance2, instance3));
     DefaultDataset expectedDataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));

     dataset.addAll(instancesToAdd);

     assertDataSetEquals(expectedDataset, dataset);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNullCollection() {
     dataset.addAll(null);
 }

 @Test
 public void testAdd() {
     Instance newInstance = instanceFactory.createInstance(new double[]{10.0, 11.0, 12.0}, new double[]{2.0, 2.0, 2.0});

     dataset.add(newInstance);

     assertDataSetSize(4, dataset);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
     dataset.add(null);
 }

 @Test
 public void testClear() {
     dataset.clear();

     assertDataSetSize(0, dataset);
 }

 @Test
 public void testSize() {
     assertDataSetSize(3, dataset);
 }

 @Test
 public void testGet() {
     Instance expectedInstance = instance1;

     Instance actualInstance = dataset.get(0);

     assertInstanceEquals(expectedInstance, actualInstance);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
     dataset.get(-1);
 }

 @Test
 public void testClasses() {
     Set<Object> expectedClasses = new LinkedHashSet<>(Arrays.asList(0.1, 1.0));

     Set<Object> actualClasses = dataset.classes();

     assertDataSetClassesEquals(expectedClasses, actualClasses);
 }

 @Test
 public void testkNearest() {
     Instance testInstance = instanceFactory.createInstance(new double[]{5.5, 5.5, 5.5}, new double[]{1.0, 1.0, 1.0});
     double distanceThreshold = 3.0;

     List<Instance> expectedNeighbors = Arrays.asList(instance1, instance2);

     List<Instance> actualNeighbors = dataset.kNearest(2, testInstance, new EuclideanDistance());

     assertDataSetEquals(expectedNeighbors, actualNeighbors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testkNearestNullInstance() {
     dataset.kNearest(2, null, new EuclideanDistance());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testkNearestIllegalArgument() {
     dataset.kNearest(-1, instance1, new EuclideanDistance());
 }

 @Test
 public void testFolds() {
     int numFolds = 3;
     Random rg = new Random();

     DefaultDataset[] expectedFolds = new DefaultDataset[numFolds];

     for (int i = 0; i < numFolds; i++) {
         List<Instance> instancesInFold = new ArrayList<>();

         for (Instance instance : dataset) {
             int index = rg.nextInt(dataset.size());

             if (Collections.binarySearch(Arrays.asList(i, i + 1, i + 2), index) >= 0) {
                 instancesInFold.add(instance);
             }
         }

         expectedFolds[i] = new DefaultDataset(instancesInFold);
     }

     DefaultDataset[] actualFolds = dataset.folds(numFolds, rg);

     for (int i = 0; i < numFolds; i++) {
         assertDataSetEquals(expectedFolds[i], actualFolds[i]);
     }
 }

 @Test
 public void testNoAttributes() {
     assertDataSetNoAttributesEquals(3, dataset);
 }

 @Test
 public void testClassIndex() {
     assertDataSetClassIndexEquals(0, dataset, 0.1);
     assertDataSetClassIndexEquals(1, dataset, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClassIndexNullObject() {
     dataset.classIndex(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassIndexOutOfBounds() {
     dataset.classIndex(3);
 }

 @Test
 public void testClassValue() {
     assertDataSetClassValueEquals(0, dataset, instance1, 0.1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueOutOfBounds() {
     dataset.classValue(3);
 }

 @Test
 public void testCopy() {
     DefaultDataset expectedCopy = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));

     DefaultDataset actualCopy = dataset.copy();

     assertDataSetEquals(expectedCopy, actualCopy);
 }

 private void assertDataSetSize(int expectedSize, DefaultDataset dataset) {
     int actualSize = dataset.size();

     assertEquals("Unexpected dataset size: expected=" + expectedSize + ", actual=" + actualSize, expectedSize, actualSize);
 }

 private void assertDataSetClassesEquals(Set<Object> expectedClasses, Set<Object> actualClasses) {
     assertEquals("Unexpected dataset classes: expected=" + expectedClasses + ", actual=" + actualClasses, expectedClasses, actualClasses);
 }

 private void assertDataSetEquals(DefaultDataset expectedDataset, DefaultDataset actualDataset) {
     assertEquals("Unexpected datasets: expected=" + expectedDataset + ", actual=" + actualDataset, expectedDataset, actualDataset);
 }

 private void assertInstanceEquals(Instance expectedInstance, Instance actualInstance) {
     assertEquals("Unexpected instances: expected=" + expectedInstance + ", actual=" + actualInstance, expectedInstance, actualInstance);
 }

 private void assertDataSetNoAttributesEquals(int expectedNumberOfAttributes, DefaultDataset dataset) {
     int actualNumberOfAttributes = dataset.noAttributes();

     assertEquals("Unexpected number of attributes: expected=" + expectedNumberOfAttributes + ", actual=" + actualNumberOfAttributes, expectedNumberOfAttributes, actualNumberOfAttributes);
 }

 private void assertDataSetClassIndexEquals(int index, DefaultDataset dataset, Object expectedClass) {
     Object actualClass = dataset.classValue(index);

     assertEquals("Unexpected class at index " + index + ": expected=" + expectedClass + ", actual=" + actualClass, expectedClass, actualClass);
 }

 private void assertDataSetClassValueEquals(int index, DefaultDataset dataset, Instance expectedInstance, Object expectedClass) {
     Object actualClass = dataset.classValue(index);
     Instance actualInstance = dataset.instance(index);

     assertEquals("Unexpected class or instance at index " + index + ": expected class=" + expectedClass + ", actual class=" + actualClass, expectedClass, actualClass);
     assertEquals("Unexpected instance at index " + index + ": expected=" + expectedInstance + ", actual=" + actualInstance, expectedInstance, actualInstance);
 }
}