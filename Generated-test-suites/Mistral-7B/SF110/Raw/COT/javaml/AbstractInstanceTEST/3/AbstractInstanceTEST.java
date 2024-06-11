package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private AbstractInstance instance;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private Map<Integer, Double> map;

 @Before
 public void setUp() {
 map = new HashMap<>();
 instance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, map);
 instance3 = new DenseInstance(new double[] {7.0, 8.0, 9.0}, map);
 }

 @Test
 public void testGetID() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 assertEquals(testInstance.getID(), testInstance.ID);
 }

 @Test
 public void testIterator() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 Iterator<Double> iterator = testInstance.iterator();
 List<Double> list = new ArrayList<>();
 while (iterator.hasNext()) {
 list.add(iterator.next());
 }
 assertEquals(list.size(), testInstance.noAttributes());
 }

 @Test
 public void testConstructorNoArguments() {
 AbstractInstance testInstance = new AbstractInstance();
 assertNotNull(testInstance);
 }

 @Test
 public void testConstructorWithArguments() {
 AbstractInstance testInstance = new AbstractInstance(new Object() {});
 assertNotNull(testInstance);
 }

 @Test
 public void testClassValue() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 Object classValue = new Object();
 testInstance.setClassValue(classValue);
 assertEquals(classValue, testInstance.classValue());
 }

 @Test
 public void testSetClassValue() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 Object classValue = new Object();
 testInstance.setClassValue(classValue);
 assertEquals(classValue, testInstance.classValue());
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance result = instance.minus(instance2);
 assertNotNull(result);
 assertArrayEquals(new double[] {-3.0, -3.0, -3.0}, result.toArray(), 0.001);
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance result = instance.minus(3.0);
 assertNotNull(result);
 assertArrayEquals(new double[] {-2.0, -1.0, 0.0}, result.toArray(), 0.001);
 }

 @Test
 public void testDivideWithDouble() {
 AbstractInstance result = instance.divide(3.0);
 assertNotNull(result);
 assertArrayEquals(new double[] {0.3333333333333333, 0.6666666666666666, 1.0}, result.toArray(), 0.001);
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance result = instance.multiply(3.0);
 assertNotNull(result);
 assertArrayEquals(new double[] {3.0, 6.0, 9.0}, result.toArray(), 0.001);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testHashCode() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 int hashCode1 = testInstance.hashCode();
 AbstractInstance testInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 int hashCode2 = testInstance2.hashCode();
 assertNotSame(hashCode1, hashCode2);
 }

 @Test
 public void testEquals() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 AbstractInstance testInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 assertTrue(testInstance.equals(testInstance2));
 }

 @Test
 public void testEqualsFalse() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 AbstractInstance testInstance2 = new DenseInstance(new double[] {1.0, 3.0, 3.0}, map);
 assertFalse(testInstance.equals(testInstance2));
 }

 @Test
 public void testMinusWithInstanceAndMinusWithDoubleEdgeCase() {
 AbstractInstance result = instance3.minus(instance2);
 assertNotNull(result);
 assertArrayEquals(new double[] {2.0, 3.0, 6.0}, result.toArray(), 0.001);

 AbstractInstance result2 = instance3.minus(new DenseInstance(new double[] {1.0, 1.0, 1.0}, map));
 assertNotNull(result2);
 assertArrayEquals(new double[] {2.0, 2.0, 8.0}, result2.toArray(), 0.001);
 }

 @Test
 public void testDivideWithInstanceAndDivideWithDoubleEdgeCase() {
 AbstractInstance result = instance3.divide(instance2);
 assertNotNull(result);
 assertArrayEquals(new double[] {1.5, 1.25, 2.75}, result.toArray(), 0.001);

 AbstractInstance result2 = instance3.divide(new DenseInstance(new double[] {1.0, 1.0, 1.0}, map));
 assertNotNull(result2);
 assertArrayEquals(new double[] {7.0, 8.0, 9.0}, result2.toArray(), 0.001);
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 AbstractInstance result = testInstance.divide(0.0);
 }

 @Test(expected = ArithmeticException.class)
 public void testSubtractByNegativeNumber() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 AbstractInstance result = testInstance.minus(-1.0);
 }

 @Test(expected = NullPointerException.class)
 public void testNullIterator() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 Iterator<Double> nullIterator = null;
 assertFalse(testInstance.iterator().hasNext());
 assertThrows(NullPointerException.class, () -> testInstance.iterator(nullIterator));
 }

 @Test(expected = ClassCastException.class)
 public void testClassCastException() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, map);
 Object wrongClass = new ArrayList<>();
 assertFalse(testInstance.iterator().hasNext());
 assertThrows(ClassCastException.class, () -> (AbstractInstance) wrongClass);
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance result = instance.multiply(instance2);
 assertNotNull(result);
 assertArrayEquals(new double[] {5.0, 11.0, 15.0}, result.toArray(), 0.001);
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance result = instance3.divide(instance2);
 assertNotNull(result);
 assertArrayEquals(new double[] {1.5, 0.6, 1.3333333333333333}, result.toArray(), 0.001);
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance result = instance.add(instance2);
 assertNotNull(result);
 assertArrayEquals(new double[] {5.0, 7.0, 6.0}, result.toArray(), 0.001);
 }

 @Test
 public void testAddWithInstanceEdgeCase() {
 AbstractInstance result = new DenseInstance(new double[] {1.0, 1.0, 1.0}, map).add(instance);
 assertNotNull(result);
 assertArrayEquals(new double[] {1.0, 2.0, 4.0}, result.toArray(), 0.001);
 }

 @Test
 public void testAddWithDouble() {
 AbstractInstance result = instance.add(3.0);
 assertNotNull(result);
 assertArrayEquals(new double[] {4.0, 5.0, 6.0}, result.toArray(), 0.001);
 }

 @Test
 public void testAddWithDoubleEdgeCase() {
 AbstractInstance result = new DenseInstance(new double[] {1.0, 1.0, 1.0}, map).add(3.0);
 assertNotNull(result);
 assertArrayEquals(new double[] {4.0, 4.0, 4.0}, result.toArray(), 0.001);
 }

 @Test
 public void testSqrtWithDoubleEdgeCase() {
 AbstractInstance result = new DenseInstance(new double[] {1.0, 1.0, 1.0}, map).sqrt();
 assertNotNull(result);
 assertArrayEquals(new double[] {1.0, 1.0, 1.0}, result.toArray(), 0.001);
 }

 @Test
 public void testSqrtWithNegativeNumber() {
 AbstractInstance testInstance = new DenseInstance(new double[] {1.0, 2.0, -3.0}, map);
 Assertions.assertThrows(IllegalArgumentException.class, () -> testInstance.sqrt());
 }
}