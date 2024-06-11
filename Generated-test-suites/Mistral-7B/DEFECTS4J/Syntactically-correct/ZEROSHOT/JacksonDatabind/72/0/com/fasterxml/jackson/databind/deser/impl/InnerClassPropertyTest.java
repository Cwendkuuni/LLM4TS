package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonValueDeserializer;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedIntrospector;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for InnerClassProperty.
 */
public class InnerClassPropertyTest {

    @Mock
    private JsonDeserializer<Object> valueDeserializer;

    @Mock
    private JsonDeserializer<Object> valueTypeDeserializer;

    @Mock
    private AnnotatedConstructor annotatedConstructor;

    @Mock
    private AnnotatedMember annotatedMember;

    @Mock
    private BeanPropertyDefinition beanPropertyDefinition;

    @Mock
    private JsonFactory jsonFactory;

    @Mock
    private Object bean;

    @Mock
    private StdSerializer<Object> stdSerializer;

    @Mock
    private PropertySerializerMap propertySerializerMap;

    @InjectMocks
    private InnerClassProperty innerClassProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(valueDeserializer.getNullValue(Mockito.any(DeserializationContext.class))).thenReturn(null);
        when(valueSerializerMap.findSerializableSerializer(beanPropertyDefinition, jsonFactory)).thenReturn(stdSerializer);
    }

    @Test
    public void testInnerClassPropertyConstruction() throws IOException {
        Constructor<MyInnerClass> innerClassConstructor = mock(Constructor.class);
        when(innerClassConstructor.newInstance(Mockito.anyObject())).thenReturn(new MyInnerClass());
        AnnotatedConstructor annotatedInnerClassConstructor = new AnnotatedConstructor(null, innerClassConstructor, null, null);

        innerClassProperty = new InnerClassProperty(mock(SettableBeanProperty.class), annotatedInnerClassConstructor);
        assertNotNull(innerClassProperty);
        assertNull(innerClassProperty._annotated);
        assertNull(innerClassProperty._creator);
    }

    @Test
    public void testInnerClassPropertySerialization() {
        // JDK Serialization handling tests
        when(annotatedConstructor.getAnnotated()).thenReturn(annotatedInnerClassConstructor);
        when(innerClassProperty.writeReplace()).thenReturn(innerClassProperty);
        when(innerClassProperty.readResolve()).thenReturn(innerClassProperty);

        // Test serialization
        Object result = innerClassProperty.writeReplace();
        assertNotNull(result);
        assertInstanceof(InnerClassProperty.class, result);
    }

    @Test
    public void testDeserialization() throws IOException {
        Object value = new Object();
        when(jsonFactory.createParser("{\"value\": 42}"))
            .thenReturn(new JsonParser() {
                private boolean firstTokenRead = false;

                @Override
                public JsonToken nextToken() throws IOException {
                    if (!firstTokenRead) {
                        firstTokenRead = true;
                        return JsonToken.START_OBJECT;
                    }
                    return JsonToken.VALUE_NUMBER_INT;
                }

                @Override
                public int getValueAsInt() throws IOException, JsonProcessingException {
                    return 42;
                }
            });

        when(valueDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(value);
        when(innerClassProperty._valueDeserializer).thenReturn(valueDeserializer);

        Object result = innerClassProperty.deserializeAndSet(jsonFactory, null, bean);
        assertNotNull(result);
        assertInstanceof(MyInnerClass.class, result);
        assertEquals(42, BeanUtil.getValue(bean, innerClassProperty.getName()));
    }

    @Test
    public void testDeserializationWithTypeHandler() throws IOException {
        Object value = new Object();
        when(jsonFactory.createParser("{\"value\": 42}")).thenReturn(new JsonParser() {
            private boolean firstTokenRead = false;

            @Override
            public JsonToken nextToken() throws IOException {
                if (!firstTokenRead) {
                    firstTokenRead = true;
                    return JsonToken.START_OBJECT;
                }
                return JsonToken.VALUE_NUMBER_INT;
            }

            @Override
            public int getValueAsInt() throws IOException, JsonProcessingException {
                return 42;
            }
        });

        when(valueDeserializer.deserializeWithType(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class), Mockito.any()))
                .thenReturn(value);
        when(innerClassProperty._valueTypeDeserializer).thenReturn(valueTypeDeserializer);

        Object result = innerClassProperty.deserializeSetAndReturn(jsonFactory, null, bean);
        assertNotNull(result);
        assertInstanceof(MyInnerClass.class, result);
        assertEquals(42, BeanUtil.getValue(bean, innerClassProperty.getName()));
    }

    @Test
    public void testTransientConstructor() throws IOException {
        when(annotatedConstructor.getAnnotated()).thenReturn(null);
        when(innerClassProperty._annotated).thenReturn(annotatedConstructor);

        assertNotNull(innerClassProperty);
        assertNotNull(innerClassProperty._annotated);
        assertNull(innerClassProperty._creator);
    }

    @Test
    public void testSetName() {
        String newName = "newName";
        InnerClassProperty result = innerClassProperty.withName(newName);

        assertEquals(newName, result.getName());
    }

    @Test
    public void testSetValueDeserializer() {
        JsonDeserializer<Object> valueDeserializer2 = mock(JsonDeserializer.class);
        InnerClassProperty result = innerClassProperty.withValueDeserializer(valueDeserializer2);

        assertEquals(valueDeserializer2, result._valueDeserializer);
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(beanPropertyDefinition.getAnnotation(JsonDeserialize.class)).thenReturn(annotation);

        assertEquals(annotation, innerClassProperty.getAnnotation(JsonDeserialize.class));
    }

    @Test
    public void testGetMember() {
        when(annotatedMember.getAnnotated()).thenReturn(innerClassProperty);

        assertEquals(annotatedMember, innerClassProperty.getMember());
    }

    @Test
    public void testAssignIndex() {
        int index = 42;
        innerClassProperty.assignIndex(index);

        assertEquals(index, innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(-1, innerClassProperty.getPropertyIndex());
    }

    private static class MyInnerClass {
        @JsonValueDeserializer(value = MySerializer.class)
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private static class MySerializer extends StdSerializer<MyInnerClass> {
        public MySerializer() {
            this(null);
        }

        private MySerializer(Class<MyInnerClass> t) {
            super(t);
        }

        @Override
        public void serialize(MyInnerClass value, JsonGenerator jg, SerializerProvider serializers) throws IOException {
            jg.writeNumber(value.getValue());
        }
    }
}