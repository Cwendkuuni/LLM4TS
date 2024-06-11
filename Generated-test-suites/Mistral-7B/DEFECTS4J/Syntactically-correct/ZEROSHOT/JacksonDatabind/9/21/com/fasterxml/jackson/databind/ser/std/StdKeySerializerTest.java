import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import java.io.IOException;
import java.util.Date;

public class StdKeySerializerTest {

 private StdKeySerializer serializer;
 private SerializerProvider provider;
 private JsonGenerator gen;

 @Before
 public void setup() {
 serializer = new StdKeySerializer();
 provider = mock(SerializerProvider.class);
 gen = mock(JsonGenerator.class);
 }

 @Test
 public void testSerializeDate() throws JsonProcessingException, IOException {
 Date date = new Date();
 when(provider.getDefaultSerializeDateKey(date, gen)).thenCallRealMethod();
 serializer.serialize(date, gen, provider);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException, IOException {
 String value = "test";
 when(gen.writeFieldName(value)).thenCallRealMethod();
 serializer.serialize(value, gen, provider);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNull() throws JsonProcessingException, IOException {
 serializer.serialize(null, gen, provider);
 }

 @Test
 public void testGetSchema() {
 JsonNode schema = serializer.getSchema(provider, Object.class);
 assert schema.isTextual();
 assert schema.asText().equals("string");
 }

 @Test
 public void testAcceptJsonFormatVisitor() {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = mock(JavaType.class);
 serializer.acceptJsonFormatVisitor(visitor, typeHint);
 }
}