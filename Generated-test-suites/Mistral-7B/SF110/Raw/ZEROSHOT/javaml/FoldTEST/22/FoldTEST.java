package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.instance.InstanceFactory;
import net.sf.javaml.instance.InstanceLoader;
import net.sf.javaml.instance.Instances;
import net.sf.javaml.instance.AttributeHandler;
import net.sf.javaml.instance.DoubleAttributeHandler;
import net.sf.javaml.tools.dataset.file.CSVReader;

public class FoldTest {

 private Dataset dataset;
 private Fold fold;

 @Before
 public void setUp() throws Exception {
 String dataFile = "data.csv"; // Replace with the path to your dataset file
 Instances instances = new InstanceLoader().loadInstances(dataFile);
 this.dataset = instances;
 this.fold = new Fold(dataset, new int[] { 2, 4, 7, 10, 12 });
 }

 @Test
 public void testSize() {
 assertEquals(5, fold.size());
 }

 @Test
 public void testParentDataset() {
 assertEquals(dataset, fold.parent);
 }

 @Test
 public void testGetInstance() {
 Instance instance1 = fold.instance(0);
 Instance instance2 = fold.instance(1);
 Instance instance3 = fold.instance(2);

 assertEquals(dataset.instance(2), instance1);
 assertEquals(dataset.instance(4), instance2);
 assertEquals(dataset.instance(7), instance3);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(InstanceFactory.createRandomInstance(new DoubleAttributeHandler(2)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 Instances instances = new Instances(new CSVReader(new File("data.csv")), true);
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAtIndex() {
 Instances instances = new Instances(new CSVReader(new File("data.csv")), true);
 fold.addAll(2, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContains() {
 fold.contains(InstanceFactory.createRandomInstance(new DoubleAttributeHandler(2)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsAll() {
 Instances instances = new Instances(new CSVReader(new File("data.csv")), true);
 fold.containsAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 fold.remove(InstanceFactory.createRandomInstance(new DoubleAttributeHandler(2)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAtIndex() {
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 Instances instances = new Instances(new CSVReader(new File("data.csv")), true);
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 fold.set(0, InstanceFactory.createRandomInstance(new DoubleAttributeHandler(2)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllFolds() {
 Dataset dataset2 = new Instances(new CSVReader(new File("data2.csv")), true);
 Fold fold2 = new Fold(dataset2, new int[] { 2, 4, 7, 12 });
 fold.addAllFolds(fold2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetFolds() {
 fold.getFolds(5, new Random());
 }

 @Test
 public void testKNearest() {
 Instance testInstance = InstanceFactory.createRandomInstance(new DoubleAttributeHandler(2));
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 Set<Instance> nearestInstances = fold.kNearest(2, testInstance, distanceMeasure);
 assertEquals(2, nearestInstances.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 fold.folds(5, new Random());
 }
}