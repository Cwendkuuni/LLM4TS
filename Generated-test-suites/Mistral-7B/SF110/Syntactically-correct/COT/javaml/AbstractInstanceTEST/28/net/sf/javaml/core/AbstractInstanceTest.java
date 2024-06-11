package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;

 @Test
 public void testGetID() {
     instance1 = new DenseInstance(new double[5]);
     assertEquals("ID should be correctly initialized", 0, instance1.getID());

     instance2 = new DenseInstance(new double[5], 5);
     assertEquals("ID should be correctly initialized for custom ID", 5, instance2.getID());
 }

 @Test
 public void testIterator() {
     instance1 = new DenseInstance(new double[5]);
     Iterator<Double> it = instance1.iterator();
     assertNotNull("iterator() should not return null", it);
 }

 @Test
 public void testConstructorNoArgs() {
     instance1 = new AbstractInstance();
     assertNotNull("Instance with no arguments constructor should not be null", instance1);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullClassValue() {
     instance1 = new AbstractInstance(null);
 }

 @Test
 public void testSetClassValue() {
     instance1 = new DenseInstance(new double[5]);
     Object classValue = new Object();
     instance1.setClassValue(classValue);
     assertEquals("Class value should be correctly set", classValue, instance1.classValue());
 }

 @Test(expected = NullPointerException.class)
 public void testMinusWithNullInstance() {
     instance1 = new DenseInstance(new double[5]);
     Instance min = null;
     instance1.minus(min);
 }

 @Test
 public void testMinusWithInstance() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance2 = new DenseInstance(new double[]{0.5, 1.5, 2.5, 3.5, 4.5});
     Instance result = instance1.minus(instance2);

     Instance expected = new DenseInstance(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
     assertArrayEquals("Minus method should correctly subtract values of instances", expected.toArray(), result.toArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithInvalidArgumentType() {
     instance1 = new DenseInstance(new double[5]);
     instance2 = new String("Test String");
     instance1.minus(instance2);
 }

 @Test
 public void testMinusWithNumber() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     Instance result = instance1.minus(5.0);

     Instance expected = new DenseInstance(new double[]{-4.0, -3.0, -2.0, -1.0, -1.0});
     assertArrayEquals("Minus method should correctly subtract number from each attribute", expected.toArray(), result.toArray());
 }

 @Test
 public void testDivideWithInstance() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance2 = new DenseInstance(new double[]{2.0, 2.0, 2.0, 2.0, 2.0});
     Instance result = instance1.divide(instance2);

     Instance expected = new DenseInstance(new double[]{2.5, 1.0, 1.5, 2.0, 2.5});
     assertArrayEquals("Divide method should correctly divide each attribute by another instance", expected.toArray(), result.toArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithInvalidArgumentType() {
     instance1 = new DenseInstance(new double[5]);
     instance2 = new String("Test String");
     instance1.divide(instance2);
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance2 = new DenseInstance(new double[]{1.0, 0.0, 0.0, 0.0, 0.0});
     instance1.divide(instance2);
 }

 @Test
 public void testDivideWithNumber() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     Instance result = instance1.divide(2.0);

     Instance expected = new DenseInstance(new double[]{0.5, 1.0, 1.5, 2.0, 2.5});
     assertArrayEquals("Divide method should correctly divide each attribute by a number", expected.toArray(), result.toArray());
 }

 @Test
 public void testMultiplyWithInstance() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance2 = new DenseInstance(new double[]{2.0, 2.0, 2.0, 2.0, 2.0});
     Instance result = instance1.multiply(instance2);

     Instance expected = new DenseInstance(new double[]{2.0, 4.0, 6.0, 8.0, 10.0});
     assertArrayEquals("Multiply method should correctly multiply each attribute by another instance", expected.toArray(), result.toArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithInvalidArgumentType() {
     instance1 = new DenseInstance(new double[5]);
     instance2 = new String("Test String");
     instance1.multiply(instance2);
 }

 @Test
 public void testMultiplyWithNumber() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     Instance result = instance1.multiply(2.0);

     Instance expected = new DenseInstance(new double[]{2.0, 4.0, 6.0, 8.0, 10.0});
     assertArrayEquals("Multiply method should correctly multiply each attribute by a number", expected.toArray(), result.toArray());
 }

 @Test
 public void testHashCode() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance3 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 6.0});

     assertEquals("Hashcodes should be equal if instances are the same", instance1.hashCode(), instance1.hashCode());
     assertNotSame("Hashcodes should not be equal if instances are different", instance1.hashCode(), instance2.hashCode());
     assertNotSame("Hashcodes should not be equal if instances have different attributes", instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testEquals() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance3 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 6.0});

     assertTrue("Instances with the same attributes should be equal", instance1.equals(instance2));
     assertFalse("Instances with different attributes should not be equal", instance1.equals(instance3));
 }

 @Test
 public void testEqualsWithNull() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     assertFalse("Instance should not be equal to null", instance1.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClass() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     String test = new String("Test String");
     assertFalse("Instance should not be equal to a different class", instance1.equals(test));
 }

 @Test
 public void testAddWithNumber() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     Instance result = instance1.add(2.0);

     Instance expected = new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0, 7.0});
     assertArrayEquals("Add method should correctly add a number to each attribute", expected.toArray(), result.toArray());
 }

 @Test
 public void testAddWithInstance() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance2 = new DenseInstance(new double[]{0.5, 1.0, 1.5, 2.0, 2.5});
     Instance result = instance1.add(instance2);

     Instance expected = new DenseInstance(new double[]{1.5, 3.0, 4.5, 6.0, 7.5});
     assertArrayEquals("Add method should correctly add another instance to each attribute", expected.toArray(), result.toArray());
 }

 @Test
 public void testAddWithInvalidArgumentType() {
     instance1 = new DenseInstance(new double[5]);
     String test = new String("Test String");
     instance1.add(test);
 }

 @Test
 public void testSqrt() {
     instance1 = new DenseInstance(new double[]{4.0, 9.0, 16.0, 25.0, 36.0});
     Instance result = instance1.sqrt();

     Instance expected = new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0, 6.0});
     assertArrayEquals("Sqrt method should correctly calculate the square root of each attribute", expected.toArray(), result.toArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeNumber() {
     instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance1.sqrt();

     // Negative numbers should not be allowed
 }
}