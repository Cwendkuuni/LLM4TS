import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"typePropertyName\": \"MyType\", \"value\": 123}");
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "typePropertyName", true, null);
        Object result = deserializer.deserializeTypedFromObject(mapper.treeToJsonParser(jsonNode), new TestContext());
        assertEquals("123", result.toString());
    }
}