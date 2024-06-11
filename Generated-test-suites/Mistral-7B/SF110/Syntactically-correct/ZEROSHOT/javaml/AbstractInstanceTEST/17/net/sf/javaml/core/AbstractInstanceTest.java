package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private Map<Integer, Double> map1;
 private Map<Integer, Double> map2;
 private Map<Integer, Double> map3;
 private Instance instanceOut;

 @Before
 public void setUp() {
 map1 = new HashMap<>();
 map2 = new HashMap<>();
 map3 = new HashMap<>();
 for (int i = 0; i < 10; i++) {
 map1.put(i, (double) i);
 map2.put(i, (double) (i + 1));
 map3.put(i, (double) (i + 2));
 }
 instance1 = new AbstractInstance() {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 return map1;
 }
 }.getInstance();
 instance2 = new AbstractInstance(map2.values().toArray(new Double[0])) {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 return map2;
 }
 }.getInstance();
 instance3 = new AbstractInstance(map3.values().toArray(new Double[0])) {
 @Override
 public Map<Integer, Double> getAttributeValues() {
 return map3;
 }
 }.getInstance();
 }

 @Test
 public void testGetID() {
 assertEquals(instance1.getID(), 0);
 assertEquals(instance2.getID(), AbstractInstance.nextID);
 assertEquals(instance3.getID(), AbstractInstance.nextID + 1);
 }

 @Test
 public void testIterator() {
 List<Double> list1 = new ArrayList<>();
 Iterator<Double> iterator1 = instance1.iterator();
 while (iterator1.hasNext()) {
 list1.add(iterator1.next());
 }
 assertEquals(list1.size(), instance1.noAttributes());
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(list1.get(i), instance1.value(i));
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveUsingIterator() {
 Iterator<Double> iterator1 = instance1.iterator();
 iterator1.next();
 iterator1.remove();
 }

 @Test
 public void testMinus() {
 instanceOut = instance1.minus(instance2);
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(instanceOut.value(i), instance1.value(i) - instance2.value(i), 0.0);
 }
 instanceOut = instance1.minus(1.0);
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(instanceOut.value(i), instance1.value(i) - 1.0, 0.0);
 }
 }

 @Test
 public void testDivide() {
 instanceOut = instance1.divide(2.0);
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(instanceOut.value(i), instance1.value(i) / 2.0, 0.0);
 }
 }

 @Test
 public void testMultiply() {
 instanceOut = instance1.multiply(2.0);
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(instanceOut.value(i), instance1.value(i) * 2.0, 0.0);
 }
 }

 @Test
 public void testEquals() {
 assertTrue(instance1.equals(instance1));
 assertFalse(instance1.equals(instance2));
 assertFalse(instance1.equals(instance3));
 assertFalse(instance1.equals(new Object()));
 }

 @Test
 public void testHashCode() {
 assertEquals(instance1.hashCode(), instance1.getID());
 }

 @Test
 public void testAdd() {
 instanceOut = instance1.add(1.0);
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(instanceOut.value(i), instance1.value(i) + 1.0, 0.0);
 }
 instanceOut = instance1.add(instance2);
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(instanceOut.value(i), instance1.value(i) + instance2.value(i), 0.0);
 }
 }

 @Test
 public void testSqrt() {
 instanceOut = instance1.sqrt();
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(Math.sqrt(instance1.value(i)), instanceOut.value(i), 0.0);
 }
 }
}