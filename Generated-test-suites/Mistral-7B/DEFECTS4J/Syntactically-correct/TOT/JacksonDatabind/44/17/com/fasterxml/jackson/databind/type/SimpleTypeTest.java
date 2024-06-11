import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<String> stringClass = String.class;
 SimpleType stringSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
 assertEquals("class [Ljava.lang.String;", stringSimpleType.toCanonical());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType stringSimpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
 SimpleType stringSimpleTypeCopy = new SimpleType(stringSimpleType);
 assertEquals(stringSimpleType, stringSimpleTypeCopy);
 }

 @Test
 public void testSimpleTypeSuperClass() {
 Class< Number> numberClass = Number.class;
 SimpleType numberSimpleType = new SimpleType(numberClass, TypeBindings.emptyBindings(), null, null);
 JavaType superClass = numberSimpleType.getSuperClass();
 assertEquals(Object.class, superClass.getRawClass());
 }

 @Test
 public void testSimpleTypeInvalidConstruction() {
 try {
 Class<List<String>> listClass = List.class.getComponentType();
 SimpleType listSimpleType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSimpleTypeWithContentTypeHandler() {
 SimpleType stringSimpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
 SimpleType stringWithContentTypeHandler = stringSimpleType.withContentTypeHandler(new Object() {});
 assertNotEquals(stringSimpleType, stringWithContentTypeHandler);
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 SimpleType stringSimpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
 SimpleType stringWithValueHandler = stringSimpleType.withValueHandler(new Object() {});
 assertNotEquals(stringSimpleType, stringWithValueHandler);
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 SimpleType stringSimpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
 SimpleType stringWithTypeHandler = stringSimpleType.withTypeHandler(new Object() {});
 assertNotEquals(stringSimpleType, stringWithTypeHandler);
 }

 @Test
 public void testSimpleTypeRefine() {
 Class<List<String>> listClass = List.class;
 JavaType listJavaType = TypeFactory.defaultInstance().constructType(listClass);
 SimpleType listSimpleType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
 SimpleType listRefined = listSimpleType.refine(List.class, TypeBindings.emptyBindings(), null, null);
 assertNull(listRefined);
 }
}