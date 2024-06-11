import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType reference = new BasicType(Integer.class);

 // Test setting a valid reference
 type.setReference(reference);
 JavaType selfReferencedType = type.getSelfReferencedType();
 assertEquals(reference, selfReferencedType);

 // Test setting a reference multiple times
 type.setReference(reference);
 selfReferencedType = type.getSelfReferencedType();
 assertEquals(reference, selfReferencedType);

 type.setReference(reference); // should throw an exception
 try {
 type.setReference(reference);
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testGetSelfReferencedType() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType reference = new BasicType(Integer.class);
 type.setReference(reference);
 JavaType selfReferencedType = type.getSelfReferencedType();
 assertEquals(reference, selfReferencedType);
 }

 @Test
 public void testGetGenericSignature() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 StringBuilder sb = new StringBuilder();
 JavaType reference = new BasicType(Integer.class);
 type.setReference(reference);
 String expected = "[recursive type; " + reference.getGenericSignature(new StringBuilder()).toString() + "]";
 assertEquals(expected, type.getGenericSignature(sb).toString());
 }

 @Test
 public void testGetErasedSignature() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 StringBuilder sb = new StringBuilder();
 JavaType reference = new BasicType(Integer.class);
 type.setReference(reference);
 String expected = reference.getErasedSignature(sb).toString();
 assertEquals(expected, type.getErasedSignature(sb).toString());
 }

 @Test
 public void testWithContentType() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType contentType = new BasicType(Integer.class);
 JavaType result = type.withContentType(contentType);
 assertEquals(type, result);
 }

 @Test
 public void testWithTypeHandler() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType result = type.withTypeHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType result = type.withContentTypeHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testWithValueHandler() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType result = type.withValueHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testWithContentValueHandler() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType result = type.withContentValueHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testWithStaticTyping() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType result = type.withStaticTyping();
 assertEquals(type, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrow() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Class<?> subclass = IntType.class;
 JavaType result = type._narrow(subclass);
 fail("Unexpected result");
 }

 @Test
 public void testRefine() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Class<?> rawType = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(result);
 }

 @Test
 public void testIsContainerType() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 boolean result = type.isContainerType();
 assertFalse(result);
 }

 @Test
 public void testToString() {
 TypeBase type = new ResolvedRecursiveType(String.class, new TypeBindings());
 String expected = "[recursive type; UNRESOLVED]";
 assertEquals(expected, type.toString());

 JavaType reference = new BasicType(Integer.class);
 type.setReference(reference);
 expected = "[recursive type; " + reference.getRawClass().getName() + "]";
 assertEquals(expected, type.toString());
 }

 @Test
 public void testEquals() {
 TypeBase type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 TypeBase type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(type1.equals(type2));

 JavaType reference = new BasicType(Integer.class);
 TypeBase type3 = new ResolvedRecursiveType(String.class, new TypeBindings());
 type3.setReference(reference);
 assertFalse(type1.equals(type3));

 TypeBase type4 = new ResolvedRecursiveType(String.class, new TypeBindings());
 type4.setReference(reference);
 assertTrue(type1.equals(type4));
 }
}