package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Before
 public void setUp() {
 sparseInstance = new SparseInstance(5, 0.0);
 }

 @Test
 public void testConstructorWithNoArguments() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithOneArgument() {
 SparseInstance sparseInstance = new SparseInstance(1);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithTwoArguments() {
 SparseInstance sparseInstance = new SparseInstance(2, 0.0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithThreeArguments() {
 SparseInstance sparseInstance = new SparseInstance(3, 0.0, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDoubleArray() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance sparseInstance = new SparseInstance(datavector);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithDoubleArrayAndDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, 0.0);
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(4, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDoubleArrayAndDefaultValueAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(4, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testValue() {
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, sparseInstance.value(0), 0.01);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 assertEquals(0.0, sparseInstance.value(5), 0.01);
 }

 @Test
 public void testClear() {
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.clear();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testContainsValue() {
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 }

 @Test
 public void testEntrySet() {
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertTrue(entrySet.size() > 0);
 }

 @Test
 public void testGet() {
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.01);
 }

 @Test
 public void testGetWithInvalidKey() {
 assertEquals(0.0, sparseInstance.get(Integer.valueOf(5)), 0.01);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 Set<Integer> keySet = sparseInstance.keySet();
 assertTrue(keySet.size() > 0);
 }

 @Test
 public void testPut() {
 Double oldValue = sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 assertEquals(1.0, oldValue.doubleValue(), 0.01);
 assertEquals(2.0, sparseInstance.value(0), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithNullKey() {
 sparseInstance.put(null, Double.valueOf(1.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithNullValue() {
 sparseInstance.put(Integer.valueOf(0), null);
 }

 @Test
 public void testPutAll() {
 SparseInstance otherSparseInstance = new SparseInstance();
 otherSparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 otherSparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));

 sparseInstance.putAll(otherSparseInstance.entrySet());
 assertEquals(2, sparseInstance.noAttributes());
 assertTrue(sparseInstance.containsKey(Integer.valueOf(1)));
 assertEquals(2.0, sparseInstance.value(1), 0.01);
 assertTrue(sparseInstance.containsKey(Integer.valueOf(2)));
 assertEquals(3.0, sparseInstance.value(2), 0.01);
 }

 @Test
 public void testRemove() {
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Double oldValue = sparseInstance.remove(Integer.valueOf(0));
 assertEquals(1.0, oldValue.doubleValue(), 0.01);
 assertFalse(sparseInstance.containsKey(Integer.valueOf(0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithInvalidKey() {
 sparseInstance.remove(Integer.valueOf(5));
 }

 @Test
 public void testSize() {
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, sparseInstance.size());
 }

 @Test(expected = Deprecated.class)
 public void testSizeDeprecated() {
 assertEquals(1, sparseInstance.size());
 }

 @Test
 public void testValues() {
 Collection<Double> values = sparseInstance.values();
 assertTrue(values.size() > 0);
 }

 @Test
 public void testNoAttributes() {
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 sparseInstance.removeAttribute(0);
 assertEquals(2, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(Integer.valueOf(0)));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithInvalidIndex() {
 sparseInstance.removeAttribute(5);
 }

 @Test
 public void testToString() {
 assertEquals("{" + sparseInstance.data.toString() + ";" + sparseInstance.getClassValue() + "}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance otherSparseInstance = new SparseInstance();
 otherSparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 otherSparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));

 int hashCode = sparseInstance.hashCode();
 SparseInstance sparseInstanceCopy = sparseInstance.copy();
 int hashCodeCopy = sparseInstanceCopy.hashCode();

 assertEquals(hashCode, hashCodeCopy);
 }

 @Test
 public void testEquals() {
 SparseInstance otherSparseInstance = new SparseInstance();
 otherSparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 otherSparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));

 assertFalse(sparseInstance.equals(otherSparseInstance));

 SparseInstance sparseInstanceCopy = sparseInstance.copy();
 assertTrue(sparseInstance.equals(sparseInstanceCopy));
 }

 @Test
 public void testCopy() {
 SparseInstance sparseInstanceCopy = sparseInstance.copy();
 assertEquals(sparseInstance.noAttributes(), sparseInstanceCopy.noAttributes());
 }

 @Test
 public void testRemoveAttributes() {
 Set<Integer> indices = new HashSet<Integer>();
 indices.addAll(Arrays.asList(0, 1, 2));
 sparseInstance.removeAttributes(indices);
 assertEquals(2, sparseInstance.noAttributes());
 }
}