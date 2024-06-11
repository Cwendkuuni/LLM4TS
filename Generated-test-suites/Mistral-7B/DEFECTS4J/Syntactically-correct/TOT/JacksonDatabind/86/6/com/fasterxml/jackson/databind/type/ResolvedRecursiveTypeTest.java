import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 // Test for constructor and setting reference
 @Test
 public void testConstructorAndReference() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 JavaType javaType1 = new TypeFactory().constructType(type.getCanonicalName());
 JavaType javaType2 = new TypeFactory().constructType(String.class.getName());
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(type, bindings);
 recursiveType1.setReference(javaType1);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(type, bindings);
 recursiveType2.setReference(javaType2);

 assertNotSame(recursiveType1, recursiveType2);
 assertNull(recursiveType1.getSelfReferencedType());
 assertNull(recursiveType2.getSelfReferencedType());

 recursiveType1.setReference(javaType1);
 recursiveType2.setReference(javaType1);
 assertSame(recursiveType1, recursiveType2);
 assertSame(javaType1, recursiveType1.getSelfReferencedType());
 assertSame(javaType1, recursiveType2.getSelfReferencedType());
 }

 // Test for getSelfReferencedType method
 @Test
 public void testGetSelfReferencedType() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 JavaType javaType1 = new TypeFactory().constructType(type.getCanonicalName());
 JavaType javaType2 = new TypeFactory().constructType(String.class.getName());
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(type, bindings);

 recursiveType.setReference(javaType1);
 assertNull(recursiveType.getSelfReferencedType());
 recursiveType.setReference(javaType2);
 assertSame(javaType2, recursiveType.getSelfReferencedType());
 }

 // Test for equals method
 @Test
 public void testEquals() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 JavaType javaType1 = new TypeFactory().constructType(type.getCanonicalName());
 JavaType javaType2 = new TypeFactory().constructType(type.getCanonicalName());
 JavaType javaType3 = new TypeFactory().constructType(String.class.getName());
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(type, bindings);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(type, bindings);
 ResolvedRecursiveType recursiveType3 = new ResolvedRecursiveType(type, bindings);

 recursiveType1.setReference(javaType1);
 recursiveType2.setReference(javaType1);
 recursiveType3.setReference(javaType3);

 assertTrue(recursiveType1.equals(recursiveType1));
 assertTrue(recursiveType1.equals(recursiveType2));
 assertFalse(recursiveType1.equals(recursiveType3));
 }

 // Test for toString method
 @Test
 public void testToString() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 JavaType javaType1 = new TypeFactory().constructType(type.getCanonicalName());
 JavaType javaType2 = new TypeFactory().constructType(String.class.getName());
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(type, bindings);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(type, bindings);

 recursiveType1.setReference(javaType1);
 recursiveType2.setReference(javaType1);

 assertEquals("[recursive type; [Ljava.lang.Class;@6d00d662]", recursiveType1.toString());
 assertEquals("[recursive type; [Ljava.lang.Class;@18b4ef2d]", recursiveType2.toString());
 recursiveType1.setReference(javaType2);
 assertEquals("[recursive type; java.lang.String]", recursiveType1.toString());
 }
}