import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@JacksonStdImpl
public class EnumSerializerTest {

 private EnumSerializer serializer;
 private EnumValues enumValues;

 @Before
 public void setup() {
 enumValues = EnumValues.constructFromName(new Object(), MyEnum.class);
 serializer = new EnumSerializer(enumValues, false);
 }

 @Test
 public void testSerializeAsEnumValue() throws JsonProcessingException {
 ObjectNode rootNode = ObjectMapper.writeValueAsObject(MyEnum.RED);
 assertEquals("\"RED\"", rootNode.path("$").asText());
 }

 @Test
 public void testSerializeAsOrdinal() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer = new EnumSerializer(enumValues, true);
 serializer.serialize(MyEnum.RED, jsonGenerator, null);
 assertEquals("0", jsonGenerator.getOutputBuffer().toString().trim());
 }

 @Test
 public void testSerializeWithCustomValues() throws JsonProcessingException {
 ObjectNode rootNode = ObjectMapper.writeValueAsObject(MyEnum.GREEN, new ObjectMapper().findValueToSerialize(MyEnum.class, null, null));
 assertEquals("\"CUSTOM_GREEN\"", rootNode.path("$").asText());
 }

 @Test
 public void testCreateContextualSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode rootNode = ObjectMapper.writeValueAsObject(new MyEnumProperty("CUSTOM_BLUE"));
 JsonFormat.Value format = new JsonFormat.Value() {{
 setShape(JsonFormat.Shape.STRING);
 setEnumValues(enumValues);
 setCustomSerializers(Collections.singletonMap(MyEnumProperty.class, new StringEnumSerializer()));
 }};
 JsonSerializer<?> serializer = objectMapper.findValueSerializer(MyEnumProperty.class, null, null);
 serializer = serializer.createContextual(objectMapper.getSerializerProvider(), new BeanProperty("myProperty"));
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 ((EnumSerializer) serializer).serialize(new MyEnumProperty("CUSTOM_BLUE"), jsonGenerator, null);
 assertEquals("\"CUSTOM_BLUE\"", jsonGenerator.getOutputBuffer().toString().trim());
 }

 @Test
 public void testCreateContextualSerializerWithDefaultShape() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper objectMapperWithShape = new ObjectMapper();
 objectMapperWithShape.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 objectMapperWithShape.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 ObjectNode rootNode = ObjectMapper.writeValueAsObject(new MyEnumProperty("CUSTOM_BLUE"), new ObjectMapper().findValueToSerialize(MyEnumProperty.class, null, null));
 JsonFormat.Value format = new JsonFormat.Value() {{
 setShape(JsonFormat.Shape.ARRAY);
 setEnumValues(enumValues);
 }};
 JsonSerializer<?> serializer = objectMapper.findValueSerializer(MyEnumProperty.class, null, null);
 serializer = serializer.createContextual(objectMapper.getSerializerProvider(), new BeanProperty("myProperty"));
 JsonGenerator jsonGenerator = objectMapperWithShape.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 ((EnumSerializer) serializer).serialize(new MyEnumProperty("CUSTOM_BLUE"), jsonGenerator, null);
 assertEquals("[null]", jsonGenerator.getOutputBuffer().toString().trim());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateContextualSerializerWithUnsupportedShape() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper objectMapperWithShape = new ObjectMapper();
 ObjectMapper objectMapperWithCustomSerializer = new ObjectMapper();
 objectMapperWithCustomSerializer.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 ObjectNode rootNode = ObjectMapper.writeValueAsObject(new MyEnumProperty("CUSTOM_BLUE"), new ObjectMapper().findValueToSerialize(MyEnumProperty.class, null, null));
 JsonFormat.Value format = new JsonFormat.Value() {{
 setShape(JsonFormat.Shape.ENUM);
 setEnumValues(enumValues);
 }};
 JsonSerializer<?> serializer = objectMapper.findValueSerializer(MyEnumProperty.class, null, null);
 serializer = serializer.createContextual(objectMapper.getSerializerProvider(), new BeanProperty("myProperty"));
 JsonGenerator jsonGenerator = objectMapperWithShape.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 ((EnumSerializer) serializer).serialize(new MyEnumProperty("CUSTOM_BLUE"), jsonGenerator, null);
 }
}

class MyEnum {
 @JsonFormat(shape = Shape.STRING, value = "RED")
 static Enum RED = Enum.valueOf(MyEnum.class, "RED");
 @JsonFormat(shape = Shape.STRING, value = "GREEN")
 static Enum GREEN = Enum.valueOf(MyEnum.class, "GREEN");
 @JsonFormat(shape = Shape.STRING, value = "BLUE")
 static Enum BLUE = Enum.valueOf(MyEnum.class, "BLUE");
 @JsonFormat(shape = Shape.STRING, value = "CUSTOM_GREEN")
 static Enum CUSTOM_GREEN = Enum.valueOf(MyEnum.class, "CUSTOM_GREEN");
}

class MyEnumProperty {
 @JsonFormat(shape = Shape.STRING)
 private Enum enumValue;

 public MyEnumProperty() {
 }

 public MyEnumProperty(String value) {
 this.enumValue = Enum.valueOf(MyEnum.class, value);
 }

 public Enum getEnumValue() {
 return enumValue;
 }
}

class StringEnumSerializer extends JsonSerializer<Enum> {
 @Override
 public void serialize(Enum enumValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
 throws IOException {
 jsonGenerator.writeString(enumValue.name());
 }
}