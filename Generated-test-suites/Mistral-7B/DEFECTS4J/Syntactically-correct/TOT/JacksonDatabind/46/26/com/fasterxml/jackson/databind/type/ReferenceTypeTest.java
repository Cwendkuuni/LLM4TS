package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 @Test
 public void testConstruct() {
 ReferenceType referenceType = ReferenceType.construct(String.class,
 ClassType.constructRawType(int[].class), null, null);
 JavaType referencedType = referenceType.getReferencedType();
 assertEquals(int[].class, referencedType.getRawClass());
 }

 @Test
 public void testWithTypeHandler() {
 ClassType stringType = ClassType.constructRawType(String.class);
 ReferenceType referenceType = new ReferenceType(String.class, stringType,
 null, null, false);
 JavaType typeHandler = TypeFactory.defaultInstance().getTypeHandler(String.class);

 ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
 JavaType newReferencedType = newReferenceType.getReferencedType();
 assertEquals(stringType, newReferencedType);
 }

 @Test
 public void testWithContentTypeHandler() {
 ClassType intType = ClassType.constructRawType(int.class);
 ClassType stringType = ClassType.constructRawType(String.class);
 ReferenceType referenceType = new ReferenceType(String.class, intType,
 null, null, false);
 JavaType typeHandler = TypeFactory.defaultInstance().getTypeHandler(String.class);
 JavaType contentTypeHandler = TypeFactory.defaultInstance().getTypeHandler(int.class);

 ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
 JavaType newReferencedType = newReferenceType.getReferencedType();
 JavaType newContentTypeHandler = newReferenceType.getContentTypeHandler();

 assertEquals(intType, newReferencedType);
 assertEquals(contentTypeHandler, newContentTypeHandler);
 }

 @Test
 public void testWithValueHandler() {
 ClassType stringType = ClassType.constructRawType(String.class);
 ReferenceType referenceType = new ReferenceType(String.class, stringType,
 null, null, false);
 StringValueHandler stringValueHandler = new StringValueHandler();

 ReferenceType newReferenceType = referenceType.withValueHandler(stringValueHandler);
 JavaType newReferencedType = newReferenceType.getReferencedType();
 JavaType newValueHandler = newReferenceType.getValueHandler();

 assertEquals(stringType, newReferencedType);
 assertEquals(stringValueHandler, newValueHandler);
 }

 @Test
 public void testWithContentValueHandler() {
 ClassType intType = ClassType.constructRawType(int.class);
 ClassType stringType = ClassType.constructRawType(String.class);
 ReferenceType referenceType = new ReferenceType(String.class, intType,
 null, null, false);
 IntValueHandler intValueHandler = new IntValueHandler();
 StringValueHandler stringValueHandler = new StringValueHandler();

 ReferenceType newReferenceType = referenceType.withContentValueHandler(intValueHandler);
 JavaType newReferencedType = newReferenceType.getReferencedType();
 JavaType newContentValueHandler = newReferenceType.getContentValueHandler();

 assertEquals(intType, newReferencedType);
 assertEquals(intValueHandler, newContentValueHandler);
 }

 @Test
 public void testWithStaticTyping() {
 ClassType stringType = ClassType.constructRawType(String.class);
 ReferenceType referenceType = new ReferenceType(String.class, stringType,
 null, null, false);

 ReferenceType newReferenceType = referenceType.withStaticTyping();
 assertEquals(true, newReferenceType.isStaticTyping());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowWithInvalidSubclass() {
 ClassType stringType = ClassType.constructRawType(String.class);
 ClassType intType = ClassType.constructRawType(int.class);

 ReferenceType referenceType = new ReferenceType(String.class, stringType,
 null, null, false);
 JavaType narrowedType = referenceType.narrow(intType.getRawClass());
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetReferencedType() {
 ClassType stringType = ClassType.constructRawType(String.class);
 ReferenceType referenceType = new ReferenceType(String.class, stringType,
 null, null, false);
 JavaType referencedType = referenceType.getReferencedType();
 assertEquals(stringType, referencedType);
 }

 @Test
 public void testIsReferenceType() {
 ClassType stringType = ClassType.constructRawType(String.class);
 SimpleType simpleType = new SimpleType(stringType.getRawClass(), null);

 ReferenceType referenceType = new ReferenceType(String.class, stringType,
 null, null, false);
 SimpleType newSimpleType = referenceType.withStaticTyping();

 assertTrue(referenceType.isReferenceType());
 assertFalse(newSimpleType.isReferenceType());
 }
}