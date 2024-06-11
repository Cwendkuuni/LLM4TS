package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FoldTest {

 @Test
 public void testFold() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });

 assertEquals("Wrong number of instances in Fold", 4, fold.size());

 Instance inst1 = fold.instance(0);
 Instance inst2 = fold.instance(1);
 Instance inst3 = fold.instance(2);
 Instance inst4 = fold.instance(3);

 assertEquals("Instance at index 0 not equal to parent dataset instance", inst1, new DefaultDataset().instance(0));
 assertEquals("Instance at index 1 not equal to parent dataset instance", inst2, new DefaultDataset().instance(1));
 assertEquals("Instance at index 2 not equal to parent dataset instance", inst3, new DefaultDataset().instance(2));
 assertEquals("Instance at index 3 not equal to parent dataset instance", inst4, new DefaultDataset().instance(3));

 // Test iterator
 List<Instance> instances = Arrays.asList(inst1, inst2, inst3, inst4);
 List<Instance> foldInstances = new ArrayList<>();
 for (Instance inst : fold) {
 foldInstances.add(inst);
 }
 assertEquals("Instances in Fold don't match parent dataset instances", instances, foldInstances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 try {
 fold.add(new DenseInstance(new double[0]));
 fail("Should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 try {
 fold.addAll(Arrays.asList(new DenseInstance(new double[0])));
 fail("Should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAtIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 try {
 fold.addAll(Arrays.asList(new DenseInstance(new double[0])), 0);
 fail("Should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Fold fold = new Fold(new DefaultDataset(), new int[] {});
 try {
 fold.clear();
 fail("Should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 try {
 fold.remove(inst1);
 fail("Should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceByIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 try {
 fold.remove(0);
 fail("Should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllInstances() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 try {
 fold.removeAll(Collections.singleton(inst1));
 fail("Should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAllInstancesAtIndex() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 try {
 fold.remove(0, 1);
 fail("Should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2, 3 });
 try {
 fold.set(0, inst1);
 fail("Should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

}