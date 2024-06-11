import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.JavaType.TypeBindings$Std;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 assertNotNull(simpleType);
 assertEquals(cls, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertArrayEquals(new Class[0], simpleType.getSuperTypes());
 assertEquals(TypeBindings.emptyBindings(), simpleType.getTypeBindings());
 }

 @Test
 public void testSimpleTypeConstructionWithBindings() {
 Class<?> cls = String[].class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(cls);
 TypeBindings bindings = new TypeBindings() {{
 addBinding(new TypeBinding() {{
 setBindClass(List.class);
 setBindType(JavaType.construct(List.class, JavaType.construct(String.class)));
 }});
 }};
 SimpleType simpleType = new SimpleType(cls, bindings, null, null);
 assertNotNull(simpleType);
 assertEquals(cls, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertArrayEquals(new Class[0], simpleType.getSuperTypes());
 assertEquals(bindings, simpleType.getTypeBindings());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType original = new SimpleType(String.class);
 SimpleType copy = new SimpleType(original);
 assertEquals(original, copy);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionForMap() {
 Class<?> cls = HashMap.class;
 new SimpleType(cls);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionForCollection() {
 Class<?> cls = ArrayList.class;
 new SimpleType(cls);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionForArray() {
 Class<?> cls = int[].class;
 new SimpleType(cls);
 }

 @Test
 public void testNarrowingSimpleType() {
 SimpleType simpleType = new SimpleType(String.class);
 Class<?> subclass = Integer.class;
 JavaType narrowedType = simpleType.narrow(subclass);
 assertNotNull(narrowedType);
 assertEquals(subclass, narrowedType.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionForInvalidSubclass() {
 SimpleType simpleType = new SimpleType(String.class);
 Class<?> subclass = Object.class;
 simpleType.narrow(subclass);
 }

 @Test
 public void testConstructUnsafeMethod() {
 Class<?> cls = String.class;
 SimpleType simpleType = SimpleType.constructUnsafe(cls);
 assertNotNull(simpleType);
 assertEquals(cls, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertArrayEquals(new Class[0], simpleType.getSuperTypes());
 assertEquals(TypeBindings.emptyBindings(), simpleType.getTypeBindings());
 }

 @Test(expected = DeprecatedException.class)
 public void testDeprecatedConstructMethod() {
 Class<?> cls = String.class;
 SimpleType simpleType = SimpleType.construct(cls);
 fail("Construct method should not be called");
 }
}