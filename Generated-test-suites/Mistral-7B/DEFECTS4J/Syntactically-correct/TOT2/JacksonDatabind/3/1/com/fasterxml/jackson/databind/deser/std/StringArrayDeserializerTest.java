import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class StringArrayDeserializerTest {
    @Test
    public void testDeserialize() throws Exception {
        String jsonString = "[\"Hello\", \"World\", \"Java\"]";
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readValue(jsonString, deserializer);
        assertArrayEquals(new String[]{"Hello", "World", "Java"}, result);
    }
}