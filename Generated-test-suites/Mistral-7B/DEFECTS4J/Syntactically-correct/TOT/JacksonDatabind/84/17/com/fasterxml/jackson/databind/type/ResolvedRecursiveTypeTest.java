import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new JavaTypeImpl(String.class);
 type.setReference(ref);
 JavaType expected = ref;
 JavaType actual = type.getSelfReferencedType();
 assertNotNull(actual);
 assertSame(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref1 = new JavaTypeImpl(String.class);
 type.setReference(ref1);
 JavaType ref2 = new JavaTypeImpl(Integer.class);
 type.setReference(ref2);
 }

 @Test
 public void testGetSelfReferencedTypeNull() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType actual = type.getSelfReferencedType();
 assertNull(actual);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 StringBuilder expected = new StringBuilder();
 String result = type.getGenericSignature(expected).toString();
 StringBuilder actual = new StringBuilder();
 type.getGenericSignature(actual);
 assertSame(result, actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 StringBuilder expected = new StringBuilder();
 String result = type.getErasedSignature(expected).toString();
 StringBuilder actual = new StringBuilder();
 type.getErasedSignature(actual);
 assertSame(result, actual.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = new JavaTypeImpl(Integer.class);
 JavaType expected = type;
 JavaType actual = type.withContentType(contentType);
 assertSame(expected, actual);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType expected = type;
 JavaType actual = type.withTypeHandler(handler);
 assertSame(expected, actual);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType expected = type;
 JavaType actual = type.withContentTypeHandler(handler);
 assertSame(expected, actual);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType expected = type;
 JavaType actual = type.withValueHandler(handler);
 assertSame(expected, actual);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType expected = type;
 JavaType actual = type.withContentValueHandler(handler);
 assertSame(expected, actual);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType expected = type;
 JavaType actual = type.withStaticTyping();
 assertSame(expected, actual);
 }
}