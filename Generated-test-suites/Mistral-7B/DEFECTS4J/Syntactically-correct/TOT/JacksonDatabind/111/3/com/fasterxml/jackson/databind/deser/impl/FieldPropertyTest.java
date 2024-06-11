package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link FieldProperty} class.
 */
public class FieldPropertyTest {

    private Object testInstance;
    private Field testField;
    private AnnotatedField testAnnotatedField;
    private BeanPropertyDefinition testPropertyDefinition;
    private TypeDeserializer testTypeDeserializer;
    private Annotations testContextAnnotations;

    @Before
    public void setUp() throws Exception {
        testInstance = new Object();
        testField = new Field(true, "testField", Integer.class, null, null);
        testField.setAccessible(true);
        testAnnotatedField = Mockito.mock(AnnotatedField.class);
        when(testAnnotatedField.getAnnotated()).thenReturn(testField);
        testPropertyDefinition = Mockito.mock(BeanPropertyDefinition.class);
        testContextAnnotations = Mockito.mock(Annotations.class);
        testTypeDeserializer = Mockito.mock(TypeDeserializer.class);
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(testAnnotatedField, testPropertyDefinition, testTypeDeserializer, testContextAnnotations);
    }

    @Test
    public void testFieldPropertyConstruction() {
        FieldProperty fieldProperty = new FieldProperty(testPropertyDefinition, ObjectTypeFactory.instance, testTypeDeserializer, testContextAnnotations, testAnnotatedField);
        assertEquals(testPropertyDefinition, fieldProperty.getPropertyDefinition());
        assertEquals(ObjectTypeFactory.instance, fieldProperty.getType());
        assertEquals(testTypeDeserializer, fieldProperty.getTypeDeserializer());
        assertEquals(testContextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(testAnnotatedField, fieldProperty.getAnnotated());
        assertEquals(testField, fieldProperty.getField());
        assertEquals(false, fieldProperty.isSkipNulls());
    }

    @Test
    public void testFieldPropertySerialization() throws JsonProcessingException {
        Object value = 123;
        when(testTypeDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(value);

        FieldProperty fieldProperty = new FieldProperty(testPropertyDefinition, ObjectTypeFactory.instance, testTypeDeserializer, testContextAnnotations, testAnnotatedField);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = testInstance;

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        assertEquals(value, testField.get(instance));
    }

    @Test
    public void testFieldPropertySerializationWithNullValue() throws JsonProcessingException {
        NullValueProvider nvp = new NullValueProvider();

        FieldProperty fieldProperty = new FieldProperty(testPropertyDefinition, ObjectTypeFactory.instance, testTypeDeserializer, testContextAnnotations, testAnnotatedField);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = testInstance;

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        assertNull(testField.get(instance));

        when(jsonParser.getValueAsInt()).thenReturn(JsonProcessingException.code.valueOf("TEST"));
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        assertNull(testField.get(instance));

        when(jsonParser.getValueAsInt(nvp)).thenReturn(null);
        JsonNode emptyNode = Mockito.mock(JsonNode.class);
        when(jsonParser.getCodec()).thenReturn(new ObjectMapper());
        when(jsonParser.readValueAsTree()).thenReturn(emptyNode);
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        assertNull(testField.get(instance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFieldPropertySerializationMissingField() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectParser parser = objectMapper.treeParser();
        JsonNode node = parser.readValueAsTree("{}");
        FieldProperty fieldProperty = new FieldProperty(testPropertyDefinition, ObjectTypeFactory.instance, testTypeDeserializer, testContextAnnotations, testAnnotatedField);
        Object instance = testInstance;

        fieldProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
        fail("Should throw an IllegalArgumentException");
    }

    @Test
    public void testFieldPropertyNullProvider() {
        NullValueProvider nvp = new NullValueProvider();

        FieldProperty fieldProperty = new FieldProperty(testPropertyDefinition, ObjectTypeFactory.instance, testTypeDeserializer, testContextAnnotations, testAnnotatedField);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = testInstance;

        fieldProperty = fieldProperty.withNullProvider(nvp);
        assertEquals(nvp, fieldProperty.getNullProvider());
    }

    @Test
    public void testFieldPropertyValueDeserializer() {
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);

        FieldProperty fieldProperty = new FieldProperty(testPropertyDefinition, ObjectTypeFactory.instance, testTypeDeserializer, testContextAnnotations, testAnnotatedField);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = testInstance;

        fieldProperty = fieldProperty.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, fieldProperty.getValueDeserializer());
    }

    @Test
    public void testFieldPropertyWithName() {
        PropertyName newName = PropertyName.construct("testName");

        FieldProperty fieldProperty = new FieldProperty(testPropertyDefinition, ObjectTypeFactory.instance, testTypeDeserializer, testContextAnnotations, testAnnotatedField);
        assertEquals(testPropertyDefinition, fieldProperty.getPropertyDefinition());
        assertEquals(ObjectTypeFactory.instance, fieldProperty.getType());
        assertEquals(testTypeDeserializer, fieldProperty.getTypeDeserializer());
        assertEquals(testContextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(testAnnotatedField, fieldProperty.getAnnotated());
        assertEquals(testField, fieldProperty.getField());
        assertEquals(false, fieldProperty.isSkipNulls());
        assertEquals(fieldProperty, fieldProperty.withName(newName));
    }
}