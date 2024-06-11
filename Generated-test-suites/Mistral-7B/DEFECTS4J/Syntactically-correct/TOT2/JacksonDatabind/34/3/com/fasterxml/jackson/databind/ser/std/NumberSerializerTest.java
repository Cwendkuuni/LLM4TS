import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new BigDecimal("123.45");
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new byte[0]);
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String jsonString = jsonGenerator.getOutputStream().toString();
        assertEquals("123.45", jsonString);
    }
}