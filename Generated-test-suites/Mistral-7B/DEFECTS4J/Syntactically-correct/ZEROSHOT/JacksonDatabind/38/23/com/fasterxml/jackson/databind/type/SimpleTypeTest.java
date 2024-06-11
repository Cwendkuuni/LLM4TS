import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 SimpleType simpleType = new SimpleType(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test
 public void testConstructUnsafe() {
 SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
 assertNotNull(simpleType);
 assertEquals(Integer.class, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructInvalidClass() {
 SimpleType simpleType = null;
 try {
 simpleType = SimpleType.construct(Map.class);
 } catch (Exception e) {
 simpleType = (SimpleType) e.getCause();
 }
 assertNull(simpleType);
 }

 @Test
 public void testNarrow() {
 SimpleType intType = new SimpleType(Integer.class);
 SimpleType longType = new SimpleType(Long.class);
 SimpleType intLongType = intType.narrow(Long.class);
 assertNotNull(intLongType);
 assertEquals(Long.class, intLongType.getRawClass());
 }

 @Test
 public void testWithStaticTyping() {
 SimpleType intType = new SimpleType(Integer.class);
 SimpleType intStaticType = intType.withStaticTyping();
 assertNotNull(intStaticType);
 assertTrue(intStaticType._asStatic);
 }

 @Test
 public void testContainsType() {
 SimpleType simpleType = new SimpleType(String[].class);
 JavaType containedType = simpleType.containedType(0);
 assertNotNull(containedType);
 assertEquals(String.class, containedType.getRawClass());
 }

 @Test
 public void testGetErasedSignature() {
 SimpleType stringListType = new SimpleType(String[].class);
 StringBuilder sb = new StringBuilder();
 stringListType.getErasedSignature(sb);
 assertEquals("[Ljava.lang.String;", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 SimpleType listType = new SimpleType(List.class);
 StringBuilder sb = new StringBuilder();
 listType.getGenericSignature(sb);
 assertEquals("[Ljava.util.List;", sb.toString());
 }

 @Test
 public void testBuildCanonicalName() {
 SimpleType listType = new SimpleType(List.class);
 assertEquals("java.util.List", listType.buildCanonicalName());
 SimpleType stringListType = new SimpleType(String[].class);
 assertEquals("[Ljava.lang.String;", stringListType.buildCanonicalName());
 }

 @Test
 public void testIsContainerType() {
 SimpleType simpleType = new SimpleType(String.class);
 assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testToString() {
 SimpleType simpleType = new SimpleType(String.class);
 assertEquals("[simple type, class [Ljava.lang.String;]", simpleType.toString());
 }

 @Test
 public void testEquals() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(String.class);
 assertTrue(simpleType1.equals(simpleType2));
 SimpleType simpleType3 = new SimpleType(Integer.class);
 assertFalse(simpleType1.equals(simpleType3));
 }
}