import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 }

 @Test
 public void testBooleanTypeKeyDeserialization() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", "true");
 Object result = mapper.readValue(input.toString(), new TypeReference<Object>() {});
 assertTrue((Boolean) result);
 }

 @Test
 public void testBooleanTypeKeyDeserialization_InvalidInput() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", "invalidInput");
 try {
 mapper.readValue(input.toString(), StdKeyDeserializer.class);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 @Test
 public void testByteTypeKeyDeserialization() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", "10");
 Byte result = mapper.readValue(input.toString(), (Class<Byte>) Byte.class);
 assertEquals((byte) 10, result);
 }

 @Test
 public void testByteTypeKeyDeserialization_InvalidInput() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", "invalidInput");
 try {
 mapper.readValue(input.toString(), StdKeyDeserializer.class);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 // Add test cases for short, char, int, long, float, double, Locale, Date, Calendar, UUID, URI, URL, Class, Currency types...

 @Test
 public void testEnumTypeKeyDeserialization() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", "MyEnum.VALUE_ONE");
 StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(EnumResolver.constructForClass(MyEnum.class), null);
 Object result = mapper.readValue(input.toString(), new TypeReference<Object>() {}.withType(enumKD.getClass()));
 assertEquals(MyEnum.VALUE_ONE, result);
 }

 @Test
 public void testEnumTypeKeyDeserialization_InvalidInput() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", "InvalidEnumValue");
 StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(EnumResolver.constructForClass(MyEnum.class), null);
 try {
 mapper.readValue(input.toString(), new TypeReference<Object>() {}.withType(enumKD.getClass()));
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 @Test
 public void testStringCtorTypeKeyDeserialization() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", "\"MyString\"");
 StdKeyDeserializer.StringCtorKeyDeserializer stringCtorKeyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(String.class.getConstructor(String.class));
 Object result = mapper.readValue(input.toString(), new TypeReference<Object>() {}.withType(String.class));
 assertEquals("MyString", result);
 }

 @Test
 public void testStringFactoryTypeKeyDeserialization() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", "MyStringFactory.createInstance(\"MyString\")");
 StdKeyDeserializer.StringFactoryKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(
 StringFactory.class.getMethod("createInstance", String.class));
 Object result = mapper.readValue(input.toString(), new TypeReference<Object>() {}.withType(StringFactory.class));
 assertEquals(StringFactory.class, result.getClass());
 }

 @Test
 public void testNullKeyDeserialization() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", null);
 Object result = mapper.readValue(input.toString(), StdKeyDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testEmptyStringKeyDeserialization() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", "");
 Object result = mapper.readValue(input.toString(), StdKeyDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testWhitespaceStringKeyDeserialization() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("key", " \t \r \n MyString ");
 Object result = mapper.readValue(input.toString(), StdKeyDeserializer.class);
 assertEquals("MyString", result);
 }
}