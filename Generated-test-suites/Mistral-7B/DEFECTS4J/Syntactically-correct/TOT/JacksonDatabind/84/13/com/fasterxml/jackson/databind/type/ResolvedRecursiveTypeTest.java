import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

 private JavaType createResolvedRecursiveType(Class<?> erasedType) {
 TypeBindings bindings = new TypeBindings();
 return new ResolvedRecursiveType(erasedType, bindings);
 }

 @Test
 public void testSetReference() {
 // Test setting reference
 JavaType baseType = createResolvedRecursiveType(String.class);
 JavaType referenceType = createResolvedRecursiveType(Integer.class);

 baseType.setReference(referenceType);
 assertNotNull(baseType.getSelfReferencedType());
 assertEquals(referenceType, baseType.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType baseType = createResolvedRecursiveType(String.class);
 JavaType referenceType = createResolvedRecursiveType(Integer.class);

 baseType.setReference(referenceType);
 baseType.setReference(referenceType); // Should throw exception
 }

 @Test
 public void testGetSelfReferencedType() {
 JavaType baseType = createResolvedRecursiveType(String.class);
 JavaType referenceType = createResolvedRecursiveType(Integer.class);
 baseType.setReference(referenceType);

 assertEquals(referenceType, baseType.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 JavaType baseType = createResolvedRecursiveType(List.class);
 JavaType listOfStringType = createResolvedRecursiveType(List.class);
 baseType.setReference(listOfStringType);

 listOfStringType.setReference(baseType); // Resolves to a loop

 baseType.getGenericSignature(sb);
 // The signature of a recursive type will not be printed
 // due to [databind#1301] as mentioned in the class comments
 assertNull(sb.toString());
 }

 @Test
 public void testEquals() {
 JavaType baseType1 = createResolvedRecursiveType(String.class);
 JavaType baseType2 = createResolvedRecursiveType(String.class);
 JavaType referenceType = createResolvedRecursiveType(Integer.class);

 baseType1.setReference(referenceType);
 baseType2.setReference(referenceType);

 assertTrue(baseType1.equals(baseType2));
 }

 @Test
 public void testEqualsDifferentReferencedTypes() {
 JavaType baseType1 = createResolvedRecursiveType(String.class);
 JavaType referenceType1 = createResolvedRecursiveType(Integer.class);
 JavaType baseType2 = createResolvedRecursiveType(String.class);
 JavaType referenceType2 = createResolvedRecursiveType(Long.class);

 baseType1.setReference(referenceType1);
 baseType2.setReference(referenceType2);

 assertFalse(baseType1.equals(baseType2));
 }
}