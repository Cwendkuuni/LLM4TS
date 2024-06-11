import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StdDelegatingSerializer Test")
public class StdDelegatingSerializerTest {

 private ObjectMapper mapper;
 private StdDelegatingSerializer<String> stringConverterSerializer;
 private StdDelegatingSerializer<Integer> integerConverterSerializer;

 @BeforeAll
 public static void setupClass() {
 Converter<String, Object> stringConverter = new StringToObjectConverter();
 Converter<Integer, Object> integerConverter = new IntegerToObjectConverter();
 mapper = new ObjectMapper();
 }

 @BeforeEach
 public void setup() throws JsonProcessingException {
 stringConverterSerializer = mapper.writerWithTypeSerializer(String.class)
 .withConverter(stringConverter)
 .findValueSerializer(String.class);
 integerConverterSerializer = mapper.writerWithTypeSerializer(Integer.class)
 .withConverter(integerConverter)
 .findValueSerializer(Integer.class);
 }

 @Test
 @DisplayName("Test serialization of String to Object using StdDelegatingSerializer")
 public void testSerializationOfStringToObject() throws JsonProcessingException {
 String input = "Test string";
 ObjectNode expectedOutput = mapper.createObjectNode();
 expectedOutput.put("value", input);
 ObjectNode output = mapper.writeValueAsTree(input, stringConverterSerializer);
 assertEquals(expectedOutput, output);
 }

 @Test
 @DisplayName("Test serialization of null String to Object using StdDelegatingSerializer")
 public void testSerializationOfNullStringToObject() throws JsonProcessingException {
 ObjectNode output = mapper.writeValueAsTree(null, stringConverterSerializer);
 assertTrue(output.isNull());
 }

 @Test
 @DisplayName("Test serialization of Integer to Object using StdDelegatingSerializer")
 public void testSerializationOfIntegerToObject() throws JsonProcessingException {
 Integer input = 123;
 ObjectNode expectedOutput = mapper.createObjectNode();
 expectedOutput.put("value", input.toString());
 ObjectNode output = mapper.writeValueAsTree(input, integerConverterSerializer);
 assertEquals(expectedOutput, output);
 }

 @Test
 @DisplayName("Test serialization of null Integer to Object using StdDelegatingSerializer")
 public void testSerializationOfNullIntegerToObject() throws JsonProcessingException {
 ObjectNode output = mapper.writeValueAsTree(null, integerConverterSerializer);
 assertTrue(output.isNull());
 }

 @Test
 @DisplayName("Test serialization of custom types using StdDelegatingSerializer")
 public void testSerializationOfCustomTypes() throws JsonProcessingException {
 MyClass myClassInput = new MyClass("Test", 123);
 ObjectNode expectedOutput = mapper.createObjectNode();
 expectedOutput.put("value", myClassInput.toString());
 ObjectNode output = mapper.writeValueAsTree(myClassInput, mapper.writerWithTypeSerializer(MyClass.class)
 .withConverter(new MyClassToObjectConverter())
 .findValueSerializer(MyClass.class));
 assertEquals(expectedOutput, output);
 }

 @Test
 @DisplayName("Test serialization of null custom type using StdDelegatingSerializer")
 public void testSerializationOfNullCustomType() throws JsonProcessingException {
 ObjectNode output = mapper.writeValueAsTree(null, mapper.writerWithTypeSerializer(MyClass.class)
 .withConverter(new MyClassToObjectConverter())
 .findValueSerializer(MyClass.class));
 assertTrue(output.isNull());
 }

 @Test
 @DisplayName("Test serialization of non-supported type using StdDelegatingSerializer")
 public void testSerializationOfNonSupportedType() throws JsonProcessingException {
 assertThrows(JsonMappingException.class, () -> mapper.writeValueAsTree(new BigDecimal(123), integerConverterSerializer));
 }

 @AfterEach
 public void teardown() throws Exception {
 mapper.close();
 }

}

class MyClass {
 private final String name;
 private final Integer id;

 public MyClass(String name, Integer id) {
 this.name = name;
 this.id = id;
 }

 @Override
 public String toString() {
 return "MyClass{" +
 "name='" + name + '\'' +
 ", id=" + id +
 '}';
 }
}

class StringToObjectConverter implements Converter<String, Object> {
 @Override
 public Object convert(String s) {
 return s;
 }
}

class IntegerToObjectConverter implements Converter<Integer, Object> {
 @Override
 public Object convert(Integer intValue) {
 return intValue.toString();
 }
}

class MyClassToObjectConverter implements Converter<MyClass, Object> {
 @Override
 public Object convert(MyClass myClass) {
 return myClass.toString();
 }
}