package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testConstructor() {
 Class<?> erasedType = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 assertEquals(erasedType, type.getRawClass());
 assertNull(type.getSelfReferencedType());
 }

 @Test
 public void testSetReference() {
 Class<?> erasedType = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 JavaType ref = new TypeFactory().constructType(String.class);
 type.setReference(ref);
 JavaType selfRef = type.getSelfReferencedType();
 assertEquals(ref, selfRef);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 Class<?> erasedType = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 JavaType ref = new TypeFactory().constructType(String.class);
 type.setReference(ref);
 type.setReference(ref);
 }

 @Test
 public void testGetSelfReferencedType() {
 Class<?> erasedType = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 JavaType ref = new TypeFactory().constructType(String.class);
 type.setReference(ref);
 JavaType selfRef = type.getSelfReferencedType();
 assertEquals(ref, selfRef);
 }

 @Test
 public void testGetErasedSignature() {
 Class<?> erasedType = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 StringBuilder sb = new StringBuilder();
 JavaType ref = new TypeFactory().constructType(String.class);
 type.setReference(ref);
 type.getErasedSignature(sb);
 String expected = "L" + erasedType.getName().replaceAll("\\.", "_") + ";";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 Class<?> erasedType = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 JavaType ref = new TypeFactory().constructType(String.class);
 type.setReference(ref);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 String expected = "<T extends java.lang.Object>";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testEquals() {
 Class<?> erasedType1 = ResolvedRecursiveType.class;
 Class<?> erasedType2 = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings);
 JavaType ref1 = new TypeFactory().constructType(String.class);
 JavaType ref2 = new TypeFactory().constructType(Integer.class);
 type1.setReference(ref1);
 type2.setReference(ref2);
 assertFalse(type1.equals(type2));
 JavaType equalRef = new TypeFactory().constructType(String.class);
 type1.setReference(equalRef);
 type2.setReference(equalRef);
 assertTrue(type1.equals(type2));
 }

 @Test
 public void testHashCode() {
 Class<?> erasedType = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
 JavaType ref1 = new TypeFactory().constructType(String.class);
 type1.setReference(ref1);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
 JavaType ref2 = new TypeFactory().constructType(String.class);
 type2.setReference(ref2);
 assertEquals(type1.hashCode(), type2.hashCode());
 JavaType equalRef = new TypeFactory().constructType(String.class);
 type1.setReference(equalRef);
 type2.setReference(equalRef);
 assertEquals(type1.hashCode(), type2.hashCode());
 }
}