import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter(), mapper.getTypeFactory().constructType(String.class), mapper.findValueSerializer(String.class));
        String input = "Hello World";
        byte[] expectedOutput = "\"Hello World\"".getBytes();

        byte[] output = serializer.serialize(input, new ByteArrayOutputStream(), new JsonFactory(), mapper.getSerializerProvider());

        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testSerializeNull() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter(), mapper.getTypeFactory().constructType(String.class), mapper.findValueSerializer(String.class));

        byte[] output = serializer.serialize(null, new ByteArrayOutputStream(), new JsonFactory(), mapper.getSerializerProvider());

        assertTrue(output.length == 0);
    }

    @Test
    public void testSerializeEmptyString() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter(), mapper.getTypeFactory().constructType(String.class), mapper.findValueSerializer(String.class));
        byte[] expectedOutput = "\"\"".getBytes();

        byte[] output = serializer.serialize("", new ByteArrayOutputStream(), new JsonFactory(), mapper.getSerializerProvider());

        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testSerializeLargeString() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter(), mapper.getTypeFactory().constructType(String.class), mapper.findValueSerializer(String.class));
        String largeInput = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        byte[] expectedOutput = "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\"".getBytes();

        byte[] output = serializer.serialize(largeInput, new ByteArrayOutputStream(), new JsonFactory(), mapper.getSerializerProvider());

        assertArrayEquals(expectedOutput, output);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeSpecialChars() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter(), mapper.getTypeFactory().constructType(String.class), mapper.findValueSerializer(String.class));
        String input = "Hello \\ World";

        serializer.serialize(input, new ByteArrayOutputStream(), new JsonFactory(), mapper.getSerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeException() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter(), mapper.getTypeFactory().constructType(String.class), mapper.findValueSerializer(String.class));

        try {
            serializer.serialize(new IllegalArgumentException("Test Exception"), new ByteArrayOutputStream(), new JsonFactory(), mapper.getSerializerProvider());
        } catch (JsonProcessingException e) {
            throw new JsonMappingException("Unexpected exception", e);
        }
    }
}