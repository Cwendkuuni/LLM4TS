import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    private ObjectMapper mapper;
    private StdDelegatingSerializer serializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerializeStringToInteger() throws JsonGenerationException, IOException {
        Object input = "123";
        byte[] output = serializer.serialize(input);
        assertEquals(123, mapper.readValue(output, Integer.class));
    }

    @Test
    public void testSerializeStringArrayToList() throws JsonGenerationException, IOException {
        Object input = new String[]{"123", "456", "789"};
        byte[] output = serializer.serialize(input);
        assertEquals(Arrays.asList(123, 456, 789), mapper.readValue(output, List.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNull() {
        serializer.serialize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullArray() {
        serializer = new StdDelegatingSerializer(mapper.getTypeFactory().constructConverter(String[].class, List.class));
        serializer.serialize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullConverter() {
        serializer = new StdDelegatingSerializer(null, mapper.getTypeFactory().constructValueType(List.class, String.class), null);
        serializer.serialize(new Object());
    }

    @Test
    public void testSerializeNullDelegateValue() throws JsonGenerationException, IOException {
        Object input = null;
        byte[] output = serializer.serialize(input);
        assertNull(mapper.readValue(output, Integer.class));
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeDifferentTypes() throws JsonGenerationException, IOException {
        Object input = "abc";
        serializer = new StdDelegatingSerializer(mapper.getTypeFactory().constructConverter(Integer.class, String.class));
        serializer.serialize(input);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingDelegateSerializer() throws JsonGenerationException, IOException {
        Object input = new Object();
        serializer = new StdDelegatingSerializer(mapper.getTypeFactory().constructConverter(Integer.class, Integer.class), null, null);
        serializer.serialize(input);
    }
}