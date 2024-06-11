package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

 @Test
 public void testFoldConstructor() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(parent, indices);

 assertTrue(fold instanceof Fold);
 assertArrayEquals(indices, fold.indices);
 assertSame(parent, fold.parent);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstanceMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 fold.add(new DenseInstance(1.0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesMethodWithIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
 fold.addAll(1, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClearMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0 });
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceMethodWithIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0 });
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllInstancesMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 fold.removeAll(Collections.singletonList(new DenseInstance(1.0)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllInstancesMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 fold.retainAll(Collections.singletonList(new DenseInstance(1.0)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstanceMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 fold.set(0, new DenseInstance(1.0));
 }

 @Test
 public void testSizeMethod() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(parent, indices);

 assertEquals(5, fold.size());
 }

 @Test
 public void testIteratorMethod() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(parent, indices);

 List<Instance> instances = new ArrayList<>();
 for (Instance instance : fold) {
 instances.add(instance);
 }

 assertEquals(5, instances.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesMethodWithNullList() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 List<Instance> instances = null;
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesMethodWithEmptyList() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 List<Instance> instances = Collections.emptyList();
 fold.addAll(instances);
 }

 @Test
 public void testToStringMethod() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(parent, indices);

 assertEquals("Fold [indices=[0, 1, 2, 3, 4], parent=Dataset]", fold.toString());
 }

 @Test
 public void testClassesMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DenseInstance(0.0));
 parent.add(new DenseInstance(1.0));
 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parent, indices);

 Set<Object> classes = fold.classes();

 assertEquals(2, classes.size());
 assertTrue(classes.contains(0.0));
 assertTrue(classes.contains(1.0));
 }

 @Test
 public void testGetInstanceMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DenseInstance(0.0));
 parent.add(new DenseInstance(1.0));
 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parent, indices);

 Instance instance = fold.instance(0);

 assertEquals(0.0, instance.classValue(0), 0.001);
 assertEquals(0.0, instance.value(0), 0.001);
 assertEquals(0.0, instance.value(1), 0.001);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 fold.add(new DenseInstance(1.0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllMethod() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 Dataset dataset = new DefaultDataset();
 fold.addAll(dataset);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllMethodWithCollection() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllMethodWithIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 List<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
 fold.addAll(1, instances);
 }

 @Test
 public void testToArrayMethod() {
 Dataset parent = new DefaultDataset();
 parent.addAll(Arrays.asList(new DenseInstance(0.0), new DenseInstance(1.0)));
 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parent, indices);

 Object[] array = fold.toArray();

 assertEquals(2, array.length);
 assertTrue(array[0] instanceof DenseInstance);
 assertTrue(array[1] instanceof DenseInstance);
 }

 @Test
 public void testToArrayMethodWithArray() {
 Dataset parent = new DefaultDataset();
 parent.addAll(Arrays.asList(new DenseInstance(0.0), new DenseInstance(1.0)));
 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parent, indices);

 Object[] array = fold.toArray(new Object[10]);

 assertEquals(2, array.length);
 assertTrue(array[0] instanceof DenseInstance);
 assertTrue(array[1] instanceof DenseInstance);
 }

 @Test
 public void testNoAttributesMethod() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parent, indices);

 assertEquals(parent.noAttributes(), fold.noAttributes());
 }

 @Test
 public void testClassIndexMethod() {
 Dataset parent = new DefaultDataset();
 parent.addAll(Arrays.asList(new DenseInstance(0.0, 0.0), new DenseInstance(1.0, 1.0)));
 int[] indices = new int[] { 1 };
 Fold fold = new Fold(parent, indices);

 assertEquals(1, fold.classIndex(1.0));
 }

 @Test
 public void testClassValueMethod() {
 Dataset parent = new DefaultDataset();
 parent.addAll(Arrays.asList(new DenseInstance(0.0, 0.0), new DenseInstance(1.0, 1.0)));
 int[] indices = new int[] { 1 };
 Fold fold = new Fold(parent, indices);

 assertEquals(1.0, fold.classValue(0), 0.001);
 }

 @Test
 public void testCopyMethod() {
 Dataset parent = new DefaultDataset();
 parent.addAll(Arrays.asList(new DenseInstance(0.0), new DenseInstance(1.0)));
 int[] indices = new int[] { 0 };
 Fold fold = new Fold(parent, indices);

 Dataset copy = fold.copy();

 assertEquals(1, copy.size());
 assertTrue(copy instanceof Fold);
 }
}