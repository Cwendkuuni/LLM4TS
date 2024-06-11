package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;
 private static final Object CLASS_VALUE = "testClass";

 @Test
 public void testSparseInstanceWithDefaultConstructor() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntConstructor() {
 SparseInstance sparseInstance = new SparseInstance(5);
 assertEquals(5, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntAndDefaultValueConstructor() {
 SparseInstance sparseInstance = new SparseInstance(5, DEFAULT_VALUE);
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals(DEFAULT_VALUE, sparseInstance.get(0), 0.001);
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntAndClassValueConstructor() {
 SparseInstance sparseInstance = new SparseInstance(5, CLASS_VALUE);
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals(CLASS_VALUE, sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntAndDefaultValueAndClassValueConstructor() {
 SparseInstance sparseInstance = new SparseInstance(5, DEFAULT_VALUE, CLASS_VALUE);
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals(DEFAULT_VALUE, sparseInstance.get(0), 0.001);
 assertEquals(CLASS_VALUE, sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDoubleArrayConstructor() {
 double[] datavector = new double[] {1.0, 2.0, 3.0};
 SparseInstance sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 assertEquals(3.0, sparseInstance.get(2), 0.001);
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndDefaultValueConstructor() {
 double[] datavector = new double[] {1.0, 2.0, 3.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, DEFAULT_VALUE);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 assertEquals(3.0, sparseInstance.get(2), 0.001);
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndClassValueConstructor() {
 double[] datavector = new double[] {1.0, 2.0, 3.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, CLASS_VALUE);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(CLASS_VALUE, sparseInstance.getClassValue());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 assertEquals(3.0, sparseInstance.get(2), 0.001);
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndDefaultValueAndClassValueConstructor() {
 double[] datavector = new double[] {1.0, 2.0, 3.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, DEFAULT_VALUE, CLASS_VALUE);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(DEFAULT_VALUE, sparseInstance.get(0), 0.001);
 assertEquals(DEFAULT_VALUE, sparseInstance.get(1), 0.001);
 assertEquals(DEFAULT_VALUE, sparseInstance.get(2), 0.001);
 assertEquals(CLASS_VALUE, sparseInstance.getClassValue());
 }

 @Test
 public void testValueMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 assertEquals(2.0, sparseInstance.value(1), 0.001);
 assertEquals(3.0, sparseInstance.value(2), 0.001);
 }

 @Test
 public void testClearMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKeyMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsKey(0));
 assertTrue(sparseInstance.containsKey(1));
 assertTrue(sparseInstance.containsKey(2));
 assertFalse(sparseInstance.containsKey(3));
 }

 @Test
 public void testContainsValueMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsValue(1.0));
 assertTrue(sparseInstance.containsValue(2.0));
 assertTrue(sparseInstance.containsValue(3.0));
 }

 @Test
 public void testEntrySetMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 Map<Map.Entry<Integer, Double>, Integer> expectedMap = new HashMap<>();
 List<Map.Entry<Integer, Double>> expectedList = Arrays.asList(
         Map.entry(0, 1.0),
         Map.entry(1, 2.0),
         Map.entry(2, 3.0)
 );
 Map<Map.Entry<Integer, Double>, Integer> actualMap = new HashMap<>();
 List<Map.Entry<Integer, Double>> actualList = new ArrayList<>();
 for (Map.Entry<Integer, Double> entry : sparseInstance.entrySet()) {
 actualMap.put(entry, 0);
 actualList.add(entry);
 }
 assertEquals(expectedMap, actualMap);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testGetMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 assertEquals(3.0, sparseInstance.get(2), 0.001);
 assertEquals(DEFAULT_VALUE, sparseInstance.get(3), 0.001);
 }

 @Test
 public void testIsEmptyMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertFalse(sparseInstance.isEmpty());
 SparseInstance emptySparseInstance = new SparseInstance();
 assertTrue(emptySparseInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 TreeSet<Integer> expectedSet = new TreeSet<>(Arrays.asList(0, 1, 2));
 TreeSet<Integer> actualSet = new TreeSet<>(sparseInstance.keySet());
 assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testPutMethod() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 }

 @Test
 public void testPutAllMethod() {
 SparseInstance sparseInstance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 }

 @Test
 public void testRemoveMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 Double removedValue = sparseInstance.remove(0);
 assertEquals(2.0, removedValue.doubleValue(), 0.001);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSizeMethodDeprecated() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 int size = sparseInstance.size();
 fail("Size method should be deprecated and not accessible");
 }

 @Test
 public void testValuesMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 List<Double> expectedList = Arrays.asList(1.0, 2.0, 3.0);
 List<Double> actualList = new ArrayList<>(sparseInstance.values());
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testNoAttributesMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, sparseInstance.noAttributes());
 SparseInstance emptySparseInstance = new SparseInstance();
 assertEquals(0, emptySparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributeMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 sparseInstance.removeAttribute(1);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertEquals(3.0, sparseInstance.get(2), 0.001);
 }

 @Test
 public void testToStringMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 String expectedString = "{1.0=1.0, 2.0=2.0, 3.0=3.0;}";
 String actualString = sparseInstance.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testHashCodeMethod() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 SparseInstance sparseInstance3 = new SparseInstance(new double[] {1.1, 2.1, 3.1});
 int expectedHashCode1 = 384716723;
 int expectedHashCode2 = expectedHashCode1;
 int actualHashCode1 = sparseInstance1.hashCode();
 int actualHashCode2 = sparseInstance2.hashCode();
 int actualHashCode3 = sparseInstance3.hashCode();
 assertEquals(expectedHashCode1, actualHashCode1);
 assertEquals(expectedHashCode2, actualHashCode2);
 assertNotEquals(expectedHashCode1, actualHashCode3);
 }

 @Test
 public void testEqualsMethod() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 SparseInstance sparseInstance3 = new SparseInstance(new double[] {1.1, 2.1, 3.1});
 assertTrue(sparseInstance1.equals(sparseInstance2));
 assertFalse(sparseInstance1.equals(sparseInstance3));
 }

 @Test
 public void testCopyMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 SparseInstance copiedSparseInstance = sparseInstance.copy();
 assertEquals(sparseInstance, copiedSparseInstance);
 }

 @Test
 public void testRemoveAttributesMethod() {
 SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 Set<Integer> indices = new HashSet<>(Arrays.asList(1, 2));
 sparseInstance.removeAttributes(indices);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertEquals(3.0, sparseInstance.get(1), 0.001);
 }
}