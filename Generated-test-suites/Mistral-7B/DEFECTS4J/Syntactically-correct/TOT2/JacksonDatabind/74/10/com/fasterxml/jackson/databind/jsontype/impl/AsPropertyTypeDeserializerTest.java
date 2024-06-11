import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;

public class AsPropertyTypeDeserializerTest {
    @Test
    public void testDeserializeTypedFromObject_WithPropertyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typePropertyName\":\"StringType\",\"value\":\"Test\"}";
        AsPropertyTypeDeserializer deserializer = mapper.getTypeFactory()
                .constructType(AsPropertyTypeDeserializer.class, String.class, new TypeIdResolverBase(), "typePropertyName", true, String.class);
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
        assertEquals("Test", result);
    }

    @Test
    public void testDeserializeTypedFromObject_WithMissingPropertyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"Test\"}";
        AsPropertyTypeDeserializer deserializer = mapper.getTypeFactory()
                .constructType(AsPropertyTypeDeserializer.class, String.class, new TypeIdResolverBase(), "typePropertyName", true, String.class);
        JsonParser jp = mapper.getFactory().createParser(json);
        assertNull(deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeTypedFromObject_WithNullTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typePropertyName\":null,\"value\":\"Test\"}";
        AsPropertyTypeDeserializer deserializer = mapper.getTypeFactory()
                .constructType(AsPropertyTypeDeserializer.class, String.class, new TypeIdResolverBase(), "typePropertyName", true, String.class);
        JsonParser jp = mapper.getFactory().createParser(json);
        assertNull(deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeTypedFromObject_WithEmptyTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typePropertyName\":\"\",\"value\":\"Test\"}";
        AsPropertyTypeDeserializer deserializer = mapper.getTypeFactory()
                .constructType(AsPropertyTypeDeserializer.class, String.class, new TypeIdResolverBase(), "typePropertyName", true, String.class);
        JsonParser jp = mapper.getFactory().createParser(json);
        assertNull(deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext()));
    }

    // Complete test file goes here
}