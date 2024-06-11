import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TreeTraversingParserTest {

 private TreeTraversingParser parser;
 private JsonNode input;

 @BeforeEach
 public void setup() throws JsonProcessingException {
 input = JsonNodeFactory.instance.textNode("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
 parser = new TreeTraversingParser(input);
 }

 @Test
 public void testNextTokenIsStartObject() throws IOException {
 JsonToken actual = parser.nextToken();
 JsonToken expected = JsonToken.START_OBJECT;
 assertEquals(expected, actual);
 }

 @Test
 public void testNextTokenIsEndObject() throws IOException {
 parser.nextToken(); // consume START_OBJECT
 JsonToken actual = parser.nextToken();
 JsonToken expected = JsonToken.END_OBJECT;
 assertEquals(expected, actual);
 }

 @Test
 public void testNextTokenIsStartArray() throws IOException {
 input = JsonNodeFactory.instance.arrayNode(1, 2, 3);
 parser = new TreeTraversingParser(input);
 JsonToken actual = parser.nextToken();
 JsonToken expected = JsonToken.START_ARRAY;
 assertEquals(expected, actual);
 }

 @Test
 public void testNextTokenIsEndArray() throws IOException {
 parser.nextToken(); // consume START_ARRAY
 JsonToken actual = parser.nextToken();
 JsonToken expected = JsonToken.END_ARRAY;
 assertEquals(expected, actual);
 }

 @Test
 public void testNextTokenIsValueString() throws IOException {
 JsonToken actual = parser.nextToken();
 JsonToken expected = JsonToken.VALUE_STRING;
 assertEquals(expected, actual);
 }

 @Test
 public void testNextTokenIsValueNumberInt() throws IOException {
 JsonToken actual = parser.nextToken();
 JsonToken expected = JsonToken.VALUE_NUMBER_INT;
 assertEquals(expected, actual);
 }

 @Test
 public void testNextTokenIsValueNumberFloat() throws IOException {
 JsonToken actual = parser.nextToken();
 JsonToken expected = JsonToken.VALUE_NUMBER_FLOAT;
 assertEquals(expected, actual);
 }

 @Test
 public void testNextTokenIsFieldName() throws IOException {
 JsonToken actual = parser.nextToken();
 JsonToken expected = JsonToken.FIELD_NAME;
 assertEquals(expected, actual);
 }

 @Test
 public void testNextValueIsString() throws IOException {
 String actual = parser.getText();
 String expected = "John";
 assertEquals(expected, actual);
 }

 @Test
 public void testNextValueIsNumberInt() throws IOException {
 int actual = parser.getIntValue();
 int expected = 30;
 assertEquals(expected, actual);
 }

 @Test
 public void testNextValueIsNumberFloat() throws IOException {
 float actual = parser.getFloatValue();
 float expected = 30.0F;
 assertEquals(expected, actual, 0.001F);
 }

 @Test
 public void testSkipChildren() throws IOException {
 JsonToken actual = parser.nextToken();
 parser.skipChildren();
 JsonToken expected = JsonToken.END_OBJECT;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsClosed() throws IOException {
 parser.close();
 boolean actual = parser.isClosed();
 assertTrue(actual);
 }

 @Test
 public void testGetTextIsEmpty() throws IOException {
 parser.close();
 String actual = parser.getText();
 assertEquals("", actual);
 }

 @Test
 public void testGetTextIsNull() throws IOException {
 JsonToken nullToken = JsonToken.NULL;
 parser.nextToken();
 parser.nextToken(); // consume START_OBJECT
 parser.nextToken(); // consume "name" field name
 parser.nextToken(); // consume null value
 String actual = parser.getText();
 assertEquals(null, actual);
 }

 @Test
 public void testGetTextIsString() throws IOException {
 JsonToken stringToken = JsonToken.VALUE_STRING;
 parser.nextToken();
 parser.nextToken(); // consume "name" field name
 parser.nextToken(); // consume "John" value
 String actual = parser.getText();
 assertEquals("John", actual);
 }

 @Test
 public void testGetTextIsNumberInt() throws IOException {
 JsonToken numberTokenInt = JsonToken.VALUE_NUMBER_INT;
 parser.nextToken();
 parser.nextToken(); // consume "age" field name
 parser.nextToken(); // consume 30 value
 String actual = parser.getText();
 assertEquals("30", actual);
 }

 @Test
 public void testGetTextIsNumberFloat() throws IOException {
 JsonToken numberTokenFloat = JsonToken.VALUE_Number_FLOAT;
 parser.nextToken();
 parser.nextToken(); // consume "age" field name
 parser.nextToken(); // consume 30.0 value
 String actual = parser.getText();
 assertEquals("30.0", actual);
 }

 @Test
 public void testGetTextIsBooleanTrue() throws IOException {
 JsonToken booleanTokenTrue = JsonToken.VALUE_boolean;
 parser.nextToken();
 parser.nextToken(); // consume "isStudent" field name
 parser.nextToken(); // consume true value
 String actual = parser.getText();
 assertEquals("true", actual);
 }

 @Test
 public void testGetTextIsBooleanFalse() throws IOException {
 JsonToken booleanTokenFalse = JsonToken.VALUE_boolean;
 parser.nextToken();
 parser.nextToken(); // consume "isStudent" field name
 parser.nextToken(); // consume false value
 String actual = parser.getText();
 assertEquals("false", actual);
 }

 @Test
 public void testGetTextIsArray() throws IOException {
 JsonToken arrayToken = JsonToken.START_ARRAY;
 parser.nextToken();
 String actual = parser.getText();
 assertEquals("[]", actual);
 parser.close();
 }

 @Test
 public void testGetTextIsObject() throws IOException {
 JsonToken objectToken = JsonToken.START_OBJECT;
 parser.nextToken();
 String actual = parser.getText();
 assertEquals("{}", actual);
 parser.close();
 }

 @Test
 public void testGetTextIsNullForClosedParser() throws IOException {
 parser.close();
 String actual = parser.getText();
 assertEquals(null, actual);
 }

 @Test
 public void testGetTextThrowsIOExceptionForNullParser() throws IOException {
 TreeTraversingParser closedParser = null;
 assertThrows(IOException.class, () -> closedParser.getText());
 }

 @Test
 public void testNextValueIsEmbeddedObject() throws IOException {
 Object actual = parser.getEmbeddedObject();
 JsonNode expected = JsonNodeFactory.instance.objectNode()
 .put("name", JsonNodeFactory.instance.textNode("John Doe"))
 .put("age", JsonNodeFactory.instance.numberNode(30));
 assertTrue(actual.equals(expected));
 }
}