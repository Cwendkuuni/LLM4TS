package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class FoldTest {

 private Dataset parentDataset;
 private Fold fold;

 @Before
 public void setUp() {
 parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1}));
 parentDataset.add(new DenseInstance(new double[]{2.0, 2.0, 3.0}, new Object[]{2}));
 parentDataset.add(new DenseInstance(new double[]{3.0, 2.0, 3.0}, new Object[]{3}));
 parentDataset.add(new DenseInstance(new double[]{4.0, 2.0, 3.0}, new Object[]{4}));
 parentDataset.add(new DenseInstance(new double[]{5.0, 2.0, 3.0}, new Object[]{5}));

 int[] indices = {1, 2, 3, 0, 4};
 fold = new Fold(parentDataset, indices);
 }

 @Test
 public void testSize() {
 assertEquals(parentDataset.size(), fold.size());
 }

 @Test
 public void testGetInstance() {
 Instance instance;
 for (int i = 0; i < fold.size(); ++i) {
 instance = fold.instance(i);
 assertEquals(parentDataset.instance(indices[i]), instance);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 fold.add(new DenseInstance(new double[]{6.0, 2.0, 3.0}, new Object[]{6}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{6.0, 2.0, 3.0}, new Object[]{6}));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 fold.remove(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAll() {
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1}));
 fold.removeAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1}));
 fold.retainAll(Collections.emptyList());
 }

 @Test
 public void testIterator() {
 List<Instance> result = new ArrayList<>();
 for (Instance instance : fold) {
 result.add(instance);
 }
 assertEquals(parentDataset.size(), result.size());
 for (int i = 0; i < result.size(); ++i) {
 assertEquals(parentDataset.instance(indices[i]), result.get(i));
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1});
 fold.set(0, instance);
 }

 @Test
 public void testNoAttributes() {
 assertEquals(parentDataset.noAttributes(), fold.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(parentDataset.classIndex(Collections.singletonList(new Object[]{1})), fold.classIndex(Collections.singletonList(new Object[]{1})));
 }

 @Test
 public void testClassValue() {
 assertEquals(parentDataset.classValue(0), fold.classValue(0));
 }

 @Test
 public void testCopy() {
 Dataset copy = fold.copy();
 assertEquals(fold.size(), copy.size());
 for (int i = 0; i < fold.size(); ++i) {
 assertEquals(fold.instance(i), copy.instance(i));
 }
 }
}