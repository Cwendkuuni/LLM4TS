import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(ObjectIdReader.Type.INT);
        when(objectIdReader.generator).thenReturn(new StdDateKeyGenerator.Instance());
        when(objectIdReader.resolver).thenReturn(null);
        when(objectIdReader.idProperty).thenReturn(null);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new ObjectNode(JSON.parse("{\"id\": 123}")).getPOJO());
        property.deserializeAndSet(jsonParser, ctxt, instance);

        assertNotNull(instance);
    }
}