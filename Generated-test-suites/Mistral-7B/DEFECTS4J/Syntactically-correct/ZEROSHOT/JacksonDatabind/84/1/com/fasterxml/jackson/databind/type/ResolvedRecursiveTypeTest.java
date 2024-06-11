package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 java.lang.Class<?> erasedType = ResolvedRecursiveType.class;
 TypeBindings bindings = TypeBindings.empty();

 @Test
 public void testConstructors() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(erasedType, bindings);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(ResolvedRecursiveType.class, bindings);

 assertEquals(type1, type2);
 assertEquals(type1, type3);
 assertNotSame(type1, type4);
 }

 @Test
 public void testSetReference() {
 JavaType refType = TypeFactory.defaultInstance().constructType(Object.class);
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

 type.setReference(refType);

 assertEquals(refType, type.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType refType = TypeFactory.defaultInstance().constructType(Object.class);
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 type.setReference(refType);
 type.setReference(refType);
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 JavaType refType = TypeFactory.defaultInstance().constructType(Object.class);
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 type.setReference(refType);

 type.getGenericSignature(sb);

 String expected = "<type '[" + refType.getRawClass().getName() + ";']>";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder sb = new StringBuilder();
 JavaType refType = TypeFactory.defaultInstance().constructType(Object.class);
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 type.setReference(refType);

 type.getErasedSignature(sb);

 String expected = "[Ljava/lang/Object;";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testIsContainerType() {
 assertTrue(typeBaseInstance.isContainerType());
 assertFalse(type.isContainerType());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(erasedType, bindings);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(ResolvedRecursiveType.class, bindings);
 JavaType refType = TypeFactory.defaultInstance().constructType(Object.class);
 ResolvedRecursiveType type5 = new ResolvedRecursiveType(erasedType, bindings);
 type5.setReference(refType);

 assertEquals(type1, type2);
 assertEquals(type1, type3);
 assertNotSame(type1, type4);
 assertNotSame(type1, type5);
 }

}