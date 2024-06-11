package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idProperty");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        Assert.assertEquals("idProperty", property.getName());
        Assert.assertNull(property.getValueDeserializer());
        Assert.assertNull(property._objectIdReader);
    }

    @Test
    public void testCopyConstructor() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idProperty");
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, null, null);

        Assert.assertEquals("idProperty", property.getName());
        Assert.assertSame(src.getValueDeserializer(), property.getValueDeserializer());
        Assert.assertSame(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithNameConstructor() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idProperty");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ, new PropertyName("newName"));

        Assert.assertEquals("newName", property.getName());
        Assert.assertNull(property.getValueDeserializer());
        Assert.assertSame(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idProperty");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        Assert.assertNull(property.getAnnotation(Null.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idProperty");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        Assert.assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idProperty");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"idProperty\": 1}");
        Object id = node.get("idProperty").asLong();

        Object instance = new Object() {
            private long id;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }
        };

        property.deserializeAndSet(new BaseJsonParser(objectMapper.getFactory()), objectMapper.getDeserializationContext(), instance);

        Assert.assertEquals(1, instance.getId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idProperty");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"idProperty\": 1}");
        Object id = node.get("idProperty").asLong();

        Object instance = new Object() {
            private long id;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }
        };

        Object result = property.deserializeSetAndReturn(new BaseJsonParser(objectMapper.getFactory()), objectMapper.getDeserializationContext(), instance);

        Assert.assertEquals(1, instance.getId());
        Assert.assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idProperty");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        Object instance = new Object() {
            private long id;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }
        };

        property.set(instance, 1);

        Assert.assertEquals(1, instance.getId());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idProperty");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        Object instance = new Object() {
            private long id;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }
        };

        Object result = property.setAndReturn(instance, 1);

        Assert.assertEquals(1, instance.getId());
        Assert.assertEquals(instance, result);
    }

    private static class SimpleObjectIdReader implements ObjectIdReader {
        private final PropertyName propertyName;

        public SimpleObjectIdReader(String propertyName) {
            this.propertyName = new PropertyName(propertyName);
        }

        @Override
        public PropertyName propertyName() {
            return propertyName;
        }

        @Override
        public IdReader.IdType idType() {
            return IdReader.IdType.INT_ID;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return null;
        }

        @Override
        public ObjectIdResolver idResolver() {
            return null;
        }
    }
}