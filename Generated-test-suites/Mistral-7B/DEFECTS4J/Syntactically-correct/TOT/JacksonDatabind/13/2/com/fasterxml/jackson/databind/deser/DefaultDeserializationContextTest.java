package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private JsonParser parser;
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Class<?> idType() {
                        return String.class;
                    }

                    @Override
                    public String idFromValue(Object idObject) {
                        return idObject.toString();
                    }
                };
            }
        };
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public Object resolveId(DeserializationContext ctxt, Object id) throws IOException {
                return id;
            }
        };
        context = new DefaultDeserializationContext.Builder()
                .independentSerializerBinder(new SimpleSerializerProvider())
                .serializerFactory(new DefaultSerializerFactory())
                .objectIdResolver(resolver)
                .objectIdGenerator(generator)
                .build();
        mapper = new ObjectMapper();
        parser = mapper.getFactory().createParser("{ \"one\": 1, \"two\": \"two\", \"three\": { \"id\": \"123\" } }");
    }

    @Test
    public void testFindObjectId() throws Exception {
        Object id = context.findObjectId(new Object(), context.getObjectIdGenerator(), ObjectIdGenerator.IdKey.CLASS);
        assertNotNull(id);
        assertEquals(String.class, id.getClass());
        assertEquals("1", id.toString());

        id = context.findObjectId(new Object(), context.getObjectIdGenerator(), ObjectIdGenerator.IdKey.STRING);
        assertNotNull(id);
        assertEquals(String.class, id.getClass());
        assertEquals("123", id.toString());
    }

    @Test
    public void testFindObjectIdWithNullId() throws Exception {
        Object id = context.findObjectId(null, context.getObjectIdGenerator(), ObjectIdGenerator.IdKey.CLASS);
        assertNull(id);
    }

    @Test
    public void testFindObjectIdWithNullGenerator() throws Exception {
        assertThrows(NullPointerException.class, () -> context.findObjectId(new Object(), null, ObjectIdGenerator.IdKey.CLASS));
    }

    @Test
    public void testFindObjectIdWithNullKey() throws Exception {
        assertThrows(NullPointerException.class, () -> context.findObjectId(new Object(), context.getObjectIdGenerator(), null));
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        context.findObjectId(new Object(), context.getObjectIdGenerator(), ObjectIdGenerator.IdKey.CLASS);
        context.findObjectId(new Object(), context.getObjectIdGenerator(), ObjectIdGenerator.IdKey.STRING);

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference to be thrown");
        } catch (UnresolvedForwardReference ufr) {
            // Expected
        }
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        JsonDeserializer<Integer> deserializer = new StandardDeserializer<>(Integer.class);
        Annotated ann = mapper.findAnnotation(JsonDeserializer.class, Integer.class);
        Object deserDef = ann;

        JsonDeserializer<Integer> deserializerInstance = context.deserializerInstance(ann, deserDef);
        assertEquals(deserializer, deserializerInstance);

        deserializerInstance = context.deserializerInstance(ann, null);
        assertNull(deserializerInstance);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        KeyDeserializer deserializer = new StdKeyDeserializer();
        Annotated ann = mapper.findAnnotation(KeyDeserializer.class, String.class);
        Object deserDef = deserializer;

        KeyDeserializer keyDeserializerInstance = context.keyDeserializerInstance(ann, deserDef);
        assertEquals(deserializer, keyDeserializerInstance);

        keyDeserializerInstance = context.keyDeserializerInstance(ann, null);
        assertNull(keyDeserializerInstance);
    }

    @Test
    public void testWith() throws Exception {
        DefaultDeserializationContext context2 = context.with(new DefaultSerializerFactory());
        assertNotEquals(context, context2);

        JsonDeserializer<Integer> deserializer = new StandardDeserializer<>(Integer.class);
        Object deserDef = deserializer;

        JsonDeserializer<Integer> deserializerInstance = context2.deserializerInstance(mapper.findAnnotation(JsonDeserializer.class, Integer.class), deserDef);
        assertNotEquals(deserializer, deserializerInstance);

        context2 = context.with(null);
        assertTrue(context.equals(context2));
    }

    @Test
    public void testCreateInstance() throws Exception {
        DefaultDeserializationContext context2 = context.createInstance(new DeserializationConfig(), parser, null);
        assertNotEquals(context, context2);

        JsonDeserializer<Integer> deserializer = new StandardDeserializer<>(Integer.class);
        Object deserDef = deserializer;

        JsonDeserializer<Integer> deserializerInstance = context2.deserializerInstance(mapper.findAnnotation(JsonDeserializer.class, Integer.class), deserDef);
        assertNotEquals(deserializer, deserializerInstance);

        context2 = context.createInstance(new DeserializationConfig(), null, null);
        assertTrue(context.equals(context2));
    }
}