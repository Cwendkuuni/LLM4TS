import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 private JavaType createResolvedRecursiveType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, new TypeBindings());
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type1 = createResolvedRecursiveType(ResolvedRecursiveType.class);
 JavaType refType = createResolvedRecursiveType(String.class);
 type1.setReference(refType);
 ResolvedRecursiveType type2 = createResolvedRecursiveType(ResolvedRecursiveType.class);
 type2.setReference(type1);

 assertEquals(type1.getSelfReferencedType(), type2.getSelfReferencedType());
 assertEquals(refType, type1.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = createResolvedRecursiveType(ResolvedRecursiveType.class);
 JavaType refType = createResolvedRecursiveType(String.class);
 type.setReference(refType);
 type.setReference(refType);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = createResolvedRecursiveType(String[].class);
 StringBuilder sb = new StringBuilder(100);
 type.getGenericSignature(sb);

 String expected = "[Ljava.lang.String;[recursive type; Ljava.lang.Class<[Ljava.lang.String;>; Ljava.lang.Class<java.lang.reflect.TypeBindings>;]";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = createResolvedRecursiveType(String[].class);
 StringBuilder sb = new StringBuilder(50);
 type.getErasedSignature(sb);

 String expected = "[Ljava.lang.String;";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = createResolvedRecursiveType(String[].class);
 JavaType contentType = createResolvedRecursiveType(Integer.class);
 ResolvedRecursiveType result = type.withContentType(contentType);

 assertEquals(type, result);
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = createResolvedRecursiveType(String[].class);
 assertEquals("[recursive type; UNRESOLVED]", type.toString());

 JavaType refType = createResolvedRecursiveType(String[].class);
 type.setReference(refType);
 assertEquals("[recursive type; [" + refType.getRawClass().getName() + "]", type.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = createResolvedRecursiveType(String[].class);
 ResolvedRecursiveType type2 = createResolvedRecursiveType(String[].class);
 assertFalse(type1.equals(type2));

 JavaType refType = createResolvedRecursiveType(String[].class);
 type1.setReference(refType);
 type2.setReference(refType);
 assertTrue(type1.equals(type2));
 }
}