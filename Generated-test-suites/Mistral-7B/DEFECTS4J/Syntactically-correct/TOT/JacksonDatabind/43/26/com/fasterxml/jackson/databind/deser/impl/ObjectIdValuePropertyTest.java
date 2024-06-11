package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty property;
    private BeanPropertyDefinition propertyDefinition;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        ObjectIdReader.IdType idType = ObjectIdReader.IdType.LONG;
        Object id = 1L;
        objectIdReader = new ObjectIdReader() {
            @Override
            public IdType getIdType() {
                return idType;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return new ObjectIdGenerator() {
                    long nextId = 1;

                    @Override
                    public ObjectId generateId(SerializableIdOwner idOwner) {
                        return new ObjectId(nextId++, idType);
                    }
                };
            }

            @Override
            public ObjectIdResolver getResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId getId(Object id, ObjectIdGenerator generator) {
                        return new ObjectId(id, idType);
                    }

                    @Override
                    public Object resolveId(ObjectId id, ObjectIdGenerator generator) {
                        return id.getIdValue();
                    }
                };
            }

            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return new StandardDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt)
                            throws IOException {
                        return p.getValueAsLong();
                    }
                };
            }

            @Override
            public JsonDeserializer<Object> getIdPropertyReader(DeserializationContext ctxt)
                    throws JsonMappingException {
                ObjectIdReader reader = this;
                return new BeanPropertyWrapperDeserializer(reader, null, null, null, null, null, null, null, null);
            }

            @Override
            public SettableBeanProperty idProperty;
        };

        propertyDefinition = new BeanPropertyDefinition("id", Object.class, null, null, null, null);
        property = new ObjectIdValueProperty(objectIdReader, propertyDefinition);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        JsonParser parser = new TestJsonParser();
        parser.setValue(1);

        property.deserializeAndSet(parser, new TestDeserializationContext(), instance);

        Object id = property.get(instance);
        assertEquals(1, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        JsonParser parser = new TestJsonParser();
        parser.setValue(1);

        Object result = property.deserializeSetAndReturn(parser, new TestDeserializationContext(), instance);

        assertEquals(instance, result);
        Object id = property.get(instance);
        assertEquals(1, id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object id = 1;

        Object result = property.setAndReturn(instance, id);

        assertEquals(instance, result);
        Object idFromProperty = property.get(instance);
        assertEquals(id, idFromProperty);
    }

    @Test
    public void testNullId() throws IOException {
        Object instance = new Object();
        JsonParser parser = new TestJsonParser();

        property.deserializeAndSet(parser, new TestDeserializationContext(), instance);
        assertNull(property.get(instance));
    }

    @Test
    public void testSetWithIdProperty() throws IOException {
        Object instance = new Object() {
            private long id;
        };
        Object id = 1;
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, propertyDefinition) {
            @Override
            SettableBeanProperty idProperty;

            @Override
            public SettableBeanProperty getIdProperty() {
                return new StubSettableBeanProperty("id", Long.TYPE, propertyDefinition, null, null) {
                    @Override
                    public void setAndReturn(Object instance, Object value)
                            throws IOException {
                        super.setAndReturn(instance, value);
                        ((Object) instance).id = (Long) value;
                        return instance;
                    }
                };
            }
        };

        Object result = idProperty.setAndReturn(instance, id);

        assertEquals(instance, result);
        assertEquals(id, ((Object) instance).id);
    }

    @Test
    public void testToString() throws IOException {
        Object instance = new Object();
        property.setValue(instance, 1);

        String expected = String.format("ObjectIdValueProperty[name='id', getter=class %s, setter=class %s, value = %s]",
                property.getClass().getName(),
                ToStringSerializer.class.getName(),
                1);
        String actual = property.toString();

        assertEquals(expected, actual);
    }

    static class StubSettableBeanProperty extends SettableBeanProperty {
        public StubSettableBeanProperty(String name, Class<?> type, PropertyDefinition metadata, Object value,
                JsonDeserializer<?> valueDeserializer, JsonSerializer<?> valueSerializer,
                NameTransformer nameTransformer, Object parent, AnnotatedMember member) {
            super(name, type, metadata, value, valueDeserializer, valueSerializer, nameTransformer, parent, member);
        }

        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            Object oldValue = getValue(instance);
            setValue(instance, value);
            return oldValue;
        }
    }

    static class TestJsonParser extends JsonParser {
        Object value;

        @Override
        public boolean nextToken() throws IOException {
            return true;
        }

        @Override
        public int nextValue() throws IOException {
            Object result = value;
            value = null;
            return result == null ? JsonToken.VALUE_NULL : JsonToken.VALUE_NUMBER_INT;
        }

        @Override
        public long ValueAsLong() throws IOException {
            long result = (long) value;
            value = null;
            return result;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    static class TestDeserializationContext extends DeserializationContext {
    }
}