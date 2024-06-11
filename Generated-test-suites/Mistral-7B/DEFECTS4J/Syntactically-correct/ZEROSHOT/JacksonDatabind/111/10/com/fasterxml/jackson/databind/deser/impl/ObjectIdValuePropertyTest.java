package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return null;
            }

            @Override
            public BeanPropertyPropertyDescriptor idProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
    }

    @Test
    public void testConstructor() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        assertNotNull(property);
    }

    @Test
    public void testWithName() throws IOException {
        PropertyName newName = PropertyName.construct("newName");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        JsonDeserializer<String> valueDeserializer = new StdSerializer<String>(String.class) {};
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        ObjectIdValueProperty newProperty = property.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() throws IOException {
        NullValueProvider nullValueProvider = new NullValueProvider() {};
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        ObjectIdValueProperty newProperty = property.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, newProperty.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        assertNull(property.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException, JsonProcessingException {
        JsonDeserializer<ObjectId> idDeserializer = new StdDeserializer<ObjectId>(ObjectId.class) {};
        ObjectIdReader objectIdReaderWithIdDeserializer = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return idDeserializer;
            }

            @Override
            public BeanPropertyPropertyDescriptor idProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderWithIdDeserializer, PropertyMetadata.empty());
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdValueProperty deserializedProperty = mapper.readValue(
                getClass().getResource("/testObjectIdValueProperty.json"), ObjectIdValueProperty.class);
        assertEquals(property.getName(), deserializedProperty.getName());
        assertEquals(property.getIdType(), deserializedProperty.getIdType());
        assertEquals(property.getValueDeserializer(), deserializedProperty.getValueDeserializer());
        assertEquals(property.getNullProvider(), deserializedProperty.getNullProvider());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper deserializerMapper = new ObjectMapper();
        deserializerMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonDeserializer<ObjectId> idDeserializer = new StdDeserializer<ObjectId>(ObjectId.class) {};
        ObjectIdReader objectIdReaderWithIdDeserializer = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return idDeserializer;
            }

            @Override
            public BeanPropertyPropertyDescriptor idProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty idValuePropertyWithIdDeserializer = new ObjectIdValueProperty(objectIdReaderWithIdDeserializer, PropertyMetadata.empty());
        BeanDeserializerFactory beanDeserializerFactory = new BeanDeserializerFactory(objectMapper.getTypeFactory(), objectMapper.getConfig(), objectMapper.getDefaultTyping());
        TypeDeserializer idTypeDeserializer = beanDeserializerFactory.findValueDeserializer(Long.TYPE, null);
        ObjectIdValueProperty propertyToSet = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        ObjectMapper mapperWithIdDeserializer = new ObjectMapper();
        mapperWithIdDeserializer.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonDeserializer<Object> rootDeserializer = mapperWithIdDeserializer.readValue(
                getClass().getResource("/testObject.json"), Object.class);
        assertEquals(null, propertyToSet.deserializeSetAndReturn(null, null, rootDeserializer));
        assertEquals(null, propertyToSet.deserializeAndSet(null, null, rootDeserializer));
        JsonParser jsonParser = mapper.readerForValueType(Long.TYPE);
        jsonParser.setValue(1L);
        Object id = propertyToSet.deserializeSetAndReturn(jsonParser, null, rootDeserializer);
        assertEquals(1L, id);
        assertEquals(1L, propertyToSet.get(rootDeserializer));
        assertNotNull(propertyToSet.getAnnotation(JsonProperty.class));
        assertEquals("id", propertyToSet.getName());
        assertEquals(Long.TYPE, propertyToSet.getIdType());
        assertEquals(idTypeDeserializer, propertyToSet.getValueDeserializer());
        assertNull(propertyToSet.getNullProvider());

        ObjectIdValueProperty propertyWithIdDeserializer = new ObjectIdValueProperty(objectIdReaderWithIdDeserializer, PropertyMetadata.empty());
        ObjectMapper mapperWithIdDeserializer = new ObjectMapper();
        ObjectMapper deserializerMapperWithIdDeserializer = new ObjectMapper();
        deserializerMapperWithIdDeserializer.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser jsonParserWithIdDeserializer = mapperWithIdDeserializer.readerForValueType(Long.TYPE);
        jsonParserWithIdDeserializer.setValue(2L);
        Object idWithIdDeserializer = propertyWithIdDeserializer.deserializeSetAndReturn(jsonParserWithIdDeserializer, null, rootDeserializer);
        assertEquals(2L, idWithIdDeserializer);
        assertEquals(2L, propertyWithIdDeserializer.get(rootDeserializer));
        assertNotNull(propertyWithIdDeserializer.getAnnotation(JsonProperty.class));
        assertEquals("id", propertyWithIdDeserializer.getName());
        assertEquals(Long.TYPE, propertyWithIdDeserializer.getIdType());
        assertEquals(idDeserializer, propertyWithIdDeserializer.getValueDeserializer());
        assertNull(propertyWithIdDeserializer.getNullProvider());
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper deserializerMapper = new ObjectMapper();
        deserializerMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonDeserializer<ObjectId> idDeserializer = new StdDeserializer<ObjectId>(ObjectId.class) {};
        ObjectIdReader objectIdReaderWithIdDeserializer = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return idDeserializer;
            }

            @Override
            public BeanPropertyPropertyDescriptor idProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty idValuePropertyWithIdDeserializer = new ObjectIdValueProperty(objectIdReaderWithIdDeserializer, PropertyMetadata.empty());
        ObjectMapper mapperWithIdDeserializer = new ObjectMapper();
        ObjectMapper deserializerMapperWithIdDeserializer = new ObjectMapper();
        deserializerMapperWithIdDeserializer.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonDeserializer<Object> rootDeserializer = mapperWithIdDeserializer.readValue(
                getClass().getResource("/testObject.json"), Object.class);
        Object id = deserializerMapperWithIdDeserializer.readValue(getClass().getResource("/testObjectId.json"), ObjectId.class);
        ObjectIdValueProperty propertyToSet = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        ObjectIdValueProperty propertyWithIdDeserializer = new ObjectIdValueProperty(objectIdReaderWithIdDeserializer, PropertyMetadata.empty());
        propertyToSet.set(rootDeserializer, id);
        assertEquals(id, propertyToSet.get(rootDeserializer));
        propertyWithIdDeserializer.set(rootDeserializer, idWithIdDeserializer);
        assertEquals(idWithIdDeserializer, propertyWithIdDeserializer.get(rootDeserializer));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper deserializerMapper = new ObjectMapper();
        deserializerMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonDeserializer<ObjectId> idDeserializer = new StdDeserializer<ObjectId>(ObjectId.class) {};
        ObjectIdReader objectIdReaderWithIdDeserializer = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return idDeserializer;
            }

            @Override
            public BeanPropertyPropertyDescriptor idProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty idValuePropertyWithIdDeserializer = new ObjectIdValueProperty(objectIdReaderWithIdDeserializer, PropertyMetadata.empty());
        ObjectMapper mapperWithIdDeserializer = new ObjectMapper();
        ObjectMapper deserializerMapperWithIdDeserializer = new ObjectMapper();
        deserializerMapperWithIdDeserializer.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonDeserializer<Object> rootDeserializer = mapperWithIdDeserializer.readValue(
                getClass().getResource("/testObject.json"), Object.class);
        Object id = deserializerMapperWithIdDeserializer.readValue(getClass().getResource("/testObjectId.json"), ObjectId.class);
        ObjectIdValueProperty propertyToSet = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        ObjectIdValueProperty propertyWithIdDeserializer = new ObjectIdValueProperty(objectIdReaderWithIdDeserializer, PropertyMetadata.empty());
        Object idReturned = propertyToSet.setAndReturn(rootDeserializer, id);
        assertEquals(id, idReturned);
        Object idWithIdDeserializerReturned = propertyWithIdDeserializer.setAndReturn(rootDeserializer, idWithIdDeserializer);
        assertEquals(idWithIdDeserializer, idWithIdDeserializerReturned);
    }
}