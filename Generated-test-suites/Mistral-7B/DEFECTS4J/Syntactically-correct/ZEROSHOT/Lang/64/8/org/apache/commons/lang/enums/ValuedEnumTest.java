package org.apache.commons.lang.enums;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 ValuedEnum java1_0 = new ValuedEnumTest.JavaVersionEnum("Java 1.0", 100);
 ValuedEnum java1_1 = new ValuedEnumTest.JavaVersionEnum("Java 1.1", 110);
 ValuedEnum java1_2 = new ValuedEnumTest.JavaVersionEnum("Java 1.2", 120);
 ValuedEnum java1_3 = new ValuedEnumTest.JavaVersionEnum("Java 1.3", 130);

 @Test
 public void testGetEnumByValue() {
 ValuedEnum javaVersion = ValuedEnum.getEnum(ValuedEnum.class, 100);
 assertEquals("Java 1.0", javaVersion.getName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumByNullClass() {
 ValuedEnum javaVersion = ValuedEnum.getEnum(null, 100);
 fail("Expected NullPointerException but got: " + javaVersion);
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumByNullValue() {
 ValuedEnum javaVersion = ValuedEnum.getEnum(ValuedEnum.class, null);
 fail("Expected NullPointerException but got: " + javaVersion);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumByNegativeValue() {
 ValuedEnum javaVersion = ValuedEnum.getEnum(ValuedEnum.class, -1);
 fail("Expected IllegalArgumentException but got: " + javaVersion);
 }

 @Test
 public void testGetEnumByValueNotExist() {
 ValuedEnum javaVersion = ValuedEnum.getEnum(ValuedEnum.class, 150);
 assertNull(javaVersion);
 }

 @Test
 public void testGetEnumByValueExist() {
 ValuedEnum javaVersion = ValuedEnum.getEnum(ValuedEnum.class, 110);
 assertEquals("Java 1.1", javaVersion.getName());
 }

 @Test
 public void testGetEnumByValueWithReflection() throws NoSuchMethodException,
 IllegalAccessException, InvocationTargetException {
 ValuedEnum javaVersion = (ValuedEnum) ValuedEnum.getEnum(ValuedEnum.class, 110,
 new Object[]{}, new Class[]{}).get(0);
 assertEquals("Java 1.1", javaVersion.getName());
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
 Map<ValuedEnum, Integer> javaVersionMap = ValuedEnum.getEnumMap(ValuedEnum.class);
 for (ValuedEnum javaVersion : javaVersions) {
 assertEquals(javaVersion, javaVersionMap.get(javaVersion));
 }
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
 assertTrue(javaVersions.contains(java1_0));
 assertTrue(javaVersions.contains(java1_1));
 assertTrue(javaVersions.contains(java1_2));
 assertTrue(javaVersions.contains(java1_3));
 }

 @Test
 public void testIterator() {
 List<ValuedEnum> javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
 Iterator<ValuedEnum> iterator = ValuedEnum.iterator(ValuedEnum.class);
 while (iterator.hasNext()) {
 ValuedEnum javaVersion = iterator.next();
 assertTrue(javaVersions.contains(javaVersion));
 }
 }

 @Test
 public void testCompareTo() {
 int comparison = java1_0.compareTo(java1_1);
 assertTrue(comparison < 0);

 comparison = java1_1.compareTo(java1_0);
 assertTrue(comparison > 0);

 comparison = java1_0.compareTo(java1_0);
 assertEquals(0, comparison);
 }

 @Test
 public void testCompareToDifferentClass() {
 try {
 ValuedEnum javaVersion1 = new ValuedEnumTest.JavaVersionEnum("Java 1.4", 140);
 ValuedEnum javaVersion2 = new ValuedEnumTest.JavaVersionEnum("Java 1.5", 150);
 int comparison = javaVersion1.compareTo(javaVersion2);
 fail("Expected ClassCastException but got: " + comparison);
 } catch (ClassCastException e) {
 // pass
 }
 }

 @Test
 public void testToString() {
 assertEquals("ValuedEnum[Java 1.0=100]", java1_0.toString());
 }

 @Test
 public void testGetName() {
 assertEquals("Java 1.0", java1_0.getName());
 }

 @Test
 public void testGetValue() {
 assertEquals(100, java1_0.getValue());
 }

 @Test
 public void testGetValueByReflection() throws NoSuchMethodException,
 IllegalAccessException, InvocationTargetException {
 Object value = ValuedEnum.class.getDeclaredMethod("getValue").invoke(java1_0);
 assertEquals(100, value.intValue());
 }

}

class JavaVersionEnum extends ValuedEnum {
 ValuedEnum(String name, int value) {
 super(name, value);
 }
}