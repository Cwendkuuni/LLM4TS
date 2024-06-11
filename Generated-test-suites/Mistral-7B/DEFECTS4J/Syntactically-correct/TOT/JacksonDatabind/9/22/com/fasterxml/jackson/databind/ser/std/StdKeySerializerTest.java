import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TestUtil;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeySerializerTest extends BaseMapTest {

    // Serialization tests

    @org.junit.Test
    public void testSerializeDate() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Date> map = new HashMap<>();
        map.put("testKey", new Date());
        String json = mapper.writeValueAsString(map);

        StdKeySerializer serializer = new StdKeySerializer();
        ObjectWriter writer = mapper.writerWithView(serializer);
        String serialized = writer.writeValueAsString(new Date());

        assertEquals(json, serialized);
    }

    @org.junit.Test
    public void testSerializeString() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("testKey", "testValue");
        String json = mapper.writeValueAsString(map);

        StdKeySerializer serializer = new StdKeySerializer();
        ObjectWriter writer = mapper.writerWithView(serializer);
        String serialized = writer.writeValueAsString("testValue");

        assertEquals(json, serialized);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testSerializeInvalidType() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Number> map = new HashMap<>();
        map.put("testKey", 1);

        StdKeySerializer serializer = new StdKeySerializer();
        ObjectWriter writer = mapper.writerWithView(serializer);
        writer.writeValueAsString(map);
    }

    // Deserialization tests

    @org.junit.Test
    public void testDeserializeDate() throws JsonGenerationException, JsonMappingException, IOException {
        String json = "{\"testKey\": \"2022-01-01T00:00:00.000Z\"}";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Date> map = mapper.readValue(json, TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Date.class));

        assertEquals(new Date(), map.get("testKey"));
    }

    @org.junit.Test
    public void testDeserializeString() throws JsonGenerationException, JsonMappingException, IOException {
        String json = "{\"testKey\": \"testValue\"}";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(json, TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, String.class));

        assertEquals("testValue", map.get("testKey"));
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws JsonGenerationException, JsonMappingException, IOException {
        String json = "{\"testKey\": 1}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        Map<String, Date> map = mapper.readValue(json, TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Date.class));
    }

    // Schemas tests

    @org.junit.Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode schema = serializer.getSchema(new SerializerProvider(), Object.class);

        assertEquals("string", schema.asText());
    }

    // Visitor tests

    @org.junit.Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.acceptJsonFormatVisitor(null, Object.class);
    }

    // Error handling tests

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(null, null, null);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(new Date(), null, null);
    }

    @org.junit.Test(expected = IOException.class)
    public void testIOException() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(new Date(), new JsonGenerator(){
            @Override
            public void writeFieldName(String fieldName) throws IOException {
                throw new IOException("Test IOException");
            }
        }, null);
    }
}