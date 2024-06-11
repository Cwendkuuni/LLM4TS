package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;
 private static final int NO_ATTRIBUTES = 5;
 private static final Object CLASS_VALUE = "ClassValue";

 @Test
 public void testSparseInstanceWithDefaultConstructor() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesConstructor() {
 SparseInstance instance = new SparseInstance(NO_ATTRIBUTES);
 assertEquals(NO_ATTRIBUTES, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueConstructor() {
 SparseInstance instance = new SparseInstance(NO_ATTRIBUTES, DEFAULT_VALUE);
 assertEquals(NO_ATTRIBUTES, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndClassValueConstructor() {
 SparseInstance instance = new SparseInstance(NO_ATTRIBUTES, CLASS_VALUE);
 assertEquals(NO_ATTRIBUTES, instance.noAttributes());
 assertEquals(CLASS_VALUE, instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValueConstructor() {
 SparseInstance instance = new SparseInstance(NO_ATTRIBUTES, DEFAULT_VALUE, CLASS_VALUE);
 assertEquals(NO_ATTRIBUTES, instance.noAttributes());
 assertEquals(CLASS_VALUE, instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDoubleArrayConstructor() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(datavector.length, instance.noAttributes());
 assertEquals(CLASS_VALUE, instance.getClassValue());

 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 Map.Entry<Integer, Double> entry;

 for (int i = 0; i < datavector.length; i++) {
 entry = entrySet.iterator().next();
 assertEquals(Integer.valueOf(i), entry.getKey());
 assertEquals(Double.valueOf(datavector[i]), entry.getValue());
 entrySet.remove();
 }

 assertTrue(instance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndDefaultValueConstructor() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0};
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE);
 assertEquals(datavector.length, instance.noAttributes());
 assertEquals(CLASS_VALUE, instance.getClassValue());

 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 Map.Entry<Integer, Double> entry;

 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != DEFAULT_VALUE) {
 entry = entrySet.iterator().next();
 assertEquals(Integer.valueOf(i), entry.getKey());
 assertEquals(Double.valueOf(datavector[i]), entry.getValue());
 entrySet.remove();
 }
 }

 assertTrue(instance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndClassValueConstructor() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0};
 SparseInstance instance = new SparseInstance(datavector, CLASS_VALUE);
 assertEquals(datavector.length, instance.noAttributes());
 assertEquals(CLASS_VALUE, instance.getClassValue());

 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 Map.Entry<Integer, Double> entry;

 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != 0.0) {
 entry = entrySet.iterator().next();
 assertEquals(Integer.valueOf(i), entry.getKey());
 assertEquals(Double.valueOf(datavector[i]), entry.getValue());
 entrySet.remove();
 }
 }

 assertTrue(instance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndDefaultValueAndClassValueConstructor() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0};
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE, CLASS_VALUE);
 assertEquals(datavector.length, instance.noAttributes());
 assertEquals(CLASS_VALUE, instance.getClassValue());

 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 Map.Entry<Integer, Double> entry;

 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != DEFAULT_VALUE) {
 entry = entrySet.iterator().next();
 assertEquals(Integer.valueOf(i), entry.getKey());
 assertEquals(Double.valueOf(datavector[i]), entry.getValue());
 entrySet.remove();
 }
 }

 assertTrue(instance.isEmpty());
 }

 @Test
 public void testSparseInstanceGetValue() {
 SparseInstance instance = new SparseInstance(Collections.nCopies(3, 1.0));
 assertEquals(1.0, instance.value(0));
 assertEquals(1.0, instance.value(2));
 assertNull(instance.value(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSparseInstanceGetValueOutOfBounds() {
 SparseInstance instance = new SparseInstance(Collections.nCopies(3, 1.0));
 instance.value(-1);
 }

 @Test
 public void testSparseInstanceIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testSparseInstanceContainsKey() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance.containsKey(Integer.valueOf(0)));
 assertFalse(instance.containsKey(Integer.valueOf(5)));
 }

 @Test
 public void testSparseInstanceContainsValue() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance.containsValue(Double.valueOf(1.0)));
 assertFalse(instance.containsValue(Double.valueOf(5.0)));
 }

 @Test
 public void testSparseInstanceEntrySet() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 Map.Entry<Integer, Double> entry;

 for (int i = 0; i < instance.noAttributes(); i++) {
 entry = entrySet.iterator().next();
 assertEquals(Integer.valueOf(i), entry.getKey());
 assertEquals(Double.valueOf(instance.value(i)), entry.getValue());
 entrySet.remove();
 }

 assertTrue(instance.isEmpty());
 }

 @Test
 public void testSparseInstanceGet() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, instance.get(Integer.valueOf(0)));
 assertEquals(2.0, instance.get(Integer.valueOf(1)));
 assertNull(instance.get(Integer.valueOf(3)));
 assertNull(instance.get(null));
 }

 @Test
 public void testSparseInstanceHashCode() {
 SparseInstance instance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance instance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance instance3 = new SparseInstance(new double[]{2.0, 1.0, 3.0});

 assertEquals(instance1.hashCode(), instance1.hashCode());
 assertNotEquals(instance1.hashCode(), instance2.hashCode());
 assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testSparseInstanceEquals() {
 SparseInstance instance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance instance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance instance3 = new SparseInstance(new double[]{2.0, 1.0, 3.0});

 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance2));
 assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testSparseInstanceCopy() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance copiedInstance = instance.copy();

 assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
 assertEquals(instance.getClassValue(), copiedInstance.getClassValue());

 Set<Map.Entry<Integer, Double>> entrySetInstance = instance.entrySet();
 Map.Entry<Integer, Double> entry;
 Set<Map.Entry<Integer, Double>> entrySetCopiedInstance = copiedInstance.entrySet();

 for (int i = 0; i < instance.noAttributes(); i++) {
 entry = entrySetInstance.iterator().next();
 assertEquals(Integer.valueOf(i), entry.getKey());
 assertEquals(Double.valueOf(entry.getValue().doubleValue()),
 copiedInstance.get(entry.getKey()).doubleValue());
 entrySetInstance.remove();
 entrySetCopiedInstance.remove();
 }
 }

 @Test
 public void testSparseInstanceRemoveAttribute() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 instance.removeAttribute(2);
 assertEquals(3, instance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSparseInstanceRemoveAttributeOutOfBounds() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 instance.removeAttribute(5);
 }
}