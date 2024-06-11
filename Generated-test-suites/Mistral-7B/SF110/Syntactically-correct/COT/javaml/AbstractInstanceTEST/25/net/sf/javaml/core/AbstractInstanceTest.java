package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private AbstractInstance instance;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private double minValue = -1.0;
 private double maxValue = 1.0;
 private int noAttributes = 3;

 @org.junit.Before
 public void setUp() {
 instance = new DenseInstance(new double[noAttributes]);
 instance2 = new DenseInstance(new double[noAttributes]);
 instance3 = new DenseInstance(new double[noAttributes]);
 for (int i = 0; i < noAttributes; i++) {
 instance.put(i, maxValue);
 instance2.put(i, minValue);
 }
 }

 @org.junit.Test
 public void testGetID() {
 AbstractInstance testInstance = new DenseInstance(new double[noAttributes]);
 assertEquals("ID should be correctly initialized", testInstance.getID(), testInstance.ID);
 }

 @org.junit.Test
 public void testIterator() {
 List<Double> expectedList = new ArrayList<>();
 List<Double> actualList = new ArrayList<>();
 for (Double value : instance.iterator()) {
 expectedList.add(instance.get(value.intValue()));
 actualList.add(value);
 }
 assertEquals("Iterator should return the same values as the get method", expectedList, actualList);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetID() {
 AbstractInstance testInstance = new DenseInstance(new double[noAttributes]);
 try {
 testInstance.setID(-1);
 } catch (IllegalArgumentException e) {
 fail("Setting ID with a negative number should throw an IllegalArgumentException, but it threw an UnsupportedOperationException.");
 }
 testInstance.setID(10);
 assertEquals("ID should be set correctly", 10, testInstance.getID());
 }

 @org.junit.Test
 public void testMinusWithInstance() {
 Instance result = instance.minus(instance2);
 for (int i = 0; i < noAttributes; i++) {
 assertEquals("Subtraction with an instance should return the correct result", maxValue - minValue, result.get(i), 0.001);
 }
 }

 @org.junit.Test
 public void testMinusWithDouble() {
 Instance result = instance.minus(minValue);
 for (int i = 0; i < noAttributes; i++) {
 assertEquals("Subtraction with a double should return the correct result", maxValue - minValue, result.get(i), 0.001);
 }
 }

 @org.junit.Test
 public void testDivideWithDouble() {
 Instance result = instance.divide(minValue);
 for (int i = 0; i < noAttributes; i++) {
 assertEquals("Division with a double should return the correct result", maxValue / minValue, result.get(i), 0.001);
 }
 }

 @org.junit.Test
 public void testMultiplyWithDouble() {
 Instance result = instance.multiply(minValue);
 for (int i = 0; i < noAttributes; i++) {
 assertEquals("Multiplication with a double should return the correct result", maxValue * minValue, result.get(i), 0.001);
 }
 }

 @org.junit.Test
 public void testMultiplyWithInstance() {
 Instance result = instance.multiply(instance2);
 for (int i = 0; i < noAttributes; i++) {
 assertEquals("Multiplication with an instance should return the correct result", maxValue * minValue, result.get(i), 0.001);
 }
 }

 @org.junit.Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
 Instance result = instance.divide(minValue);
 try {
 result.divide(new DenseInstance(new double[noAttributes]));
 } catch (ArithmeticException e) {
 // Expected
 }
 }

 @org.junit.Test(expected = ArithmeticException.class)
 public void testSubtractWithNegativeNumber() {
 Instance result = instance.minus(minValue);
 try {
 result.minus(minValue);
 } catch (ArithmeticException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testAddWithInstance() {
 Instance result = instance.add(instance2);
 for (int i = 0; i < noAttributes; i++) {
 assertEquals("Addition with an instance should return the correct result", maxValue + minValue, result.get(i), 0.001);
 }
 }

 @org.junit.Test
 public void testAddWithDouble() {
 Instance result = instance.add(minValue);
 for (int i = 0; i < noAttributes; i++) {
 assertEquals("Addition with a double should return the correct result", maxValue + minValue, result.get(i), 0.001);
 }
 }

 @org.junit.Test
 public void testSqrt() {
 Instance result = instance.sqrt();
 for (int i = 0; i < noAttributes; i++) {
 assertEquals("Square root should return the correct result", Math.sqrt(maxValue), result.get(i), 0.001);
 }
 }
}