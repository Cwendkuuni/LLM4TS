import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializeDate() throws JsonGenerationException, JsonMappingException, IOException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 SimpleObjectSerializerProvider provider = new SimpleObjectSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider);

 serializer.serialize(date, jgen, provider);

 JsonNode expected = createSchemaNode("\"@type\":\"string\",\"pattern\":\"^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z$\"");
 assertEquals(expected, jgen.getOutputContent());
 }

 @Test
 public void testSerializeString() throws JsonGenerationException, JsonMappingException, IOException {
 String str = "test";
 StdKeySerializer serializer = new StdKeySerializer();
 SimpleObjectSerializerProvider provider = new SimpleObjectSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider);

 serializer.serialize(str, jgen, provider);

 JsonNode expected = createSchemaNode("\"@type\":\"string\",\"description\":\"A string value.\",\"examples\":[\"test\"]");
 assertEquals(expected, jgen.getOutputContent());
 }

 @Test
 public void testSerializeLocalDateTime() throws JsonGenerationException, JsonMappingException, IOException {
 LocalDateTime localDateTime = LocalDateTime.now();
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writer();
 StdKeySerializer serializer = new StdKeySerializer();
 SimpleObjectSerializerProvider provider = new SimpleObjectSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider);
 writer.withSerializerFor(LocalDateTime.class, new LocalDateTimeSerializer(provider));
 writer.writeValue(jgen, localDateTime);

 JsonNode expected = createSchemaNode("\"@type\":\"string\",\"pattern\":\"^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z$\"");
 assertEquals(expected, jgen.getOutputContent());
 }

 @Test
 public void testSerializeMap() throws JsonGenerationException, JsonMappingException, IOException {
 Map<String, Integer> map = new HashMap<>();
 map.put("key1", 1);
 map.put("key2", 2);
 StdKeySerializer serializer = new StdKeySerializer();
 SimpleObjectSerializerProvider provider = new SimpleObjectSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider);

 objectMapper.writeValue(jgen, map);

 JsonNode expected = createSchemaNode("{\"type\":\"object\",\"properties\":{\"key1\":{\"type\":\"string\",\"description\":\"A string value.\",\"examples\":[\"key1\"]},\"key2\":{\"type\":\"string\",\"description\":\"A string value.\",\"examples\":[\"key2\"]}},\"additionalProperties\":false}");
 assertEquals(expected, jgen.getOutputContent());
 }

 @Test
 public void testNullValue() throws JsonGenerationException, JsonMappingException, IOException {
 StdKeySerializer serializer = new StdKeySerializer();
 SimpleObjectSerializerProvider provider = new SimpleObjectSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider);

 serializer.serialize(null, jgen, provider);

 JsonNode expected = createSchemaNode("null");
 assertEquals(expected, jgen.getOutputContent());
 }

 @Test
 public void testInvalidType() throws JsonGenerationException, JsonMappingException, IOException {
 Object invalidType = new Object();
 StdKeySerializer serializer = new StdKeySerializer();
 SimpleObjectSerializerProvider provider = new SimpleObjectSerializerProvider();
 JsonGenerator jgen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider);

 try {
 serializer.serialize(invalidType, jgen, provider);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 private static JsonNode createSchemaNode(String jsonString) throws JsonMappingException {
 return objectMapper.readTree(jsonString);
 }

 private static class SimpleObjectSerializerProvider extends SerializerProvider {
 @Override public void serializeValue(SerializerProvider p, JsonGenerator g, Object value, Type serializableType) throws IOException {
 g.writeNull();
 }
 }
}