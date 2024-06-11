package com.example.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
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
    private DeserializerFactory factory;
    private DeserializerCache cache;

    @Before
    public void setUp() throws Exception {
        factory = new StandardDeserializerFactory();
        cache = new MapDeserializerCache();
        context = new DefaultDeserializationContext(factory, cache);
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectIdGenerator<String> idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectGenerator<?> key(Object id) {
                return new ObjectIdGenerator.Key("testKey");
            }

            @Override
            public Object generateId(SerializerProvider serializers, Object id) throws JsonProcessingException {
                return id;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        Object id = "testId";
        ReadableObjectId readableId = context.findObjectId(id, idGenerator, resolver);

        assertEquals(id, readableId.getId());
        assertEquals(resolver.getClass(), readableId.getResolver().getClass());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator<String> idGenerator = new ObjectIdGenerator<String>() {
            @Override
            public ObjectGenerator<?> key(Object id) {
                return new ObjectIdGenerator.Key("testKey");
            }

            @Override
            public Object generateId(SerializerProvider serializers, Object id) throws JsonProcessingException {
                return id;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        Object id1 = "testId1";
        Object id2 = "testId2";

        context.findObjectId(id1, idGenerator, null);
        context.findObjectId(id2, idGenerator, null);

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        Annotated ann = mock(Annotated.class);
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();

        Object deserDef = deserializer.getClass();

        JsonDeserializer<Object> deser = context.deserializerInstance(ann, deserDef);

        assertSame(deserializer, deser);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        Annotated ann = mock(Annotated.class);
        KeyDeserializer deserializer = new StubKeyDeserializer();

        Object deserDef = deserializer.getClass();

        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(ann, deserDef);

        assertSame(deserializer, keyDeserializer);
    }

    @Test
    public void testAbstractMethodsImpls() throws Exception {
        ObjectIdGenerator<String> idGenerator = new ObjectIdGenerator<String>() {
            @Override
            public ObjectGenerator<?> key(Object id) {
                return new ObjectIdGenerator.Key("testKey");
            }

            @Override
            public Object generateId(SerializerProvider serializers, Object id) throws JsonProcessingException {
                return id;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        Object id = "testId";
        ReadableObjectId readableId = context.findObjectId(id, idGenerator, null);

        Object id1 = "testId1";
        Object id2 = "testId2";

        context.findObjectId(id1, idGenerator, null);
        context.findObjectId(id2, idGenerator, null);

        context.checkUnresolvedObjectId();

        HandlerInstantiator handlerInstantiator = context.getConfig().getHandlerInstantiator();

        Object deserDef = new ObjectDeserializer().getClass();
        JsonDeserializer<Object> deserializer = handlerInstantiator.deserializerInstance(context.getConfig(), null, deserDef);

        assertEquals(deserializer.getClass(), ObjectDeserializer.class);

        Object deserDef2 = KeyDeserializer.class;
        KeyDeserializer keyDeserializer = handlerInstantiator.keyDeserializerInstance(context.getConfig(), null, deserDef2);

        assertEquals(keyDeserializer.getClass(), KeyDeserializer.class);
    }

    @Test
    public void testCopy() throws Exception {
        ObjectIdGenerator<String> idGenerator = new ObjectIdGenerator<String>() {
            @Override
            public ObjectGenerator<?> key(Object id) {
                return new ObjectIdGenerator.Key("testKey");
            }

            @Override
            public Object generateId(SerializerProvider serializers, Object id) throws JsonProcessingException {
                return id;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        Object id = "testId";
        ReadableObjectId readableId = context.findObjectId(id, idGenerator, null);

        context.checkUnresolvedObjectId();

        DefaultDeserializationContext copy = context.copy();

        assertEquals(context.getFactory(), copy.getFactory());
        assertEquals(context.getCache(), copy.getCache());
        assertEquals(context._objectIds, copy._objectIds);
        assertEquals(context._objectIdResolvers, copy._objectIdResolvers);

        Object id1 = "testId1";
        context.findObjectId(id1, idGenerator, null);

        copy.checkUnresolvedObjectId();

        assertNotSame(context._objectIds, copy._objectIds);
        assertNotSame(context._objectIdResolvers, copy._objectIdResolvers);
    }

    @Test
    public void testCreateInstance() throws Exception {
        ObjectDeserializer deserializer = new ObjectDeserializer();

        DeserializationConfig config = new DeserializationConfig();
        config.setHandlerInstantiator(new StandardHandlerInstantiator());

        JsonParser jp = createMockParser();
        InjectableValues values = new StandardInjectableValues();

        DefaultDeserializationContext newContext = context.createInstance(config, jp, values);

        assertEquals(config, newContext.getConfig());
        assertEquals(jp, newContext.getParser());
        assertEquals(values, newContext.getInjectables());

        ObjectIdGenerator<String> idGenerator = new ObjectIdGenerator<String>() {
            @Override
            public ObjectGenerator<?> key(Object id) {
                return new ObjectIdGenerator.Key("testKey");
            }

            @Override
            public Object generateId(SerializerProvider serializers, Object id) throws JsonProcessingException {
                return id;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        Object id = "testId";

        newContext.findObjectId(id, idGenerator, null);

        assertEquals(id, newContext._objectIds.get(new ObjectIdGenerator.Key("testKey")).getId());

        JsonDeserializer<Object> deserializerInstance = newContext.deserializerInstance(null, deserializer.getClass());

        assertEquals(deserializer, deserializerInstance);
    }

    @Test
    public void testWith() throws Exception {
        DeserializerFactory newFactory = new StandardDeserializerFactory();

        DefaultDeserializationContext newContext = context.with(newFactory);

        assertNotSame(context.getFactory(), newContext.getFactory());
        assertSame(context.getCache(), newContext.getCache());
    }

    static class ObjectDeserializer extends JsonDeserializer<Object> {}
    static class StubJsonDeserializer extends JsonDeserializer<Object> {}
    static class StubKeyDeserializer extends KeyDeserializer {}
}

class MapDeserializerCache extends DeserializerCache {

    @Override
    public JsonDeserializer<?> findDeserializer(Class<?> deserializerType, BeanDescription beanDesc) {
        return null;
    }

    @Override
    public KeyDeserializer findKeyDeserializer(Class<?> keyDeserializerType, Annotated member) {
        return null;
    }
}