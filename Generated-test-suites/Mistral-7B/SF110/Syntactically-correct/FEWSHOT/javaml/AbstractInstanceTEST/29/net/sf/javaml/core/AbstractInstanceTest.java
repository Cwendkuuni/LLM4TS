package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractInstanceTest {

 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
 AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.0, 3.0});

 @Test
 public void testNextID() {
 int id1 = instance1.getID();
 int id2 = instance2.getID();
 int id3 = instance3.getID();
 assertEquals(0, id1);
 assertEquals(1, id2);
 assertEquals(2, id3);
 }

 @Test
 public void testIterator() {
 Iterator<Double> it = instance1.iterator();
 assertEquals(3, it.next());
 assertEquals(2.0, it.next());
 assertEquals(1.0, it.next());
 assertFalse(it.hasNext());
 }

 @Test
 public void testMinusInstance() {
 AbstractInstance result = instance1.minus(instance2);
 AbstractInstance expected = new DenseInstance(new double[]{-3.0, -1.0, -3.0});
 assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
 }

 @Test
 public void testMinusDouble() {
 AbstractInstance result = instance1.minus(1.0);
 AbstractInstance expected = new DenseInstance(new double[]{0.0, -1.0, 2.0});
 assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
 }

 @Test
 public void testDivideDouble() {
 AbstractInstance result = instance1.divide(2.0);
 AbstractInstance expected = new DenseInstance(new double[]{0.5, 1.0, 1.5});
 assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
 }

 @Test
 public void testMultiplyDouble() {
 AbstractInstance result = instance1.multiply(2.0);
 AbstractInstance expected = new DenseInstance(new double[]{2.0, 4.0, 6.0});
 assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
 }

 @Test
 public void testEquals() {
 assertTrue(instance1.equals(instance3));
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testMinusDoubleAndInstance() {
 AbstractInstance result = instance1.minus(instance2.minus(instance1));
 AbstractInstance expected = new DenseInstance(new double[]{0.0, 0.0, 0.0});
 assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
 }

 @Test
 public void testMultiplyInstance() {
 AbstractInstance result = instance1.multiply(instance2);
 AbstractInstance expected = new DenseInstance(new double[]{4.0, 10.0, 18.0});
 assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
 }

 @Test
 public void testDivideInstance() {
 AbstractInstance result = instance1.divide(instance2);
 AbstractInstance expected = new DenseInstance(new double[]{0.25, 0.4, 0.5});
 assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
 }

 @Test
 public void testAddDouble() {
 AbstractInstance result = instance1.add(1.0);
 AbstractInstance expected = new DenseInstance(new double[]{2.0, 3.0, 4.0});
 assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
 }

 @Test
 public void testAddInstance() {
 AbstractInstance result = instance1.add(instance2);
 AbstractInstance expected = new DenseInstance(new double[]{5.0, 7.0, 9.0});
 assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
 }

 @Test
 public void testSqrt() {
 AbstractInstance result = instance1.sqrt();
 AbstractInstance expected = new DenseInstance(new double[]{1.0, 1.4142135623730955, 1.7320508075688772});
 assertArrayEquals(expected.toArray(), result.toArray(), 0.001);
 }
}