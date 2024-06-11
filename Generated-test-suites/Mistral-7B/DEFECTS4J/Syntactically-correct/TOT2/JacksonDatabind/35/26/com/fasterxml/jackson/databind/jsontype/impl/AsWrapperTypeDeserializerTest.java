import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static com.fasterxml.jackson.databind.SerializationFeature.*;
import static com.fasterxml.jackson.databind.JsonMapping.*;

public class AsWrapperTypeDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(FAIL_ON_EMPTY_OR_NULL_STRING_FIELDS, false);
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        String jsonString = "{\"type\":\"java.lang.String\",\"value\":\"Test String\"}";
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, String.class);
        AsWrapperTypeDeserializer.TypeIdResolver idResolver = new AsWrapperTypeDeserializer.TypeIdResolver() {
            @Override
            public TypeIdResolver findTypeIdResolver(DeserializationContext ctxt, Class<?> type) {
                return null;
            }
        };
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser(new ByteArrayInputStream(jsonString.getBytes()));
        Object result = deserializer.deserializeTypedFromObject(jp, new DeserializationContext(objectMapper, jp, idResolver));
        Assert.assertEquals("Test String", result);
    }
}