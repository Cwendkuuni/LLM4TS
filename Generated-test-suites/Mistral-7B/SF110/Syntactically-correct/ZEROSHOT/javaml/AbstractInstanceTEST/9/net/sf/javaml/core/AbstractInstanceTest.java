package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class AbstractInstanceTest {

 @Test
 public void testGetID() {
 AbstractInstance instance = new AbstractInstance();
 assertEquals(0, instance.getID());

 instance = new AbstractInstance(new Object());
 assertEquals(1, instance.getID());
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = new AbstractInstance(); // empty instance
 Iterator<Double> iterator = instance.iterator();
 assertFalse(iterator.hasNext());

 instance = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 1.0);
 put("attribute2", 2.0);
 put("attribute3", 3.0);
 }});
 iterator = instance.iterator();
 assertEquals(3, instance.noAttributes());
 assertTrue(iterator.hasNext());
 assertEquals(1.0, iterator.next(), 0.0);
 assertTrue(iterator.hasNext());
 assertEquals(2.0, iterator.next(), 0.0);
 assertTrue(iterator.hasNext());
 assertEquals(3.0, iterator.next(), 0.0);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testProtectedConstructor() {
 AbstractInstance instance = new AbstractInstance();
 assertNotNull(instance);
 instance = new AbstractInstance(new Object());
 assertNotNull(instance);
 }

 @Test
 public void testClassValue() {
 AbstractInstance instance = new AbstractInstance();
 assertNull(instance.classValue());

 instance = new AbstractInstance(new Object());
 assertEquals(instance.classValue(), instance.classValue());
 }

 @Test
 public void testSetClassValue() {
 AbstractInstance instance = new AbstractInstance();
 assertNull(instance.classValue());

 instance.setClassValue("Test Class");
 assertEquals("Test Class", instance.classValue());
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance instance1 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 1.0);
 put("attribute2", 2.0);
 put("attribute3", 3.0);
 }});
 AbstractInstance instance2 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", -1.0);
 put("attribute2", -2.0);
 put("attribute3", -3.0);
 }});

 AbstractInstance result = instance1.minus(instance2);
 assertEquals(instance1.noAttributes(), result.noAttributes());
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(instance1.value(i) - instance2.value(i), result.value(i), 0.0);
 }
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance instance = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 1.0);
 put("attribute2", 2.0);
 put("attribute3", 3.0);
 }});

 AbstractInstance result = instance.minus(1.0);
 assertEquals(instance.noAttributes(), result.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.value(i) - 1.0, result.value(i), 0.0);
 }
 }

 @Test
 public void testDivideWithDouble() {
 AbstractInstance instance = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 5.0);
 put("attribute2", 10.0);
 put("attribute3", 15.0);
 }});

 AbstractInstance result = instance.divide(3.0);
 assertEquals(instance.noAttributes(), result.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.value(i) / 3.0, result.value(i), 0.0);
 }
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance instance = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 2.0);
 put("attribute2", 3.0);
 put("attribute3", 4.0);
 }});

 AbstractInstance result = instance.multiply(5.0);
 assertEquals(instance.noAttributes(), result.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.value(i) * 5.0, result.value(i), 0.0);
 }
 }

 @Test
 public void testHashCode() {
 AbstractInstance instance1 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 1.0);
 put("attribute2", 2.0);
 put("attribute3", 3.0);
 }});
 AbstractInstance instance2 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 1.0);
 put("attribute2", 2.0);
 put("attribute3", 3.0);
 }});
 assertEquals(instance1.hashCode(), instance2.hashCode());

 instance1.setID(1);
 instance2.setID(2);
 assertNotEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testEquals() {
 AbstractInstance instance1 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 1.0);
 put("attribute2", 2.0);
 put("attribute3", 3.0);
 }});
 AbstractInstance instance2 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 1.0);
 put("attribute2", 2.0);
 put("attribute3", 3.0);
 }});
 assertTrue(instance1.equals(instance2));

 instance1.setID(1);
 instance2.setID(2);
 assertFalse(instance1.equals(instance2));

 instance2 = new AbstractInstance(instance1);
 assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance instance1 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 2.0);
 put("attribute2", 3.0);
 put("attribute3", 4.0);
 }});
 AbstractInstance instance2 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 5.0);
 put("attribute2", 6.0);
 put("attribute3", 7.0);
 }});

 AbstractInstance result = instance1.multiply(instance2);
 assertEquals(instance1.noAttributes(), result.noAttributes());
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(instance1.value(i) * instance2.value(i), result.value(i), 0.0);
 }
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance instance1 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 10.0);
 put("attribute2", 5.0);
 put("attribute3", 15.0);
 }});
 AbstractInstance instance2 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 2.0);
 put("attribute2", 3.0);
 put("attribute3", 5.0);
 }});

 AbstractInstance result = instance1.divide(instance2);
 assertEquals(instance1.noAttributes(), result.noAttributes());
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(instance1.value(i) / instance2.value(i), result.value(i), 0.0);
 }
 }

 @Test
 public void testAddWithDouble() {
 AbstractInstance instance = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 2.0);
 put("attribute2", 3.0);
 put("attribute3", 4.0);
 }});

 AbstractInstance result = instance.add(1.0);
 assertEquals(instance.noAttributes(), result.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.value(i) + 1.0, result.value(i), 0.0);
 }
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance instance1 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 2.0);
 put("attribute2", 3.0);
 put("attribute3", 4.0);
 }});
 AbstractInstance instance2 = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 1.0);
 put("attribute2", 2.0);
 put("attribute3", 3.0);
 }});

 AbstractInstance result = instance1.add(instance2);
 assertEquals(instance1.noAttributes(), result.noAttributes());
 for (int i = 0; i < instance1.noAttributes(); i++) {
 assertEquals(instance1.value(i) + instance2.value(i), result.value(i), 0.0);
 }
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = new AbstractInstance(new HashMap<String, Double>() {{
 put("attribute1", 9.0);
 put("attribute2", 16.0);
 put("attribute3", 25.0);
 }});

 AbstractInstance result = instance.sqrt();
 assertEquals(instance.noAttributes(), result.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(Math.sqrt(instance.value(i)), result.value(i), 0.0);
 }
 }
}