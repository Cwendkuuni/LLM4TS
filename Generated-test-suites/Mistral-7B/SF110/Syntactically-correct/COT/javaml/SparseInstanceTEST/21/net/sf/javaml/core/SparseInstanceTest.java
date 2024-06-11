package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;
 private static final Integer KEY_ONE = Integer.valueOf(1);
 private static final Double VALUE_ONE = Double.valueOf(1.0);
 private static final Object CLASS_VALUE = new Object() {};
 private SparseInstance instance;

 @Before
 public void setUp() {
 this.instance = new SparseInstance();
 }

 @Test
 public void testConstructorWithNoArguments() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 assertNotNull(sparseInstance.getData());
 assertNull(sparseInstance.getDefaultValue());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 this.instance.setNoAttributes(0);
 assertEquals(0, this.instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 this.instance = new SparseInstance(0, DEFAULT_VALUE);
 assertEquals(0, this.instance.noAttributes());
 assertNotNull(this.instance.getData());
 assertEquals(DEFAULT_VALUE, this.instance.getDefaultValue());
 }

 @Test
 public void testConstructorWithNoAttributesAndClassValue() {
 this.instance = new SparseInstance(0, CLASS_VALUE);
 assertEquals(0, this.instance.noAttributes());
 assertNotNull(this.instance.getData());
 assertSame(CLASS_VALUE, this.instance.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributesClassValueAndDefaultValue() {
 this.instance = new SparseInstance(0, CLASS_VALUE, DEFAULT_VALUE);
 assertEquals(0, this.instance.noAttributes());
 assertNotNull(this.instance.getData());
 assertSame(CLASS_VALUE, this.instance.getClassValue());
 assertEquals(DEFAULT_VALUE, this.instance.getDefaultValue());
 }

 @Test
 public void testConstructorWithAttributes() {
 double[] datavector = new double[] {1.0, 2.0, 3.0};
 this.instance = new SparseInstance(datavector);
 assertEquals(3, this.instance.noAttributes());
 assertEquals(Arrays.asList(KEY_ONE, KEY_ONE.valueOf(2), KEY_ONE.valueOf(3)),
 this.instance.keySet());
 }

 @Test
 public void testConstructorWithAttributesDefaultValue() {
 double[] datavector = new double[] {1.0, 2.0, 3.0};
 this.instance = new SparseInstance(datavector, DEFAULT_VALUE);
 assertEquals(3, this.instance.noAttributes());
 assertEquals(Arrays.asList(KEY_ONE, KEY_ONE.valueOf(2), KEY_ONE.valueOf(3)),
 this.instance.keySet());
 }

 @Test
 public void testConstructorWithAttributesClassValue() {
 double[] datavector = new double[] {1.0, 2.0, 3.0};
 this.instance = new SparseInstance(datavector, CLASS_VALUE);
 assertEquals(3, this.instance.noAttributes());
 assertEquals(Arrays.asList(KEY_ONE, KEY_ONE.valueOf(2), KEY_ONE.valueOf(3)),
 this.instance.keySet());
 assertSame(CLASS_VALUE, this.instance.getClassValue());
 }

 @Test
 public void testConstructorWithAttributesClassValueAndDefaultValue() {
 double[] datavector = new double[] {1.0, 2.0, 3.0};
 this.instance = new SparseInstance(datavector, CLASS_VALUE, DEFAULT_VALUE);
 assertEquals(3, this.instance.noAttributes());
 assertEquals(Arrays.asList(KEY_ONE, KEY_ONE.valueOf(2), KEY_ONE.valueOf(3)),
 this.instance.keySet());
 assertSame(CLASS_VALUE, this.instance.getClassValue());
 assertEquals(DEFAULT_VALUE, this.instance.getDefaultValue());
 }

 @Test
 public void testSetNoAttributes() {
 this.instance.setNoAttributes(5);
 assertEquals(5, this.instance.noAttributes());
 }

 @Test
 public void testValueMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertEquals(VALUE_ONE.doubleValue(), this.instance.value(0), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueMethodWithNegativeIndex() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 fail("Should throw IndexOutOfBoundsException");
 this.instance.value(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueMethodWithIndexGreaterThanNoAttributes() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 fail("Should throw IndexOutOfBoundsException");
 this.instance.value(this.instance.noAttributes());
 }

 @Test
 public void testClearMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 this.instance.clear();
 assertEquals(0, this.instance.noAttributes());
 assertNull(this.instance.getData());
 }

 @Test
 public void testContainsKeyMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertTrue(this.instance.containsKey(KEY_ONE));
 }

 @Test
 public void testContainsKeyMethodWithNonExistentKey() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertFalse(this.instance.containsKey(KEY_ONE.valueOf(2)));
 }

 @Test
 public void testContainsValueMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertTrue(this.instance.containsValue(VALUE_ONE));
 }

 @Test
 public void testContainsValueMethodWithNonExistentValue() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertFalse(this.instance.containsValue(VALUE_ONE.doubleValue() + 0.001));
 }

 @Test
 public void testEntrySetMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 Set<Map.Entry<Integer, Double>> entrySet = this.instance.entrySet();
 assertTrue(entrySet instanceof Set);
 assertTrue(entrySet.getClass().isAssignableFrom(Set.class));
 }

 @Test
 public void testGetMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertEquals(VALUE_ONE, this.instance.get(KEY_ONE));
 }

 @Test
 public void testGetMethodWithNonExistentKey() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertEquals(DEFAULT_VALUE, this.instance.get(KEY_ONE.valueOf(2)));
 }

 @Test
 public void testIsEmptyMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertFalse(this.instance.isEmpty());
 }

 @Test
 public void testIsEmptyMethodWithEmptyInstance() {
 SparseInstance emptyInstance = new SparseInstance();
 assertTrue(emptyInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 Set<Integer> keySet = this.instance.keySet();
 assertTrue(keySet instanceof Set);
 assertTrue(keySet.getClass().isAssignableFrom(Set.class));
 assertEquals(1, keySet.size());
 }

 @Test
 public void testPutMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertEquals(VALUE_ONE, this.instance.get(KEY_ONE));
 }

 @Test(expected = NullPointerException.class)
 public void testPutMethodWithNullKey() {
 this.instance.put(null, VALUE_ONE);
 fail("Should throw NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithEmptyMap() {
 this.instance.put(null, VALUE_ONE);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testPutMethodWithNullValue() {
 this.instance.put(KEY_ONE, null);
 fail("Should throw NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithKeyAlreadyPresent() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 this.instance.put(KEY_ONE, VALUE_ONE);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testPutAllMethod() {
 Map<Integer, Double> mapToAdd = new HashMap<>();
 mapToAdd.put(KEY_ONE, VALUE_ONE);
 this.instance.putAll(mapToAdd);
 assertEquals(VALUE_ONE, this.instance.get(KEY_ONE));
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllMethodWithNullMap() {
 Map<Integer, Double> mapToAdd = null;
 this.instance.putAll(mapToAdd);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testRemoveMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 Double removedValue = this.instance.remove(KEY_ONE);
 assertEquals(VALUE_ONE, removedValue);
 assertEquals(0, this.instance.noAttributes());
 }

 @Test
 public void testRemoveMethodWithNonExistentKey() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 Double removedValue = this.instance.remove(KEY_ONE.valueOf(2));
 assertNull(removedValue);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveMethodWithNullKey() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 fail("Should throw NullPointerException");
 this.instance.remove(null);
 }

 @Test
 public void testSizeMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertEquals(1, this.instance.size());
 }

 @Test
 public void testValuesMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 Collection<Double> values = this.instance.values();
 assertTrue(values instanceof Collection);
 assertTrue(values.getClass().isAssignableFrom(Collection.class));
 assertEquals(1, values.size());
 }

 @Test
 public void testNoAttributesMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertEquals(1, this.instance.noAttributes());
 }

 @Test
 public void testRemoveAttributeMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 this.instance.removeAttribute(0);
 assertEquals(0, this.instance.noAttributes());
 }

 @Test
 public void testRemoveAttributeMethodWithNonExistentIndex() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 this.instance.removeAttribute(1);
 assertEquals(0, this.instance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeMethodWithIndexGreaterThanNoAttributes() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 fail("Should throw IndexOutOfBoundsException");
 this.instance.removeAttribute(this.instance.noAttributes());
 }

 @Test
 public void testToStringMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 assertEquals("{" + this.instance.getData() + ";" + this.instance.getClassValue() + "}",
 this.instance.toString());
 }

 @Test
 public void testHashCodeMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 Map<String, Integer> hashCodeMap = new HashMap<>();
 hashCodeMap.put("instance", 1);
 assertEquals(hashCodeMap.hashCode(), this.instance.hashCode());
 }

 @Test
 public void testEqualsMethodWithSameObject() {
 SparseInstance instanceToCompare = this.instance;
 assertTrue(this.instance.equals(instanceToCompare));
 }

 @Test
 public void testEqualsMethodWithDifferentObjectType() {
 String stringToCompare = "test";
 assertFalse(this.instance.equals(stringToCompare));
 }

 @Test
 public void testEqualsMethodWithNullObject() {
 Object nullObjectToCompare = null;
 assertFalse(this.instance.equals(nullObjectToCompare));
 }

 @Test
 public void testEqualsMethodWithDifferentClassValue() {
 SparseInstance instanceToCompare = new SparseInstance();
 instanceToCompare.setClassValue("test");
 assertFalse(this.instance.equals(instanceToCompare));
 }

 @Test
 public void testEqualsMethodWithDifferentData() {
 SparseInstance instanceToCompare = new SparseInstance();
 instanceToCompare.put(KEY_ONE, Double.valueOf(2.0));
 assertFalse(this.instance.equals(instanceToCompare));
 }

 @Test
 public void testCopyMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 SparseInstance copy = this.instance.copy();
 assertEquals(VALUE_ONE, copy.get(KEY_ONE));
 }

 @Test
 public void testRemoveAttributesMethod() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(0));
 this.instance.removeAttributes(indicesToRemove);
 assertEquals(0, this.instance.noAttributes());
 }

 @Test
 public void testRemoveAttributesMethodWithEmptySet() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 Set<Integer> emptySet = Collections.emptySet();
 this.instance.removeAttributes(emptySet);
 assertEquals(1, this.instance.noAttributes());
 }

 @Test
 public void testRemoveAttributesMethodWithNullSet() {
 this.instance.put(KEY_ONE, VALUE_ONE);
 Set<Integer> nullSet = null;
 fail("Should throw NullPointerException");
 this.instance.removeAttributes(nullSet);
 }
}