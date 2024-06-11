import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"SomeType\", \"value\": 123}");
        DeserializationContext context = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "typePropertyName", true, null, As.PROPERTY);

        // When
        Object result = deserializer.deserializeTypedFromObject(parser, context);

        // Then
        assertEquals("SomeType", result.getClass().getName());
        // Add assertions for other expected edge cases and exceptions
    }
}