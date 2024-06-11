import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {

 // Testing serialize method with Date object
 @Test
 public void testSerializeDate() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jgen = mapper.getFactory().createGenerator();

 serializer.serialize(date, jgen, mapper.getSerializationConfig().getDefaultSerializerProvider());

 JsonNode actual = jgen.getOutputContent();
 JsonNode expected = mapper.readTree("{\"@type\":\"Date\"}");

 assertEquals(expected, actual);
 }

 // Testing serialize method with String object
 @Test
 public void testSerializeString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 String str = "test_string";
 JsonGenerator jgen = mapper.getFactory().createGenerator();

 serializer.serialize(str, jgen, mapper.getSerializationConfig().getDefaultSerializerProvider());

 JsonNode actual = jgen.getOutputContent();
 JsonNode expected = mapper.readTree("{\"@type\":\"string\",\"c\":\"" + str + "\"}");

 assertEquals(expected, actual);
 }

 // Testing getSchema method
 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();

 JsonNode actual = serializer.getSchema(mapper.getSerializationConfig().getDefaultSerializerProvider(), Object.class);
 JsonNode expected = mapper.readTree("{\"type\":\"string\"}");

 assertEquals(expected.path("type"), actual.path("@type"));
 }

 // Testing acceptJsonFormatVisitor method
 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType javaType = mapper.getTypeFactory().constructType("java.lang.String");

 JsonFormatVisitorWrapper visitor = mapper.schmIntrospector().findSchemaVisitor(JavaType.constructType(JsonNode.class, javaType), null);

 serializer.acceptJsonFormatVisitor(visitor, javaType);

 assertEquals("string", visitor.asStringFormat().getShape().getFormat().getShape().asText());
 }

 // Testing serialize method with null value
 @Test(expected = JsonProcessingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jgen = mapper.getFactory().createGenerator();

 serializer.serialize(null, jgen, mapper.getSerializationConfig().getDefaultSerializerProvider());
 }

 // Testing serialize method with non-serializable object
 @Test(expected = JsonProcessingException.class)
 public void testSerializeNonSerializable() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 Object nonSerializableObj = new Object() {
 };
 JsonGenerator jgen = mapper.getFactory().createGenerator();

 serializer.serialize(nonSerializableObj, jgen, mapper.getSerializationConfig().getDefaultSerializerProvider());
 }
}