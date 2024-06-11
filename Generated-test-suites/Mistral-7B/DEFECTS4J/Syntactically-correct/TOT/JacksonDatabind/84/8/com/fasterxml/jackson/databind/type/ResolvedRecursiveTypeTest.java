import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 JavaType refType = new TypeBase(String.class, null, null, 0, null, null, null, false);
 ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
 selfRefType.setReference(refType);
 JavaType expectedSelfRefType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings()).setReference(refType);
 assertEquals("Self reference not set correctly", expectedSelfRefType, selfRefType);

 refType = null;
 selfRefType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
 try {
 selfRefType.setReference(refType);
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 String actualMessage = e.getMessage();
 assertTrue("Unexpected message", actualMessage.contains("Trying to re-set self reference"));
 }
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertNull("Expected uninitialized self reference", selfRefType.getSelfReferencedType());

 JavaType refType = new TypeBase(String.class, null, null, 0, null, null, null, false);
 selfRefType.setReference(refType);
 JavaType expectedSelfRefType = refType;
 JavaType actualSelfRefType = selfRefType.getSelfReferencedType();
 assertEquals("Self reference not set correctly", expectedSelfRefType, actualSelfRefType);
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder stringBuilder = new StringBuilder();
 ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, new TypeBindings());
 selfRefType.getGenericSignature(stringBuilder);
 String expectedSignature = "[Ljava/lang/String;";
 String actualSignature = stringBuilder.toString();
 assertEquals("Generic signature not as expected", expectedSignature, actualSignature);

 JavaType refType = new TypeBase(String.class, null, null, 0, null, null, null, false);
 selfRefType.setReference(refType);
 selfRefType.getGenericSignature(stringBuilder);
 expectedSignature = "[Lcom.fasterxml.jackson.databind.TypeBase;";
 actualSignature = stringBuilder.toString();
 assertEquals("Generic signature not as expected", expectedSignature, actualSignature);
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder stringBuilder = new StringBuilder();
 ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, new TypeBindings());
 selfRefType.getErasedSignature(stringBuilder);
 String expectedSignature = "Ljava/lang/String;";
 String actualSignature = stringBuilder.toString();
 assertEquals("Erased signature not as expected", expectedSignature, actualSignature);

 JavaType refType = new TypeBase(String.class, null, null, 0, null, null, null, false);
 selfRefType.setReference(refType);
 selfRefType.getErasedSignature(stringBuilder);
 expectedSignature = "Lcom/fasterxml/jackson/databind/TypeBase;";
 actualSignature = stringBuilder.toString();
 assertEquals("Erased signature not as expected", expectedSignature, actualSignature);
 }

 @Test
 public void testWithContentType() {
 JavaType contentType = new TypeBase(Integer.class, null, null, 0, null, null, null, false);
 ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType expectedSelfRefType = selfRefType.withContentType(contentType);
 JavaType actualSelfRefType = new ResolvedRecursiveType(String.class, new TypeBindings()).withContentType(contentType);
 assertEquals("Self reference with contentType not created correctly", expectedSelfRefType, actualSelfRefType);
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object();
 ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType expectedSelfRefType = selfRefType.withTypeHandler(handler);
 JavaType actualSelfRefType = new ResolvedRecursiveType(String.class, new TypeBindings()).withTypeHandler(handler);
 assertEquals("Self reference with typeHandler not created correctly", expectedSelfRefType, actualSelfRefType);
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = new Object();
 ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType expectedSelfRefType = selfRefType.withContentTypeHandler(handler);
 JavaType actualSelfRefType = new ResolvedRecursiveType(String.class, new TypeBindings()).withContentTypeHandler(handler);
 assertEquals("Self reference with contentTypeHandler not created correctly", expectedSelfRefType, actualSelfRefType);
 }

 @Test
 public void testWithValueHandler() {
 Object handler = new Object();
 ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType expectedSelfRefType = selfRefType.withValueHandler(handler);
 JavaType actualSelfRefType = new ResolvedRecursiveType(String.class, new TypeBindings()).withValueHandler(handler);
 assertEquals("Self reference with valueHandler not created correctly", expectedSelfRefType, actualSelfRefType);
 }

 @Test
 public void testWithContentValueHandler() {
 Object handler = new Object();
 ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType expectedSelfRefType = selfRefType.withContentValueHandler(handler);
 JavaType actualSelfRefType = new ResolvedRecursiveType(String.class, new TypeBindings()).withContentValueHandler(handler);
 assertEquals("Self reference with contentValueHandler not created correctly", expectedSelfRefType, actualSelfRefType);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType expectedSelfRefType = selfRefType.withStaticTyping();
 JavaType actualSelfRefType = new ResolvedRecursiveType(String.class, new TypeBindings()).withStaticTyping();
 assertEquals("Self reference with staticTyping not created correctly", expectedSelfRefType, actualSelfRefType);
 }
}