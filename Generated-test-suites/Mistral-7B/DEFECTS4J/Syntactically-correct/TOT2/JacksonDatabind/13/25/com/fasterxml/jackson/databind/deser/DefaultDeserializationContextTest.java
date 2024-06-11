package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class DefaultDeserializationContextTest {

    private DeserializationContext context;
    private ObjectMapper objectMapper;
    private ObjectReader objectReader;

    @Before
    public void setUp() throws Exception {
        DeserializerFactory factory = new DefaultDeserializerFactory();
        context = new DefaultDeserializationContext.Impl(factory);
        objectMapper = new ObjectMapper();
        objectReader = objectMapper.reader();
    }

    @Test
    public void testFindObjectId() throws JsonMappingException, IOException {
        ObjectIdGenerator<?> idGenerator = new IdKeyGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        DefaultDeserializationContext ctx = new DefaultDeserializationContext(context, idGenerator, resolver);
        Object id = new Object();
        ReadableObjectId objectId = ctx.findObjectId(id, idGenerator);

        Assert.assertNotNull(objectId);
        Assert.assertEquals(IdKey.CLASS, objectId.getKey());
        Assert.assertEquals(id.getClass(), objectId.getId());
    }

    @Test
    public void testFindObjectIdWithNullIdGenerator() {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext(context, null);
        Object id = new Object();

        try {
            ctx.findObjectId(id, null);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Cannot find ObjectIdGenerator for given class", e.getMessage());
        }
    }

    @Test
    public void testFindObjectIdWithNullResolver() {
        ObjectIdGenerator<?> idGenerator = new IdKeyGenerator();
        DefaultDeserializationContext ctx = new DefaultDeserializationContext(context, idGenerator, null);
        Object id = new Object();

        try {
            ctx.findObjectId(id, idGenerator);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Assert.assertEquals("No ObjectIdResolver found for given resolver type: null", e.getMessage());
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        class TestJsonDeserializer implements JsonDeserializer<Object> {
            @Override
            public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        }

        Annotated annotated = ObjectMapper.getDefaultInstance().findConcreteAnnotated(TestJsonDeserializer.class);
        JsonDeserializer<Object> deserializer = context.deserializerInstance(annotated, TestJsonDeserializer.class);

        Assert.assertNotNull(deserializer);
        Assert.assertEquals(TestJsonDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testDeserializerInstanceWithNullDefinition() {
        Annotated annotated = null;
        JsonDeserializer<Object> deserializer = context.deserializerInstance(annotated, Object.class);

        Assert.assertNull(deserializer);
    }

    @Test
    public void testDeserializerInstanceWithClassDefinition() throws JsonMappingException {
        Annotated annotated = ObjectMapper.getDefaultInstance().findConcreteAnnotated(Object.class);
        JsonDeserializer<Object> deserializer = context.deserializerInstance(annotated, Object.class);

        Assert.assertNotNull(deserializer);
        Assert.assertEquals(JsonDeserializer.None.class, deserializer.getClass());
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext(context, null);
        Object id1 = new Object();
        Object id2 = new Object();

        ctx.findObjectId(id1, new IdKeyGenerator());
        ctx.findObjectId(id2, new IdKeyGenerator());

        try {
            ctx.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            List<UnresolvedForwardReference.IdReference> refs = e.getUnresolvedReferences();
            Assert.assertEquals(2, refs.size());
            for (UnresolvedForwardReference.IdReference ref : refs) {
                Assert.assertEquals(IdKey.CLASS, ref.getIdKey());
                Assert.assertEquals(Object.class, ref.getBeanType());
            }
        }
    }
}