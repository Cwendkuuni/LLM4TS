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
import com.fasterxml.jackson.databind.util.TestBase;

import java.util.*;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest extends TestBase {

    // Test case for findObjectId method with null id, null generator and null resolver
    @Test
    public void testFindObjectIdNullIdNullGeneratorNullResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator<?> gen = null;
        ObjectIdResolver resolver = null;
        Object id = null;
        ReadableObjectId roid = context.findObjectId(id, gen, resolver);
        assertNull(roid);
    }

    // Test case for findObjectId method with null id, null generator but valid resolver
    @Test
    public void testFindObjectIdNullIdNullGeneratorValidResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator<?> gen = null;
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object id = null;
        ReadableObjectId roid = context.findObjectId(id, gen, resolver);
        assertNull(roid);
    }

    // Test case for findObjectId method with null id, valid generator and null resolver
    @Test
    public void testFindObjectIdNullIdValidGeneratorNullResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey();
            }
        };
        ObjectIdResolver resolver = null;
        Object id = null;
        ReadableObjectId roid = context.findObjectId(id, gen, resolver);
        assertNull(roid);
    }

    // Test case for findObjectId method with valid id, null generator and null resolver
    @Test
    public void testFindObjectIdValidIdNullGeneratorNullResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator<?> gen = null;
        ObjectIdResolver resolver = null;
        Object id = new Object();
        ReadableObjectId roid = context.findObjectId(id, gen, resolver);
        assertNull(roid);
    }

    // Test case for findObjectId method with valid id, null generator but valid resolver
    @Test
    public void testFindObjectIdValidIdNullGeneratorValidResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator<?> gen = null;
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object id = new Object();
        ReadableObjectId roid = context.findObjectId(id, gen, resolver);
        assertNull(roid);
    }

    // Test case for findObjectId method with valid id, valid generator and null resolver
    @Test
    public void testFindObjectIdValidIdValidGeneratorNullResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey();
            }
        };
        ObjectIdResolver resolver = null;
        Object id = new Object();
        ReadableObjectId roid = context.findObjectId(id, gen, resolver);
        assertNull(roid);
    }

    // Test case for findObjectId method with valid id, valid generator and valid resolver
    @Test
    public void testFindObjectIdValidIdValidGeneratorValidResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey();
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object id = new Object();
        ReadableObjectId roid = context.findObjectId(id, gen, resolver);
        assertNotNull(roid);
    }

    // Test case for checkUnresolvedObjectId method with empty objectIds
    @Test
    public void testCheckUnresolvedObjectIdEmptyObjectIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context.checkUnresolvedObjectId();
    }

    // Test case for checkUnresolvedObjectId method with unresolved forward references
    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdUnresolvedForwardReferences() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new IdKey(), new ReadableObjectId(new IdKey()));
        context.checkUnresolvedObjectId();
    }

    // Test case for deserializerInstance method with null deserDef
    @Test
    public void testDeserializerInstanceNullDeserDef() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object deserDef = null;
        JsonDeserializer<Object> deser = context.deserializerInstance(null, deserDef);
        assertNull(deser);
    }

    // Test case for deserializerInstance method with valid deserDef of type JsonDeserializer
    @Test
    public void testDeserializerInstanceJsonDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        JsonDeserializer<Object> deserDef = new JsonDeserializer<Object>() {};
        JsonDeserializer<Object> deser = context.deserializerInstance(null, deserDef);
        assertSame(deserDef, deser);
    }

    // Test case for deserializerInstance method with valid deserDef of type Class<JsonDeserializer>
    @Test
    public void testDeserializerInstanceClassJsonDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Class<JsonDeserializer<Object>> deserDef = JsonDeserializer.class;
        JsonDeserializer<Object> deser = context.deserializerInstance(null, deserDef);
        assertInstanceOf(JsonDeserializer.class, deser);
    }

    // Test case for keyDeserializerInstance method with null deserDef
    @Test
    public void testKeyDeserializerInstanceNullDeserDef() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object deserDef = null;
        KeyDeserializer deser = context.keyDeserializerInstance(null, deserDef);
        assertNull(deser);
    }

    // Test case for keyDeserializerInstance method with valid deserDef of type KeyDeserializer
    @Test
    public void testKeyDeserializerInstanceKeyDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        KeyDeserializer deserDef = new KeyDeserializer() {};
        KeyDeserializer deser = context.keyDeserializerInstance(null, deserDef);
        assertSame(deserDef, deser);
    }

    // Test case for keyDeserializerInstance method with valid deserDef of type Class<KeyDeserializer>
    @Test
    public void testKeyDeserializerInstanceClassKeyDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Class<KeyDeserializer> deserDef = KeyDeserializer.class;
        KeyDeserializer deser = context.keyDeserializerInstance(null, deserDef);
        assertInstanceOf(KeyDeserializer.class, deser);
    }
}