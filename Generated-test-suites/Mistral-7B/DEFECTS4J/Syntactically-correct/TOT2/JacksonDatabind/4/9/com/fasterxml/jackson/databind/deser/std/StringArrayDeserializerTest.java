import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeValidJsonStringArray() throws IOException {
        String jsonString = "[\"hello\", \"world\", \"test\"]";
        String[] expected = {"hello", "world", "test"};
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = new JsonFactory().createParser(jsonString);
        String[] result = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
        Assert.assertArrayEquals(expected, result);
    }
}