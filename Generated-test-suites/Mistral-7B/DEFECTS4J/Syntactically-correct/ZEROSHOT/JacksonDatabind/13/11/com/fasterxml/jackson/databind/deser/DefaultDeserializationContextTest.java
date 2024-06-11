package com.example.junit.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

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
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    private DefaultDeserializationContext context;
    private DeserializerFactory factory;
    private DeserializerCache cache;

    @Before
    public void setup() {
        factory = new DefaultDeserializerFactory();
        cache = new MapBasedDeserializerCache();
        context = new DefaultDeserializationContext(factory, cache);
    }

    @Test
    public void testConstructor() {
        assertNotNull(context);
    }

    @Test
    public void testFindObjectId() {
        // Test with null id
        assertNull(context.findObjectId(null, new ObjectIdGenerator<Object>() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return null;
            }
        }));

        // Test with valid id
        Object id = new Object();
        ObjectIdGenerator.IdKey key = new IdKey();
        ReadableObjectId objectId = context.findObjectId(id, new ObjectIdGenerator<Object>() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return key;
            }
        }, SimpleObjectIdResolver.instance);
        assertNotNull(objectId);
        assertEquals(key, objectId.getIdKey());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        Object id = new Object();
        ObjectIdGenerator.IdKey key = new IdKey();
        context.findObjectId(id, new ObjectIdGenerator<Object>() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return key;
            }
        }, SimpleObjectIdResolver.instance);
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new JsonDeserializer<>(String.class);
        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(String.class, deserializer.getClass());

        deserDef = new Class<JsonDeserializer<Object>>(JsonDeserializer.class);
        deserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(JsonDeserializer.class, deserializer.getClass());

        deserDef = new Class<JsonDeserializer<List<String>>>(JsonDeserializer.class);
        deserializer = context.deserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new KeyDeserializer();
        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(KeyDeserializer.class, deserializer.getClass());

        deserDef = new Class<KeyDeserializer>(KeyDeserializer.class);
        deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertEquals(KeyDeserializer.class, deserializer.getClass());

        deserDef = new Class<JsonDeserializer<Object>>(JsonDeserializer.class);
        deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @Test
    public void testCopy() {
        DefaultDeserializationContext src = context;
        DefaultDeserializationContext dest = src.copy();
        assertNotNull(dest);
        assertNotSame(src, dest);
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext context1 = context;
        DefaultDeserializationContext context2 = context1.with(new DefaultDeserializerFactory());
        assertNotSame(context1, context2);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context1 = context;
        DefaultDeserializationContext context2 = context1.createInstance(getConfig(), mock(JsonParser.class), new InjectableValues());
        assertNotSame(context1, context2);
    }
}