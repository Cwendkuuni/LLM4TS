import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CreatorPropertyTest {

 private ObjectNode input;
 private ObjectMapper mapper;
 private CreatorProperty property;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 input = mapper.createObjectNode();
 property = new CreatorProperty(
 PropertyName.constructor("name"), JavaType.valueOf(String.class), null,
 null, null, null, 0, null, PropertyMetadata.empty());
 }

 @Test
 public void testDefaultConstructor() throws JsonProcessingException {
 Object result = mapper.readValue(input, property.getType());
 assertNull(result);
 }

 @Test
 public void testSettingValueViaConstructor() throws JsonProcessingException {
 input.put("name", "test");
 Object result = mapper.readValue(input, property.getType());
 assertEquals("test", result);
 }

 @Test(expected = InvalidDefinitionException.class)
 public void testMissingSetter() throws JsonProcessingException {
 property.markAsIgnorable();
 input.put("name", "test");
 mapper.readValue(input, property.getType());
 }

 @Test
 public void testInjectingValue() throws JsonProcessingException {
 Object injectedValue = new Object();
 input.put("name", mapper.valueToTree("{\"@injectableValueId\":\"" + injectedValue.getClass().getName() + "\", \"value\":\"" + injectedValue.toString() + "\"}"));
 property.setFallbackSetter(new SettableBeanProperty.Std("injectedProperty", JavaType.valueOf(injectedValue.getClass()), null, null, null, null, null));
 Object result = mapper.readValue(input, property.getType());
 assertEquals(injectedValue, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidInjectedValueId() throws JsonProcessingException {
 property.setFallbackSetter(new SettableBeanProperty.Std("injectedProperty", JavaType.valueOf(int.class), null, null, null, null, null));
 input.put("name", mapper.valueToTree("{\"@injectableValueId\":\"" + String.class.getName() + "\", \"value\":\"" + "test" + "\"}"));
 mapper.readValue(input, property.getType());
 }

 @Test
 public void testSetValueViaSetter() throws JsonProcessingException {
 property.markAsIgnorable();
 property.setFallbackSetter(new SettableBeanProperty.Std("injectedProperty", JavaType.valueOf(String.class), null, null, null, null, null));
 input.put("name", "test");
 Object result = mapper.readValue(input, property.getType());
 assertNull(result);
 property.setFallbackSetter(null);
 input.put("injectedProperty", "test");
 result = mapper.readValue(input, property.getType());
 assertEquals("test", result);
 }

 @Test
 public void testValueDeserializer() throws JsonProcessingException {
 JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsString();
 }
 };
 property.setValueDeserializer(deserializer);
 Object result = mapper.readValue(input, property.getType());
 assertEquals("test", result);
 }

 @Test
 public void testNullValueProvider() throws JsonProcessingException {
 NullValueProvider nva = new JacksonAnnotations.NullValueProvider();
 property.setNullProvider(nva);
 Object result = mapper.readValue(input, property.getType());
 assertNull(result);
 }
}