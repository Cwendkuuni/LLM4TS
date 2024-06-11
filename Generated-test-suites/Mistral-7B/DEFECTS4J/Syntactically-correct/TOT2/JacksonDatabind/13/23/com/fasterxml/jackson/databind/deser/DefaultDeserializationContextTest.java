package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
    }

    // Test for findObjectId method
    @Test
    public void testFindObjectId() throws JsonProcessingException {
        ObjectIdGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(String.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object id = new Object();
        IdKey key = generator.key(id);
        ReadableObjectId objectId = context.findObjectId(id, generator, resolver);
        assertNotNull(objectId);
        assertEquals(key, objectId.getKey());
    }

    // Test for checkUnresolvedObjectId method
    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws JsonProcessingException {
        context.findObjectId(null, null, null);
        context.checkUnresolvedObjectId();
    }

    // Test for deserializerInstance method
    @Test
    public void testDeserializerInstance() throws JsonProcessingException {
        AnnotatedClass annotatedClass = mapper.getAnnotationIntrospector().findProperties(MyClass.class, null);
        JsonDeserializer deserializer = mapper.getDeserializationConfig().findValueDeserializer(MyClass.class, null);
        Object deserDef = deserializer;
        JsonDeserializer result = context.deserializerInstance(annotatedClass, deserDef);
        assertEquals(deserializer, result);
    }

    // Test for keyDeserializerInstance method
    @Test
    public void testKeyDeserializerInstance() throws JsonProcessingException {
        AnnotatedClass annotatedClass = mapper.getAnnotationIntrospector().findProperties(MyClass.class, null);
        KeyDeserializer keyDeserializer = mapper.getDeserializationConfig().findKeyDeserializer(MyClass.class, null);
        Object deserDef = keyDeserializer;
        KeyDeserializer result = context.keyDeserializerInstance(annotatedClass, deserDef);
        assertEquals(keyDeserializer, result);
    }

    // Test for with method
    @Test
    public void testWithMethod() throws JsonProcessingException {
        ObjectMapper newMapper = new ObjectMapper();
        DefaultDeserializationContext newContext = newMapper.getDeserializationContext();
        DefaultDeserializationContext newContextWithFactory = newContext.with(newJacksonStdDeserializerFactory());
        assertNotSame(context, newContextWithFactory);
    }

    // Test for createInstance method
    @Test
    public void testCreateInstanceMethod() throws JsonProcessingException {
        ObjectMapper newMapper = new ObjectMapper();
        DefaultDeserializationContext newContext = newMapper.getDeserializationContext();
        JsonParser parser = new JsonFactory().createParser("{ \"name\": \"John\" }");
        InjectableValues values = new StandardInjectableValues();
        DefaultDeserializationContext createdContext = newContext.createInstance(mapper.getDeserializationConfig(), parser, values);
        assertNotSame(newContext, createdContext);
    }

    // Test for copy method
    @Test
    public void testCopyMethod() throws JsonProcessingException {
        DefaultDeserializationContext originalContext = context;
        DefaultDeserializationContext copiedContext = originalContext.copy();
        assertNotSame(originalContext, copiedContext);
        assertEquals(originalContext.getObjectIdResolverList(), copiedContext.getObjectIdResolverList());
    }

    // Test for edge cases and exception handling
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() throws JsonProcessingException {
        context.copy();
    }

    // Test for null arguments
    @Test
    public void testNullArguments() {
        assertNull(context.findObjectId(null, null, null));
        try {
            context.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            // expected
        }
        assertNull(context.deserializerInstance(null, null));
        assertNull(context.keyDeserializerInstance(null, null));
    }

    // Test for empty collections
    @Test
    public void testEmptyCollections() throws JsonProcessingException {
        context.findObjectId(null, null, Collections.emptyList());
        context.checkUnresolvedObjectId();
        JsonDeserializer deserializer = mapper.getDeserializationConfig().findValueDeserializer(String.class, null);
        Object deserDef = deserializer;
        JsonDeserializer result = context.deserializerInstance(null, deserDef);
        assertEquals(deserializer, result);
        KeyDeserializer keyDeserializer = mapper.getDeserializationConfig().findKeyDeserializer(String.class, null);
        Object keyDef = keyDeserializer;
        KeyDeserializer resultKey = context.keyDeserializerInstance(null, keyDef);
        assertEquals(keyDeserializer, resultKey);
    }

    // Test for invalid arguments
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() throws JsonProcessingException {
        context.findObjectId("", null, null);
        context.checkUnresolvedObjectId();
        JsonDeserializer deserializer = mapper.getDeserializationConfig().findValueDeserializer(Object.class, null);
        Object deserDef = deserializer;
        JsonDeserializer result = context.deserializerInstance(null, deserDef);
        KeyDeserializer keyDeserializer = mapper.getDeserializationConfig().findKeyDeserializer(Object.class, null);
        Object keyDef = keyDeserializer;
        KeyDeserializer resultKey = context.keyDeserializerInstance(null, keyDef);
    }

    // Test for circular references
    @Test(expected = UnresolvedForwardReference.class)
    public void testCircularReferences() throws JsonProcessingException {
        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(Object.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context.findObjectId(id1, generator, resolver);
        context.findObjectId(id2, generator, resolver);
        context.findObjectId(id1, generator, resolver);
        context.checkUnresolvedObjectId();
    }

    // Test for missing keys
    @Test(expected = UnresolvedForwardReference.class)
    public void testMissingKeys() throws JsonProcessingException {
        ObjectIdGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(String.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context.findObjectId(null, generator, resolver);
        context.checkUnresolvedObjectId();
    }

    // Test for null objects
    @Test
    public void testNullObjects() throws JsonProcessingException {
        ObjectIdGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(Object.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context.findObjectId(null, generator, resolver);
        context.checkUnresolvedObjectId();
    }

    // Test for empty objects
    @Test
    public void testEmptyObjects() throws JsonProcessingException {
        ObjectIdGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(Object.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context.findObjectId(new Object(), generator, resolver);
        context.checkUnresolvedObjectId();
    }

    // Test for non-serializable objects
    @Test(expected = IllegalStateException.class)
    public void testNonSerializableObjects() throws JsonProcessingException {
        ObjectIdGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(Thread.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context.findObjectId(new Thread(), generator, resolver);
    }

    private static class MyClass {
        @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
        private int id;
        private String name;
    }

    private static ObjectIdGenerator.ObjectIdGeneratorFactory newJacksonStdDeserializerFactory() {
        return new StandardObjectIdGeneratorFactory();
    }
}