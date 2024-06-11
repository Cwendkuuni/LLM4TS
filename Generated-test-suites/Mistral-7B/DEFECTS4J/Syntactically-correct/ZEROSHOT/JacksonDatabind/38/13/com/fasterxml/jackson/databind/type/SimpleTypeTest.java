import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 SimpleType simpleType = new SimpleType(String.class);
 assertEquals(String.class, simpleType.getRawClass());
 assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType original = new SimpleType(String.class);
 SimpleType copy = new SimpleType(original);
 assertEquals(original, copy);
 }

 @Test
 public void testSimpleTypeWithContentTypeHandler() {
 try {
 SimpleType simpleType = new SimpleType(String.class);
 simpleType.withContentTypeHandler(new Object() {});
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object handler = new Object();
 SimpleType withHandler = simpleType.withTypeHandler(handler);
 assertEquals(handler, withHandler.getTypeHandler());
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object handler = new Object();
 SimpleType withHandler = simpleType.withValueHandler(handler);
 assertEquals(handler, withHandler.getValueHandler());
 }

 @Test
 public void testSimpleTypeStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType staticType = simpleType.withStaticTyping();
 assertTrue(staticType._asStatic);
 }

 @Test
 public void testSimpleTypeRefine() {
 SimpleType simpleType = new SimpleType(String.class);
 assertNull(simpleType.refine(Integer.class, TypeBindings.emptyBindings(), null, null));
 }

 @Test
 public void testSimpleTypeGetCanonicalName() {
 SimpleType simpleType = new SimpleType(String[].class);
 assertEquals("class [Ljava.lang.String;", simpleType.getCanonicalName());
 }

 @Test
 public void testSimpleTypeGetErasedSignature() {
 SimpleType simpleType = new SimpleType(List.class);
 assertEquals("[Ljava.util.List;", simpleType.getErasedSignature().toString());
 }

 @Test
 public void testSimpleTypeGetGenericSignature() {
 SimpleType simpleType = new SimpleType(List.class);
 StringBuilder expected = new StringBuilder("[");
 expected.append("java.util.List");
 expected.append("<");
 expected.append("T");
 expected.append(">");
 expected.append(']');
 assertEquals(expected.toString(), simpleType.getGenericSignature().toString());
 }

 @Test
 public void testSimpleTypeIsContainerType() {
 SimpleType simpleType = new SimpleType(String.class);
 assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testSimpleTypeConstructUnsafe() {
 Class<?> clazz = String.class;
 SimpleType simpleType = SimpleType.constructUnsafe(clazz);
 assertEquals(clazz, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeConstruct() {
 try {
 SimpleType simpleType = SimpleType.construct(String.class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSimpleTypeNarrow() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType narrowed = simpleType._narrow(String[].class);
 assertNotNull(narrowed);
 assertFalse(narrowed._asStatic);
 assertEquals(String[].class, narrowed.getRawClass());
 assertEquals(simpleType.getBindings(), narrowed.getBindings());
 assertEquals(simpleType.getSuperClass(), narrowed.getSuperClass());
 assertEquals(simpleType.getSuperInterfaces(), narrowed.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeWithContentType() {
 try {
 SimpleType simpleType = new SimpleType(String.class);
 simpleType.withContentType(new JavaType());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}