import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;

public class EnumSerializerTest {

    private ObjectMapper mapper;
    private EnumSerializer<MyEnum> enumSerializer;

    @Before
    public void setup() {
        this.mapper = new ObjectMapper();
        this.enumSerializer = new EnumSerializer<MyEnum>(MyEnumValues.getInstance(), null);
    }

    @Test
    public void testSerialize() throws IOException {
        MyEnum enumValue = MyEnum.VALUE_1;
        JsonGenerator jsonGenerator = this.mapper.getFactory().createGenerator(new StringWriter());
        this.enumSerializer.serialize(enumValue, jsonGenerator, this.mapper.getSerializationConfig());
        ObjectNode actualNode = this.mapper.readValue(new StringReader(jsonGenerator.getOutputStream().toString()), ObjectNode.class);
        String expectedValue = _values.serializedValueFor(enumValue);
        Assert.assertEquals(expectedValue, actualNode.get("value").asText());
    }

    @Test
    public void testSerializeAsIndex() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().with(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        MyEnum enumValue = MyEnum.VALUE_1;
        String jsonString = objectWriter.writeValueAsString(enumValue);
        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader(jsonString));
        JsonToken token = jsonParser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(enumValue.ordinal(), jsonParser.getValueAsInt());
    }

    @Test
    public void testSerializeToString() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().without(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        MyEnum enumValue = MyEnum.VALUE_1;
        String jsonString = objectWriter.writeValueAsString(enumValue);
        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader(jsonString));
        String expectedValue = enumValue.name();
        JsonToken token = jsonParser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals(expectedValue, jsonParser.getValueAsString());
    }

    @Test
    public void testCreateContextual() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();
        BeanDescription beanDescription = new BeanDescription(MyEnum.class);
        JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
        jsonFormatValue.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<MyEnum> jsonSerializer = this.enumSerializer.createContextual(objectMapper.getSerializationConfig(), beanDescription, jsonFormatValue);
        Assert.assertSame(this.enumSerializer, jsonSerializer);
    }

    @Test
    public void testCreateContextualWithShapeWrittenUsingIndex() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();
        BeanDescription beanDescription = new BeanDescription(MyEnum.class);
        JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
        jsonFormatValue.setShape(JsonFormat.Shape.INT);
        JsonSerializer<MyEnum> jsonSerializer = this.enumSerializer.createContextual(objectMapper.getSerializationConfig(), beanDescription, jsonFormatValue);
        Assert.assertNotSame(this.enumSerializer, jsonSerializer);
        Assert.assertTrue(jsonSerializer instanceof EnumSerializer);
    }

    @Test
    public void testGetSchema() throws IOException {
        JsonNode schemaNode = this.enumSerializer.getSchema(this.mapper.getSerializationConfig(), MyEnum.class);
        Assert.assertNotNull(schemaNode);
        Assert.assertEquals("string", schemaNode.get("type").get("typeName").asText());
        Assert.assertEquals("true", schemaNode.get("discriminatorProperty").asText());
        Assert.assertNull(schemaNode.get("discriminatorValue"));
        Assert.assertEquals("value", schemaNode.get("properties").get("value").get("name").asText());
        Assert.assertEquals("string", schemaNode.get("properties").get("value").get("type").get("typeName").asText());
        Assert.assertEquals(Arrays.asList(MyEnum.VALUE_1.name(), MyEnum.VALUE_2.name()), schemaNode.get("properties").get("value").get("enum").asArray().getValuesAsArray());
    }

    @Test
    public void testAcceptJsonFormatVisitorWrapper() throws IOException {
        JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
        jsonFormatValue.setShape(JsonFormat.Shape.STRING);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = this.mapper.getFactory().createJsonFormatVisitorWrapper(MyEnum.class, jsonFormatValue);
        this.enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, MyEnum.class);
        Assert.assertEquals("STRING", jsonFormatVisitorWrapper.getSerializerProvided().getName());
    }

    @Test
    public void testAcceptJsonFormatVisitorWrapperWithShapeWrittenUsingIndex() throws IOException {
        JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
        jsonFormatValue.setShape(JsonFormat.Shape.INT);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = this.mapper.getFactory().createJsonFormatVisitorWrapper(MyEnum.class, jsonFormatValue);
        this.enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, MyEnum.class);
        Assert.assertEquals("INT", jsonFormatVisitorWrapper.getSerializerProvided().getName());
    }

    @Test
    public void testAcceptJsonFormatVisitorWrapperWithShapeWrittenUsingToString() throws IOException {
        JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
        jsonFormatValue.setShape(JsonFormat.Shape.STRING);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = this.mapper.getFactory().createJsonFormatVisitorWrapper(MyEnum.class, jsonFormatValue);
        this.enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, MyEnum.class);
        Assert.assertEquals("STRING", jsonFormatVisitorWrapper.getSerializerProvided().getName());
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    enum MyEnum {
        VALUE_1,
        VALUE_2,
        VALUE_3
    }

    enum MyEnumValues {
        INSTANCE;

        public EnumSerializer<MyEnum> getSerializer() {
            return (EnumSerializer<MyEnum>) StdSerializerFinder.findValueSerializer(MyEnum.class, this.mapper.getSerializationConfig());
        }

        public EnumValues getEnumValues() {
            return new EnumValues(MyEnum.class, this.mapper.getSerializationConfig());
        }

        public SerializableString serializedValueFor(MyEnum enumValue) {
            return new SerializableString(enumValue.name());
        }
    }
}