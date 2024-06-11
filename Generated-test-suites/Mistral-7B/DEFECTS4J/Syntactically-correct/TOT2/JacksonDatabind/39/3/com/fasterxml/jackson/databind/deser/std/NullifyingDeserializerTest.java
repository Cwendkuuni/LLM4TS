import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

public class NullifyingDeserializerTest {

    @Test
    public void testSerializeNullifyingDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"someKey\": \"someValue\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(jsonParser, null);
        assertNull(result);
    }
}