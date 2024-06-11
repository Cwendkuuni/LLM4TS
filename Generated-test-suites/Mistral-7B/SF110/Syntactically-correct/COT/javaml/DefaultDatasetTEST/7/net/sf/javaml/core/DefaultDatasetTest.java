package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;

import org.junit.Test;

public class DefaultDatasetTest {

 private DefaultDataset createEmptyDataset() {
 return new DefaultDataset();
 }

 private DefaultDataset createDatasetWithInstances(Instance... instances) {
 Collection<Instance> coll = new ArrayList<>();
 for (Instance instance : instances) {
 coll.add(instance);
 }
 return new DefaultDataset(coll);
 }

 // Test constructor with empty Collection
 @Test
 public void testDefaultDatasetEmptyCollection() {
 DefaultDataset ds = createEmptyDataset();
 assertEquals(0, ds.size());
 }

 // Test constructor with Collection of Instances
 @Test
 public void testDefaultDatasetCollectionInstances() {
 Instance inst1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1);
 Instance inst2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2);
 DefaultDataset ds = createDatasetWithInstances(inst1, inst2);
 assertEquals(2, ds.size());
 }

 // Test check method for Collection of Instances
 @Test
 public void testCheckCollectionInstances() {
 Instance inst1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1);
 Instance inst2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2);
 DefaultDataset ds = createDatasetWithInstances(inst1, inst2);
 Collection<Instance> c = new ArrayList<>();
 c.add(inst1);
 ds.check(c);
 assertEquals(1, ds.size());
 }

 // Test addAll method with null Collection
 @Test(expected = NullPointerException.class)
 public void testAddAllNullCollection() {
 DefaultDataset ds = createEmptyDataset();
 ds.addAll(null);
 }

 // Test addAll method with Collection of Instances
 @Test
 public void testAddAllCollectionInstances() {
 Instance inst1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1);
 Instance inst2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2);
 DefaultDataset ds1 = createDatasetWithInstances(inst1);
 DefaultDataset ds2 = createDatasetWithInstances(inst2);
 ds1.addAll(ds2);
 assertEquals(3, ds1.size());
 }

 // Test addAll method with Collection of different type
 @Test(expected = ClassCastException.class)
 public void testAddAllCollectionDifferentType() {
 DefaultDataset ds1 = createEmptyDataset();
 List<String> list = Arrays.asList("one", "two", "three");
 ds1.addAll(list);
 }

 // Test clear method
 @Test
 public void testClear() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 ds.clear();
 assertEquals(0, ds.size());
 }

 // Test add method with null Instance
 @Test(expected = NullPointerException.class)
 public void testAddNullInstance() {
 DefaultDataset ds = createEmptyDataset();
 ds.add(null);
 }

 // Test add method with Instance
 @Test
 public void testAddInstance() {
 DefaultDataset ds = createEmptyDataset();
 Instance inst = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1);
 ds.add(inst);
 assertEquals(1, ds.size());
 }

 // Test add method with different type Instance
 @Test(expected = ClassCastException.class)
 public void testAddDifferentTypeInstance() {
 DefaultDataset ds = createEmptyDataset();
 String strInst = "Instance";
 ds.add(strInst);
 }

 // Test addElementAt method with null Index
 @Test(expected = NullPointerException.class)
 public void testAddElementAtNullIndex() {
 DefaultDataset ds = createEmptyDataset();
 ds.addElementAt(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1), null);
 }

 // Test addElementAt method with Index and null Instance
 @Test(expected = NullPointerException.class)
 public void testAddElementAtNullInstance() {
 DefaultDataset ds = createEmptyDataset();
 ds.addElementAt(null, 0);
 }

 // Test addElementAt method with Index and Instance
 @Test
 public void testAddElementAtInstance() {
 DefaultDataset ds = createEmptyDataset();
 Instance inst = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1);
 ds.addElementAt(inst, 0);
 assertEquals(1, ds.size());
 }

 // Test addElementAt method with Index and different type Instance
 @Test(expected = ClassCastException.class)
 public void testAddElementAtDifferentTypeInstance() {
 DefaultDataset ds = createEmptyDataset();
 String strInst = "Instance";
 ds.addElementAt(strInst, 0);
 }

 // Test setElementAt method with null Index
 @Test(expected = NullPointerException.class)
 public void testSetElementAtNullIndex() {
 DefaultDataset ds = createEmptyDataset();
 Instance inst = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1);
 ds.setElementAt(inst, null);
 }

 // Test setElementAt method with null Instance
 @Test(expected = NullPointerException.class)
 public void testSetElementAtNullInstance() {
 DefaultDataset ds = createEmptyDataset();
 ds.setElementAt(null, 0);
 }

 // Test setElementAt method with Index and Instance
 @Test
 public void testSetElementAtInstance() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 Instance inst = new DenseInstance(new double[] { 7.0, 8.0, 9.0 }, 3);
 ds.setElementAt(inst, 0);
 assertEquals(1, inst.noAttributes());
 assertEquals(7.0, inst.value(0), 0.0);
 assertEquals(1.0, ds.instance(0).value(0), 0.0);
 assertEquals(3, ds.size());
 }

 // Test setElementAt method with Index and different type Instance
 @Test(expected = ClassCastException.class)
 public void testSetElementAtDifferentTypeInstance() {
 DefaultDataset ds = createEmptyDataset();
 String strInst = "Instance";
 ds.setElementAt(strInst, 0);
 }

 // Test get method with null Index
 @Test(expected = NullPointerException.class)
 public void testGetNullIndex() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 ds.get(null);
 }

 // Test get method with Index
 @Test
 public void testGetInstance() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 Instance inst = ds.get(0);
 assertEquals(1, inst.noAttributes());
 assertEquals(1.0, inst.value(0), 0.0);
 }

 // Test classes method with empty Dataset
 @Test
 public void testClassesEmptyDataset() {
 DefaultDataset ds = createEmptyDataset();
 assertEquals(0, ds.classes().size());
 }

 // Test classes method with Dataset containing Instances of different classes
 @Test
 public void testClassesDatasetDifferentClasses() {
 Instance inst1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1);
 Instance inst2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2);
 DefaultDataset ds = createDatasetWithInstances(inst1, inst2);
 assertEquals(2, ds.classes().size());
 }

 // Test classIndex method with null Object
 @Test(expected = NullPointerException.class)
 public void testClassIndexNullObject() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 ds.classIndex(null);
 }

 // Test classIndex method with valid Object
 @Test
 public void testClassIndexValidObject() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 assertEquals(0, ds.classIndex(1));
 assertEquals(1, ds.classIndex(2));
 }

 // Test classValue method with null Index
 @Test(expected = NullPointerException.class)
 public void testClassValueNullIndex() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 ds.classValue(-1);
 }

 // Test classValue method with valid Index
 @Test
 public void testClassValueValidIndex() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 assertEquals("1", ds.classValue(0).toString());
 assertEquals("2", ds.classValue(1).toString());
 }

 // Test copy method
 @Test
 public void testCopy() {
 DefaultDataset ds1 = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 DefaultDataset ds2 = ds1.copy();
 assertEquals(ds1.size(), ds2.size());
 }

 // Test kNearest method with null Instance
 @Test(expected = NullPointerException.class)
 public void testKNearestNullInstance() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 ds.kNearest(1, null, new ManhattanDistance());
 }

 // Test kNearest method with valid Instance and DistanceMeasure
 @Test
 public void testKNearestValidInstanceAndDistanceMeasure() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2), new DenseInstance(new double[] { 0.5, 0.5, 0.5 }, 1));
 Set<Instance> result = ds.kNearest(1, new DenseInstance(new double[] { 3.0, 3.0, 3.0 }, 1), new ManhattanDistance());
 assertEquals(1, result.size());
 }

 // Test folds method with null Random
 @Test(expected = NullPointerException.class)
 public void testFoldsNullRandom() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2), new DenseInstance(new double[] { 0.5, 0.5, 0.5 }, 1));
 ds.folds(3, null);
 }

 // Test folds method with valid Random and valid number of folds
 @Test
 public void testFoldsValidRandomAndNumberOfFolds() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2), new DenseInstance(new double[] { 0.5, 0.5, 0.5 }, 1));
 Dataset[] result = ds.folds(3, new Random());
 assertEquals(3, result.length);
 }

 // Test noAttributes method with empty Dataset
 @Test
 public void testNoAttributesEmptyDataset() {
 DefaultDataset ds = createEmptyDataset();
 assertEquals(0, ds.noAttributes());
 }

 // Test noAttributes method with Dataset containing Instances
 @Test
 public void testNoAttributesDatasetInstances() {
 DefaultDataset ds = createDatasetWithInstances(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1),
 new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 2));
 assertEquals(3, ds.noAttributes());
 }
}