import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new JsonMapper();
        ObjectReader reader = mapper.readerForClass(ObjectIdValueProperty.class);
        ObjectNode node = mapper.createObjectNode();
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        ObjectIdValueProperty property = reader.readValue(node);
        JsonParser jp = mapper.getFactory().createParser(node.traverse());
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(jp, ctxt, instance);
        verify(objectIdReader, times(1)).deserialize(jp, ctxt);
        verifyNoMoreInteractions(objectIdReader);
        assertEquals("id value should be set", "expectedIdValue", property.getId());
    }
}