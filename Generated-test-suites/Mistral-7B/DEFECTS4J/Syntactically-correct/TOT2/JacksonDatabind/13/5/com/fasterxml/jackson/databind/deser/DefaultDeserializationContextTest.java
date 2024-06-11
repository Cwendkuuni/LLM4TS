package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

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

import java.util.*;
import java.util.Map.Entry;

/**
 * Tests for DefaultDeserializationContext class.
 */
public class DefaultDeserializationContextTest {

    @SuppressWarnings("unchecked")
    private static DefaultDeserializationContext createDefaultContext() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializerFactory deserializerFactory = factory.getDeserializerFactory();
        DefaultDeserializationContext context = new Impl(deserializerFactory);
        return context;
    }

    @org.junit.Test
    public void testFindObjectId() throws Exception {
        DefaultDeserializationContext context = createDefaultContext();

        // Test null id and missing id
        assertNull(context.findObjectId(null, ObjectIdGenerator.StringIdGenerator.class, SimpleObjectIdResolver.class));
        assertNull(context.findObjectId(new Object(), ObjectIdGenerator.StringIdGenerator.class, SimpleObjectIdResolver.class));

        // Test valid id and simple resolver
        String id = "testId";
        ObjectIdGenerator generator = new ObjectIdGenerator.StringIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId objectId = context.findObjectId(id, generator, resolver.getClass());
        assertEquals(id, objectId.getId());
        assertEquals(resolver.getClass(), objectId.getResolver().getClass());

        // Test valid id and custom resolver
        Object customId = new Object();
        ObjectIdGenerator customGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String toString() {
                        return "customId";
                    }
                };
            }
        };
        ObjectIdResolver customResolver = new ObjectIdResolver() {
            @Override
            public ObjectId newForDeserialization(DefaultDeserializationContext ctxt, Object id) {
                return new ReadableObjectId(ctxt.key(customId));
            }
        };
        ReadableObjectId customObjectId = context.findObjectId(customId, customGenerator, customResolver.getClass());
        assertEquals("customId", customObjectId.getId().toString());
        assertEquals(customResolver.getClass(), customObjectId.getResolver().getClass());
    }

    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws Exception {
        DefaultDeserializationContext context = createDefaultContext();

        // Test unresolved id
        ObjectIdGenerator generator = new ObjectIdGenerator.StringIdGenerator();
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId newForDeserialization(DefaultDeserializationContext ctxt, Object id) {
                return new ReadableObjectId(new IdKey() {
                    @Override
                    public String toString() {
                        return "unresolvedId";
                    }
                });
            }
        };
        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = new ArrayList<>();
        context.findObjectId("unresolvedId", generator, resolver.getClass());
        context.checkUnresolvedObjectId();
    }

    @org.junit.Test
    public void testDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = createDefaultContext();

        // Test null deserDef
        assertNull(context.deserializerInstance(null, null));

        // Test simple deserDef (JsonDeserializer)
        JsonDeserializer<String> jsonDeserializer = new JsonDeserializer<String>() {};
        JsonDeserializer<String> deser = context.deserializerInstance(new AnnotatedClassTest(), jsonDeserializer);
        assertEquals(jsonDeserializer.getClass(), deser.getClass());

        // Test complex deserDef (Class)
        Class<JsonDeserializer<String>> deserDef = JsonDeserializer.class;
        JsonDeserializer<String> deser2 = context.deserializerInstance(new AnnotatedClassTest(), deserDef);
        assertEquals(deserDef, deser2.getClass().getInterfaces()[0]);

        // Test missing deserDef
        Class<JsonDeserializer<String>> missingDeserDef = Object.class;
        assertNull(context.deserializerInstance(new AnnotatedClassTest(), missingDeserDef));
    }

    @org.junit.Test
    public void testKeyDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = createDefaultContext();

        // Test null deserDef
        assertNull(context.keyDeserializerInstance(null, null));

        // Test simple deserDef (JsonDeserializer)
        JsonDeserializer<String> jsonDeserializer = new JsonDeserializer<String>() {};
        KeyDeserializer deser = context.keyDeserializerInstance(new AnnotatedClassTest(), jsonDeserializer);
        assertEquals(jsonDeserializer.getClass(), deser.getClass());

        // Test complex deserDef (Class)
        Class<JsonDeserializer<String>> deserDef = JsonDeserializer.class;
        KeyDeserializer deser2 = context.keyDeserializerInstance(new AnnotatedClassTest(), deserDef);
        assertEquals(deserDef, deser2.getClass().getInterfaces()[0]);

        // Test missing deserDef
        Class<JsonDeserializer<String>> missingDeserDef = Object.class;
        assertNull(context.keyDeserializerInstance(new AnnotatedClassTest(), missingDeserDef));
    }

    private static class AnnotatedClassTest implements Annotated {
        @Override
        public ObjectIdResolver _value() {
            return null;
        }

        @Override
        public AnnotationMetadata annotations() {
            return null;
        }

        @Override
        public Class<?> getRawType() {
            return Object.class;
        }

        @Override
        public Class<?> getType() {
            return Object.class;
        }

        @Override
        public String toString() {
            return "AnnotatedClassTest";
        }
    }
}