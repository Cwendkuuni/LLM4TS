import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * Test class for {@link EnumSerializer}
 */
public class EnumSerializerTest {

    private EnumSerializer serializer;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        EnumSet<Colors> colors = EnumSet.allOf(Colors.class);
        EnumValues enumValues = new EnumValues(Colors.class, colors);
        serializer = new EnumSerializer(enumValues, false);
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerializeEnumToJson() throws JsonProcessingException {
        Colors color = Colors.RED;
        ObjectNode objectNode = mapper.readValue(serializer.serializeAsString(color), ObjectNode.class);
        assertEquals("\"RED\"", objectNode.get("name").asText());
    }

    @Test
    public void testSerializeEnumToJsonWithIndex() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = mapper.readValue(serializer.serializeAsStringWithIndex(Colors.RED), ObjectNode.class);
        assertEquals("0", objectNode.get("index").asText());
        assertEquals("\"RED\"", objectNode.get("name").asText());
    }

    @Test
    public void testSerializeEnumToJsonWithToString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper objectMapperWithToString = new ObjectMapper();
        objectMapperWithToString.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        ObjectNode objectNode = mapper.readValue(serializer.serializeAsString(Colors.RED), ObjectNode.class);
        ObjectNode objectNodeWithToString = mapperWithToString.readValue(serializer.serializeAsString(Colors.RED), ObjectNode.class);

        assertEquals("\"RED\"", objectNode.get("name").asText());
        assertEquals("RED", objectNodeWithToString.get("name").asText());
    }

    @Test
    public void testSerializeEnumArrayToJson() throws JsonProcessingException {
        Colors[] colors = Colors.values();
        ArrayNode arrayNode = mapper.readValue(serializer.serializeAsString(colors), ArrayNode.class);
        assertEquals(3, arrayNode.size());
        for (int i = 0; i < colors.length; i++) {
            JsonNode jsonNode = arrayNode.get(i);
            assertEquals(colors[i].name(), jsonNode.get("name").asText());
        }
    }

    @Test
    public void testSerializeEnumArrayToJsonWithIndex() throws JsonProcessingException {
        Colors[] colors = Colors.values();
        ArrayNode arrayNode = mapper.readValue(serializer.serializeAsStringWithIndex(colors), ArrayNode.class);
        assertEquals(3, arrayNode.size());
        for (int i = 0; i < colors.length; i++) {
            JsonNode jsonNode = arrayNode.get(i);
            assertEquals(colors[i].ordinal(), jsonNode.get("index").asInt());
            assertEquals(colors[i].name(), jsonNode.get("name").asText());
        }
    }

    @Test
    public void testSerializeEnumArrayToJsonWithToStringEnabled() throws JsonProcessingException {
        Colors[] colors = Colors.values();
        ArrayNode arrayNode = mapperWithToString.readValue(serializer.serializeAsString(colors), ArrayNode.class);
        assertEquals(3, arrayNode.size());
        for (int i = 0; i < colors.length; i++) {
            JsonNode jsonNode = arrayNode.get(i);
            assertEquals(colors[i].toString(), jsonNode.get("name").asText());
        }
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        Schema schema = serializer.getSchema(mapper.getSerializationConfig(), null);
        assertEquals("string", schema.getName());
        ArrayNode enumNode = schema.get("enum");
        assertEquals(3, enumNode.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(Colors.values()[i].name(), enumNode.get(i).asText());
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mapper.schemaFactory().getJsonFormatVisitor(null, null, EnumSerializer.class);
        assertEquals("string", jsonFormatVisitorWrapper.getFormatName());
        JsonStringFormatVisitor jsonStringFormatVisitor = jsonFormatVisitorWrapper.expectStringFormat(null);
        assertEquals(Arrays.asList(Colors.RED.name(), Colors.GREEN.name(), Colors.BLUE.name()), jsonStringFormatVisitor.enumTypes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithInvalidShape() throws JsonProcessingException {
        EnumValues enumValues = new EnumValues(Integer.class, Arrays.asList("One", "Two", "Three"));
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new StringWriter(), null);
        enumSerializer.serialize(null, jsonGenerator, mapper.getSerializationConfig().getDefaultSerializerProvider());
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    static enum Colors {
        RED,
        GREEN,
        BLUE;

        @JsonValue
        public String name() {
            return this.toString();
        }
    }
}