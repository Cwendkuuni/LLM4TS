import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MappingIteratorTest {

    @Test
    public void testMappingIterator_BasicFunctionality() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(String.class);
        JsonNode input = new ObjectMapper().readTree("['hello', 'world']");
        Iterator<String> iterator = reader.readValues(new JsonFactory(), input);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("hello", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("world", iterator.next());
        Assert.assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test
    public void testMappingIterator_EdgeCases() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(String.class);
        JsonNode input = new ObjectMapper().readTree("[]");
        Iterator<String> iterator = reader.readValues(new JsonFactory(), input);

        Assert.assertFalse(iterator.hasNext());

        iterator.close();

        input = new ObjectMapper().readTree("{'data':['hello', 'world']}");
        iterator = reader.readValues(new JsonFactory(), input.path("data"));

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("hello", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("world", iterator.next());
        Assert.assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test
    public void testMappingIterator_ExceptionHandling() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(String.class);
        JsonNode input = new ObjectMapper().readTree("{'data':[1,2,3]}");
        Iterator<String> iterator = reader.readValues(new JsonFactory(), input);

        Assert.assertFalse(iterator.hasNext());

        try {
            iterator.next();
        } catch (JsonMappingException e) {
            Assert.assertEquals(Reference.FIELD, e.getReferenceType());
            Assert.assertEquals("data", e.getPath());
            Assert.assertEquals("Not a valid JSON Array", e.getMessage());
        }

        iterator.close();
    }
}