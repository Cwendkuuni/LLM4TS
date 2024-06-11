package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private DefaultDataset emptyDataset;
 private Instance instance1;
 private Instance instance2;
 private Instance instance3;
 private Instance instance4;
 private Collection<Instance> instances;
 private Set<Object> classes;

 @Before
 public void setUp() {
 instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, new double[] { 4.0 });
 instance2 = new DenseInstance(new double[] { 2.0, 2.0, 3.0 }, new double[] { 5.0 });
 instance3 = new DenseInstance(new double[] { 3.0, 2.0, 3.0 }, new double[] { 6.0 });
 instance4 = new DenseInstance(new double[] { 3.0, 2.0, 3.0 }, new double[] { 7.0 });
 instances = Arrays.asList(instance1, instance2, instance3, instance4);
 classes = new TreeSet<Object>();
 classes.add("Class1");
 classes.add("Class2");
 dataset = new DefaultDataset(instances);
 emptyDataset = new DefaultDataset();
 }

 @Test
 public void testDefaultConstructor() {
 assertSame(DefaultDataset.class, emptyDataset.getClass());
 assertEquals(0, emptyDataset.size());
 assertEquals(0, emptyDataset.noAttributes());
 assertNotNull(emptyDataset.classes());
 assertTrue(emptyDataset.classes().isEmpty());
 }

 @Test
 public void testCollectionConstructor() {
 assertSame(DefaultDataset.class, dataset.getClass());
 assertEquals(4, dataset.size());
 assertEquals(3, dataset.noAttributes());
 assertSame(classes, dataset.classes());
 }

 @Test
 public void testAddAll() {
 final Collection<Instance> instancesToAdd = Arrays.asList(instance1, instance2, instance3, instance4);
 dataset.addAll(instancesToAdd);
 assertEquals(8, dataset.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithUnmodifiableCollection() {
 final Collection<Instance> instancesToAdd = Collections.unmodifiableList(Arrays.asList(instance1, instance2, instance3, instance4));
 dataset.addAll(instancesToAdd);
 }

 @Test
 public void testAdd() {
 dataset.add(instance4);
 assertEquals(5, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 dataset.add(null);
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testCheck() {
 final Instance instance5 = new DenseInstance(new double[] { 3.0, 2.0, 3.0 }, new double[] { 8.0 });
 final Collection<Instance> instancesToCheck = Arrays.asList(instance1, instance2, instance3, instance4, instance5);
 dataset.check(instancesToCheck);
 assertEquals(5, dataset.size());
 assertEquals(3, dataset.noAttributes());
 assertSame(classes, dataset.classes());
 }

 @Test
 public void testKNearestWithValidParameters() {
 final int k = 2;
 final Instance instanceToFindNearestTo = instance1;
 final DistanceMeasure distanceMeasure = new EuclideanDistance();
 final Set<Instance> nearestInstances = dataset.kNearest(k, instanceToFindNearestTo, distanceMeasure);
 assertEquals(2, nearestInstances.size());
 assertSame(instance2, nearestInstances.iterator().next());
 assertSame(instance3, nearestInstances.iterator().next());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestWithNullInstance() {
 final int k = 2;
 final Instance instanceToFindNearestTo = null;
 final DistanceMeasure distanceMeasure = new EuclideanDistance();
 dataset.kNearest(k, instanceToFindNearestTo, distanceMeasure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestWithNegativeK() {
 final int k = -1;
 final Instance instanceToFindNearestTo = instance1;
 final DistanceMeasure distanceMeasure = new EuclideanDistance();
 dataset.kNearest(k, instanceToFindNearestTo, distanceMeasure);
 }

 @Test(expected = NullPointerException.class)
 public void testKNearestWithNullDistanceMeasure() {
 final int k = 2;
 final Instance instanceToFindNearestTo = instance1;
 final DistanceMeasure distanceMeasure = null;
 dataset.kNearest(k, instanceToFindNearestTo, distanceMeasure);
 }

 @Test
 public void testFoldsWithValidParameters() {
 final int numFolds = 2;
 final Random random = new Random();
 final Dataset[] expectedFoldSets = new Dataset[] {
 new Fold(dataset, new int[] { 0, 2, 3, 1 }),
 new Fold(dataset, new int[] { 1, 3, 0, 2 })
 };
 final Dataset[] actualFoldSets = dataset.folds(numFolds, random);
 assertEquals(numFolds, actualFoldSets.length);
 for (int i = 0; i < numFolds; ++i) {
 assertSame(Fold.class, actualFoldSets[i].getClass());
 assertSame(dataset, actualFoldSets[i].getBase());
 assertSame(expectedFoldSets[i].getBase(), actualFoldSets[i].getBase());
 assertArrayEquals(expectedFoldSets[i].getFoldIndices(), actualFoldSets[i].getFoldIndices());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsWithNegativeNumFolds() {
 final int numFolds = -1;
 final Random random = new Random();
 dataset.folds(numFolds, random);
 }

 @Test(expected = NullPointerException.class)
 public void testFoldsWithNullRandom() {
 final int numFolds = 2;
 dataset.folds(numFolds, null);
 }
}