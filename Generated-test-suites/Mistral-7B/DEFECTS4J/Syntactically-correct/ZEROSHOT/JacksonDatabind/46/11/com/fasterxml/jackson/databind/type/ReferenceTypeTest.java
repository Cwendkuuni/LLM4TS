import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ReferenceTypeTest {

 @Test
 public void testReferenceTypeConstruction() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);

 ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
 assertSame(cls, referenceType.getRawClass());
 assertSame(refType, referenceType.getContentType());
 }

 @Test
 public void testReferenceTypeWithTypeHandler() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);
 Object typeHandler = Mockito.mock(Object.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
 assertNotSame(referenceType, newReferenceType);
 assertSame(typeHandler, newReferenceType.getTypeHandler());
 }

 @Test
 public void testReferenceTypeWithContentTypeHandler() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);
 JavaType contentTypeHandler = Mockito.mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
 assertNotSame(referenceType, newReferenceType);
 assertSame(contentTypeHandler, newReferenceType.getContentType());
 }

 @Test
 public void testReferenceTypeWithValueHandler() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);
 Object valueHandler = Mockito.mock(Object.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
 assertNotSame(referenceType, newReferenceType);
 assertSame(valueHandler, newReferenceType.getValueHandler());
 }

 @Test
 public void testReferenceTypeWithContentValueHandler() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);
 Object contentValueHandler = Mockito.mock(Object.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
 assertNotSame(referenceType, newReferenceType);
 assertSame(contentValueHandler, newReferenceType.getContentValueHandler());
 }

 @Test
 public void testReferenceTypeWithStaticTyping() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 ReferenceType newReferenceType = referenceType.withStaticTyping();
 assertNotSame(referenceType, newReferenceType);
 assertTrue(newReferenceType.isStatic());
 }

 @Test
 public void testReferenceTypeGetReferencedType() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 assertSame(refType, referenceType.getReferencedType());
 }

 @Test
 public void testReferenceTypeIsReferenceType() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testReferenceTypeContainedTypeCount() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 assertEquals(1, referenceType.containedTypeCount());
 }

 @Test
 public void testReferenceTypeContainedType() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 JavaType containedType = referenceType.containedType(0);
 assertSame(refType, containedType);
 }

 @Test
 public void testReferenceTypeContainedTypeName() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 String containedTypeName = referenceType.containedTypeName(0);
 assertEquals("T", containedTypeName);
 }

 @Test
 public void testReferenceTypeGetParameterSource() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 assertSame(cls, referenceType.getParameterSource());
 }

 @Test
 public void testReferenceTypeGetErasedSignature() {
 Class<?> cls = Mockito.mock(Class.class);
 ReferenceType referenceType = new ReferenceType(cls, null, null, null, false);
 StringBuilder sb = new StringBuilder();
 referenceType.getErasedSignature(sb);
 assertEquals("[class com.example.TestClass]", sb.toString());
 }

 @Test
 public void testReferenceTypeGetGenericSignature() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);
 StringBuilder sb = new StringBuilder();
 referenceType.getGenericSignature(sb);
 assertEquals("[reference type, class com.example.TestClass<T>]", sb.toString());
 }

 @Test
 public void testReferenceTypeToString() {
 Class<?> cls = Mockito.mock(Class.class);
 JavaType refType = Mockito.mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
 assertEquals("[reference type, class com.example.TestClass<T>]", referenceType.toString());
 }

 @Test
 public void testReferenceTypeEquals() {
 Class<?> cls1 = Mockito.mock(Class.class);
 JavaType refType1 = Mockito.mock(JavaType.class);
 JavaType refType2 = Mockito.mock(JavaType.class);
 Class<?> cls2 = Mockito.mock(Class.class);

 ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);
 ReferenceType referenceType3 = new ReferenceType(cls1, refType1, null, null, false);

 assertTrue(referenceType1.equals(referenceType1));
 assertTrue(referenceType1.equals(referenceType3));
 assertFalse(referenceType1.equals(referenceType2));
 }
}