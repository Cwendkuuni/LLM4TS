import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 private JavaType createResolvedRecursiveType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, new TypeBindings());
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = createResolvedRecursiveType(ResolvedRecursiveType.class);
 JavaType referenceType = createResolvedRecursiveType(String.class);

 type.setReference(referenceType);
 JavaType selfReferencedType = type.getSelfReferencedType();

 assertTrue("Expected reference to be set", referenceType.equals(selfReferencedType));
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = createResolvedRecursiveType(ResolvedRecursiveType.class);
 JavaType referenceType = createResolvedRecursiveType(String.class);
 JavaType referenceType2 = createResolvedRecursiveType(Integer.class);

 type.setReference(referenceType);
 type.setReference(referenceType2);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = createResolvedRecursiveType(ResolvedRecursiveType.class);
 StringBuilder expected = new StringBuilder("[recursive type; UNRESOLVED]");
 StringBuilder actual = type.getGenericSignature(new StringBuilder());

 assertEquals("Expected UNRESOLVED signature", expected.toString(), actual.toString());
 }

 @Test
 public void testGetGenericSignatureWithReference() {
 ResolvedRecursiveType type = createResolvedRecursiveType(ResolvedRecursiveType.class);
 JavaType referenceType = createResolvedRecursiveType(String.class);
 type.setReference(referenceType);

 StringBuilder expected = new StringBuilder("[recursive type; ");
 expected.append(referenceType.getErasedSignature(new StringBuilder()).toString());
 expected.append(']');
 StringBuilder actual = type.getGenericSignature(new StringBuilder());

 assertEquals("Expected signedature to match", expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = createResolvedRecursiveType(ResolvedRecursiveType.class);
 StringBuilder expected = new StringBuilder("com.fasterxml.jackson.databind.type.ResolvedRecursiveType");
 StringBuilder actual = type.getErasedSignature(new StringBuilder());

 assertEquals("Expected erased signature to match", expected.toString(), actual.toString());
 }

 @Test
 public void testMethodsThatReturnThis() {
 ResolvedRecursiveType type = createResolvedRecursiveType(ResolvedRecursiveType.class);
 JavaType result = type.withContentType(null);

 assertEquals("Expected this type to be returned", type.getClass(), result.getClass());
 }

 @Test
 public void testDeprecatedNarrowMethod() {
 ResolvedRecursiveType type = createResolvedRecursiveType(ResolvedRecursiveType.class);
 Class<?> subclass = ResolvedRecursiveType.class;
 JavaType result = type._narrow(subclass);

 assertNull("Expected _narrow method to be deprecated and do nothing", result);
 }
}