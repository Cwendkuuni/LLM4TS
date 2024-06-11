package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceTypeTest {

 private JavaType referencedType;
 private Class<?> clazz;

 @Before
 public void setUp() {
 clazz = Mockito.mock(Class.class);
 referencedType = Mockito.mock(JavaType.class);
 }

 @Test
 public void testConstruct() {
 ReferenceType referenceType = ReferenceType.construct(clazz, referencedType, null, null);

 assertEquals(clazz, referenceType.getClass());
 assertEquals(referencedType, referenceType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 JavaType newTypeHandler = Mockito.mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);

 ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

 assertEquals(clazz, newReferenceType.getClass());
 assertEquals(referencedType, newReferenceType.getContentType());
 assertEquals(newTypeHandler, newReferenceType.getContentTypeHandler());
 }

 @Test
 public void testIsReferenceType() {
 ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testGetReferencedType() {
 ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);
 assertEquals(referencedType, referenceType.getReferencedType());
 }

 @Test
 public void testContainedTypeCount() {
 ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);
 assertEquals(1, referenceType.containedTypeCount());
 }

 @Test
 public void testContainedType() {
 ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);
 assertEquals(referencedType, referenceType.containedType(0));
 }

 @Test
 public void testContainedTypeName() {
 ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);
 assertEquals("T", referenceType.containedTypeName(0));
 }

 @Test
 public void testGetParameterSource() {
 ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);
 assertEquals(clazz, referenceType.getParameterSource());
 }

 @Test
 public void testGetErasedSignature() {
 ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);
 StringBuilder sb = new StringBuilder();
 referenceType.getErasedSignature(sb);
 verify(clazz).getCanonicalName();
 assertEquals("[Ljava/lang/Object;", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);
 StringBuilder sb = new StringBuilder();
 referenceType.getGenericSignature(sb);
 verify(clazz).getCanonicalName();
 verify(referencedType).getGenericSignature(sb);
 assertEquals("L" + clazz.getCanonicalName() + "<T>;", sb.toString());
 }

 @Test
 public void testToString() {
 ReferenceType referenceType = new ReferenceType(clazz, referencedType, null, null, false);
 assertEquals("[reference type, class " + clazz.getCanonicalName() + "<T> <T>]", referenceType.toString());
 }

 @Test
 public void testEquals() {
 ReferenceType referenceType1 = new ReferenceType(clazz, referencedType, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(clazz, referencedType, null, null, false);
 ReferenceType referenceType3 = new ReferenceType(clazz, referencedType, null, null, true);

 assertEquals(referenceType1, referenceType1);
 assertTrue(referenceType1.equals(referenceType1));

 assertEquals(referenceType1, referenceType2);
 assertTrue(referenceType1.equals(referenceType2));

 assertFalse(referenceType1.equals(null));
 assertFalse(referenceType1.equals(""));

 assertFalse(referenceType1.equals(new Object()));

 assertEquals(referenceType1, referenceType1);
 assertFalse(referenceType1.equals(referenceType3));
 }
}