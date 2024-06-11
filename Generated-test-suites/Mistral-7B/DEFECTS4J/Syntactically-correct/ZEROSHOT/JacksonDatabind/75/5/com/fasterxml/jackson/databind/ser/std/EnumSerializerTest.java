import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnumSerializerTest {

 private static final EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), null);

 @Test
 public void testSerialization() throws IOException {
 ObjectWriter writer = new ObjectMapper().writer();
 MyEnum value = MyEnum.VALUE1;
 String json = writer.writeValueAsString(value);
 assertEquals("\"VALUE1\"", json);

 value = MyEnum.VALUE2;
 json = writer.writeValueAsString(value);
 assertEquals("\"VALUE2\"", json);
 }

 @Test
 public void testSerializationWithIndex() throws IOException {
 ObjectWriter writer = new ObjectMapper().writer().withFeature(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
 MyEnum value = MyEnum.VALUE1;
 String json = writer.writeValueAsString(value);
 assertEquals("0", json);

 value = MyEnum.VALUE2;
 json = writer.writeValueAsString(value);
 assertEquals("1", json);
 }

 @Test
 public void testSerializationWithEnumValues() throws IOException {
 ObjectWriter writer = new ObjectMapper().writer();
 MyEnum value = MyEnum.VALUE3;
 String json = writer.writeValueAsString(value);
 assertEquals("\"VALUE3\"", json);

 ObjectNode node = writer.writeValueAsObject(new ObjectMapper().createObjectNode().put("test", value));
 assertEquals("\"VALUE3\"", node.get("test").asText());
 }

 @Test
 public void testSerializationWithCustomFormat() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormat format = new JsonFormat();
 format.shape(JsonFormat.Shape.STRING);
 mapper.setDefaultPropertyFormat(MyEnum.class, format);
 ObjectWriter writer = mapper.writer();
 MyEnum value = MyEnum.VALUE4;
 String json = writer.writeValueAsString(value);
 assertEquals("VALUE4", json);
 }

 @Test
 public void testDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 MyEnum value = mapper.readValue("\"VALUE1\"", MyEnum.class);
 assertEquals(MyEnum.VALUE1, value);

 value = mapper.readValue("0", MyEnum.class);
 assertEquals(MyEnum.VALUE1, value);

 ObjectNode node = mapper.readTree("{\"test\": \"VALUE2\"}");
 value = node.path("test").as(MyEnum.class);
 assertEquals(MyEnum.VALUE2, value);
 }

 @Test
 public void testDeserializationWithCustomFormat() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormat format = new JsonFormat();
 format.shape(JsonFormat.Shape.STRING);
 mapper.setDefaultPropertyFormat(MyEnum.class, format);
 MyEnum value = mapper.readValue("VALUE3", MyEnum.class);
 assertEquals(MyEnum.VALUE3, value);
 }

 @Test
 public void testGetSchema() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode schema = mapper.readTree(serializer.getSchema(mapper.getSerializationConfig(), null));
 assertEquals(TypeFactory.defaultInstance().constructType(ObjectNode.class), schema.getClass());
 ArrayNode enumNode = schema.path("$ref");
 assertEquals("schema#/$ref/properties/test/items/enum", enumNode.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormat format = new JsonFormat();
 format.shape(JsonFormat.Shape.STRING);
 mapper.setDefaultPropertyFormat(MyEnum.class, format);
 JsonFormatVisitorWrapper visitor = mapper.schemaProvider().getSchemaFactory().getJsonFormatVisitor(mapper.getSerializationConfig(), null);
 serializer.acceptJsonFormatVisitor(visitor, null);
 assertEquals(JsonFormat.Value.class, visitor.getHandlerType());
 JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(null);
 assertNotNull(stringVisitor);
 Set<String> enums = stringVisitor.enumTypes();
 assertEquals(Arrays.asList("VALUE1", "VALUE2", "VALUE3", "VALUE4"), enums);
 }

 @AfterAll
 public void tearDown() {
 serializer = null;
 }
}

 enum MyEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE1")
 VALUE1,
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE2")
 VALUE2,
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE3")
 VALUE3,
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE4")
 VALUE4;
}