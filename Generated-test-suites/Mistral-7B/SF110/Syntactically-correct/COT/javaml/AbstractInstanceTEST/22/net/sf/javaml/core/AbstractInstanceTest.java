package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.*;

import org.junit.Test;

public class AbstractInstanceTest {

 private static final int NUM_ATTRIBUTES = 5;

 @Test
 public void testConstructorNoArguments() {
 AbstractInstance instance = new AbstractInstance();
 assertEquals(0, instance.getID());
 assertNull(instance.classValue());
 }

 @Test
 public void testConstructorWithArguments() {
 Object classValue = new Object();
 AbstractInstance instance = new AbstractInstance(classValue);
 assertEquals(1, instance.getID());
 assertEquals(classValue, instance.classValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeID() {
 AbstractInstance instance = new AbstractInstance();
 instance.ID = -1;
 }

 @Test
 public void testGetID() {
 AbstractInstance instance = new AbstractInstance();
 assertEquals(0, instance.getID());
 instance.ID = 10;
 assertEquals(10, instance.getID());
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance.put(String.valueOf(i), (double)i);
 }
 InstanceIterator iterator = instance.iterator();
 int i = 0;
 while (iterator.hasNext()) {
 Double next = iterator.next();
 assertEquals((double)i++, next);
 }
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance1.put(String.valueOf(i), (double)i);
 instance2.put(String.valueOf(i), (double)(i + 1));
 }
 AbstractInstance result = instance1.minus(instance2);
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 assertEquals((double)i, result.get(String.valueOf(i)));
 }
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance instance = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance.put(String.valueOf(i), (double)i);
 }
 AbstractInstance result = instance.minus(1.0);
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 assertEquals((double)(i - 1), result.get(String.valueOf(i)));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMinusWithDouble() {
 AbstractInstance instance = new AbstractInstance();
 instance.minus(-1.0);
 }

 @Test
 public void testDivideWithDouble() {
 AbstractInstance instance = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance.put(String.valueOf(i), (double)(i + 1));
 }
 AbstractInstance result = instance.divide(1.0);
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 assertEquals((double)(i + 1), result.get(String.valueOf(i)), 0.001);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithZero() {
 AbstractInstance instance = new AbstractInstance();
 instance.divide(0.0);
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance instance = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance.put(String.valueOf(i), (double)(i + 1));
 }
 AbstractInstance result = instance.multiply(2.0);
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 assertEquals((double)(i + 1) * 2, result.get(String.valueOf(i)), 0.001);
 }
 }

 @Test
 public void testEquals() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance1.put(String.valueOf(i), (double)i);
 instance2.put(String.valueOf(i), (double)i);
 }
 assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testNotEquals() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance1.put(String.valueOf(i), (double)i);
 instance2.put(String.valueOf(i), (double)(i + 1));
 }
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testHashCode() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance1.put(String.valueOf(i), (double)i);
 instance2.put(String.valueOf(i), (double)i);
 }
 assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testDifferentHashCodes() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 instance1.put(String.valueOf(0), 1.0);
 instance2.put(String.valueOf(0), 2.0);
 assertNotSame(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance1.put(String.valueOf(i), (double)(i + 1));
 instance2.put(String.valueOf(i), (double)(i + 1));
 }
 AbstractInstance result = instance1.multiply(instance2);
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 assertEquals((double)(i + 1) * (i + 1), result.get(String.valueOf(i)), 0.001);
 }
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance1.put(String.valueOf(i), (double)(i + 1));
 instance2.put(String.valueOf(i), (double)(i + 2));
 }
 AbstractInstance result = instance1.divide(instance2);
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 assertEquals((double)(i + 1) / (i + 2), result.get(String.valueOf(i)), 0.001);
 }
 }

 @Test
 public void testAddWithDouble() {
 AbstractInstance instance = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance.put(String.valueOf(i), (double)i);
 }
 AbstractInstance result = instance.add(1.0);
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 assertEquals((double)(i + 1), result.get(String.valueOf(i)));
 }
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance1.put(String.valueOf(i), (double)i);
 instance2.put(String.valueOf(i), (double)(i + 1));
 }
 AbstractInstance result = instance1.add(instance2);
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 assertEquals((double)(i + 1 + i), result.get(String.valueOf(i)));
 }
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = new AbstractInstance();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 instance.put(String.valueOf(i), (double)(i * i));
 }
 AbstractInstance result = instance.sqrt();
 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 assertEquals(Math.sqrt((double)(i * i)), result.get(String.valueOf(i)), 0.001);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeValue() {
 AbstractInstance instance = new AbstractInstance();
 instance.put(String.valueOf(0), -1.0);
 instance.sqrt();
 }

}