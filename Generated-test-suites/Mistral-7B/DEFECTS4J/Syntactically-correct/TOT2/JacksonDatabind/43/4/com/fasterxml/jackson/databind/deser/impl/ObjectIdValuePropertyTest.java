import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        ObjectIdNode objectIdNode = ObjectMapper.createObjectMapper().readValueAsTree("{\"id\":123}");
        Object id = objectIdNode.asInt();

        when(jsonParser.getValueAsToken()).thenReturn(ContextualSerializationTest.emptyStringToken());
        when(jsonParser.nextValue()).thenReturn(objectIdNode);
        when(objectIdReader.getIdType()).thenReturn(ObjectIdReader.Type.OBJECT_ID);
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(objectIdValueProperty, times(1))._objectIdReader;
        verify(deserializationContext, times(1)).bindItem(instance);
        verify(objectIdReader.idProperty, times(1)).setAndReturn(instance, id);
        assertEquals(id, instance.hashCode());
    }
}