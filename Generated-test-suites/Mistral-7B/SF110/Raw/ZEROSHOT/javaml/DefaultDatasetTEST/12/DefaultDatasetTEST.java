package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.InstanceList;
import net.sf.javaml.core.Singleton;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private InstanceList instanceList;
 private Set<Object> expectedClasses;
 private int maxAttributes;
 private Random random;

 @Before
 public void setup() {
 dataset = new DefaultDataset();
 maxAttributes = 3;
 expectedClasses = new HashSet<>(Arrays.asList("Class1", "Class2", "Class3"));
 random = new Random();

 // Create some instances
 Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
 Instance inst2 = new DenseInstance(new double[]{2.0, 3.0, 1.0}, "Class2");
 Instance inst3 = new DenseInstance(new double[]{3.0, 1.0, 2.0}, "Class3");
 Instance inst4 = new DenseInstance(new double[]{3.0, 3.0, 3.0}, null);

 // Add instances to the dataset
 dataset.add(inst1);
 dataset.add(inst2);
 dataset.add(inst3);
 dataset.add(inst4);

 // Create an InstanceList with the same instances
 instanceList = new InstanceList(Arrays.asList(inst1, inst2, inst3, inst4));
 }

 @Test
 public void testDefaultDatasetEmptyConstructor() {
 DefaultDataset emptyDataset = new DefaultDataset();
 assertTrue(emptyDataset.isEmpty());
 assertEquals(0, emptyDataset.size());
 }

 @Test
 public void testDefaultDatasetCollectionConstructor() {
 DefaultDataset datasetFromCollection = new DefaultDataset(instanceList);
 assertEquals(dataset, datasetFromCollection);
 }

 @Test
 public void testSetClasses() {
 // Test setting classes when constructing the dataset from a collection
 assertEquals(expectedClasses, dataset.classes());

 // Test setting classes manually
 dataset.clear();
 dataset.addAll(instanceList);
 dataset.check(instanceList);
 assertEquals(expectedClasses, dataset.classes());
 }

 @Test
 public void testAddInstance() {
 // Test adding an instance with no attributes
 Instance instanceWithNoAttributes = new Singleton(null);
 dataset.add(instanceWithNoAttributes);
 assertEquals(expectedClasses, dataset.classes());
 assertEquals(4, dataset.size());

 // Test adding an instance with attributes
 Instance instanceWithAttributes = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
 dataset.add(instanceWithAttributes);
 assertEquals(expectedClasses, dataset.classes());
 assertEquals(5, dataset.size());

 // Test checking the instance added
 dataset.check(instanceList);
 assertFalse(dataset.contains(instanceWithNoAttributes));
 assertTrue(dataset.contains(instanceWithAttributes));
 }

 @Test
 public void testAddAllInstances() {
 // Test adding all instances from a collection
 InstanceList instancesToAdd = new InstanceList(Arrays.asList(inst1, inst2, inst3));
 dataset.addAll(instancesToAdd);
 assertEquals(expectedClasses, dataset.classes());
 assertEquals(7, dataset.size());

 // Test checking the instances added
 dataset.check(instanceList);
 for (Instance instance : instancesToAdd) {
 assertTrue(dataset.contains(instance));
 }
 }

 @Test
 public void testAddAllInstancesWithDuplicates() {
 // Test adding instances with duplicates
 Instance instanceToAdd = inst1;
 dataset.addAll(Collections.nCopies(5, instanceToAdd));
 assertEquals(expectedClasses, dataset.classes());
 assertEquals(6, dataset.size());

 // Test checking the instances added
 dataset.check(instanceList);
 for (int i = 0; i < 6; i++) {
 assertTrue(dataset.contains(instanceToAdd));
 }
 }

 @Test
 public void testAddInstanceAt() {
 // Test adding an instance at index 0
 Instance instanceToAdd = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
 dataset.add(0, instanceToAdd);
 assertEquals(expectedClasses, dataset.classes());
 assertEquals(6, dataset.size());

 // Test checking the instance added
 dataset.check(instanceList);
 assertTrue(dataset.contains(instanceToAdd));
 }

 @Test
 public void testSetElementAt() {
 // Replace instance1 with instance5
 Instance instanceToReplace = inst1;
 Instance instanceToSet = inst3;
 dataset.setElementAt(instanceToSet, 0);
 assertEquals(expectedClasses, dataset.classes());
 assertEquals(6, dataset.size());

 // Test checking the replaced instance
 dataset.check(instanceList);
 assertFalse(dataset.contains(instanceToReplace));
 assertTrue(dataset.contains(instanceToSet));
 }

 @Test
 public void testRemoveInstance() {
 // Remove instance1
 Instance instanceToRemove = inst1;
 dataset.remove(instanceToRemove);
 assertEquals(expectedClasses, dataset.classes());
 assertEquals(5, dataset.size());

 // Test checking the removed instance
 dataset.check(instanceList);
 assertFalse(dataset.contains(instanceToRemove));
 }

 @Test
 public void testClear() {
 // Clear the dataset
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAddAllFolds() {
 // Create a dataset with 10 instances
 Dataset datasetWith10Instances = new DefaultDataset(instanceList);
 Dataset[] folds = datasetWith10Instances.folds(5, random);

 // Check that the number of folds is correct
 assertEquals(5, folds.length);

 // Check that the size of each fold is correct
 for (Dataset fold : folds) {
 assertEquals(2, fold.size());
 }
 }

 @Test
 public void testKNearest() {
 // Test k-nearest neighbor search with k = 1
 Instance queryInstance = new DenseInstance(new double[]{2.5, 2.5, 2.5}, null);
 Set<Instance> nearestNeighbors = dataset.kNearest(1, queryInstance, new EuclideanDistance());
 assertEquals(1, nearestNeighbors.size());
 Instance nearestNeighbor = nearestNeighbors.iterator().next();
 assertEquals(inst2, nearestNeighbor);

 // Test k-nearest neighbor search with k = 2
 nearestNeighbors = dataset.kNearest(2, queryInstance, new EuclideanDistance());
 assertEquals(2, nearestNeighbors.size());
 assertTrue(nearestNeighbors.contains(inst2));
 assertTrue(nearestNeighbors.contains(inst3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestWithInvalidK() {
 // Test k-nearest neighbor search with an invalid number of neighbors
 dataset.kNearest(-1, queryInstance, new EuclideanDistance());
 }
}