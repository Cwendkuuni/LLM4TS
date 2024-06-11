package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserializer.Caching;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.ObjectIdProvider;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdProviderDelegatingDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class DefaultDeserializationContextTest {

    private DeserializerFactory factory;
    private ObjectMapper mapper;
    private ObjectReader reader;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        factory = mapper.getDeserializerFactory();
        reader = mapper.getReaderForUpdating(this.getClass());
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectIdProvider idProvider = mock(ObjectIdProvider.class);
        DeserializationContext context = new DefaultDeserializationContext(null, null, null, null);
        when(idProvider.getId(any(DeserializationContext.class), any(Class.class))).thenReturn("id123");

        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey();
            }

            @Override
            public ObjectIdGenerator.IdKey key(Class<?> valueType) {
                return new IdKey(valueType);
            }

            @Override
            public ObjectIdGenerator.IdKey key(Class<?> valueType, Object id) {
                return new IdKey(valueType, id);
            }

            @Override
            public ObjectIdGenerator.IdKey key(JsonParser p, DeserializationContext ctxt, Object id) {
                return new IdKey(p, ctxt, id);
            }

            @Override
            public ObjectIdGenerator.IdKey key(DeserializationContext ctxt, Object id) {
                return new IdKey(ctxt, id);
            }

            @Override
            public ObjectIdGenerator.IdKey key(DeserializationContext ctxt, Object id, JsonParser p) {
                return new IdKey(ctxt, id, p);
            }

            @Override
            public ObjectIdGenerator.IdKey key(ObjectIdGenerator.IdKey idKey, Object id) {
                return new IdKey(idKey, id);
            }

            @Override
            public ObjectIdGenerator.IdKey key(ObjectIdGenerator.IdKey idKey, Object id, JsonParser p) {
                return new IdKey(idKey, id, p);
            }

            @Override
            public ObjectIdGenerator.IdKey key(DeserializationContext ctxt, ObjectIdGenerator<?> gen) {
                return new IdKey(ctxt, gen);
            }

            @Override
            public ObjectIdGenerator<?> getIdResolver(DeserializationContext ctxt) {
                return idProvider;
            }

            @Override
            public ObjectIdGenerator<?> getIdGenerator(DeserializationContext ctxt) {
                return this;
            }
        };

        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context = new DefaultDeserializationContext(factory, null);
        context.setObjectIdResolver(resolver);
        context.setObjectIdGenerator(generator);

        Object id = new Object();
        ObjectIdGenerator.IdKey key = generator.key(id);
        ReadableObjectId objectId = context.findObjectId(id, generator, ObjectIdGenerator.IdKey.class);
        assertEquals("id123", objectId.getId());

        Map<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
        context = new DefaultDeserializationContext(null, null, null, null);
        when(idProvider.getId(any(DeserializationContext.class), any(Class.class)))
                .thenReturn("id456");
        objectIds.put(new IdKey(), new ReadableObjectId(new IdKey()));
        context._objectIds = objectIds;

        id = new Object();
        key = generator.key(id);
        objectId = context.findObjectId(id, generator, ObjectIdGenerator.IdKey.class);
        assertEquals("id456", objectId.getId());

        assertNull(context.findObjectId(null, generator, ObjectIdGenerator.IdKey.class));
    }

    @Test
    public void testCreateInstance() throws Exception {
        HandlerInstantiator handlerInst = mock(HandlerInstantiator.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(handlerInst.deserializerInstance(any(DeserializationConfig.class), any(Annotated.class), any(Class.class)))
                .thenReturn(deserializer);

        ObjectReader reader = new MinimalObjectReader(null, null);
        reader.setAnnotationIntrospector(mapper.getAnnotationIntrospector());
        reader.setHandlerInstantiator(handlerInst);

        DefaultDeserializationContext context = new DefaultDeserializationContext(factory, null);
        when(handlerInst.deserializerInstance(any(DeserializationConfig.class), any(Annotated.class),
                eq(JsonDeserializer.class)))
                .thenReturn(deserializer);
        when(handlerInst.deserializerInstance(any(DeserializationConfig.class), any(Annotated.class),
                eq(LocalDateTimeSerializer.class)))
                .thenReturn(deserializer);
        when(handlerInst.keyDeserializerInstance(any(DeserializationConfig.class), any(Annotated.class),
                eq(LocalDateTimeSerializer.class)))
                .thenReturn(deserializer);

        JsonParser parser = mapper.getFactory().createParser("{}");
        Object id = mapper.readValue(parser, Object.class);

        assertEquals(id.getClass(), Object.class);
        assertEquals(id, reader.readValue(parser));
        assertEquals(id, context.deserialize(parser, Object.class));
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForUpdating(Object.class);
        DefaultDeserializationContext context = mapper.getSerializationConfig().getDefaultDeserializationContext();
        context.copy();

        ObjectIdGenerator generator = ObjectIdGenerators.Int.class;
        Object object1 = new Object();
        Object object2 = new Object();
        ObjectIdGenerator.IdKey key1 = generator.key(object1);
        ObjectIdGenerator.IdKey key2 = generator.key(object2);

        context.findObjectId(object1, generator, ObjectIdGenerator.IdKey.class);
        context.findObjectId(object2, generator, ObjectIdGenerator.IdKey.class);

        context.checkUnresolvedObjectId();

        Map<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
        context.copy();
        ObjectIdGenerator generator2 = ObjectIdGenerators.Int.class;
        Object object3 = new Object();
        Object object4 = new Object();
        ObjectIdGenerator.IdKey key3 = generator2.key(object3);
        ObjectIdGenerator.IdKey key4 = generator2.key(object4);

        objectIds.put(key1, new ReadableObjectId(key1));
        context._objectIds = objectIds;
        context.checkUnresolvedObjectId();

        ObjectIdGenerator generator3 = ObjectIdGenerators.Int.class;
        Object object5 = new Object();
        ObjectIdGenerator.IdKey key5 = generator3.key(object5);

        context.checkUnresolvedObjectId();
    }

    @JsonAutoDetect(getterVisibility = PropertyAccessor.ALL, setterVisibility = PropertyAccessor.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.Int.class)
    @JsonIdentityResolver(Resolver.class)
    static class TestObject {
        int id;
        TestObject ref;
    }

    static class TestObjectDeserializer extends StdSerializer<TestObject> {
        public TestObjectDeserializer() {
            this(null);
        }

        public TestObjectDeserializer(Class<TestObject> t) {
            this(null);
        }

        private TestObjectDeserializer() {
            this(null);
        }

        private TestObjectDeserializer(Class<TestObject> t, Caching strategicSizeThreshold) {
            super(t);
        }

        @Override
        public TestObject deserialize(JsonParser jp, DeserializationContext ctxt) throws JsonMappingException {
            TestObject obj = new TestObject();
            obj.id = jp.nextIntValue();
            if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)) {
                String name = jp.nextValueAsString();
                if (name == null || name.isEmpty()) {
                    throw ctxt.wrappedException(new JsonMappingException("Empty bean not allowed: " + TestObject.class.getName()));
                }
            }
            if (jp.nextToken() != JsonToken.END_OBJECT) {
                ctxt.handleUnexpectedValue(jp, null, null);
            }
            int refId = jp.nextIntValue();
            obj.ref = ctxt.findReferenceById(refId, TestObject.class);
            return obj;
        }

        @Override
        public void serializeWithEmpty(SerializerProvider p, TestObject t, JsonGenerator g, SerializerProvider.Empty emptyCtxt)
                throws IOException {
            if (t.ref == null) {
                g.writeStartObject();
                g.writeNumberField("id", 0);
                g.writeEndObject();
            }
        }
    }

    static class Resolver extends SimpleObjectIdResolver {
        @Override
        public ObjectIdProvider getProviderIdProvider(DeserializationContext ctxt) {
            return new ObjectIdProviderDelegatingDeserializer(ctxt);
        }
    }

    static class MinimalObjectReader extends ObjectReader {
        public MinimalObjectReader(HandlerInstantiator handlerInst, DeserializerProvider deserializers) {
            super(null, null, null, null, handlerInst, deserializers, null);
        }

        @Override
        public <T> T readValue(JsonParser p, Class<T> valueType) throws IOException {
            return p.readValueAs(valueType);
        }
    }
}