import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {
    @Test
    public void testSerialize_DefaultConstructor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = mapper.findValueSerializer(String.class);

        String input = "Hello World";
        String output = "\"Hello World\"";

        String serialized = serializer.serialize(input, new JsonGenerator(null), new SerializerProvider());
        assertEquals(output, serialized);
    }

    @Test
    public void testSerialize_ConstructorWithConverter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<String, Integer> converter = mapper.getFactory().getConverter(String.class, Integer.class);
        StdDelegatingSerializer<Integer> serializer = new StdDelegatingSerializer<>(converter, mapper.getTypeFactory().constructType(Integer.class), null);

        String input = "123";
        Integer output = 123;

        Integer serialized = serializer.serialize(input, new JsonGenerator(null), new SerializerProvider());
        assertEquals(output.toString(), serialized.toString());
    }

    @Test
    public void testSerialize_ConstructorWithDelegateSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Integer> delegateSerializer = mapper.findValueSerializer(Integer.class);
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(null, null, delegateSerializer);

        Integer input = 123;
        Integer output = 123;

        Integer serialized = serializer.serialize(input, new JsonGenerator(null), new SerializerProvider());
        assertEquals(output, serialized);
    }

    @Test(expected = IllegalStateException.class)
    public void testSerialize_SubclassDoesNotOverrideWithDelegate() throws JsonProcessingException {
        class TestSerializer extends StdDelegatingSerializer<Object> {
            // Do not override 'withDelegate'
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Integer> delegateSerializer = mapper.findValueSerializer(Integer.class);
        TestSerializer serializer = new TestSerializer(null, null, delegateSerializer);

        Integer input = 123;
        serializer.serialize(input, new JsonGenerator(null), new SerializerProvider());
    }
}