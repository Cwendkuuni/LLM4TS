package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

 private Class<?> simpleClass = String.class;
 private SimpleType simpleType;
 private SimpleType simpleType2;
 private SimpleType simpleType3;
 private SimpleType simpleType4;

 @Test
 public void testSimpleTypeConstruction() {
 simpleType = new SimpleType(simpleClass);
 }

 @Test
 public void testConstructUnsafe() {
 simpleType2 = SimpleType.constructUnsafe(simpleClass);
 assertEquals(simpleType.getClass(), simpleType2.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructDeprecated() {
 simpleType3 = SimpleType.construct(simpleClass);
 fail("Should not construct SimpleType using deprecated method");
 }

 @Test
 public void testNarrow() {
 simpleType4 = new SimpleType(String[].class);
 simpleType = new SimpleType(simpleClass);
 assertEquals(simpleType, simpleType.narrow(String.class));
 assertNotEquals(simpleType4, simpleType.narrow(String[].class));
 }

 @Test
 public void testEquals() {
 SimpleType simpleType5 = new SimpleType(simpleClass);
 assertEquals(simpleType, simpleType5);
 SimpleType simpleType6 = new SimpleType(Object.class);
 assertNotEquals(simpleType, simpleType6);
 }

 @Test
 public void testIsContainerType() {
 assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testToString() {
 String expected = "[simple type, class [" + simpleClass.getName() + "]]";
 assertEquals(expected, simpleType.toString());
 }

 @Test
 public void testBuildCanonicalName() {
 StringBuilder expected = new StringBuilder("[simple type, class ");
 expected.append(simpleClass.getName()).append(']');
 assertEquals(expected.toString(), simpleType.buildCanonicalName());
 }
}