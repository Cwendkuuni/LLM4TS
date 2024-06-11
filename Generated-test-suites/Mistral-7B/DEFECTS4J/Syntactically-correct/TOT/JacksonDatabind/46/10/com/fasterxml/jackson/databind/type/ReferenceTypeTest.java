import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 @Test
 public void testReferenceTypeConstruction() {
 ReferenceType referenceType = ReferenceType.construct(String.class, TypeFactory.defaultInstance.constructType(int.class), null, null);
 JavaType referencedType = referenceType.getReferencedType();
 assertTrue(referenceType instanceof ReferenceType);
 assertEquals(String.class, referenceType.getRawClass());
 assertEquals(int.class, referencedType.getRawClass());
 }

 @Test
 public void testReferenceTypeWithTypeHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeFactory.defaultInstance.constructType(int.class), null, null, false);
 JavaType referencedType = referenceType.getReferencedType();
 referenceType = referenceType.withTypeHandler(TypeFactory.defaultInstance.constructValueType(String.class));
 JavaType newReferencedType = referenceType.getReferencedType();
 assertNotSame(referencedType, newReferencedType);
 assertEquals(String.class, newReferencedType.getRawClass());
 }

 @Test
 public void testReferenceTypeWithContentTypeHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeFactory.defaultInstance.constructType(int[].class), null, null, false);
 JavaType referencedType = referenceType.getReferencedType();
 JavaType newReferencedType = referencedType.withTypeHandler(TypeFactory.defaultInstance.constructCollectionType(ArrayList.class, int.class));
 referenceType = referenceType.withContentTypeHandler(newReferencedType);
 JavaType newReferencedContentType = referenceType.getReferencedType();
 assertNotSame(referencedType, newReferencedContentType);
 assertEquals(List.class, newReferencedContentType.getRawClass());
 assertEquals(int.class, newReferencedContentType.getContentType().getRawClass());
 }

 @Test
 public void testReferenceTypeWithValueHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeFactory.defaultInstance.constructType(int.class), null, null, false);
 JavaType referencedType = referenceType.getReferencedType();
 referenceType = referenceType.withValueHandler(TypeFactory.defaultInstance.constructValueHandler(Integer.class));
 JavaType newReferencedType = referenceType.getReferencedType();
 assertNotSame(referencedType, newReferencedType);
 assertEquals(int.class, newReferencedType.getRawClass());
 }

 @Test
 public void testReferenceTypeWithContentValueHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeFactory.defaultInstance.constructType(int[].class), null, null, false);
 JavaType referencedType = referenceType.getReferencedType();
 JavaType newReferencedType = referencedType.withValueHandler(TypeFactory.defaultInstance.constructValueHandler(Integer.class));
 referenceType = referenceType.withContentValueHandler(newReferencedType);
 JavaType newReferencedContentValueType = referenceType.getReferencedType();
 assertNotSame(referencedType, newReferencedContentValueType);
 assertEquals(Integer.class, newReferencedContentValueType.getRawClass());
 }

 @Test
 public void testReferenceTypeWithStaticTyping() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeFactory.defaultInstance.constructType(int.class), null, null, false);
 ReferenceType staticReferencedType = referenceType.withStaticTyping();
 assertTrue(staticReferencedType.isStatic());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidReferenceTypeWithValueHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeFactory.defaultInstance.constructType(int.class), null, null, false);
 referenceType = referenceType.withValueHandler(TypeFactory.defaultInstance.constructValueType(String.class));
 // This should throw an IllegalArgumentException
 }

 @Test(expected = NullPointerException.class)
 public void testNullReferenceType() {
 ReferenceType referenceType = null;
 assertNull(referenceType);
 try {
 referenceType = referenceType.withTypeHandler(TypeFactory.defaultInstance.constructValueType(String.class));
 fail("Should not reach this point");
 } catch (NullPointerException e) {
 // expected
 }
 }
}