import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private EnumValues enumValues;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 enumValues = EnumValues.allValues(String.class);
 objectMapper = new JsonMapper();
 serializer = new EnumSerializer(enumValues, false);
 }

 @Test
 public void testSerializeEnumAsString() throws JsonProcessingException, JsonGenerationException {
 Enum<?> enumConstant = enumValues.findValue("ONE");
 ObjectNode actual = (ObjectNode) serializer.serialize(enumConstant, new JsonGenerator(), new SerializerProvider());
 assertEquals("ONE", actual.get("$value").asText());
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException, JsonGenerationException {
 Enum<?> enumConstant = enumValues.findValue("ONE");
 SerializerProvider serializerProvider = new SerializerProvider();
 serializerProvider.setSerializationConfig(getSerializationConfig());
 serializer = new EnumSerializer(enumValues, true);
 ObjectNode actual = (ObjectNode) serializer.serialize(enumConstant, new JsonGenerator(), serializerProvider);
 assertEquals(0, actual.get("$numberInt").asInt());
 }

 @Test
 public void testSerializeEnumWithToStringAnnotation() throws JsonProcessingException, JsonGenerationException {
 MyEnum enumConstant = MyEnum.THREE;
 ObjectNode actual = (ObjectNode) serializer.serialize(enumConstant, new JsonGenerator(), new SerializerProvider());
 assertEquals("THREE", actual.get("$value").asText());
 }

 @Test
 public void testSerializeEnumWithCustomFormat() throws JsonProcessingException, JsonGenerationException {
 JsonFormat.Value jsonFormat = new JsonFormat.Value();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 SerializerProvider serializerProvider = new SerializerProvider();
 serializerProvider.setSerializationConfig(getSerializationConfig());
 serializerProvider.setActiveValueInclusion(JsonInclude.Include.NON_NULL);
 serializer = new EnumSerializer(null, false).withResolver(new StandardSerializer(String.class) {
 @Override
 public void serialize(String s, JsonGenerator jgen, SerializerProvider sp) throws IOException {
 jgen.writeString("CUSTOM_" + s);
 }
 });
 ObjectNode actual = (ObjectNode) serializer.serialize(MyEnum.ONE, new JsonGenerator(), serializerProvider);
 assertEquals("CUSTOM_ONE", actual.get("$value").asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullEnum() throws JsonProcessingException, JsonGenerationException {
 ObjectNode actual = (ObjectNode) serializer.serialize(null, new JsonGenerator(), new SerializerProvider());
 fail("Expected JsonMappingException but got " + actual);
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 ObjectMapper objectMapper = new JsonMapper();
 JsonNode schema = serializer.getSchema(objectMapper.getSerializationConfig(), Object.class);
 assertEquals("string", schema.get("type").asText());
 }

 private SerializationConfig getSerializationConfig() {
 return new SerializationConfig();
 }

 enum MyEnum {
 ONE,
 TWO,
 THREE
 }

}