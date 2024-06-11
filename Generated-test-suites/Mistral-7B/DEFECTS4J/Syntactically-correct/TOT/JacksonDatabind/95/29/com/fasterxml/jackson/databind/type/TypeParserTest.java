import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactory();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonical = "int";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals(int.class, javaType.getRawClass());
 }

 @Test
 public void testParseComplexType() {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>";
 JavaType javaType = typeParser.parse(canonical);
 JavaType expectedType = typeFactory.constructType(new TypeReference<List<JsonNode>>() {}.getType());
 assertEquals(expectedType, javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonical = "java.util.List<int";
 typeParser.parse(canonical);
 }

 @Test
 public void testParseInvalidTokenType() {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>asd";
 typeParser.parse(canonical);
 }

 @Test
 public void testParseInvalidEndToken() {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>";
 typeParser.parse(canonical + "x");
 }

 @Test
 public void testParseInvalidClassToken() {
 String canonical = "java.util.List<xyz.InvalidClass>";
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(canonical));
 }

 @Test
 public void testParseInvalidGenericToken() {
 String canonical = "java.util.List<int<";
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(canonical));
 }

 @Test
 public void testParseInvalidBracketToken() {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>(<";
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(canonical));
 }

 @Test
 public void testParseInvalidCommaToken() {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>,com.fasterxml.jackson.databind.JsonNode>";
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(canonical));
 }
}