import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class StringArrayDeserializerTest {

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        jsonFactory = mapper.getFactory();
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        String jsonString = "[\"String1\", \"String2\", \"String3\"]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"String1", "String2", "String3"};

        String[] actual = deserializer.deserialize(jsonFactory.createParser(jsonString), new TestingDeserializationContext());

        assertArrayEquals(expected, actual);
    }
}