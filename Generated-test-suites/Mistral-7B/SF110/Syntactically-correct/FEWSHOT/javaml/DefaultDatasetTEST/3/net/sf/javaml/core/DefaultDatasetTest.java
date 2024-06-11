package net.sf.javaml.core;

import java.util.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DefaultDatasetTest {

 private DefaultDataset ds;
 private Collection<Instance> coll;

 @Parameters
 public static Collection<Object[]> data() {
 Collection<Object[]> data = new ArrayList<>();
 data.add(new Object[] { new ArrayList<Instance>() });
 data.add(new Object[] { Arrays.asList(new DenseInstance(new double[]{1.0, 2.0}, new double[]{3.0})) });
 data.add(new Object[] { Arrays.asList(new DenseInstance(new double[]{1.0, 2.0}, new double[]{3.0, 4.0}), new DenseInstance(new double[]{1.0, 2.0}, new double[]{3.0})) });
 return data;
 }

 public DefaultDatasetTest(Collection<Instance> coll) {
 this.coll = coll;
 ds = new DefaultDataset(coll);
 }

 @Test
 public void testDefaultDataset() {
 assertEquals(0, ds.maxAttributes());
 assertNotNull(ds.classes());
 assertTrue(ds.isEmpty());
 }

 @Test
 public void testAddAll() {
 ds.clear();
 ds.addAll(coll);
 assertEquals(ds.size(), coll.size());
 }

 @Test
 public void testAddInstance() {
 Instance newInst = new DenseInstance(new double[]{1.0, 2.0}, new double[]{3.0});
 ds.add(newInst);
 assertEquals(ds.size(), coll.size() + 1);
 assertEquals(newInst, ds.instance(ds.size() - 1));
 }

 @Test
 public void testRemoveInstance() {
 ds.remove(ds.instance(0));
 assertEquals(ds.size(), coll.size());
 assertNotSame(ds.instance(0), ds.instance(0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 ds.set(0, new Instance() {});
 }

 @Test
 public void testClasses() {
 assertEquals(ds.classes().size(), coll.size());
 }

 @Test
 public void testClassIndex() {
 Instance inst = coll.iterator().next();
 assertEquals(ds.classIndex(inst.classValue()), 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClassIndexNull() {
 ds.classIndex(null);
 }

 @Test
 public void testSize() {
 assertEquals(ds.size(), coll.size());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(ds.isEmpty());
 ds.clear();
 assertTrue(ds.isEmpty());
 }

 @Test
 public void testGet() {
 Instance inst = coll.iterator().next();
 assertEquals(inst, ds.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 ds.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBoundsIndex() {
 ds.addAll(coll);
 ds.get(ds.size());
 }

 @Test
 public void testSetSize() {
 ds.setSize(ds.size() + 1);
 assertEquals(ds.size(), coll.size() + 1);
 ds.setSize(ds.size() - 1);
 assertEquals(ds.size(), coll.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetSizeBelowZero() {
 ds.setSize(-1);
 }

 @Test
 public void testSubList() {
 int start = 0;
 int end = ds.size();
 List<Instance> subList = ds.subList(start, end);
 assertEquals(subList.size(), ds.size() - start);
 }

 @Test
 public void testSubListNegativeIndex() {
 int start = -1;
 int end = ds.size();
 try {
 ds.subList(start, end);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testSubListOutOfBoundsIndex() {
 int start = ds.size();
 int end = ds.size();
 try {
 ds.subList(start, end);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testKNearest() {
 Instance queryInst = new DenseInstance(new double[]{1.0, 2.0}, new double[]{3.0});
 DistanceMeasure dm = new EuclideanDistance();
 SortedSet<Instance> nearestInstances = ds.kNearest(3, queryInst, dm);
 assertEquals(3, nearestInstances.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestNullQueryInstance() {
 ds.kNearest(3, null, dm);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestNullDistanceMeasure() {
 ds.kNearest(3, queryInst, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestInvalidK() {
 ds.kNearest(-1, queryInst, dm);
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 Random rg = new Random();
 Dataset[] folds = ds.folds(numFolds, rg);
 assertEquals(numFolds, folds.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsInvalidNumFolds() {
 ds.folds(-1, rg);
 }

 @Test
 public void testFoldsEmptyDataSet() {
 ds.clear();
 int numFolds = 3;
 Random rg = new Random();
 Dataset[] folds = ds.folds(numFolds, rg);
 assertEquals(numFolds, folds.length);
 for (Dataset fold : folds) {
 assertTrue(fold instanceof Fold);
 assertTrue(fold.isEmpty());
 }
 }

 @Test
 public void testFoldsRandomFolds() {
 int numFolds = 3;
 Random rg = new Random();
 Dataset[] folds = ds.folds(numFolds, rg);
 for (int i = 0; i < numFolds; i++) {
 Dataset fold = folds[i];
 int size = fold.size();
 assertEquals(ds.size() / numFolds, size);
 }
 }

 @Test
 public void testFoldsEmptyFold() {
 int numFolds = 3;
 Random rg = new Random();
 Dataset[] folds = ds.folds(numFolds, rg);
 for (int i = 0; i < numFolds; i++) {
 Dataset fold = folds[i];
 assertTrue(fold instanceof Fold);
 int size = fold.size();
 if (size == -1) {
 fail();
 }
 for (int j = 0; j < size; j++) {
 Instance instance = fold.instance(j);
 assertEquals(instance, ds.instance(rg.nextInt(ds.size())));
 }
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(ds.noAttributes(), coll.iterator().next().noAttributes());
 }

 @Test
 public void testCopy() {
 DefaultDataset copy = ds.copy();
 assertEquals(ds.size(), copy.size());
 for (int i = 0; i < ds.size(); i++) {
 assertEquals(ds.instance(i), copy.instance(i));
 }
 }
}