import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.BaseSerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonValueSerializerTest {

 private ObjectMapper objectMapper;
 private JsonValueSerializer jsonValueSerializer;
 private BeanProperty beanProperty;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 Class<?> clazz = JsonValueSerializer.class;
 AnnotatedMethod accessorMethod = new AnnotatedMethod(clazz, "getValue", JsonValueSerializer.class);
 jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
 beanProperty = new BeanProperty("testProperty", JsonValueSerializer.class, null);
 }

 @Test
 public void testSerializationWithNullValue() throws JsonMappingException, JsonProcessingException {
 Object nullValue = null;
 jsonValueSerializer.serialize(nullValue, objectMapper.getFactory().createGenerator(null), objectMapper.getSerializerProvider());
 assertEquals("null", objectMapper.writeValueAsString(jsonValueSerializer.serializeValues(new Object[] { nullValue })));
 }

 @Test
 public void testSerializationWithStringValue() throws JsonMappingException, JsonProcessingException {
 String stringValue = "test";
 jsonValueSerializer.setValueSerializer(objectMapper.findValueSerializer(String.class, beanProperty));
 Object value = jsonValueSerializer.serializeValues(new Object[] { stringValue });
 assertEquals("\"test\"", objectMapper.writeValueAsString(value[0]));
 }

 @Test
 public void testSerializationWithIntegerValue() throws JsonMappingException, JsonProcessingException {
 Integer integerValue = 1;
 jsonValueSerializer.setValueSerializer(objectMapper.findValueSerializer(Integer.class, beanProperty));
 Object value = jsonValueSerializer.serializeValues(new Object[] { integerValue });
 assertEquals("1", objectMapper.writeValueAsString(value[0]));
 }

 @Test
 public void testSerializationWithBooleanValue() throws JsonMappingException, JsonProcessingException {
 Boolean booleanValue = true;
 jsonValueSerializer.setValueSerializer(objectMapper.findValueSerializer(Boolean.class, beanProperty));
 Object value = jsonValueSerializer.serializeValues(new Object[] { booleanValue });
 assertEquals("true", objectMapper.writeValueAsString(value[0]));
 }

 @Test
 public void testSerializationWithCustomValueType() throws JsonMappingException, JsonProcessingException {
 Class<?> customValueType = new Class<TestClass>() {};
 TestClass testClass = new TestClass();
 jsonValueSerializer.setValueSerializer(objectMapper.findValueSerializer(customValueType, beanProperty));
 Object value = jsonValueSerializer.serializeValues(new Object[] { testClass });
 String jsonString = objectMapper.writeValueAsString(value[0]);
 TestClass deserializedValue = objectMapper.readValue(jsonString, customValueType);
 assertEquals(testClass, deserializedValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithMissingValueMethod() throws JsonMappingException, JsonProcessingException {
 jsonValueSerializer = new JsonValueSerializer(null, null);
 Object[] values = new Object[1];
 jsonValueSerializer.serializeValues(values);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithExceptionInValueMethod() throws JsonMappingException, JsonProcessingException {
 Class<?> exceptionClass = ArithmeticException.class;
 AnnotatedMethod accessorMethod = new AnnotatedMethod(JsonValueSerializer.class, "getValue", JsonValueSerializer.class) {
 @Override public AnnotatedMember getMember() {
 return new AnnotatedMethod.Parameter(JsonValueSerializer.class, "value", exceptionClass);
 }
 };
 jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
 Object[] values = new Object[1];
 values[0] = new ArithmeticException("Test Exception");
 jsonValueSerializer.serializeValues(values);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithNullInput() throws JsonMappingException, JsonProcessingException {
 jsonValueSerializer.setValueSerializer(objectMapper.findValueSerializer(String.class, beanProperty));
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 Type type = typeFactory.constructCollectionType(List.class, String.class);
 SerializerProvider provider = new BaseSerializerProvider.Base();
 jsonValueSerializer = new JsonValueSerializer(beanProperty, jsonValueSerializer, false);
 Object value = jsonValueSerializer.deserialize(null, objectMapper, type, provider);
 assertNotNull(value);
 assertEquals(0, value.getClass().getComponentCount());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithEmptyInput() throws JsonMappingException, JsonProcessingException {
 jsonValueSerializer.setValueSerializer(objectMapper.findValueSerializer(String.class, beanProperty));
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 Type type = typeFactory.constructCollectionType(List.class, String.class);
 SerializerProvider provider = new BaseSerializerProvider.Base();
 jsonValueSerializer = new JsonValueSerializer(beanProperty, jsonValueSerializer, false);
 Object value = jsonValueSerializer.deserialize(new String[0], objectMapper, type, provider);
 assertNotNull(value);
 assertEquals(0, value.getClass().getComponentCount());
 }

 @Test
 public void testDeserializationWithSingleInput() throws JsonMappingException, JsonProcessingException {
 String jsonString = "{\"test\":\"testValue\"}";
 jsonValueSerializer.setValueSerializer(objectMapper.findValueSerializer(String.class, beanProperty));
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 Type type = typeFactory.constructType(List.class, String.class);
 SerializerProvider provider = new BaseSerializerProvider.Base();
 jsonValueSerializer = new JsonValueSerializer(beanProperty, jsonValueSerializer, false);
 Object value = jsonValueSerializer.deserialize(new String[] { jsonString }, objectMapper, type, provider);
 assertEquals(1, value.getClass().getComponentCount());
 assertEquals("testValue", value[0]);
 }

 @Test
 public void testDeserializationWithMultipleInputs() throws JsonMappingException, JsonProcessingException {
 String jsonString1 = "{\"test1\":\"testValue1\"}";
 String jsonString2 = "{\"test2\":\"testValue2\"}";
 jsonValueSerializer.setValueSerializer(objectMapper.findValueSerializer(String.class, beanProperty));
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 Type type = typeFactory.constructCollectionType(List.class, String.class);
 SerializerProvider provider = new BaseSerializerProvider.Base();
 jsonValueSerializer = new JsonValueSerializer(beanProperty, jsonValueSerializer, false);
 Object value = jsonValueSerializer.deserialize(new String[] { jsonString1, jsonString2 }, objectMapper, type, provider);
 assertEquals(2, value.getClass().getComponentCount());
 assertEquals("testValue1", value[0]);
 assertEquals("testValue2", value[1]);
 }
}

class TestClass {
 // Empty class for testing purposes
}