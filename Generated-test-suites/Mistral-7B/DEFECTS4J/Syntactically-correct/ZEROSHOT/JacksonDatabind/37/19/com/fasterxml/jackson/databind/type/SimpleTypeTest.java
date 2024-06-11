import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testConstructUnsafe() {
 SimpleType simpleType = SimpleType.constructUnsafe(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructUnsafeWithArray() {
 SimpleType.constructUnsafe(int[].class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructUnsafeWithMap() {
 SimpleType.constructUnsafe(HashMap.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructUnsafeWithCollection() {
 SimpleType.constructUnsafe(ArrayList.class);
 }

 @Test
 public void testConstruct() {
 SimpleType simpleType = SimpleType.construct(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithArray() {
 SimpleType.construct(int[].class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithMap() {
 SimpleType.construct(HashMap.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithCollection() {
 SimpleType.construct(ArrayList.class);
 }

 @Test
 public void testNarrow() {
 SimpleType simpleType = SimpleType.construct(String.class);
 SimpleType narrowType = simpleType.narrow(StringBuffer.class);
 assertNotNull(narrowType);
 assertEquals(StringBuffer.class, narrowType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowWithWrongSubclass() {
 SimpleType simpleType = SimpleType.construct(String.class);
 simpleType.narrow(ArrayList.class);
 }

 @Test
 public void testIsContainerType() {
 SimpleType simpleType = SimpleType.construct(String.class);
 assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testBuildCanonicalName() {
 SimpleType simpleType = SimpleType.construct(String.class);
 assertEquals("java.lang.String", simpleType.getCanonicalName());

 SimpleType listType = SimpleType.construct(List.class);
 assertEquals("java.util.List", listType.getCanonicalName());

 SimpleType listOfStringType = SimpleType.construct(List.class).narrow(String.class);
 assertEquals("java.util.List<java.lang.String>", listType.getCanonicalName());
 }

 @Test
 public void testGetErasedSignature() {
 SimpleType simpleType = SimpleType.construct(String.class);
 assertEquals("java.lang.String", simpleType.getErasedSignature().toString());
 }

 @Test
 public void testGetGenericSignature() {
 SimpleType simpleType = SimpleType.construct(String.class);
 assertEquals("java.lang.String", simpleType.getGenericSignature().toString());

 SimpleType listType = SimpleType.construct(List.class);
 assertEquals("[Ljava.lang.Object;", listType.getGenericSignature().toString());

 SimpleType listOfStringType = SimpleType.construct(List.class).narrow(String.class);
 assertEquals("java.util.List<java.lang.String>", listType.getGenericSignature().toString());
 }

 @Test
 public void testToString() {
 SimpleType simpleType = SimpleType.construct(String.class);
 assertEquals("[simple type, class java.lang.String]", simpleType.toString());
 }

 @Test
 public void testEquals() {
 SimpleType simpleType1 = SimpleType.construct(String.class);
 SimpleType simpleType2 = SimpleType.construct(String.class);
 assertTrue(simpleType1.equals(simpleType2));

 SimpleType simpleType3 = SimpleType.construct(Integer.class);
 assertFalse(simpleType1.equals(simpleType3));
 }
}