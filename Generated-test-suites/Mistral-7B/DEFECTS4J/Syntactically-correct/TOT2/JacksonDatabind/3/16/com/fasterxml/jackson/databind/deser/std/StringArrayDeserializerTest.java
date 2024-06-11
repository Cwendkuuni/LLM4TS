import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("[\"apple\", \"banana\", \"orange\"]");
        String[] result = deserializer.deserialize(jsonParser, null);
        assertEquals(3, result.length);
        assertEquals("apple", result[0]);
        assertEquals("banana", result[1]);
        assertEquals("orange", result[2]);
    }
}