import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.core.type.TypeReference;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 TypeBindings bindings = TypeBindings.empty();
 JavaType javaType1 = new TypeFactory().constructType(String[].class);
 JavaType javaType2 = new TypeFactory().constructType(ResolvedRecursiveType.class, bindings, javaType1, null, 0, null, null, false);
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, bindings);
 JavaType referencedType = javaType2;

 recursiveType1.setReference(referencedType);
 Assert.assertNotNull(recursiveType1.getSelfReferencedType());
 Assert.assertEquals(referencedType, recursiveType1.getSelfReferencedType());

 // error scenario: calling setReference multiple times
 JavaType referencedType2 = new TypeFactory().constructType(Integer.class);
 recursiveType1.setReference(referencedType2);
 try {
 recursiveType1.setReference(referencedType);
 } catch (IllegalStateException e) {
 Assert.assertEquals("Trying to re-set self reference", e.getMessage());
 }
 }

 @Test
 public void testGetSelfReferencedType() {
 TypeBindings bindings = TypeBindings.empty();
 JavaType javaType = new TypeFactory().constructType(ResolvedRecursiveType.class, bindings, null, null, 0, null, null, false);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, bindings);
 recursiveType.setReference(javaType);

 Assert.assertNotNull(recursiveType.getSelfReferencedType());
 Assert.assertEquals(javaType, recursiveType.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 TypeBindings bindings = TypeBindings.empty();
 JavaType javaType = new TypeFactory().constructType(ResolvedRecursiveType.class, bindings, null, null, 0, null, null, false);
 StringBuilder expected = new StringBuilder("java.lang.Class<[recursive type; java.lang.Class<com.fasterxml.jackson.databind.type.ResolvedRecursiveType>]>");
 StringBuilder result = new StringBuilder();
 javaType.getGenericSignature(result);

 Assert.assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testGetErasedSignature() {
 TypeBindings bindings = TypeBindings.empty();
 JavaType javaType = new TypeFactory().constructType(ResolvedRecursiveType.class, bindings, null, null, 0, null, null, false);
 StringBuilder expected = new StringBuilder("Lcom/fasterxml/jackson/databind/type/ResolvedRecursiveType;");
 StringBuilder result = new StringBuilder();
 javaType.getErasedSignature(result);

 Assert.assertEquals(expected.toString(), result.toString());
 }

 // Add more test cases here for other overridden methods if needed
}