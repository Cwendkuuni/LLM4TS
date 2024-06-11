import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.type.*;

public class MappingIteratorTest {

    @Test
    public void testEmptyIterator() throws Exception {
        MappingIterator<String> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testUnexpectedToken() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\"}");
        MappingIterator<String> iterator = new MappingIterator<>(
            TypeFactory.defaultInstance().constructCollectionType(List.class, String.class),
            parser,
            mapper.getDeserializationContext(),
            new StdDeserializer<String>() {},
            true,
            null
        );
        assertFalse(iterator.hasNext());
        parser.nextToken(); // Clear the unexpected token
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testCloseParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<>(
            TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class),
            parser,
            mapper.getDeserializationContext(),
            new StdDeserializer<Integer>() {},
            true,
            null
        );

        // Test that the parser is closed when the iterator is closed
        iterator.close();
        assertFalse(parser.isOpen());

        // Test that the iterator throws an exception if the parser is already closed
        iterator = new MappingIterator<>(
            TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class),
            parser,
            mapper.getDeserializationContext(),
            new StdDeserializer<Integer>() {},
            true,
            null
        );
        iterator.close();
        iterator = new MappingIterator<>(
            TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class),
            parser,
            mapper.getDeserializationContext(),
            new StdDeserializer<Integer>() {},
            true,
            null
        );
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("null");
        MappingIterator<String> iterator = new MappingIterator<>(
            TypeFactory.defaultInstance().constructCollectionType(List.class, String.class),
            parser,
            mapper.getDeserializationContext(),
            new StdDeserializer<String>() {},
            true,
            null
        );
        assertEquals(null, iterator.next());
    }

    @Test
    public void testArraysAndLists() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, \"three\", 4, [5, 6], null]");
        MappingIterator<Object> iterator = new MappingIterator<>(
            TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Object.class),
            parser,
            mapper.getDeserializationContext(),
            new StdDeserializer<Object>() {},
            true,
            null
        );

        ArrayList<Object> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        assertEquals(6, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals("three", result.get(3));
        assertEquals(4, result.get(4));
        assertNotNull(result.get(5));
    }

    @Test
    public void testHasNextValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<>(
            TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class),
            parser,
            mapper.getDeserializationContext(),
            new StdDeserializer<Integer>() {},
            true,
            null
        );

        assertFalse(iterator.hasNextValue());

        parser.nextToken(); // Clear the unexpected token
        assertTrue(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<>(
            TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class),
            parser,
            mapper.getDeserializationContext(),
            new StdDeserializer<Integer>() {},
            true,
            null
        );

        assertEquals(0, iterator.nextValue().intValue());
    }
}