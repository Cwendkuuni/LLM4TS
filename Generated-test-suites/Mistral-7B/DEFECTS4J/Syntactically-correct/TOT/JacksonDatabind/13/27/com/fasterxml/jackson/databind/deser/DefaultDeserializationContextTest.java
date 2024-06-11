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
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test with null id and SimpleObjectIdResolver
        Object id = null;
        ObjectIdGenerator<Object> gen = new SimpleObjectIdGenerator();
        ReadableObjectId result = context.findObjectId(id, gen, SimpleObjectIdResolver.class);
        assertNull(result);

        // Test with valid id and SimpleObjectIdResolver
        id = new Object();
        gen = new SimpleObjectIdGenerator();
        result = context.findObjectId(id, gen, SimpleObjectIdResolver.class);
        assertNotNull(result);
        assertEquals(IdKey.STD, result.getKey());

        // Test with valid id and custom ObjectIdResolver
        ObjectIdResolver resolver = new CustomObjectIdResolver();
        result = context.findObjectId(id, gen, resolver.getClass());
        assertNotNull(result);
        assertEquals(IdKey.CUSTOM, result.getKey());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdGenerator<Object> gen = new SimpleObjectIdGenerator();

        context.findObjectId(id1, gen, SimpleObjectIdResolver.class);
        context.findObjectId(id2, gen, SimpleObjectIdResolver.class);

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test with null deserDef
        JsonDeserializer<Object> result = context.deserializerInstance(null, null);
        assertNull(result);

        // Test with JsonDeserializer
        JsonDeserializer<String> deser = new StringDeserializer();
        result = context.deserializerInstance(new AnnotatedClass(), deser);
        assertEquals(String.class, result.getClass());

        // Test with Class<JsonDeserializer>
        Class<JsonDeserializer<Integer>> deserClass = JsonDeserializer.class;
        result = context.deserializerInstance(new AnnotatedClass(), deserClass);
        assertEquals(Integer.class, result.getType());
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test with null deserDef
        KeyDeserializer result = context.keyDeserializerInstance(null, null);
        assertNull(result);

        // Test with KeyDeserializer
        KeyDeserializer deser = new StringKeyDeserializer();
        result = context.keyDeserializerInstance(new AnnotatedClass(), deser);
        assertEquals(String.class, result.getClass());

        // Test with Class<KeyDeserializer>
        Class<KeyDeserializer<Integer>> deserClass = KeyDeserializer.class;
        result = context.keyDeserializerInstance(new AnnotatedClass(), deserClass);
        assertEquals(Integer.class, result.getType());
    }

    static class AnnotatedClass extends Annotated {
        @Override
        public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Class<?> getClassId() {
            return Object.class;
        }
    }

    static class StringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    static class StringKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    static class CustomObjectIdResolver implements ObjectIdResolver {
        @Override
        public boolean canUseFor(DeserializationContext ctxt) {
            return true;
        }

        @Override
        public ObjectIdResolver newForDeserialization(DeserializationContext ctxt) {
            return this;
        }

        @Override
        public ReadableObjectId idEvent(DeserializationContext ctxt) {
            return null;
        }
    }

    static class SimpleObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return IdKey.STD;
        }
    }
}