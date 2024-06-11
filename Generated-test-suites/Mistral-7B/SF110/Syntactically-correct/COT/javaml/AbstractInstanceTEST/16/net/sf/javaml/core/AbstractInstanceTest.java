package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import net.sf.javaml.core.*;

@RunWith(JUnit4.class)
public class AbstractInstanceTest {

 // List all the public methods and their signatures
 @Test
 public void testGetID() {
 AbstractInstance instance = new DenseInstance(new double[1]);
 AbstractInstance anotherInstance = new DenseInstance(new double[1]);
 int idInstance = instance.getID();
 int idAnotherInstance = anotherInstance.getID();

 // Basic test case for getID() method
 assertEquals("Both instances should have different IDs", idInstance, !equals(instance, anotherInstance) ? idAnotherInstance : idInstance + 1);
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = new DenseInstance(new double[1]);
 instance.put(0, 5.0);
 Iterator<Double> iterator = instance.iterator();
 Double value = iterator.next();

 // Basic test case for iterator() method
 assertEquals("First value of the iterator should be the first value of the instance", 5.0, value);
 }

 @Test
 public void testConstructorWithoutClassValue() {
 AbstractInstance instance = new AbstractInstance();
 assertEquals("ID should be initialized correctly", AbstractInstance.nextID, instance.getID());
 }

 @Test
 public void testConstructorWithClassValue() {
 Object classValue = new Object();
 AbstractInstance instance = new AbstractInstance(classValue);
 assertEquals("Class value should be set correctly", classValue, instance.classValue());
 }

 @Test
 public void testClassValue() {
 Object classValue = new Object();
 AbstractInstance instance = new AbstractInstance();
 instance.setClassValue(classValue);
 assertEquals("Class value should be set and get correctly", classValue, instance.classValue());
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
 AbstractInstance min = new DenseInstance(new double[]{1.0, 1.0});
 AbstractInstance result = instance.minus(min);

 // Basic test case for minus(Instance) method
 assertArrayEquals("Both instances should have the same number of attributes", new double[]{0.0, 1.0}, result.toArray());
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
 AbstractInstance result = instance.minus(2.0);

 // Basic test case for minus(double) method
 assertArrayEquals("Both instances should have the same number of attributes", new double[]{0.0, -1.0}, result.toArray());
 }

 @Test
 public void testDivideWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
 AbstractInstance result = instance.divide(2.0);

 // Basic test case for divide(double) method
 assertArrayEquals("Both instances should have the same number of attributes", new double[]{0.5, 1.0}, result.toArray());
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
 AbstractInstance result = instance.multiply(2.0);

 // Basic test case for multiply(double) method
 assertArrayEquals("Both instances should have the same number of attributes", new double[]{2.0, 4.0}, result.toArray());
 }

 @Test
 public void testEquals() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
 AbstractInstance anotherInstance = new DenseInstance(new double[]{1.0, 3.0});

 // Basic test case for equals(Object) method
 assertTrue("Two identical instances should be considered equal", instance1.equals(instance2));
 assertFalse("Two different instances should not be considered equal", instance1.equals(anotherInstance));
 }

 @Test
 public void testMinusWithInstanceAndThrowException() {
 AbstractInstance instance = new DenseInstance(new double[1]);
 assertThrows("SizeMismatchException", () -> instance.minus((Instance)new DenseInstance(new double[2])));
 }

 @Test
 public void testDivideWithInstanceAndThrowException() {
 AbstractInstance instance = new DenseInstance(new double[1]);
 assertThrows("IllegalArgumentException", () -> instance.divide(null));
 }

 @Test
 public void testDivideWithInstanceAndZero() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
 AbstractInstance min = new DenseInstance(new double[]{1.0, 0.0});
 AbstractInstance result = instance.divide(min);

 // Test for dividing by zero scenario
 assertArrayEquals("Both instances should have the same number of attributes", new double[]{Double.NaN, Double.NaN}, result.toArray());
 }

 @Test
 public void testMultiplyWithInstanceAndZero() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
 AbstractInstance value = new DenseInstance(new double[]{1.0, 0.0});
 AbstractInstance result = instance.multiply(value);

 // Test for multiplying by zero scenario
 assertArrayEquals("Both instances should have the same number of attributes", new double[]{0.0, 0.0}, result.toArray());
 }

 @Test
 public void testAddWithInstanceAndThrowException() {
 AbstractInstance instance = new DenseInstance(new double[1]);
 assertThrows("SizeMismatchException", () -> instance.add((Instance)new DenseInstance(new double[2])));
 }

 @Test
 public void testAddWithInstanceAndDifferentSize() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
 AbstractInstance min = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.add(min);

 // Test for adding instances with different sizes scenario
 assertArrayEquals("Result should have the same number of attributes as the sum of instances", new double[]{2.0, 4.0, 3.0}, result.toArray());
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = new DenseInstance(new double[]{4.0});
 AbstractInstance result = instance.sqrt();

 // Basic test case for sqrt() method
 assertEquals("Square root of a positive number should be a positive number", 2.0, result.get(0), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeNumber() {
 AbstractInstance instance = new DenseInstance(new double[]{4.0, -2.0});
 AbstractInstance result = instance.sqrt();
 }

}