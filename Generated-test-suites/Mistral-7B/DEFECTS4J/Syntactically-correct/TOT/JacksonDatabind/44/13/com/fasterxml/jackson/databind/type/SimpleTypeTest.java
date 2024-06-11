import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstructor() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 JavaType expectedType = TypeFactory.defaultInstance().constructType(cls);
 assertEquals(expectedType, simpleType);
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 Class<?> cls = String[].class;
 SimpleType originalSimpleType = new SimpleType(cls);
 SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
 JavaType expectedType = originalSimpleType.getType();
 assertEquals(expectedType, copiedSimpleType.getType());
 }

 @Test
 public void testConstructUnsafeMethod() {
 Class<?> cls = Integer.class;
 SimpleType simpleType = SimpleType.constructUnsafe(cls);
 JavaType expectedType = TypeFactory.defaultInstance().constructType(cls);
 assertEquals(expectedType, simpleType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructMethodWithMap() {
 Class<?> cls = HashSet.class;
 SimpleType.construct(cls);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructMethodWithCollection() {
 Class<?> cls = ArrayList.class;
 SimpleType.construct(cls);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructMethodWithArray() {
 Class<?> cls = int[].class;
 SimpleType.construct(cls);
 }

 @Test
 public void testNarrowMethod() {
 Class<?> subclass = Integer[].class;
 SimpleType simpleType = new SimpleType(Integer.class);
 JavaType narrowedType = simpleType.narrow(subclass);
 JavaType expectedType = TypeFactory.defaultInstance().constructType(subclass);
 assertEquals(expectedType, narrowedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidNarrowMethod() {
 Class<?> subclass = String.class;
 SimpleType simpleType = new SimpleType(Integer.class);
 simpleType.narrow(subclass);
 }

 @Test
 public void testWithTypeHandlerMethod() {
 Object typeHandler = new Object() {};
 SimpleType simpleType = new SimpleType(Integer.class);
 JavaType type = simpleType.withTypeHandler(typeHandler);
 assertEquals(typeHandler, type.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandlerMethod() {
 Object contentTypeHandler = new Object() {};
 SimpleType simpleType = new SimpleType(Integer.class);
 JavaType type = simpleType.withContentTypeHandler(contentTypeHandler);
 assertNull(type.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandlerMethod() {
 Object valueHandler = new Object() {};
 SimpleType simpleType = new SimpleType(Integer.class);
 JavaType type = simpleType.withValueHandler(valueHandler);
 assertEquals(valueHandler, type.getValueHandler());
 }

 @Test
 public void testWithContentValueHandlerMethod() {
 Object contentValueHandler = new Object() {};
 SimpleType simpleType = new SimpleType(Integer.class);
 JavaType type = simpleType.withContentValueHandler(contentValueHandler);
 assertNull(type.getContentValueHandler());
 }

 @Test
 public void testWithStaticTypingMethod() {
 SimpleType simpleType = new SimpleType(Integer.class);
 JavaType typeWithStaticTyping = simpleType.withStaticTyping();
 assertTrue(typeWithStaticTyping.isStatic());
 }

 @Test
 public void testRefineMethod() {
 Class<?> rawType = String[].class;
 SimpleType simpleType = new SimpleType(String.class);
 JavaType refinedType = simpleType.refine(rawType, TypeBindings.emptyBindings(), null, null);
 assertNull(refinedType);
 }

 @Test
 public void testBuildCanonicalNameMethod() {
 Class<?> cls = Integer[].class;
 SimpleType simpleType = new SimpleType(cls);
 String expectedCanonicalName = "[Ljava.lang.Integer;";
 String actualCanonicalName = simpleType.buildCanonicalName();
 assertEquals(expectedCanonicalName, actualCanonicalName);
 }
}