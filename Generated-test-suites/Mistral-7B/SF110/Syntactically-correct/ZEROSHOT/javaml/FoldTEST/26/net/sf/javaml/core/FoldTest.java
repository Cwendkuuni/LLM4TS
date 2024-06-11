package net.sf.javaml.core;

import static org.junit.Assert.*;
import net.sf.javaml.distance.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class FoldTest {

 private Dataset parentDataSet;
 private Fold fold;

 @Before
 public void setUp() {
 parentDataSet = new DefaultDataset();
 parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{1.0}));
 parentDataSet.add(new DenseInstance(new double[]{2.0, 2.0, 3.0}, new double[]{2.0}));
 parentDataSet.add(new DenseInstance(new double[]{3.0, 2.0, 3.0}, new double[]{3.0}));

 int[] indices = {1, 2};
 fold = new Fold(parentDataSet, indices);
 }

 @Test
 public void testSize() {
 assertEquals("Size of fold should be equal to the number of indices", indices.length, fold.size());
 }

 @Test
 public void testClassIndex() {
 Object expectedClass = parentDataSet.classValue(1);
 assertEquals("Class index should be equal to the class index of the instance in the parent dataset", parentDataSet.classIndex(expectedClass), fold.classIndex(expectedClass));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(new DenseInstance(new double[]{4.0, 2.0, 3.0}, new double[]{4.0}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 List<Instance> instancesToAdd = new ArrayList<>();
 instancesToAdd.add(new DenseInstance(new double[]{4.0, 2.0, 3.0}, new double[]{4.0}));
 fold.addAll(instancesToAdd);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 List<Instance> instancesToRetain = new ArrayList<>();
 fold.retainAll(instancesToRetain);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 Instance instanceToSet = new DenseInstance(new double[]{4.0, 2.0, 3.0}, new double[]{4.0});
 fold.set(0, instanceToSet);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllIndex() {
 List<Instance> instancesToAdd = new ArrayList<>();
 instancesToAdd.add(new DenseInstance(new double[]{4.0, 2.0, 3.0}, new double[]{4.0}));
 fold.addAll(1, instancesToAdd);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllIndex() {
 List<Instance> instancesToRemove = new ArrayList<>();
 instancesToRemove.add(fold.instance(0));
 fold.removeAll(instancesToRemove);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllCollection() {
 Collection<Instance> instancesToAdd = new ArrayList<>();
 instancesToAdd.add(new DenseInstance(new double[]{4.0, 2.0, 3.0}, new double[]{4.0}));
 fold.addAll(instancesToAdd);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllCollection() {
 Collection<Instance> instancesToRemove = new ArrayList<>();
 instancesToRemove.add(fold.instance(0));
 fold.removeAll(instancesToRemove);
 }

 @Test
 public void testInstance() {
 Instance expectedInstance = parentDataSet.instance(indices[0]);
 assertEquals("The fold instance should be equal to the instance in the parent dataset", expectedInstance, fold.instance(0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 fold.folds(2, new Random());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 KNearest neighborFind = new KNearestTree();
 Set<Instance> expectedNearest = new TreeSet<>();
 expectedNearest.add(parentDataSet.instance(1));
 expectedNearest.add(parentDataSet.instance(2));
 fold.kNearest(2, fold.instance(0), neighborFind);
 }
}