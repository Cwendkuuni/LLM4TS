package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ObjectIdGenerator;
import java.util.ObjectIdResolver;

public class ObjectIdValuePropertyTest {

    private static ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public PropertyName propertyName() {
            return PropertyName.construct("id");
        }

        @Override
        public Class<?> idType() {
            return Long.class;
        }

        @Override
        public JsonDeserializer<?> getIdDeserializer() {
            return null;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return new ObjectIdGenerator() {
                long counter = 0;

                @Override
                public Object generateId() {
                    return counter++;
                }
            };
        }

        @Override
        public ObjectIdResolver idResolver() {
            return new ObjectIdResolver() {
                @Override
                public Object resolveId(Object id, ObjectIdGenerator generator) {
                    return id;
                }
            };
        }

        @Override
        public SettableBeanProperty idProperty;
    };

    @org.junit.Test
    public void testObjectIdValueProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        ObjectNode node = jsonFactory.createObjectNode();

        // Test setting null value
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object idInstance = new Object() {
            private Long id;
        };
        property.set(idInstance, null);
        assertNull(idInstance.getId());

        // Test setting valid value
        property.setValueDeserializer(new SimpleNumberDeserializer(Long.class));
        node.put("id", 1L);
        JsonParser parser = jsonFactory.createParser(node);
        idInstance = mapper.readValue(parser, idInstance.getClass());
        assertEquals(1L, idInstance.getId());

        // Test setting invalid value
        parser = jsonFactory.createParser(node.put("id", "invalid"));
        try {
            mapper.readValue(parser, idInstance.getClass());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test setting value with annotation and member
        ObjectAnnotatedClass annotatedClass = new ObjectAnnotatedClass(AnnotatedClass.class, new AnnotatedMember(new AnnotatedField("id", Long.class)));
        property = new ObjectIdValueProperty(objectIdReader, annotatedClass, null);
        // Test setting null value
        property.set(new AnnotatedClass(), null);
        assertNull(annotatedClass.getId());

        // Test setting valid value
        node.put("id", 1L);
        parser = jsonFactory.createParser(node);
        annotatedClass = mapper.readValue(parser, AnnotatedClass.class);
        assertEquals(1L, annotatedClass.getId().longValue());

        // Test setting invalid value
        node.put("id", "invalid");
        parser = jsonFactory.createParser(node);
        try {
            mapper.readValue(parser, AnnotatedClass.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}