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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;
import java.util.Map.Entry;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private DeserializerFactory factory;
    private DeserializationConfig config;
    private JsonParser parser;
    private InjectableValues values;

    @Before
    public void setup() {
        factory = mock(DeserializerFactory.class);
        config = mock(DeserializationConfig.class);
        parser = mock(JsonParser.class);
        values = mock(InjectableValues.class);

        context = new DefaultDeserializationContext(factory, config) {
            @Override
            public DefaultDeserializationContext copy() { return new Impl(this); }
            @Override
            public DefaultDeserializationContext createInstance(DeserializationConfig config,
                    JsonParser jp, InjectableValues values) {
                return new Impl(this, config, jp, values);
            }
            @Override
            public DefaultDeserializationContext with(DeserializerFactory factory) {
                return new Impl(this, factory);
            }
        };
    }

    @Test
    public void testFindObjectId_NullId() {
        when(config.getHandlerInstantiator()).thenReturn(null);
        assertNull(context.findObjectId(null, null, SimpleObjectIdResolver.class));
    }

    @Test
    public void testFindObjectId_NullGenerator() {
        when(config.getHandlerInstantiator()).thenReturn(null);
        ObjectIdGenerator<?> generator = null;
        ObjectIdResolver resolverType = SimpleObjectIdResolver.class;
        assertNull(context.findObjectId(null, generator, resolverType));
    }

    @Test
    public void testFindObjectId_EmptyObjectIds() {
        assertFalse(context._objectIds.isEmpty());
        context._objectIds = new LinkedHashMap<>();
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        ObjectIdGenerator.IdKey key = new IdKey();
        ReadableObjectId roid = new ReadableObjectId(key);
        roid.setResolver(resolver);
        context._objectIds.put(key, roid);

        Object id = new Object();
        Object expected = roid;
        assertSame(expected, context.findObjectId(id, generator, resolverType));
    }

    @Test
    public void testFindObjectId_EmptyObjectIdResolvers() {
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        ObjectIdGenerator.IdKey key = new IdKey();
        ReadableObjectId roid = new ReadableObjectId(key);
        roid.setResolver(resolver);

        when(config.getHandlerInstantiator()).thenReturn(mock(HandlerInstantiator.class));
        when(config.getHandlerInstantiator().keyDeserializerInstance(any(Annotated.class), any(Class.class)))
                .thenReturn(mock(KeyDeserializer.class));
        when(config.getHandlerInstantiator().deserializerInstance(any(Annotated.class), any(Object.class)))
                .thenReturn(mock(JsonDeserializer.class));

        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = new ArrayList<>();

        Object id = new Object();
        Object expected = roid;
        when(resolver.getIdKey(any(Annotated.class))).thenReturn(key);
        when(resolver.canUseFor(resolverType)).thenReturn(true);
        assertSame(expected, context.findObjectId(id, generator, resolverType));

        List<ObjectIdResolver> resolvers = new ArrayList<>();
        resolvers.add(resolver);
        when(config.getObjectIdResolver()).thenReturn(resolvers);

        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        ObjectIdGenerator.IdKey key2 = new IdKey();
        ReadableObjectId roid2 = new ReadableObjectId(key2);
        roid2.setResolver(mock(ObjectIdResolver.class));
        context._objectIds.put(key2, roid2);

        Object id2 = new Object();
        Object expected2 = roid2;
        when(resolver.getIdKey(any(Annotated.class))).thenReturn(key2);
        assertSame(expected2, context.findObjectId(id2, gen, resolverType));
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testFindObjectId_ReferringProperties() {
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        ObjectIdGenerator.IdKey key = new IdKey();
        ReadableObjectId roid = new ReadableObjectId(key);
        roid.setResolver(resolver);

        when(config.getHandlerInstantiator()).thenReturn(mock(HandlerInstantiator.class));
        when(config.getHandlerInstantiator().keyDeserializerInstance(any(Annotated.class), any(Class.class)))
                .thenReturn(mock(KeyDeserializer.class));
        when(config.getHandlerInstantiator().deserializerInstance(any(Annotated.class), any(Object.class)))
                .thenReturn(mock(JsonDeserializer.class));

        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = new ArrayList<>();

        Object id = new Object();
        Object expected = roid;
        when(resolver.getIdKey(any(Annotated.class))).thenReturn(key);
        when(resolver.canUseFor(resolverType)).thenReturn(true);
        assertSame(expected, context.findObjectId(id, generator, resolverType));

        List<ObjectIdResolver> resolvers = new ArrayList<>();
        resolvers.add(resolver);
        when(config.getObjectIdResolver()).thenReturn(resolvers);

        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        ObjectIdGenerator.IdKey key2 = new IdKey();
        ReadableObjectId roid2 = new ReadableObjectId(key2);
        roid2.setResolver(mock(ObjectIdResolver.class));
        roid2.addReferringProperty(new Referring(roid, null));
        context._objectIds.put(key2, roid2);

        Object id2 = new Object();
        Object expected2 = roid2;
        when(resolver.getIdKey(any(Annotated.class))).thenReturn(key2);
        assertThrows(UnresolvedForwardReference.class, () -> context.findObjectId(id2, gen, resolverType));
    }

    @Test
    public void testDeserializerInstance_JsonDeserializer() {
        Annotated ann = mock(Annotated.class);
        Object deserDef = mock(JsonDeserializer.class);

        JsonDeserializer<?> deser = context.deserializerInstance(ann, deserDef);
        assertSame(deserDef, deser);
    }

    @Test
    public void testDeserializerInstance_ClassJsonDeserializer() {
        Annotated ann = mock(Annotated.class);
        Object deserDef = JsonDeserializer.class;

        JsonDeserializer<?> deser = context.deserializerInstance(ann, deserDef);
        assertInstanceOf(JsonDeserializer.class, deser);
    }

    @Test
    public void testDeserializerInstance_ClassKeyDeserializer() {
        Annotated ann = mock(Annotated.class);
        Object deserDef = KeyDeserializer.class;

        KeyDeserializer deser = context.deserializerInstance(ann, deserDef);
        assertInstanceOf(KeyDeserializer.class, deser);
    }

    @Test
    public void testDeserializerInstance_NullDeserDef() {
        Annotated ann = mock(Annotated.class);
        Object deserDef = null;

        JsonDeserializer<?> deser = context.deserializerInstance(ann, deserDef);
        assertNull(deser);
    }

    @Test
    public void testDeserializerInstance_ClassBogus() {
        Annotated ann = mock(Annotated.class);
        Object deserDef = BogusClass.class;

        JsonDeserializer<?> deser = context.deserializerInstance(ann, deserDef);
        assertNull(deser);
    }

    @Test
    public void testDeserializerInstance_ClassJsonDeserializerWithHi() {
        Annotated ann = mock(Annotated.class);
        Object deserDef = JsonDeserializer.class;

        HandlerInstantiator hi = mock(HandlerInstantiator.class);
        when(config.getHandlerInstantiator()).thenReturn(hi);
        when(hi.deserializerInstance(config, ann, JsonDeserializer.class)).thenReturn(mock(JsonDeserializer.class));

        JsonDeserializer<?> deser = context.deserializerInstance(ann, deserDef);
        assertSame(mock(JsonDeserializer.class), deser);
    }

    @Test
    public void testDeserializerInstance_ClassJsonDeserializerWithoutHi() {
        Annotated ann = mock(Annotated.class);
        Object deserDef = JsonDeserializer.class;

        when(config.getHandlerInstantiator()).thenReturn(null);

        JsonDeserializer<?> deser = context.deserializerInstance(ann, deserDef);
        assertNull(deser);
    }

    @Test
    public void testDeserializerInstance_ClassKeyDeserializerWithHi() {
        Annotated ann = mock(Annotated.class);
        Object deserDef = KeyDeserializer.class;

        HandlerInstantiator hi = mock(HandlerInstantiator.class);
        when(config.getHandlerInstantiator()).thenReturn(hi);
        when(hi.keyDeserializerInstance(config, ann, KeyDeserializer.class)).thenReturn(mock(KeyDeserializer.class));

        KeyDeserializer deser = context.deserializerInstance(ann, deserDef);
        assertSame(mock(KeyDeserializer.class), deser);
    }

    @Test
    public void testDeserializerInstance_ClassKeyDeserializerWithoutHi() {
        Annotated ann = mock(Annotated.class);
        Object deserDef = KeyDeserializer.class;

        when(config.getHandlerInstantiator()).thenReturn(null);

        KeyDeserializer deser = context.deserializerInstance(ann, deserDef);
        assertNull(deser);
    }
}