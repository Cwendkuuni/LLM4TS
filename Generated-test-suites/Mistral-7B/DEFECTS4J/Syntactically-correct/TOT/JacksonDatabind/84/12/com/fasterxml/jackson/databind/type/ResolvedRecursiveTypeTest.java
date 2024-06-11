import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
 JavaType ref = new SimpleType(String.class);
 type1.setReference(ref);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
 type2.setReference(type1); // Test setting a reference to another ResolvedRecursiveType

 // Test exception when trying to set a reference multiple times
 try {
 type1.setReference(ref);
 fail("Expected IllegalStateException when setting reference multiple times.");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.class, new TypeBindings());
 type.setReference(new SimpleType(String.class));
 JavaType referencedType = type.getSelfReferencedType();
 assertEquals(String.class, referencedType.getRawClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetSelfReferencedTypeTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.class, new TypeBindings());
 type.setReference(new SimpleType(String.class));
 JavaType referencedType = type.getSelfReferencedType();
 JavaType referencedType2 = type.getSelfReferencedType();
 fail("Expected IllegalStateException when getting self-referenced type twice.");
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.class, new TypeBindings());
 StringBuilder sb = new StringBuilder(100);
 type.getGenericSignature(sb);
 String expected = "[Ljava.lang.Class;<T:Ljava.lang.Object;>;[Ljava.lang.Class;<LResolvedRecursiveType;>;]";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.class, new TypeBindings());
 StringBuilder sb = new StringBuilder(100);
 type.getErasedSignature(sb);
 String expected = "Ljava/lang/Object;Ljava/lang/Class;";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 // Add more test cases for other methods if necessary
}