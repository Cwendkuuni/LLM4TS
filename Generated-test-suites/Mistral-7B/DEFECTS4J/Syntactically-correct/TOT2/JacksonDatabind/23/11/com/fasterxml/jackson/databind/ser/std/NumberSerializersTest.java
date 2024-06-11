import org.junit.Test;
import static org.mockito.Mockito.*;

public class NumberSerializersTest {
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = mock(Map.class);

        NumberSerializers.addAll(allDeserializers);

        // Check if all the expected serializers were added to the map
        verify(allDeserializers).put(eq(Integer.class.getName()), any());
        verify(allDeserializers).put(eq(Integer.TYPE.getName()), any());
        verify(allDeserializers).put(eq(Long.class.getName()), any());
        verify(allDeserializers).put(eq(Long.TYPE.getName()), any());
        verify(allDeserializers).put(eq(Byte.class.getName()), any());
        verify(allDeserializers).put(eq(Byte.TYPE.getName()), any());
        verify(allDeserializers).put(eq(Short.class.getName()), any());
        verify(allDeserializers).put(eq(Short.TYPE.getName()), any());
        verify(allDeserializers).put(eq(Float.class.getName()), any());
        verify(allDeserializers).put(eq(Float.TYPE.getName()), any());
        verify(allDeserializers).put(eq(Double.class.getName()), any());
        verify(allDeserializers).put(eq(Double.TYPE.getName()), any());
    }
}