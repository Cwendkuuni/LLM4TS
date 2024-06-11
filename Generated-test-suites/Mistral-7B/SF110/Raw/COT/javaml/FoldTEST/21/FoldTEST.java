package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.*;

import org.junit.Test;

public class FoldTest {

 private static final Random RANDOM = new Random();

 @Test
 public void testFoldConstructor() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parent, indices);
 assertEquals(indices.length, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddMethod() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parent, indices);
 Instance instance = new DefaultInstance(new double[5]);
 fold.add(instance);
 }

 @Test
 public void testClassesMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[5]));
 parent.add(new DefaultInstance(new double[5]));
 Fold fold = new Fold(parent, new int[] { 0 });
 Set<Object> classes = fold.classes();
 assertEquals(parent.classes().size(), classes.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFoldsMethod() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parent, indices);
 fold.folds(5, RANDOM);
 }

 @Test
 public void testGetInstanceMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[5]));
 parent.add(new DefaultInstance(new double[5]));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 Instance instance = fold.getInstance(1);
 assertEquals(parent.getInstance(1), instance);
 }

 @Test
 public void testKNearestMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[] { 1.0, 1.0 }));
 parent.add(new DefaultInstance(new double[] { 2.0, 2.0 }));
 parent.add(new DefaultInstance(new double[] { 3.0, 3.0 }));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 KNearestNeighbors kNN = new KNearestNeighbors();
 SortedSet<Instance> nearestInstances = fold.kNearest(2, null, kNN);
 assertEquals(2, nearestInstances.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllMethod() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parent, indices);
 Instance[] instances = new Instance[3];
 for (int i = 0; i < instances.length; ++i) {
 instances[i] = new DefaultInstance(new double[5]);
 }
 fold.addAll(Arrays.asList(instances));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethod() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parent, indices);
 fold.remove(0);
 }

 @Test
 public void testSizeMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[5]));
 parent.add(new DefaultInstance(new double[5]));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 assertEquals(parent.size(), fold.size());
 }

 @Test
 public void testSubListMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[5]));
 parent.add(new DefaultInstance(new double[5]));
 parent.add(new DefaultInstance(new double[5]));
 Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
 List<Instance> subList = fold.subList(1, 2);
assertEquals(1, subList.size());
 }

 @Test
 public void testIteratorMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[5]));
 parent.add(new DefaultInstance(new double[5]));
 Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
 List<Instance> instances = new ArrayList<Instance>();
 for (Instance instance : fold) {
 instances.add(instance);
 }
 assertEquals(parent.size(), instances.size());
 }

 @Test
 public void testNoAttributesMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[5]));
 Fold fold = new Fold(parent, new int[] {});
 assertEquals(parent.noAttributes(), fold.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClassIndexMethodNegativeIndex() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[5]));
 parent.add(new DefaultInstance(new double[5]));
 Fold fold = new Fold(parent, new int[] {});
 fold.classIndex(-1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClassIndexMethodUnknownClass() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[5]));
 parent.add(new DefaultInstance(new double[5]));
 parent.add(new DefaultInstance(new double[5]));
 parent.setClassValue(0, "A");
 parent.setClassValue(1, "B");
 Fold fold = new Fold(parent, new int[] {});
 fold.classIndex("C");
 }

 @Test
 public void testClassValueMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[5], new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 }, "A"));
 parent.add(new DefaultInstance(new double[5], new double[] { 2.0, 2.0, 2.0, 2.0, 2.0 }, "B"));
 Fold fold = new Fold(parent, new int[] { 0 });
 assertEquals("A", fold.classValue(0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClearMethod() {
 Dataset parent = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parent, indices);
 fold.clear();
 }

 @Test
 public void testCopyMethod() {
 Dataset parent = new DefaultDataset();
 parent.add(new DefaultInstance(new double[5]));
 parent.add(new DefaultInstance(new double[5]));
 Fold fold = new Fold(parent, new int[] { 0, 1 });
 Dataset copiedFold = fold.copy();
 assertEquals(fold, copiedFold);
 }
}