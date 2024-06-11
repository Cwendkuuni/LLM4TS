package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.*;

public class DefaultDeserializationContextTest extends TestBase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public void testDefaultDeserializationContext() throws IOException {
        // Test constructor with default arguments
        DefaultDeserializationContext ctx = MAPPER.getDeserializationContext();
        assertNotNull(ctx);

        // Test ObjectId resolution
        ObjectIdGenerator idGen = MAPPER.getFactory().getObjectIdGenerator(ObjectIdGenerator.Value.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object id = new Object();
        ObjectIdKey key = idGen.key(id);
        ReadableObjectId objId = ctx.findObjectId(id, idGen, resolver);
        assertEquals(key, objId.getKey());
        assertEquals(resolver, objId.getResolver());

        // Test checkUnresolvedObjectId
        Object unresolvedId = new Object();
        ctx.findObjectId(unresolvedId, idGen, resolver);
        try {
            ctx.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        // Test deserializerInstance
        JsonDeserializer<Object> deserializer = ctx.deserializerInstance(createAnnotated(Object.class), ObjectDeserializer.class);
        assertNotNull(deserializer);

        // Test keyDeserializerInstance
        KeyDeserializer keyDeserializer = ctx.keyDeserializerInstance(createAnnotated(Object.class), ObjectKeyDeserializer.class);
        assertNotNull(keyDeserializer);

        // Test with, createInstance and copy
        DefaultDeserializationContext ctx1 = ctx.with(MAPPER.getFactory());
        assertNotSame(ctx, ctx1);

        DefaultDeserializationContext ctx2 = ctx.createInstance(MAPPER.getDeserializationConfig(), createJsonParser("{}"), null);
        assertNotSame(ctx, ctx2);

        DefaultDeserializationContext ctx3 = ctx.copy();
        assertNotSame(ctx, ctx3);
    }

    private Annotated createAnnotated(Class<?> cls) {
        return new AnnotatedClass(cls, null);
    }

    private JsonParser createJsonParser(String jsonString) throws IOException {
        return MAPPER.getFactory().createParser(jsonString);
    }
}