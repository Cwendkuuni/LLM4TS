import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetAndGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType expectedType = new SimpleType(String.class);

 type.setReference(expectedType);
 JavaType actualType = type.getSelfReferencedType();

 assertNotNull(actualType);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetSelfReferencedTypeTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType expectedType = new SimpleType(String.class);
 JavaType existingType = new SimpleType(Integer.class);
 type.setReference(existingType);
 type.setReference(expectedType);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, null);
 String expectedSignature = "Ljava/lang/Object;[Ljava/lang/Object;";
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);

 assertEquals(expectedSignature, sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, null);
 String expectedSignature = "[Ljava/lang/Object;";
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);

 assertEquals(expectedSignature, sb.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType contentType = new SimpleType(Integer.class);
 JavaType expectedType = type.withContentType(contentType);

 assertSame(type, expectedType);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 JavaType selfReferenceType1 = new SimpleType(Integer.class);
 JavaType selfReferenceType2 = new SimpleType(Integer.class);
 type1.setReference(selfReferenceType1);
 type2.setReference(selfReferenceType2);

 assertFalse(type1.equals(type2));

 ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, null);
 type3.setReference(selfReferenceType1);

 assertTrue(type1.equals(type3));
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType selfReferenceType = new SimpleType(Integer.class);
 type.setReference(selfReferenceType);
 String expectedString = "[recursive type; " + selfReferenceType.getRawClass().getName() + "]";

 assertEquals(expectedString, type.toString());
 }

 @Test
 public void testUnresolvedReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 assertNull(type.getSelfReferencedType());
 }
}