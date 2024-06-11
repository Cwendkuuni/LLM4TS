import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 StdKeySerializer serializer = new StdKeySerializer();
 Date date = new Date();

 serializer.serialize(date, gen, null);
 gen.flush();

 JsonParser parser = mapper.getFactory().createParser("\"2022-12-31T12:34:56Z\""); // ISO 8601 date string
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 StdKeySerializer serializer = new StdKeySerializer();
 String str = "test";

 serializer.serialize(str, gen, null);
 gen.flush();

 JsonParser parser = mapper.getFactory().createParser("{\"test\":1}"); // JSON Object with string key
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 StdKeySerializer serializer = new StdKeySerializer();
 Object value = null;

 serializer.serialize(value, gen, null);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType typeHint = mapper.getTypeFactory().constructType(String[].class);

 JsonNode schema = serializer.getSchema(mapper.getSerializationConfig(), typeHint);
 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JavaType typeHint = mapper.getTypeFactory().constructType(String[].class);

 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(typeHint, null);
 serializer.acceptJsonFormatVisitor(visitor, typeHint);
 }
}