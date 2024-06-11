import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

 // Test case for the constructor with a Class argument
 @Test
 public void testSimpleTypeConstructorWithClass() {
 Class<String> stringClass = String.class;
 SimpleType simpleType = new SimpleType(stringClass);
 assertNotNull(simpleType);
 assertEquals("java.lang.String", simpleType.getClassName());
 }

 // Test case for the constructor with Class, TypeBindings, JavaType superClass, and JavaType[] superInts arguments
 @Test
 public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInts() {
 Class<List<Integer>> listIntegerClass = List.class.getTypeParameters()[0].getType();
 SimpleType simpleType = new SimpleType(listIntegerClass, TypeBindings.emptyBindings(), null, null);
 assertNotNull(simpleType);
 assertEquals("java.util.List", simpleType.getClassName());
 }

 // Test case for the copy constructor
 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType originalSimpleType = new SimpleType(String.class);
 SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
 assertEquals(originalSimpleType.getClassName(), copiedSimpleType.getClassName());
 }

 // Test case for the constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInts, Object valueHandler, Object typeHandler, and boolean asStatic arguments
 @Test
 public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperIntsValueHandlerTypeHandlerAndAsStatic() {
 Class<Double> doubleClass = Double.class;
 Object handler = new Object() {};
 SimpleType simpleType = new SimpleType(doubleClass, TypeBindings.emptyBindings(), null, null, handler, handler, true);
 assertNotNull(simpleType);
 assertEquals("java.lang.Double", simpleType.getClassName());
 assertEquals(handler, simpleType.getValueHandler());
 assertEquals(handler, simpleType.getTypeHandler());
 assertTrue(simpleType.isStatic());
 }

 // Test case for the constructor used by ReferenceType
 @Test
 public void testSimpleTypeConstructorUsedByReferenceType() {
 Class<List<Integer>> listIntegerClass = List.class.getTypeParameters()[0].getType();
 SimpleType simpleType = new SimpleType(listIntegerClass, TypeBindings.emptyBindings(), null, new JavaType[] { JavaType.construct(int[].class) }, 0, null, false);
 assertNotNull(simpleType);
 assertEquals("java.util.List", simpleType.getClassName());
 }

 // Test case for the constructUnsafe method
 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructUnsafeWithIllegalArgument() {
 Class<Map<String, String>> mapClass = Map.class.getTypeParameters()[0].getType();
 SimpleType simpleType = SimpleType.constructUnsafe(mapClass);
 }

 // Test case for the construct method (deprecated)
 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructWithIllegalArgument() {
 Class<Map<String, String>> mapClass = Map.class.getTypeParameters()[0].getType();
 SimpleType simpleType = SimpleType.construct(mapClass);
 }

 // Test case for the _narrow method
 @Test
 public void testSimpleTypeNarrow() {
 Class<Integer> integerClass = Integer.class;
 SimpleType intSimpleType = new SimpleType(integerClass);
 SimpleType intSimpleTypeNarrowed = intSimpleType.narrow(Integer.class);
 assertEquals(intSimpleType, intSimpleTypeNarrowed);
 }

 // Test case for the isContainerType method
 @Test
 public void testSimpleTypeIsContainerType() {
 SimpleType simpleType = new SimpleType(String.class);
 assertFalse(simpleType.isContainerType());
 }

 // Test case for the getErasedSignature method
 @Test
 public void testSimpleTypeGetErasedSignature() {
 SimpleType simpleType = new SimpleType(String.class);
 StringBuilder expected = new StringBuilder("java.lang.String");
 assertEquals(expected.toString(), simpleType.getErasedSignature(new StringBuilder()).toString());
 }

 // Test case for the getGenericSignature method
 @Test
 public void testSimpleTypeGetGenericSignature() {
 SimpleType simpleType = new SimpleType(List.class);
 StringBuilder expected = new StringBuilder("java.util.List<T>");
 assertEquals(expected.toString(), simpleType.getGenericSignature(new StringBuilder()).toString());
 }

 // Test case for the equals method
 @Test
 public void testSimpleTypeEquals() {
 Class<Integer> integerClass = Integer.class;
 SimpleType intSimpleType1 = new SimpleType(integerClass);
 SimpleType intSimpleType2 = new SimpleType(integerClass);
 SimpleType intSimpleType3 = new SimpleType(String.class);
 assertTrue(intSimpleType1.equals(intSimpleType1));
 assertTrue(intSimpleType1.equals(intSimpleType2));
 assertFalse(intSimpleType1.equals(intSimpleType3));
 }
}