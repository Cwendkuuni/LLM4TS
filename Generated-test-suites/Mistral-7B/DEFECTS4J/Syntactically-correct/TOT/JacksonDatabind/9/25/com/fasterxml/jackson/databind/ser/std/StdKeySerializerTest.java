import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeySerializerTest {

    private ObjectMapper objectMapper = JsonMapperFactory.getMapper();

    @Test
    public void testSerializeDate() throws IOException {
        Date date = new Date();

        String json = "{\"e\":\"@\"}"; // empty JSON object, representing the map that will be serialized
        JsonGenerator jgen = objectMapper.getFactory().createGenerator(System.out);
        jgen.writeStartObject();
        jgen.writeFieldName("e");
        jgen.writeNull();
        jgen.writeFieldName("k");
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(date, jgen, new SerializerProvider() {
            @Override
            public void defaultSerializeDateKey(Date date, JsonGenerator sg) throws IOException {
                sg.writeString(date.toString());
            }
        });
        jgen.writeEndObject();

        // verify the serialized JSON
        ObjectNode jsonNode = objectMapper.readValue(System.in, ObjectNode.class);
        String actualDate = jsonNode.get("k").asText();
        String expectedDate = date.toString();
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testSerializeString() throws IOException {
        String key = "testKey";

        String json = "{\"e\":\"testValue\", \"k\":\"testKey\"}";
        ObjectNode jsonNode = objectMapper.readValue(json.getBytes(), ObjectNode.class);

        StdKeySerializer serializer = new StdKeySerializer();
        String actualSerializedKey = serializer.serializeToString(key, new SerializerProvider() {});
        String expectedSerializedKey = jsonNode.get("k").asText();

        assertEquals(expectedSerializedKey, actualSerializedKey);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode schema = serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance().constructType(Map.class));

        assertEquals("string", schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode expectedSchema = objectMapper.createObjectNode().put("type", "string");

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {
            @Override
            public void visitStringFormat(Type typeHint, JsonSchema schema) {
                JsonNode actualSchema = serializer.acceptJsonFormatVisitor(this, new JavaType(Map.class));
                assertEquals(expectedSchema.toString(), actualSchema.toString());
            }
        };

        serializer.acceptJsonFormatVisitor(visitor, new JavaType(Map.class));
    }

    @Test
    public void testNullValue() throws IOException {
        Map<String, Object> map = Map.of("k", null);

        String json = map.toString();
        ObjectNode jsonNode = objectMapper.readValue(json.getBytes(), ObjectNode.class);

        StdKeySerializer serializer = new StdKeySerializer();
        String actualSerializedNullValue = serializer.serializeToString(null, new SerializerProvider());
        String expectedSerializedNullValue = jsonNode.get("k").asText();

        assertNull(actualSerializedNullValue);
        assertNull(expectedSerializedNullValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testNonStringValue() throws IOException {
        Number number = 123;

        String json = "{\"e\":\"testValue\", \"k\":\"testKey\", \"v\":\"testValue\"}";
        ObjectNode jsonNode = objectMapper.readValue(json.getBytes(), ObjectNode.class);

        StdKeySerializer serializer = new StdKeySerializer();
        String actualSerializedKey = serializer.serializeToString(number, new SerializerProvider());

        fail("Expected IllegalStateException but got: " + actualSerializedKey);
    }
}