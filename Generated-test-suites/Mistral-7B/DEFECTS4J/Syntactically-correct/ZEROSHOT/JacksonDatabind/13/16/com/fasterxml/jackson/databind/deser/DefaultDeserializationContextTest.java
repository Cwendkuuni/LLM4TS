package com.example.jackson.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.After;
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
    private DefaultDeserializationContext.Impl impl;
    private HandlerInstantiator handlerInstantiator;
    private JsonParser parser;
    private InjectableValues injectableValues;
    private DeserializerFactory deserializerFactory;
    private DeserializerCache deserializerCache;

    @Before
    public void setUp() throws Exception {
        handlerInstantiator = mock(HandlerInstantiator.class);
        parser = mock(JsonParser.class);
        injectableValues = mock(InjectableValues.class);
        deserializerFactory = mock(DeserializerFactory.class);
        deserializerCache = mock(DeserializerCache.class);
        context = new DefaultDeserializationContext(deserializerFactory, deserializerCache);
        impl = new DefaultDeserializationContext.Impl(context);
        when(handlerInstantiator.deserializerInstance(any(DeserializationConfig.class), any(Annotated.class), eq(JsonDeserializer.class)))
                .thenReturn(mock(JsonDeserializer.class));
    }

    @After
    public void tearDown() throws Exception {
        context = null;
        impl = null;
        handlerInstantiator = null;
        parser = null;
        injectableValues = null;
        deserializerFactory = null;
        deserializerCache = null;
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectIdGenerator<?> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);
        Object objectId = new Object();

        when(idGenerator.key(anyObject()))
                .thenReturn(new IdKey(Object.class, "id"));
        when(idResolver.canUseFor(eq(SimpleObjectIdResolver.class)))
                .thenReturn(true);
        when(idResolver.newForDeserialization(any(DefaultDeserializationContext.class)))
                .thenReturn(idResolver);

        context.findObjectId(objectId, idGenerator, idResolver);
        verify(context).findObjectId(objectId, idGenerator, idResolver);
    }

    @Test
    public void testFindObjectIdWithNullIdGenerator() throws Exception {
        assertNull(context.findObjectId(null, new ObjectIdGenerator<Object>() {}));
    }

    @Test
    public void testFindObjectIdWithNullIdResolver() throws Exception {
        ObjectIdGenerator<?> idGenerator = mock(ObjectIdGenerator.class);
        Object objectId = new Object();

        when(idGenerator.key(anyObject()))
                .thenReturn(new IdKey(Object.class, "id"));

        context.findObjectId(objectId, idGenerator, (ObjectIdResolver) null);
        verifyNoMoreInteractions(context);
    }

    @Test
    public void testFindObjectIdWithNullId() throws Exception {
        ObjectIdGenerator<?> idGenerator = mock(ObjectIdGenerator.class);

        context.findObjectId(null, idGenerator, new SimpleObjectIdResolver());
        verifyNoMoreInteractions(context);
    }

    @Test
    public void testFindObjectIdWithExistingId() throws Exception {
        ObjectIdGenerator<?> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);
        Object objectId = new Object();
        ReadableObjectId entry = new ReadableObjectId(new IdKey(Object.class, "id"));

        when(idGenerator.key(anyObject()))
                .thenReturn(new IdKey(Object.class, "id"));
        when(idResolver.canUseFor(eq(SimpleObjectIdResolver.class)))
                .thenReturn(true);
        when(idResolver.newForDeserialization(any(DefaultDeserializationContext.class)))
                .thenReturn(idResolver);
        when(context._objectIds).thenReturn(new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>() {{
            put(new IdKey(Object.class, "id"), entry);
        }});

        Object result = context.findObjectId(objectId, idGenerator, idResolver);
        assertNotNull(result);
        assertEquals(entry, result);
    }

    @Test
    public void testFindObjectIdWithNonExistingId() throws Exception {
        ObjectIdGenerator<?> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);
        Object objectId = new Object();

        when(idGenerator.key(anyObject()))
                .thenReturn(new IdKey(Object.class, "id"));
        when(idResolver.canUseFor(eq(SimpleObjectIdResolver.class)))
                .thenReturn(true);
        when(idResolver.newForDeserialization(any(DefaultDeserializationContext.class)))
                .thenReturn(idResolver);
        when(context._objectIds).thenReturn(Collections.emptyMap());

        Object result = context.findObjectId(objectId, idGenerator, idResolver);
        assertNull(result);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator<?> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);
        ReadableObjectId entry = new ReadableObjectId(new IdKey(Object.class, "id"));

        when(idGenerator.key(anyObject()))
                .thenReturn(new IdKey(Object.class, "id"));
        when(idResolver.canUseFor(eq(SimpleObjectIdResolver.class)))
                .thenReturn(true);
        when(idResolver.newForDeserialization(any(DefaultDeserializationContext.class)))
                .thenReturn(idResolver);
        when(context._objectIds).thenReturn(new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>() {{
            put(new IdKey(Object.class, "id"), entry);
        }});

        context.checkUnresolvedObjectId();
        verifyNoMoreInteractions(context);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithUnresolvedObjectId() throws Exception {
        ObjectIdGenerator<?> idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);
        ReadableObjectId entry = new ReadableObjectId(new IdKey(Object.class, "id"));

        when(idGenerator.key(anyObject()))
                .thenReturn(new IdKey(Object.class, "id"));
        when(idResolver.canUseFor(eq(SimpleObjectIdResolver.class)))
                .thenReturn(true);
        when(idResolver.newForDeserialization(any(DefaultDeserializationContext.class)))
                .thenReturn(idResolver);
        when(context._objectIds).thenReturn(new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>() {{
            put(new IdKey(Object.class, "id"), entry);
        }});

        doThrow(new UnresolvedForwardReference("Test"))
                .when(entry)
                .getUnresolvedId(anyString(), any(Class.class), any(Object.class));

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new JsonDeserializer<Object>() {};

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
        assertNotNull(result);
        verify(handlerInstantiator).deserializerInstance(eq(_config), eq(ann), eq(JsonDeserializer.class));
    }

    @Test
    public void testDeserializerInstanceWithNullAnnotated() throws Exception {
        Object deserDef = new JsonDeserializer<Object>() {};

        JsonDeserializer<Object> result = context.deserializerInstance(null, deserDef);
        assertNull(result);
    }

    @Test
    public void testDeserializerInstanceWithNullDeserializerDef() throws Exception {
        JsonDeserializer<Object> result = context.deserializerInstance(mock(Annotated.class), null);
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new KeyDeserializer() {};

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(result);
        verify(handlerInstantiator).keyDeserializerInstance(eq(_config), eq(ann), eq(KeyDeserializer.class));
    }

    @Test
    public void testKeyDeserializerInstanceWithNullAnnotated() throws Exception {
        Object deserDef = new KeyDeserializer() {};

        KeyDeserializer result = context.keyDeserializerInstance(null, deserDef);
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstanceWithNullDeserializerDef() throws Exception {
        KeyDeserializer result = context.keyDeserializerInstance(mock(Annotated.class), null);
        assertNull(result);
    }

    @Test
    public void testWith() throws Exception {
        DefaultDeserializationContext result = impl.with(deserializerFactory);
        assertNotNull(result);
        verify(impl).createInstance(_config, parser, injectableValues);
        verify(impl).copy();
        assertEquals(deserializerFactory, result.getFactory());
    }

    @Test
    public void testCreateInstance() throws Exception {
        DefaultDeserializationContext result = impl.createInstance(_config, parser, injectableValues);
        assertNotNull(result);
        verify(impl, times(2)).copy();
    }
}