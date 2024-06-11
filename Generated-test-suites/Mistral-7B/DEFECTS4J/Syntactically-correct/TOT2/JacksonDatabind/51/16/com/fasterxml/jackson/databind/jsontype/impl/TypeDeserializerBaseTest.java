import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 typeDeserializer = new TypeDeserializerBase(
 ObjectType.Class,
 new SimpleTypeIdResolver(),
 "",
 true,
 String.class) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }
 };
 }

 @Test
 public void testDefaultDeserializer() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("someValue", "defaultValue");
 JsonParser parser = objectMapper.treeToValues(input, String[].class)[0];
 assertEquals("defaultValue", typeDeserializer.deserialize(parser, objectMapper.getContext(), null));
 }

 @Test
 public void testTypeIdDeserializer() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("typeId", "StringDeserializer");
 input.put("value", "testString");
 JsonParser parser = objectMapper.treeToValues(input, String[].class)[0];
 TypeDeserializer stringDeserializer = objectMapper.findValueDeserializer(String.class, objectMapper.getContext());
 JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(objectMapper.getContext(), "StringDeserializer");
 assertEquals("testString", deserializer.deserialize(parser, objectMapper.getContext(), null));
 }

 @Test(expected = IOException.class)
 public void testUnknownTypeId() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("typeId", "UnknownDeserializer");
 JsonParser parser = objectMapper.treeToValues(input, String[].class)[0];
 typeDeserializer._handleUnknownTypeId(objectMapper.getContext(), "UnknownDeserializer", objectMapper.getTypeIdResolver(), typeDeserializer._baseType);
 }

 @Test
 public void testNullTypeId() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 JsonParser parser = objectMapper.treeToValues(input, String[].class)[0];
 assertNull(typeDeserializer._findDeserializer(objectMapper.getContext(), null));
 }
}