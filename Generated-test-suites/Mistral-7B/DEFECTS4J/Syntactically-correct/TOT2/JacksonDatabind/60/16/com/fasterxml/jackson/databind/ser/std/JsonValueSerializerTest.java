import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializingStringValue() throws JsonProcessingException {
 String input = "Test String";
 JsonValueSerializer serializer = objectMapper.findValueSerializer(String.class, null);

 assertEquals("\"Test String\"", serializer.serializeValue("Test String", null, null));
 }

 @Test
 public void testSerializingIntegerValue() throws JsonProcessingException {
 Integer input = 123;
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Integer.class, null);

 assertEquals("123", serializer.serializeValue(input, null, null));
 }

 @Test
 public void testSerializingBooleanValue() throws JsonProcessingException {
 boolean input = true;
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Boolean.class, null);

 assertEquals("true", serializer.serializeValue(input, null, null));
 }

 @Test
 public void testSerializingNullValue() throws JsonProcessingException {
 JsonValueSerializer serializer = objectMapper.findValueSerializer(String.class, null);

 assertNull(serializer.serializeValue(null, null, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializingInvalidTypeValue() throws JsonProcessingException {
 Object input = new Object() {};
 JsonValueSerializer serializer = objectMapper.findValueSerializer(String.class, null);

 serializer.serializeValue(input, null, null);
 }

 @Test
 public void testSerializingEnumValue() throws JsonProcessingException {
 MyEnum input = MyEnum.VALUE_1;
 JsonValueSerializer serializer = objectMapper.findValueSerializer(MyEnum.class, null);

 assertEquals("VALUE_1", serializer.serializeValue(input, null, null));
 }

 @Test
 public void testSerializingBeanPropertyValue() throws JsonProcessingException {
 MyBean input = new MyBean("Test", 123);
 JsonValueSerializer serializer = objectMapper.findValueSerializer(MyBean.class, null);

 assertEquals("{\"name\":\"Test\",\"value\":123}", serializer.serializeValue(input, null, null));
 }

 @Test
 public void testSerializingCustomSerializerValue() throws JsonProcessingException {
 MyCustomClass input = new MyCustomClass("Test");
 JsonValueSerializer serializer = objectMapper.findValueSerializer(MyCustomClass.class, null);

 assertEquals("{\"foo\":\"Test\"}", serializer.serializeValue(input, null, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializingExceptionThrownByAccessorMethod() throws JsonProcessingException {
 MyExceptionThrowingClass input = new MyExceptionThrowingClass();
 JsonValueSerializer serializer = objectMapper.findValueSerializer(MyExceptionThrowingClass.class, null);

 try {
 serializer.serializeValue(input, null, null);
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializingNullAccessorMethod() throws JsonProcessingException {
 MyNullAccessorClass input = new MyNullAccessorClass();
 JsonValueSerializer serializer = objectMapper.findValueSerializer(MyNullAccessorClass.class, null);

 try {
 serializer.serializeValue(input, null, null);
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializingTypeMismatchAccessorMethod() throws JsonProcessingException {
 MyTypeMismatchAccessorClass input = new MyTypeMismatchAccessorClass();
 JsonValueSerializer serializer = objectMapper.findValueSerializer(MyTypeMismatchAccessorClass.class, null);

 try {
 serializer.serializeValue(input, null, null);
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}