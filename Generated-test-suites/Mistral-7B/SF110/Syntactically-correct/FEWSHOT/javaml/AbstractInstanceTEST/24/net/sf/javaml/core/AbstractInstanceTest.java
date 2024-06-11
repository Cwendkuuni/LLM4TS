package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

 AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[] {0.5, 0.5, 0.5});
 AbstractInstance instance3 = new DenseInstance(new double[] {-1.0, -2.0, -3.0});
 AbstractInstance instance4 = new DenseInstance(new double[] {4.0, 5.0, 6.0});

 @Test
 public void testGetID() {
 ArrayList<AbstractInstance> instances = new ArrayList<>();
 instances.add(instance1);
 instances.add(instance2);
 instances.add(instance3);
 instances.add(instance4);

 for (AbstractInstance abstractInstance : instances) {
 assertEquals(abstractInstance.getID(), abstractInstance.hashCode());
 }
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = instance1;
 Iterator<Double> iterator = instance.iterator();
 List<Double> expectedValues = Arrays.asList(1.0, 2.0, 3.0);

 while (iterator.hasNext()) {
 Double value = iterator.next();
 assertTrue(expectedValues.contains(value));
 expectedValues.remove(value);
 }

 assertTrue(expectedValues.isEmpty());
 }

 @Test
 public void testAbstractInstance() {
 AbstractInstance abstractInstance = new AbstractInstance();
 assertNull(abstractInstance.classValue());
 assertEquals(0, abstractInstance.getID());
 }

 @Test
 public void testAbstractInstanceWithClassValue() {
 Object classValue = new Object();
 AbstractInstance abstractInstance = new AbstractInstance(classValue);
 assertEquals(classValue, abstractInstance.classValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMinusNull() {
 AbstractInstance min = null;
 AbstractInstance instance = instance1;
 AbstractInstance result = instance.minus(min);
 fail("Minus method should throw an UnsupportedOperationException when passed a null Instance.");
 }

 @Test
 public void testMinusInstance() {
 AbstractInstance min = instance2;
 AbstractInstance result = instance1.minus(min);
 AbstractInstance expectedResult = new DenseInstance(new double[] {0.5, 1.5, 2.5});

 assertEquals(expectedResult, result);
 }

 @Test
 public void testMinusDouble() {
 double min = 1.5;
 AbstractInstance result = instance1.minus(min);
 AbstractInstance expectedResult = new DenseInstance(new double[] {-0.5, 1.5, 1.5});

 assertEquals(expectedResult, result);
 }

 @Test
 public void testDivideInstance() {
 AbstractInstance min = instance2;
 AbstractInstance result = instance1.divide(min);
 AbstractInstance expectedResult = new DenseInstance(new double[] {2.0, 4.0, 6.0});

 assertEquals(expectedResult, result);
 }

 @Test
 public void testDivideDouble() {
 double min = 2.0;
 AbstractInstance result = instance1.divide(min);
 AbstractInstance expectedResult = new DenseInstance(new double[] {0.5, 1.0, 1.5});

 assertEquals(expectedResult, result);
 }

 @Test
 public void testMultiplyInstance() {
 AbstractInstance value = instance2;
 AbstractInstance result = instance1.multiply(value);
 AbstractInstance expectedResult = new DenseInstance(new double[] {2.0, 4.0, 6.0});

 assertEquals(expectedResult, result);
 }

 @Test
 public void testMultiplyDouble() {
 double value = 2.0;
 AbstractInstance result = instance1.multiply(value);
 AbstractInstance expectedResult = new DenseInstance(new double[] {2.0, 4.0, 6.0});

 assertEquals(expectedResult, result);
 }

 @Test
 public void testDenseInstanceEquals() {
 AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 AbstractInstance instance3 = new DenseInstance(new double[] {1.0, 2.0, 3.1});

 assertEquals(instance1, instance2);
 assertNotEquals(instance1, instance3);
 }

 @Test
 public void testDenseInstanceAddDouble() {
 double value = 1.0;
 AbstractInstance result = instance1.add(value);
 AbstractInstance expectedResult = new DenseInstance(new double[] {2.0, 3.0, 4.0});

 assertEquals(expectedResult, result);
 }

 @Test
 public void testDenseInstanceAddInstance() {
 AbstractInstance min = instance2;
 AbstractInstance result = instance1.add(min);
 AbstractInstance expectedResult = new DenseInstance(new double[] {1.5, 3.5, 4.5});

 assertEquals(expectedResult, result);
 }

 @Test
 public void testDenseInstanceSqrt() {
 AbstractInstance result = instance1.sqrt();
 AbstractInstance expectedResult = new DenseInstance(new double[] {1.0, 1.4142135623730955, 1.7320508075688772});

 assertEquals(expectedResult, result);
 }
}