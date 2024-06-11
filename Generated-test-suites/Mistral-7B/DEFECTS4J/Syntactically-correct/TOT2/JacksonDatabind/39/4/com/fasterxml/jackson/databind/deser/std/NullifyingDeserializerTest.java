import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    static class TestContext extends DeserializationContext {
        // Empty implementation
    }
}