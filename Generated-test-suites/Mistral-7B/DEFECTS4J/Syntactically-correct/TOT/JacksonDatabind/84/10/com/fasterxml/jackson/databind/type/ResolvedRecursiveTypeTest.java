import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 // Test case for setReference method
 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new TypeReference<List<Number>>() {}.getType();

 type.setReference(ref);
 JavaType expected = ref;
 JavaType actual = type.getSelfReferencedType();
 assertEquals(expected, actual);
 }

 // Test case for getSelfReferencedType method
 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, null);
 JavaType expected = null; // uninitialized
 JavaType actual = type.getSelfReferencedType();
 assertEquals(expected, actual);

 type.setReference(new TypeReference<List<Number>>() {}.getType());
 expected = new TypeReference<List<Number>>() {}.getType();
 actual = type.getSelfReferencedType();
 assertEquals(expected, actual);
 }

 // Test case for getGenericSignature and getErasedSignature methods
 @Test
 public void testGetSignatures() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Map.class, null);
 StringBuilder expected = new StringBuilder("[recursive type; ");
 StringBuilder actual;

 expected.append(Map.class.getName());
 expected.append("]");
 type.setReference(List.class);
 actual = new StringBuilder();
 type.getGenericSignature(actual);
 assertEquals(expected.toString(), actual.toString());

 expected.setLength(0);
 expected.append("[recursive type; ");
 expected.append(List.class.getName());
 expected.append("]");
 actual = new StringBuilder();
 type.getErasedSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 // Test case for equals method
 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object[].class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object[].class, null);
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(String[].class, null);
 JavaType ref = new TypeReference<List<Number>>() {}.getType();

 assertTrue(type1.equals(type1));
 assertTrue(type1.equals(type2));
 assertFalse(type1.equals(type3));
 assertFalse(type1.equals(null));

 type1.setReference(ref);
 type2.setReference(ref);
 assertTrue(type1.equals(type2));

 type1.setReference(ref);
 type3.setReference(new TypeReference<List<String>>() {}.getType());
 assertFalse(type1.equals(type3));
 }

 // Test case for toString method
 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Map.class, null);
 assertEquals("[recursive type; UNRESOLVED]", type.toString());

 JavaType ref = new TypeReference<List<Number>>() {}.getType();
 type.setReference(ref);
 assertEquals("[recursive type; [" + List.class.getName() + "]", type.toString());
 }
}