import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testNullifyingDeserializer_deserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        NullifyingDeserializer deserializer = new NullifyingDeserializer();

        Object result = deserializer.deserialize(parser, new DeserializationContext(null, null, null));

        assertNull(result);
    }
}