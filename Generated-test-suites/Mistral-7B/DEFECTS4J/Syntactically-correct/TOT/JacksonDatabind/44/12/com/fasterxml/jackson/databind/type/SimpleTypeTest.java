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
 assertEquals(expectedType, simpleType.getType());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 Class<?> cls = String.class;
 SimpleType simpleType1 = new SimpleType(cls);
 SimpleType simpleType2 = new SimpleType(simpleType1);
 JavaType expectedType = TypeFactory.defaultInstance().constructType(cls);
 assertEquals(expectedType, simpleType1.getType());
 assertEquals(expectedType, simpleType2.getType());
 }

 @Test
 public void testSimpleTypeConstructStatic() {
 Class<?> cls = String[].class;
 SimpleType simpleType = SimpleType.constructUnsafe(cls);
 JavaType expectedType = TypeFactory.defaultInstance().constructType(cls);
 assertEquals(expectedType, simpleType.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructError() {
 Class<?> cls = Map.class;
 SimpleType simpleType = new SimpleType(cls);
 fail("Should not be able to construct SimpleType for a Map");
 }

 @Test
 public void testSimpleTypeNarrow() {
 Class<?> subclass = String.class;
 SimpleType simpleType = new SimpleType(Number.class);
 JavaType narrowType = simpleType.narrow(subclass);
 assertEquals(simpleType.getType(), simpleType);
 JavaType expectedNarrowType = TypeFactory.defaultInstance().constructType(subclass);
 assertEquals(expectedNarrowType, narrowType);
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 Object handler = new Object() {
 @Override
 public JavaType constructType(Class<?> cls) {
 return TypeFactory.defaultInstance().constructType(cls);
 }
 };
 SimpleType simpleType = new SimpleType(String.class);
 JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
 SimpleType newSimpleType = simpleType.withTypeHandler(handler);
 assertEquals(expectedType, newSimpleType.getType());
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 Object handler = new Object() {
 @Override
 public Object valueFromString(String str) {
 return null;
 }
 };
 SimpleType simpleType = new SimpleType(String.class);
 Object value = simpleType.valueHandler.valueFromString("test");
 assertNull(value);
 SimpleType newSimpleType = simpleType.withValueHandler(handler);
 Object newValue = newSimpleType.valueHandler.valueFromString("test");
 assertNull(newValue);
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType staticSimpleType = simpleType.withStaticTyping();
 assertFalse(simpleType.isStatic());
 assertTrue(staticSimpleType.isStatic());
 }

 @Test
 public void testSimpleTypeEquals() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(String.class);
 SimpleType simpleType3 = new SimpleType(Integer.class);
 assertEquals(simpleType1, simpleType1);
 assertEquals(simpleType1, simpleType2);
 assertNotEquals(simpleType1, simpleType3);
 }

 @Test
 public void testSimpleTypeCanonicalName() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 String expectedName = "class java.lang.String";
 assertEquals(expectedName, simpleType.buildCanonicalName());
 }
}