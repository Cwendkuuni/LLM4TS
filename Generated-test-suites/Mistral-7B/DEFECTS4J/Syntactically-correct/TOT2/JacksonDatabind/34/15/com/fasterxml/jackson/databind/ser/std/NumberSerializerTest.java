import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NumberSerializerTest {

    // test serialize(Number value, JsonGenerator g, SerializerProvider provider)
    @Test
    public void testSerializeNumber() throws IOException {
        // ...
    }

    @Test(expected = IOException.class)
    public void testSerializeNullNumber() throws IOException {
        // ...
    }

    @Test
    public void testSerializeInteger() throws IOException {
        // ...
    }

    // test getSchema(SerializerProvider provider, Type typeHint)
    @Test
    public void testGetSchema() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSchemaWithInvalidTypeHint() {
        // ...
    }

    // test acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // ...
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorWithInvalidTypeHint() throws JsonMappingException {
        // ...
    }

    // ...
}