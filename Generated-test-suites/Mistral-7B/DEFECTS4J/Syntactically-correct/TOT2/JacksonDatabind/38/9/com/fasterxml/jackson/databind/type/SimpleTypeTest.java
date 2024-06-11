import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 // Test case for the constructor that takes a Class<?> argument
 @Test
 public void testSimpleTypeConstructor() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 assertTrue(simpleType instanceof SimpleType);
 assertEquals(cls.getName(), simpleType.toString());
 }

 // Test case for the copy constructor
 @Test
 public void testCopyConstructor() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 SimpleType copiedSimpleType = new SimpleType(simpleType);
 assertTrue(copiedSimpleType instanceof SimpleType);
 assertEquals(simpleType.toString(), copiedSimpleType.toString());
 }

 // Test case for the constructor that takes Class<?> cls, TypeBindings bindings, JavaType superClass, JavaType[] superInts
 @Test
 public void testConstructWithArguments() {
 Class<?> cls = String.class;
 JavaType superClass = Integer.class;
 JavaType[] superInts = new JavaType[] {Integer.TYPE};
 SimpleType simpleType = new SimpleType(cls, new TypeBindings(), superClass, superInts);
 assertTrue(simpleType instanceof SimpleType);
 assertEquals(cls.getName(), simpleType.toString());
 assertNull(simpleType.getSuperClass());
 assertArrayEquals(new JavaType[0], simpleType.getSuperTypes());
 }

 // Test case for the static method constructUnsafe
 @Test
 public void testConstructUnsafe() {
 Class<?> cls = String.class;
 SimpleType simpleType = SimpleType.constructUnsafe(cls);
 assertTrue(simpleType instanceof SimpleType);
 assertEquals(cls.getName(), simpleType.toString());
 }

 // Test case for the deprecated constructor method construct
 @Test
 public void testDeprecatedConstruct() {
 Class<?> cls = String.class;
 SimpleType simpleType = SimpleType.construct(cls);
 assertTrue(simpleType instanceof SimpleType);
 assertEquals(cls.getName(), simpleType.toString());
 }

 // Test case for the _narrow method
 @Test
 public void testNarrow() {
 Class<?> subclass = String[].class;
 SimpleType superType = new SimpleType(String.class);
 SimpleType narrowType = superType._narrow(subclass);
 assertNotNull(narrowType);
 assertEquals(subclass.getName(), narrowType.toString());
 }

 // Test case for the isContainerType method
 @Test
 public void testIsContainerType() {
 SimpleType simpleType = new SimpleType(String.class);
 assertFalse(simpleType.isContainerType());
 }

 // Test case for the getErasedSignature method
 @Test
 public void testGetErasedSignature() {
 SimpleType simpleType = new SimpleType(String.class);
 StringBuilder sb = new StringBuilder();
 simpleType.getErasedSignature(sb);
 assertEquals("java.lang.String", sb.toString());
 }

 // Test case for the getGenericSignature method
 @Test
 public void testGetGenericSignature() {
 SimpleType simpleType = new SimpleType(String[].class);
 StringBuilder sb = new StringBuilder();
 simpleType.getGenericSignature(sb);
 assertEquals("[Ljava.lang.String;", sb.toString());
 }

 // Test case for the toString method
 @Test
 public void testToString() {
 SimpleType simpleType = new SimpleType(String.class);
 assertEquals("[simple type, class java.lang.String]", simpleType.toString());
 }

 // Test case for equals method
 @Test
 public void testEquals() {
 Class<?> cls = String.class;
 SimpleType simpleType1 = new SimpleType(cls);
 SimpleType simpleType2 = new SimpleType(cls);
 assertTrue(simpleType1.equals(simpleType2));
 }

 // Test case for null argument
 @Test(expected = IllegalArgumentException.class)
 public void testEqualsWithNull() {
 SimpleType simpleType = new SimpleType(String.class);
 assertFalse(simpleType.equals(null));
 }

 // Test case for non-equal classes
 @Test
 public void testEqualsWithDifferentClasses() {
 Class<?> cls1 = String.class;
 Class<?> cls2 = Integer.class;
 SimpleType simpleType1 = new SimpleType(cls1);
 SimpleType simpleType2 = new SimpleType(cls2);
 assertFalse(simpleType1.equals(simpleType2));
 }

 // Test case for edge case with super classes
 @Test
 public void testEqualsWithSuperClasses() {
 Class<?> superClass = Object.class;
 Class<?> subClass1 = String.class;
 Class<?> subClass2 = Object.class;
 SimpleType simpleType1 = new SimpleType(subClass1, new TypeBindings(), superClass, null);
 SimpleType simpleType2 = new SimpleType(subClass2, new TypeBindings(), superClass, null);
 assertTrue(simpleType1.equals(simpleType2));
 }

 // Test case for exception handling scenarios
 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithArrayClass() {
 Class<?> cls = String[].class;
 SimpleType simpleType = new SimpleType(cls);
 fail("This constructor should not be called with array classes.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithMapClass() {
 Class<?> cls = HashMap.class;
 SimpleType simpleType = new SimpleType(cls);
 fail("This constructor should not be called with Map classes.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithCollectionClass() {
 Class<?> cls = ArrayList.class;
 SimpleType simpleType = new SimpleType(cls);
 fail("This constructor should not be called with Collection classes.");
 }
}