import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReferenceAndGetSelfReferencedType() {
 JavaType javaType1 = new ClassType(String.class); // arbitrary JavaType
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(Object.class, null);
 recursiveType1.setReference(javaType1);
 JavaType expectedSelfReferencedType = javaType1;
 JavaType actualSelfReferencedType = recursiveType1.getSelfReferencedType();
 assertEquals(expectedSelfReferencedType, actualSelfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType javaType1 = new ClassType(String.class); // arbitrary JavaType
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(Object.class, null);
 recursiveType1.setReference(javaType1);
 recursiveType1.setReference(new ClassType(Integer.class)); // different JavaType
 }

 @Test
 public void testGetGenericSignatureAndErasedSignature() {
 JavaType javaType = new ArrayType(new ClassType(String.class), 5); // arbitrary JavaType
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getTypeId(), null);
 StringBuilder expectedGenericSignature = new StringBuilder("[Ljava.lang.String;");
 StringBuilder expectedErasedSignature = new StringBuilder("Ljava/lang/Object;[Ljava/lang/Object;");
 StringBuilder actualGenericSignature = recursiveType.getGenericSignature(new StringBuilder());
 StringBuilder actualErasedSignature = recursiveType.getErasedSignature(new StringBuilder());
 assertEquals(expectedGenericSignature, actualGenericSignature);
 assertEquals(expectedErasedSignature, actualErasedSignature);
 }

 @Test
 public void testWithContentType() {
 JavaType javaType = new ClassType(String.class); // arbitrary JavaType
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 JavaType expectedWithContentType = recursiveType;
 recursiveType = recursiveType.withContentType(javaType);
 assertEquals(expectedWithContentType, recursiveType);
 }

 @Test
 public void testOtherMethods() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 JavaType expected = recursiveType;
 JavaType actual = recursiveType.withTypeHandler(null);
 assertEquals(expected, actual);
 JavaType expected2 = recursiveType;
 JavaType actual2 = recursiveType.withContentTypeHandler(null);
 assertEquals(expected2, actual2);
 JavaType expected3 = recursiveType;
 JavaType actual3 = recursiveType.withValueHandler(null);
 assertEquals(expected3, actual3);
 JavaType expected4 = recursiveType;
 JavaType actual4 = recursiveType.withContentValueHandler(null);
 assertEquals(expected4, actual4);
 JavaType expected5 = recursiveType;
 JavaType actual5 = recursiveType.withStaticTyping();
 assertEquals(expected5, actual5);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 assertFalse(recursiveType.isContainerType());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType recursiveType3 = new ResolvedRecursiveType(String.class, null);
 assertTrue(recursiveType1.equals(recursiveType1));
 assertTrue(recursiveType1.equals(recursiveType2));
 assertFalse(recursiveType1.equals(recursiveType3));
 }
}