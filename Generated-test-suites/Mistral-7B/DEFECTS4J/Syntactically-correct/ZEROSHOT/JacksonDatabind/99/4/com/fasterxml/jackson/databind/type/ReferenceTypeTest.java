import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

 @Test
 public void testConstruct() {
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), refType);
 assertEquals(Object.class, referenceType.getRawClass());
 assertEquals(refType, referenceType.getContentType());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 assertEquals(Integer.class, referenceType.getRawClass());
 assertEquals(String.class, referenceType.getContentType().getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromNullRefType() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType.upgradeFrom(baseType, null);
 }

 @Test
 public void testGetContentType() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 assertEquals(String.class, referenceType.getContentType().getRawClass());
 }

 @Test
 public void testIsReferenceType() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testGetErasedSignature() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 StringBuilder sb = new StringBuilder();
 referenceType.getErasedSignature(sb);
 assertEquals("I", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 StringBuilder sb = new StringBuilder();
 referenceType.getGenericSignature(sb);
 assertEquals("Ljava/lang/Integer;<Ljava/lang/String;>", sb.toString());
 }

 @Test
 public void testGetAnchorType() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 assertNull(referenceType.getAnchorType());
 }

 @Test
 public void testIsAnchorType() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType baseReferenceType = ReferenceType.upgradeFrom(baseType, refType);
 JavaType childType = JavaType.construct(Long.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType childReferenceType = ReferenceType.upgradeFrom(baseReferenceType, childType);
 assertFalse(childReferenceType.isAnchorType());
 baseReferenceType = ReferenceType.upgradeFrom(baseReferenceType, childType);
 assertTrue(baseReferenceType.isAnchorType());
 }

 @Test
 public void testWithContentType() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 JavaType newContentType = JavaType.construct(Double.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType newReferenceType = referenceType.withContentType(newContentType);
 assertEquals(Double.class, newReferenceType.getContentType().getRawClass());
 }

 @Test
 public void testWithContentTypeHandler() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 Object valueHandler = new Object();
 Object typeHandler = new Object();
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 JavaType newContentType = JavaType.construct(Double.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 Object newContentValueHandler = new Object();
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentType)
 .withContentValueHandler(newContentValueHandler);
 assertEquals(Double.class, newReferenceType.getContentType().getRawClass());
 assertEquals(newContentValueHandler, newReferenceType.<Object>getValueHandler());
 }

 @Test
 public void testWithTypeHandler() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 Object typeHandler = new Object();
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
 assertEquals(typeHandler, newReferenceType._typeHandler);
 }

 @Test
 public void testWithValueHandler() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 Object valueHandler = new Object();
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
 assertEquals(valueHandler, newReferenceType._valueHandler);
 }

 @Test
 public void testWithStaticTyping() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 ReferenceType newReferenceType = referenceType.withStaticTyping();
 assertTrue(newReferenceType._asStatic);
 }

 @Test
 public void testRefine() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType childType = JavaType.construct(Long.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 ReferenceType newReferenceType = referenceType.refine(Long.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 assertEquals(Long.class, newReferenceType.getRawClass());
 assertEquals(String.class, newReferenceType.getContentType().getRawClass());
 }

 @Test
 public void testBuildCanonicalName() {
 JavaType baseType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 String canonicalName = referenceType.buildCanonicalName();
 assertEquals("class java.lang.Object<Ljava.lang.String;>", canonicalName);
 }
}