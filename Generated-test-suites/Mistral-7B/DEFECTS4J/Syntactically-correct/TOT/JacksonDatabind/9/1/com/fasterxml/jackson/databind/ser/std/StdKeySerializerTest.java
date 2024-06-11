import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.JsonProcessingException;

public class StdKeySerializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 SerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator jgen = provider.getJsonFactory().createJsonGenerator(new org.apache.commons.io.output.ByteArrayOutputStream(), provider);

 serializer.serialize(date, jgen, provider);

 JsonNode actual = mapper.readTree(jgen.getOutputStream());
 JsonNode expected = mapper.createObjectNode().put("@type", "string").put("$value", mapper.valueToTree(date.toString()));

 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 String value = "testString";
 StdKeySerializer serializer = new StdKeySerializer();
 SerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator jgen = provider.getJsonFactory().createJsonGenerator(new org.apache.commons.io.output.ByteArrayOutputStream(), provider);

 serializer.serialize(value, jgen, provider);

 JsonNode actual = mapper.readTree(jgen.getOutputStream());
 JsonNode expected = mapper.createObjectNode().put("@type", "string").put("$value", value);

 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 SerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator jgen = provider.getJsonFactory().createJsonGenerator(new org.apache.commons.io.output.ByteArrayOutputStream(), provider);

 serializer.serialize(null, jgen, provider);

 JsonNode actual = mapper.readTree(jgen.getOutputStream());
 JsonNode expected = mapper.createNullNode();

 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNonStringOrDate() throws JsonProcessingException {
 Integer value = new Integer(1);
 StdKeySerializer serializer = new StdKeySerializer();
 SerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator jgen = provider.getJsonFactory().createJsonGenerator(new org.apache.commons.io.output.ByteArrayOutputStream(), provider);

 serializer.serialize(value, jgen, provider);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 Type typeHint = TypeFactory.defaultInstance().constructType(String.class);
 JsonNode actual = serializer.getSchema(mapper.getSerializerProvider(), typeHint);
 JsonNode expected = mapper.createObjectNode().put("type", "string");

 assertEquals(expected, actual);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(SerializerProvider.StandardSerializers.FORMAT_STRINGS, null);
 serializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance().constructType(String.class));
 }
}