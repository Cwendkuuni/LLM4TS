import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectMapper mapper;
 private JsonValueSerializer serializer;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 serializer = mapper.findValueSerializer(String.class, null);
 }

 @Test
 public void testSimpleString() throws JsonProcessingException {
 String input = "Hello World";
 String expected = "\"Hello World\"";
 ObjectWriter writer = mapper.writerFor(String.class);
 String output = writer.writeValueAsString(input);
 assertEquals(expected, output);
 }

 @Test
 public void testNullValue() throws JsonProcessingException {
 ObjectWriter writer = mapper.writerFor(String.class);
 String output = writer.writeValueAsString(null);
 assertNull(output);
 }

 @Test
 public void testObjectWithJsonValueAnnotation() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public String getValue() {
 return "Test";
 }
 }
 TestClass testObject = new TestClass();
 String expected = "\"Test\"";
 ObjectWriter writer = mapper.writerFor(TestClass.class);
 String output = writer.writeValueAsString(testObject);
 assertEquals(expected, output);
 }

 @Test(expected = JsonMappingException.class)
 public void testObjectWithNullJsonValueAnnotation() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public String getValue() {
 return null;
 }
 }
 TestClass testObject = new TestClass();
 ObjectWriter writer = mapper.writerFor(TestClass.class);
 writer.writeValueAsString(testObject);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testNaturalTypes() throws JsonProcessingException {
 int input = 42;
 String expectedInt = "42";
 ObjectWriter writerInt = mapper.writerFor(int.class);
 String outputInt = writerInt.writeValueAsString(input);
 assertEquals(expectedInt, outputInt);

 boolean inputBoolean = true;
 String expectedBoolean = "true";
 ObjectWriter writerBoolean = mapper.writerFor(boolean.class);
 String outputBoolean = writerBoolean.writeValueAsString(inputBoolean);
 assertEquals(expectedBoolean, outputBoolean);

 double inputDouble = 3.14;
 String expectedDouble = "3.14";
 ObjectWriter writerDouble = mapper.writerFor(double.class);
 String outputDouble = writerDouble.writeValueAsString(inputDouble);
 assertEquals(expectedDouble, outputDouble);
 }

 @Test
 public void testEnumTypes() throws JsonProcessingException {
 enum TestEnum {
 ONE, TWO, THREE
 }
 ObjectWriter writer = mapper.writerFor(TestEnum.class);
 String output = writer.writeValueAsString(TestEnum.ONE);
 assertEquals("\"ONE\"", output);
 }

 @Test
 public void testCustomSerializer() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public int getValue() {
 return 42;
 }
 }
 ObjectMapper mapperWithCustomSerializer = new ObjectMapper();
 mapperWithCustomSerializer.addSerializer(TestClass.class, new JsonValueSerializer(mapperWithCustomSerializer.findMethodValue("getValue", TestClass.class), null));
 ObjectWriter writer = mapperWithCustomSerializer.writerFor(TestClass.class);
 String output = writer.writeValueAsString(new TestClass());
 String expected = "42";
 assertEquals(expected, output);
 }

 @Test(expected = JsonMappingException.class)
 public void testCustomSerializerWithError() throws JsonProcessingException {
 class TestClass {
 @JsonValue
 public int getValue() {
 throw new RuntimeException();
 }
 }
 ObjectMapper mapperWithCustomSerializer = new ObjectMapper();
 mapperWithCustomSerializer.addSerializer(TestClass.class, new JsonValueSerializer(mapperWithCustomSerializer.findMethodValue("getValue", TestClass.class), null));
 ObjectWriter writer = mapperWithCustomSerializer.writerFor(TestClass.class);
 writer.writeValueAsString(new TestClass());
 fail("Expected JsonMappingException");
 }
}