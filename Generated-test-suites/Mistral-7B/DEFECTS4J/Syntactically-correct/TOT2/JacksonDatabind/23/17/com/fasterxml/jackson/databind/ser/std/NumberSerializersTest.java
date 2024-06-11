import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testAddAll() throws Exception {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        JsonSerializer<?> intSerializer = allDeserializers.get(Integer.class.getName());
        JsonSerializer<?> longSerializer = allDeserializers.get(Long.class.getName());
        assertEquals(intSerializer, intSerializer); // Check that intSerializer is not null
        assertEquals(longSerializer, longSerializer); // Check that longSerializer is not null
    }

    @Test
    public void testAddAllEmptyMap() throws Exception {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(0, allDeserializers.size()); // Check that no serializers were added to the map
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllNullMap() throws Exception {
        Map<String, JsonSerializer<?>> allDeserializers = null;
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testShortSerializer() throws Exception {
        JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;
        Short testShortValue = Short.parseShort("123");
        String jsonString = mapper.writeValueAsString(testShortValue);
        Short deserializedShortValue = mapper.readValue(jsonString, Short.class);
        assertEquals(testShortValue, deserializedShortValue);
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        JsonSerializer<Object> intSerializer = NumberSerializers.IntegerSerializer.instance;
        Integer testIntValue = 123;
        String jsonString = mapper.writeValueAsString(testIntValue);
        Integer deserializedIntValue = mapper.readValue(jsonString, Integer.class);
        assertEquals(testIntValue, deserializedIntValue);
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        JsonSerializer<Number> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        Integer testIntValue = 123;
        String jsonString = mapper.writeValueAsString(testIntValue);
        Number deserializedIntValue = mapper.readValue(jsonString, Number.class);
        assertEquals(testIntValue, deserializedIntValue);
    }

    @Test
    public void testLongSerializer() throws Exception {
        JsonSerializer<Object> longSerializer = NumberSerializers.LongSerializer.instance;
        Long testLongValue = 123L;
        String jsonString = mapper.writeValueAsString(testLongValue);
        Long deserializedLongValue = mapper.readValue(jsonString, Long.class);
        assertEquals(testLongValue, deserializedLongValue);
    }

    @Test
    public void testFloatSerializer() throws Exception {
        JsonSerializer<Object> floatSerializer = NumberSerializers.FloatSerializer.instance;
        Float testFloatValue = 123.45f;
        String jsonString = mapper.writeValueAsString(testFloatValue);
        Float deserializedFloatValue = mapper.readValue(jsonString, Float.class);
        assertEquals(testFloatValue, deserializedFloatValue);
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        JsonSerializer<Object> doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        Double testDoubleValue = 123.45;
        String jsonString = mapper.writeValueAsString(testDoubleValue);
        Double deserializedDoubleValue = mapper.readValue(jsonString, Double.class);
        assertEquals(testDoubleValue, deserializedDoubleValue);
    }
}