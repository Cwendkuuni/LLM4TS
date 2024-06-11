package com.fasterxml.jackson.databind.deser.test;

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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest extends TestBase {

    private DefaultDeserializationContext context;
    private DeserializerFactory df;

    @Before
    public void setUp() throws Exception {
        df = new DeserializerFactory();
        context = new DefaultDeserializationContext(df, null);
    }

    @Test
    public void testFindObjectId() throws Exception {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new StubObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ReadableObjectId objectId = context.findObjectId(id, gen, resolver);
        assertNotNull(objectId);
        assertEquals(id.getClass(), objectId.getIdType());
        assertEquals(id, objectId.getId());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws Exception {
        Object id1 = new Object();
        Object id2 = new Object();

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey(id1.getClass(), id1), new ReadableObjectId(new ObjectIdGenerator.IdKey(id1.getClass(), id1)));

        context.checkUnresolvedObjectId();

        context._objectIds.put(new ObjectIdGenerator.IdKey(id2.getClass(), id2), new ReadableObjectId(new ObjectIdGenerator.IdKey(id2.getClass(), id2)));
        context.checkUnresolvedObjectId();

        context._objectIds.remove(new ObjectIdGenerator.IdKey(id1.getClass(), id1));
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Annotated ann = mapper.getAnnotationIntrospector().findAnnotation(Object.class, JsonDeserialize.class);
        JsonDeserializer<?> deser = context.deserializerInstance(ann, new JsonDeserializerClassDef());
        assertNotNull(deser);

        Class<?> deserClass = new JsonDeserializerClassDef().getClass();
        deser = context.deserializerInstance(ann, deserClass);
        assertNotNull(deser);
        assertTrue(deser instanceof JsonDeserializer);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Annotated ann = mapper.getAnnotationIntrospector().findAnnotation(Object.class, JsonDeserialize.KeyAsProperty.class);
        KeyDeserializer deser = context.keyDeserializerInstance(ann, new JsonDeserializerClassDef());
        assertNotNull(deser);

        Class<?> deserClass = new JsonDeserializerClassDef().getClass();
        deser = context.keyDeserializerInstance(ann, deserClass);
        assertNotNull(deser);
        assertTrue(deser instanceof KeyDeserializer);
    }

    @Test
    public void testWith() throws Exception {
        DefaultDeserializationContext newContext = context.with(new DeserializerFactory());
        assertNotSame(context, newContext);
    }

    @Test
    public void testCreateInstance() throws Exception {
        DefaultDeserializationContext newContext = context.createInstance(new DeserializationConfig(), new JsonParser(null), new InjectableValues());
        assertNotSame(context, newContext);
    }

    private static class StubObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return new ObjectIdGenerator.IdKey(id.getClass(), id);
        }
    }

    private static class JsonDeserializerClassDef extends JsonDeserializer<Object> {}
}