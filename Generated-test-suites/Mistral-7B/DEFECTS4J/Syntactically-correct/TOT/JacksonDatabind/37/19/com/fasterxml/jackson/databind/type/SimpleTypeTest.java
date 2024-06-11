import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstructor() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 assertEquals(cls, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeConstructorWithBindings() {
 Class<?> cls = String[].class;
 SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
 assertEquals(cls, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleCopyConstructor() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(simpleType1);
 assertEquals(simpleType1, simpleType2);
 }

 @Test
 public void testSimpleTypeNarrowing() {
 Class<?> stringClass = String.class;
 Class<?> stringArrayClass = String[].class;
 SimpleType simpleTypeString = new SimpleType(stringClass);
 SimpleType simpleTypeStringArray = new SimpleType(stringArrayClass);
 SimpleType narrowedType = simpleTypeStringArray._narrow(stringClass);
 assertEquals(simpleTypeString, narrowedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructUnsafe() {
 Class<?> cls = Map.class;
 SimpleType simpleType = SimpleType.constructUnsafe(cls);
 fail("Should not reach this point");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstruct() {
 Class<?> cls = Map.class;
 SimpleType simpleType = SimpleType.construct(cls);
 fail("Should not reach this point");
 }

 @Test
 public void testIsContainerType() {
 SimpleType simpleType = new SimpleType(String.class);
 assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testGetErasedSignature() {
 SimpleType simpleType = new SimpleType(String.class);
 StringBuilder sb = new StringBuilder(10);
 simpleType.getErasedSignature(sb);
 assertEquals("java.lang.String", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 SimpleType simpleType = new SimpleType(String[].class);
 StringBuilder sb = new StringBuilder(20);
 simpleType.getGenericSignature(sb);
 assertEquals("[Ljava.lang.String;", sb.toString());
 }

 @Test
 public void testToString() {
 SimpleType simpleType = new SimpleType(String.class);
 assertEquals("[simple type, class [Ljava.lang.String;]", simpleType.toString());
 }

 @Test
 public void testEquals() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(String.class);
 assertTrue(simpleType1.equals(simpleType2));
 SimpleType simpleType3 = new SimpleType(Integer.class);
 assertFalse(simpleType1.equals(simpleType3));
 }

 @Test
 public void testRefine() {
 Class<?> stringClass = String.class;
 SimpleType simpleType1 = new SimpleType(stringClass);
 JavaType refinedType = simpleType1.refine(Number[].class, TypeBindings.emptyBindings(), null, null);
 assertNull(refinedType);
 }

 @Test
 public void testWithContentType() {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType contentType = new SimpleType(Number.class);
 try {
 simpleType.withContentType(contentType);
 fail("Should throw an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object handler = new Object();
 SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
 assertNotSame(simpleType, withTypeHandler);
 }

 @Test
 public void testWithContentTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object handler = new Object();
 try {
 simpleType.withContentTypeHandler(handler);
 fail("Should throw anIllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object handler = new Object();
 SimpleType withValueHandler = simpleType.withValueHandler(handler);
 assertNotSame(simpleType, withValueHandler);
 }

 @Test
 public void testWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType withStaticTyping = simpleType.withStaticTyping();
 assertNotSame(simpleType, withStaticTyping);
 }

 @Test
 public void testTypeFactoryConstruction() {
 TypeFactory typeFactory = new TypeFactory();
 SimpleType simpleType = typeFactory.constructType(String.class);
 assertEquals(String.class, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }
}