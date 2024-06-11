package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Testing for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() throws Exception {
        objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdReader.IdType idType = new ObjectIdReader.IdType() {
            @Override
            public Class<?> getIdClass() {
                return String.class;
            }
        };
        JavaType javaType = Mockito.mock(JavaType.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);

        when(objectIdReader.getIdType()).thenReturn(idType);
        when(objectIdReader.getPropertyName()).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getDeserializer()).thenReturn(new None());
        when(objectIdReader.getIdProperty()).thenReturn(null);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);

        AnnotationIntrospector introspector = new AnnotationIntrospector();
        BeanDescription beanDescription = new BeanDescription(introspector, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null));
        objectIdValueProperty = new ObjectIdValueProperty(property, javaType, typeFactory);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() throws Exception {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String id = "1";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(jsonParser.getText()).thenReturn(id);

        Object instance = new Object();

        doAnswer(invocationOnMock -> objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance))
                .when(objectIdValueProperty).deserializeAndSet(jsonParser, context, instance);

        objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);

        assertEquals(id, objectIdValueProperty.get(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        Object instance = new Object();
        String value = "1";

        objectIdValueProperty.set(instance, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueDeserializer() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null));
        ObjectIdValueProperty copy = new ObjectIdValueProperty(property, new JsonDeserializer<String>() {});

        copy.withValueDeserializer(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithName() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null));
        ObjectIdValueProperty copy = new ObjectIdValueProperty(property, new PropertyName("name"));

        copy.withName(null);
    }

    @Test
    public void testToString() throws Exception {
        String expected = "ObjectIdValueProperty[name='id', getter=[], setter=[], valueDeserializer=class com.fasterxml.jackson.databind.JsonDeserializer$None, beanPropertyName=PropertyName { 'id' }, annotations={}, idType=class java.lang.String]";
        String result = objectIdValueProperty.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testSetWithIdProperty() throws Exception {
        String id = "1";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.getText()).thenReturn(id);
        when(objectIdReader.getIdProperty()).thenReturn(new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null)) {
            @Override
            public ObjectIdValueProperty withName(PropertyName newName) {
                return new ObjectIdValueProperty(this, new PropertyName("id")) {
                    @Override
                    public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser) {
                        return new ObjectIdValueProperty(this, deser) {
                            @Override
                            public Object deserializeSetAndReturn(JsonParser p,
                                    DeserializationContext ctxt, Object instance) throws IOException {
                                Object id = _valueDeserializer.deserialize(p, ctxt);
                                if (id == null) {
                                    return null;
                                }
                                setAndReturn(instance, id);
                                return instance;
                            }
                        };
                    }
                };
            }
        });

        doAnswer(invocationOnMock -> objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance))
                .when(objectIdValueProperty.getObjectIdReader().getIdProperty())
                .thenCallRealMethod();

        objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);

        assertEquals(id, objectIdValueProperty.get(instance));
        assertEquals(id, objectIdValueProperty.getObjectIdReader().getIdProperty().get(instance));
    }

    @Test
    public void testDeserializeWithJsonMappingException() throws Exception {
        String json = "{\"id\": \"invalid\"}";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(jsonParser.getText()).thenReturn(json);
        doThrow(new JsonMappingException(DeserializationFeature.FAIL_ON_EMPTY_BEANS, "Invalid id: 'invalid'")).when(objectIdValueProperty)
                .deserializeSetAndReturn(jsonParser, context, ArgumentMatchers.any(Object.class));

        Object instance = new Object();

        assertThrows(JsonMappingException.class, () -> objectIdValueProperty.deserializeAndSet(jsonParser, context, instance));
    }

    @Test
    public void testDeserializeAndSetWithToStringSerializer() throws Exception {
        String json = "{\"id\": 1}";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.getText()).thenReturn(json);
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        when(objectIdReader.getIdClass()).thenReturn(Long.class);
        when(objectIdValueProperty.getObjectIdReader()).thenReturn(objectIdReader);

        when(objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance))
                .thenReturn(Mockito.mock(Object.class));

        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                Object id = p.getText();
                if (id == null) {
                    return null;
                }
                return new Long(Long.parseLong(id));
            }
        };

        JsonDeserializer<Object> jsonDeserializerWithContext = new JsonDeserializer<Object>(Long.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                Object id = p.getText();
                if (id == null) {
                    return null;
                }
                return new Long(Long.parseLong(id));
            }

            @Override
            public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws IOException {
                ObjectIdValueProperty objectIdValueProperty = (ObjectIdValueProperty) property;
                return new JsonDeserializer<Object>(Long.class) {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        Object id = p.getText();
                        if (id == null) {
                            return null;
                        }
                        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, objectIdValueProperty.getObjectIdReader().getIdProperty().get(instance));
                        return result;
                    }
                };
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(MapperFeature.USE_TOSTRING_AS_FALLBACK_FOR_EMPTY_STRINGS, false);

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>(Object.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                Object id = p.getText();
                if (id == null) {
                    return null;
                }
                Object idValueProperty = ctxt.findProperty("id");
                Object idValuePropertyResult = idValueProperty.deserialize(p, ctxt);
                if (idValuePropertyResult == null) {
                    return null;
                }
                idValueProperty.setValue(idValueProperty.get(instance), idValuePropertyResult);
                return instance;
            }

            @Override
            public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws IOException {
                ObjectIdValueProperty objectIdValueProperty = (ObjectIdValueProperty) property;
                JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        Object id = p.getText();
                        if (id == null) {
                            return null;
                        }
                        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, objectIdValueProperty.getObjectIdReader().getIdProperty().get(instance));
                        return result;
                    }
                };
                return new JsonDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        Object id = p.getText();
                        if (id == null) {
                            return null;
                        }
                        Object idValueProperty = property;
                        Object result = idValueProperty.deserialize(p, ctxt);
                        idValueProperty.setValue(instance, result);
                        Object idValuePropertyResult = idValueProperty.getObjectIdReader().getIdProperty().get(instance);
                        if (idValuePropertyResult != null && idValuePropertyResult.getClass().isAssignableFrom(idValueProperty.getType().getRawClass())) {
                            idValuePropertyResult = idValuePropertyResult.deserialize(p, ctxt);
                            idValueProperty.setValue(idValueProperty.get(instance), idValuePropertyResult);
                        }
                        return instance;
                    }

                    @Override
                    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws IOException {
                        ObjectIdValueProperty objectIdValueProperty = (ObjectIdValueProperty) property;
                        return jsonDeserializerWithContext;
                    }
                };
            }
        };

        mapper.registerDeserializer(ObjectIdValueProperty.class, objectIdValueProperty);
        mapper.registerDeserializer(Long.class, new ToStringSerializer(Long.class));

        assertEquals(1L, mapper.readValue(json, Object.class));
    }
}