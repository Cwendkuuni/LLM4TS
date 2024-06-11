import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class {@link EnumSerializerTest} is used to test all methods in {@link EnumSerializer} class.
 */
@JacksonStdImpl
public class EnumSerializerTest {

 private EnumSerializer enumSerializer;
 private EnumValues enumValues;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 enumValues = new EnumValues(MyEnum.class);
 enumSerializer = new EnumSerializer(enumValues, null);
 objectMapper = new ObjectMapper();
 }

 /**
 * Test constructor when EnumValues v is provided.
 */
 @Test
 public void testEnumSerializerConstructor() {
 EnumSerializer serializer = new EnumSerializer(enumValues, null);
 assertEquals(MyEnum.class, serializer.handledType());
 assertNotNull(serializer._values);
 assertNull(serializer._serializeAsIndex);
 }

 /**
 * Test constructor when EnumValues v and Boolean serializeAsIndex are provided.
 */
 @Test
 public void testEnumSerializerConstructorWithSerializeAsIndex() {
 EnumSerializer serializer = new EnumSerializer(enumValues, true);
 assertEquals(MyEnum.class, serializer.handledType());
 assertNotNull(serializer._values);
 assertNotNull(serializer._serializeAsIndex);
 }

 /**
 * Test factory method used by BasicSerializerFactory for constructing serializer instance of Enum types.
 */
 @Test
 public void testConstruct() throws JsonProcessingException {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("enumClass", MyEnum.class.getName());
 ObjectNode configNode = objectMapper.createObjectNode();
 ObjectNode beanDescNode = objectMapper.createObjectNode();
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 JsonNode formatNode = objectMapper.valueToTree(format);
 beanDescNode.set("name", "name");
 rootNode.set("config", configNode);
 rootNode.set("beanDesc", beanDescNode);
 rootNode.set("format", formatNode);
 ObjectNode result = objectMapper.treeToObject(enumSerializer.construct(MyEnum.class, objectMapper.getSerializationConfig(), null, null), ObjectNode.class);
 assertEquals(MyEnum.class, result.get("handledType").asClass());
 assertNotNull(result.get("_values"));
 assertNull(result.get("_serializeAsIndex"));
 }

 /**
 * Test createContextual method.
 */
 @Test
 public void testCreateContextual() throws JsonMappingException {
 JsonSerializer<?> result1 = enumSerializer.createContextual(null, null);
 assertEquals(enumSerializer, result1);
 JsonSerializer<?> result2 = enumSerializer.createContextual(null, new BeanProperty("name", MyEnum.class));
 assertEquals(enumSerializer, result2);
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.NUMBER);
 JsonSerializer<?> result3 = enumSerializer.createContextual(null, new BeanProperty("name", MyEnum.class, format));
 assertNotEquals(enumSerializer, result3);
 }

 /**
 * Test getEnumValues method.
 */
 @Test
 public void testGetEnumValues() {
 assertEquals(enumValues, enumSerializer.getEnumValues());
 }

 /**
 * Test serialize method.
 */
 @Test
 public void testSerialize() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream);
 enumSerializer.serialize(MyEnum.ONE, generator, objectMapper.getSerializationConfig());
 generator.flush();
 byte[] expectedBytes = "{\"c\":\"ONE\"}".getBytes();
 assertArrayEquals(expectedBytes, outputStream.toByteArray());
 generator.close();
 outputStream.close();
 }

 /**
 * Test serialize method with index serialization.
 */
 @Test
 public void testSerializeWithIndexSerialization() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream);
 JsonSerializer<?> indexSerializer = new EnumSerializer(enumValues, true);
 indexSerializer.serialize(MyEnum.ONE, generator, objectMapper.getSerializationConfig());
 generator.flush();
 byte[] expectedBytes = "0\n".getBytes();
 assertArrayEquals(expectedBytes, outputStream.toByteArray());
 generator.close();
 outputStream.close();
 }

 /**
 * Test serialize method with toString serialization.
 */
 @Test
 public void testSerializeWithToStringSerialization() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream);
 ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
 JsonSerializer<?> toStringSerializer = new EnumSerializer(enumValues, null) {
 @Override
 public void serialize(Enum<?> en, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 super.serialize(en, gen, serializers);
 gen.writeString(en.toString());
 }
 };
 toStringSerializer.serialize(MyEnum.ONE, generator, objectMapper.getSerializationConfig());
 generator.flush();
 byte[] expectedBytes = "{\"c\":\"ONE\"}".getBytes();
 assertArrayEquals(expectedBytes, outputStream.toByteArray());
 generator.close();
 outputStream.close();
 }

 /**
 * Test getSchema method.
 */
 @Test
 public void testGetSchema() throws JsonProcessingException {
 ObjectNode result = objectMapper.treeToObject(enumSerializer.getSchema(objectMapper.getSerializationConfig(), MyEnum.class.asType()), ObjectNode.class);
 assertTrue(result.get("type").equals("string"));
 ArrayNode enumNode = result.path("properties").get("c").get("enum");
 assertEquals(2, enumNode.size());
 assertEquals(MyEnum.ONE.toString(), enumNode.get(0).asText());
 assertEquals(MyEnum.TWO.toString(), enumNode.get(1).asText());
 }

 /**
 * Test acceptJsonFormatVisitor method.
 */
 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = objectMapper.getSerializationConfig().acceptJsonFormatVisitor(ObjectNode.class, null);
 enumSerializer.acceptJsonFormatVisitor(visitor, MyEnum.class.asType());
 ArrayNode expectedEnumNode = objectMapper.createArrayNode();
 expectedEnumNode.add("ONE");
 expectedEnumNode.add("TWO");
 assertEquals(expectedEnumNode, visitor.getAcceptedNode());
 }
}

 enum MyEnum {
 ONE,
 TWO;
}