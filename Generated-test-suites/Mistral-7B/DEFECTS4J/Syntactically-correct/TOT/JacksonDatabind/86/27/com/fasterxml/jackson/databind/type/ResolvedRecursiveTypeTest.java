import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 JavaType originalType = TypeFactory.defaultInstance().constructType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, TypeBindings.empty());

 recursiveType.setReference(originalType);
 JavaType referencedType = recursiveType.getSelfReferencedType();

 assertEquals(originalType, referencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType originalType = TypeFactory.defaultInstance().constructType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, TypeBindings.empty());

 recursiveType.setReference(originalType);
 recursiveType.setReference(TypeFactory.defaultInstance().constructType(Integer.class));
 }

 @Test
 public void testGetGenericSignature() {
 TypeFactory typeFactory = TypeFactory.defaultInstance();
 JavaType originalType = typeFactory.constructType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, TypeBindings.empty());
 recursiveType.setReference(originalType);

 StringBuilder expected = new StringBuilder("[recursive type; ");
 expected.append(originalType.getGenericSignature(new StringBuilder()).toString());
 expected.append("]");

 StringBuilder actual = new StringBuilder(recursiveType.getGenericSignature(new StringBuilder()).toString());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 TypeFactory typeFactory = TypeFactory.defaultInstance();
 JavaType originalType = typeFactory.constructType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, TypeBindings.empty());
 recursiveType.setReference(originalType);

 StringBuilder expected = new StringBuilder(originalType.getRawClass().getName());
 StringBuilder actual = new StringBuilder(recursiveType.getErasedSignature(new StringBuilder()).toString());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testEquals() {
 JavaType originalType1 = TypeFactory.defaultInstance().constructType(String.class);
 JavaType originalType2 = TypeFactory.defaultInstance().constructType(String.class);
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String.class, TypeBindings.empty());
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(String.class, TypeBindings.empty());

 recursiveType1.setReference(originalType1);
 recursiveType2.setReference(originalType2);

 assertFalse(recursiveType1.equals(recursiveType2));
 recursiveType1.setReference(originalType2);
 recursiveType2.setReference(originalType1);

 assertTrue(recursiveType1.equals(recursiveType2));
 }

 @Test
 public void testToString() {
 JavaType originalType = TypeFactory.defaultInstance().constructType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, TypeBindings.empty());
 recursiveType.setReference(originalType);

 String expected = "[recursive type; " + originalType.getRawClass().getName() + "]";
 String actual = recursiveType.toString();
 assertEquals(expected, actual);
 }
}