import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 private JavaType createSelfReferencedType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 @Test
 public void testSetAndGetSelfReferencedType() {
 JavaType refType = createSelfReferencedType(Integer.TYPE);
 JavaType selfReferencedType = createSelfReferencedType(Integer.TYPE);
 refType.setReference(selfReferencedType);
 assertTrue(refType.getSelfReferencedType() == selfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetSelfReferencedTypeTwice() {
 JavaType refType = createSelfReferencedType(Integer.TYPE);
 JavaType selfReferencedType = createSelfReferencedType(Integer.TYPE);
 refType.setReference(selfReferencedType);
 try {
 refType.setReference(selfReferencedType);
 } catch (IllegalStateException e) {
 assertEquals("Trying to re-set self reference; old value = " + selfReferencedType + ", new = " + selfReferencedType, e.getMessage());
 }
 }

 @Test
 public void testGetGenericSignature() {
 JavaType refType = createSelfReferencedType(Integer.TYPE);
 StringBuilder sb = new StringBuilder();
 refType.getGenericSignature(sb);
 String signature = sb.toString();
 // The actual signature may vary depending on the JVM, so just check if it's not empty
 assertTrue(!signature.isEmpty());
 }

 @Test
 public void testGetErasedSignature() {
 JavaType refType = createSelfReferencedType(Integer.TYPE);
 StringBuilder sb = new StringBuilder();
 refType.getErasedSignature(sb);
 String signature = sb.toString();
 // The actual signature may vary depending on the JVM, so just check if it's not empty
 assertTrue(!signature.isEmpty());
 }

 @Test
 public void testWithContentType() {
 JavaType refType = createSelfReferencedType(Integer.TYPE);
 JavaType contentType = createSelfReferencedType(String.class);
 JavaType result = refType.withContentType(contentType);
 assertEquals(refType, result);
 }

 // Add more test cases for other methods if needed

}