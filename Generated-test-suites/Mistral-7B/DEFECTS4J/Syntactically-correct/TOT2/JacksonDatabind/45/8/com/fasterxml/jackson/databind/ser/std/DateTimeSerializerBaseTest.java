import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.TextNode;

public class DateTimeSerializerBaseTest {

    @Test
    public void testSerialize() throws IOException {
        JsonSerializer<java.util.Date> serializer = new DateTimeSerializerBase<java.util.Date>(Date.class, Boolean.FALSE, null);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        Date date = new Date();
        serializer.serialize(date, gen, new JsonFactory());
        TextNode result = new ObjectMapper().readValue(gen.getOutputStreamAsString(), TextNode.class);
        assertEquals("\"2023-03-15T14:25:35.000Z\"", result.asText());
    }
}