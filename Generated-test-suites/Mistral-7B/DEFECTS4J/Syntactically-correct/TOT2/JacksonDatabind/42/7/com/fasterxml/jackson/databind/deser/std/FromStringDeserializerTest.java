import org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;

public class FromStringDeserializerTest {

    private static final FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);

    @Test
    public void testDeserializeSuccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String jsonString = "\"Test String\"";
        JsonNode jsonNode = mapper.readTree(jsonString);

        String result = stringDeserializer.deserialize(JsonFactory.instance.createParser(jsonNode), new DeserializationContext.EmptyContext());
        assertEquals("Test String", result);
    }
}