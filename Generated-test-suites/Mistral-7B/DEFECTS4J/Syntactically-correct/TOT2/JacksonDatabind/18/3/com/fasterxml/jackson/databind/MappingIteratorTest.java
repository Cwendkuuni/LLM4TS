import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MappingIteratorTest {

    @Test
    public void testHasNextAndNextWithValidIterator() throws IOException {
        // Given: A valid iterator with data
        MappingIterator<String> iterator = new MappingIterator<>(
                new JavaType(String.class),
                new JsonFactory(),
                new StandardDeserializationContext(),
                new StringDeserializer(),
                true,
                null
        );
        String value = "test";
        iterator.nextValue();

        // When: Calling hasNext and next methods
        assertTrue(iterator.hasNext());
        assertEquals(value, iterator.next());

        // Then: Iterator should not have next value and should return false for hasNext
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextAndNextWithEmptyIterator() {
        // Given: An empty iterator
        MappingIterator<String> iterator = MappingIterator.emptyIterator();

        // When: Calling hasNext and next methods
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElementException() throws IOException {
        // Given: A valid iterator with data
        MappingIterator<String> iterator = new MappingIterator<>(
                new JavaType(String.class),
                new JsonFactory(),
                new StandardDeserializationContext(),
                new StringDeserializer(),
                true,
                null
        );
        iterator.nextValue();
        iterator.nextValue();

        // When: Calling next method without calling hasNext first
        iterator.next();
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException() throws IOException {
        // Given: A valid iterator with data
        MappingIterator<String> iterator = new MappingIterator<>(
                new JavaType(String.class),
                new JsonFactory(),
                new StandardDeserializationContext(),
                new StringDeserializer(),
                true,
                null
        );
        JsonParser parser = newJsonParser("{\"invalid_json\":[]}");
        iterator = new MappingIterator<>(
                new JavaType(String.class),
                parser,
                new StandardDeserializationContext(),
                new StringDeserializer(),
                true,
                null
        );

        // When: Deserializing an object with invalid JSON
        iterator.nextValue();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        // Given: A valid iterator with data
        MappingIterator<String> iterator = new MappingIterator<>(
                new JavaType(String.class),
                new JsonFactory(),
                new StandardDeserializationContext(),
                new StringDeserializer(),
                true,
                null
        );
        JsonParser parser = newJsonParser("{\"data\":[]}");
        iterator = new MappingIterator<>(
                new JavaType(String.class),
                parser,
                new StandardDeserializationContext(),
                new StringDeserializer(),
                true,
                null
        );
        parser.close();

        // When: Deserializing an object with a closed parser
        iterator.nextValue();
    }

    private static JsonParser newJsonParser(String json) throws IOException {
        return new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes()));
    }
}