import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerializeMethod() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SimpleConverter(), JsonTypeFactory.defaultInstance.constructType(String.class), new StringSerializer(objectMapper));
        ObjectNode input = JsonNodeFactory.instance.objectNode();
        input.put("name", "John Doe");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(byteArrayOutputStream);
        serializer.serialize(input, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();

        String expectedJson = "{\"name\":\"John Doe\"}";
        String actualJson = new String(byteArrayOutputStream.toByteArray());

        assertEquals(expectedJson, actualJson);
    }
}