package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

 AbstractInstance inst1, inst2, inst3;

 @Test
 public void testAbstractInstanceConstructor() {
 inst1 = new AbstractInstance();
 inst2 = new AbstractInstance("Class Value");
 assertEquals(0, inst1.getID());
 assertEquals("Class Value", inst2.getClassValue().toString());
 }

 @Test
 public void testAbstractInstanceID() {
 inst1 = new AbstractInstance();
 inst2 = new AbstractInstance();
 inst3 = new AbstractInstance();
 assertEquals(0, inst1.getID());
 assertEquals(1, inst2.getID());
 assertEquals(2, inst3.getID());
 }

 @Test
 public void testAbstractInstanceIterator() {
 inst1 = new AbstractInstance();
 inst1.put(0, 1.0);
 inst1.put(1, 2.0);
 Iterator<Double> it = inst1.iterator();
 assertTrue(it.hasNext());
 assertEquals(1.0, it.next(), 0);
 assertTrue(it.hasNext());
 assertEquals(2.0, it.next(), 0);
 assertFalse(it.hasNext());
 }

 @Test
 public void testAbstractInstanceSetClassValue() {
 inst1 = new AbstractInstance();
 inst1.setClassValue("Class Value");
 assertEquals("Class Value", inst1.getClassValue().toString());
 }

 @Test
 public void testAbstractInstanceMinusDouble() {
 inst1 = new AbstractInstance();
 inst1.put(0, 5.0);
 inst1.put(1, 3.0);
 Instance result = inst1.minus(2.0);
 Double[] expected = new Double[]{3.0, 1.0};
 assertArrayEquals(expected, result.toArray(), 0.0);
 }

 @Test
 public void testAbstractInstanceMinusInstance() {
 inst1 = new AbstractInstance();
 inst1.put(0, 5.0);
 inst1.put(1, 3.0);
 inst2 = new AbstractInstance();
 inst2.put(0, 2.0);
 inst2.put(1, 1.0);
 Instance result = inst1.minus(inst2);
 Double[] expected = new Double[]{3.0, 2.0};
 assertArrayEquals(expected, result.toArray(), 0.0);
 }

 @Test
 public void testAbstractInstanceDivideDouble() {
 inst1 = new AbstractInstance();
 inst1.put(0, 10.0);
 inst1.put(1, 5.0);
 Instance result = inst1.divide(2.5);
 Double[] expected = new Double[]{4.0, 2.0};
 assertArrayEquals(expected, result.toArray(), 0.0);
 }

 @Test
 public void testAbstractInstanceDivideInstance() {
 inst1 = new AbstractInstance();
 inst1.put(0, 10.0);
 inst1.put(1, 5.0);
 inst2 = new AbstractInstance();
 inst2.put(0, 2.5);
 inst2.put(1, 1.25);
 Instance result = inst1.divide(inst2);
 Double[] expected = new Double[]{3.9166666666666665, 4.0};
 assertArrayEquals(expected, result.toArray(), 0.0);
 }

 @Test
 public void testAbstractInstanceMultiplyDouble() {
 inst1 = new AbstractInstance();
 inst1.put(0, 2.0);
 inst1.put(1, 3.0);
 Instance result = inst1.multiply(2.5);
 Double[] expected = new Double[]{5.0, 7.5};
 assertArrayEquals(expected, result.toArray(), 0.0);
 }

 @Test
 public void testAbstractInstanceMultiplyInstance() {
 inst1 = new AbstractInstance();
 inst1.put(0, 2.0);
 inst1.put(1, 3.0);
 inst2 = new AbstractInstance();
 inst2.put(0, 1.5);
 inst2.put(1, 2.5);
 Instance result = inst1.multiply(inst2);
 Double[] expected = new Double[]{3.0, 7.5};
 assertArrayEquals(expected, result.toArray(), 0.0);
 }

 @Test
 public void testAbstractInstanceSubtractFromHashCode() {
 inst1 = new AbstractInstance();
 inst1.put(0, 10L);
 inst1.put(1, 5L);
 inst2 = new AbstractInstance();
 inst2.put(0, 5L);
 inst2.put(1, 2L);
 assertEquals(inst1.hashCode(), inst1.hashCode());
 inst1 = inst1.subtract(inst2);
 assertEquals(inst1.hashCode(), 11);
 }

 @Test
 public void testAbstractInstanceEquals() {
 inst1 = new AbstractInstance();
 inst1.put(0, 1.0);
 inst1.put(1, 2.0);
 inst2 = new AbstractInstance();
 inst2.put(0, 1.0);
 inst2.put(1, 2.0);
 assertTrue(inst1.equals(inst2));
 inst3 = new AbstractInstance();
 inst3.put(0, 1.0);
 inst3.put(1, 3.0);
 assertFalse(inst1.equals(inst3));
 inst4 = new AbstractInstance();
 inst4.put(0, 2.0);
 inst4.put(1, 2.0);
 assertFalse(inst1.equals(inst4));
 }

 @Test
 public void testAbstractInstanceAddDouble() {
 inst1 = new AbstractInstance();
 inst1.put(0, 1.0);
 inst1.put(1, 2.0);
 Instance result = inst1.add(3.0);
 Double[] expected = new Double[]{4.0, 5.0};
 assertArrayEquals(expected, result.toArray(), 0.0);
 }

 @Test
 public void testAbstractInstanceAddInstance() {
 inst1 = new AbstractInstance();
 inst1.put(0, 1.0);
 inst1.put(1, 2.0);
 inst2 = new AbstractInstance();
 inst2.put(0, 2.0);
 inst2.put(1, 3.0);
 Instance result = inst1.add(inst2);
 Double[] expected = new Double[]{3.0, 5.0};
 assertArrayEquals(expected, result.toArray(), 0.0);
 }

 @Test
 public void testAbstractInstanceSqrt() {
 inst1 = new AbstractInstance();
 inst1.put(0, 9.0);
 inst1.put(1, 16.0);
 Instance result = inst1.sqrt();
 Double[] expected = new Double[]{3.0, 4.0};
 assertArrayEquals(expected, result.toArray(), 0.0);
 }

 @Test
 public void testAbstractInstanceStaticInitialization() {
 int nextID = AbstractInstance.nextID;
 assertEquals(0, nextID);
 }
}