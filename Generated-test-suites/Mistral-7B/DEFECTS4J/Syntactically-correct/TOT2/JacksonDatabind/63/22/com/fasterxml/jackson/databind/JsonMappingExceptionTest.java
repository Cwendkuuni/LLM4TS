import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testNullObjectSerialization() {
        try {
            mapper.writeValueAsString(null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Null object cannot be serialized", e.getMessage());
            assertNull(e.getPath());
        }
    }

    @Test
    public void testEmptyListSerialization() {
        try {
            mapper.writeValueAsString(Collections.emptyList());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Empty list cannot be serialized", e.getMessage());
            assertNull(e.getPath());
        }
    }

    @Test
    public void testComplexObjectSerialization() throws IOException {
        Person person = new Person("John Doe", 30);
        String json = mapper.writeValueAsString(person);
        Person readPerson = mapper.readValue(json, Person.class);
        assertEquals(person, readPerson);
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        String json = "{\"name\": \"John Doe\", \"age\": 30, \"invalidField\": 5}";
        try {
            mapper.readValue(json, Person.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Can not construct instance of com.example.Person, problem: invalid field 'invalidField' (class com.fasterxml.jackson.databind.JsonMappingException)", e.getMessage());
            Reference ref = e.getPath().get(0);
            assertEquals("invalidField", ref.getFieldName());
            assertEquals(JsonMappingException.class, ref.getFrom().getClass());
        }
    }

    @Test
    public void testMaxPathLimit() throws IOException {
        Object[] objs = new Object[JsonMappingException.MAX_REFS_TO_LIST + 1];
        try {
            mapper.writeValueAsString(objs);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Reference chain is not allowed to have more than " + JsonMappingException.MAX_REFS_TO_LIST + " elements. It looks like a circular reference", e.getMessage());
        }
    }

    @Test
    public void testUnexpectedIOException() {
        try {
            throw JsonMappingException.fromUnexpectedIOE(new IOException("Unexpected IO Exception"));
        } catch (JsonMappingException e) {
            assertEquals("Unexpected IOException (of type java.io.IOException): Unexpected IO Exception", e.getMessage());
        }
    }

    @Test
    public void testWrapWithPath() {
        String json = "{\"name\": [\"John\", \"Jane\", \"Jack\"]}";
        List<String> names = mapper.readValue(json, List.class);
        try {
            mapper.writeValueAsString(null);
            JsonMappingException e = JsonMappingException.wrapWithPath(new NullPointerException("Null pointer exception"), names, 1);
            assertEquals("Null pointer exception (was [Ljava.lang.String;@2e75e8a2) (through reference chain: [java.util.ArrayList[0]]->java.util.ArrayList[1])", e.getMessage());
        } catch (JsonProcessingException e1) {
            JsonMappingException e = JsonMappingException.wrapWithPath(e1, null, "name");
            assertEquals("Null pointer exception (was [Ljava.lang.String;@2e75e8a2) (through reference chain: JsonProcessingException[\"root cause\"]->com.fasterxml.jackson.core.JsonProcessingException[\"path\"]->com.fasterxml.jackson.databind.JsonMappingException[\"path\"]->java.lang.String[\"name\"]->java.lang.Object[\"null\"])", e.getMessage());
        }
    }

    static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}