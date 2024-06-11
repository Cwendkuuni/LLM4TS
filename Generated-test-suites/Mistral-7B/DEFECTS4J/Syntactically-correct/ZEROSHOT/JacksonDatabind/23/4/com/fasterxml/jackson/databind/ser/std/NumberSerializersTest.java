import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NumberSerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testIntegerSerialization() throws JsonProcessingException {
        JsonSerializer<Integer> integerSerializer = NumberSerializers.findValueSerializer(objectMapper, Integer.class);
        JsonNode node = mapperTest("123", integerSerializer);
        Assert.assertEquals("123", node.asText());
    }

    @Test
    public void testIntegerDeserialization() throws JsonProcessingException {
        JsonParser parser = mapperTest("123", NumberSerializers.IntegerSerializer.instance);
        Assert.assertEquals(123, parser.readValueAs(Integer.class));
    }

    @Test
    public void testShortSerialization() throws JsonProcessingException {
        JsonSerializer<Short> shortSerializer = NumberSerializers.findValueSerializer(objectMapper, Short.class);
        JsonNode node = mapperTest("123", shortSerializer);
        Assert.assertEquals("123", node.asText());
    }

    @Test
    public void testShortDeserialization() throws JsonProcessingException {
        JsonParser parser = mapperTest("123", NumberSerializers.ShortSerializer.instance);
        Assert.assertEquals((short) 123, parser.readValueAs(Short.class));
    }

    @Test
    public void testLongSerialization() throws JsonProcessingException {
        JsonSerializer<Long> longSerializer = NumberSerializers.findValueSerializer(objectMapper, Long.class);
        JsonNode node = mapperTest("12345678901234567890", longSerializer);
        Assert.assertEquals("12345678901234567890", node.asText());
    }

    @Test
    public void testLongDeserialization() throws JsonProcessingException {
        JsonParser parser = mapperTest("12345678901234567890", NumberSerializers.LongSerializer.instance);
        Assert.assertEquals(12345678901234567890L, parser.readValueAs(Long.class));
    }

    @Test
    public void testFloatSerialization() throws JsonProcessingException {
        JsonSerializer<Float> floatSerializer = NumberSerializers.findValueSerializer(objectMapper, Float.class);
        JsonNode node = mapperTest("123.45", floatSerializer);
        Assert.assertEquals("123.45", node.asText());
    }

    @Test
    public void testFloatDeserialization() throws JsonProcessingException {
        JsonParser parser = mapperTest("123.45", NumberSerializers.FloatSerializer.instance);
        Assert.assertEquals(123.45f, parser.readValueAs(Float.class));
    }

    @Test
    public void testDoubleSerialization() throws JsonProcessingException {
        JsonSerializer<Double> doubleSerializer = NumberSerializers.findValueSerializer(objectMapper, Double.class);
        JsonNode node = mapperTest("123.456789", doubleSerializer);
        Assert.assertEquals("123.456789", node.asText());
    }

    @Test
    public void testDoubleDeserialization() throws JsonProcessingException {
        JsonParser parser = mapperTest("123.456789", NumberSerializers.DoubleSerializer.instance);
        Assert.assertEquals(123.456789, parser.readValueAs(Double.class));
    }

    private JsonNode mapperTest(String jsonString, JsonSerializer<?> serializer)
            throws JsonProcessingException {
        return objectMapper.writeValueAsTree(jsonString).traverse(new JsonNode() {
            @Override
            public <T> T accept(JsonNodeVisitor<T> visitor) {
                return visitor.visitObject(this);
            }

            @Override
            public void serialize(SerializerProvider serializerProvider, TypeSerializer typeSerializer)
                    throws IOException {
                serializer.serializeValues(this, new JsonGenerator(serializerProvider.getFactory(), this), serializerProvider);
            }

            @Override
            public JsonNode traverse(JsonNodeVisitor<?> visitor) {
                return visitor.visitContainerNode(this);
            }
        }, serializer);
    }

    private JsonParser mapperTest(String jsonString, JsonSerializer<?> serializer) throws JsonProcessingException {
        ObjectReader reader = objectMapper.readerFor(Object.class);
        return reader.readValue(new ByteArrayInputStream(jsonString.getBytes()));
    }
}