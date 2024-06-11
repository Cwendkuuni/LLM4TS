package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class FoldTest {

 private Dataset parentDataSet;
 private Fold fold;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 parentDataSet = new DefaultDataset();
 parentDataSet.add(createInstance(1));
 parentDataSet.add(createInstance(2));
 parentDataSet.add(createInstance(3));
 fold = new Fold(parentDataSet, new int[] { 1, 2 });
 }

 @Test
 public void testConstructor() {
 assertNotNull(fold);
 assertEquals(2, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(createInstance(4));
 fail("Should not be able to add an instance to a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 List<Instance> instances = new ArrayList<>();
 instances.add(createInstance(4));
 fold.addAll(instances);
 fail("Should not be able to add all instances to a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAtIndex() {
 List<Instance> instances = new ArrayList<>();
 instances.add(createInstance(4));
 fold.addAll(1, instances);
 fail("Should not be able to add all instances to a fold at a specific index");
 }

 @Test
 public void testClear() {
 fold.clear();
 assertTrue(fold.isEmpty());
 }

 @Test
 public void testSize() {
 assertEquals(2, fold.size());
 }

 @Test
 public void testGetInstance() {
 Instance instance1 = fold.instance(0);
 Instance instance2 = fold.instance(1);
 assertEquals(instance1, parentDataSet.instance(0));
 assertEquals(instance2, parentDataSet.instance(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 fold.remove(0);
 fail("Should not be able to remove an instance from a fold");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceAtIndex() {
 fold.remove(0);
 fail("Should not be able to remove an instance from a fold at a specific index");
 }

 @Test
 public void testRetainAll() {
 List<Instance> instancesToKeep = new ArrayList<>();
 instancesToKeep.add(createInstance(1));
 instancesToKeep.add(createInstance(2));
 fold.retainAll(instancesToKeep);
 assertEquals(2, fold.size());
 }

 @Test
 public void testRemoveAll() {
 List<Instance> instancesToRemove = new ArrayList<>();
 instancesToRemove.add(createInstance(1));
 instancesToRemove.add(createInstance(2));
 fold.removeAll(instancesToRemove);
 assertTrue(fold.isEmpty());
 }

 @Test
 public void testClasses() {
 Set<Object> classes = fold.classes();
 assertEquals(parentDataSet.classes().size(), classes.size());
 }

 @Test
 public void testKNearest() {
 throw new UnsupportedOperationException("Method is not yet implemented in Fold class");
 }

 @Test
 public void testFolds() {
 throw new UnsupportedOperationException("Method is not yet implemented in Fold class");
 }

 @Test
 public void testNoAttributes() {
 assertEquals(parentDataSet.noAttributes(), fold.noAttributes());
 }

 @Test
 public void testClassIndex() {
 Object clazz = parentDataSet.getClassValue(0);
 assertEquals(parentDataSet.classIndex(clazz), fold.classIndex(clazz));
 }

 @Test
 public void testClassValue() {
 Object clazz = parentDataSet.getClassValue(0);
 assertEquals(clazz, fold.classValue(0));
 }

 @Test
 public void testCopy() {
 Dataset copiedFold = fold.copy();
 assertEquals(fold.size(), copiedFold.size());
 for (int i = 0; i < fold.size(); i++) {
 assertEquals(fold.instance(i), copiedFold.instance(i));
 }
 }

 private Instance createInstance(int index) {
 Double[] attributes = new Double[parentDataSet.noAttributes()];
 for (int j = 0; j < attributes.length; j++) {
 attributes[j] = (double) j;
 }
 return new DenseInstance(1.0, attributes);
 }
}