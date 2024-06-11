import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.SimpleType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeWithBindings() {
 Class<?> cls = List.class;
 JavaType listType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>(){}.getType());
 TypeBindings bindings = TypeBindings.emptyBindings();
 bindings.putBinding(String.class);
 SimpleType simpleType = new SimpleType(cls, bindings);
 assertEquals(List.class, simpleType.getRawClass());
 assertEquals(ArrayList.class, simpleType.getSuperClass());
 assertTrue(simpleType.getSuperInterfaces().length == 0);
 }

 @Test
 public void testSimpleTypeWithSuperClass() {
 Class<?> cls = ArrayList.class;
 SimpleType simpleType = new SimpleType(cls, null, JavaType.constructType(Collection.class), null);
 assertEquals(ArrayList.class, simpleType.getRawClass());
 assertEquals(Collection.class, simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType original = new SimpleType(Integer.class);
 SimpleType copy = new SimpleType(original);
 assertEquals(original, copy);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalConstruct() {
 Class<?> cls = Map.class;
 new SimpleType(cls);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithIllegalArgs() {
 Class<?> cls = Map.class;
 SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, true);
 }

 @Test
 public void testNarrow() {
 SimpleType simpleType = new SimpleType(Integer.class);
 SimpleType narrowed = simpleType._narrow(Integer[].class);
 assertNull(narrowed);
 }

 @Test
 public void testNarrowWithSubclass() {
 SimpleType simpleType = new SimpleType(Integer.class);
 SimpleType narrowed = simpleType._narrow(IntegerSubclass.class);
 assertTrue(narrowed instanceof SimpleType);
 assertEquals(IntegerSubclass.class, narrowed.getRawClass());
 }

 @Test
 public void testWithTypeHandler() {
 SimpleType simpleType = new SimpleType(Integer.class);
 Object handler = new Object();
 SimpleType withHandler = simpleType.withTypeHandler(handler);
 assertEquals(handler, withHandler.getTypeHandler());
 }

 @Test
 public void testWithSuperType() {
 SimpleType simpleType = new SimpleType(Integer.class);
 JavaType superType = TypeFactory.defaultInstance().constructType(Collection.class);
 SimpleType withSuperType = simpleType.withSuperClass(superType);
 assertEquals(superType, withSuperType.getSuperClass());
 }

 @Test
 public void testWithSuperInterfaces() {
 SimpleType simpleType = new SimpleType(Integer.class);
 JavaType[] superInterfaces = new JavaType[]{TypeFactory.defaultInstance().constructType(List.class), TypeFactory.defaultInstance().constructType(Map.class)};
 SimpleType withInterfaces = simpleType.withSuperInterfaces(superInterfaces);
 assertArrayEquals(superInterfaces, withInterfaces.getSuperInterfaces());
 }

 @Test
 public void testCanonicalName() {
 Class<?> cls = List.class;
 SimpleType simpleType = new SimpleType(cls);
 String canonicalName = simpleType.buildCanonicalName();
 assertEquals("[simple type, class [Ljava.util.List;]", canonicalName);
 }

 @Test
 public void testToString() {
 Class<?> cls = List.class;
 SimpleType simpleType = new SimpleType(cls);
 assertEquals("[simple type, class java.util.List]", simpleType.toString());
 }

 @Test
 public void testEquals() {
 Class<?> cls = List.class;
 SimpleType simpleType1 = new SimpleType(cls);
 SimpleType simpleType2 = new SimpleType(cls);
 assertTrue(simpleType1.equals(simpleType2));
 }

 @Test
 public void testNotEquals() {
 Class<?> cls1 = List.class;
 Class<?> cls2 = ArrayList.class;
 SimpleType simpleType1 = new SimpleType(cls1);
 SimpleType simpleType2 = new SimpleType(cls2);
 assertFalse(simpleType1.equals(simpleType2));
 }

 @Test
 public void testHashcodes() {
 Class<?> cls = List.class;
 SimpleType simpleType1 = new SimpleType(cls);
 SimpleType simpleType2 = new SimpleType(cls);
 assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
 }

 @Test
 public void testDifferentHashcodes() {
 Class<?> cls1 = List.class;
 Class<?> cls2 = ArrayList.class;
 SimpleType simpleType1 = new SimpleType(cls1);
 SimpleType simpleType2 = new SimpleType(cls2);
 assertNotSame(simpleType1.hashCode(), simpleType2.hashCode());
 }
}