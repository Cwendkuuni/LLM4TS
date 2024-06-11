import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core. JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NumberSerializersTest {

 // Test for addAll method
 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.LinkedHashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(7, allDeserializers.size());
 }

 // Test for IntegerSerializer
 @Test
 public void testIntegerSerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.getParser("25");
 Integer value = parser.readValueAs(Integer.class);
 JsonSerializer<Integer> serializer = NumberSerializers.findValueSerializer(factory, Integer.class);
 JsonGenerator gen = factory.createGenerator(System.out);
 serializer.serialize(value, gen, null);
 assertEquals("25", gen.getOutputContent().toString());
 }

 // Test for IntegerSerializer with negative number
 @Test
 public void testIntegerSerializerNegative() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.getParser("-25");
 Integer value = parser.readValueAs(Integer.class);
 JsonSerializer<Integer> serializer = NumberSerializers.findValueSerializer(factory, Integer.class);
 JsonGenerator gen = factory.createGenerator(System.out);
 serializer.serialize(value, gen, null);
 assertEquals("-25", gen.getOutputContent().toString());
 }

 // Test for IntegerSerializer with max value
 @Test
 public void testIntegerSerializerMaxValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.getParser("2147483647");
 Integer value = parser.readValueAs(Integer.class);
 JsonSerializer<Integer> serializer = NumberSerializers.findValueSerializer(factory, Integer.class);
 JsonGenerator gen = factory.createGenerator(System.out);
 serializer.serialize(value, gen, null);
 assertEquals("2147483647", gen.getOutputContent().toString());
 }

 // Test for IntegerSerializer with min value
 @Test
 public void testIntegerSerializerMinValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.getParser("-2147483648");
 Integer value = parser.readValueAs(Integer.class);
 JsonSerializer<Integer> serializer = NumberSerializers.findValueSerializer(factory, Integer.class);
 JsonGenerator gen = factory.createGenerator(System.out);
 serializer.serialize(value, gen, null);
 assertEquals("-2147483648", gen.getOutputContent().toString());
 }

 // Test for IntegerSerializer with null
 @Test
 public void testIntegerSerializerNull() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.getParser("null");
 Integer value = parser.readValueAs(Integer.class);
 assertNull(value);
 }

 // Test for IntegerSerializer with String that can't be parsed as integer
 @Test(expected = IOException.class)
 public void testIntegerSerializerInvalidString() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.getParser("abc");
 Integer value = parser.readValueAs(Integer.class);
 }

 // ... other test cases for other serializers and methods in the class can be added here

}