import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.junit.SerializerTestBase;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest extends SerializerTestBase {

    @Test
    public void testSerializeDate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Date date = new Date();

        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = getProvider();

        String result = mapper.writeValueAsString(new Object() {
            private Date key = date;
        });

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode node = parser.readValueAsTree();
        assertEquals("\"date\":" + date.toISO8601String() + "\"", node.toString());
    }

    @Test
    public void testSerializeString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String key = "test";

        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = getProvider();

        String result = mapper.writeValueAsString(new Object() {
            private String key = key;
        });

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode node = parser.readValueAsTree();
        assertEquals("\"test\"", node.toString());
    }

    @Test
    public void testSerializeNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = getProvider();

        String result = mapper.writeValueAsString(null);

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode node = parser.readValueAsTree();
        assertEquals("null", node.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = getProvider();

        String result = mapper.writeValueAsString(new Object());

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode node = parser.readValueAsTree();

        // The serializer should not be able to serialize an invalid object
        parser.nextValue();
        fail("Unexpectedly parsed valid JSON");
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode schema = serializer.getSchema(getProvider(), Object.class);

        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        ObjectNode node = mapper.createObjectNode();
        serializer.acceptJsonFormatVisitor(serializer.getProvider().getFormatVisitor(node, Object.class), Object.class);

        assertEquals("StringSerializers.StringSerializer", node.get("serializer").asText());
    }
}