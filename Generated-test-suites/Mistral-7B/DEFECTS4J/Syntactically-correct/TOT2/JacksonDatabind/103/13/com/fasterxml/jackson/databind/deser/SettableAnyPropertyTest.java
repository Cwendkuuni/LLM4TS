import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyConstructor() throws IOException {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMember(null, null, null, null, null, null);
        JavaType type = new TypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new ObjectMapper().findValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertNull(settableAnyProperty.getValueTypeDeserializer());
        assertNull(settableAnyProperty.getKeyDeserializer());
        assertFalse(settableAnyProperty.isSetterIsField());
    }
}