import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testValidJsonMapping() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"age\": 30}";
        Map<String, Object> map = mapper.readValue(json, Map.class);
        assertEquals("John", map.get("name"));
        assertEquals(30, map.get("age"));
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"age\": [\"invalid\"]}";
        try {
            Map<String, Object> map = mapper.readValue(json, Map.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            List<Reference> path = e.getPath();
            assertEquals(1, path.size());
            Reference ref = path.get(0);
            assertEquals("[age]", ref.getDescription());
        }
    }

    @Test
    public void testJsonMappingExceptionWithRootCause() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"invalid\": [1]}";
        try {
            Map<String, Object> map = mapper.readValue(json, Map.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Throwable rootCause = e.getRootCause();
            assertTrue(rootCause instanceof JsonParseException);
            String message = rootCause.getMessage();
            assertTrue(message.contains("Cannot deserialize value of type java.util.ArrayList from ValueToken"));
        }
    }

    @Test
    public void testJsonMappingExceptionWithMaxRefsLimit() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"age\": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70]}";
        try {
            Map<String, Object> map = mapper.readValue(json, Map.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            List<Reference> path = e.getPath();
            assertEquals(2, path.size());
            Reference ref1 = path.get(0);
            assertEquals("[age]", ref1.getDescription());
            Reference ref2 = path.get(1);
            assertEquals("2", ref2.getIndex());
        }
    }
}