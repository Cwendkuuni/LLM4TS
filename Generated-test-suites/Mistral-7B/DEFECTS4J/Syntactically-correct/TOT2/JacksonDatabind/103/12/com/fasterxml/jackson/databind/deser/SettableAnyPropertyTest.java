import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.mockito.Mockito.*;;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        // Given
        Object instance = new Object();
        String propName = "testProperty";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        // When
        settableAnyProperty.deserializeAndSet(jsonParser, context, instance, propName);

        // Then
        verify(setter).callOnWith(instance, eq(propName), any());
    }

    @Test
    public void testDeserializeAndSetWithValueDeserializer() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        Object value = new Object();

        // Given
        Object instance = new Object();
        String propName = "testProperty";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(value);

        // When
        settableAnyProperty.deserializeAndSet(jsonParser, context, instance, propName);

        // Then
        verify(setter).callOnWith(instance, eq(propName), eq(value));
    }
}