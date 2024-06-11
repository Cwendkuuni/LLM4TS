import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(new BeanProperty(null), null, null, null, null, null);
        Object instance = new Object();
        String propName = "key";

        settableAnyProperty.deserializeAndSet(parser, new DeserializationContext(mapper), instance, propName);

        assertEquals("value", settableAnyProperty.getProperty().get(instance));
    }
}