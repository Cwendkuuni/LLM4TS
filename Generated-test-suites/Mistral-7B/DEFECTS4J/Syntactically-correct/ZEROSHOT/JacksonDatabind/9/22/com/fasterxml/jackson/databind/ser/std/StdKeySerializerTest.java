import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.Types;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {

 private StdKeySerializer serializer;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 serializer = new StdKeySerializer();
 mockJsonGenerator = Mockito.mock(JsonGenerator.class);
 mockSerializerProvider = Mockito.mock(SerializerProvider.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializeDate() throws IOException, JsonProcessingException {
 // Given
 Date date = new Date();

 // When
 serializer.serialize(date, mockJsonGenerator, mockSerializerProvider);

 // Then
 Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeFieldName(Mockito.anyString());
 Mockito.verify(mockSerializerProvider, Mockito.times(1)).defaultSerializeDateKey(date, mockJsonGenerator);
 }

 @Test
 public void testSerializeString() throws IOException, JsonProcessingException {
 // Given
 String key = "test";

 // When
 serializer.serialize(key, mockJsonGenerator, mockSerializerProvider);

 // Then
 Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeFieldName(Mockito.eq(key));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);

 // When
 JsonNode schema = serializer.getSchema(mockSerializerProvider, Types.uncheckedType(JavaType.class, javaType));

 // Then
 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 // Given
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);

 // When
 serializer.acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), javaType);

 // Then
 // No verification needed, as we don't care about the implementation details of acceptJsonFormatVisitor() method.
 }

 @Test
 public void testSerializeMap() throws IOException, JsonProcessingException {
 // Given
 Map<String, Integer> testMap = new HashMap<>();
 testMap.put("testKey1", 1);
 testMap.put("testKey2", 2);

 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addSerializer(Map.class, new StdMapSerializer(serializer));
 objectMapper.registerModule(module);

 // When
 String json = objectMapper.writeValueAsString(testMap);

 // Then
 JsonNode jsonNode = objectMapper.readTree(json);
 assertEquals(2, jsonNode.size());
 }
}