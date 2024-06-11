import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testConstructWithReferenceType() {
 ReferenceType referenceType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType referencedType = referenceType.getReferencedType();
 assertEquals(IntegerType.class, referencedType.getClass());
 }

 @Test
 public void testWithTypeHandler() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType newType = originalType.withTypeHandler(new TypeReference<String>() {});
 assertNotSame(originalType, newType);
 assertEquals(String.class, newType.getReferencedType().getClass());
 }

 @Test
 public void testWithContentTypeHandler() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType newType = originalType.withContentTypeHandler(new TypeReference<String>() {});
 assertNotSame(originalType, newType);
 assertEquals(String.class, newType.getReferencedType().getClass());
 }

 @Test
 public void testWithValueHandler() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType newType = originalType.withValueHandler(new Object() {});
 assertNotSame(originalType, newType);
 assertEquals(String.class, newType.getReferencedType().getClass());
 }

 @Test
 public void testWithContentValueHandler() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType newType = originalType.withContentValueHandler(new Object() {});
 assertNotSame(originalType, newType);
 assertEquals(IntegerType.class, newType.getReferencedType().getClass());
 }

 @Test
 public void testWithStaticTyping() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType newType = originalType.withStaticTyping();
 assertNotSame(originalType, newType);
 assertTrue(newType.isStatic());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowWithInvalidSubclass() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType narrowedType = originalType._narrow(Double.class);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testGetReferencedType() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType referencedType = originalType.getReferencedType();
 assertEquals(IntegerType.class, referencedType.getClass());
 }

 @Test
 public void testIsReferenceType() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 assertTrue(originalType.isReferenceType());
 }

 @Test
 public void testContainedTypeCount() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 assertEquals(1, originalType.containedTypeCount());
 }

 @Test
 public void testContainedType() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType containedType = originalType.containedType(0);
 assertEquals(IntegerType.class, containedType.getClass());
 }

 @Test
 public void testContainedTypeName() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 String containedTypeName = originalType.containedTypeName(0);
 assertEquals("T", containedTypeName);
 }

 @Test
 public void testGetErasedSignature() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 StringBuilder expected = new StringBuilder();
 expected.append("Ljava/lang/String;");
 StringBuilder actual = originalType.getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetGenericSignature() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 StringBuilder expected = new StringBuilder();
 expected.append("Ljava/lang/String;<T>T;");
 StringBuilder actual = originalType.getGenericSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testToString() {
 JavaType originalType = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 String expected = "[reference type, class [Ljava/lang/String;<T>T>]";
 String actual = originalType.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 JavaType originalType1 = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType originalType2 = ReferenceType.construct(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null);
 JavaType sameType = originalType1;
 JavaType differentType = new ReferenceType(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null, false);
 JavaType differentStaticType = new ReferenceType(String.class,
 IntegerType.construct(ObjectMapper.getDefaultInstance().getTypeFactory(), null),
 null, null, true);
 JavaType nullType = null;
 assertTrue(originalType1.equals(originalType1));
 assertTrue(originalType1.equals(originalType2));
 assertFalse(originalType1.equals(sameType));
 assertFalse(originalType1.equals(differentType));
 assertFalse(originalType1.equals(differentStaticType));
 assertFalse(originalType1.equals(nullType));
 }

}