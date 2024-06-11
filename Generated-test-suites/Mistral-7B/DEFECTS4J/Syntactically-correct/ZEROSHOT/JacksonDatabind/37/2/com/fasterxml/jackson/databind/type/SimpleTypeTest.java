import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<?> simpleClass = String.class;
 SimpleType simpleType = new SimpleType(simpleClass);
 SimpleType simpleTypeConstruct = SimpleType.construct(simpleClass);
 assertEquals(simpleClass, simpleType.getRawClass());
 assertEquals(simpleType, simpleTypeConstruct);
 }

 @Test
 public void testSimpleTypeUnsafeConstruction() {
 Class<?> simpleClass = String.class;
 SimpleType simpleTypeUnsafe = SimpleType.constructUnsafe(simpleClass);
 SimpleType simpleType = new SimpleType(simpleClass);
 assertEquals(simpleClass, simpleTypeUnsafe.getRawClass());
 assertEquals(simpleType, simpleTypeUnsafe);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidConstruction() {
 Class<?> mapClass = HashMap.class;
 SimpleType simpleType = new SimpleType(mapClass);
 fail("Should not be able to construct SimpleType for a Map");
 }

 @Test
 public void testSimpleTypeNarrowing() {
 Class<?> stringClass = String.class;
 Class<?> stringBufferClass = StringBuffer.class;
 SimpleType stringType = new SimpleType(stringClass);
 SimpleType stringBufferType = stringType.narrow(StringBuffer.class);
 assertEquals(stringBufferClass, stringBufferType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidNarrowing() {
 Class<?> stringClass = String.class;
 Class<?> integerClass = Integer.class;
 SimpleType stringType = new SimpleType(stringClass);
 SimpleType integerType = stringType.narrow(Integer.class);
 fail("String is not a subclass of Integer, should not be able to narrow");
 }

 @Test
 public void testSimpleTypeHashCode() {
 Class<?> stringClass = String.class;
 SimpleType stringType1 = new SimpleType(stringClass);
 SimpleType stringType2 = new SimpleType(stringClass);
 assertEquals(stringType1.hashCode(), stringType2.hashCode());
 }

 @Test
 public void testSimpleTypeToString() {
 Class<?> stringClass = String.class;
 SimpleType stringType = new SimpleType(stringClass);
 assertEquals("[simple type, class [Ljava.lang.String;]", stringType.toString());
 }

 @Test
 public void testSimpleTypeEquals() {
 Class<?> stringClass = String.class;
 SimpleType stringType1 = new SimpleType(stringClass);
 SimpleType stringType2 = new SimpleType(stringClass);
 SimpleType stringType3 = new SimpleType(Integer.class);
 SimpleType stringType4 = stringType1;
 assertTrue(stringType1.equals(stringType1));
 assertTrue(stringType1.equals(stringType2));
 assertFalse(stringType1.equals(stringType3));
 assertFalse(stringType1.equals(stringType4)); // should be same instance
 }

 @Test
 public void testSimpleTypeTypeId() {
 Class<?> stringClass = String.class;
 SimpleType stringType = new SimpleType(stringClass);
 assertEquals("[simple type, class java.lang.String]", stringType.getId().toString());
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 Class<?> stringClass = String.class;
 SimpleType stringType = new SimpleType(stringClass);
 Object handler = new Object() {};
 SimpleType stringTypeWithHandler = stringType.withTypeHandler(handler);
 assertNotEquals(stringType, stringTypeWithHandler);
 assertEquals(handler, stringTypeWithHandler.getTypeHandler());
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 Class<?> stringClass = String.class;
 SimpleType stringType = new SimpleType(stringClass);
 Object handler = new Object() {};
 SimpleType stringTypeWithHandler = stringType.withValueHandler(handler);
 assertNotEquals(stringType, stringTypeWithHandler);
 assertEquals(handler, stringTypeWithHandler.getValueHandler());
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 Class<?> stringClass = String.class;
 SimpleType stringType = new SimpleType(stringClass, null, null, null, null, null, false);
 SimpleType stringTypeStatic = stringType.withStaticTyping();
 assertTrue(stringTypeStatic.isStaticTyping());
 }

 @Test
 public void testSimpleTypeWithContentType() {
 try {
 Class<?> stringClass = String.class;SimpleType stringType = new SimpleType(stringClass);
 JavaType contentType = new SimpleType(Integer.class);
 SimpleType stringTypeWithContentType = stringType.withContentType(contentType);
 fail("Should not be able to set content type for a simple type");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSimpleTypeWithContentTypeHandler() {
 try {
 Class<?> stringClass = String.class;SimpleType stringType = new SimpleType(stringClass);JavaType contentType = new SimpleType(Integer.class);
 SimpleType stringTypeWithContentTypeHandler = stringType.withContentTypeHandler(contentType);
 fail("Should not be able to set content type handler for a simple type");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSimpleTypeWithTypeBindings() {
 Class<?> listClass = ArrayList.class;
 Class<?> stringClass = String.class;
 Class<?> integerClass = Integer.class;
 SimpleType listType = new SimpleType(listClass);
 SimpleType stringType = new SimpleType(stringClass);
 SimpleType integerType = new SimpleType(integerClass);
 TypeBindings bindings = TypeBindings.map("list", listType, "string", stringType, "integer", integerType);
 SimpleType listTypeWithBindings = listType.withBindings(bindings);
 SimpleType listTypeWithBindings2 = listType.withBindings(bindings);
 assertEquals(listTypeWithBindings, listTypeWithBindings2);
 }

 @Test
 public void testSimpleTypeWithValueHandlersAndTypeHandlers() {
 Class<?> listClass = ArrayList.class;
 Class<?> stringClass = String.class;
 Class<?> integerClass = Integer.class;
 SimpleType listType = new SimpleType(listClass);
 SimpleType stringType = new SimpleType(stringClass);
 SimpleType integerType = new SimpleType(integerClass);
 Object valueHandler = new Object() {};
 Object typeHandler = new Object() {};
 SimpleType listTypeWithHandlers = listType.withValueHandler(valueHandler).withTypeHandler(typeHandler);
 SimpleType listTypeWithHandlers2 = listType.withValueHandler(valueHandler).withTypeHandler(typeHandler);
 assertEquals(listTypeWithHandlers, listTypeWithHandlers2);
 }

 @Test
 public void testSimpleTypeWithSuperInterfaces() {
 Class<?> listClass = ArrayList.class;
 Class<?> listInterface = List.class;
 SimpleType listType = new SimpleType(listClass);
 SimpleType listInterfaceType = new SimpleType(listInterface);
 SimpleType listTypeWithSuperInterfaces = listType.withSuperInterfaces(new JavaType[] {listInterfaceType});
 assertEquals(Arrays.asList(new JavaType[] {listInterfaceType}), listTypeWithSuperInterfaces.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeWithSuperClass() {
 Class<?> listClass = ArrayList.class;
 Class<?> listInterface = List.class;
 SimpleType listInterfaceType = new SimpleType(listInterface);
 SimpleType listType = new SimpleType(listClass);
 SimpleType listTypeWithSuperClass = listType.withSuperClass(listInterfaceType.getRawClass());
 assertEquals(listInterfaceType.getRawClass(), listTypeWithSuperClass.getSuperClass());
 }
}