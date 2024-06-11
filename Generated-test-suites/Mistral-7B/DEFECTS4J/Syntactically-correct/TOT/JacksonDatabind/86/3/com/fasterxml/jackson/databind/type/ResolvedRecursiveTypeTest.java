import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 JavaType refType = new TypeFactory().constructType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 recursiveType.setReference(refType);
 JavaType actualType = recursiveType.getSelfReferencedType();
 Assert.assertEquals(refType, actualType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType refType = new TypeFactory().constructType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 recursiveType.setReference(refType);
 recursiveType.setReference(new TypeFactory().constructType(Integer.class));
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = new TypeFactory().constructType(String.class);
 recursiveType.setReference(refType);
 String expected = "[recursive type; " + refType.getRawClass().getName() + "]";
 StringBuilder actual = recursiveType.getGenericSignature(sb);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder sb = new StringBuilder();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = new TypeFactory().constructType(String.class);
 recursiveType.setReference(refType);
 String expected = refType.getErasedSignature(sb).toString();
 String actual = recursiveType.getErasedSignature(sb).toString();
 assertEquals(expected, actual);
 }
}