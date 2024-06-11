import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

 // Test constructor with valid arguments
 @Test
 public void testReferenceTypeConstructor() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 assertNotNull(referenceType);
 assertEquals(String.class, referenceType.getRawClass());
 assertEquals(JavaType.class, referenceType.getContentType());
 }

 // Test getReferencedType method
 @Test
 public void testGetReferencedType() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 assertEquals(javaType, referenceType.getReferencedType());
 }

 // Test isReferenceType method
 @Test
 public void testIsReferenceType() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 assertTrue(referenceType.isReferenceType());
 }

 // Test withTypeHandler method with valid argument
 @Test
 public void testWithTypeHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 Object handler = new Object() {};
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
 assertNotSame(referenceType, newReferenceType);
 assertEquals(handler, newReferenceType.getTypeHandler());
 }

 // Test withContentTypeHandler method with valid argument
 @Test
 public void testWithContentTypeHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 JavaType handler = new TypeReference<List<String>>(){}.getType();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
 assertNotSame(referenceType, newReferenceType);
 assertEquals(handler, newReferenceType.getContentType());
 }

 // Test withValueHandler method with valid argument
 @Test
 public void testWithValueHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 Object handler = new Object() {};
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType newReferenceType = referenceType.withValueHandler(handler);
 assertNotSame(referenceType, newReferenceType);
 assertEquals(handler, newReferenceType.getValueHandler());
 }

 // Test withContentValueHandler method with valid argument
 @Test
 public void testWithContentValueHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 Object handler = new Object() {};
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
 assertNotSame(referenceType, newReferenceType);
 assertEquals(handler, newReferenceType.getContentValueHandler());
 }

 // Test withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType newReferenceType = referenceType.withStaticTyping();
 assertNotSame(referenceType, newReferenceType);
 assertTrue(newReferenceType.isStatic());
 }

 // Test buildCanonicalName method
 @Test
 public void testBuildCanonicalName() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 String canonicalName = referenceType.buildCanonicalName();
 assertEquals("[reference type, class java.util.List<java.lang.Integer>]", canonicalName);
 }

 // Test narrow method with valid argument
 @Test
 public void testNarrow() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 JavaType narrowedJavaType = new TypeReference<List<String>>(){}.getType();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 JavaType narrowedType = referenceType.narrow(String.class);
 assertNotSame(javaType, narrowedType);
 assertEquals(narrowedJavaType, narrowedType);
 }

 // Test getErasedSignature method
 @Test
 public void testGetErasedSignature() {
 Class<String> stringClass = String.class;
 ReferenceType referenceType = new ReferenceType(stringClass, null, null, null, false);
 StringBuilder expectedSignature = new StringBuilder("[Ljava.lang.String;");
 StringBuilder actualSignature = referenceType.getErasedSignature(new StringBuilder());
 assertEquals(expectedSignature.toString(), actualSignature.toString());
 }

 // Test getGenericSignature method
 @Test
 public void testGetGenericSignature() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 StringBuilder expectedSignature = new StringBuilder("[Ljava.lang.String;<T extends java.lang.Object>");
 StringBuilder actualSignature = new StringBuilder();
 referenceType.getGenericSignature(actualSignature);
 assertEquals(expectedSignature.toString(), actualSignature.toString());
 }

 // Test toString method
 @Test
 public void testToString() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 String expectedToString = "[reference type, class java.lang.String, class java.util.List<java.lang.Integer>]";
 String actualToString = referenceType.toString();
 assertEquals(expectedToString, actualToString);
 }

 // Test equals method with same object
 @Test
 public void testEqualsSameObject() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 ReferenceType referenceType1 = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType referenceType2 = referenceType1;
 assertTrue(referenceType1.equals(referenceType2));
 }

 // Test equals method with different classes
 @Test
 public void testEqualsDifferentClasses() {
 Class<String> stringClass = String.class;
 JavaType javaType1 = new TypeReference<List<Integer>>(){}.getType();
 JavaType javaType2 = new TypeReference<List<Long>>(){}.getType();
 ReferenceType referenceType1 = new ReferenceType(stringClass, javaType1, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(stringClass, javaType2, null, null, false);
 assertFalse(referenceType1.equals(referenceType2));
 }

 // Test equals method with different referenced types
 @Test
 public void testEqualsDifferentReferencedTypes() {
 Class<String> stringClass = String.class;
 JavaType javaType1 = new TypeReference<List<Integer>>(){}.getType();
 JavaType javaType2 = new TypeReference<ArrayList<Integer>>(){}.getType();
 ReferenceType referenceType1 = new ReferenceType(stringClass, javaType1, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(stringClass, javaType2, null, null, false);
 assertFalse(referenceType1.equals(referenceType2));
 }

 // Test equals method with different raw types
 @Test
 public void testEqualsDifferentRawTypes() {
 Class<String> stringClass1 = String.class;
 Class<Integer> intClass1 = Integer.class;
 Class<String> stringClass2 = String.class;
 Class<Long> longClass1 = Long.class;
 ReferenceType referenceType1 = new ReferenceType(stringClass1, null, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(intClass1, null, null, null, false);
 ReferenceType referenceType3 = new ReferenceType(stringClass2, null, null, null, false);
 ReferenceType referenceType4 = new ReferenceType(longClass1, null, null, null, false);
 assertFalse(referenceType1.equals(referenceType2));
 assertFalse(referenceType1.equals(referenceType3));
 assertFalse(referenceType1.equals(referenceType4));
 }

 // Test equals method with null
 @Test
 public void testEqualsNull() {
 Class<String> stringClass = String.class;
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 assertFalse(referenceType.equals(null));
 }
}