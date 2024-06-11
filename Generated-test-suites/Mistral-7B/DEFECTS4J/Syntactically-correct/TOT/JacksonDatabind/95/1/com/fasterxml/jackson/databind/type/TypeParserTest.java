import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import static com.fasterxml.jackson.databind.types.TypeUtils.*;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactory();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonical = "java.lang.String";
 JavaType expected = getCollectionType(String[].class, JavaType.constructCollectionType(List.class, String.class));
 JavaType actual = typeParser.parse(canonical);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseGenericType() {
 String canonical = "java.util.List<java.lang.String>";
 JavaType expected = getCollectionType(List.class, JavaType.constructTypeReference(String.class));
 JavaType actual = typeParser.parse(canonical);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInvalidCanonicalType() {
 String canonical = "java.util.List<java.lang.String>asdasdasd";
 try {
 JavaType actual = typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type '(remaining: asdasdasd)': Unexpected token 'asdasdasd', expected ',' or '>'";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testParseInvalidClass() {
 String canonical = "com.nonexistent.package.MyClass";
 try {
 JavaType actual = typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class 'com.nonexistent.package.MyClass'";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }
}