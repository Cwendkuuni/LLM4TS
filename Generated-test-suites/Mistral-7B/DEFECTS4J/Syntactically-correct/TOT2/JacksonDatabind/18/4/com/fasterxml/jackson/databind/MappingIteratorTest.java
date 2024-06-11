import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class MappingIteratorTest {
    private MappingIterator<String> iterator;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws IOException {
        mapper = new ObjectMapper();
        String json = "[\"apple\",\"banana\",\"orange\"]";
        Iterator<String> inputIterator = new ArrayIter(new StringReader(json));
        iterator = mapper.readerForListOf(String.class).withContext(new TestingDeserializationContext()).readValues(inputIterator);
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        assertEquals("apple", iterator.next());
        assertEquals("banana", iterator.next());
        assertEquals("orange", iterator.next());
        assertNull(iterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() {
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testClose() throws IOException {
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        iterator.remove();
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> result = iterator.readAll();
        assertEquals(Arrays.asList("apple", "banana", "orange"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException() throws IOException {
        mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonParser parser = mapper.getFactory().createParser("{invalid_json}");
        MappingIterator<String> malformedIterator = mapper.readerForListOf(String.class).readValues(parser);
        malformedIterator.next();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ObjectInputSteam stream = mapper.getFactory().createInputStream(new File("non_existent_file.json"));
        MappingIterator<String> emptyIterator = mapper.readerForListOf(String.class).readValues(stream);
        emptyIterator.next();
        stream.close();
    }
}