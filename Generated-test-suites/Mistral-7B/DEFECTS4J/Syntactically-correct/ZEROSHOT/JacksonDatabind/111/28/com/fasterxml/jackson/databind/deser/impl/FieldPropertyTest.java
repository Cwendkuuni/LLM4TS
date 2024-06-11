package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for {@link FieldProperty}
 */
public class FieldPropertyTest {

    private static final String FIELD_NAME = "testField";
    private static final Class<?> TEST_CLASS = Object.class;
    private static final String TEST_FIELD_NAME = "testField";
    private static final Field TEST_FIELD;
    private static final Object TEST_INSTANCE = new Object();
    private static final JsonParser TEST_PARSER = mock(JsonParser.class);
    private static final DeserializationContext TEST_CONTEXT = mock(DeserializationContext.class);

    static {
        try {
            TEST_FIELD = TEST_CLASS.getDeclaredField(TEST_FIELD_NAME);
            TEST_FIELD.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void setUpBeforeClass() {
        when(TEST_PARSER.getCurrentName()).thenReturn(FIELD_NAME);
        when(TEST_PARSER.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(TEST_PARSER.getValueAsString()).thenReturn(null);
        when(TEST_PARSER.getValueAsInt()).thenReturn(0);
        when(TEST_PARSER.getValueAsLong()).thenReturn(0L);
        when(TEST_PARSER.getValueAsBoolean()).thenReturn(false);
    }

    @Test
    public void testConstructor() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
        assertSame(propDef, fieldProperty.getPropertyDefinition());
        assertSame(type, fieldProperty.getType());
        assertSame(typeDeser, fieldProperty.getTypeDeserializer());
        assertSame(contextAnnotations, fieldProperty.getContextAnnotations());
        assertSame(annotatedField, fieldProperty.getAnnotated());
        assertSame(TEST_FIELD, fieldProperty.getField());
        assertFalse(fieldProperty.isSkipNulls());
    }

    @Test
    public void testConstructorWithSkipNulls() throws IOException {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.isEnabled()).thenReturn(true);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField, nullValueProvider);
        assertSame(propDef, fieldProperty.getPropertyDefinition());
        assertSame(type, fieldProperty.getType());
        assertSame(typeDeser, fieldProperty.getTypeDeserializer());
        assertSame(contextAnnotations, fieldProperty.getContextAnnotations());
        assertSame(annotatedField, fieldProperty.getAnnotated());
        assertSame(TEST_FIELD, fieldProperty.getField());
        assertTrue(fieldProperty.isSkipNulls());
    }

    @Test
    public void testWithName() throws IOException {
        PropertyName newName = mock(PropertyName.class);
        FieldProperty fieldProperty = new FieldProperty(this, newName);
        assertSame(this, fieldProperty);
        assertSame(newName, fieldProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        FieldProperty fieldProperty = new FieldProperty(this, valueDeserializer, nullValueProvider);
        assertSame(this, fieldProperty);
        assertSame(valueDeserializer, fieldProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() throws IOException {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        FieldProperty fieldProperty = new FieldProperty(this, null, nullValueProvider);
        assertSame(this, fieldProperty);
        assertSame(nullValueProvider, fieldProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() throws IOException {
        DeserializationConfig config = mock(DeserializationConfig.class);
        FieldProperty fieldProperty = new FieldProperty(this);
        fieldProperty.fixAccess(config);
        verify(TEST_FIELD).setAccessible(true);
    }

    @Test
    public void testGetAnnotation() throws IOException {
        Annotation annotation = mock(Annotation.class);
        when(TEST_ANNOTATED.getAnnotation(Annotation.class)).thenReturn(annotation);
        FieldProperty fieldProperty = new FieldProperty(this);
        assertSame(annotation, fieldProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() throws IOException {
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(TEST_ANNOTATED.getMember()).thenReturn(member);
        FieldProperty fieldProperty = new FieldProperty(this);
        assertSame(member, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object value = null;
        when(TEST_VALUE_DESERIALIZER.deserialize(TEST_PARSER, TEST_CONTEXT)).thenReturn(value);
        FieldProperty fieldProperty = new FieldProperty(this);
        fieldProperty.deserializeAndSet(TEST_PARSER, TEST_CONTEXT, TEST_INSTANCE);
        assertNull(TEST_INSTANCE);
        verify(TEST_FIELD).set(TEST_INSTANCE, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object value = null;
        when(TEST_VALUE_DESERIALIZER.deserialize(TEST_PARSER, TEST_CONTEXT)).thenReturn(value);
        when(TEST_VALUE_DESERIALIZER.deserializeWithType(TEST_PARSER, TEST_CONTEXT, TEST_TYPE_DESERIALIZER)).thenReturn(value);
        FieldProperty fieldProperty = new FieldProperty(this);
        Object result = fieldProperty.deserializeSetAndReturn(TEST_PARSER, TEST_CONTEXT, TEST_INSTANCE);
        assertSame(TEST_INSTANCE, result);
        verify(TEST_FIELD).set(TEST_INSTANCE, value);
    }

    @Test
    public void testSet() throws IOException {
        Object value = null;
        FieldProperty fieldProperty = new FieldProperty(this);
        fieldProperty.set(TEST_INSTANCE, value);
        verify(TEST_FIELD).set(TEST_INSTANCE, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = null;
        FieldProperty fieldProperty = new FieldProperty(this);
        Object result = fieldProperty.setAndReturn(TEST_INSTANCE, value);
        assertSame(TEST_INSTANCE, result);
        verify(TEST_FIELD).set(TEST_INSTANCE, value);
    }

    @Test
    public void testReadResolve() throws IOException {
        FieldProperty fieldProperty = new FieldProperty(this);
        Object result = fieldProperty.readResolve();
        assertSame(fieldProperty, result);
    }

    @After
    public void tearDown() throws Exception {
        TEST_FIELD.setAccessible(false);
    }
}

class NullsConstantProvider {
    public static boolean isSkipper(NullValueProvider provider) {
        return false;
    }
}