import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static ObjectMapper mapper;

 static {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testSerializeEnumAsString() throws JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(mapper.getSerializationConfig(), MyEnum.class), false);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", serializer);
 JsonGenerator gen = mapper.getFactory().createGenerator(node, getClass());
 MyEnum value = MyEnum.VALUE_1;
 serializer.serialize(value, gen, mapper.getSerializationConfig());
 gen.flush();
 String serializedValue = node.get("myEnumKey").asText();
 assertEquals("VALUE_1", serializedValue);
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(mapper.getSerializationConfig(), MyEnum.class), true);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", serializer);
 JsonGenerator gen = mapper.getFactory().createGenerator(node, getClass());
 MyEnum value = MyEnum.VALUE_1;
 serializer.serialize(value, gen, mapper.getSerializationConfig());
 gen.flush();
 int serializedValue = node.get("myEnumKey").asInt();
 assertEquals(value.ordinal(), serializedValue);
 }

 @Test
 public void testCircularReference() throws JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(mapper.getSerializationConfig(), MyEnumWithCircularReference.class), false);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", serializer);
 MyEnumWithCircularReference value = MyEnumWithCircularReference.VALUE_1;
 serializer.serialize(value, node.get("myEnumKey"), mapper.getSerializationConfig());
 String serializedValue = node.get("myEnumKey").asText();
 assertEquals("VALUE_1", serializedValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownEnumValue() throws JsonMappingException, IOException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(mapper.getSerializationConfig(), MyEnum.class), false);
 ObjectNode node = mapper.createObjectNode();
 node.put("myEnumKey", serializer);
 MyEnum unknownValue = new MyEnum("UNKNOWN_VALUE");
 serializer.serialize(unknownValue, node.get("myEnumKey"), mapper.getSerializationConfig());
 fail("Unexpected successful serialization of unknown enum value");
 }
}

enum MyEnum {
 VALUE_1,
 VALUE_2
}

enum MyEnumWithCircularReference {
 VALUE_1 {
 @Override
 public EnumSerializer getSerializer(SerializationConfig config) {
 return new EnumSerializer(this, false);
 }
 },
 VALUE_2 {
 @Override
 public EnumSerializer getSerializer(SerializationConfig config) {
 return new EnumSerializer(this, false);
 }
 };

 public EnumSerializer getSerializer(SerializationConfig config) {
 return null;
 }
}