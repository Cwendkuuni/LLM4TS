package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractInstanceTest {

 AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[] {2.0, 3.0, 4.0});
 AbstractInstance instance3 = new DenseInstance(new double[] {1.0, 1.0, 1.0});

 @Test
 public void testGetID() {
 int id1 = instance1.getID();
 int id2 = instance2.getID();
 int id3 = instance3.getID();

 assertEquals(0, id1);
 assertEquals(1, id2);
 assertEquals(2, id3);
 }

 @Test
 public void testIterator() {
 Double value;
 Iterator<Double> iterator = instance1.iterator();

 assertTrue(iterator.hasNext());
 value = iterator.next();
 assertEquals(1.0, value.doubleValue(), 0.0);

 assertTrue(iterator.hasNext());
 value = iterator.next();
 assertEquals(2.0, value.doubleValue(), 0.0);

 assertTrue(iterator.hasNext());
 value = iterator.next();
 assertEquals(3.0, value.doubleValue(), 0.0);

 assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructor() {
 AbstractInstance abstractInstance = new AbstractInstance();
 assertNull(abstractInstance.classValue());

 abstractInstance = new AbstractInstance("TestClass");
 assertEquals("TestClass", abstractInstance.classValue());
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance result = instance1.minus(instance2);

 assertNotNull(result);
 assertEquals(instance1.noAttributes(), result.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 Double value1 = instance1.value(i);
 Double value2 = instance2.value(i);
 Double resultValue = result.value(i);

 assertEquals(-value2.doubleValue(), resultValue.doubleValue(), 0.0);
 }
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance result = instance1.minus((double) 1.0);

 assertNotNull(result);
 assertEquals(instance1.noAttributes(), result.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 Double value1 = instance1.value(i);
 Double resultValue = result.value(i);

 assertEquals(-1.0, resultValue.doubleValue(), 0.0);
 }
 }

 @Test
 public void testDivideWithDouble() {
 AbstractInstance result = instance1.divide((double) 2.0);

 assertNotNull(result);
 assertEquals(instance1.noAttributes(), result.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 Double value1 = instance1.value(i);
 Double resultValue = result.value(i);

 assertEquals(value1.doubleValue() / 2.0, resultValue.doubleValue(), 0.0);
 }
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance result = instance1.multiply((double) 2.0);

 assertNotNull(result);
 assertEquals(instance1.noAttributes(), result.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 Double value1 = instance1.value(i);
 Double resultValue = result.value(i);

 assertEquals(value1.doubleValue() * 2.0, resultValue.doubleValue(), 0.0);
 }
 }

 @Test
 public void testEquals() {
 assertTrue(instance1.equals(instance1));
 assertFalse(instance1.equals(instance2));
 assertFalse(instance1.equals(null));
 }

 @Test
 public void testHashCode() {
 int hashCode1 = instance1.hashCode();
 int hashCode2 = instance2.hashCode();

 assertNotSame(hashCode1, hashCode2);
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance result = instance1.multiply(instance2);

 assertNotNull(result);
 assertEquals(instance1.noAttributes(), result.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 Double value1 = instance1.value(i);
 Double value2 = instance2.value(i);
 Double resultValue = result.value(i);

 assertEquals(value1.doubleValue() * value2.doubleValue(), resultValue.doubleValue(), 0.0);
 }
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance result = instance1.divide(instance2);

 assertNotNull(result);
 assertEquals(instance1.noAttributes(), result.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 Double value1 = instance1.value(i);
 Double value2 = instance2.value(i);
 Double resultValue = result.value(i);

 assertEquals(value1.doubleValue() / value2.doubleValue(), resultValue.doubleValue(), 0.0);
 }
 }

 @Test
 public void testAddWithDouble() {
 AbstractInstance result = instance1.add((double) 1.0);

 assertNotNull(result);
 assertEquals(instance1.noAttributes(), result.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 Double value1 = instance1.value(i);
 Double resultValue = result.value(i);

 assertEquals(value1.doubleValue() + 1.0, resultValue.doubleValue(), 0.0);
 }
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance result = instance1.add(instance2);

 assertNotNull(result);
 assertEquals(instance1.noAttributes(), result.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 Double value1 = instance1.value(i);
 Double value2 = instance2.value(i);
 Double resultValue = result.value(i);

 assertEquals(value1.doubleValue() + value2.doubleValue(), resultValue.doubleValue(), 0.0);
 }
 }

 @Test
 public void testSqrt() {
 AbstractInstance result = instance1.sqrt();

 assertNotNull(result);
 assertEquals(instance1.noAttributes(), result.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 Double value1 = instance1.value(i);
 Double resultValue = result.value(i);

 assertEquals(Math.sqrt(value1.doubleValue()), resultValue.doubleValue(), 0.0);
 }
 }
}