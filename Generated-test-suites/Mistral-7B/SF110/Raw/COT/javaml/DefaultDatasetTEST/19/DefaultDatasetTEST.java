package net.sf.javaml.core.test;

import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance;
 private DistanceMeasure distanceMeasure;

 @Before
 public void setUp() {
 dataset = new DefaultDataset();
 instance = new DummyInstance("testInstance", new Double[]{1.0, 2.0, 3.0});
 distanceMeasure = new EuclideanDistance();
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset defaultDataset = new DefaultDataset();
 Assert.assertEquals(0, defaultDataset.size());
 }

 @Test
 public void testAddAll() {
 Collection<Instance> coll = new ArrayList<>();
 coll.add(instance);
 dataset.addAll(coll);
 Assert.assertEquals(1, dataset.size());
 }

 @Test
 public void testAddAllWithCheck() {
 Collection<Instance> coll = new ArrayList<>();
 coll.add(instance);
 dataset.addAll(coll);
 Assert.assertEquals(1, dataset.size());
 verifyAllInstancesAdded(coll, dataset);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithUnmodifiableCollection() {
 Collection<Instance> coll = Collections.unmodifiableList(Arrays.asList(instance));
 try {
 dataset.addAll(coll);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testAdd() {
 dataset.add(instance);
 Assert.assertEquals(1, dataset.size());
 }

 @Test
 public void testAddWithCheck() {
 dataset.add(instance);
 Assert.assertEquals(1, dataset.size());
 verifyInstanceAdded(instance, dataset);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 dataset.add(null);
 }

 @Test
 public void testClasses() {
 dataset.add(instance);
 Assert.assertEquals(1, dataset.classes().size());
 }

 @Test
 public void testClear() {
 dataset.add(instance);
 dataset.clear();
 Assert.assertEquals(0, dataset.size());
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 Random rg = new Random();
 Dataset[] result = dataset.folds(numFolds, rg);
 Assert.assertArrayEquals(numFolds, result.length);
 for (Dataset ds : result) {
 Assert.assertNotNull(ds);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsWithIllegalNumberOfFolds() {
 int numFolds = 0;
 Random rg = new Random();
 try {
 dataset.folds(numFolds, rg);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testFoldsWithNullRandomGenerator() {
 int numFolds = 3;
 try {
 dataset.folds(numFolds, null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testGet() {
 dataset.add(instance);
 Assert.assertEquals(instance, dataset.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithInvalidIndex() {
 try {
 dataset.get(-1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testInstance() {
 dataset.add(instance);
 Assert.assertEquals(instance, dataset.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceWithInvalidIndex() {
 try {
 dataset.instance(1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testKNearest() {
 Set<Instance> result = dataset.kNearest(2, instance, distanceMeasure);
 Assert.assertEquals(2, result.size());
 }

 @Test
 public void testKNearestWithInvalidK() {
 Set<Instance> result = dataset.kNearest(-1, instance, distanceMeasure);
 Assert.assertEquals(0, result.size());
 }

 @Test(expected = NullPointerException.class)
 public void testKNearestWithNullDistanceMeasure() {
 Set<Instance> result = dataset.kNearest(2, instance, null);
 }

 @Test
 public void testNoAttributes() {
 dataset.add(instance);
 Assert.assertEquals(instance.noAttributes(), dataset.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 dataset.add(instance);
 try {
 dataset.remove(instance);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveWithInvalidIndex() {
 try {
 dataset.remove(1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testSize() {
 dataset.add(instance);
 Assert.assertEquals(1, dataset.size());
 }

 @Test
 public void testToArray() {
 Instance[] result = dataset.toArray(new Instance[0]);
 Assert.assertEquals(1, result.length);
 Assert.assertEquals(instance, result[0]);
 }

 @Test
 public void testToArrayWithCustomSize() {
 Instance[] result = dataset.toArray(new Instance[2]);
 Assert.assertEquals(0, result.length);
 }

 private void verifyAllInstancesAdded(Collection<Instance> coll, DefaultDataset dataset) {
 for (Instance i : coll) {
 verifyInstanceAdded(i, dataset);
 }
 }

 private void verifyInstanceAdded(Instance instance, DefaultDataset dataset) {
 assertEquals(instance.getClass(), dataset.getClass(instance.classValue()));
 assertEquals(instance.noAttributes(), dataset.noAttributes());
 assertFalse(dataset.contains(null));
 }
}