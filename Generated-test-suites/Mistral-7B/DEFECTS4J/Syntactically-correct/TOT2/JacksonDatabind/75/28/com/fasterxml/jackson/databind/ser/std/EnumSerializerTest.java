import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test cases for EnumSerializer class")
public class EnumSerializerTest {

 @BeforeAll
 static void setup() {
 // Initialize any shared resources or setup state for all tests
 }

 @AfterAll
 static void tearDown() {
 // Clean up any shared resources or restore state after all tests
 }

 @Test
 @DisplayName("Test serialization of an Enum using the default constructor")
 void testSerializationDefaultConstructor() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.create(MyEnum.class));
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 MyEnum enumValue = MyEnum.VALUE_1;
 ObjectNode jsonNode = writer.writeValueAsTree(enumValue);
 String jsonString = jsonNode.toString();
 assertEquals("\"VALUE_1\"", jsonString);
 }

 @Test
 @DisplayName("Test serialization of an Enum using a SerializerProvider and JsonFormat.Value")
 void testSerializationSerializerProviderAndJsonFormatValue() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
 MyEnum enumValue = MyEnum.VALUE_1;
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 ObjectNode jsonNode = writer.writeValueAsTree(enumValue, format);
 String jsonString = jsonNode.toString();
 assertEquals("\"VALUE_1\"", jsonString);
 }

 @Test
 @DisplayName("Test serialization of an Enum using ContextualSerializer")
 void testSerializationContextualSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
 MyEnum enumValue = MyEnum.VALUE_1;
 ObjectNode jsonNode = writer.writeValueAsTree(enumValue, new EnumSerializer(EnumValues.create(MyEnum.class), true));
 String jsonString = jsonNode.toString();
 assertEquals("0", jsonString);
 }

 @Test
 @DisplayName("Test serialization of an Enum with custom values using JsonFormat.Value")
 void testSerializationCustomValuesJsonFormatValue() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
 MyEnum enumValue = MyEnum.CUSTOM_VALUE;
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setSerializableValues(new SerializableString[] { new SerializableString("CUSTOM_VALUE_STRING") });
 ObjectNode jsonNode = writer.writeValueAsTree(enumValue, format);
 String jsonString = jsonNode.toString();
 assertEquals("\"CUSTOM_VALUE_STRING\"", jsonString);
 }

 @Test
 @DisplayName("Test serialization of an Enum with annotation @JsonFormat(shape = Shape.ARRAY)")
 void testSerializationAnnotationShapeArray() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
 MyEnum enumValue = MyEnum.VALUE_1;
 ObjectNode jsonNode = writer.writeValueAsTree(enumValue, new JsonFormat.Value().shape(JsonFormat.Shape.ARRAY));
 String jsonString = jsonNode.toString();
 assertEquals("[0]", jsonString);
 }

 @Nested
 @DisplayName("Test Edge Cases and Exceptions")
 class EdgeCasesAndExceptions {

 @Test
 @DisplayName("Test serialization of null Enum value")
 void testSerializationNullEnumValue() {
 Assertions.assertThrows(NullPointerException.class, () -> new EnumSerializer(null, null));
 }

 @Test
 @DisplayName("Test serialization of Enum value with no associated string value")
 void testSerializationNoAssociatedStringValue() {
 EnumSerializer serializer = new EnumSerializer(EnumValues.create(MyEnumWithNoStringValue.class));
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 MyEnumWithNoStringValue enumValue = MyEnumWithNoStringValue.VALUE_1;
 ObjectNode jsonNode = writer.writeValueAsTree(enumValue);
 String jsonString = jsonNode.toString();
 assertNull(jsonString);
 }

 @Test
 @DisplayName("Test serialization of Enum value with no ordinal value")
 void testSerializationNoOrdinalValue() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new EnumSerializer(EnumValues.create(MyEnumWithNoOrdinalValue.class)));
 }
 }

 @Test
 @DisplayName("Test deserialization of Enum from JSON string using the default constructor")
 void testDeserializationDefaultConstructor() throws JsonProcessingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 MyEnum enumValue = objectMapper.readValue(new ObjectMapper().readTree("\"VALUE_1\"").get("VALUE_1"), MyEnum.class);
 assertEquals(MyEnum.VALUE_1, enumValue);
 }

 @Test
 @DisplayName("Test deserialization of Enum from JSON string using JsonFactory and JsonParser")
 void testDeserializationJsonFactoryAndJsonParser() throws JsonProcessingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFactory factory = objectMapper.getFactory();
 JsonParser parser = factory.createParser(new StringReader("{\"VALUE_1\":\"VALUE_1\"}"));
 MyEnum enumValue = parser.readValueAs(MyEnum.class);
 assertEquals(MyEnum.VALUE_1, enumValue);
 }

 @Test
 @DisplayName("Test deserialization of Enum from JSON string using ObjectMapper and JsonNode")
 void testDeserializationObjectMapperAndJsonNode() throws JsonProcessingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode jsonNode = new ObjectMapper().readTree("{\"VALUE_1\":\"VALUE_1\"}");
 MyEnum enumValue = objectMapper.treeToValue(jsonNode.path("VALUE_1"), MyEnum.class);
 assertEquals(MyEnum.VALUE_1, enumValue);
 }

 @Test
 @DisplayName("Test deserialization of Enum from JSON string with custom string values")
 void testDeserializationCustomStringValues() throws JsonProcessingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode jsonNode = new ObjectMapper().readTree("{\"VALUE_1\":\"CUSTOM_VALUE_STRING\"}");
 MyEnumCustomValue enumValue = objectMapper.treeToValue(jsonNode.path("VALUE_1"), MyEnumCustomValue.class);
 assertEquals(MyEnumCustomValue.CUSTOM_VALUE, enumValue);
 }

 @Test
 @DisplayName("Test deserialization of Enum from JSON string with annotation @JsonFormat(shape = Shape.ARRAY)")
 void testDeserializationAnnotationShapeArray() throws JsonProcessingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode jsonNode = new ObjectMapper().readTree("[0]");
 MyEnumWithArrayShape enumValue = objectMapper.treeToValue(jsonNode, MyEnumWithArrayShape.class);
 assertEquals(MyEnumWithArrayShape.VALUE_1, enumValue);
 }
}