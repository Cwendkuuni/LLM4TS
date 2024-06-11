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
 public void testEmptyJsonValueAnnotation() throws JsonProcessingException {
 Object emptyBean = new Object();
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Object.class, null);
 String json = serializer.serializeValueAsString(emptyBean, null, null);
 assertEquals("{}", json);
 }

 @Test
 public void testJsonValueAnnotationWithNullValue() throws JsonProcessingException {
 Object beanWithNullValue = new Object() {
 @JsonValue
 public String getValue() {
 return null;
 }
 };
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Object.class, null);
 String json = serializer.serializeValueAsString(beanWithNullValue, null, null);
 assertEquals("null", json);
 }

 @Test
 public void testJsonValueAnnotationWithStringValue() throws JsonProcessingException {
 Object beanWithStringValue = new Object() {
 @JsonValue
 public String getValue() {
 return "hello";
 }
 };
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Object.class, null);
 String json = serializer.serializeValueAsString(beanWithStringValue, null, null);
 assertEquals("\"hello\"", json);
 }

 @Test
 public void testJsonValueAnnotationWithIntegerValue() throws JsonProcessingException {
 Object beanWithIntegerValue = new Object() {
 @JsonValue
 public int getValue() {
 return 42;
 }
 };
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Object.class, null);
 String json = serializer.serializeValueAsString(beanWithIntegerValue, null, null);
 assertEquals("42", json);
 }

 @Test
 public void testJsonValueAnnotationWithBooleanValue() throws JsonProcessingException {
 Object beanWithBooleanValue = new Object() {
 @JsonValue
 public boolean isValue() {
 return true;
 }
 };
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Object.class, null);
 String json = serializer.serializeValueAsString(beanWithBooleanValue, null, null);
 assertEquals("true", json);
 }

 @Test
 public void testJsonValueAnnotationWithCustomSerializer() throws JsonProcessingException {
 Object beanWithCustomSerializer = new Object() {
 @JsonValue
 public String getValue() {
 return new MyCustomClass();
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addSerializer(MyCustomClass.class, new MyCustomSerializer());
 mapper.registerModule(module);
 JsonValueSerializer serializer = mapper.findValueSerializer(Object.class, null);
 String json = serializer.serializeValueAsString(beanWithCustomSerializer, null, null);
 assertEquals("{\"class\":\"com.example.MyCustomClass\"}", json);
 }

 @Test(expected = JsonProcessingException.class)
 public void testJsonValueAnnotationWithExceptionThrowingMethod() throws JsonProcessingException {
 Object beanWithExceptionThrowingMethod = new Object() {
 @JsonValue
 public void throwException() throws IOException {
 throw new IOException("Expected exception");
 }
 };
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Object.class, null);
 String json = null;
 try {
 json = serializer.serializeValueAsString(beanWithExceptionThrowingMethod, null, null);
 } catch (JsonProcessingException jpe) {
 // Pass
 }
 fail("Expected JsonProcessingException");
 }

 @Test(expected = IllegalAccessException.class)
 public void testJsonValueAnnotationWithPrivateMethod() throws JsonProcessingException {
 Object beanWithPrivateMethod = new Object() {
 private static int value = 42;
 @JsonValue
 public static int getValue() {
 return value;
 }
 };
 JsonValueSerializer serializer = objectMapper.findValueSerializer(Object.class, null);
 String json = serializer.serializeValueAsString(beanWithPrivateMethod, null, null);
 fail("Expected IllegalAccessException");
 }
}