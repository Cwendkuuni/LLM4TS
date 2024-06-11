import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstructor() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 assertEquals(cls, simpleType.getRawClass());
 assertNotNull(simpleType.getBindings());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 Class<?> cls = String.class;
 SimpleType original = new SimpleType(cls);
 SimpleType copy = new SimpleType(original);
 assertEquals(original, copy);
 }

 @Test
 public void testSimpleTypeWithContentType() {
 JavaType contentType = TypeFactory.defaultInstance().constructType(int[].class);
 SimpleType simpleType = new SimpleType(String.class);
 JavaType result = simpleType.withContentType(contentType);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithMapClass() {
 new SimpleType(Map.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithCollectionClass() {
 new SimpleType(Collection.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithArrayClass() {
 new SimpleType(int[].class);
 }

 @Test
 public void testNarrowMethod() {
 Class<?> subclass = StringBuffer.class;
 SimpleType original = new SimpleType(String.class);
 SimpleType narrowed = original._narrow(subclass);
 assertNotNull(narrowed);
 assertEquals(subclass, narrowed.getRawClass());
 }
}