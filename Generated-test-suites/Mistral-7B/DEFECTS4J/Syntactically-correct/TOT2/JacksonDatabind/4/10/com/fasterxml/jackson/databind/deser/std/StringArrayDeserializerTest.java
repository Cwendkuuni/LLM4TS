import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.json.JsonParser;
import org.testng.annotations.Test;
import static org.junit.Assert.assertArrayEquals;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeStandard() throws IOException {
        StringJsonParser jp = new StringJsonParser("[\"hello\", \"world\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        StringJsonParser jp = new StringJsonParser("[null, \"hello\", \"world\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"null", "hello", "world"}, result);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        StringJsonParser jp = new StringJsonParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testDeserializeNullInput() throws IOException {
        StringJsonParser jp = new StringJsonParser("null");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeMissingStartArrayToken() throws IOException {
        StringJsonParser jp = new StringJsonParser("hello, world");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserialize(jp, new TestContext());
            fail("Expected deserialization exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeNonStringElement() throws IOException {
        StringJsonParser jp = new StringJsonParser("[1, \"hello\", 3.14]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserialize(jp, new TestContext());
            fail("Expected deserialization exception");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeOverflow() throws IOException {
        StringJsonParser jp = new StringJsonParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        ObjectBuffer buffer = new ObjectBuffer(1);
        String[] result = deserializer.deserialize(jp, new TestContext());
        deserializer.handleNonArray(jp, new TestContext());
        StringJsonParser jp2 = new StringJsonParser("[\"element1\", \"element2\", \"element3\", \"element4\", \"element5\", \"element6\", \"element7\", \"element8\", \"element9\", \"element10\", \"element11\", \"element12\", \"element13\", \"element14\", \"element15\", \"element16\", \"element17\", \"element18\", \"element19\", \"element20\"]");
        String[] result2 = deserializer.deserialize(jp2, new TestContext());
        ObjectBuffer buffer2 = (ObjectBuffer) jp2.getContext().getDeserializationContext().findActiveObjectBuffer();
        assertEquals(buffer2.size(), 21);
        assertArrayEquals(new String[]{"element1", "element2", "element3", "element4", "element5", "element6", "element7", "element8", "element9", "element10", "element11", "element12", "element13", "element14", "element15", "element16", "element17", "element18", "element19", "element20"}, result2);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper _mapper() {
            return new ObjectMapper();
        }

        @Override
        public JsonFactory _factory() {
            return new JsonFactory();
        }
    }

    private static class StringJsonParser extends StringParserBase {
        public StringJsonParser(String json) {
            super(new JsonFactory().createParser(json));
        }
    }
}