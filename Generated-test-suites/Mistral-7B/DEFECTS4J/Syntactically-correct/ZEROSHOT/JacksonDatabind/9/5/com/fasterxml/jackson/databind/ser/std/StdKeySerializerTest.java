import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.json.JsonMapperFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.ser.std.TypeSerializers;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdKeySerializerTest {

 private ObjectMapper objectMapper;
 private StdKeySerializer stdKeySerializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper(new JsonMapperFactoryBuilder()
 .builder()
 .findAndRegisterModules()
 .build());
 stdKeySerializer = new StdKeySerializer();
 }

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 SerializerProvider provider = mock(SerializerProvider.class);
 when(provider.defaultSerializeDateKey(Mockito.any(Date.class), Mockito.any(JsonGenerator.class)))
 .thenCallRealMethod();
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);

 stdKeySerializer.serialize(date, jsonGenerator, provider);

 // Verify that the default serialization of date key is called
 Mockito.verify(provider).defaultSerializeDateKey(date, jsonGenerator);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 String key = "testKey";
 SerializerProvider provider = mock(SerializerProvider.class);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);

 stdKeySerializer.serialize(key, jsonGenerator, provider);

 // Verify that the writeFieldName method is called with the provided string
 Mockito.verify(jsonGenerator).writeFieldName(key);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 SerializerProvider provider = mock(SerializerProvider.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 JsonNode expectedSchema = createSchemaNode("string");
 when(provider.findValueSerializer(Mockito.any(Class.class), Mockito.eq(null), Mockito.anyBoolean()))
 .thenReturn(stdKeySerializer);
 when(typeDeserializer.findSchema(Mockito.any(JavaType.class), Mockito.any()))
 .thenReturn(expectedSchema);
 when(typeSerializer.mangleTypeNames(Mockito.any(), Mockito.any()))
 .thenReturn(null);

 JsonNode actualSchema = stdKeySerializer.getSchema(provider, Mockito.any(Type.class));

 // Verify that the returned schema is the expected one
 assertEquals(expectedSchema, actualSchema);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JavaType javaType = JavaType.construct(Map.class, String.class, String.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

 stdKeySerializer.acceptJsonFormatVisitor(visitor, javaType);

 // Verify that the visitor is expected string format is called
 Mockito.verify(visitor).expectStringFormat(javaType);
 }

 private JsonNode createSchemaNode(String typeName) {
 ObjectMapper objectMapper = new ObjectMapper();
 return objectMapper.readTree("{\"type\":\"" + typeName + "\"}");
 }
}