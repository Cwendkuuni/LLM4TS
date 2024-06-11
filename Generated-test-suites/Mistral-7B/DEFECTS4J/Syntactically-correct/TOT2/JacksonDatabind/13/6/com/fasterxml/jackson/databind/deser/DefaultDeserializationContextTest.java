package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

import java.util.*;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest extends BaseTest {

    // Test for findObjectId method
    @org.junit.Test
    public void testFindObjectId() {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };
        DefaultDeserializationContext context = new DefaultDeserializationContext(new JacksonAnnotationIntrospector(), null, null, null);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId roid = context.findObjectId(new Object(), idGen, resolver);
        assertEquals("testKey", roid.getKey().key);
    }

    // Test for findObjectId method with null id
    @org.junit.Test
    public void testFindObjectIdNullId() {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };
        DefaultDeserializationContext context = new DefaultDeserializationContext(new JacksonAnnotationIntrospector(), null, null, null);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId roid = context.findObjectId(null, idGen, resolver);
        assertNull(roid);
    }

    // Test for checkUnresolvedObjectId method
    @org.junit.Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(new JacksonAnnotationIntrospector(), null, null, null);
        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = new ArrayList<>();
        context._objectIdResolvers.add(new SimpleObjectIdResolver());

        ReadableObjectId roid = new ReadableObjectId(new ObjectIdGenerator.IdKey("testKey"));
        roid.setResolver(new SimpleObjectIdResolver());
        context._objectIds.put(new ObjectIdGenerator.IdKey("testKey"), roid);

        List<Referring> referringProperties = new ArrayList<>();
        referringProperties.add(new Referring(null, null, "testProp"));
        roid.setReferringProperties(referringProperties);

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // Expected
            assertNotNull(e.getId());
            assertNotNull(e.getId().getClass());
            assertEquals("testKey", e.getId().getKey().key);
            assertEquals("testProp", e.getPath());
        }
    }

    // Test for deserializerInstance method
    @org.junit.Test
    public void testDeserializerInstance() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        Annotated ann = mapper.getAnnotationIntrospector().findAnnotation(MyBean.class);
        JsonDeserializer<MyBean> deser = context.deserializerInstance(ann, MyBeanDeserializer.class);
        assertNotNull(deser);
        assertEquals(MyBeanDeserializer.class, deser.getClass());
    }

    // Test for deserializerInstance method with null deserDef
    @org.junit.Test
    public void testDeserializerInstanceNullDeserDef() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        Annotated ann = mapper.getAnnotationIntrospector().findAnnotation(MyBean.class);
        JsonDeserializer<MyBean> deser = context.deserializerInstance(ann, null);
        assertNull(deser);
    }

    // Test for keyDeserializerInstance method
    @org.junit.Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        Annotated ann = mapper.getAnnotationIntrospector().findAnnotation(MyKey.class);
        KeyDeserializer deser = context.keyDeserializerInstance(ann, MyKeyDeserializer.class);
        assertNotNull(deser);
        assertEquals(MyKeyDeserializer.class, deser.getClass());
    }

    // Test for keyDeserializerInstance method with null deserDef
    @org.junit.Test
    public void testKeyDeserializerInstanceNullDeserDef() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        Annotated ann = mapper.getAnnotationIntrospector().findAnnotation(MyKey.class);
        KeyDeserializer deser = context.keyDeserializerInstance(ann, null);
        assertNull(deser);
    }

    // Test for with method
    @org.junit.Test
    public void testWithMethod() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        DefaultDeserializationContext newContext = context.with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated ann, Class<?> type) {
                return new JsonDeserializer<Number>() {
                    @Override
                    public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return null;
                    }
                };
            }
        });

        assertNotSame(context, newContext);

        JsonParser jp = createParser("{}");
        ObjectReader reader = mapper.readerForValueType(Number.class);
        Object readValue = reader.readValue(jp);
        assertNull(readValue);

        JsonParser jp2 = createParser("{\"@class\":\"java.lang.Integer\"}");
        Object readValue2 = newContext.readerForValueType(Number.class).readValue(jp2);
        assertEquals(Integer.class, readValue2.getClass());
    }

    // Test for createInstance method
    @org.junit.Test
    public void testCreateInstanceMethod() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        DefaultDeserializationContext newContext = context.createInstance(
                new DeserializationConfig(mapper.getFactory()),
                createParser("{}"),
                new InjectableValues() {{
                    addValue("java.lang.Boolean", Boolean.FALSE);
                    addValue("java.lang.Integer", 1);
                }}
        );

        assertNotSame(context, newContext);

        JsonParser jp = createParser("{}");
        ObjectReader reader = mapper.readerForValueType(Number.class);
        Object readValue = reader.readValue(jp);
        assertNull(readValue);

        JsonParser jp2 = createParser("{\"@class\":\"java.lang.Integer\"}");
        Object readValue2 = newContext.readerForValueType(Number.class).readValue(jp2);
        assertEquals(Integer.class, readValue2.getClass());
    }
}