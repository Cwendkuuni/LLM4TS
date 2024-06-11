package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private JsonParser parser;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        ObjectMapper.DefaultTypingResolver defaultTypingResolver = new ObjectMapper.DefaultTypingResolver();
        this.mapper = new ObjectMapper()
                .registerModule(new SimpleModule())
                .setDefaultTyping(defaultTypingResolver);
        this.parser = mapper.getFactory().createParser("{}");
        this.context = new DefaultDeserializationContext.Impl(mapper.getDeserializerFactory(), mapper.getConfig(), parser, new EmptyBeanContext());
    }

    @Test
    public void testFindObjectId() throws Exception {
        Object id = new Object();
        ObjectIdGenerator<Object> gen = new StdObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ReadableObjectId roid = context.findObjectId(id, gen, resolver);
        assertNotNull(roid);
        assertEquals(gen.key(id), roid.getKey());
        assertEquals(resolver, roid.getResolver());

        // Test null IdGenerator
        roid = context.findObjectId(null, gen, resolver);
        assertNull(roid);

        // Test null ObjectIdResolver
        roid = context.findObjectId(id, gen, null);
        assertNull(roid);

        // Test null ObjectIdGenerator
        roid = context.findObjectId(id, null, resolver);
        assertNull(roid);

        // Test already registered Id
        context._objectIds.put(gen.key(id), new ReadableObjectId(gen.key(id)));
        roid = context.findObjectId(id, gen, resolver);
        assertNotNull(roid);
        assertEquals(gen.key(id), roid.getKey());
        assertEquals(resolver, roid.getResolver());
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdGenerator<Object> gen = new StdObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        context._objectIds.put(gen.key(id1), new ReadableObjectId(gen.key(id1)));
        context._objectIds.put(gen.key(id2), new ReadableObjectId(gen.key(id2)));

        // Test no unresolved object ids
        context.checkUnresolvedObjectId();

        // Test no unresolved object ids with failOnUnresolvedObjectIds disabled
        context.setConfig(mapper.getConfig().with(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false));
        context.checkUnresolvedObjectId();

        // Test unresolved object ids
        context._objectIds.remove(gen.key(id1));
        try {
            context.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: " + gen.key(id1), e.getMessage());
        }
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        Object id = new Object();
        Annotated ann = mapper.getAnnotationIntrospector().findPropertyAnnotation(Object.class, "id");
        Object deserDef = new StringDeserializer();

        JsonDeserializer<Object> deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(deserDef.getClass(), deser.getClass());

        // Test null deserDef
        deser = context.deserializerInstance(ann, null);
        assertNull(deser);

        // Test non-JsonDeserializer deserDef
        deser = context.deserializerInstance(ann, new Class<JsonDeserializer<Object>>() {});
        assertNull(deser);

        // Test non-JsonDeserializer Class deserDef
        deser = context.deserializerInstance(ann, ObjectDeserializer.class);
        assertNull(deser);

        // Test already registered deserializer
        deser = (JsonDeserializer<Object>) context.contextData.getDeserializerInstance("TestDeserializer");
        assertNotNull(deser);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        Object id = new Object();
        Annotated ann = mapper.getAnnotationIntrospector().findPropertyAnnotation(Object.class, "id");
        Object deserDef = StringDeserializer.class;

        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(StringDeserializer.class, deser.getClass());

        // Test null deserDef
        deser = context.keyDeserializerInstance(ann, null);
        assertNull(deser);

        // Test non-KeyDeserializer Class deserDef
        deser = context.keyDeserializerInstance(ann, JsonDeserializer.class);
        assertNull(deser);

        // Test already registered key deserializer
        deser = (KeyDeserializer) context.contextData.getKeyDeserializerInstance("TestKeyDeserializer");
        assertNotNull(deser);
    }

    @Test
    public void testWith() throws Exception {
        ObjectMapper newMapper = mapper.copy();
        DefaultDeserializationContext newContext = new DefaultDeserializationContext.Impl(newMapper.getDeserializerFactory(), newMapper.getConfig(), null, new EmptyBeanContext());

        DefaultDeserializationContext newContextWith = newContext.with(newMapper.getDeserializerFactory());
        assertEquals(newMapper, newContextWith.getDeserializerFactory().getFactory());

        // Test null DeserializerFactory
        try {
            newContextWith = newContext.with(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Factory must not be null", e.getMessage());
        }
    }

    @Test
    public void testCreateInstance() throws Exception {
        ObjectMapper newMapper = mapper.copy();

        DefaultDeserializationContext newContext = new DefaultDeserializationContext.Impl(null, newMapper.getConfig(), null, new EmptyBeanContext());
        DefaultDeserializationContext newContextInstance = newContext.createInstance(newMapper.getConfig(), null, new EmptyBeanContext());

        assertEquals(newMapper, newContextInstance.getFactory());

        // Test null Config
        try {
            DefaultDeserializationContext newContextInstance2 = newContext.createInstance(null, null, new EmptyBeanContext());
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Config must not be null", e.getMessage());
        }

        // Test null JsonParser
        try {
            DefaultDeserializationContext newContextInstance2 = newContext.createInstance(newMapper.getConfig(), null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("JsonParser must not be null", e.getMessage());
        }

        // Test null InjectableValues
        try {
            DefaultDeserializationContext newContextInstance2 = newContext.createInstance(newMapper.getConfig(), null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("InjectableValues must not be null", e.getMessage());
        }
    }

    private static class StringDeserializer extends StdDeserializer<String> {

        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<String> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class StdObjectIdGenerator implements ObjectIdGenerator<Object> {

        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return new IdKey() {
                @Override
                public String toString() {
                    return "StdObjectIdGenerator." + id.getClass().getName();
                }
            };
        }
    }

    private static class SimpleObjectIdResolver implements ObjectIdResolver {

        @Override
        public ReadableObjectId idResolverFor(DeserializationContext ctxt, Object id) throws JsonMappingException {
            return new ReadableObjectId(ctxt.keyId(id));
        }

        @Override
        public void resolveReference(DeserializationContext ctxt, ObjectId id, BeanProperty property)
                throws JsonMappingException {
            Annotated ann = property.getAnnotation(ObjectIdGenerator.Property.class);
            Converter<Object, ObjectId> converter = ctxt.findConverters(ObjectId.class, Object.class)
                    .nextGettingResolvedConverter(null, ObjectId.class, Object.class);
            ObjectId resolvedId = converter.convertValue(id, ObjectId.class);
            ctxt.handle低位字节序列化错误(_contextData.getSerializers().findValueSerializer(ObjectId.class));
            ctxt.handle低位字节序列化错误(_contextData.getSerializerProvider().getSerializerInstance(ObjectId.class));
            ctxt.handleValueFinished(property.getName());
            ctxt.handleValueFinished(property);
            ObjectIdGenerator.Property propAnn = ann != null ? ann : property.getAnnotation(ObjectIdGenerator.Property.class);
            ObjectIdGenerator idGen = propAnn != null ? ctxt.handleResolveIdReference(propAnn.value()) : null;
            if (idGen != null) {
                ObjectId idObj = idGen.idId(id);
                ObjectId resolvedIdObj = idGen.idResolverFor(ctxt, idObj).idValue();
                property.setValue(resolvedIdObj);
            }
        }

        @Override
        public boolean canUseFor(DeserializationContext ctxt, ObjectIdResolver resolver) {
            return this.getClass() == resolver.getClass();
        }
    }

    private static class EmptyBeanContext implements BeanContext {

        @Override
        public JsonDeserializer<Object> findBeanDeserializer(BeanDescription beanDesc, DeserializationContext ctxt)
                throws JsonMappingException {
            return null;
        }

        @Override
        public Object findValueDeserializer(DeserializationContext ctxt, BeanProperty property)
                throws JsonMappingException {
            return null;
        }

        @Override
        public ObjectIdGenerator findValueIdGenerator(DeserializationContext ctxt, Annotated member)
                throws JsonMappingException {
            return null;
        }

        @Override
        public ObjectIdGenerator.IdKey keyId(Object id) {
            return null;
        }

        @Override
        public Object findValueHandler(DeserializationContext ctxt, BeanProperty property)
                throws JsonMappingException {
            return null;
        }

        @Override
        public Object findValueHandler(DeserializationContext ctxt, BeanProperty property, Type descriptor)
                throws JsonMappingException {
            return null;
        }

        @Override
        public Object findValueHandler(DeserializationContext ctxt, Annotated ann) throws JsonMappingException {
            return null;
        }

        @Override
        public Object findComparator(DeserializationContext ctxt, BeanProperty prop) throws JsonMappingException {
            return null;
        }

        @Override
        public Object findValueSerializer(DeserializationContext ctxt, BeanProperty property)
                throws JsonMappingException {
            return null;
        }

        @Override
        public Object findValueSerializer(DeserializationContext ctxt, Annotated ann) throws JsonMappingException {
            return null;
        }

        @Override
        public Object findValueSerializer(DeserializationContext ctxt, Type descriptor) throws JsonMappingException {
            return null;
        }

        @Override
        public Object findValueSerializer(DeserializationContext ctxt, BeanDescription beanDesc, Type descriptor)
                throws JsonMappingException {
            return null;
        }

        @Override
        public Object findValueSerializer(DeserializationContext ctxt, ValueDeserializer vd) throws JsonMappingException {
            return null;
        }

        @Override
        public SerializerProvider getSerializerProvider() {
            return new DefaultSerializerProvider();
        }
    }
}