package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.distance.*;
import net.sf.javaml.instances.InstanceFactory;
import net.sf.javaml.instances.InstanceList;
import net.sf.javaml.instances.InstanceLoader;
import net.sf.javaml.instances.InstanceMemory;
import net.sf.javaml.util.SimpleDataset;

public class FoldTest {

 private Fold fold;
 private Dataset parentDataSet;

 @Before
 public void setUp() throws Exception {
 // Create a dataset with 10 instances
 InstanceLoader irisLoader = new InstanceLoader();
 InstanceMemory memory = new InstanceMemory();
 memory.setCapacity(10);
 InstanceList instances = (InstanceList) irisLoader.load("iris.arff", memory);
 parentDataSet = new SimpleDataset("MyDatasetName", instances.toVector(), instances.classAttribute().isNominal());

 // Create a fold with indices 0, 2, 4, 6, 8
 int[] foldIndices = {0, 2, 4, 6, 8};
 fold = new Fold(parentDataSet, foldIndices);
 }

 @Test
 public void testSize() {
 assertEquals(parentDataSet.size(), fold.size());
 }

 @Test
 public void testGetInstance() {
 for (int i = 0; i < fold.size(); i++) {
 Instance instance = fold.instance(i);
 assertEquals(parentDataSet.instance(foldIndices[i]), instance);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(InstanceFactory.createAttributeVector(new double[parentDataSet.noAttributes()]));
 fail("Should not be able to add an instance to a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClasses() {
 fold.classes();
 fail("Should not be able to call classes() on a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 fold.folds(5, new Random());
 fail("Should not be able to call folds() on a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Instance testInstance = parentDataSet.instance(0);
 DistanceMeasure distance = new EuclideanDistance();
 fold.kNearest(2, testInstance, distance);
 fail("Should not be able to call kNearest() on a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 InstanceList instancesToAdd = new InstanceList();
 instancesToAdd.addAll(parentDataSet);
 fold.addAll(instancesToAdd);
 fail("Should not be able to add all instances to a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAtIndex() {
 InstanceList instancesToAdd = new InstanceList();
 instancesToAdd.addAll(parentDataSet);
 fold.addAll(1, instancesToAdd);
 fail("Should not be able to add all instances to a fold at a specific index");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 fold.clear();
 fail("Should not be able to clear a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 fold.remove(0);
 fail("Should not be able to remove an instance from a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceByIndex() {
 fold.remove(0);
 fail("Should not be able to remove an instance by index from a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 InstanceList instancesToRetain = new InstanceList();
 fold.retainAll(instancesToRetain);
 fail("Should not be able to retain all instances from a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 Instance instanceToSet = parentDataSet.instance(0);
 fold.set(0, instanceToSet);
 fail("Should not be able to set an instance in a fold");
 }
}