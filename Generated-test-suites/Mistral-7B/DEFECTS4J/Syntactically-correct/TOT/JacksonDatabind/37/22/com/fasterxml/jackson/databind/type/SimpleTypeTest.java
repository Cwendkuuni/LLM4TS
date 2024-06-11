import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<?> simpleClass = String.class;
 SimpleType simpleType = new SimpleType(simpleClass);
 assertNotNull(simpleType);
 assertEquals(simpleClass, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMapConstruction() {
 Class<?> mapClass = HashMap.class;
 SimpleType simpleType = new SimpleType(mapClass);
 fail("Should not reach this point");
 }

 @Test
 public void testSimpleTypeNarrowing() {
 Class<?> stringClass = String.class;
 SimpleType stringSimpleType = new SimpleType(stringClass);
 Class<?> intClass = Integer.class;
 SimpleType intSimpleType = stringSimpleType._narrow(intClass);
 assertNotNull(intSimpleType);
 assertFalse(intSimpleType.equals(stringSimpleType));
 assertEquals(intClass, intSimpleType.getRawClass());
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object typeHandler = new Object() {};
 SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
 assertNotNull(newSimpleType);
 assertNotSame(simpleType, newSimpleType);
 assertEquals(simpleType.getRawClass(), newSimpleType.getRawClass());
 assertEquals(typeHandler, newSimpleType.getTypeHandler());
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object valueHandler = new Object() {};
 SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
 assertNotNull(newSimpleType);
 assertNotSame(simpleType, newSimpleType);
 assertEquals(simpleType.getRawClass(), newSimpleType.getRawClass());
 assertEquals(valueHandler, newSimpleType.getValueHandler());
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType staticType = simpleType.withStaticTyping();
 assertNotNull(staticType);
 assertTrue(staticType.isStatic());
 assertFalse(simpleType.isStatic());
 }

 @Test
 public void testSimpleTypeToCanonicalName() {
 Class<?> listClass = ArrayList.class;
 SimpleType simpleType = new SimpleType(listClass);
 String canonicalName = simpleType.buildCanonicalName();
 assertEquals("java.util.ArrayList", canonicalName);
 }
}