import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = createParser("[\"hello\", \"world\", \"test\"]");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals(Arrays.asList("hello", "world", "test").toArray(new String[0]), result);
    }
}