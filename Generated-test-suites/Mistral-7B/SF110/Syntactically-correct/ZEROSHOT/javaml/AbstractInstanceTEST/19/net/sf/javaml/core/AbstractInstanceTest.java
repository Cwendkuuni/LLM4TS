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
 private AbstractInstance instance5;

 @Test
 public void testConstructor() {
 // Test default constructor
 instance1 = new AbstractInstance();
 assertEquals(0, instance1.getID());
 assertNull(instance1.classValue());

 // Test constructor with classValue
 instance2 = new AbstractInstance("testClassValue");
 assertEquals(1, instance2.getID());
 assertEquals("testClassValue", instance2.classValue());
 }

 @Test
 public void testGetID() {
 // Test getID method
 instance3 = new AbstractInstance();
 instance3.setID(10);
 assertEquals(10, instance3.getID());
 }

 @Test
 public void testIterator() {
 // Test iterator method
 instance4 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Iterator<Double> iterator = instance4.iterator();
 assertEquals(3, instance4.noAttributes());
 assertTrue(iterator.hasNext());
 assertEquals(1.0, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(2.0, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(3.0, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMinus() {
 // Test minus method with another instance
 instance5 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance6 = new DenseInstance(new double[]{0.5, 1.5, 2.5});
 AbstractInstance result = instance5.minus(instance6);
 assertEquals(3, result.noAttributes());
 for (int i = 0; i < result.noAttributes(); i++) {
 assertEquals(result.value(i) - instance6.value(i), instance5.value(i), 0.0001);
 }
 }

 @Test
 public void testMinusDouble() {
 // Test minus method with double
 instance5 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance5.minus(1.5);
 assertEquals(3, result.noAttributes());
 for (int i = 0; i < result.noAttributes(); i++) {
 assertEquals(result.value(i) - 1.5, instance5.value(i), 0.0001);
 }
 }

 @Test
 public void testDivide() {
 // Test divide method with double
 instance5 = new DenseInstance(new double[]{4.0, 8.0, 16.0});
 AbstractInstance result = instance5.divide(2.0);
 assertEquals(3, result.noAttributes());
 for (int i = 0; i < result.noAttributes(); i++) {
 assertEquals(result.value(i) / 2.0, instance5.value(i), 0.0001);
 }
 }

 @Test
 public void testMultiply() {
 // Test multiply method with double
 instance5 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance5.multiply(2.0);
 assertEquals(3, result.noAttributes());
 for (int i = 0; i < result.noAttributes(); i++) {
 assertEquals(result.value(i) * 2.0, instance5.value(i), 0.0001);
 }
 }

 @Test
 public void testEquals() {
 // Test equals method
 AbstractInstance instance7 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance8 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance9 = new DenseInstance(new double[]{1.1, 2.1, 3.1});
 assertTrue(instance7.equals(instance7));
 assertTrue(instance7.equals(instance8));
 assertFalse(instance7.equals(instance9));
 }

 @Test
 public void testHashCode() {
 // Test hashCode method
 AbstractInstance instance10 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance11 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertNotSame(instance10.hashCode(), instance11.hashCode());
 }

 @Test
 public void testMinusInstance() {
 // Test minus method with another instance
 instance5 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance6 = new DenseInstance(new double[]{0.5, 1.5, 2.5});
 AbstractInstance result = instance5.minus(instance6);
 assertEquals(3, result.noAttributes());
 for (int i = 0; i < result.noAttributes(); i++) {
 assertEquals(result.value(i) - instance6.value(i), instance5.value(i), 0.0001);
 }
 }

 @Test
 public void testDivideInstance() {
 // Test divide method with another instance
 instance5 = new DenseInstance(new double[]{4.0, 8.0, 16.0});
 AbstractInstance instance6 = new DenseInstance(new double[]{2.0, 4.0, 8.0});
 AbstractInstance result = instance5.divide(instance6);
 assertEquals(3, result.noAttributes());
 for (int i = 0; i < result.noAttributes(); i++) {
 assertEquals(result.value(i) / instance6.value(i), instance5.value(i) / instance6.value(i), 0.0001);
 }
 }

 @Test
 public void testMultiplyInstance() {
 // Test multiply method with another instance
 instance5 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance6 = new DenseInstance(new double[]{2.0, 2.0, 2.0});
 AbstractInstance result = instance5.multiply(instance6);
 assertEquals(3, result.noAttributes());
 for (int i = 0; i < result.noAttributes(); i++) {
 assertEquals(instance5.value(i) * instance6.value(i), result.value(i), 0.0001);
 }
 }

 @Test
 public void testAddDouble() {
 // Test add method with double
 instance5 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance5.add(1.5);
 assertEquals(3, result.noAttributes());
 for (int i = 0; i < result.noAttributes(); i++) {
 assertEquals(result.value(i) + 1.5, instance5.value(i), 0.0001);
 }
 }

 @Test
 public void testAddInstance() {
 // Test add method with another instance
 instance5 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance6 = new DenseInstance(new double[]{0.5, 1.5, 2.5});
 AbstractInstance result = instance5.add(instance6);
 assertEquals(3, result.noAttributes());
 for (int i = 0; i < result.noAttributes(); i++) {
 assertEquals(instance5.value(i) + instance6.value(i), result.value(i), 0.0001);
 }
 }

 @Test
 public void testSqrt() {
 // Test sqrt method
 instance5 = new DenseInstance(new double[]{1.0, 4.0, 9.0});
 AbstractInstance result = instance5.sqrt();
 assertEquals(3, result.noAttributes());
 for (int i = 0; i < result.noAttributes(); i++) {
 assertEquals(Math.sqrt(instance5.value(i)), result.value(i), 0.0001);
 }
 }
}