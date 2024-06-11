package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
    }

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "test_id";
                    }
                };
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }
        };

        ReadableObjectId result = context.findObjectId(new Object(), idGenerator, resolver);
        Assert.assertNotNull(result);
        Assert.assertEquals("test_id", result.getId());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "test_id";
                    }
                };
            }
        };

        context.findObjectId(new Object(), idGenerator, null);
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {};
        Annotated annotated = null;

        JsonDeserializer<String> result = context.deserializerInstance(annotated, deserializer);
        Assert.assertNotNull(result);
        Assert.assertEquals(deserializer.getClass(), result.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializerInstanceWithInvalidClass() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {};
        Annotated annotated = null;

        Object deserDef = new Object() {};
        JsonDeserializer<String> result = context.deserializerInstance(annotated, deserDef);
        Assert.fail("Expected JsonMappingException but got: " + result);
    }

    @Test
    public void testKeyDeserializerInstance() {
        ObjectMapper mapper = new ObjectMapper();
        KeyDeserializer deserializer = new KeyDeserializer() {};
        Annotated annotated = null;

        KeyDeserializer result = context.keyDeserializerInstance(annotated, deserializer);
        Assert.assertNotNull(result);
        Assert.assertEquals(deserializer.getClass(), result.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testKeyDeserializerInstanceWithInvalidClass() {
        ObjectMapper mapper = new ObjectMapper();
        KeyDeserializer deserializer = new KeyDeserializer() {};
        Annotated annotated = null;

        Object deserDef = new Object() {};
        KeyDeserializer result = context.keyDeserializerInstance(annotated, deserDef);
        Assert.fail("Expected JsonMappingException but got: " + result);
    }

    @Test
    public void testWith() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext newContext = context.with(mapper.getDeserializationFactory());
        Assert.assertEquals(mapper.getDeserializationFactory(), newContext.getFactory());
    }

    @Test
    public void testCreateInstance() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext newContext = context.createInstance(mapper.getConfig(), null, null);
        Assert.assertNotNull(newContext);
        Assert.assertNotSame(context, newContext);
    }
}