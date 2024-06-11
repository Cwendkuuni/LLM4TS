package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DELTA = 0.001;

 @Test
 public void testConstructorWithNoArguments() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance sparseInstance = new SparseInstance(0);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValue() {
 SparseInstance sparseInstance = new SparseInstance(0, "StringClassValue");
 assertEquals("StringClassValue", sparseInstance.classValue());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValueAndDatavector() {
 SparseInstance sparseInstance = new SparseInstance(new double[0]);
 assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValueAndDatavectorAndInit() {
 SparseInstance sparseInstance = new SparseInstance(new double[0], 0.5, "StringClassValue");
 assertEquals(0, sparseInstance.noAttributes());
 assertEquals(0.5, sparseInstance.defaultValue, DELTA);
 assertEquals("StringClassValue", sparseInstance.classValue());
 }

 @Test
 public void testConstructorWithDatavector() {
 double[] datavector = {1.1, 2.2, 3.3, 4.4};
 SparseInstance sparseInstance = new SparseInstance(datavector);
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithDatavectorAndDefaultValue() {
 double[] datavector = {1.1, 2.2, 3.3, 4.4};
 SparseInstance sparseInstance = new SparseInstance(datavector, 5.5);
 assertEquals(4, sparseInstance.noAttributes());
 assertEquals(5.5, sparseInstance.defaultValue, DELTA);
 }

 @Test
 public void testConstructorWithDatavectorAndDefaultValueAndClassValue() {
 double[] datavector = {1.1, 2.2, 3.3, 4.4};
 SparseInstance sparseInstance = new SparseInstance(datavector, 5.5, "StringClassValue");
 assertEquals(4, sparseInstance.noAttributes());
 assertEquals(5.5, sparseInstance.defaultValue, DELTA);
 assertEquals("StringClassValue", sparseInstance.classValue());
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance(new double[0]);
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeNumber() {
 SparseInstance sparseInstance = new SparseInstance(new double[0]);
 sparseInstance.setNoAttributes(-1);
 }

 @Test
 public void testValue() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 assertEquals(1.1, sparseInstance.value(0), DELTA);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueNegativeIndex() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 sparseInstance.value(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueMaxIndex() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 sparseInstance.value(4);
 }

 @Test
 public void testClear() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsKeyNullKey() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 sparseInstance.containsKey(null);
 }

 @Test
 public void testContainsValue() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 assertTrue(sparseInstance.containsValue(Double.valueOf(1.1)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsValueNullValue() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 sparseInstance.containsValue(null);
 }

 @Test
 public void testEntrySet() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(4, entrySet.size());
 }

 @Test
 public void testGet() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 assertEquals(1.1, sparseInstance.get(Integer.valueOf(0)), DELTA);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNullKey() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 sparseInstance.get(null);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 Set<Integer> keySet = sparseInstance.keySet();
 assertEquals(4, keySet.size());
 }

 @Test
 public void testPut() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(null, sparseInstance.get(Integer.valueOf(0)));
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.1));
 assertEquals(1.1, sparseInstance.get(Integer.valueOf(0)), DELTA);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKey() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(null, Double.valueOf(1.1));
 }

 @Test
 public void testPutSameKeyTwice() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.1));
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.2));
 assertEquals(2.2, sparseInstance.get(Integer.valueOf(0)), DELTA);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<Integer, Double>() {{
 put(Integer.valueOf(0), Double.valueOf(1.1));
 put(Integer.valueOf(1), Double.valueOf(2.2));
 put(Integer.valueOf(2), Double.valueOf(3.3));
 put(Integer.valueOf(3), Double.valueOf(4.4));
 }};
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testRemove() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 assertEquals(1.1, sparseInstance.get(Integer.valueOf(0)), DELTA);
 sparseInstance.remove(Integer.valueOf(0));
 assertEquals(null, sparseInstance.get(Integer.valueOf(0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNullKey() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 sparseInstance.remove(null);
 }

 @Test
 public void testSize() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 assertEquals(4, sparseInstance.size());
 }

 @Deprecated
 @Test
 public void testSizeDeprecated() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 assertEquals(4, sparseInstance.size());
 }

 @Test
 public void testValues() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 Collection<Double> values = sparseInstance.values();
 assertEquals(4, values.size());
 }

 @Test
 public void testNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 sparseInstance.removeAttribute(0);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeNegativeIndex() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 sparseInstance.removeAttribute(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeMaxIndex() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 sparseInstance.removeAttribute(4);
 }

 @Test
 public void testToString() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 assertEquals("{" + Arrays.toString(new double[0]) + ";" + "ObjectClassValue" + "}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 SparseInstance sparseInstance2 = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 SparseInstance sparseInstance3 = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 SparseInstance sparseInstance4 = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 SparseInstance sparseInstance5 = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4}, "StringClassValue");
 assertNotSame(sparseInstance1.hashCode(), sparseInstance2.hashCode());
 assertNotSame(sparseInstance1.hashCode(), sparseInstance3.hashCode());
 assertNotSame(sparseInstance1.hashCode(), sparseInstance4.hashCode());
 assertNotSame(sparseInstance1.hashCode(), sparseInstance5.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 SparseInstance sparseInstance2 = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 SparseInstance sparseInstance3 = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 SparseInstance sparseInstance4 = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4}, "StringClassValue");
 assertTrue(sparseInstance1.equals(sparseInstance1));
 assertTrue(sparseInstance1.equals(sparseInstance2));
 assertTrue(sparseInstance1.equals(sparseInstance3));
 assertFalse(sparseInstance1.equals(sparseInstance4));
 }

 @Test
 public void testCopy() {
 SparseInstance original = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 SparseInstance copy = original.copy();
 assertEquals(original.noAttributes(), copy.noAttributes());
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 Set<Integer> indices = new HashSet<Integer>(Arrays.asList(0, 2));
 sparseInstance.removeAttributes(indices);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributesEmptySet() {
 SparseInstance sparseInstance = new SparseInstance(new double[0]);
 Set<Integer> indices = new HashSet<Integer>();
 sparseInstance.removeAttributes(indices);
 }

 @Test
 public void testRemoveAttributesNullSet() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.1, 2.2, 3.3, 4.4});
 Set<Integer> indices = null;
 sparseInstance.removeAttributes(indices);
 }

}