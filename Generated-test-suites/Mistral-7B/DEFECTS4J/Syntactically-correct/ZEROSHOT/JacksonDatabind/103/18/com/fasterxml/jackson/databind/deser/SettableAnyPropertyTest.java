package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SettableAnyPropertyTest {

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;
    private Object intance;

    @BeforeAll
    public void beforeAll() throws Exception {
        jsonFactory = new JsonFactory();
        mapper = new ObjectMapper(jsonFactory);
        intance = new Object();
    }

    @Test
    public void testSettableAnyPropertyConstruction() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals(property, settableAnyProperty.getProperty());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty.getKeyDeserializer());
        assertFalse(settableAnyProperty.isSetterField());

        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);
        assertEquals(settableAnyProperty, settableAnyPropertyWithValueDeserializer.getProperty());
        assertNotNull(settableAnyPropertyWithValueDeserializer.getValueDeserializer());
        assertEquals(type, settableAnyPropertyWithValueDeserializer.getType());
        assertNull(settableAnyPropertyWithValueDeserializer.getKeyDeserializer());
        assertFalse(settableAnyPropertyWithValueDeserializer.isSetterField());
    }

    @Test
    public void testSettableAnyPropertyFixAccess() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        settableAnyProperty.fixAccess(mapper.getDeserializationConfig());
        verify(setter).fixAccess(mapper.getDeserializationConfig().isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSet() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        when(keyDeserializer.deserializeKey("propName", mapper.getDeserializationContext())).thenReturn("propName");
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser jsonParser = mapper.getFactory().createParser("[{\"propName\":\"value\"}]");

        settableAnyProperty.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), intance, "propName");

        if (setter instanceof AnnotatedField) {
            AnnotatedField field = (AnnotatedField) setter;
            Map<Object, Object> valueMap = (Map<Object, Object>) field.getValue(intance);
            assertNotNull(valueMap);
            assertEquals("value", valueMap.get("propName"));
        } else {
            assertInstanceOf(AnnotatedMethod.class, setter);
            verify(setter).callOnWith(intance, "propName", "value");
        }
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetWithNullValue() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        when(keyDeserializer.deserializeKey("propName", mapper.getDeserializationContext())).thenReturn("propName");
        when(valueDeserializer.getNullValue(mapper.getDeserializationContext())).thenReturn(null);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser jsonParser = mapper.getFactory().createParser("[{null}]");

        settableAnyProperty.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), intance, "propName");

        if (setter instanceof AnnotatedField) {
            AnnotatedField field = (AnnotatedField) setter;
            Map<Object, Object> valueMap = (Map<Object, Object>) field.getValue(intance);
            assertNull(valueMap);
        } else {
            assertInstanceOf(AnnotatedMethod.class, setter);
            verify(setter, never()).callOnWith(intance, "propName", null);
        }
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetWithValueTypeDeserializer() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(Integer.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        when(keyDeserializer.deserializeKey("propName", mapper.getDeserializationContext())).thenReturn("propName");
        when(valueDeserializer.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), any(TypeDeserializer.class))).thenReturn(42);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser jsonParser = mapper.getFactory().createParser("[{\"propName\":42}]");

        settableAnyProperty.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), intance, "propName");

        if (setter instanceof AnnotatedField) {
            AnnotatedField field = (AnnotatedField) setter;
            Map<Object, Object> valueMap = (Map<Object, Object>) field.getValue(intance);
            assertNotNull(valueMap);
            assertEquals(42, Objects.requireNonNull(valueMap.get("propName")));
        } else {
            assertInstanceOf(AnnotatedMethod.class, setter);
            verify(setter).callOnWith(intance, "propName", 42);
        }
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetWithKeyDeserializer() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        when(keyDeserializer.deserializeKey("1", mapper.getDeserializationContext())).thenReturn("propName");
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser jsonParser = mapper.getFactory().createParser("[{\"1\":\"value\"}]");

        settableAnyProperty.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), intance, "1");

        if (setter instanceof AnnotatedField) {
            AnnotatedField field = (AnnotatedField) setter;
            Map<Object, Object> valueMap = (Map<Object, Object>) field.getValue(intance);
            assertNotNull(valueMap);
            assertEquals("value", Objects.requireNonNull(valueMap.get("propName")));
        } else {
            assertInstanceOf(AnnotatedMethod.class, setter);
            verify(setter).callOnWith(intance, "propName", "value");
        }
    }

    @Test
    public void testSettableAnyPropertyThrowsIllegalArgumentException() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        when(keyDeserializer.deserializeKey("propName", mapper.getDeserializationContext())).thenReturn("propName");
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");
        when(valueDeserializer.getType(mapper.getDeserializationConfig())).thenReturn(mapper.getTypeFactory().constructType(Float.class));

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser jsonParser = mapper.getFactory().createParser("[{\"propName\":\"value\"}]");

        assertThrows(IllegalArgumentException.class, () -> settableAnyProperty.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), intance, "propName"));
    }
}