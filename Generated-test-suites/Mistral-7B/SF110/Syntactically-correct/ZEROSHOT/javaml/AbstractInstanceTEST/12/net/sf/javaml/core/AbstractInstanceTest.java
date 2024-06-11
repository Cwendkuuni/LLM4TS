package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private static final int NUM_ATTRIBUTES = 3;

 @Test
 public void testAbstractInstanceConstructor() {
 AbstractInstance instance = new AbstractInstance();
 assertEquals(0, instance.getID());
 assertNull(instance.classValue());
 assertEquals(NUM_ATTRIBUTES, instance.noAttributes());

 AbstractInstance instanceWithClassValue = new AbstractInstance("testClassValue");
 assertEquals("testClassValue", instanceWithClassValue.classValue());
 }

 @Test
 public void testAbstractInstanceGetID() {
 AbstractInstance instance = new AbstractInstance();
 assertEquals(0, instance.getID());

 AbstractInstance instanceWithID = new AbstractInstance(1);
 assertEquals(1, instanceWithID.getID());
 }

 @Test
 public void testAbstractInstanceGetClassValue() {
 AbstractInstance instance = new AbstractInstance();
 assertNull(instance.classValue());

 AbstractInstance instanceWithClassValue = new AbstractInstance("testClassValue");
 assertEquals("testClassValue", instanceWithClassValue.classValue());
 }

 @Test
 public void testAbstractInstanceSetClassValue() {
 AbstractInstance instance = new AbstractInstance();
 assertNull(instance.classValue());

 instance.setClassValue("testClassValue");
 assertEquals("testClassValue", instance.classValue());
 }

 @Test
 public void testAbstractInstanceMinusInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0, 1.5});
 AbstractInstance result = instance1.minus(instance2);

 Map<Integer, Double> map1 = new HashMap<>();
 map1.put(0, 0.5);
 map1.put(1, 1.0);
 map1.put(2, 1.5);

 Map<Integer, Double> map2 = new HashMap<>();
 map2.putAll(map1);
 map2.put(0, -0.5);
 map2.put(1, 0.0);
 map2.put(2, 0.5);

 Map<Integer, Double> expectedMap = map2;
 Map<Integer, Double> actualMap = result.asMap();

 assertEquals(expectedMap.size(), actualMap.size());
 for (Map.Entry<Integer, Double> entry : expectedMap.entrySet()) {
 assertEquals(entry.getValue(), actualMap.get(entry.getKey()));
 }
 }

 @Test
 public void testAbstractInstanceMinusDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.minus(2.0);

 Map<Integer, Double> expectedMap = new HashMap<>(Arrays.asList(1.0, 1.0, 1.0));
 Map<Integer, Double> actualMap = result.asMap();

 assertEquals(expectedMap.size(), actualMap.size());
 for (Map.Entry<Integer, Double> entry : expectedMap.entrySet()) {
 assertEquals(entry.getValue(), actualMap.get(entry.getKey()));
 }
 }

 @Test
 public void testAbstractInstanceDivideDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.divide(2.0);

 Map<Integer, Double> expectedMap = new HashMap<>(Collections.singletonMap(0, 0.5));
 Map<Integer, Double> actualMap = result.asMap();

 assertEquals(expectedMap.size(), actualMap.size());
 assertEquals(expectedMap.get(0), actualMap.get(0));
 }

 @Test
 public void testAbstractInstanceMultiplyDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.multiply(2.0);

 Map<Integer, Double> expectedMap = new HashMap<>(Collections.singletonMap(0, 2.0));
 for (int i = 1; i < NUM_ATTRIBUTES; i++) {
 expectedMap.put(i, 2.0 * instance.value(i - 1));
 }

 Map<Integer, Double> actualMap = result.asMap();

 assertEquals(expectedMap.size(), actualMap.size());
 for (Map.Entry<Integer, Double> entry : expectedMap.entrySet()) {
 assertEquals(entry.getValue(), actualMap.get(entry.getKey()));
 }
 }

 @Test
 public void testAbstractInstanceHashCode() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.1, 3.0});

 assertEquals(instance1.getID(), instance1.hashCode());
 assertEquals(instance1.hashCode(), instance1.hashCode());

 assertEquals(instance1.hashCode(), instance2.hashCode());

 assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testAbstractInstanceEqualsObjectNull() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(instance.equals(null));
 }

 @Test
 public void testAbstractInstanceEqualsObjectNotInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(instance.equals(new Object()));
 }

 @Test
 public void testAbstractInstanceEqualsObjectSameInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance.equals(instance));
 }

 @Test
 public void testAbstractInstanceEqualsObjectDifferentInstanceSameValues() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testAbstractInstanceEqualsObjectDifferentInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.1, 2.1, 3.1});
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testAbstractInstanceEqualsObjectDifferentClass() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(instance.equals(new String("test")));
 }

 @Test
 public void testAbstractInstanceMinusInstanceWithDifferentSize() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
 try {
 instance1.minus(instance2);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAbstractInstanceMinusDoubleWithDifferentSize() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 try {
 instance.minus(-1.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAbstractInstanceDivideInstanceWithDifferentSize() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
 try {
 instance1.divide(instance2);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAbstractInstanceDivideDoubleZero() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 try {
 instance.divide(0.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAbstractInstanceMultiplyInstanceWithDifferentSize() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
 try {
 instance1.multiply(instance2);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAbstractInstanceMultiplyDoubleZero() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 try {
 instance.multiply(0.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAbstractInstanceAddInstanceWithDifferentSize() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
 try {
 instance1.add(instance2);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAbstractInstanceAddDoubleNegative() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 try {
 instance.add(-1.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAbstractInstanceAddDoubleAndInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0, 1.5});
 AbstractInstance result = instance1.add(instance2);

 Map<Integer, Double> map1 = new HashMap<>();
 map1.putAll(instance1.asMap());

 Map<Integer, Double> map2 = new HashMap<>();
 map2.putAll(instance2.asMap());

 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 expectedMap.put(i, map1.get(i) + map2.get(i));
 }

 Map<Integer, Double> actualMap = result.asMap();

 assertEquals(expectedMap.size(), actualMap.size());
 for (Map.Entry<Integer, Double> entry : expectedMap.entrySet()) {
 assertEquals(entry.getValue(), actualMap.get(entry.getKey()));
 }
 }

 @Test
 public void testAbstractInstanceAddDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.add(1.0);

 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 expectedMap.put(i, instance.value(i) + 1.0);
 }

 Map<Integer, Double> actualMap = result.asMap();

 assertEquals(expectedMap.size(), actualMap.size());
 for (Map.Entry<Integer, Double> entry : expectedMap.entrySet()) {
 assertEquals(entry.getValue(), actualMap.get(entry.getKey()));
 }
 }

 @Test
 public void testAbstractInstanceSqrt() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0});
 AbstractInstance result = instance.sqrt();

 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 expectedMap.put(i, Math.sqrt(instance.value(i)));
 }

 Map<Integer, Double> actualMap = result.asMap();

 assertEquals(expectedMap.size(), actualMap.size());
 for (Map.Entry<Integer, Double> entry : expectedMap.entrySet()) {
 assertEquals(entry.getValue(), actualMap.get(entry.getKey()), 0.001);
 }
 }
}