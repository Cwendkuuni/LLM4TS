package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;

 @Before
 public void setUp() {
 // Create an instance with default values
 instance1 = new AbstractInstance();
 // Create an instance with a custom class value
 instance2 = new AbstractInstance("customClassValue");
 // Create an instance with attributes
 Map<String, Double> attributes1 = new HashMap<>();
 attributes1.put("attribute1", 1.0);
 attributes1.put("attribute2", 2.0);
 instance3 = new AbstractInstance(attributes1);
 }

 @Test
 public void testGetID() {
 // Test default instance ID
 assertEquals(0, instance1.getID());
 // Test custom instance ID
 assertEquals(1, instance2.getID());
 // Test instance3 ID
 assertEquals(2, instance3.getID());
 }

 @Test
 public void testIterator() {
 // Test default instance iterator
 AbstractInstance expectedInstance1 = new AbstractInstance();
 expectedInstance1.put(0, 1.0);
 expectedInstance1.put(1, 2.0);
 Iterator<Double> actualIterator1 = instance1.iterator();
 assertEquals(expectedInstance1, createInstanceFromIterator(actualIterator1));
 // Test instance2 iterator
 AbstractInstance expectedInstance2 = new AbstractInstance();
 expectedInstance2.put(0, 3.0);
 expectedInstance2.put(1, 4.0);
 Iterator<Double> actualIterator2 = instance2.iterator();
 assertEquals(expectedInstance2, createInstanceFromIterator(actualIterator2));
 // Test instance3 iterator
 Iterator<Double> actualIterator3 = instance3.iterator();
 AbstractInstance expectedInstance3 = instance3;
 assertEquals(expectedInstance3, createInstanceFromIterator(actualIterator3));
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 assertNull(instance1.getClassValue());
 // Test constructor with custom class value
 assertEquals("customClassValue", instance2.getClassValue());
 // Test constructor with attributes
 assertEquals(2, instance3.noAttributes());
 assertEquals(1.0, instance3.value(0), 0.001);
 assertEquals(2.0, instance3.value(1), 0.001);
 }

 @Test
 public void testClassValue() {
 // Test default instance class value
 assertNull(instance1.classValue());
 // Test custom instance class value
 assertEquals("customClassValue", instance2.classValue());
 // Test instance3 class value
 assertNull(instance3.classValue());
 // Test set and get class value
 instance3.setClassValue("testClassValue");
 assertEquals("testClassValue", instance3.classValue());
 }

 @Test
 public void testMinus() {
 // Test instance1 minus instance1
 AbstractInstance result1 = instance1.minus(instance1);
 assertEquals(new DenseInstance(new double[0]), result1);
 // Test instance1 minus instance2
 AbstractInstance result2 = instance1.minus(instance2);
 AbstractInstance expectedResult2 = new DenseInstance(new double[]{0.0, -1.0});
 assertEquals(expectedResult2, result2);
 // Test instance2 minus instance1
 AbstractInstance result3 = instance2.minus(instance1);
 AbstractInstance expectedResult3 = new DenseInstance(new double[]{-1.0, 1.0});
 assertEquals(expectedResult3, result3);
 // Test instance3 minus instance1
 AbstractInstance result4 = instance3.minus(instance1);
 AbstractInstance expectedResult4 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? (index == 0 ? -1.0 : 1.0) : null;
 }
 };
 assertEquals(expectedResult4, result4);
 }

 @Test
 public void testMinusDouble() {
 // Test instance1 minus double
 AbstractInstance result1 = instance1.minus(0.0);
 assertEquals(new DenseInstance(new double[]{0.0, 0.0}), result1);
 // Test instance2 minus double
 AbstractInstance result2 = instance2.minus(1.0);
 AbstractInstance expectedResult2 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? (index == 0 ? 0.0 : -1.0) : null;
 };
 };
 assertEquals(expectedResult2, result2);
 // Test instance3 minus double
 AbstractInstance result3 = instance3.minus(1.0);
 AbstractInstance expectedResult3 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? (index == 0 ? 0.0 : -1.0 + instance3.value(index)) : null;
 };
 };
 assertEquals(expectedResult3, result3);
 }

 @Test
 public void testDivide() {
 // Test instance1 divide by double
 AbstractInstance result1 = instance1.divide(1.0);
 assertEquals(new DenseInstance(new double[]{0.0, 0.0}), result1);
 // Test instance2 divide by double
 AbstractInstance result2 = instance2.divide(2.0);
 AbstractInstance expectedResult2 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 0.5 : null;
 };
 };
 assertEquals(expectedResult2, result2);
 // Test instance3 divide by double
 AbstractInstance result3 = instance3.divide(2.0);
 AbstractInstance expectedResult3 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 0.5 * instance3.value(index) : null;
 };
 };
 assertEquals(expectedResult3, result3);
 }

 @Test
 public void testMultiply() {
 // Test instance1 multiply by double
 AbstractInstance result1 = instance1.multiply(1.0);
 assertEquals(new DenseInstance(new double[]{0.0, 0.0}), result1);
 // Test instance2 multiply by double
 AbstractInstance result2 = instance2.multiply(2.0);
 AbstractInstance expectedResult2 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 2.0 * instance2.value(index) : null;
 };
 };
 assertEquals(expectedResult2, result2);
 // Test instance3 multiply by double
 AbstractInstance result3 = instance3.multiply(2.0);
 AbstractInstance expectedResult3 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 2.0 * instance3.value(index) : null;
 };
 };
 assertEquals(expectedResult3, result3);
 }

 @Test
 public void testHashCode() {
 // Test default instance hashcode
 assertEquals(0, instance1.hashCode());
 // Test custom instance hashcode
 assertEquals(1, instance2.hashCode());
 // Test instance3 hashcode
 assertEquals(2, instance3.hashCode());
 // Test equal instances have the same hashcode
 AbstractInstance instance3Copy = new AbstractInstance(instance3);
 assertEquals(instance3.hashCode(), instance3Copy.hashCode());
 }

 @Test
 public void testEquals() {
 // Test default instance equals itself
 assertTrue(instance1.equals(instance1));
 // Test default instance equals null
 assertFalse(instance1.equals(null));
 // Test different instances are not equal
 assertFalse(instance1.equals(instance2));
 // Test equal instances are equal
 AbstractInstance instance3Copy = new AbstractInstance(instance3);
 assertTrue(instance3.equals(instance3Copy));
 // Test instance3 equals instance1 if instance1 is modified
 instance1.setClassValue("testClassValue");
 assertTrue(instance3.equals(instance1));
 }

 @Test
 public void testMultiplyInstance() {
 // Test instance1 multiplied by instance1
 AbstractInstance result1 = instance1.multiply(instance1);
 assertEquals(new DenseInstance(new double[0]), result1);
 // Test instance1 multiplied by instance2
 AbstractInstance result2 = instance1.multiply(instance2);
 AbstractInstance expectedResult2 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return 0.0;
 }
 };
 assertEquals(expectedResult2, result2);
 // Test instance2 multiplied by instance1
 AbstractInstance result3 = instance2.multiply(instance1);
 AbstractInstance expectedResult3 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 0.0 : null;
 };
 };
 assertEquals(expectedResult3, result3);
 // Test instance3 multiplied by instance1
 AbstractInstance result4 = instance3.multiply(instance1);
 AbstractInstance expectedResult4 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 0.0 : null;
 };
 };
 assertEquals(expectedResult4, result4);
 }

 @Test
 public void testDivideInstance() {
 // Test instance1 divided by instance1
 AbstractInstance result1 = instance1.divide(instance1);
 assertEquals(new DenseInstance(new double[0]), result1);
 // Test instance1 divided by instance2
 AbstractInstance result2 = instance1.divide(instance2);
 AbstractInstance expectedResult2 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 0.0 : null;
 };
 };
 assertEquals(expectedResult2, result2);
 // Test instance2 divided by instance1
 AbstractInstance result3 = instance2.divide(instance1);
 AbstractInstance expectedResult3 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 0.0 : null;
 };
 };
 assertEquals(expectedResult3, result3);
 // Test instance3 divided by instance1
 AbstractInstance result4 = instance3.divide(instance1);
 AbstractInstance expectedResult4 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 0.0 : null;
 };
 };
 assertEquals(expectedResult4, result4);
 }

 @Test
 public void testAddDouble() {
 // Test instance1 added to double
 AbstractInstance result1 = instance1.add(0.0);
 assertEquals(new DenseInstance(new double[]{0.0, 0.0}), result1);
 // Test instance2 added to double
 AbstractInstance result2 = instance2.add(1.0);
 AbstractInstance expectedResult2 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 1.0 + instance2.value(index) : null;
 };
 };
 assertEquals(expectedResult2, result2);
 // Test instance3 added to double
 AbstractInstance result3 = instance3.add(1.0);
 AbstractInstance expectedResult3 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? 1.0 + instance3.value(index) : null;
 };
 };
 assertEquals(expectedResult3, result3);
 }

 @Test
 public void testAddInstance() {
 // Test instance1 added to instance1
 AbstractInstance result1 = instance1.add(instance1);
 assertEquals(new DenseInstance(new double[0]), result1);
 // Test instance1 added to instance2
 AbstractInstance result2 = instance1.add(instance2);
 AbstractInstance expectedResult2 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? instance1.value(index) + instance2.value(index) : null;
 };
 };
 assertEquals(expectedResult2, result2);
 // Test instance2 added to instance1
 AbstractInstance result3 = instance2.add(instance1);
 AbstractInstance expectedResult3 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? instance1.value(index) + instance2.value(index) : null;
 };
 };
 assertEquals(expectedResult3, result3);
 // Test instance3 added to instance1
 AbstractInstance result4 = instance3.add(instance1);
 AbstractInstance expectedResult4 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return index >= 0 && index < 2 ? instance3.value(index) + instance1.value(index) : null;
 };
 };
 assertEquals(expectedResult4, result4);
 }

 @Test
 public void testSqrt() {
 // Test instance1 square root
 AbstractInstance result1 = instance1.sqrt();
 assertEquals(new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return Math.sqrt(instance1.value(index));
 }
 }, result1);
 // Test instance2 square root
 AbstractInstance result2 = instance2.sqrt();
 AbstractInstance expectedResult2 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return Math.sqrt(instance2.value(index));
 }
 };
 assertEquals(expectedResult2, result2);
 // Test instance3 square root
 AbstractInstance result3 = instance3.sqrt();
 AbstractInstance expectedResult3 = new AbstractInstance() {
 @Override
 public int noAttributes() {
 return 2;
 }

 @Override
 public Double value(int index) {
 return Math.sqrt(instance3.value(index));
 }
 };
 assertEquals(expectedResult3, result3);
 }

 private AbstractInstance createInstanceFromIterator(Iterator<Double> iterator) {
 AbstractInstance instance = new AbstractInstance();
 int index = 0;
 while (iterator.hasNext()) {
 instance.put(index++, iterator.next());
 }
 return instance;
 }
}