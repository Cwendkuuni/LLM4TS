import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class ObjectIdValuePropertyTest {
    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"id\": 1}");
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(deserializationContext.findObjectId(anyLong(), eq(objectIdReader.generator), eq(objectIdReader.resolver))).thenReturn(new ReadableObjectId(1L));
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        verify(instance).setId(1L);
    }
}