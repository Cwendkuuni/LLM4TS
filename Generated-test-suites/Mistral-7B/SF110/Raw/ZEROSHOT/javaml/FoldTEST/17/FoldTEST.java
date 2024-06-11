package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.impl.cosine.CosineDistance;

public class FoldTest {

 @org.junit.Test
 public void testConstructor() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parent, indices);
 assertEquals(parent, fold.getParent());
 assertEquals(indices, fold.getIndices());
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testAddMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 Instance instance = new DefaultInstance(2);
 fold.add(instance);
 }

 @org.junit.Test
 public void testClassesMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2));
 parent.add(new DefaultInstance(1));
 Fold fold = new Fold(parent, new int[0]);
 Set<Object> classes = fold.classes();
 assertTrue(classes.contains(1));
 assertTrue(classes.contains(2));
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testFoldsMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 fold.folds(5, null);
 }

 @org.junit.Test
 public void testInstanceMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 Instance instance = fold.instance(0);
 assertEquals(new DoubleValue(1.0), instance.getValue(0));
 assertEquals(new DoubleValue(2.0), instance.getValue(1));
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testKNearestMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 Instance instance = new DefaultInstance(1, Collections.singletonList(new DoubleValue(3.0)));
 fold.kNearest(5, instance, new CosineDistance());
 }

 @org.junit.Test
 public void testAddMethodWithExceptionMessage() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 try {
 fold.add(new DefaultInstance(1));
 fail();
 } catch (UnsupportedOperationException e) {
 assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
 }
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testAddAllMethod() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 List<Instance> instances = Arrays.asList(new DefaultInstance(1), new DefaultInstance(2));
 fold.addAll(instances);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testAddAllMethodWithIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 List<Instance> instances = Arrays.asList(new DefaultInstance(1), new DefaultInstance(2));
 fold.addAll(1, instances);
 }

 @org.junit.Test
 public void testClearMethodWithExceptionMessage() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 try {
 fold.clear();
 fail();
 } catch (UnsupportedOperationException e) {
 assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
 }
 }

 @org.junit.Test
 public void testContainsMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0 });
 assertTrue(fold.contains(parent.instance(0)));
 }

 @org.junit.Test
 public void testContainsAllMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 List<Instance> instances = Arrays.asList(parent.instance(0), parent.instance(1));
 assertTrue(fold.containsAll(instances));
 }

 @org.junit.Test
 public void testGetMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0 });
 Instance instance = fold.get(0);
 assertEquals(parent.instance(0), instance);
 }

 @org.junit.Test
 public void testIndexOfMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0 });
 assertEquals(0, fold.indexOf(parent.instance(0)));
 }

 @org.junit.Test
 public void testIsEmptyMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[0]);
 assertFalse(fold.isEmpty());
 }

 @org.junit.Test
 public void testIteratorMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 List<Instance> instances = new ArrayList<>();
 for (Instance instance : fold) {
 instances.add(instance);
 }
 assertEquals(Arrays.asList(parent.instance(0), parent.instance(1)), instances);
 }

 @org.junit.Test
 public void testLastIndexOfMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 assertEquals(1, fold.lastIndexOf(parent.instance(1)));
 }

 @org.junit.Test
 public void testListIteratorMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 Fold.FoldIterator iterator = fold.listIterator();
 Instance instance = iterator.next();
 assertEquals(parent.instance(0), instance);
 iterator.remove();
 assertFalse(fold.contains(instance));
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethod() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 Instance instance = new DefaultInstance(1);
 fold.remove(instance);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethodWithIndex() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 fold.remove(0);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllMethod() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 List<Instance> instances = Arrays.asList(new DefaultInstance(1), new DefaultInstance(2));
 fold.removeAll(instances);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testRetainAllMethod() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 List<Instance> instances = Arrays.asList(new DefaultInstance(1), new DefaultInstance(2));
 fold.retainAll(instances);
 }

 @org.junit.Test
 public void testSetMethodWithExceptionMessage() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 try {
 fold.set(0, new DefaultInstance(1));
 fail();
 } catch (UnsupportedOperationException e) {
 assertEquals("You cannot do this on a fold.", e.getMessage());
 }
 }

 @org.junit.Test
 public void testSizeMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 assertEquals(2, fold.size());
 }

 @org.junit.Test
 public void testSubListMethodWithExceptionMessage() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 try {
 fold.subList(0, 1);
 fail();
 } catch (UnsupportedOperationException e) {
 assertEquals("You cannot do this on a fold.", e.getMessage());
 }
 }

 @org.junit.Test
 public void testToArrayMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 Object[] array = fold.toArray();
 assertEquals(2, array.length);
 }

 @org.junit.Test
 public void testToArrayMethodWithArgument() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 Object[] array = new Object[2];
 fold.toArray(array);
 assertEquals(2, array.length);
 }

 @org.junit.Test
 public void testNoAttributesMethod() {
 Dataset parent = new DefaultDataset();
 Fold fold = new Fold(parent, new int[0]);
 assertEquals(parent.noAttributes(), fold.noAttributes());
 }

 @org.junit.Test
 public void testClassIndexMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 assertEquals(parent.classIndex(1), fold.classIndex(1));
 }

 @org.junit.Test
 public void testClassValueMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0), new StringValue("Class1"))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0), new StringValue("Class2"))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 assertEquals("Class1", fold.classValue(0));
 assertEquals("Class2", fold.classValue(1));
 }

 @org.junit.Test
 public void testCopyMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(2, Arrays.asList(new DoubleValue(1.0), new DoubleValue(2.0))));
 parent.add(new DefaultInstance(1, Arrays.asList(new DoubleValue(3.0), new DoubleValue(4.0))));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 Dataset copy = fold.copy();
 assertEquals(fold.size(), copy.size());
 }
}