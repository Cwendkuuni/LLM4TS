import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;

import static com.fasterxml.jackson.databind.JsonMapping.ValueInstantiator.Std;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {

    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;
    private KeyDeserializer keyDeserializer;
    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setup() throws Exception {
        AnnotatedField field = Mockito.mock(AnnotatedField.class);
        when(field.getValue(any())).thenReturn(Map.of());
        JavaType javaType = JsonTypeFactory.instance.constructType(Map.class);
        valueDeserializer = Mockito.mock(JsonDeserializer.class);
        valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);
        keyDeserializer = Mockito.mock(KeyDeserializer.class);
        settableAnyProperty = new SettableAnyProperty(
                Mockito.mock(BeanProperty.class), field, javaType, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @Test
    public void testInitialization() {
        assertNotNull(settableAnyProperty);
        assertEquals(Mockito.mock(BeanProperty.class), settableAnyProperty.getProperty());
        assertEquals(field, settableAnyProperty._setter);
        assertEquals(javaType, settableAnyProperty._type);
        assertNull(settableAnyProperty._valueDeserializer);
        assertNull(settableAnyProperty._valueTypeDeserializer);
        assertNull(settableAnyProperty._keyDeserializer);
        assertFalse(settableAnyProperty._setterIsField);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Object> newValueDeserializer = Mockito.mock(JsonDeserializer.class);
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(newValueDeserializer);
        assertEquals(settableAnyProperty, newSettableAnyProperty.getProperty());
        assertEquals(field, newSettableAnyProperty._setter);
        assertEquals(javaType, newSettableAnyProperty._type);
        assertEquals(valueDeserializer, newSettableAnyProperty._valueDeserializer);
        assertEquals(valueTypeDeserializer, newSettableAnyProperty._valueTypeDeserializer);
        assertEquals(keyDeserializer, newSettableAnyProperty._keyDeserializer);
        assertTrue(newSettableAnyProperty._setterIsField);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig deserializationConfig = Mockito.mock(DeserializationConfig.class);
        settableAnyProperty.fixAccess(deserializationConfig);
        verify(field).fixAccess(deserializationConfig.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testDeserializeAndSet() throws JsonProcessingException {
        Object key = "testKey";
        Object value = "testValue";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        String propName = "testPropName";

        when(jsonParser.getText()).thenReturn(propName);

        settableAnyProperty.deserializeAndSet(jsonParser, deserializationContext, instance, propName);

        verify(instance).getClass();
        verify(field).callOnWith(instance, eq(propName), eq(value));
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object value = "testValue";

        when(jsonParser.getValueAsString()).thenReturn("testValueString");
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getText()).thenReturn("testKeyString");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = settableAnyProperty.deserialize(jsonParser, deserializationContext);

        assertEquals(value, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNullValueDeserializer() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        String propName = "testPropName";

        settableAnyProperty._valueDeserializer = null;
        settableAnyProperty.deserializeAndSet(jsonParser, deserializationContext, instance, propName);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object key = "testKey";
        Object value = "testValue";

        settableAnyProperty.set(instance, key, value);

        verify(instance).getClass();
        verify(field).callOnWith(instance, eq(key), eq(value));
    }
}