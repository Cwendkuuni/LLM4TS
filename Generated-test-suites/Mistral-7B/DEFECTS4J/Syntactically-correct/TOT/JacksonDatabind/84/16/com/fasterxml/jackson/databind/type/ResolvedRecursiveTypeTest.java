import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 JavaType javaType1 = TypeFactory.defaultInstance().constructType(String[].class);
 ResolvedRecursiveType refType1 = new ResolvedRecursiveType(String[].class, TypeBindings.empty());
 refType1.setReference(javaType1);
 JavaType expected = javaType1;
 JavaType actual = refType1.getSelfReferencedType();
 assertEquals("Set reference correctly", expected, actual);

 JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
 ResolvedRecursiveType refType2 = new ResolvedRecursiveType(Integer.class, TypeBindings.empty());
 refType2.setReference(javaType2);
 JavaType actual2 = refType2.getSelfReferencedType();
 assertNull("Should not be able to set reference twice", actual2);
 }

 @Test
 public void testGetSelfReferencedType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 ResolvedRecursiveType refType = new ResolvedRecursiveType(String.class, TypeBindings.empty());
 assertNull("Get self referenced type before setting it", refType.getSelfReferencedType());
 refType.setReference(javaType);
 JavaType expected = javaType;
 JavaType actual = refType.getSelfReferencedType();
 assertEquals("Get self referenced type correctly", expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalSetReference() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 ResolvedRecursiveType refType = new ResolvedRecursiveType(String.class, TypeBindings.empty());
 refType.setReference(javaType);
 refType.setReference(javaType);
 }

 @Test
 public void testGetGenericSignature() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
 ResolvedRecursiveType refType = new ResolvedRecursiveType(String[].class, TypeBindings.empty());
 refType.setReference(javaType);
 StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
 StringBuilder actual = refType.getGenericSignature(new StringBuilder());
 assertEquals("Get generic signature correctly", expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
 ResolvedRecursiveType refType = new ResolvedRecursiveType(String[].class, TypeBindings.empty());
 refType.setReference(javaType);
 StringBuilder expected = new StringBuilder("[Ljava.lang.Object;");
 StringBuilder actual = refType.getErasedSignature(new StringBuilder());
 assertEquals("Get erased signature correctly", expected.toString(), actual.toString());
 }
}