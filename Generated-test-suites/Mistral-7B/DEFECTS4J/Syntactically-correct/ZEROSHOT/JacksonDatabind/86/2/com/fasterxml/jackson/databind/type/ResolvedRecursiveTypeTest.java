package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 private JavaType referencedTypeMock;
 private ResolvedRecursiveType testedInstance;

 @Before
 public void setUp() {
 referencedTypeMock = mock(JavaType.class);
 testedInstance = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
 private static final long serialVersionUID = 1L;

 @Override
 public JavaType getSelfReferencedType() {
 return referencedTypeMock;
 }
 };
 }

 @Test
 public void test_instantiation() {
 // test basic instantiation
 ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertNotNull(instance);
 // test with provided referenced type
 ResolvedRecursiveType instanceWithRef = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
 private static final long serialVersionUID = 1L;

 @Override
 public JavaType getSelfReferencedType() {
 return referencedTypeMock;
 }
 };
 instanceWithRef.setReference(referencedTypeMock);
 assertNotNull(instanceWithRef.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void test_multiple_reference_setting() {
 // test multiple reference setting
 testedInstance.setReference(referencedTypeMock);
 testedInstance.setReference(mock(JavaType.class));
 }

 @Test
 public void test_getSelfReferencedType() {
 // test getSelfReferencedType with null reference
 assertNull(testedInstance.getSelfReferencedType());
 // test getSelfReferencedType with set reference
 testedInstance.setReference(referencedTypeMock);
 assertEquals(referencedTypeMock, testedInstance.getSelfReferencedType());
 }

 @Test
 public void test_getGenericSignature() {
 // test getGenericSignature with null reference
 testedInstance.getGenericSignature(new StringBuilder());
 // test getGenericSignature with set reference
 StringBuilder sb = new StringBuilder();
 when(referencedTypeMock.getGenericSignature(sb)).thenReturn(sb);
 testedInstance.setReference(referencedTypeMock);
 testedInstance.getGenericSignature(sb);
 assertEquals(sb.toString(), testedInstance.getSelfReferencedType().getGenericSignature(new StringBuilder()).toString());
 }

 @Test
 public void test_getErasedSignature() {
 // test getErasedSignature with null reference
 testedInstance.getErasedSignature(new StringBuilder());
 // test getErasedSignature with set reference
 StringBuilder sb = new StringBuilder();
 when(referencedTypeMock.getErasedSignature(sb)).thenReturn(sb);
 testedInstance.setReference(referencedTypeMock);
 testedInstance.getErasedSignature(sb);
 assertEquals(sb.toString(), testedInstance.getSelfReferencedType().getErasedSignature(new StringBuilder()).toString());
 }

 @Test
 public void test_withContentType() {
 // test withContentType with null referencedType
 testedInstance.withContentType(mock(JavaType.class));
 // test withContentType with set reference
 JavaType contentType = mock(JavaType.class);
 testedInstance.setReference(referencedTypeMock);
 testedInstance.withContentType(contentType);
 assertEquals(testedInstance, testedInstance.withContentType(contentType));
 }

 @Test
 public void test_withTypeHandler() {
 // test withTypeHandler with null referencedType
 testedInstance.withTypeHandler(mock(Object.class));
 // test withTypeHandler with set reference
 Object typeHandler = mock(Object.class);
 testedInstance.setReference(referencedTypeMock);
 testedInstance.withTypeHandler(typeHandler);
 assertEquals(testedInstance, testedInstance.withTypeHandler(typeHandler));
 }

 @Test
 public void test_withContentTypeHandler() {
 // test withContentTypeHandler with null referencedType
 testedInstance.withContentTypeHandler(mock(Object.class));
 // test withContentTypeHandler with set reference
 Object contentTypeHandler = mock(Object.class);
 testedInstance.setReference(referencedTypeMock);
 testedInstance.withContentTypeHandler(contentTypeHandler);
 assertEquals(testedInstance, testedInstance.withContentTypeHandler(contentTypeHandler));
 }

 @Test
 public void test_withValueHandler() {
 // test withValueHandler with null referencedType
 testedInstance.withValueHandler(mock(Object.class));
 // test withValueHandler with set reference
 Object valueHandler = mock(Object.class);
 testedInstance.setReference(referencedTypeMock);
 testedInstance.withValueHandler(valueHandler);
 assertEquals(testedInstance, testedInstance.withValueHandler(valueHandler));
 }

 @Test
 public void test_withContentValueHandler() {
 // test withContentValueHandler with null referencedType
 testedInstance.withContentValueHandler(mock(Object.class));
 // test withContentValueHandler with set reference
 Object contentValueHandler = mock(Object.class);
 testedInstance.setReference(referencedTypeMock);
 testedInstance.withContentValueHandler(contentValueHandler);
 assertEquals(testedInstance, testedInstance.withContentValueHandler(contentValueHandler));
 }

 @Test
 public void test_withStaticTyping() {
 // test withStaticTyping with null referencedType
 testedInstance.withStaticTyping();
 // test withStaticTyping with set reference
 testedInstance.setReference(referencedTypeMock);
 testedInstance.withStaticTyping();
 assertEquals(testedInstance, testedInstance.withStaticTyping());
 }

 @Test
 public void test_toString() {
 // test toString with null referencedType
 assertEquals("[recursive type; UNRESOLVED]", testedInstance.toString());
 // test toString with set reference
 StringBuilder sb = new StringBuilder("[recursive type; ");
 sb.append(referencedTypeMock.getRawClass().getName());
 sb.append(']');
 when(referencedTypeMock.toString()).thenReturn(sb.toString());
 testedInstance.setReference(referencedTypeMock);
 assertEquals(sb.toString(), testedInstance.toString());
 }

 @Test
 public void test_equals() {
 ResolvedRecursiveType otherInstance = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
 private static final long serialVersionUID = 1L;

 @Override
 public JavaType getSelfReferencedType() {
 return referencedTypeMock;
 }
 };
 otherInstance.setReference(referencedTypeMock);
 ResolvedRecursiveType otherInstanceDifferent = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
 private static final long serialVersionUID = 1L;

 @Override
 public JavaType getSelfReferencedType() {
 return mock(JavaType.class);
 }
 };
 // test equals with same instance
 assertEquals(testedInstance, testedInstance);
 // test equals with different instance
 assertFalse(testedInstance.equals(otherInstanceDifferent));
 // test equals with same class and same reference
 ResolvedRecursiveType sameReferenceInstance = new ResolvedRecursiveType(Integer.class, new TypeBindings()) {
 private static final long serialVersionUID = 1L;

 @Override
 public JavaType getSelfReferencedType() {
 return referencedTypeMock;
 }
 };
 sameReferenceInstance.setReference(referencedTypeMock);
 assertTrue(testedInstance.equals(sameReferenceInstance));
 // test equals with same class and different reference
 ResolvedRecursiveType differentReferenceInstance = new ResolvedRecursiveType(Integer.class, new TypeBindings()) {
 private static final long serialVersionUID = 1L;

 @Override
 public JavaType getSelfReferencedType() {
 return mock(JavaType.class);
 }
 };
 assertFalse(testedInstance.equals(differentReferenceInstance));
 // test equals with different classes
 ResolvedRecursiveType differentClassInstance = new ResolvedRecursiveType(String.class, new TypeBindings()) {
 private static final long serialVersionUID = 1L;

 @Override
 public JavaType getSelfReferencedType() {
 return referencedTypeMock;
 }
 };
 assertFalse(testedInstance.equals(differentClassInstance));
 // test equals with null
 assertFalse(testedInstance.equals(null));
 }
}