import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<?> simpleClass = String.class;
 SimpleType simpleType = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null);
 assertEquals(simpleClass.getName(), simpleType.getRawClass().getName());
 assertEquals(TypeFactory.defaultInstance(), simpleType.getBindingOverride());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeConstructionWithBindings() {
 Class<?> simpleClass = String.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class, simpleClass);
 SimpleType simpleType = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null, null, null, false);
 simpleType.setValueHandler(new StdScalarValueHandler());
 simpleType.setTypeHandler(new StdTypeSerializer());
 simpleType.setBindingOverride(TypeBindings.mapType(simpleType, javaType));
 assertEquals(simpleClass.getName(), simpleType.getRawClass().getName());
 assertEquals(javaType, simpleType.getValueType());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType originalSimpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
 SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
 assertEquals(originalSimpleType, copiedSimpleType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructUnsafeWithArrayType() {
 Class<?> arrayType = int[].class;
 try {
 SimpleType simpleType = SimpleType.constructUnsafe(arrayType);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithMapType() {
 Class<?> mapType = HashMap.class;
 try {
 SimpleType simpleType = SimpleType.construct(mapType);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNarrow() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
 SimpleType narrowedType = simpleType.narrow(StringBuilder.class);
 assertNotSame(simpleType, narrowedType);
 assertEquals(StringBuilder.class, narrowedType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowWithInvalidSubclass() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
 try {
 simpleType.narrow(Integer.class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testWithContentType() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
 JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.class);
 SimpleType simpleTypeWithContentType = simpleType.withContentType(contentType);
 assertEquals(simpleType, simpleTypeWithContentType.withoutContentType());
 assertEquals(contentType, simpleTypeWithContentType.getValueType());
 }

 @Test
 public void testWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
 StdTypeSerializer typeHandler = new StdTypeSerializer();
 SimpleType simpleTypeWithTypeHandler = simpleType.withTypeHandler(typeHandler);
 assertNotSame(simpleType, simpleTypeWithTypeHandler);
 assertEquals(typeHandler, simpleTypeWithTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
 StdScalarValueHandler valueHandler = new StdScalarValueHandler();
 SimpleType simpleTypeWithValueHandler = simpleType.withValueHandler(valueHandler);
 assertNotSame(simpleType, simpleTypeWithValueHandler);
 assertEquals(valueHandler, simpleTypeWithValueHandler.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
 SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
 assertNotSame(simpleType, simpleTypeWithStaticTyping);
 assertTrue(simpleTypeWithStaticTyping.isStatic());
 }

 @Test
 public void testBuildCanonicalName() {
 Class<?> simpleClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class, simpleClass);
 SimpleType simpleType = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null, null, null, false);
 simpleType.setBindingOverride(TypeBindings.mapType(simpleType, javaType));
 String canonicalName = simpleType.buildCanonicalName();
 String expectedCanonicalName = "[java.util.List, class java.lang.Integer]";
 assertEquals(expectedCanonicalName, canonicalName);
 }

 @Test
 public void testIsContainerType() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
 assertFalse(simpleType.isContainerType());
 }
}