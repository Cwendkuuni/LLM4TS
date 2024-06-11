package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultDeserializationContextTest extends TestBase {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDefaultDeserializationContext() {
        DeserializerFactory df = new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated ann, Class<?> type) throws JsonMappingException {
                return null;
            }
        };

        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(df);

        // Test empty constructor
        assertNull(ctx._objectIds);
        assertNull(ctx._objectIdResolvers);

        // Test with constructor
        DeserializerCache cache = new DeserializerCache(16);
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = createParser();
        InjectableValues values = new InjectableValues();
        DefaultDeserializationContext ctxWithArgs = new DefaultDeserializationContext(ctx, config, jp, values, cache);
        assertEquals(ctx, ctxWithArgs.getRootContext());
        assertEquals(cache, ctxWithArgs.getCacheInstance());
        assertNotNull(ctxWithArgs._objectIds);
        assertNotNull(ctxWithArgs._objectIdResolvers);

        // Test copy constructor
        DefaultDeserializationContext ctxCopy = ctxWithArgs.copy();
        assertEquals(ctx, ctxCopy.getRootContext());
        assertEquals(cache, ctxCopy.getCacheInstance());
        assertSame(ctx._objectIds, ctxCopy._objectIds);
        assertSame(ctx._objectIdResolvers, ctxCopy._objectIdResolvers);

        // Test with constructor
        DefaultDeserializationContext ctxWith = ctx.with(df);
        assertEquals(ctx, ctxWith.getRootContext());
        assertEquals(df, ctxWith.getFactory());

        // Test createInstance
        DeserializationConfig config2 = new DeserializationConfig();
        DefaultDeserializationContext ctxInstance = ctxWith.createInstance(config2, createParser(), values);
        assertEquals(ctxWith, ctxInstance.getRootContext());
        assertEquals(df, ctxInstance.getFactory());
        assertNotNull(ctxInstance._objectIds);
        assertNotNull(ctxInstance._objectIdResolvers);

        // Test deserializerInstance
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> deserializer = mapper.deserializerInstance(createAnnotatedForTest(String.class), String.class);
        assertEquals(deserializer.getClass(), ctxInstance.deserializerInstance(createAnnotatedForTest(String.class), String.class).getClass());

        // Test keyDeserializerInstance
        KeyDeserializer keyDeserializer = mapper.keyDeserializerInstance(createAnnotatedForTest(Object.class), Object.class);
        assertEquals(keyDeserializer.getClass(), ctxInstance.keyDeserializerInstance(createAnnotatedForTest(Object.class), Object.class).getClass());

        // Test findObjectId
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return ObjectIdGenerator.IdKey.Value;
            }
        };
        Object object = new Object();
        ReadableObjectId roid = ctxInstance.findObjectId(object, idGen, SimpleObjectIdResolver.instance());
        assertNotNull(roid);
        assertEquals(idGen.key(object), roid.getKey().key);
        assertEquals(SimpleObjectIdResolver.class, roid.getResolver().getClass());

        // Test checkUnresolvedObjectId
        ctxInstance.checkUnresolvedObjectId();

        // Test findObjectId with null id
        ObjectIdGenerator idGenNull = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return ObjectIdGenerator.IdKey.Value;
            }
        };
        Object objectNull = null;
        ReadableObjectId roidNull = ctxInstance.findObjectId(objectNull, idGen, SimpleObjectIdResolver.instance());
        assertNull(roidNull);

        // Test findObjectId with already existing id
        ReadableObjectId existingRoid = ctxInstance.findObjectId(object, idGen, SimpleObjectIdResolver.instance());
        assertSame(roid, existingRoid);

        // Test findObjectId with different resolver
        List<ObjectIdResolver> resolvers = new ArrayList<>();
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DeserializationContext ctxt) {
                throw new UnsupportedOperationException();
            }

            @Override
            public ObjectIdResolver newForSerialization(SerializerProvider sp) {
                throw new UnsupportedOperationException();
            }

            @SuppressWarnings("unchecked")
            @Override
            public ObjectId resolveId(DeserializationContext ctxt, ObjectId id) throws JsonMappingException, IOException {
                return new ReadableObjectId(id);
            }

            @Override
            public boolean canUseFor(ObjectIdResolver resolverType) {
                return resolverType.getClass() == SimpleObjectIdResolver.class;
            }
        };
        resolvers.add(resolver);
        DefaultDeserializationContext ctxWithDifferentResolver = new DefaultDeserializationContext(ctxInstance, config2, jp, values) {
            @Override
            List<ObjectIdResolver> _objectIdResolvers = resolvers;
        };
        ObjectIdGenerator idGenDifferent = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return ObjectIdGenerator.IdKey.Value;
            }
        };
        Object objectDifferent = new Object();
        ReadableObjectId roidDifferent = ctxWithDifferentResolver.findObjectId(objectDifferent, idGenDifferent, resolver);
        assertNotNull(roidDifferent);
        assertEquals(idGenDifferent.key(objectDifferent), roidDifferent.getKey().key);
        assertEquals(resolver.getClass(), roidDifferent.getResolver().getClass());

        // Test findObjectId with existing id and different resolver
        ObjectIdGenerator idGenExisting = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return ObjectIdGenerator.IdKey.Value;
            }
        };
        Object objectExisting = new Object();
        ReadableObjectId existingRoidDifferent = ctxWithDifferentResolver.findObjectId(objectExisting, idGenExisting, resolver);
        assertSame(existingRoid, existingRoidDifferent);
    }

    private Annotated createAnnotatedForTest(Class<?> type) {
        return createAnnotated(type, new Object());
    }
}