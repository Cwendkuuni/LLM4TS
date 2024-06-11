import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testReferenceTypeConstruction() {
 ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.getType(int[].class), null, null);
 JavaType referencedType = referenceType.getReferencedType();
 assertTrue(referenceType instanceof ReferenceType);
 assertEquals(String.class, referenceType.getRawClass());
 assertEquals(JavaType.getType(int[].class), referencedType);
 }

 @Test
 public void testReferenceTypeWithTypeHandler() {
 JavaType intTypeHandler = JavaType.construct(int.class);
 ReferenceType referenceType = new ReferenceType(String.class, JavaType.getType(int[].class), null, intTypeHandler, false);
 JavaType newReferencedType = referenceType.withTypeHandler(new TypeReference<Object>() {}.getType());
 assertEquals(JavaType.construct(Object.class), newReferencedType);
 }

 @Test
 public void testReferenceTypeWithContentTypeHandler() {
 JavaType stringTypeHandler = JavaType.construct(String.class);
 JavaType intTypeHandler = JavaType.construct(int.class);
 ReferenceType referenceType = new ReferenceType(String.class, JavaType.getType(int[].class), null, null, false);
 JavaType newReferencedType = referenceType.withContentTypeHandler(stringTypeHandler);
 assertEquals(JavaType.construct(String.class), newReferencedType);
 }

 @Test
 public void testReferenceTypeWithValueHandler() {
 JavaType stringTypeHandler = JavaType.construct(String.class);
 ReferenceType referenceType = new ReferenceType(String.class, JavaType.getType(int[].class), stringTypeHandler, null, false);
 JavaType newReferenceType = referenceType.withValueHandler(intTypeHandler);
 assertEquals(JavaType.construct(int[].class), newReferenceType.getReferencedType());
 }

 @Test
 public void testReferenceTypeWithContentValueHandler() {
 JavaType intValueHandler = JavaType.construct(Integer.class);
 JavaType stringTypeHandler = JavaType.construct(String.class);
 ReferenceType referenceType = new ReferenceType(String.class, JavaType.getType(int[].class), null, null, false);
 JavaType newReferenceType = referenceType.withContentValueHandler(intValueHandler);
 assertEquals(JavaType.construct(Integer.class), newReferenceType.getReferencedType().getValueHandler());
 }

 @Test
 public void testReferenceTypeWithStaticTyping() {
 JavaType intType = JavaType.construct(int.class);
 ReferenceType referenceType = new ReferenceType(String.class, JavaType.getType(int[].class), null, null, false);
 ReferenceType newReferenceType = referenceType.withStaticTyping();
 assertTrue(newReferenceType.isStatic());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReferenceTypeInvalidNarrowing() {
 ReferenceType referenceType = new ReferenceType(String.class, JavaType.getType(int[].class), null, null, false);
 referenceType.narrow(Integer.TYPE);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalStateException.class)
 public void testReferenceTypeInvalidWithValueHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, JavaType.getType(int[].class), null, null, false);
 JavaType intTypeHandler = JavaType.construct(int.class);
 ReferenceType newReferenceType = referenceType.withValueHandler(intTypeHandler);
 newReferenceType.withValueHandler(intTypeHandler);
 fail("IllegalStateException expected");
 }

 @Test(expected = IllegalStateException.class)
 public void testReferenceTypeInvalidWithContentTypeHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, JavaType.getType(int[].class), null, null, false);
 JavaType stringTypeHandler = JavaType.construct(String.class);
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(stringTypeHandler);
 newReferenceType.withContentTypeHandler(stringTypeHandler);
 fail("IllegalStateException expected");
 }
}