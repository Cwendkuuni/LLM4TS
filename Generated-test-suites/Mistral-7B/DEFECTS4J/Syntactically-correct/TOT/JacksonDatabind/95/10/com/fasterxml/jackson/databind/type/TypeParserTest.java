import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactory();
 private TypeParser typeParser;

 @Test
 public void testParseSimpleType() {
 typeParser = new TypeParser(typeFactory);
 JavaType type = typeParser.parse("int");
 assertEquals(Integer.TYPE, type.getRawClass());
 }

 @Test
 public void testParseClassType() {
 typeParser = new TypeParser(typeFactory);
 JavaType type = typeParser.parse("java.lang.String");
 assertEquals(String.class, type.getRawClass());
 }

 @Test
 public void testParseArrayType() {
 typeParser = new TypeParser(typeFactory);
 JavaType type = typeParser.parse("int[]");
 assertEquals(int[].class, type.getRawClass());
 }

 @Test
 public void testParseListType() {
 typeParser = new TypeParser(typeFactory);
 JavaType type = typeParser.parse("java.util.List<java.lang.String>");
 assertEquals(List.class, type.getRawClass());
 Type typeArgument = type.getContentType();
 assertEquals(String.class, typeArgument.getRawClass());
 }

 @Test
 public void testParseNestedListType() {
 typeParser = new TypeParser(typeFactory);
 JavaType type = typeParser.parse("java.util.List<java.util.List<java.lang.String>>");
 assertEquals(List.class, type.getRawClass());
 Type typeArg1 = type.getContentType();
 assertEquals(List.class, typeArg1.getRawClass());
 Type typeArg2 = typeArg1.getContentType();
 assertEquals(String.class, typeArg2.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 typeParser = new TypeParser(typeFactory);
 try {
 typeParser.parse("invalidType");
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingEndToken() {
 typeParser = new TypeParser(typeFactory);
 try {
 typeParser.parse("int<");
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingComma() {
 typeParser = new TypeParser(typeFactory);
 try {
 typeParser.parse("int<Integer,String>");
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }
}