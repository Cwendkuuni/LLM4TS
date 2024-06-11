import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.io.ser.JsonSerializer.ValueType;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeBasic() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Hello\", \"World\"]";
        JsonParser jp = mapper.getFactory().createParser(json);
        String[] result = new StringArrayDeserializer().deserialize(jp, new DeserializationContext());
        assertArrayEquals(new String[]{"Hello", "World"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullInput() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        JsonParser jp = mapper.getFactory().createParser(json);
        new StringArrayDeserializer().deserialize(jp, new DeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeEmptyInput() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        JsonParser jp = mapper.getFactory().createParser(json);
        new StringArrayDeserializer().deserialize(jp, new DeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidInput() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"invalid\": \"json\" }";
        JsonParser jp = mapper.getFactory().createParser(json);
        new StringArrayDeserializer().deserialize(jp, new DeserializationContext());
    }
}