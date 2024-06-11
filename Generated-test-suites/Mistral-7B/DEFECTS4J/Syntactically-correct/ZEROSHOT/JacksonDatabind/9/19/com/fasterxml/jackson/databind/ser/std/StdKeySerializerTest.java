import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.*;
import java.io.IOException;
import java.util.Date;

public class StdKeySerializerTest {

 private ObjectMapper mapper;
 private StdKeySerializer serializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 serializer = new StdKeySerializer();
 }

 @Test
 public void testSerializeDate() throws IOException {
 Date date = new Date();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = new StandardSerializerProvider();
 provider.setDefaultSerializer(Date.class, new StdDateSerializer());
 serializer.serialize(date, gen, provider);
 gen.flush();
 String json = gen.getOutputContent();
 assertTrue(json.contains("\"$date\":"));
 }

 @Test
 public void testSerializeString() throws IOException {
 String key = "testKey";
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(key, gen, mapper.getSerializerProvider());
 gen.flush();
 String json = gen.getOutputContent();
 assertTrue(json.contains(key));
 }

 @Test
 public void testGetSchema() throws IOException {
 JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), JavaType.valueOf(String.class));
 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws IOException {
 ObjectType objectType = mapper.getTypeFactory().constructType(String.class);
 JsonFormatVisitorWrapper visitor = mapper.sch(objectType);
 serializer.acceptJsonFormatVisitor(visitor, objectType);
 assertTrue(visitor.isExpectedStartVector());
 assertTrue(visitor.isExpectedStartArray());
 assertFalse(visitor.isExpectedStartObject());
 assertFalse(visitor.isExpectedStartValue());
 assertFalse(visitor.isExpectedEndVector());
 assertFalse(visitor.isExpectedEndArray());
 assertFalse(visitor.isExpectedEndObject());
 assertTrue(visitor.isExpectedStringFormat());
 }
}