import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 // Test case for constructor with Class argument
 @Test
 public void testSimpleTypeConstructorWithClass() {
 Class<String> stringClass = String.class;
 SimpleType stringType = new SimpleType(stringClass);
 assertEquals("class [Ljava.lang.String;]", stringType.getGenericSignature().toString());
 }

 // Test case for constructor with Class, TypeBindings, JavaType superClass, and JavaType[] superInterfaces
 @Test
 public void testSimpleTypeConstructorWithArguments() {
 Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType javaListInt = TypeFactory.defaultInstance().constructType(listIntClass);
 SimpleType listIntType = new SimpleType(listIntClass, bindings, javaListInt, null);
 assertEquals("java.util.List<java.lang.Integer>", listIntType.getCanonicalName());
 }

 // Test case for copy-constructor
 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType listIntType = new SimpleType(List.class);
 SimpleType listIntTypeCopy = new SimpleType(listIntType);
 assertEquals(listIntType, listIntTypeCopy);
 }

 // Test case for static constructor
 @Test
 public void testSimpleTypeStaticConstructor() {
 Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
 SimpleType listIntType = SimpleType.construct(listIntClass);
 assertEquals("java.util.List<java.lang.Integer>", listIntType.getCanonicalName());
 }

 // Test case for _narrow method
 @Test (expected = IllegalArgumentException.class)
 public void testSimpleTypeNarrowMethodWithInvalidSubclass() {
 SimpleType listIntType = new SimpleType(List.class);
 Class<String> stringClass = String.class;
 listIntType.narrow(stringClass);
 fail("IllegalArgumentException expected but not thrown");
 }

 // Test case for equals method
 @Test
 public void testSimpleTypeEquals() {
 SimpleType listIntType1 = new SimpleType(List.class);
 SimpleType listIntType2 = new SimpleType(List.class);
 SimpleType listIntType3 = new SimpleType(Map.class);
 assertTrue(listIntType1.equals(listIntType1));
 assertTrue(listIntType1.equals(listIntType2));
 assertFalse(listIntType1.equals(listIntType3));
 }
}