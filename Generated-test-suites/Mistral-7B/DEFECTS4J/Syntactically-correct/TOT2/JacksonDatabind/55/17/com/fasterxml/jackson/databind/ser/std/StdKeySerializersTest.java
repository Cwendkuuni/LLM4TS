import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertEquals;

public class StdKeySerializersTest {

    @Test
    public void testDefaultKeySerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(mapper.getSerializationConfig(), Object.class, true);
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializationConfig());

        Object value = new Integer(1);
        serializer.serialize(value, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
        String expected = "1";
        String actual = generator.getOutputContentAsString();
        assertEquals(expected, actual);

        generator.close();
    }

    @Test
    public void testStringKeySerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = StdKeySerializers.DEFAULT_STRING_SERIALIZER;
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializationConfig());

        Object value = "test";
        serializer.serialize(value, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
        String expected = "test";
        String actual = generator.getOutputContentAsString();
        assertEquals(expected, actual);

        generator.close();
    }

    // Add test cases for other methods and edge cases here
}