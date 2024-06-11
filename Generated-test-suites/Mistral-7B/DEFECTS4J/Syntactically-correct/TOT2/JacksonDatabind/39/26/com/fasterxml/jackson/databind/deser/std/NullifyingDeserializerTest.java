import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"age\": 30}";
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(json, deserializer.getType());
        assertNull(result);
    }
}