import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDefaultSerializer() throws JsonProcessingException {
 // Testing default serializer
 ObjectNode input = mapper.createObjectNode();
 input.putPOJO("testObj", new TestClass());

 ObjectNode result = mapper.writeValueAsObject(input);

 String json = result.toString();

 assertNotNull(json);
 assertFalse(json.isEmpty());
 }

 @Test
 public void testCustomSerializer() throws JsonProcessingException {
 // Testing custom serializer
 TestCustomSerializer customSerializer = new TestCustomSerializer();
 mapper.registerSerializer(TestClass.class, customSerializer);

 ObjectNode input = mapper.createObjectNode();
 input.putPOJO("testObj", new TestClass());

 ObjectNode result = mapper.writeValueAsObject(input);

 String json = result.toString();

 assertNotNull(json);
 assertFalse(json.isEmpty());

 // Testing custom serializer with JsonFormatVisitorWrapper
 mapper.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(), TestClass.class);
 }

 @Test
 public void testSchemaAware() throws JsonProcessingException {
 // Testing SchemaAware
 TestSchemaAware serializer = mapper.findValueSerializer(TestClass.class);
 ObjectNode schema = serializer.getSchema(mapper.getSerializerProvider(), TestClass.class);

 ObjectNode expectedSchema = mapper.createObjectNode();
 expectedSchema.put("type", "object");
 expectedSchema.put("properties", mapper.createObjectNode());
 expectedSchema.get("properties").put("testProp", createSchemaNode("string"));

 // Testing getSchema with isOptional = false
 assertEquals(expectedSchema, schema);

 // Testing getSchema with isOptional = true
 ObjectNode optionalSchema = mapper.getSerializerProvider().defaultSerializableValueType(TestClass.class).getSerializer().getSchema(mapper.getSerializerProvider(), TestClass.class, true);
 assertEquals(expectedSchema, optionalSchema);
 }

 @Test
 public void testWrappingExceptions() throws JsonProcessingException {
 // Testing exception wrapping
 TestClass obj = new TestClass();
 StdSerializer<TestClass> serializer = new StdSerializer<>(TestClass.class) {
 @Override
 public void serialize(TestClass value, JsonGenerator jgen, SerializerProvider provider)
 throws IOException, JsonGenerationException {
 throw new RuntimeException("Something went wrong!");
 }
 };
 mapper.registerSerializer(TestClass.class, serializer);

 try {
 mapper.writeValueAsString(new TestClass());
 fail("Expected exception but didn't get it");
 } catch (JsonMappingException e) {
 // Testing that exception is wrapped correctly
 assertEquals("Something went wrong!", e.getOriginalMessage());
}
}

 @Test
 public void testIsDefaultSerializer() {
 // Testing isDefaultSerializer method
 StdSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
 assertTrue(StdSerializer.this.isDefaultSerializer(stringSerializer));

 StdSerializer<TestClass> customSerializer = new TestSerializer<>();
 assertFalse(StdSerializer.this.isDefaultSerializer(customSerializer));
}

 private ObjectNode createSchemaNode(String type) {
 return mapper.createObjectNode().putPOJO("type", type);
 }

}

class TestClass {
 private String testProp;

 public String getTestProp() {
 return testProp;
 }

 public void setTestProp(String testProp) {
 this.testProp = testProp;
 }
}

class TestCustomSerializer extends StdSerializer<TestClass> {
 protected TestCustomSerializer() {
 super(TestClass.class);
 }

 @Override
 public void serialize(TestClass value, JsonGenerator jgen, SerializerProvider provider)
 throws IOException, JsonGenerationException {
 // Custom serialization logic here
 jgen.writeString("Custom Serialization");
 }
}

class TestSchemaAware extends StdSerializer<TestClass> implements SchemaAware {
 protected TestSchemaAware() {
 super(TestClass.class);
 }

 @Override
 public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
 throws JsonMappingException {
 visitor.writeObjectFieldName("testProp");
 }
}