package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonId;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectIdReader = new ObjectIdReader() {
            @Override
            public String propertyName() {
                return "id";
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    long idCounter = 0;
                    @Override
                    public Object generateId(Serializable id) {
                        return id != null ? id : ++idCounter;
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idForValue(Object value) {
                        return value;
                    }
                };
            }
        };
    }

    @After
    public void teardown() throws Exception {
        mapper = null;
        objectIdReader = null;
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object id = null;
        Object instance = new IdEntity();
        property.deserializeAndSet(mapper.getFactory().createParser("null"), mapper.getDeserializationContext(), instance);
        assertNull(instance.getId());
        assertEquals(0, ((IdEntity) instance).getIdCounter());
    }

    @Test
    public void testDeserializeAndSetLong() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object id = 1L;
        Object instance = new IdEntity();
        property.deserializeAndSet(mapper.getFactory().createParser("1"), mapper.getDeserializationContext(), instance);
        assertEquals(id, instance.getId());
        assertEquals(1, ((IdEntity) instance).getIdCounter());
    }

    @Test
    public void testDeserializeAndSetStringWithFromStringDeserializer() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object id = new FromStringDeserializer().deserialize("1", mapper.getDeserializationContext());
        Object instance = new IdEntity();
        property.deserializeAndSet(mapper.getFactory().createParser("{\"id\":\"1\"}"), mapper.getDeserializationContext(), instance);
        assertEquals(id, instance.getId());
        assertEquals(1, ((IdEntity) instance).getIdCounter());
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object id = 1L;
        Object instance = new IdEntity();
        property.set(instance, id);
        assertEquals(id, instance.getId());
        assertEquals(1, ((IdEntity) instance).getIdCounter());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null) {
            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }
        };
        Object instance = new IdEntity();
        property.set(instance, 1L);
    }

    static class IdEntity {
        private Long id;
        private long idCounter;

        public Long getId() {
            return id;
        }

        @JsonId
        public void setId(Long id) {
            this.id = id;
        }

        public long getIdCounter() {
            return idCounter;
        }
    }

    static class ObjectIdGenerator implements ObjectIdGenerator {
        long idCounter = 0;

        @Override
        public Object generateId(Serializable id) {
            return id != null ? id : ++idCounter;
        }
    }

    static class ObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectId idForValue(Object value) {
            return value;
        }
    }
}