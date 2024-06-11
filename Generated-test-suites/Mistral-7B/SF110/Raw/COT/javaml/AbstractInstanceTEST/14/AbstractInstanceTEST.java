package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private Map<Integer, Double> map;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 Map<Integer, Double> mapVal1 = new HashMap<>();
 mapVal1.put(0, 1.0);
 mapVal1.put(1, 2.0);
 mapVal1.put(2, 3.0);

 map = mapVal1;
 instance = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 return map;
 }
 }.setClassValue("ClassValue1");

 instance2 = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal2 = new HashMap<>();
 mapVal2.put(0, 1.1);
 mapVal2.put(1, 2.1);
 mapVal2.put(2, 3.1);
 return mapVal2;
 }
 }.setClassValue("ClassValue2");

 instance3 = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal3 = new HashMap<>();
 mapVal3.put(0, null);
 mapVal3.put(1, 2.1);
 mapVal3.put(2, null);
 return mapVal3;
 }
 }.setClassValue("ClassValue3");
 }

 @Test
 public void testGetID() {
 AbstractInstance testInstance = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 return map;
 }
 }.setClassValue("ClassValueTest");

 assertEquals(instance.getID(), testInstance.getID());
 }

 @Test
 public void testIterator() {
 Double[] expected = {1.0, 2.0, 3.0};
 Double[] actual = new Double[instance.noAttributes()];

 Iterator<Double> iter = instance.iterator();
 int i = 0;
 while (iter.hasNext()) {
 actual[i] = iter.next();
 i++;
 }

 assertArrayEquals(expected, actual);
 }

 @Test
 public void testConstructor() {
 AbstractInstance testInstance = new AbstractInstance(null);
 assertNull(testInstance.getClassValue());

 AbstractInstance testInstance2 = new AbstractInstance("ClassValueTest2");
 assertEquals("ClassValueTest2", testInstance2.getClassValue());
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal4 = new HashMap<>();
 mapVal4.put(0, 1.0 - 1.1);
 mapVal4.put(1, 2.0 - 2.1);
 mapVal4.put(2, 3.0 - 3.1);
 return mapVal4;
 }
 }.setClassValue("Expected");

 AbstractInstance actual = instance.minus(instance2);
 assertEquals(expected.getClassValue(), actual.getClassValue());

 for (int i = 0; i < actual.noAttributes(); ++i) {
 assertEquals(expected.getAttributeValues().get(i), actual.getAttributeValues().get(i));
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMinusWithDouble_remove() {
 instance.minus(-1.0);
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal5 = new HashMap<>();
 mapVal5.put(0, 1.0 - 1.0);
 mapVal5.put(1, 2.0 - 1.0);
 mapVal5.put(2, 3.0 - 1.0);
 return mapVal5;
 }
 }.setClassValue("Expected");

 AbstractInstance actual = (AbstractInstance) instance.minus(1.0);
 assertEquals(expected.getClassValue(), actual.getClassValue());

 for (int i = 0; i < actual.noAttributes(); ++i) {
 assertEquals(expected.getAttributeValues().get(i), actual.getAttributeValues().get(i));
 }
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideWithDouble_divideByZero() {
 instance.divide(-1.0);
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal6 = new HashMap<>();
 mapVal6.put(0, 1.0 / 2.1);
 mapVal6.put(1, 2.0 / 2.1);
 mapVal6.put(2, 3.0 / 2.1);
 return mapVal6;
 }
 }.setClassValue("Expected");

 AbstractInstance actual = (AbstractInstance) instance.divide(instance2);
 assertEquals(expected.getClassValue(), actual.getClassValue());

 for (int i = 0; i < actual.noAttributes(); ++i) {
 assertEquals(expected.getAttributeValues().get(i), actual.getAttributeValues().get(i), 0.001);
 }
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal7 = new HashMap<>();
 mapVal7.put(0, 1.0 * 2.0);
 mapVal7.put(1, 2.0 * 2.0);
 mapVal7.put(2, 3.0 * 2.0);
 return mapVal7;
 }
 }.setClassValue("Expected");

 AbstractInstance actual = (AbstractInstance) instance.multiply(2.0);
 assertEquals(expected.getClassValue(), actual.getClassValue());

 for (int i = 0; i < actual.noAttributes(); ++i) {
 assertEquals(expected.getAttributeValues().get(i), actual.getAttributeValues().get(i));
 }
 }

 @Test
 public void testHashCode() {
 AbstractInstance testInstance = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 return map;
 }
 }.setClassValue("ClassValueTestHashCode");

 int expected = instance.hashCode();
 int actual = testInstance.hashCode();

 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 AbstractInstance testInstance = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 return map;
 }
 }.setClassValue("ClassValueTestEquals");

 assertTrue(instance.equals(testInstance));
 assertTrue(testInstance.equals(instance));

 AbstractInstance anotherInstance = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal8 = new HashMap<>();
 mapVal8.put(0, 1.1);
 mapVal8.put(1, 2.1);
 mapVal8.put(2, 3.1);
 return mapVal8;
 }
 }.setClassValue("AnotherClass");

 assertFalse(instance.equals(anotherInstance));
 assertFalse(anotherInstance.equals(instance));

 AbstractInstance testInstance2 = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 return map;
 }
 }.setClassValue("ClassValueTestEquals2");

 assertTrue(instance.equals(testInstance2));
 assertTrue(testInstance2.equals(instance));
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal9 = new HashMap<>();
 mapVal9.put(0, 1.0 * 1.1);
 mapVal9.put(1, 2.0 * 2.1);
 mapVal9.put(2, 3.0 * 3.1);
 return mapVal9;
 }
 }.setClassValue("Expected");

 AbstractInstance actual = (AbstractInstance) instance.multiply(instance2);
 assertEquals(expected.getClassValue(), actual.getClassValue());

 for (int i = 0; i < actual.noAttributes(); ++i) {
 assertEquals(expected.getAttributeValues().get(i), actual.getAttributeValues().get(i));
 }
 }

 @Test
 public void testDivideWithInstance_divideByZero() {
 try {
 instance.divide(instance3);
 fail("Should have thrown an ArithmeticException");
 } catch (ArithmeticException e) {
 // Expected
 }
 }

 @Test
 public void testAddWithDouble() {
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal10 = new HashMap<>();
 mapVal10.put(0, 1.0 + 1.0);
 mapVal10.put(1, 2.0 + 1.0);
 mapVal10.put(2, 3.0 + 1.0);
 return mapVal10;
 }
 }.setClassValue("Expected");

 AbstractInstance actual = (AbstractInstance) instance.add(1.0);
 assertEquals(expected.getClassValue(), actual.getClassValue());

 for (int i = 0; i < actual.noAttributes(); ++i) {
 assertEquals(expected.getAttributeValues().get(i), actual.getAttributeValues().get(i));
 }
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal11 = new HashMap<>();
 mapVal11.put(0, 1.0 + 1.1);
 mapVal11.put(1, 2.0 + 2.1);
 mapVal11.put(2, 3.0 + null);
 return mapVal11;
 }
 }.setClassValue("Expected");

 AbstractInstance actual = (AbstractInstance) instance.add(instance2);
 assertEquals(expected.getClassValue(), actual.getClassValue());

 for (int i = 0; i < actual.noAttributes(); ++i) {
 if (i != 2) {
 assertEquals(expected.getAttributeValues().get(i), actual.getAttributeValues().get(i));
 } else {
 assertNull(actual.getAttributeValues().get(i));
 }
 }
 }

 @Test
 public void testSqrt() {
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 Map<Integer, Double> mapVal12 = new HashMap<>();
 mapVal12.put(0, Math.sqrt(1.0));
 mapVal12.put(1, Math.sqrt(2.0));
 mapVal12.put(2, Math.sqrt(3.0));
 return mapVal12;
 }
 }.setClassValue("Expected");

 AbstractInstance actual = (AbstractInstance) instance.sqrt();
 assertEquals(expected.getClassValue(), actual.getClassValue());

 for (int i = 0; i < actual.noAttributes(); ++i) {
 assertEquals(expected.getAttributeValues().get(i), actual.getAttributeValues().get(i), 0.001);
 }
 }
}