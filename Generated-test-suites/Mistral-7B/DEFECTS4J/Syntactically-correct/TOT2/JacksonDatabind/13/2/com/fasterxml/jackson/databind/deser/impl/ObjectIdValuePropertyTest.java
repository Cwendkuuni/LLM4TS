import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyName propertyName = new PropertyName("id");
        JsonDeserializer<ObjectIdNode> valueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        ObjectNode testJson = JsonNodeFactory.instance.objectNode();
        testJson.put("id", new TextNode("123"));

        JsonParser jsonParser = mapper.getFactory().createParser(testJson.toString());

        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(ObjectIdNode.class);
        when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(valueDeserializer.deserialize(jsonParser, withSettings(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false))).thenReturn(new ObjectIdNode("123"));

        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(jsonParser, mock(DeserializationContext.class), instance);

        verify(objectIdReader).propertyName;
        verify(objectIdReader).getIdType();
        verify(objectIdReader).getDeserializer();
        verify(valueDeserializer).deserialize(jsonParser, withSettings(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false));
        verify(objectIdReader).generator;
        verify(objectIdReader).resolver;
        verify(instance).getClass();
        verify(instance).getClass().getDeclaredField("id");
        verify(instance).getClass().getDeclaredField("id").set(instance, "123");
    }
}