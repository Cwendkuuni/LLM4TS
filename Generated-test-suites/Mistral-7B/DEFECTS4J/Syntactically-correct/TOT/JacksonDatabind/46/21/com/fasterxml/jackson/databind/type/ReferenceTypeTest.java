import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

 // Test case for construct method
 @Test
 public void testConstruct() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 Object valueHandler = null;
 Object typeHandler = null;
 ReferenceType expected = new ReferenceType(cls, refType, valueHandler, typeHandler);
 ReferenceType actual = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
 assertEquals(expected, actual);
 }

 // Test case for withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 Object valueHandler = null;
 Object typeHandler1 = new Object() {};
 Object typeHandler2 = new Object() {};
 ReferenceType expected = new ReferenceType(cls, refType, valueHandler, typeHandler1);
 ReferenceType actual = expected.withTypeHandler(typeHandler2);
 assertNotSame(expected, actual);
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(expected._class, actual._class);
 assertEquals(expected._referencedType, actual._referencedType);
 assertEquals(typeHandler2, actual._typeHandler);
 }

 // Test case for getReferencedType method
 @Test
 public void testGetReferencedType() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 JavaType actual = referenceType.getReferencedType();
 JavaType expected = refType;
 assertEquals(expected, actual);
 }

 // Test case for isReferenceType method
 @Test
 public void testIsReferenceType() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 boolean actual = referenceType.isReferenceType();
 boolean expected = true;
 assertTrue(actual);
 }

 // Test case for containedTypeCount method
 @Test
 public void testContainedTypeCount() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 int actual = referenceType.containedTypeCount();
 int expected = 1;
 assertEquals(expected, actual);
 }

 // Test case for containedType method with valid index
 @Test
 public void testContainedType() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 JavaType actual = referenceType.containedType(0);
 JavaType expected = refType;
 assertEquals(expected, actual);
 }

 // Test case for containedType method with invalid index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testContainedTypeInvalidIndex() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 JavaType actual = referenceType.containedType(1);
 }

 // Test case for containedTypeName method with valid index
 @Test
 public void testContainedTypeName() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 String actual = referenceType.containedTypeName(0);
 String expected = "T";
 assertEquals(expected, actual);
 }

 // Test case for containedTypeName method with invalid index
 @Test
 public void testContainedTypeNameInvalidIndex() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 String actual = referenceType.containedTypeName(1);
 assertNull(actual);
 }

 // Test case for getParameterSource method
 @Test
 public void testGetParameterSource() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 Class<?> actual = referenceType.getParameterSource();
 Class<?> expected = cls;
 assertEquals(expected, actual);
 }

 // Test case for getErasedSignature method
 @Test
 public void testGetErasedSignature() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 StringBuilder sb = new StringBuilder();
 StringBuilder expected = _classSignature(cls, sb, true);
 StringBuilder actual = referenceType.getErasedSignature(sb);
 assertEquals(expected.toString(), actual.toString());
 }

 // Test case for getGenericSignature method
 @Test
 public void testGetGenericSignature() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 StringBuilder sb = new StringBuilder();
 StringBuilder expected = _classSignature(cls, sb, false);
 expected.append('<');
 expected.append(refType.toCanonical());
 expected.append(';');
 StringBuilder actual = referenceType.getGenericSignature(sb);
 assertEquals(expected.toString(), actual.toString());
 }

 // Test case for toString method
 @Test
 public void testToString() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 String actual = referenceType.toString();
 String expected = "[reference type, class " + cls.getName() +
 "<T>" +
 "{" +
 "  referenced type: " + refType +
 "}]";
 assertEquals(expected, actual);
 }

 // Test case for equals method with same instance
 @Test
 public void testEqualsSameInstance() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 boolean actual = referenceType.equals(referenceType);
 boolean expected = true;
 assertTrue(actual);
 }

 // Test case for equals method with null object
 @Test
 public void testEqualsNullObject() {
 Class<String> cls = String.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 boolean actual = referenceType.equals(null);
 boolean expected = false;
 assertFalse(actual);
 }

 // Test case for equals method with different class
 @Test
 public void testEqualsDifferentClass() {
 Class<String> cls1 = String.class;
 JavaType refType1 = new TypeReference<List<Integer>>() {}.getType();
 Class<Integer> cls2 = Integer.class;
 JavaType refType2 = new TypeReference<List<String>>() {}.getType();
 ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);
 boolean actual = referenceType1.equals(referenceType2);
 boolean expected = false;
 assertFalse(actual);
 }

 // Test case for equals method with different referenced types
 @Test
 public void testEqualsDifferentReferencedTypes() {
 Class<String> cls = String.class;
 JavaType refType1 = new TypeReference<List<Integer>>() {}.getType();
 JavaType refType2 = new TypeReference<List<String>>() {}.getType();
 ReferenceType referenceType1 = new ReferenceType(cls, refType1, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(cls, refType2, null, null, false);
 boolean actual = referenceType1.equals(referenceType2);
 boolean expected = false;
 assertFalse(actual);
 }

 // Test case for equals method with different classes but same referenced type
 @Test
 public void testEqualsDifferentClassesSameReferencedType() {
 Class<String> cls1 = String.class;
 Class<Integer> cls2 = Integer.class;
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ReferenceType referenceType1 = new ReferenceType(cls1, refType, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(cls2, refType, null, null, false);
 boolean actual = referenceType1.equals(referenceType2);
 boolean expected = true;
 assertTrue(actual);
 }
}