import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private JsonNode validInput;
    private MappingIterator<String> mappingIterator;

    @Before
    public void setUp() throws IOException {
        mapper = new ObjectMapper();
        validInput = mapper.readTree("[\"apple\", \"banana\", \"cherry\"]");
    }

    @Test
    public void testHasNextAndNextMethods() throws IOException {
        JsonNode rootNode = mapper.readTree(validInput);
        mappingIterator = rootNode.elements();

        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertEquals("apple", mappingIterator.next());
        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertEquals("banana", mappingIterator.next());
        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertEquals("cherry", mappingIterator.next());
        Assert.assertFalse(mappingIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElementException() throws IOException {
        JsonNode rootNode = mapper.readTree(validInput);
        mappingIterator = rootNode.elements();

        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next();

        mappingIterator.next(); // Should throw NoSuchElementException
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String invalidJson = "{ \"invalid\": \"json\" }";
        JsonParser jsonParser = objectMapper.getFactory().createParser(invalidJson);
        MappingIterator<String> mappingIterator = new MappingIterator<>(null, jsonParser, null, null, true, null);

        mappingIterator.next(); // Should throw IOException
    }

    @Test
    public void testReadAllMethod() throws IOException {
        JsonNode rootNode = mapper.readTree(validInput);
        mappingIterator = rootNode.elements();

        List<String> list = new ArrayList<>();
        mappingIterator.readAll(list);

        Assert.assertEquals(3, list.size());
        Assert.assertEquals("apple", list.get(0));
        Assert.assertEquals("banana", list.get(1));
        Assert.assertEquals("cherry", list.get(2));
    }
}