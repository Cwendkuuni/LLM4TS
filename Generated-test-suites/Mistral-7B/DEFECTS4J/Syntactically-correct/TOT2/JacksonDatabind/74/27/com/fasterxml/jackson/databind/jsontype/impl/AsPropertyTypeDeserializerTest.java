import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typePropertyName\": \"StringType\", \"value\": \"test\"}";
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typePropertyName",
                true,
                mapper.getTypeFactory().constructType(String.class.getName()),
                As.PROPERTY);
        JsonParser jp = mapper.getFactory().createParser(json);

        assertEquals("test", deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeTypedFromObjectWithNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": \"test\"}";
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typePropertyName",
                true,
                mapper.getTypeFactory().constructType(String.class.getName()),
                As.PROPERTY);
        JsonParser jp = mapper.getFactory().createParser(json);

        assertNull(deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeTypedFromObjectWithInvalidTypePropertyName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"invalidTypePropertyName\": \"StringType\", \"value\": \"test\"}";
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typePropertyName",
                true,
                mapper.getTypeFactory().constructType(String.class.getName()),
                As.PROPERTY);
        JsonParser jp = mapper.getFactory().createParser(json);

        mapper.getDeserializationContext().reportBadTypeIdException("Invalid Type Property Name Exception", jp);
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingTypePropertyName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": \"test\"}";
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typePropertyName",
                true,
                mapper.getTypeFactory().constructType(String.class.getName()),
                As.PROPERTY);
        JsonParser jp = mapper.getFactory().createParser(json);

        mapper.getDeserializationContext().reportWrongTokenException(jp, JsonToken.FIELD_NAME, "missing property 'typePropertyName' that is to contain type id  (for class String)");
    }
}