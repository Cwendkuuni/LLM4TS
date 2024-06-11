package com.example.jackson.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private DefaultDeserializationContext ctx;
    private DefaultDeserializationContext copyCtx;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ctx = mapper.getDeserializationContext();
        copyCtx = ctx.copy();
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectIdGenerator idGen = new StdObjectIdGenerator();

        Object id = new Object();
        ObjectIdResolver resolver = new StdObjectIdResolver();

        ReadableObjectId roid1 = ctx.findObjectId(id, idGen, resolver);
        assertEquals(id, roid1.getId());
        assertNull(roid1.getResolver());

        ObjectIdKey key = idGen.key(id);
        roid1 = ctx._objectIds.get(key);
        assertNotNull(roid1);
        assertEquals(id, roid1.getId());
        assertNull(roid1.getResolver());

        // test null id
        roid1 = ctx.findObjectId(null, idGen, resolver);
        assertNull(roid1);

        // test existing id
        Object id2 = new Object();
        ReadableObjectId roid2 = ctx.findObjectId(id2, idGen, resolver);
        assertNotNull(roid2);
        assertNotSame(roid1, roid2);
        assertEquals(id2, roid2.getId());
        assertNull(roid2.getResolver());

        // test find existing id from cache
        key = idGen.key(id2);
        roid2 = ctx._objectIds.get(key);
        assertEquals(id2, roid2.getId());
        assertNull(roid2.getResolver());

        // test resolve object id
        ObjectIdResolver resolver2 = new StdObjectIdResolver();
        roid1 = ctx.findObjectId(id, idGen, resolver2);
        assertEquals(id, roid1.getId());
        assertNotNull(roid1.getResolver());
        assertEquals(resolver2, roid1.getResolver());

        ObjectIdResolver resolver3 = new StdObjectIdResolver();
        roid1 = ctx.findObjectId(id, idGen, resolver3);
        assertEquals(id, roid1.getId());
        assertSame(roid1.getResolver(), resolver3);

        // test multiple resolvers
        ObjectIdResolver resolver4 = new StdObjectIdResolver();
        ObjectIdResolver resolver5 = new StdObjectIdResolver();
        List<ObjectIdResolver> resolvers = Arrays.asList(resolver4, resolver5);
        ctx._objectIdResolvers = resolvers;

        ObjectIdResolver resolver6 = new StdObjectIdResolver();
        roid1 = ctx.findObjectId(id, idGen, resolver6);
        assertEquals(id, roid1.getId());
        assertSame(roid1.getResolver(), resolver6);
        assertEquals(resolver6, resolver4.getNext());
        assertEquals(resolver5, resolver6.getNext());
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator idGen = new StdObjectIdGenerator();
        Object id1 = new Object();
        Object id2 = new Object();

        ctx.findObjectId(id1, idGen, new StdObjectIdResolver());

        ctx._objectIds = new LinkedHashMap<>();
        try {
            ctx.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: ", e.getMessage());
            assertEquals(2, e.size());
            assertEquals(Object.class, e.getIdClass());

            Map<ObjectIdKey, ReadableObjectId> expectedMap = new LinkedHashMap<>();
            expectedMap.put(IdKey.ROOT, new ReadableObjectId(IdKey.ROOT));
            expectedMap.put(IdKey.CLASS, new ReadableObjectId(IdKey.CLASS, Object.class));
            expectedMap.put(IdKey.PROPERTY, new ReadableObjectId(IdKey.PROPERTY, "id1", Object.class));
            expectedMap.put(IdKey.PROPERTY, new ReadableObjectId(IdKey.PROPERTY, "id2", Object.class));

            Map<ObjectIdKey, ReadableObjectId> actualMap = new LinkedHashMap<>();
            for (Entry<ObjectIdKey, ReadableObjectId> entry : ctx._objectIds.entrySet()) {
                actualMap.put(entry.getKey(), entry.getValue());
            }

            assertEquals(expectedMap, actualMap);
        }

        // test no unresolved references
        ctx._objectIds = null;
        ctx.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializeInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdGenerator idGen = mapper.getFactory().getObjectIdGenerator();

        JsonDeserializer<Object> deser = new StdJsonDeserializer();
        Annotated ann = mock(Annotated.class);
        Object deserDef = deser;

        Object deser1 = ctx.deserializerInstance(ann, deserDef);
        assertEquals(deser, deser1);

        JsonDeserializer<Object> deser2 = mock(JsonDeserializer.class);
        deser2 = (JsonDeserializer<Object>) deserializerFromClass(JsonDeserializer.class, ctx);
        deser2.resolve(ctx);
        Object deser3 = ctx.deserializerInstance(ann, deserDef);
        assertEquals(deser2, deser3);

        Object deser4 = ctx.deserializerInstance(ann, null);
        assertNull(deser4);

        Object deser5 = ctx.deserializerInstance(ann, new Object());
        assertNull(deser5);

        JsonDeserializer<Object> deser6 = new StdJsonDeserializer();
        deser6.getPropName();
        Object deser7 = ctx.deserializerInstance(ann, deser6);
        assertNull(deser7);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdGenerator idGen = mapper.getFactory().getObjectIdGenerator();

        KeyDeserializer deser = new StdKeyDeserializer();
        Annotated ann = mock(Annotated.class);
        Object deserDef = deser;

        KeyDeserializer deser1 = ctx.keyDeserializerInstance(ann, deserDef);
        assertEquals(deser, deser1);

        KeyDeserializer deser2 = mock(KeyDeserializer.class);
        deser2 = (KeyDeserializer) keyDeserializerFromClass(KeyDeserializer.class, ctx);
        Object deser3 = ctx.keyDeserializerInstance(ann, deserDef);
        assertEquals(deser2, deser3);

        Object deser4 = ctx.keyDeserializerInstance(ann, null);
        assertNull(deser4);

        Object deser5 = ctx.keyDeserializerInstance(ann, new Object());
        assertNull(deser5);

        KeyDeserializer deser6 = new StdKeyDeserializer();
        deser6.getPropName();
        Object deser7 = ctx.keyDeserializerInstance(ann, deser6);
        assertNull(deser7);
    }

    @Test
    public void testCopy() throws Exception {
        Object id = new Object();
        Object id2 = new Object();

        ctx.findObjectId(id, new StdObjectIdGenerator(), new StdObjectIdResolver());
        ctx.findObjectId(id2, new StdObjectIdGenerator(), new StdObjectIdResolver());

        ctx._objectIds = null;
        assertEquals(ctx, copyCtx);

        ctx._objectIds = new LinkedHashMap<>();
        ctx._objectIdResolvers = Arrays.asList(new StdObjectIdResolver(), new StdObjectIdResolver());
        assertNotEquals(ctx, copyCtx);

        copyCtx._objectIds = new LinkedHashMap<>();
        copyCtx._objectIdResolvers = Arrays.asList(new StdObjectIdResolver(), new StdObjectIdResolver());
        assertEquals(ctx, copyCtx);

        ctx._objectIds = null;
        ctx._objectIdResolvers = null;
        assertNotEquals(ctx, copyCtx);
    }

    private static class StdObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectIdResolver newForDeserialization(DeserializationContext ctxt) {
            return this;
        }

        @Override
        public ObjectId findId(Object id, DeserializationContext ctxt) throws JsonMappingException {
            return ctxt.findObjectId(id, new StdObjectIdGenerator(), this);
        }
    }

    private static class StdObjectIdGenerator implements ObjectIdGenerator {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return IdKey.ROOT;
        }
    }

    private static class StdJsonDeserializer extends JsonDeserializer<Object> {
    }

    private static class StdKeyDeserializer extends KeyDeserializer {
    }

}