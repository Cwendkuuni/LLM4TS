import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setUp() {
 typeFactory = new DefaultTypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseNull() {
 try {
 typeParser.parse("null");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseBoolean() {
 JavaType booleanType = typeParser.parse("boolean");
 assertEquals(JavaType.BOOLEAN, booleanType);
 }

 @Test
 public void testParseNumber() {
 JavaType intType = typeParser.parse("int");
 JavaType longType = typeParser.parse("long");
 JavaType doubleType = typeParser.parse("double");
 JavaType floatType = typeParser.parse("float");

 assertEquals(JavaType.INT_TYPE, intType);
 assertEquals(JavaType.LONG_TYPE, longType);
 assertEquals(JavaType.DOUBLE_TYPE, doubleType);
 assertEquals(JavaType.FLOAT_TYPE, floatType);
 }

 @Test
 public void testParseString() {
 JavaType stringType = typeParser.parse("java.lang.String");
 assertEquals(typeFactory.constructType(String.class), stringType);
 }

 @Test
 public void testParseClass() {
 JavaType intArrayType = typeParser.parse("int[]");
 JavaType listType = typeParser.parse("java.util.List<java.lang.String>");
 JavaType mapType = typeParser.parse("java.util.Map<java.lang.String, java.lang.Integer>");

 assertEquals(JavaType.ARRAY_TYPE, intArrayType.getClass());
 assertEquals(JavaType.COLLECTION_TYPE, listType.getClass());
 assertEquals(JavaType.MAP_TYPE, mapType.getClass());

 assertArrayEquals(new Class<?>[] { int[].class }, intArrayType.getRawTypes());
 assertArrayEquals(new Class<?>[] { String.class }, listType.getRawTypes()[0]);
 assertArrayEquals(new Class<?>[] { String.class, Integer.class }, mapType.getRawTypes());
 }

 @Test
 public void testParseGenericTypesWithOneParameter() {
 JavaType listType = typeParser.parse("java.util.List<java.lang.String>");
 JavaType mapType = typeParser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
 JavaType arrayType = typeParser.parse("java.lang.String[]");

 assertEquals(TypeBindings.mapType(String.class, Integer.class), mapType.getTypeBindings());
 assertEquals(TypeBindings.listType(String.class), listType.getTypeBindings());
 assertEquals(TypeBindings.arrayType(String.class), arrayType.getTypeBindings());
 }

 @Test
 public void testParseTypesWithMultipleParameters() {
 JavaType listOfList = typeParser.parse("java.util.List<java.util.List<java.lang.String>>");
 JavaType mapOfMap = typeParser.parse("java.util.Map<java.util.Map<java.lang.String, java.lang.Integer>, java.lang.String>");

 assertEquals(TypeBindings.map(TypeBindings.map(String.class, Integer.class), String.class), mapOfMap.getTypeBindings());
 assertEquals(TypeBindings.list(TypeBindings.list(String.class)), listOfList.getTypeBindings());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedType() {
 typeParser.parse("java.lang.Strin");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingClosingBracket() {
 typeParser.parse("java.util.List<java.lang.String<");
 }
}