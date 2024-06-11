import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Number.class, NumberSerializer.instance);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    @Test
    public void testNumberSerializer_BigDecimal() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;

        BigDecimal bigDecimal = new BigDecimal("123.45");
        ObjectNode jsonNode = objectMapper.writeValueAsObject(bigDecimal);

        jsonGenerator = objectMapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        serializerProvider = objectMapper.getSerializerProvider();
        numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode.toString());
        assertEquals(bigDecimal.toString(), jsonParser.getText());
    }

    @Test
    public void testNumberSerializer_BigInteger() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;

        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        ObjectNode jsonNode = objectMapper.writeValueAsObject(bigInteger);

        jsonGenerator = objectMapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        serializerProvider = objectMapper.getSerializerProvider();
        numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode.toString());
        assertEquals(bigInteger.toString(), jsonParser.getText());
    }

    // Add test cases for other methods...
}