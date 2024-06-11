package com.example.junit;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.*;
import org.mockito.*;

/**
 * Test class for DefaultDeserializationContext.
 */
public class DefaultDeserializationContextTest {

    @Before
    public void setUp() {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
        DeserializerFactory deserializerFactory = Mockito.mock(DeserializerFactory.class);
        DeserializerCache deserializerCache = Mockito.mock(DeserializerCache.class);

        when(objectMapper.getDeserializationConfig()).thenReturn(Mockito.mock(DeserializationConfig.class));
        when(objectMapper.getInjectableValues()).thenReturn(Mockito.mock(InjectableValues.class));

        DefaultDeserializationContext context = new DefaultDeserializationContext(deserializerFactory, deserializerCache);
        context.setObjectMapper(objectMapper);

        when(deserializerFactory.findValueDeserializer(any(Annotated.class), any(Object.class)))
                .thenReturn(Mockito.mock(JsonDeserializer.class));
        when(deserializerFactory.findKeyDeserializer(any(Annotated.class), any(Object.class)))
                .thenReturn(Mockito.mock(KeyDeserializer.class));
        when(deserializerFactory.findValueDeserializer(any(Annotated.class), eq(ObjectIdGenerator.class)))
                .thenReturn(Mockito.mock(JsonDeserializer.class));

        when(objectIdGenerator.key(any(Object.class))).thenReturn(new IdKey(1));
        when(objectIdResolver.newForDeserialization(context)).thenReturn(objectIdResolver);

        context.setObjectIdGenerator(objectIdGenerator);
        context.setObjectIdResolver(objectIdResolver);
    }

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);

        context.setObjectIdGenerator(objectIdGenerator);
        context.setObjectIdResolver(objectIdResolver);

        ObjectIdGenerator.IdKey idKey = new IdKey(1);

        ReadableObjectId result = context.findObjectId(new Object(), objectIdGenerator, ObjectIdGenerator.class);
        assertNotNull(result);

        verify(objectIdGenerator, times(1)).key(any(Object.class));
        verify(objectIdResolver, times(1)).newForDeserialization(context);
    }

    @Test
    public void testFindObjectIdWithSimpleObjectIdResolver() {
        ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);

        context.setObjectIdGenerator(objectIdGenerator);
        context.setObjectIdResolver(objectIdResolver);

        ObjectIdGenerator.IdKey idKey = new IdKey(1);

        ReadableObjectId result = context.findObjectId(new Object(), objectIdGenerator);
        assertNotNull(result);

        verify(objectIdGenerator, times(1)).key(any(Object.class));
        verify(objectIdResolver, never()).newForDeserialization(context);

        ObjectIdGenerator noIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        context.setObjectIdGenerator(noIdGenerator);

        try {
            context.findObjectId(new Object(), ObjectIdGenerator.class);
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference ufe) {
            // Expected
        }

        verify(objectIdGenerator, times(1)).key(any(Object.class));
        verify(objectIdResolver, never()).newForDeserialization(context);
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);

        context.setObjectIdGenerator(objectIdGenerator);
        context.setObjectIdResolver(objectIdResolver);

        ObjectIdGenerator.IdKey idKey = new IdKey(1);
        ReadableObjectId readableObjectId = new ReadableObjectId(idKey);

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(idKey, readableObjectId);

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference ufe) {
            // Expected
        }

        verify(objectIdGenerator, times(1)).key(any(Object.class));
        verify(objectIdResolver, times(1)).newForDeserialization(context);

        ObjectIdGenerator noIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        context.setObjectIdGenerator(noIdGenerator);

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference ufe) {
            // Expected
        }

        verify(objectIdGenerator, times(1)).key(any(Object.class));
        verify(objectIdResolver, never()).newForDeserialization(context);
    }

    @Test
    public void testDeserializerInstance() {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
        DeserializerFactory deserializerFactory = Mockito.mock(DeserializerFactory.class);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        JsonParser jp = Mockito.mock(JsonParser.class);
        InjectableValues values = Mockito.mock(InjectableValues.class);

        when(objectMapper.getDeserializationConfig()).thenReturn(config);
        when(objectMapper.getInjectableValues()).thenReturn(values);

        when(config.getHandlerInstantiator()).thenReturn(null);

        when(deserializerFactory.findValueDeserializer(any(Annotated.class), any(Object.class)))
                .thenReturn(Mockito.mock(JsonDeserializer.class));

        DefaultDeserializationContext context = new DefaultDeserializationContext(deserializerFactory, null);

        Annotated ann = Mockito.mock(Annotated.class);
        Object deserDef = Mockito.mock(JsonDeserializer.class);

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
        assertNotNull(result);

        verify(deserializerFactory, times(1)).findValueDeserializer(any(Annotated.class), any(Object.class));

        when(config.getHandlerInstantiator()).thenReturn(Mockito.mock(HandlerInstantiator.class));

        result = context.deserializerInstance(ann, deserDef);
        assertNotNull(result);

        verify(config.getHandlerInstantiator(), times(1)).deserializerInstance(eq(config), ann, eq(deserDef.getClass()));
    }

    @Test
    public void testKeyDeserializerInstance() {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
        DeserializerFactory deserializerFactory = Mockito.mock(DeserializerFactory.class);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        JsonParser jp = Mockito.mock(JsonParser.class);
        InjectableValues values = Mockito.mock(InjectableValues.class);

        when(objectMapper.getDeserializationConfig()).thenReturn(config);
        when(objectMapper.getInjectableValues()).thenReturn(values);

        when(config.getHandlerInstantiator()).thenReturn(null);

        when(deserializerFactory.findKeyDeserializer(any(Annotated.class), any(Object.class)))
                .thenReturn(Mockito.mock(KeyDeserializer.class));

        DefaultDeserializationContext context = new DefaultDeserializationContext(deserializerFactory, null);

        Annotated ann = Mockito.mock(Annotated.class);
        Object deserDef = Mockito.mock(KeyDeserializer.class);

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(result);

        verify(deserializerFactory, times(1)).findKeyDeserializer(any(Annotated.class), any(Object.class));

        when(config.getHandlerInstantiator()).thenReturn(Mockito.mock(HandlerInstantiator.class));

        result = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(result);

        verify(config.getHandlerInstantiator(), times(1)).keyDeserializerInstance(eq(config), ann, eq(deserDef.getClass()));
    }
}