import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeDescriptor;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(Parameterized.class)
public class ReferenceTypeTest {

 private JavaType referenceType;
 private JavaType baseType;
 private JavaType refdType;

 public ReferenceTypeTest(JavaType baseType, JavaType refdType) {
 this.baseType = baseType;
 this.refdType = refdType;
 }

 @Parameters
 public static Iterable<Object[]> data() {
 return new IterateOverTypes() {
 @Override
 public Object next() {
 JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
 JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType listIntType = TypeFactory.defaultInstance().constructCollectionType(List.class, int.class);
 JavaType listStringType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
 JavaType mapIntStringType = TypeFactory.defaultInstance().constructType(Map.class, int.class, String.class);
 JavaType mapStringIntType = TypeFactory.defaultInstance().constructType(Map.class, String.class, int.class);
 return new Object[] { intType, listIntType, intType, int.class,
 intType.getComponentType(), intType,
 stringType, listStringType, stringType, String.class,
 stringType.getComponentType(), stringType,
 mapIntStringType, mapStringIntType, int.class, String.class,
 mapStringIntType.getKeyType(), int.class, mapStringIntType.getValueType(), String.class,
 mapStringIntType, Map.class, int.class, String.class };
 }
 }.getTestCases();
 }

 @Test
 public void testConstructReferenceType() {
 this.referenceType = ReferenceType.construct(this.baseType.getRawClass(),
 TypeBindings.emptyBindings(), this.baseType, new JavaType[]{}, this.refdType);
 assertNotNull(this.referenceType);
 assertEquals(this.baseType.getRawClass(), this.referenceType.getRawClass());
 assertEquals(this.refdType, this.referenceType.getContentType());
 }

 @Test
 public void testUpgradeFrom() {
 this.referenceType = ReferenceType.upgradeFrom(this.baseType, this.refdType);
 assertSame(this.referenceType, this.referenceType); // checks hashcode
 assertEquals(this.baseType.getRawClass(), this.referenceType.getRawClass());
 assertEquals(this.refdType, this.referenceType.getContentType());
 }

 @Test
 public void testGetContentType() {
 assertEquals(this.refdType, this.referenceType.getContentType());
 }

 @Test
 public void testIsReferenceType() {
 assertTrue(this.referenceType.isReferenceType());
 }

 @Test
 public void testHasContentType() {
 assertTrue(this.referenceType.hasContentType());
 }

 @Test
 public void testGetErasedSignature() {
 assertEquals(this.baseType.getErasedSignature().toString(),
 this.referenceType.getErasedSignature().toString());
 }

 @Test
 public void testGetGenericSignature() {
 assertEquals(this.baseType.getGenericSignature().toString(),
 this.referenceType.getGenericSignature().toString().substring(0,
 this.baseType.getGenericSignature().toString().lastIndexOf('<')).concat("<?"+this.refdType.getGenericSignature().toString().substring(this.refdType.getGenericSignature().toString().lastIndexOf('<'))+"?>") );
 }

 @Test
 public void testWithContentType() {
 JavaType newContentType = TypeFactory.defaultInstance().constructType(Boolean.class);
 assertEquals(this.referenceType, this.referenceType.withContentType(this.refdType));
 JavaType newReferenceType = this.referenceType.withContentType(newContentType);
 assertNotSame(this.referenceType, newReferenceType);
 assertEquals(this.baseType.getRawClass(), newReferenceType.getRawClass());
 assertEquals(newContentType, newReferenceType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object() {};
 assertEquals(this.referenceType, this.referenceType.withTypeHandler(null));
 JavaType newReferenceType = this.referenceType.withTypeHandler(handler);
 assertNotSame(this.referenceType, newReferenceType);
 assertEquals(this.baseType.getRawClass(), newReferenceType.getRawClass());
 assertEquals(this.refdType, newReferenceType.getContentType());
 assertEquals(handler, newReferenceType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = new Object() {};
 assertEquals(this.referenceType, this.referenceType.withContentTypeHandler(null));
 JavaType newContentType = TypeFactory.defaultInstance().constructType(Boolean.class);
 JavaType newReferenceType = this.referenceType.withContentTypeHandler(handler);
 assertNotSame(this.referenceType, newReferenceType);
 assertEquals(this.baseType.getRawClass(), newReferenceType.getRawClass());
 assertEquals(newContentType, newReferenceType.getContentType());
 assertEquals(handler, newReferenceType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Object handler = new Object() {};
 assertEquals(this.referenceType, this.referenceType.withValueHandler(null));
 JavaType newReferenceType = this.referenceType.withValueHandler(handler);
 assertNotSame(this.referenceType, newReferenceType);
 assertEquals(this.baseType.getRawClass(), newReferenceType.getRawClass());
 assertEquals(this.refdType, newReferenceType.getContentType());
 assertEquals(handler, newReferenceType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Object handler = new Object() {};
 assertEquals(this.referenceType, this.referenceType.withContentValueHandler(null));
 JavaType newRefdType = this.refdType.withValueHandler(handler);
 JavaType newReferenceType = this.referenceType.withContentValueHandler(handler);
 assertNotSame(this.referenceType, newReferenceType);
 assertEquals(this.baseType.getRawClass(), newReferenceType.getRawClass());
 assertEquals(newRefdType, newReferenceType.getContentType());
 assertEquals(handler, newReferenceType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 assertFalse(this.referenceType.isStaticTyping());
 JavaType newReferenceType = this.referenceType.withStaticTyping();
 assertNotSame(this.referenceType, newReferenceType);
 assertEquals(this.baseType.getRawClass(), newReferenceType.getRawClass());
 assertEquals(this.refdType, newReferenceType.getContentType());
 assertTrue(newReferenceType.isStaticTyping());
 }

 @Test
 public void testRefine() {
 JavaType refinedType = this.referenceType.refine(this.baseType.getRawClass(),
 TypeBindings.emptyBindings(), null, new JavaType[0]);
 assertNotSame(this.referenceType, refinedType);
 assertEquals(this.baseType.getRawClass(), refinedType.getRawClass());
 assertEquals(refinedType.getContentType(), this.refdType);
 }

 @Test
 public void testEquals() {
 JavaType newReferenceType = ReferenceType.construct(this.baseType.getRawClass(),
 TypeBindings.emptyBindings(), this.baseType, new JavaType[0], this.refdType);
 assertEquals(this.referenceType, this.referenceType);
 assertEquals(this.referenceType, newReferenceType);
 JavaType otherReferenceType = ReferenceType.construct(this.baseType.getRawClass(),
 TypeBindings.emptyBindings(), this.baseType, new JavaType[0], this.refdType.getClass());
 assertNotEquals(this.referenceType, otherReferenceType);
 JavaType otherReferenceType2 = ReferenceType.construct(this.baseType.getRawClass(),
 TypeBindings.emptyBindings(), this.baseType, new JavaType[0], null);
 assertNotEquals(this.referenceType, otherReferenceType2);
 JavaType otherReferenceType3 = ReferenceType.construct(this.baseType.getRawClass(),
 TypeBindings.emptyBindings(), this.baseType, new JavaType[] {TypeFactory.defaultInstance().constructType(Integer.TYPE)}, this.refdType);
 assertNotEquals(this.referenceType, otherReferenceType3);
 JavaType otherReferenceType4 = ReferenceType.construct(this.baseType.getRawClass(),
 TypeBindings.emptyBindings(), this.baseType, new JavaType[0], this.refdType.getSuperClass());
 assertNotEquals(this.referenceType, otherReferenceType4);
 }

 @Test
 public void testToString() {
 assertEquals("["+this.baseType.getCanonicalName()+"<"+this.refdType+">]", this.referenceType.toString());
 }

 @Test
 public void testGetAnchorType() {
 assertSame(this.referenceType, this.referenceType.getAnchorType());
 }

 @Test
 public void testIsAnchorType() {
 assertTrue(this.referenceType.isAnchorType());
 }

}