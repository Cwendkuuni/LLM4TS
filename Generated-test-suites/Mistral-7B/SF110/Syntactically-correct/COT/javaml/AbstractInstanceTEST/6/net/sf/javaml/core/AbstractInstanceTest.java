package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance;
 private AbstractInstance instance2;
 private Instance testInstance;

 @Before
 public void setUp() {
 // Initialize test instances
 Map<K, Double> map = new HashMap<>();
 map.put(new Integer(0), 1.0);
 map.put(new Integer(1), 2.0);
 instance = new AbstractInstance(map);
 map.clear();
 map.put(new Integer(0), 3.0);
 map.put(new Integer(1), 4.0);
 instance2 = new AbstractInstance(map);
 testInstance = new DenseInstance(new double[]{1.0, 2.0});
 }

 @Test
 public void testGetID() {
 AbstractInstance abstractInstance = new AbstractInstance(null);
 assertEquals(abstractInstance.getID(), 0);
 instance = new AbstractInstance(null);
 assertEquals(instance.getID(), AbstractInstance.nextID);
 AbstractInstance.nextID++;
 abstractInstance = new AbstractInstance(null);
 assertEquals(abstractInstance.getID(), AbstractInstance.nextID);
 }

 @Test
 public void testIterator() {
 Iterator<Double> iterator = instance.iterator();
 List<Double> list = new ArrayList<>();
 while (iterator.hasNext()) {
 list.add(iterator.next());
 }
 assertEquals(instance.noAttributes(), list.size());
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.value(i), list.get(i));
 }
 }

 @Test
 public void testConstructorWithNullClassValue() {
 assertNotNull(new AbstractInstance());
 }

 @Test
 public void testConstructorWithClassValue() {
 assertNotNull(new AbstractInstance(new Object()));
 }

 @Test
 public void testClassValue() {
 assertEquals(instance.classValue(), null);
 instance.setClassValue(new Object());
 assertEquals(instance.classValue(), new Object());
 }

 @Test
 public void testSetClassValue() {
 instance.setClassValue("Test Value");
 assertEquals(instance.classValue(), "Test Value");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMinusWithNullInstance() {
 instance.minus(null);
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance instance3 = new AbstractInstance(new HashMap<>());
 AbstractInstance expected = instance.minus(instance2);
 Map<K, Double> map3 = new HashMap<>();
 map3.putAll((Map<? extends K, ? extends Double>) instance3.iterator());
 for (int i = 0; i < instance.noAttributes(); i++) {
 map3.put(new Integer(i), ((Map<K, Double>) instance).get(i) - ((Map<K, Double>) instance2).get(i));
 }
 AbstractInstance result = new AbstractInstance(map3);
 assertEquals(expected, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMinusWithDouble() {
 instance.minus(1.0);
 }

 @Test
 public void testMinusWithDouble_Instance() {
 AbstractInstance instance3 = new AbstractInstance(new HashMap<>());
 AbstractInstance expected = instance.minus(1.0, instance3);
 Map<K, Double> map3 = new HashMap<>();
 map3.putAll((Map<? extends K, ? extends Double>) instance3.iterator());
 for (int i = 0; i < instance.noAttributes(); i++) {
 map3.put(new Integer(i), ((Map<K, Double>) instance).get(i) - 1.0);
 }
 AbstractInstance result = new AbstractInstance(map3);
 assertEquals(expected, result);
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideWithZero() {
 instance.divide(0.0);
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance instance3 = new AbstractInstance(new HashMap<>());
 AbstractInstance expected = instance.divide(instance2);
 Map<K, Double> map3 = new HashMap<>();
 map3.putAll((Map<? extends K, ? extends Double>) instance3.iterator());
 for (int i = 0; i < instance.noAttributes(); i++) {
 map3.put(new Integer(i), ((Map<K, Double>) instance).get(i) / ((Map<K, Double>) instance2).get(i));
 }
 AbstractInstance result = new AbstractInstance(map3);
 assertEquals(expected, result);
 }

 @Test(expected = ArithmeticException.class)
 public void testMultiplyWithZero() {
 instance.multiply(0.0);
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance instance3 = new AbstractInstance(new HashMap<>());
 AbstractInstance expected = instance.multiply(instance2);
 Map<K, Double> map3 = new HashMap<>();
 map3.putAll((Map<? extends K, ? extends Double>) instance3.iterator());
 for (int i = 0; i < instance.noAttributes(); i++) {
 map3.put(new Integer(i), ((Map<K, Double>) instance).get(i) * ((Map<K, Double>) instance2).get(i));
 }
 AbstractInstance result = new AbstractInstance(map3);
 assertEquals(expected, result);
 }

 @Test
 public void testAddWithDouble() {
 AbstractInstance expected = instance.add(1.0);
 Map<K, Double> map3 = new HashMap<>();
 map3.putAll((Map<? extends K, ? extends Double>) expected.iterator());
 AbstractInstance result = new AbstractInstance(map3);
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(((Map<K, Double>) instance).get(i) + 1.0, ((Map<K, Double>) result).get(i));
 }
 assertEquals(result, expected);
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance expected = instance.add(instance2);
 Map<K, Double> map3 = new HashMap<>();
 map3.putAll((Map<? extends K, ? extends Double>) expected.iterator());
 AbstractInstance result = new AbstractInstance(map3);
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(((Map<K, Double>) instance).get(i) + ((Map<K, Double>) instance2).get(i), ((Map<K, Double>) result).get(i));
 }
 assertEquals(result, expected);
 }

 @Test
 public void testSqrt() {
 AbstractInstance expected = instance.sqrt();
 Map<K, Double> map3 = new HashMap<>();
 for (int i = 0; i < instance.noAttributes(); i++) {
 map3.put(new Integer(i), Math.sqrt(((Map<K, Double>) instance).get(i)));
 }
 AbstractInstance result = new AbstractInstance(map3);
 assertEquals(expected, result);
 }

}