package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance;
 private AbstractInstance instance2;
 private double minValue = 1.0;
 private double value = 2.0;
 private Map<Object, Double> map = new HashMap<>();

 @Test
 public void testConstructor() {
 // Test default constructor
 instance = new AbstractInstance();
 assertEquals(AbstractInstance.nextID, instance.getID());
 assertNull(instance.classValue());

 // Test constructor with classValue
 instance2 = new AbstractInstance(new Object());
 assertEquals(AbstractInstance.nextID, instance2.getID());
 assertSame(instance2.classValue(), new Object());
 }

 @Test
 public void testGetID() {
 instance = new AbstractInstance();
 assertEquals(AbstractInstance.nextID, instance.getID());
 instance2 = new AbstractInstance();
 assertEquals(AbstractInstance.nextID + 1, instance2.getID());
 }

 @Test
 public void testIterator() {
 // Add values to the instance
 for (int i = 0; i < 10; i++) {
 map.put(new Object(), new Double(i+1));
 }
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 return map;
 }
 };

 // Test hasNext and next methods
 Iterator<Double> it = instance.iterator();
 assertTrue(it.hasNext());
 assertEquals(1.0, it.next().doubleValue(), 0);
 // ... repeat for all values
 assertFalse(it.hasNext());
 }

 @Test
 public void testMinusWithInstance() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(5.0));
 map.put(new Object(), new Double(3.0));
 return map;
 }
 };
 instance2 = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(2.0));
 map.put(new Object(), new Double(4.0));
 return map;
 }
 };

 AbstractInstance result = instance.minus(instance2);
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(1.0));
 return map;
 }
 };

 assertEquals(expected, result);
 }

 @Test
 public void testMinusWithDouble() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(5.0));
 map.put(new Object(), new Double(3.0));
 return map;
 }
 };

 AbstractInstance result = instance.minus(minValue);
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(4.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };

 assertEquals(expected, result);
 }

 @Test
 public void testDivideWithInstance() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(10.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };
 instance2 = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(2.0));
 map.put(new Object(), new Double(3.0));
 return map;
 }
 };

 AbstractInstance result = instance.divide(instance2);
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(5.5));
 map.put(new Object(), new Double(0.6666666666666666));
 return map;
 }
 };

 assertEquals(expected, result);
 }

 @Test
 public void testDivideWithDouble() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(10.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };

 AbstractInstance result = instance.divide(minValue);
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(5.0));
 map.put(new Object(), new Double(0.5));
 return map;
 }
 };

 assertEquals(expected, result);
 }

 @Test
 public void testMultiplyWithInstance() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(3.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };
 instance2 = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(4.0));
 map.put(new Object(), new Double(5.0));
 return map;
 }
 };

 AbstractInstance result = instance.multiply(instance2);
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(18.0));
 map.put(new Object(), new Double(12.0));
 return map;
 }
 };

 assertEquals(expected, result);
 }

 @Test
 public void testMultiplyWithDouble() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(3.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };

 AbstractInstance result = instance.multiply(minValue);
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(3.0));
 map.put(new Object(), new Double(minValue));
 return map;
 }
 };

 assertEquals(expected, result);
 }

 @Test
 public void testEquals() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(1.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };
 instance2 = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(1.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };
 AbstractInstance instance3 = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(1.0));
 map.put(new Object(), new Double(3.0));
 return map;
 }
 };

 assertTrue(instance.equals(instance2));
 assertFalse(instance.equals(instance3));
 }

 @Test
 public void testHashCode() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(1.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };
 instance2 = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(1.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };

 assertEquals(instance.getID(), instance.hashCode());
 assertEquals(instance.hashCode(), instance2.hashCode());
 }

 @Test
 public void testMinusWithDoubleAndWithInstanceShouldReturnSameResult() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(5.0));
 map.put(new Object(), new Double(3.0));
 return map;
 }
 };
 instance2 = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(2.0));
 map.put(new Object(), new Double(4.0));
 return map;
 }
 };

 AbstractInstance result1 = instance.minus(instance2.getID());
 AbstractInstance result2 = instance.minus(instance2);

 assertEquals(result1, result2);
 }

 @Test
 public void testMinusWithDoubleAndWithInstanceShouldReturnNegativeResultWhenInstanceIsSubtracted() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(5.0));
 map.put(new Object(), new Double(3.0));
 return map;
 }
 };
 instance2 = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(2.0));
 map.put(new Object(), new Double(4.0));
 return map;
 }
 };

 AbstractInstance result1 = instance.minus(instance2.getID());
 AbstractInstance result2 = instance2.minus(instance);

 assertEquals(result1, result2.negate());
 }

 @Test
 public void testAddWithDoubleAndWithInstanceShouldReturnSameResult() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(3.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };
 instance2 = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(4.0));
 map.put(new Object(), new Double(5.0));
 return map;
 }
 };

 AbstractInstance result1 = instance.add(instance2.getID());
 AbstractInstance result2 = instance.add(instance2);

 assertEquals(result1, result2);
 }

 @Test
 public void testAddWithDoubleAndWithInstanceShouldReturnSumOfBothInstances() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(3.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };
 instance2 = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(4.0));
 map.put(new Object(), new Double(5.0));
 return map;
 }
 };

 AbstractInstance result = instance.add(instance2);
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(11.0));
 map.put(new Object(), new Double(7.0));
 return map;
 }
 };

 assertEquals(expected, result);
 }

 @Test
 public void testSqrt() {
 instance = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(9.0));
 map.put(new Object(), new Double(4.0));
 return map;
 }
 };

 AbstractInstance result = instance.sqrt();
 AbstractInstance expected = new AbstractInstance() {
 @Override
 public Map<Object, Double> getAttributeMap() {
 map = new HashMap<>();
 map.put(new Object(), new Double(3.0));
 map.put(new Object(), new Double(2.0));
 return map;
 }
 };

 assertEquals(expected, result);
 }
 }