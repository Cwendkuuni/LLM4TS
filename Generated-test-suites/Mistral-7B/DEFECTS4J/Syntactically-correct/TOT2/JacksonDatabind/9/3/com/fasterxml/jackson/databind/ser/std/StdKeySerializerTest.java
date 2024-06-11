import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mapper.getSerializerProvider();

        String result = serializer.serialize(date, null, provider).getCharacterStream().readAllBytes()
                .toString();

        JsonParser parser = mapper.getFactory().getParser();
        JsonNode node = parser.readValueAs(JsonNode.class, result);

        assertEquals("\"date\"", node.get("name").asText());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String value = "test";
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mapper.getSerializerProvider();

        String result = serializer.serialize(value, null, provider).getCharacterStream().readAllBytes()
                .toString();

        JsonParser parser = mapper.getFactory().getParser();
        JsonNode node = parser.readValueAs(JsonNode.class, result);

        assertEquals("\"test\"", node.get("name").asText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeInvalidType() {
        StdKeySerializer serializer = new StdKeySerializer();

        assertThrows(IllegalArgumentException.class, () -> serializer.serialize(new Object(), null, null));
    }

    @Test
    public void testGetSchema() {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(null, Object.class);

        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(SerializerProvider.StdioJsonFormatVisitor.class);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);

        assertTrue(visitor.isAccepted());
    }
}