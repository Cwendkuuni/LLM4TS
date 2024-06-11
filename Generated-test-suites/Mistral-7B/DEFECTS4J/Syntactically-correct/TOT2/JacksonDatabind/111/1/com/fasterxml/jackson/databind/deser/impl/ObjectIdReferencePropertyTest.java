import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {
    private ObjectMapper mapper;
    private ObjectIdReferenceProperty property;

    @Before
    public void setup() throws Exception {
        mapper = JsonMapper.builder()
                .findAndRegisterModules()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        property = new ObjectIdReferenceProperty(null, null, null) {
            @Override
            public void fixAccess(DeserializationConfig config) {
                // No-op
            }
        };
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String jsonString = "{\"property\": 123}";
        JsonParser parser = mapper.getFactory().createParser(jsonString);
        Object instance = new Object();

        property.deserializeAndSet(parser, null, instance);
        assertEquals(123, instance.getClass().getField("property").get(instance));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithNullInstance() throws Exception {
        String jsonString = "{\"property\": 123}";
        JsonParser parser = mapper.getFactory().createParser(jsonString);
        Object instance = null;

        property.deserializeAndSet(parser, null, instance);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithNullParser() throws Exception {
        Object instance = new Object();

        property.deserializeAndSet(null, null, instance);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithEmptyJson() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("{}");
        Object instance = new Object();

        property.deserializeAndSet(parser, null, instance);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithUnresolvedReference() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("{\"property\": \"$ref\"}");
        Object instance = new Object();

        property.deserializeAndSet(parser, null, instance);
    }

    @Test
    public void testDeserializeAndSetWithEmptyInstance() throws Exception {
        String jsonString = "{}";
        JsonParser parser = mapper.getFactory().createParser(jsonString);
        Object instance = new Object();

        property.deserializeAndSet(parser, null, instance);
        assertEquals(null, instance.getClass().getField("property").get(instance));
    }
}