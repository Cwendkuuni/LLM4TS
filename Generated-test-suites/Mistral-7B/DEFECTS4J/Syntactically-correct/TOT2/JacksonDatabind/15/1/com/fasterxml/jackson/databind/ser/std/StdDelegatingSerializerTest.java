import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class StdDelegatingSerializerTest {

    @Test
    public void testStandardSerialization() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter());
        JsonGenerator gen = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig());
        ObjectMapper mapper = new ObjectMapper();

        // Test serializing an object using the StdDelegatingSerializer
        String input = "{\"input\": \"test\"}";
        Object obj = mapper.readValue(input, String.class);
        serializer.serialize(obj, gen, new SerializerProvider());
        gen.flush();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(gen.getOutputByteArray());
        String output = mapper.readValue(inputStream, String.class);
        assertEquals(input, output);
    }

    @Test
    public void testConversion() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new StringToIntegerConverter(), null, null);
        JsonGenerator gen = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig());
        ObjectMapper mapper = new ObjectMapper();

        // Test conversion with a valid input
        int input = 5;
        serializer.serialize(input, gen, new SerializerProvider());
        gen.flush();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(gen.getOutputByteArray());
        int output = mapper.readValue(inputStream, Integer.class);
        assertEquals(input, output);

        // Test conversion with a null input
        serializer.serialize(null, gen, new SerializerProvider());
        gen.flush();
        ByteArrayInputStream nullInputStream = new ByteArrayInputStream(gen.getOutputByteArray());
        assertNull(mapper.readValue(nullInputStream, Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeMismatch() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new StringToIntegerConverter(), Integer.class, null);
        JsonGenerator gen = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig());

        // Test that trying to serialize a String to an Integer serializer will throw an exception
        String input = "test";
        serializer.serialize(input, gen, new SerializerProvider());
    }
}