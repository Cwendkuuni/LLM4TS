import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_Date() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new StandardSerializerProvider();

        Date date = new Date();
        serializer.serialize(date, jgen, provider);
        jgen.close();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(System.in);
        JsonNode keyNode = rootNode.get(0);

        assertEquals("\"$date\"", keyNode.asText());
        assertEquals(date.getClass().getName(), keyNode.getClass().getName());
    }

    // Add test case for String value
}