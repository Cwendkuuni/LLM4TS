import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructType(SomeClass.class);
        TypeIdResolver typeIdResolver = objectMapper.getTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver, "_type", true, SomeClass.class);

        String json = "{\"type\":\"SomeClass\",\"value\":123}";
        JsonParser jsonParser = new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        Object result = deserializer.deserializeTypedFromObject(jsonParser, new TestContext());

        assertEquals(123, result);
    }
}