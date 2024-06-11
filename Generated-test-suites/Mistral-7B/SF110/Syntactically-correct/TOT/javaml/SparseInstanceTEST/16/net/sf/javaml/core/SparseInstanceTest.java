package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SparseInstanceTest {

 // Test constructor with no arguments
 @Test
 public void testEmptySparseInstance() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 // Test constructor with int argument
 @Test
 public void testSparseInstanceWithIntArgument() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 }

 // Test constructor with defaultValue and int argument
 @Test
 public void testSparseInstanceWithDefaultValueAndIntArgument() {
 SparseInstance instance = new SparseInstance(5, 0.5);
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 }

 // Test constructor with classValue and int argument
 @Test
 public void testSparseInstanceWithClassValueAndIntArgument() {
 SparseInstance instance = new SparseInstance(5, "StringClassValue");
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 }

 // Test constructor with defaultValue, classValue and int argument
 @Test
 public void testSparseInstanceWithDefaultValueClassValueAndIntArgument() {
 SparseInstance instance = new SparseInstance(5, 0.5, "StringClassValue");
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 }

 // Test constructor with double array
 @Test
 public void testSparseInstanceWithDoubleArray() {
 Double[] dataVector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(dataVector);
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 }

 // Test constructor with defaultValue and double array
 @Test
 public void testSparseInstanceWithDefaultValueAndDoubleArray() {
 Double[] dataVector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(dataVector, 0.5);
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 }

 // Test constructor with classValue and double array
 @Test
 public void testSparseInstanceWithClassValueAndDoubleArray() {
 Double[] dataVector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(dataVector, "StringClassValue");
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 }

 // Test constructor with defaultValue, classValue and double array
 @Test
 public void testSparseInstanceWithDefaultValueClassValueAndDoubleArray() {
 Double[] dataVector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(dataVector, 0.5, "StringClassValue");
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 }

 // Test get method
 @Test
 public void testGetMethod() {
 SparseInstance instance = new SparseInstance(5, 0.5, "StringClassValue");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(3), Double.valueOf(3.0));
 assertEquals(1.0, instance.get(Integer.valueOf(0)), 0.001);
 assertEquals(3.0, instance.get(Integer.valueOf(3)), 0.001);
 assertEquals(0.5, instance.get(Integer.valueOf(1)), 0.001);
 assertEquals(0.5, instance.get(Integer.valueOf(2)), 0.001);
 assertEquals(0.5, instance.get(Integer.valueOf(4)), 0.001);
 }

 // Test put method
 @Test
 public void testPutMethod() {
 SparseInstance instance = new SparseInstance(5, 0.5, "StringClassValue");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(3), Double.valueOf(3.0));
 Map<Integer, Double> expectedMap = new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(3, 3.0);
 put(1, 0.5);
 put(2, 0.5);
 put(4, 0.5);
 }};
 Map<Integer, Double> actualMap = new HashMap<Integer, Double>();
 for (Map.Entry<Integer, Double> entry : instance.entrySet()) {
 actualMap.put(entry.getKey(), entry.getValue());
 }
 assertEquals(expectedMap, actualMap);
 }

 // Test clear method
 @Test
 public void testClearMethod() {
 SparseInstance instance = new SparseInstance(5, 0.5, "StringClassValue");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(3), Double.valueOf(3.0));
 instance.clear();
 assertTrue(instance.isEmpty());
 }

 // Test containsKey method
 @Test
 public void testContainsKeyMethod() {
 SparseInstance instance = new SparseInstance(5, 0.5, "StringClassValue");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(3), Double.valueOf(3.0));
 assertTrue(instance.containsKey(Integer.valueOf(0)));
 assertTrue(instance.containsKey(Integer.valueOf(3)));
 assertFalse(instance.containsKey(Integer.valueOf(4)));
 }

 // Test containsValue method
 @Test
 public void testContainsValueMethod() {
 SparseInstance instance = new SparseInstance(5, 0.5, "StringClassValue");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(3), Double.valueOf(3.0));
 assertTrue(instance.containsValue(Double.valueOf(1.0)));
 assertTrue(instance.containsValue(Double.valueOf(3.0)));
 assertFalse(instance.containsValue(Double.valueOf(4.0)));
 }

 // Test entrySet method
 @Test
 public void testEntrySetMethod() {
 SparseInstance instance = new SparseInstance(5, 0.5, "StringClassValue");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(3), Double.valueOf(3.0));
 Set<Map.Entry<Integer, Double>> expectedSet = new HashSet<Map.Entry<Integer, Double>>() {{
 add(new AbstractMap.SimpleEntry<Integer, Double>(0, 1.0));
 add(new AbstractMap.SimpleEntry<Integer, Double>(3, 3.0));
 }};
 Set<Map.Entry<Integer, Double>> actualSet = new HashSet<Map.Entry<Integer, Double>>(instance.entrySet());
 assertEquals(expectedSet, actualSet);
 }

 // Test noAttributes method
 @Test
 public void testNoAttributesMethod() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, instance.noAttributes());
 }

 // Test removeAttribute method with valid index
 @Test
 public void testRemoveAttributeWithValidIndex() {
 SparseInstance instance = new SparseInstance(5, 0.5, "StringClassValue");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(3), Double.valueOf(3.0));
 instance.removeAttribute(0);
 assertEquals(4, instance.noAttributes());
 Map<Integer, Double> expectedMap = new HashMap<Integer, Double>() {{
 put(3, 3.0);
 }};
 Map<Integer, Double> actualMap = new HashMap<Integer, Double>();
 for (Map.Entry<Integer, Double> entry : instance.entrySet()) {
 actualMap.put(entry.getKey(), entry.getValue());
 }
 assertEquals(expectedMap, actualMap);
 }

 // Test removeAttribute method with invalid index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithInvalidIndex() {
 SparseInstance instance = new SparseInstance(5, 0.5, "StringClassValue");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(3), Double.valueOf(3.0));
 instance.removeAttribute(6);
 }
}