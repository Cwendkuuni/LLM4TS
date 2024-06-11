package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.fasterxml.jackson.databind.util.ObjectIdGenerator$AlwaysStableIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator$IdInt;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator$NoIdGenerator;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private DeserializerFactory factory;
    private DeserializerCache cache;
    private JsonParser parser;
    private InjectableValues injectableValues;
    private HandlerInstantiator handlerInstantiator;

    @Before
    public void setUp() throws Exception {
        factory = mock(DeserializerFactory.class);
        cache = mock(DeserializerCache.class);
        parser = mock(JsonParser.class);
        injectableValues = mock(InjectableValues.class);
        handlerInstantiator = mock(HandlerInstantiator.class);
        context = new DefaultDeserializationContext(factory, cache);
    }

    @Test
    public void testConstructor() {
        assertNotNull(context);
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        when(idGenerator.key((Object) 1)).thenReturn(new IdKey(1));
        when(idResolver.canUseFor(idResolver.getClass())).thenReturn(true);

        context._objectIds = new LinkedHashMap<>();
        context.findObjectId((Object) 1, idGenerator, idResolver);

        assertNotNull(context._objectIds);
        assertNotNull(context._objectIds.get(new IdKey(1)));
    }

    @Test
    public void testFindObjectIdNullIdGenerator() throws Exception {
        context.findObjectId(null, new ObjectIdGenerator<Object>() {}, new SimpleObjectIdResolver());
        assertNull(context._objectIds);
    }

    @Test
    public void testFindObjectIdNullIdResolver() throws Exception {
        context.findObjectId((Object) 1, new ObjectIdGenerator<Object>() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(1);
            }
        }, null);
        assertNull(context._objectIds);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        when(idGenerator.key((Object) 1)).thenReturn(new IdKey(1));
        when(idResolver.canUseFor(idResolver.getClass())).thenReturn(true);
        when(idResolver.newForDeserialization(context)).thenReturn(idResolver);

        Object objectId = new Object();
        when(idResolver.idFromObject(anyObject())).thenReturn(objectId);

        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = new ArrayList<>();

        context.findObjectId((Object) 1, idGenerator, idResolver);

        List<Referring> referringProperties = new ArrayList<>();
        referringProperties.add(new Referring("test", Object.class, new ObjectLocationTest()));
        when(idResolver.referringProperties()).thenReturn(referringProperties.iterator());

        context.checkUnresolvedObjectId();

        assertNull(context._objectIds);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithUnresolvedId() throws Exception {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        when(idGenerator.key((Object) 1)).thenReturn(new IdKey(1));
        when(idResolver.canUseFor(idResolver.getClass())).thenReturn(true);
        when(idResolver.newForDeserialization(context)).thenReturn(idResolver);

        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = new ArrayList<>();

        context.findObjectId((Object) 1, idGenerator, idResolver);

        List<Referring> referringProperties = new ArrayList<>();
        referringProperties.add(new Referring("test", Object.class, new ObjectLocationTest()));
        when(idResolver.referringProperties()).thenReturn(referringProperties.iterator());

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator$AlwaysStableIdGenerator();
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(deserializer.getClass()).thenReturn(JsonDeserializer.class);
        when(deserializer.resolve(any(DefaultDeserializationContext.class))).thenReturn(deserializer);

        Annotated ann = mock(Annotated.class);
        Object deserDef = new ObjectIdGenerator$IdInt(1);

        JsonDeserializer<Object> deser = context.deserializerInstance(ann, deserDef);

        assertNotNull(deser);
        assertSame(deserializer, deser);
    }

    @Test
    public void testDeserializerInstanceWithClass() throws Exception {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator$AlwaysStableIdGenerator();
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(deserializer.getClass()).thenReturn(JsonDeserializer.class);
        when(deserializer.resolve(any(DefaultDeserializationContext.class))).thenReturn(deserializer);

        Annotated ann = mock(Annotated.class);
        Object deserDef = JsonDeserializer.class;

        JsonDeserializer<Object> deser = context.deserializerInstance(ann, deserDef);

        assertNotNull(deser);
        assertSame(deserializer, deser);
    }

    @Test
    public void testDeserializerInstanceNullAnnotated() throws Exception {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator$AlwaysStableIdGenerator();
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(deserializer.getClass()).thenReturn(JsonDeserializer.class);
        when(deserializer.resolve(any(DefaultDeserializationContext.class))).thenReturn(deserializer);

        Object deserDef = new ObjectIdGenerator$IdInt(1);

        JsonDeserializer<Object> deser = context.deserializerInstance(null, deserDef);

        assertNotNull(deser);
        assertSame(deserializer, deser);
    }

    @Test
    public void testDeserializerInstanceNullDeserDef() throws Exception {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator$AlwaysStableIdGenerator();
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        Annotated ann = mock(Annotated.class);

        JsonDeserializer<Object> deser = context.deserializerInstance(ann, null);

        assertNull(deser);
    }

    @Test
    public void testDeserializerInstanceNoJsonDeserializerClass() throws Exception {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator$AlwaysStableIdGenerator();
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        Class<JsonDeserializer<Object>> deserDef = JsonDeserializer.class;

        JsonDeserializer<Object> deser = context.deserializerInstance(mock(Annotated.class), deserDef);

        assertNull(deser);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator$AlwaysStableIdGenerator();
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        when(keyDeserializer.getClass()).thenReturn(KeyDeserializer.class);
        when(keyDeserializer.resolve(any(DefaultDeserializationContext.class))).thenReturn(keyDeserializer);

        Annotated ann = mock(Annotated.class);
        Object deserDef = new ObjectIdGenerator$IdInt(1);

        KeyDeserializer keyDeser = context.keyDeserializerInstance(ann, deserDef);

        assertNotNull(keyDeser);
        assertSame(keyDeserializer, keyDeser);
    }

    @Test
    public void testKeyDeserializerInstanceWithClass() throws Exception {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator$AlwaysStableIdGenerator();
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        when(keyDeserializer.getClass()).thenReturn(KeyDeserializer.class);
        when(keyDeserializer.resolve(any(DefaultDeserializationContext.class))).thenReturn(keyDeserializer);

        Annotated ann = mock(Annotated.class);
        Object deserDef = KeyDeserializer.class;

        KeyDeserializer keyDeser = context.keyDeserializerInstance(ann, deserDef);

        assertNotNull(keyDeser);
        assertSame(keyDeserializer, keyDeser);
    }

    @Test
    public void testKeyDeserializerInstanceNullAnnotated() throws Exception {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator$AlwaysStableIdGenerator();
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        when(keyDeserializer.getClass()).thenReturn(KeyDeserializer.class);
        when(keyDeserializer.resolve(any(DefaultDeserializationContext.class))).thenReturn(keyDeserializer);

        Object deserDef = new ObjectIdGenerator$IdInt(1);

        KeyDeserializer keyDeser = context.keyDeserializerInstance(null, deserDef);

        assertNotNull(keyDeser);
        assertSame(keyDeserializer, keyDeser);
    }

    @Test
    public void testKeyDeserializerInstanceNullDeserDef() throws Exception {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator$AlwaysStableIdGenerator();
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        Annotated ann = mock(Annotated.class);

        KeyDeserializer keyDeser = context.keyDeserializerInstance(ann, null);

        assertNull(keyDeser);
    }

    @Test
    public void testKeyDeserializerInstanceNoKeyDeserializerClass() throws Exception {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator$AlwaysStableIdGenerator();
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        Class<KeyDeserializer> keyDeserializerClass = KeyDeserializer.class;

        KeyDeserializer keyDeser = context.keyDeserializerInstance(mock(Annotated.class), keyDeserializerClass);

        assertNull(keyDeser);
    }

    static class ObjectLocationTest {
        // Empty class used as a location marker
    }
}