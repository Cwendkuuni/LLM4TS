import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleValueSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeySerializerTest {

 private SerializerProvider serializerProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 serializerProvider = Mockito.mock(SerializerProvider.class);
 objectMapper = new ObjectMapper();
 objectMapper.setDefaultSerializerProvider(serializerProvider);
 }

 @Test
 public void testSerializeDate() throws IOException {
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);

 serializer.serialize(date, jsonGenerator, serializerProvider);

 Mockito.verify(jsonGenerator).writeFieldName(date.toISO8601String());
 }

 @Test
 public void testSerializeString() throws IOException {
 String key = "test";
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);

 serializer.serialize(key, jsonGenerator, serializerProvider);

 Mockito.verify(jsonGenerator).writeFieldName(key);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonNode schema = serializer.getSchema(serializerProvider, Object.class);

 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = Mockito.mock(JavaType.class);

 serializer.acceptJsonFormatVisitor(visitor, typeHint);

 Mockito.verify(visitor).expectStringFormat(typeHint);
 }

 @Test
 public void testSerializeMapToJsonObject() throws IOException {
 Map<String, Integer> map = new HashMap<>();
 map.put("key1", 1);
 map.put("key2", 2);

 StdKeySerializer serializer = new StdKeySerializer();
 SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("*");
 SimpleValueSerializer valueSerializer = new SimpleValueSerializer();
 TypeSerializer keySerializer = objectMapper.getTypeSerializer(String.class);
 TypeDeserializer keyDeserializer = TypeDeserializerProvider.instance.findValueDeserializer(String.class, null);
 TypeSerializer valueSerializerProvider = TypeSerializerProvider.instance;
 TypeDeserializer valueDeserializerProvider = TypeDeserializerProvider.instance;
 TypeSerializer keyTypeSerializer = TypeSerializerProvider.instance.findValueSerializer(SimpleType.construct(String.class, null), null);
 TypeSerializer valueTypeSerializer = TypeSerializerProvider.instance.findValueSerializer(SimpleType.construct(Integer.class, null), null);
 TypeSerializerProvider typeSerializerProvider = Mockito.mock(TypeSerializerProvider.class);
 SerializerProvider serializerProviderForMap = Mockito.mock(SerializerProvider.class);

 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 JsonParser jsonParser = objectMapper.readTree("{\"key1\":1,\"key2\":2}").getRoot();

 Mockito.when(serializerProviderForMap.getChainableSerials()).thenReturn(typeSerializerProvider);
 Mockito.when(typeSerializerProvider.findValueSerializer(SimpleType.construct(String.class, null), null)).thenReturn(keySerializer);
 Mockito.when(typeSerializerProvider.findValueSerializer(SimpleType.construct(Integer.class, null), null)).thenReturn(valueSerializer);
 Mockito.when(typeSerializerProvider.findValueSerializer(SimpleType.construct(Map.class, null), null)).thenReturn(new MapSerializer(filter, valueSerializerProvider, valueDeserializerProvider));
 Mockito.when(serializerProviderForMap.findValueSerializer(SimpleType.construct(String.class, null), null)).thenReturn(keySerializer);
 Mockito.when(serializerProviderForMap.findValueSerializer(SimpleType.construct(Integer.class, null), null)).thenReturn(valueSerializer);
 Mockito.when(serializerProviderForMap.findValueDeserializer(SimpleType.construct(String.class, null), null)).thenReturn(keyDeserializer);
 Mockito.when(serializerProviderForMap.findValueDeserializer(SimpleType.construct(Integer.class, null), null)).thenReturn(valueDeserializerProvider.findValueDeserializer(SimpleType.construct(Integer.class, null), null));
 Mockito.when(serializerProviderForMap.findValueDeserializer(SimpleType.construct(Map.class, null), null)).thenReturn(null);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator jsonGeneratorMock = Mockito.mock(JsonGenerator.class);
 Mockito.when(jsonGeneratorMock.getOutputFactory()).thenReturn(outputStream);
 Mockito.when(jsonGeneratorMock.getOverrides()).thenReturn(new JsonGenerator.Overrides());
 Mockito.when(jsonParser.nextToken()).thenCallRealMethod();

 serializer.serializeValue(map, jsonGeneratorMock, serializerProviderForMap);

 Mockito.verify(jsonGeneratorMock, Mockito.times(2)).writeFieldName(Mockito.anyString());

 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 JsonParser jsonParserMock = objectMapper.readTree(inputStream);

 assertEquals(map.get("key1"), jsonParserMock.get("key1").asInt());
 assertEquals(map.get("key2"), jsonParserMock.get("key2").asInt());
 }
}

class MapSerializer extends StdSerializer<Map<?,?>> {
 private final SimpleBeanPropertyFilter filter;
 private final TypeSerializer valueSerializer;
 private final TypeDeserializer valueDeserializer;

 public MapSerializer(SimpleBeanPropertyFilter filter, TypeSerializer valueSerializer, TypeDeserializer valueDeserializer) {
 this(filter, valueSerializer, valueDeserializer, null);
 }

 protected MapSerializer(SimpleBeanPropertyFilter filter, TypeSerializer valueSerializer, TypeDeserializer valueDeserializer, Class<?> vc) {
 super(Map.class, null, true);
 this.filter = filter;
 this.valueSerializer = valueSerializer;
 this.valueDeserializer = valueDeserializer;
 }

 @Override
 public void serializeValue(Map<?,?> value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
 for (Map.Entry<?,?> entry : value.entrySet()) {
 SimpleType keyType = SimpleType.construct(entry.getKey().getClass(), null);
 SimpleType valueType = SimpleType.construct(entry.getValue().getClass(), null);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeKey("@class");
 jsonGenerator.writeValue(keyType.toString());
 jsonGenerator.writeFieldName(entry.getKey().toString());
 valueSerializer.serializeValue(entry.getValue(), jsonGenerator, serializerProvider);
 jsonGenerator.writeEndObject();
 }
 }

 @Override
 public Map<?> deserializeValue(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 JsonNode node = jsonParser.readValueAsTree();
 if (node.isObject()) {
 JsonNode clsNode = node.get("@class");
 if (clsNode != null) {
 SimpleType keyType = SimpleType.construct(Class.forName(clsNode.asText()));
 JsonNode keyNode = node.fields();
 Object key = keyDeserializer.deserializeValue(jsonParser, ctxt, keyType);

 JsonNode valueNode = node.elements();
 Object value = valueDeserializer.deserializeValue(jsonParser, ctxt, valueTypeSerializerProvider.findValueSerializer(valueType, null));

 return new HashMap<Object, Object>() {{
 put(key, value);
 }};
 }
 }
 return null;
 }
}