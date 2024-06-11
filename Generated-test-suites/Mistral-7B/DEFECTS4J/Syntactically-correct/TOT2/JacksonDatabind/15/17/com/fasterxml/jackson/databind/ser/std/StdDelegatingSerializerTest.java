import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdDelegatingSerializerTest {

    private Converter<Object,?> converter;
    private JavaType delegateType;
    private JsonSerializer<Object> delegateSerializer;
    private StdDelegatingSerializer serializer;
    private ObjectMapper mapper;
    private SerializerProvider provider;

    @Before
    public void setup() {
        converter = mock(Converter.class);
        delegateType = mock(JavaType.class);
        delegateSerializer = mock(JsonSerializer.class);
        mapper = new ObjectMapper();
        provider = mock(SerializerProvider.class);

        when(converter.convert(anyObject())).thenReturn(anyObject());
        when(delegateSerializer.serialize(anyObject(), anyObject(), anyObject())).thenReturn(null);

        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException {
        Object value = new Object();
        ObjectNode result = mapper.writeValueAsTree(serializer, value, provider);
        assertEquals(null, result);
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, JsonMappingException {
        Object value = new Object();
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        ObjectNode result = serializer.serializeWithType(value, mock(JsonGenerator.class), provider, typeSerializer);
        assertEquals(null, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullConverter() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, delegateType, delegateSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullDelegateType() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullDelegateSerializer() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, null);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        serializer.resolve(provider);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        Object value = new Object();
        JsonSerializer<?> createdSerializer = serializer.createContextual(provider, mock(BeanProperty.class));
        assertEquals(serializer, createdSerializer);
    }
}