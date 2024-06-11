import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class JsonMappingExceptionTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBasicJsonMappingException() {
        String json = "{\"name\": \"John\", \"age\": 30}";
        try {
            mapper.readValue(json, String.class);
        } catch (JsonMappingException e) {
            Assert.assertEquals("Expected JsonMappingException, but got: " + e.getClass().getName(),
                    JsonMappingException.class, e.getClass());
            Assert.assertNotNull(e.getPath());
            return;
        }
        Assert.fail("Expected JsonMappingException but no exception was thrown.");
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        String json = "[1, 2, 3, {name: \"John\"}]";
        try {
            mapper.readValue(json, String[].class);
        } catch (JsonMappingException e) {
            Assert.assertEquals("Expected JsonMappingException, but got: " + e.getClass().getName(),
                    JsonMappingException.class, e.getClass());
            List<Reference> path = e.getPath();
            Assert.assertEquals(4, path.size());
            Assert.assertEquals("name", path.get(path.size() - 1).getFieldName());
            return;
        }
        Assert.fail("Expected JsonMappingException but no exception was thrown.");
    }

    @Test
    public void testJsonMappingExceptionWithRootCause() {
        String json = "{\"name\": \"John\", \"age\": [\"three\", 30]}";
        try {
            mapper.readValue(json, Person.class);
        } catch (JsonMappingException e) {
            Assert.assertEquals("Expected JsonMappingException, but got: " + e.getClass().getName(),
                    JsonMappingException.class, e.getClass());
            Throwable rootCause = e.getRootCause();
            Assert.assertEquals("Expected IOException, but got: " + rootCause.getClass().getName(),
                    IOException.class, rootCause.getClass());
            return;
        }
        Assert.fail("Expected JsonMappingException but no exception was thrown.");
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionThrown() {
        String json = "{\"name\": [\"John\", 30]}";
        mapper.readValue(json, String.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionThrownWithPath() {
        String json = "[1, 2, {name:null, age:30}]";
        mapper.readValue(json, int[].class);
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionThrownWithRootCause() {
        String json = "{\"name\": \"John\", \"age\": [\"three\", 30]}";
        mapper.readValue(json, Person.class);
    }

    static class Person {
        private String name;
        private int age;
    }
}