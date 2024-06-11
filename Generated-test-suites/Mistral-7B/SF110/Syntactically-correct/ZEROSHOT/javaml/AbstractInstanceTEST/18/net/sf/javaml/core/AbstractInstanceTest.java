package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private AbstractInstance instance4;

 @org.junit.Before
 public void setUp() {
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance2 = new DenseInstance(new double[]{1.1, 2.1, 3.1});
 instance3 = new DenseInstance(new double[]{1.2, 2.2, 3.2}, "ClassLabel");
 instance4 = new DenseInstance(new double[]{1.3, 2.3, 3.3}, "ClassLabel");
 }

 @org.junit.Test
 public void testConstructor() {
 AbstractInstance abstractInstance = new AbstractInstance();
 assertNull(abstractInstance.classValue);
 abstractInstance.ID = 1;
 assertEquals(1, abstractInstance.getID());
 }

 @org.junit.Test
 public void testConstructorWithClassValue() {
 AbstractInstance abstractInstance = new AbstractInstance("ClassLabel");
 assertNull(abstractInstance.classValue);
 abstractInstance.ID = 1;
 assertEquals(1, abstractInstance.getID());
 assertNotNull(abstractInstance.classValue);
 assertEquals("ClassLabel", abstractInstance.classValue().toString());
 }

 @org.junit.Test
 public void testGetID() {
 assertEquals(instance1.getID(), instance1.getID());
 assertNotSame(instance1.getID(), instance2.getID());
 }

 @org.junit.Test
 public void testIterator() {
 List<Double> list = new ArrayList<>();
 for (Double value : instance1) {
 list.add(value);
 }
 assertEquals(instance1.noAttributes(), list.size());
 for (int i = 0; i < instance1.noAttributes(); ++i) {
 assertEquals(instance1.value(i), list.get(i));
 }
 }

 @org.junit.Test
 public void testMinusWithInstance() {
 AbstractInstance result = instance1.minus(instance2);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(instance1.value(i) - instance2.value(i), result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testMinusWithDouble() {
 AbstractInstance result = instance1.minus(1.0);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(instance1.value(i) - 1.0, result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testDivideWithDouble() {
 AbstractInstance result = instance1.divide(2.0);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(instance1.value(i) / 2.0, result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testMultiplyWithDouble() {
 AbstractInstance result = instance1.multiply(2.0);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(instance1.value(i) * 2.0, result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testEquals() {
 assertTrue(instance1.equals(instance1));
 assertFalse(instance1.equals(instance2));
 }

 @org.junit.Test
 public void testHashCode() {
 assertEquals(instance1.hashCode(), instance1.hashCode());
 assertNotSame(instance1.hashCode(), instance2.hashCode());
 }

 @org.junit.Test
 public void testMinusWithInstanceAndResultSubtraction() {
 AbstractInstance result = instance1.minus(instance2);
 AbstractInstance expectedResult = instance1.subtract(instance2);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(expectedResult.value(i), result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testMinusWithDoubleAndResultSubtraction() {
 AbstractInstance result = instance1.minus(1.0);
 AbstractInstance expectedResult = instance1.subtract(1.0);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(expectedResult.value(i), result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testMultiplyWithInstance() {
 AbstractInstance result = instance1.multiply(instance2);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(instance1.value(i) * instance2.value(i), result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testDivideWithInstance() {
 AbstractInstance result = instance1.divide(instance2);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(instance1.value(i) / instance2.value(i), result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testAddWithDouble() {
 AbstractInstance result = instance1.add(1.0);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(instance1.value(i) + 1.0, result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testAddWithInstance() {
 AbstractInstance result = instance1.add(instance2);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(instance1.value(i) + instance2.value(i), result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testSqrt() {
 AbstractInstance result = instance1.sqrt();
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(Math.sqrt(instance1.value(i)), result.value(i), 0.001);
 }
 }

 @org.junit.Test
 public void testMinusWithNull() {
 try {
 instance1.minus(null);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testMinusWithInstanceWithDifferentSize() {
 try {
 instance1.minus(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}));
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testDivideWithInstanceWithZeroDivision() {
 try {
 instance1.divide(new DenseInstance(new double[]{0.0, 0.0, 0.0}));
 fail();
 } catch (ArithmeticException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testDivideWithInstanceWithZeroValue() {
 AbstractInstance result = instance1.divide(new DenseInstance(new double[]{0.0, 0.0, 0.0}));
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(Double.NaN, result.value(i), 0.0);
 }
 }

 @org.junit.Test
 public void testMultiplyWithInstanceWithZeroInstance() {
 AbstractInstance result = instance1.multiply(new DenseInstance());
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(0.0, result.value(i), 0.0);
 }
 }

 @org.junit.Test
 public void testMultiplyWithInstanceWithZeroValue() {
 AbstractInstance result = instance1.multiply(new DenseInstance(new double[]{0.0, 0.0, 0.0}));
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(0.0, result.value(i), 0.0);
 }
 }

 @org.junit.Test
 public void testMinusWithInstanceWithDifferentSize() {
 try {
 instance1.minus(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}));
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testAddWithInstanceWithDifferentSize() {
 try {
 instance1.add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}));
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testSqrtWithNegativeValues() {
 try {
 instance1 = new DenseInstance(new double[]{-1.0, -2.0, -3.0});
 instance1.sqrt();
 fail();
 } catch (ArithmeticException e) {
 // Expected
 }
 }
}