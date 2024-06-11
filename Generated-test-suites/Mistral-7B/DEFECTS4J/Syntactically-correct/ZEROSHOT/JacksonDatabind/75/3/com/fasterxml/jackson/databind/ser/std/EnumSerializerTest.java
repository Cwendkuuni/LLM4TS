import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.databind.util.JsonWriterFactory;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;

public class EnumSerializerTest extends TestBase {

    private EnumSerializer<MyEnum> enumSerializer;
    private MyEnum[] myEnumValues = MyEnum.values();
    private Class<MyEnum> myEnumClass = MyEnum.class;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Enum<Object>> serializer = mapper.getSerializerProvider().findValueSerializer(Enum.class);
        EnumValues enumValues = EnumValues.constructFromEnum(MyEnum.class);
        enumSerializer = new EnumSerializer<>(enumValues, null);
    }

    @Test
    public void testSerializeToString() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        ObjectNode node = objectMapper.createObjectNode();
        when(gen.getOutputPrinter()).thenReturn(node);

        when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        enumSerializer.serialize(MyEnum.VALUE_ONE, gen, serializer.getSerializerProvider());

        assertEquals("VALUE_ONE", node.get("$value").asText());
    }

    @Test
    public void testSerializeToStringAnnotated() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        ObjectNode node = objectMapper.createObjectNode();
        when(gen.getOutputPrinter()).thenReturn(node);

        when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        MyEnum annotatedEnum = new AnnotatedEnum();
        enumSerializer.serialize(annotatedEnum, gen, serializer.getSerializerProvider());

        assertEquals(annotatedEnum.name(), node.get("$value").asText());
    }

    @Test
    public void testSerializeAsIndex() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);

        when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        enumSerializer.serialize(myEnumValues[0], gen, serializer.getSerializerProvider());

        verify(gen).writeNumber(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeAsIndexWithShapeAnnotation() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);

        when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        MyEnum shapeAnnotationEnum = new ShapeAnnotationEnum();
        enumSerializer.serialize(shapeAnnotationEnum, gen, serializer.getSerializerProvider());

        fail("Should throw exception as the Enum has a shape annotation not allowed for index serialization");
    }

    @Test
    public void testSerializeAsIndexWithJsonFormatAnnotation() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);

        when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        MyEnum jsonFormatAnnotationEnum = new JsonFormatAnnotationEnum();
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(JsonFormat.Shape.STRING);

        ObjectNode node = objectMapper.readTree(jsonFormat.toString());
        JsonSerializer<Object> contextualSerializer = mapper.findContextualSerializer(MyEnum.class, null);

        when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        enumSerializer.createContextual(serializer.getSerializerProvider(), null).serialize(
                jsonFormatAnnotationEnum, gen, serializer.getSerializerProvider());

        verify(gen).writeNumber(myEnumValues.length);
    }

    @Test
    public void testSerializeAsIndexWithJsonFormatAnnotationWithNullProperty() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);

        when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        MyEnum jsonFormatAnnotationEnum = new JsonFormatAnnotationEnum();
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(JsonFormat.Shape.STRING);

        ObjectNode node = objectMapper.readTree(jsonFormat.toString());
        JsonSerializer<Object> contextualSerializer = mapper.findContextualSerializer(MyEnum.class, null);

        when(gen.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        ObjectNode contextualNode = objectMapper.createObjectNode();
        contextualNode.set("myProperty", null);

        JsonNode contextObject = mapper.readTree(contextualNode.toString());

        contextualSerializer.createContextual(serializer.getSerializerProvider(), null)
                .serialize(contextObject.path("myProperty"), gen, serializer.getSerializerProvider());

        verify(gen).writeNumber(myEnumValues.length);
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectNode schemaNode = enumSerializer.getSchema(serializer.getSerializerProvider(), null);

        assertEquals("string", schemaNode.get("type").asText());

        ArrayNode enumNode = schemaNode.path("properties").get("enum");
        assertEquals(myEnumValues.length, enumNode.size());

        for (Enum<?> enumValue : myEnumValues) {
            assertEquals(enumValue.name(), enumNode.get(enumNode.size() - 1).asText());
        }
    }

    @JsonSerialize(using = EnumSerializer.class)
    static enum MyEnum {
        VALUE_ONE,
        VALUE_TWO,
        VALUE_THREE
    }

    static class AnnotatedEnum extends MyEnum {
        @JsonFormat(shape = JsonFormat.Shape.STRING, serializeAs = JsonFormat.SerializeAs.STRING)
        @Override
        public String toString() {
            return super.toString() + " ANNOTATED";
        }
    }

    static class ShapeAnnotationEnum extends MyEnum {
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        @Override
        public int ordinal() {
            return super.ordinal() + 1;
        }
    }

    static class JsonFormatAnnotationEnum extends MyEnum {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private String name;
    }
}