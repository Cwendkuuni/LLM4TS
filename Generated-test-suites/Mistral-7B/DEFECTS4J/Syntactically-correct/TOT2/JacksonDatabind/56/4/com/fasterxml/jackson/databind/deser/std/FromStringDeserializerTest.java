import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeSuccess() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer(String.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"Hello World\"";
        String result = deserializer.deserialize(mapper.parse(jsonString), new TestingDeserializationContext());
        assertEquals("Hello World", result);
    }
}

class TestingDeserializationContext extends DeserializationContext {
    @Override
    public Object findValueInMap(String name) {
        return null;
    }

    @Override
    public Object findValueInMap(String name, Class<?> valueType) {
        return null;
    }

    @Override
    public Object findValueInMap(String name, JavaType valueType) {
        return null;
    }

    @Override
    public JsonParser currentParser() {
        return null;
    }
}