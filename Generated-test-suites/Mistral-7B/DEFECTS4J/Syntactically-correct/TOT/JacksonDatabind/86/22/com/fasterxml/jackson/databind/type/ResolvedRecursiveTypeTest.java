import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSettingReference() {
 TypeFactory tf = new TypeFactory();
 JavaType baseType = tf.constructType(String.class);
 JavaType recursiveType = new ResolvedRecursiveType(String[].class, new TypeBindings());

 // Set reference
 recursiveType.setReference(baseType);

 // Check if reference is set correctly
 assertEquals(baseType, recursiveType.getSelfReferencedType());
 }

 @Test
 public void testGetSelfReferencedType() {
 TypeFactory tf = new TypeFactory();
 JavaType baseType = tf.constructType(String.class);
 JavaType recursiveType = new ResolvedRecursiveType(String[].class, new TypeBindings());

 // Set reference
 recursiveType.setReference(baseType);

 // Check if getSelfReferencedType returns the correct reference
 JavaType selfReferencedType = recursiveType.getSelfReferencedType();
 assertEquals(baseType, selfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSettingReferenceTwice() {
 TypeFactory tf = new TypeFactory();
 JavaType baseType = tf.constructType(String.class);
 JavaType recursiveType = new ResolvedRecursiveType(String[].class, new TypeBindings());

 // Set reference once
 recursiveType.setReference(baseType);

 // Try to set reference again
 recursiveType.setReference(baseType);
 }

 @Test
 public void testGetGenericSignature() {
 TypeFactory tf = new TypeFactory();
 JavaType baseType = tf.constructType(String.class);
 JavaType recursiveType = new ResolvedRecursiveType(String[].class, new TypeBindings());
 StringBuilder sb = new StringBuilder();

 // Set reference
 recursiveType.setReference(baseType);

 // Check if getGenericSignature returns the correct signature
 recursiveType.getGenericSignature(sb);
 assertEquals("[Ljava.lang.String;", sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 TypeFactory tf = new TypeFactory();
 JavaType baseType = tf.constructType(String.class);
 JavaType recursiveType = new ResolvedRecursiveType(String[].class, new TypeBindings());
 StringBuilder sb = new StringBuilder();

 // Set reference
 recursiveType.setReference(baseType);

 // Check if getErasedSignature returns the correct signature
 recursiveType.getErasedSignature(sb);
 assertEquals("Ljava/lang/String;", sb.toString());
 }
}