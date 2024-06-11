import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

public class EnumSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @BeforeEach
 public void setup() {
 mapper.registerModule(new StdSerializerModule());
 }

 @Test
 public void testSerializeEnumAsString() throws JsonGenerationException, JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), null);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", serializer.serialize(MyEnum.VALUE_ONE, new JsonGenerator(node), new SerializerProvider()));
 ObjectNode expectedNode = mapper.createObjectNode().put("myEnumKey", MyEnum.VALUE_ONE.name());
 Assertions.assertEquals(expectedNode, node);
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonGenerationException, JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), Boolean.TRUE);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", serializer.serialize(MyEnum.VALUE_ONE, new JsonGenerator(node), new SerializerProvider()));
 ObjectNode expectedNode = mapper.createObjectNode().put("myEnumKey", 0);
 Assertions.assertEquals(expectedNode, node);
 }

 @Test
 public void testSerializeEnumArrayAsString() throws JsonGenerationException, JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), null);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumArrayKey", serializer.createContextual(new SerializerProvider(), null)
 .serializeValue(Arrays.asList(MyEnum.VALUE_ONE, MyEnum.VALUE_THREE), new JsonGenerator(node), new SerializerProvider()));
 ObjectNode expectedNode = mapper.createObjectNode().put("myEnumArrayKey", "[VALUE_ONE, VALUE_THREE]");
 Assertions.assertEquals(expectedNode, node);
 }

 @Test
 public void testSerializeEnumArrayAsIndex() throws JsonGenerationException, JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), Boolean.TRUE);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumArrayKey", serializer.createContextual(new SerializerProvider(), null)
 .serializeValue(Arrays.asList(MyEnum.VALUE_ONE, MyEnum.VALUE_THREE), new JsonGenerator(node), new SerializerProvider()));
 ObjectNode expectedNode = mapper.createObjectNode().put("myEnumArrayKey", "[0, 2]");
 Assertions.assertEquals(expectedNode, node);
 }

 @Test
 public void testSerializeEnumWithCustomSerialization() throws JsonGenerationException, JsonMappingException, IOException {
 MyEnumCustomSerializer customSerializer = new MyEnumCustomSerializer();
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", customSerializer.serialize(MyEnum.VALUE_ONE, new JsonGenerator(node), new SerializerProvider()));
 ObjectNode expectedNode = mapper.createObjectNode().put("myEnumKey", "CUSTOM_VALUE_ONE");
 Assertions.assertEquals(expectedNode, node);
 }

 @Test
 public void testSerializeEnumWithJsonFormatAnnotation() throws JsonGenerationException, JsonMappingException, IOException {
 MyEnumWithJsonFormat annotatedEnum = new MyEnumWithJsonFormat(MyEnumWithJsonFormat.VALUE_ONE);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", new EnumSerializer(new EnumValues(MyEnumWithJsonFormat.class), null)
 .serialize(annotatedEnum, new JsonGenerator(node), new SerializerProvider()));
 ObjectNode expectedNode = mapper.createObjectNode().put("myEnumKey", "STRING_VALUE_ONE");
 Assertions.assertEquals(expectedNode, node);
 }

 @Test
 public void testDeserializeEnumAsString() throws JsonMappingException, IOException {
 EnumSerializer deserializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), null);
 MyEnum valueOne = deserializer.deserialize(mapper.readTree("{\"myEnumKey\":\"VALUE_ONE\"}"), new DeserializationContext(), null);
 Assertions.assertEquals(MyEnum.VALUE_ONE, valueOne);
 }

 @Test
 public void testDeserializeEnumAsIndex() throws JsonMappingException, IOException {
 EnumSerializer deserializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), Boolean.TRUE);
 MyEnum valueOne = deserializer.deserialize(mapper.readTree("{\"myEnumKey\":0}"), new DeserializationContext(), null);
 Assertions.assertEquals(MyEnum.VALUE_ONE, valueOne);
 }

 @Test
 public void testDeserializeEnumArrayAsString() throws JsonMappingException, IOException {
 EnumSerializer deserializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), null);
 MyEnum[] valueArray = deserializer.deserialize(mapper.readTree("{\"myEnumArrayKey\":[\"VALUE_ONE\", \"VALUE_THREE\"]}"), new DeserializationContext(), null);
 Assertions.assertArrayEquals(new MyEnum[]{MyEnum.VALUE_ONE, MyEnum.VALUE_THREE}, valueArray);
 }

 @Test
 public void testDeserializeEnumArrayAsIndex() throws JsonMappingException, IOException {
 EnumSerializer deserializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), Boolean.TRUE);
 MyEnum[] valueArray = deserializer.deserialize(mapper.readTree("{\"myEnumArrayKey\":[0, 2]}"), new DeserializationContext(), null);
 Assertions.assertArrayEquals(new MyEnum[]{MyEnum.VALUE_ONE, MyEnum.VALUE_THREE}, valueArray);
 }

 @Test
 public void testDeserializeEnumWithCustomDeserialization() throws JsonMappingException, IOException {
 MyEnumCustomDeserializer customDeserializer = new MyEnumCustomDeserializer();
 MyEnum valueOne = customDeserializer.deserialize(mapper.readTree("{\"myEnumKey\":\"CUSTOM_VALUE_ONE\"}"), new DeserializationContext(), null);
 Assertions.assertEquals(MyEnum.VALUE_ONE, valueOne);
 }

 @Test
 public void testDeserializeEnumWithJsonFormatAnnotation() throws JsonMappingException, IOException {
 MyEnumWithJsonFormat annotatedEnum = mapper.readValue(mapper.readTree("{\"myEnumKey\":\"STRING_VALUE_ONE\"}")
 , MyEnumWithJsonFormat.class);
 Assertions.assertEquals(MyEnumWithJsonFormat.VALUE_ONE, annotatedEnum);
 }

 @Test
 public void testDeserializeNullEnum() throws JsonMappingException, IOException {
 EnumSerializer deserializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), null);
 ObjectNode node = mapper.createObjectNode();
 node.putNull("myEnumKey");
 MyEnum value = deserializer.deserialize(node, new DeserializationContext(), null);
 Assertions.assertNull(value);
 }

 @Test
 public void testDeserializeUnknownEnumValue() throws JsonMappingException, IOException {
 EnumSerializer deserializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), null);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", "UNKNOWN_VALUE");
 MyEnum value = deserializer.deserialize(node, new DeserializationContext(), null);
 Assertions.assertNull(value);
 }

 @Test
 public void testDeserializeEnumArrayWithNullValue() throws JsonMappingException, IOException {
 EnumSerializer deserializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), null);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumArrayKey", Arrays.asList(MyEnum.VALUE_ONE, null));
 MyEnum[] valueArray = deserializer.deserialize(node, new DeserializationContext(), null);
 Assertions.assertArrayEquals(new MyEnum[]{MyEnum.VALUE_ONE, null}, valueArray);
 }

 @Test
 public void testDeserializeEnumArrayWithUnknownValue() throws JsonMappingException, IOException {
 EnumSerializer deserializer = new EnumSerializer(EnumValues.constructFromClasses(MyEnum.class), null);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumArrayKey", Arrays.asList("UNKNOWN_VALUE", MyEnum.VALUE_THREE));
 MyEnum[] valueArray = deserializer.deserialize(node, new DeserializationContext(), null);
 Assertions.assertArrayEquals(new MyEnum[]{MyEnum.VALUE_THREE}, valueArray);
 }
}

enum MyEnum {
 VALUE_ONE,
 VALUE_TWO,
 VALUE_THREE
}

class MyEnumCustomSerializer extends EnumSerializer {
 @Override
 protected String _valuesSerializedValueFor(Enum<?> enumValue) {
 return "CUSTOM_VALUE_" + enumValue.name();
 }
}

class MyEnumCustomDeserializer extends EnumDeserializer {
 @Override
 protected Enum<?> _parseValue(String value, DeserializationContext ctxt) throws IOException {
 return super._parseValue(value, ctxt).orElse(null);
 }

 @Override
 protected Enum<?> _parseUnknown(String textValue, DeserializationContext ctxt) throws IOException {
 return null;
 }

 @Override
 public Enum<?> deserialize(String textValue, DeserializationContext ctxt) throws IOException {
 if (textValue == null) {
 return null;
 }
 MyEnum[] values = EnumSet.allOf(MyEnum.class);
 for (MyEnum value : values) {
 if (value.name().equals(textValue)) {
 return value;
 }
 }
 throw new JsonMappingException("Could not find enum value for [" + textValue + "]");
 }
}

class MyEnumWithJsonFormat {
 @JsonFormat(shape = Shape.STRING)
 public enum Value {
 VALUE_ONE,
 VALUE_TWO,
 VALUE_THREE,
 @JsonFormat(shape = Shape.NUMBER)
 STRING_VALUE_ONE(1)
 }

 Value value;

 public MyEnumWithJsonFormat(Value value) {
 this.value = value;
 }
}