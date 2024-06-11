package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class DefaultDeserializationContextTest extends BaseTest {

    private DefaultDeserializationContext _ctx;
    private ObjectMapper _mapper;

    @Before
    public void setup() throws Exception {
        _mapper = new ObjectMapper();
        _ctx = _mapper.getDeserializationContext();
    }

    // Basic test cases for methods in DefaultDeserializationContext

    @Test
    public void testFindObjectId() throws JsonMappingException {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return IdKey.STD;
            }
        };
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId idFromString(String id) {
                return new ReadableObjectId(id);
            }
        };
        ReadableObjectId id = _ctx.findObjectId(new Object(), idGen, resolver);
        assertNotNull(id);
    }

    @Test
    public void testFindObjectId_noId() throws JsonMappingException {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return IdKey.STD;
            }
        };
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId idFromString(String id) {
                throw new RuntimeException("Should not be called");
            }
        };
        ReadableObjectId id = _ctx.findObjectId(new Object(), idGen, resolver);
        assertNull(id);
    }

    // Edge cases and exception handling scenarios

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws JsonMappingException {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return IdKey.STD;
            }
        };
        ObjectIdResolver resolver1 = new ObjectIdResolver() {
            @Override
            public ObjectId idFromString(String id) {
                return new ReadableObjectId(id);
            }
        };
        ObjectIdResolver resolver2 = new ObjectIdResolver() {
            @Override
            public ObjectId idFromString(String id) {
                return null;
            }
        };
        _ctx.addObjectIdResolver(resolver1);
        _ctx.addObjectIdResolver(resolver2);
        _ctx.findObjectId(new Object(), idGen, resolver1); // should be fine
        _ctx.findObjectId(new Object(), idGen, resolver2); // should be fine
        _ctx.checkUnresolvedObjectId(); // should throw exception
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        Annotated ann = mock(Annotated.class);
        Object deserDef = deserializer;
        JsonDeserializer<?> deser = _ctx.deserializerInstance(ann, deserDef);
        assertEquals(deserializer, deser);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_deserDefIsNull() throws JsonMappingException {
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        Annotated ann = mock(Annotated.class);
        Object deserDef = null;
        JsonDeserializer<?> deser = _ctx.deserializerInstance(ann, deserDef);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_deserDefClass() throws JsonMappingException {
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        Annotated ann = mock(Annotated.class);
        Object deserDef = Object.class;
        JsonDeserializer<?> deser = _ctx.deserializerInstance(ann, deserDef);
    }

    // Complete test file for DefaultDeserializationContext

    @Test
    public void testDefaultDeserializationContext() throws Exception {
        // Basic tests
        testFindObjectId();
        testFindObjectId_noId();

        // Edge cases and exception handling
        testCheckUnresolvedObjectId();
        testDeserializerInstance();
        testDeserializerInstance_deserDefIsNull();
        testDeserializerInstance_deserDefClass();

        // Test with custom handler instantiation
        HandlerInstantiator hi = mock(HandlerInstantiator.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setHandlerInstantiator(hi);
        DeserializationContext ctx = mapper.getDeserializationContext();
        Annotated ann = mock(Annotated.class);
        Object deserDef = new StubJsonDeserializer();
        JsonDeserializer<?> deser = ctx.deserializerInstance(ann, deserDef);
        verify(hi).deserializerInstance(eq(_mapper.getConfiguration()), eq(ann), eq(deserDef.getClass()));
    }

    static class StubJsonDeserializer implements JsonDeserializer<Object> {}
}