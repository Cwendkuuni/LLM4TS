import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 // Test case for the constructor that takes a Class object
 @Test
 public void testSimpleTypeConstructor() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 Assert.assertEquals(String.class, simpleType.getRawClass());
 }

 // Test case for the constructor that takes a Class object, TypeBindings, JavaType superClass, and JavaType[] superInts
 @Test
 public void testSimpleTypeConstructor2() {
 Class<?> cls = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = JavaType.construct(List.class, Object.class);
 JavaType[] superInts = new JavaType[0];
 SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts);
 Assert.assertEquals(String.class, simpleType.getRawClass());
 Assert.assertNull(simpleType.getSuperClass());
 Assert.assertArrayEquals(new JavaType[0], simpleType.getSuperTypes());
 }

 // Test case for the copy constructor
 @Test
 public void testCopyConstructor() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(simpleType1);
 Assert.assertEquals(simpleType1.getRawClass(), simpleType2.getRawClass());
 }

 // Test case for the constructor that takes Class<?> cls, TypeBindings bindings, JavaType superClass, JavaType[] superInts, int extraHash, Object valueHandler, Object typeHandler, and boolean asStatic
 @Test
 public void testSimpleTypeConstructor3() {
 Class<?> cls = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = JavaType.construct(List.class, Object.class);
 JavaType[] superInts = new JavaType[0];
 Object valueHandler = new Object(){};
 Object typeHandler = new Object(){};
 SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts, 0, valueHandler, typeHandler, false);
 Assert.assertEquals(String.class, simpleType.getRawClass());
 Assert.assertEquals(valueHandler, simpleType.getValueHandler());
 Assert.assertEquals(typeHandler, simpleType.getTypeHandler());
 Assert.assertFalse(simpleType.isStatic());
 }

 // Test case for the static method constructUnsafe
 @Test
 public void testConstructUnsafe() {
 Class<?> cls = String.class;
 SimpleType simpleType = SimpleType.constructUnsafe(cls);
 Assert.assertEquals(String.class, simpleType.getRawClass());
 }

 // Test case for the deprecated constructor method construct
 @Test
 public void testDeprecatedConstruct() {
 Class<?> cls = String.class;
 SimpleType simpleType = SimpleType.construct(cls);
 Assert.assertEquals(String.class, simpleType.getRawClass());
 }

 // Test case for the method _narrow
 @Test(expected = IllegalArgumentException.class)
 public void testNarrowException() {
 SimpleType simpleType = new SimpleType(String.class);
 Class<?> subclass = Integer.class;
 simpleType._narrow(subclass);
 }

 // Test case for the method isContainerType
 @Test
 public void testIsContainerType() {
 SimpleType simpleType = new SimpleType(String.class);
 Assert.assertFalse(simpleType.isContainerType());
 }

 // Test case for the method getErasedSignature
 @Test
 public void testGetErasedSignature() {
 SimpleType simpleType = new SimpleType(String.class);
 StringBuilder sb = new StringBuilder();
 simpleType.getErasedSignature(sb);
 String result = sb.toString();
 Assert.assertEquals("Ljava/lang/String;", result);
 }

 // Test case for the method getGenericSignature
 @Test
 public void testGetGenericSignature() {
 SimpleType simpleType = new SimpleType(String[].class);
 StringBuilder sb = new StringBuilder();
 simpleType.getGenericSignature(sb);
 String result = sb.toString();
 Assert.assertEquals("Ljava/lang/String[];", result);
 }

 // Test case for the method toString
 @Test
 public void testToString() {
 SimpleType simpleType = new SimpleType(String.class);
 String result = simpleType.toString();
 Assert.assertEquals("[simple type, class [Ljava.lang.String;]", result);
 }

 // Test case for the method equals
 @Test
 public void testEquals() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(String.class);
 Assert.assertTrue(simpleType1.equals(simpleType2));
 }

 // Test case for null equality check
 @Test
 public void testEqualsNull() {
 SimpleType simpleType = new SimpleType(String.class);
 Assert.assertFalse(simpleType.equals(null));
 }

 // Test case for different class equality check
 @Test
 public void testEqualsDifferentClass() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(Integer.class);
 Assert.assertFalse(simpleType1.equals(simpleType2));
 }

 // Test case for different bindings equality check
 @Test
 public void testEqualsDifferentBindings() {
 SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings());
 SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.bindings(Boolean.TYPE, IntTypeIds.INT_TYPE_ID));
 Assert.assertFalse(simpleType1.equals(simpleType2));
 }
}