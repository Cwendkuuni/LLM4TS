import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws JsonProcessingException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        DeserializationContext ctxt = new TestDeserializationContext();
        Object instance = new Object();

        String json = "{\"id\":1}";
        JsonParser jp = new JsonFactory().createParser(json);

        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);

        Object id = ctxt.findValueToDeserialize("1", ObjectIdReader.class, null);
        ReadableObjectId roid = ctxt.findObjectId(id, objectIdReader.generator, objectIdReader.resolver);
        assertEquals(1, roid.getId());
        assertEquals(instance, roid.getObject());
    }
}