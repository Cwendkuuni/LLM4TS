import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static com.fasterxml.jackson.databind.SerializationFeature.*;
import com.fasterxml.jackson.databind.node.*;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForType(AsPropertyTypeDeserializer.class);
        String json = "{\"typePropertyName\": \"typeId\", \"typeId\": \"TestType\"}";
        JsonNode node = mapper.readTree(json);
        AsPropertyTypeDeserializer deserializer = reader.readValue(node);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }
}