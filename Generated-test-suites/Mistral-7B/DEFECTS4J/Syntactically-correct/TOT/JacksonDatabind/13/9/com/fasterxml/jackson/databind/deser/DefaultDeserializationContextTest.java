package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;

    @Before
    public void setup() {
        context = new DefaultDeserializationContext.Impl(ObjectMapper.getDefaultInstance().getDeserializationConfig());
    }

    @Test
    public void testFindObjectId() throws JsonProcessingException {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("IdKey");
            }

            @Override
            public Object generateId(SerializerProvider serializers) {
                return new Object();
            }
        };

        Object id = new Object();
        ReadableObjectId expected = context.findObjectId(id, idGen, new SimpleObjectIdResolver());
        ReadableObjectId actual = context.findObjectId(id, idGen);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindObjectId_withNullId() throws JsonProcessingException {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("IdKey");
            }

            @Override
            public Object generateId(SerializerProvider serializers) {
                return new Object();
            }
        };

        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId actual = context.findObjectId(null, idGen, resolver);
        assertNull(actual);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws JsonProcessingException {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("IdKey");
            }

            @Override
            public Object generateId(SerializerProvider serializers) {
                return new Object();
            }
        };

        Object id1 = new Object();
        Object id2 = new Object();
        context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>() {{
            put(new ObjectIdGenerator.IdKey("IdKey1"), new ReadableObjectId(new ObjectIdGenerator.IdKey("IdKey1")));
            put(new ObjectIdGenerator.IdKey("IdKey2"), new ReadableObjectId(new ObjectIdGenerator.IdKey("IdKey2")));
        }};

        context.checkUnresolvedObjectId();

        context.findObjectId(id1, idGen);
        context.checkUnresolvedObjectId();

        context._objectIds.remove(new ObjectIdGenerator.IdKey("IdKey2"));
        assertThrows(UnresolvedForwardReference.class, () -> context.checkUnresolvedObjectId());
    }

    @Test
    public void testDeserializerInstance() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JsonDeserializer<Integer> deserializer = mapper.deserializationConfig().createBeanDeserializer(Integer.class);

        Annotated ann = mapper.introspect(Integer.class);
        Object deserDef = deserializer;

        JsonDeserializer<Integer> actual = context.deserializerInstance(ann, deserDef);
        assertEquals(deserializer, actual);
    }

    @Test
    public void testDeserializerInstance_withNullDeserDef() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JsonDeserializer<Integer> deserializer = mapper.deserializationConfig().createBeanDeserializer(Integer.class);

        Annotated ann = mapper.introspect(Integer.class);

        Object deserDef = null;
        JsonDeserializer<Integer> actual = context.deserializerInstance(ann, deserDef);
        assertNull(actual);
    }

    @Test
    public void testDeserializerInstance_withClassDeserDef() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JsonDeserializer<Integer> deserializer = mapper.deserializationConfig().createBeanDeserializer(Integer.class);

        Annotated ann = mapper.introspect(Integer.class);

        Class<JsonDeserializer<Integer>> deserDef = JsonDeserializer.class;
        JsonDeserializer<Integer> actual = context.deserializerInstance(ann, deserDef);
        assertEquals(deserializer, actual);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        KeyDeserializer keyDeserializer = mapper.deserializationConfig().createKeyDeserializer(Integer.class);

        Annotated ann = mapper.introspect(Integer.class);
        Object deserDef = keyDeserializer;

        KeyDeserializer actual = context.keyDeserializerInstance(ann, deserDef);
        assertEquals(keyDeserializer, actual);
    }

    @Test
    public void testKeyDeserializerInstance_withNullDeserDef() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        KeyDeserializer keyDeserializer = mapper.deserializationConfig().createKeyDeserializer(Integer.class);

        Annotated ann = mapper.introspect(Integer.class);

        Object deserDef = null;
        KeyDeserializer actual = context.keyDeserializerInstance(ann, deserDef);
        assertNull(actual);
    }

    @Test
    public void testKeyDeserializerInstance_withClassDeserDef() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        KeyDeserializer keyDeserializer = mapper.deserializationConfig().createKeyDeserializer(Integer.class);

        Annotated ann = mapper.introspect(Integer.class);

        Class<KeyDeserializer> deserDef = KeyDeserializer.class;
        KeyDeserializer actual = context.keyDeserializerInstance(ann, deserDef);
        assertEquals(keyDeserializer, actual);
    }
}