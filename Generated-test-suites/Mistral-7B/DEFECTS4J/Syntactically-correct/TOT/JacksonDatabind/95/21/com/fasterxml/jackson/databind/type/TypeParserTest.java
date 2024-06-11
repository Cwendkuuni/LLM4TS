import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

 private TypeFactory typeFactory = new DefaultTypeFactory();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonicalType = "java.lang.String";
 JavaType actualType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructType(new TypeReference<String>() {}.getType());
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 }

 @Test
 public void testParseClassWithGenerics() {
 String canonicalType = "java.util.ArrayList<java.lang.String>";
 JavaType actualType = typeParser.parse(canonicalType);
 TypeReference<List<String>> expectedTypeRef = new TypeReference<List<String>>() {};
 JavaType expectedType = typeFactory.constructType(expectedTypeRef.getType());
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 assertEquals(expectedTypeRef.getType().getComponentType(), actualType.getGenerics()[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedToken() {
 String canonicalType = "java.lang.String<";
 MyTokenizer tokens = new MyTokenizer(canonicalType);
 try {
 typeParser.parseType(tokens);
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type 'java.lang.String<(remaining: )', unexpected token '<', expected ',' or '>', problem: Unexpected '<' token", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedEndOfString() {
 String canonicalType = "java.lang.String";
 MyTokenizer tokens = new MyTokenizer(canonicalType);
 try {
 typeParser.parseType(tokens);
 tokens.nextToken();
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type 'java.lang.String' (remaining: ), expected end-of-string:", e.getMessage());
 }
 }

 @Test
 public void testFindClass() {
 String className = "java.util.ArrayList";
 JavaType actualType = typeParser.parse(className);
 JavaType expectedType = typeFactory.constructType(new TypeReference<ArrayList<Object>>() {}.getType());
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCannotFindClass() {
 String className = "com.faker.package.NonExistentClass";
 MyTokenizer tokens = new MyTokenizer(className);
 try {
 typeParser.findClass(className, tokens);
 } catch (IllegalArgumentException e) {
 assertEquals("Can not locate class 'com.faker.package.NonExistentClass', problem: Class not found", e.getMessage());
 }
 }
}