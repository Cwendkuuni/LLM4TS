import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty beanProperty = mock(BeanProperty.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
        Object bean = new Object();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        when(annotatedMember.getValue(bean)).thenReturn(new HashMap<>());

        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeStartObject();
        verify(jsonGenerator, times(1)).writeFields(_mapSerializer);
        verify(jsonGenerator, times(1)).writeEndObject();
    }
}