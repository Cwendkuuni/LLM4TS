import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        JsonParser parser = new JsonFactory().createParser("[\"apple\", \"banana\", \"cherry\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
        String[] expected = {"apple", "banana", "cherry"};
        assertArrayEquals(expected, result);
    }
}