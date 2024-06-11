import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnumSerializerTest {

    private EnumSerializer<MyEnum> enumSerializer;
    private ObjectMapper objectMapper;

    @BeforeAll
    public void setup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        enumSerializer = objectMapper.findValueSerializer(MyEnum.class);
    }

    @Test
    public void testSerializeEnumAsText() throws IOException {
        MyEnum enumValue = MyEnum.VALUE_1;
        String expectedResult = "VALUE_1";

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        enumSerializer.serialize(enumValue, jsonGenerator, new SerializerProvider());
        String actualResult = jsonGenerator.getOutputBuffer().toString();
        jsonGenerator.close();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSerializeEnumAsIndex() throws IOException {
        MyEnum enumValue = MyEnum.VALUE_1;
        int expectedIndex = 0;

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        if (enumSerializer.canSerializeAsNumber()) {
            jsonGenerator.writeNumber(enumValue.ordinal());
        } else {
            enumSerializer.serialize(enumValue, jsonGenerator, new SerializerProvider());
        }
        String actualResult = jsonGenerator.getOutputBuffer().toString();
        jsonGenerator.close();

        assertEquals(Integer.toString(expectedIndex), actualResult);
    }

    @Test
    public void testSerializeEnumWithCustomFormat() throws IOException {
        MyEnum enumValue = MyEnum.VALUE_2;
        String expectedResult = "CUSTOM_VALUE_2";

        JsonFormat format = new JsonFormat(Shape.STRING, "CUSTOM_VALUE_");
        ObjectNode rootNode = objectMapper.readValue(
                new ByteArrayInputStream(new byte[]{"{'enum':['VALUE_1','CUSTOM_VALUE_2']}"
                        .getBytes()}), ObjectNode.class);
        ObjectNode node = rootNode.path("__V1_jackson_databind_JsonFormatVisitorWrapper");
        JsonStringFormatVisitor visitor = node.findValueDelegate().expectStringFormat(null);
        visitor.enumTypes(Arrays.asList("VALUE_1", "CUSTOM_VALUE_2"));

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<MyEnum> serializer = objectMapper.findValueSerializer(MyEnum.class);
        serializer.acceptJsonFormatVisitor(visitor, null);
        serializer.serialize(enumValue, jsonGenerator, new SerializerProvider());
        String actualResult = jsonGenerator.getOutputBuffer().toString();
        jsonGenerator.close();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSerializeEnumWithDefaultFormat() throws IOException {
        MyEnum enumValue = MyEnum.VALUE_3;
        String expectedResult = "VALUE_3";

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        enumSerializer.serialize(enumValue, jsonGenerator, new SerializerProvider());
        String actualResult = jsonGenerator.getOutputBuffer().toString();
        jsonGenerator.close();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeserializeEnumAsText() throws IOException {
        String jsonString = "\"VALUE_1\"";
        MyEnum enumValue = enumSerializer.deserialize(jsonString, MyEnum.class, null);

        assertEquals(MyEnum.VALUE_1, enumValue);
    }

    @Test
    public void testDeserializeEnumAsIndex() throws IOException {
        int index = 0;
        String jsonString = "0";
        MyEnum enumValue = enumSerializer.deserialize(jsonString, MyEnum.class, null);

        assertEquals(MyEnum.VALUE_1, enumValue);
    }

    @Test
    public void testDeserializeEnumWithCustomFormat() throws IOException {
        String jsonString = "\"CUSTOM_VALUE_2\"";
        MyEnum enumValue = enumSerializer.deserialize(jsonString, MyEnum.class, null);

        assertEquals(MyEnum.VALUE_2, enumValue);
    }

    @Test
    public void testDeserializeEnumWithInvalidValue() throws IOException {
        String jsonString = "\"INVALID_VALUE\"";

        assertThrows(JsonMappingException.class, () -> {
            enumSerializer.deserialize(jsonString, MyEnum.class, null);
        });
    }

    @Test
    public void testDeserializeEnumWithNullValue() throws IOException {
        String jsonString = null;

        assertThrows(JsonProcessingException.class, () -> {
            enumSerializer.deserialize(jsonString, MyEnum.class, null);
        });
    }

    @Test
    public void testDeserializeEnumWithMissingValue() throws IOException {
        String jsonString = "{}";

        assertThrows(JsonMappingException.class, () -> {
            enumSerializer.deserialize(jsonString, MyEnum.class, null);
        });
    }

    @Test
    public void testGetEnumValues() {
        EnumSet<MyEnum> expectedEnumSet = EnumSet.allOf(MyEnum.class);

        EnumSerializer<MyEnum> enumSerializer = new EnumSerializer<>(
                new EnumValues(MyEnum.class), null);
        EnumValues enumValues = enumSerializer.getEnumValues();

        assertEquals(expectedEnumSet, enumValues.asJavaSet());
    }

}

enum MyEnum {
    VALUE_1,
    VALUE_2("CUSTOM_VALUE_2"),
    VALUE_3
}