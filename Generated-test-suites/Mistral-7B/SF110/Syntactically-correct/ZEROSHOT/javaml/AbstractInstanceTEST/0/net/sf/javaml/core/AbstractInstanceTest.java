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
 private AbstractInstance instance4;

 @Test
 public void testAbstractInstanceConstructor() {
 // Test default constructor
 AbstractInstance instance = new AbstractInstance();
 assertEquals(0, instance.getID());
 assertNull(instance.classValue());
 // Test constructor with class value
 instance = new AbstractInstance("testClassValue");
 assertEquals(1, instance.getID());
 assertEquals("testClassValue", instance.classValue());
 }

 @Test
 public void testGetID() {
 // Test getID for different instances
 instance1 = new AbstractInstance();
 instance2 = new AbstractInstance();
 assertEquals(0, instance1.getID());
 assertEquals(1, instance2.getID());
 }

 @Test
 public void testIterator() {
 // Test iterator for empty instance
 instance1 = new AbstractInstance();
 Iterator<Double> iterator = instance1.iterator();
 assertFalse(iterator.hasNext());
 // Test iterator for instance with values
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), 1.0);
 put(new Object(), 2.0);
 put(new Object(), 3.0);
 }} );
 iterator = instance2.iterator();
 assertEquals(3.0, iterator.next());
 assertEquals(2.0, iterator.next());
 assertEquals(1.0, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMinus() {
 // Test minus with another instance
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), 1.0);
 put(new Object(), 2.0);
 put(new Object(), 3.0);
 }} );
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), 0.5);
 put(new Object(), 1.5);
 put(new Object(), 2.5);
 }} );
 AbstractInstance result = instance1.minus(instance2);
 // Check result values
 Map<Object, Double> resultMap = (Map<Object, Double>) result;
 assertEquals(3.0, resultMap.get(new Object()), 0.001);
 assertEquals(1.5, resultMap.get(new Object()), 0.001);
 assertEquals(0.5, resultMap.get(new Object()), 0.001);
 // Test minus with a double
 result = instance1.minus(1.0);
 // Check result values
 resultMap = (Map<Object, Double>) result;
 assertEquals(2.0, resultMap.get(new Object()), 0.001);
 assertEquals(1.0, resultMap.get(new Object()), 0.001);
 assertEquals(0.0, resultMap.get(new Object()), 0.001);
 }

 @Test
 public void testMinusWithZero() {
 // Test minus with zero
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), 1.0);
 put(new Object(), 2.0);
 put(new Object(), 3.0);
 }} );
 AbstractInstance result = instance1.minus(0.0);
 // Check result values
 Map<Object, Double> resultMap = (Map<Object, Double>) result;
 assertEquals(1.0, resultMap.get(new Object()), 0.001);
 assertEquals(1.0, resultMap.get(new Object()), 0.001);
 assertEquals(1.0, resultMap.get(new Object()), 0.001);
 }

 @Test
 public void testDivide() {
 // Test divide with a double
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), 4.0);
 put(new Object(), 8.0);
 put(new Object(), 12.0);
 }} );
 AbstractInstance result = instance1.divide(2.0);
 // Check result values
 Map<Object, Double> resultMap = (Map<Object, Double>) result;
 assertEquals(2.0, resultMap.get(new Object()), 0.001);
 assertEquals(4.0, resultMap.get(new Object()), 0.001);
 assertEquals(6.0, resultMap.get(new Object()), 0.001);
 // Test divide with another instance
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), 2.0);
 put(new Object(), 4.0);
 put(new Object(), 6.0);
 }} );
 result = instance1.divide(instance2);
 // Check result values
 resultMap = (Map<Object, Double>) result;
 assertEquals(1.0, resultMap.get(new Object()), 0.001);
 assertEquals(2.0, resultMap.get(new Object()), 0.001);
 assertEquals(2.0, resultMap.get(new Object()), 0.001);
 }

 @Test
 public void testMultiply() {
 // Test multiply with a double
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), 1.0);
 put(new Object(), 2.0);
 put(new Object(), 3.0);
 }} );
 AbstractInstance result = instance1.multiply(2.0);
 // Check result values
 Map<Object, Double> resultMap = (Map<Object, Double>) result;
 assertEquals(2.0, resultMap.get(new Object()), 0.001);
 assertEquals(4.0, resultMap.get(new Object()), 0.001);
 assertEquals(6.0, resultMap.get(new Object()), 0.001);
 // Test multiply with another instance
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), 2.0);
 put(new Object(), 4.0);
 put(new Object(), 6.0);
 }} );
 result = instance1.multiply(instance2);
 // Check result values
 resultMap = (Map<Object, Double>) result;
 assertEquals(4.0, resultMap.get(new Object()), 0.001);
 assertEquals(8.0, resultMap.get(new Object()), 0.001);
 assertEquals(14.0, resultMap.get(new Object()), 0.001);
 }

 @Test
 public void testHashCodeEquals() {
 // Test hashCode and equals for same instance
 instance1 = new AbstractInstance();
 AbstractInstance instance1Copy = new AbstractInstance();
 assertEquals(instance1.hashCode(), instance1Copy.hashCode());
 assertTrue(instance1.equals(instance1Copy));
 // Test hashCode and equals for different instances
 instance1 = new AbstractInstance();
 instance2 = new AbstractInstance();
 assertNotSame(instance1.hashCode(), instance2.hashCode());
 assertFalse(instance1.equals(instance2));
 // Test equals with null
 assertFalse(instance1.equals(null));
 }

 @Test
 public void testMultiplyInstance() {
 // Test multiplyInstance with another instance
 instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance2 = new DenseInstance(new double[] {2.0, 4.0, 6.0});
 AbstractInstance result = instance1.multiply(instance2);
 // Check result values
 assertEquals(2.0, result.getValue(0), 0.001);
 assertEquals(8.0, result.getValue(1), 0.001);
 assertEquals(18.0, result.getValue(2), 0.001);
 }

 @Test
 public void testDivideInstance() {
 // Test divideInstance with another instance
 instance1 = new DenseInstance(new double[] {4.0, 8.0, 12.0});
 instance2 = new DenseInstance(new double[] {2.0, 4.0, 6.0});
 AbstractInstance result = instance1.divide(instance2);
 // Check result values
 assertEquals(2.0, result.getValue(0), 0.001);
 assertEquals(2.0, result.getValue(1), 0.001);
 assertEquals(2.0, result.getValue(2), 0.001);
 }

 @Test
 public void testAddInstance() {
 // Test addInstance with another instance
 instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance2 = new DenseInstance(new double[] {0.5, 1.5, 2.5});
 AbstractInstance result = instance1.add(instance2);
 // Check result values
 assertEquals(1.5, result.getValue(0), 0.001);
 assertEquals(3.5, result.getValue(1), 0.001);
 assertEquals(5.5, result.getValue(2), 0.001);
 }

 @Test
 public void testAddInstanceWithZero() {
 // Test addInstance with zero
 instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 AbstractInstance result = instance1.add(0.0);
 // Check result values
 assertEquals(1.0, result.getValue(0), 0.001);
 assertEquals(2.0, result.getValue(1), 0.001);
 assertEquals(3.0, result.getValue(2), 0.001);
 }

 @Test
 public void testSqrt() {
 // Test sqrt for positive values
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), 4.0);
 put(new Object(), 9.0);
 put(new Object(), 16.0);
 }} );
 AbstractInstance result = instance1.sqrt();
 // Check result values
 Map<Object, Double> resultMap = (Map<Object, Double>) result;
 assertEquals(2.0, resultMap.get(new Object()), 0.001);
 assertEquals(3.0, resultMap.get(new Object()), 0.001);
 assertEquals(4.0, resultMap.get(new Object()), 0.001);
 // Test sqrt for negative values
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), -4.0);
 put(new Object(), -9.0);
 put(new Object(), -16.0);
 }} );
 result = instance2.sqrt();
 // Check result values
 resultMap = (Map<Object, Double>) result;
 assertEquals(-2.0, resultMap.get(new Object()), 0.001);
 assertEquals(-3.0, resultMap.get(new Object()), 0.001);
 assertEquals(-4.0, resultMap.get(new Object()), 0.001);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveWithIterator() {
 // Test remove with iterator
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Object(), 1.0);
 put(new Object(), 2.0);
 put(new Object(), 3.0);
 }} );
 iterator = instance1.iterator();
 iterator.next();
 iterator.remove();
 }
}