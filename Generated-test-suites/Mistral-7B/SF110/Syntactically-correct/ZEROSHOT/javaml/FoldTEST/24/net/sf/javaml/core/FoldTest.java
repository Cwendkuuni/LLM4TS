package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

public class FoldTest {

 @Test
 public void testSize() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 assertEquals(5, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.add(new DenseInstance(new double[]{1.0}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0}), new DenseInstance(new double[]{2.0}));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0}), new DenseInstance(new double[]{2.0}));
 fold.addAll(2, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.remove(new DenseInstance(new double[]{1.0}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0}), new DenseInstance(new double[]{2.0}));
 fold.removeAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0}), new DenseInstance(new double[]{2.0}));
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.set(0, new DenseInstance(new double[]{1.0}));
 }

 @Test
 public void testClasses() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 Set<Object> classes = fold.classes();
 assertEquals(fold.parent.classes().size(), classes.size());
 }

 @Test
 public void testInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 Instance instance = fold.instance(0);
 assertEquals(fold.parent.instance(0), instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.kNearest(2, fold.instance(0), new EuclideanDistance());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 fold.folds(5, new Random());
 }

 @Test
 public void testRandomAccess() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 Instance instance0 = fold.instance(0);
 Instance instance1 = fold.instance(1);
 Instance instance2 = fold.instance(2);
 Instance instance3 = fold.instance(3);
 Instance instance4 = fold.instance(4);
 assertEquals(fold.parent.instance(0), instance0);
 assertEquals(fold.parent.instance(1), instance1);
 assertEquals(fold.parent.instance(2), instance2);
 assertEquals(fold.parent.instance(3), instance3);
 assertEquals(fold.parent.instance(4), instance4);
 }

 @Test
 public void testIterator() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 List<Instance> instances = new ArrayList<>();
 for (Instance instance : fold) {
 instances.add(instance);
 }
 assertEquals(fold.size(), instances.size());
 for (int i = 0; i < fold.size(); ++i) {
 Instance instance = fold.instance(i);
 assertEquals(instance, instances.get(i));
 }
 }

 @Test
 public void testListIterator() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 ListIterator<Instance> listIterator = fold.listIterator();
 List<Instance> instances = new ArrayList<>();
 while (listIterator.hasNext()) {
 instances.add(listIterator.next());
 }
 assertEquals(fold.size(), instances.size());
 for (int i = 0; i < fold.size(); ++i) {
 Instance instance = fold.instance(i);
 assertEquals(instance, instances.get(i));
 }
 listIterator = fold.listIterator(fold.size());
 assertTrue(listIterator.hasNext());
 Instance lastInstance = listIterator.next();
 Instance previousInstance = fold.instance(fold.size() - 2);
 assertEquals(lastInstance, previousInstance);
 }

 @Test
 public void testListIteratorWithIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2, 3, 4});
 ListIterator<Instance> listIterator = fold.listIterator(1);
 List<Instance> instances = new ArrayList<>();
 while (listIterator.hasNext()) {
 instances.add(listIterator.next());
 }
 assertEquals(fold.size(), instances.size());
 Instance instance1 = fold.instance(1);
 assertEquals(instance1, instances.get(0));
 }
}