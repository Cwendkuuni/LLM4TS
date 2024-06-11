package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for DefaultDeserializationContext.
 */
public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext _context;

    @Before
    public void setUp() {
        _context = new DefaultDeserializationContext.Impl(new TestDeserializerFactory());
    }

    /**
     * Test method for {@link DefaultDeserializationContext#findObjectId(Object, ObjectIdGenerator, ObjectIdResolver)}
     * with valid object id and default resolver.
     */
    @Test
    public void testFindObjectIdWithDefaultResolver() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        ObjectIdKey idKey = new ObjectIdKey("testId");
        Object testObject = new Object();

        ReadableObjectId result = _context.findObjectId(testObject, idGenerator, idResolver);
        assertNotNull(result);
        assertEquals(idKey, result.getKey());
    }

    /**
     * Test method for {@link DefaultDeserializationContext#findObjectId(Object, ObjectIdGenerator, ObjectIdResolver)}
     * with valid object id and custom resolver.
     */
    @Test
    public void testFindObjectIdWithCustomResolver() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);
        when(idResolver.canUseFor(Mockito.any(ObjectIdResolver.class))).thenReturn(true);

        ObjectIdKey idKey = new ObjectIdKey("testId");
        Object testObject = new Object();

        ReadableObjectId result = _context.findObjectId(testObject, idGenerator, idResolver);
        assertNotNull(result);
        assertEquals(idKey, result.getKey());
    }

    /**
     * Test method for {@link DefaultDeserializationContext#findObjectId(Object, ObjectIdGenerator)} with valid object id and default resolver.
     */
    @Test
    public void testFindObjectIdWithDefaultResolverDeprecated() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);

        Object testObject = new Object();

        ReadableObjectId result = _context.findObjectId(testObject, idGenerator);
        assertNotNull(result);
        // since we're using a mock, we can't directly test the key, but we can verify that it's been created
        verify(idGenerator, times(1)).key(testObject);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#checkUnresolvedObjectId()} with unresolved object ids.
     */
    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);
        when(idResolver.canUseFor(Mockito.any(ObjectIdResolver.class))).thenReturn(true);

        Object testObject1 = new Object();
        Object testObject2 = new Object();

        _context
                ._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        _context._objectIdResolvers = new ArrayList<ObjectIdResolver>(8);

        ReadableObjectId entry1 = new ReadableObjectId(new ObjectIdKey("testId1"));
        ReadableObjectId entry2 = new ReadableObjectId(new ObjectIdKey("testId2"));

        entry1.setResolver(idResolver);
        entry2.setResolver(idResolver);

        _context._objectIds.put(new ObjectIdKey("testId1"), entry1);
        _context._objectIds.put(new ObjectIdKey("testId2"), entry2);
        _context._objectIdResolvers.add(idResolver);

        _context.checkUnresolvedObjectId();

        // there should be an UnresolvedForwardReference exception with the details of the unresolved ids
    }

    /**
     * Test method for {@link DefaultDeserializationContext#deserializerInstance(Annotated, Object)} with valid deserializer definition.
     */
    @Test
    public void testDeserializerInstanceWithValidDeserializerDefinition() throws IOException {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new MyJsonDeserializer();

        JsonDeserializer<Object> result = _context.deserializerInstance(ann, deserDef);
        assertNotNull(result);
        assertTrue(result instanceof MyJsonDeserializer);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#deserializerInstance(Annotated, Object)} with invalid deserializer definition.
     */
    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDeserializerDefinition() throws IOException {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new String();

        _context.deserializerInstance(ann, deserDef);
        // there should be an IllegalStateException with a relevant message
    }

    /**
     * Test method for {@link DefaultDeserializationContext#deserializerInstance(Annotated, Object)} with deserializer definition as class.
     */
    @Test
    public void testDeserializerInstanceWithDeserializerDefinitionAsClass() throws IOException {
        Annotated ann = mock(Annotated.class);
        Class<JsonDeserializer<Object>> deserClass = JsonDeserializer.class;

        JsonDeserializer<Object> result = _context.deserializerInstance(ann, deserClass);
        assertNotNull(result);
        assertTrue(result instanceof JsonDeserializer);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#keyDeserializerInstance(Annotated, Object)} with valid key deserializer definition.
     */
    @Test
    public void testKeyDeserializerInstanceWithValidKeyDeserializerDefinition() throws IOException {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new MyKeyDeserializer();

        KeyDeserializer result = _context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(result);
        assertTrue(result instanceof MyKeyDeserializer);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#keyDeserializerInstance(Annotated, Object)} with invalid key deserializer definition.
     */
    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithInvalidKeyDeserializerDefinition() throws IOException {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new String();

        _context.keyDeserializerInstance(ann, deserDef);
        // there should be an IllegalStateException with a relevant message
    }

    /**
     * Test method for {@link DefaultDeserializationContext#keyDeserializerInstance(Annotated, Object)} with key deserializer definition as class.
     */
    @Test
    public void testKeyDeserializerInstanceWithKeyDeserializerDefinitionAsClass() throws IOException {
        Annotated ann = mock(Annotated.class);
        Class<KeyDeserializer> deserClass = KeyDeserializer.class;

        KeyDeserializer result = _context.keyDeserializerInstance(ann, deserClass);
        assertNotNull(result);
        assertTrue(result instanceof KeyDeserializer);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#copy()} that ensures no sharing of state occurs.
     */
    @Test
    public void testCopy() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);
        when(idResolver.canUseFor(Mockito.any(ObjectIdResolver.class))).thenReturn(true);

        Object testObject = new Object();

        _context
                ._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        _context._objectIdResolvers = new ArrayList<ObjectIdResolver>(8);

        // we add an object id with a custom resolver to the original context
        ReadableObjectId entry = new ReadableObjectId(new ObjectIdKey("testId"));
        entry.setResolver(idResolver);
        _context._objectIds.put(new ObjectIdKey("testId"), entry);
        _context._objectIdResolvers.add(idResolver);

        DefaultDeserializationContext copy = _context.copy();

        // the copy should not have the object id or the resolver in its _objectIds or _objectIdResolvers
        assertFalse(copy._objectIds.containsKey(new ObjectIdKey("testId")));
        assertFalse(copy._objectIdResolvers.contains(idResolver));
    }

    /**
     * Test method for abstract methods {@link DefaultDeserializationContext#createInstance(DeserializationConfig, JsonParser, InjectableValues)},
     * {@link DefaultDeserializationContext#with(DeserializerFactory)} and {@link DefaultDeserializationContext#createInstance(DeserializationConfig)}.
     */
    @Test
    public void testAbstractMethods() throws IOException {
        DeserializerFactory factory = new TestDeserializerFactory();
        DeserializationConfig config = new TestDeserializationConfig();
        JsonParser jp = mock(JsonParser.class);
        InjectableValues values = new TestInjectableValues();

        DefaultDeserializationContext contextWithFactory = _context.with(factory);
        DefaultDeserializationContext contextWithConfig = _context.createInstance(config);
        DefaultDeserializationContext contextWithFactoryAndConfig = _context.createInstance(config, jp, values);

        // the abstract methods should not be called in this test case
        // since the class provides concrete implementations for them in its subclasses
    }

    /**
     * Test class for {@link DefaultDeserializationContext#Impl}.
     */
    public static class TestDeserializerFactory implements DeserializerFactory {
        @Override
        public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated member, Class<?> deserClass)
                throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> findValueDeserializer(DeserializationConfig config, Annotated member, TypeDeserializer typeDeserializer)
                throws JsonMappingException {
            return null;
        }

        @Override
        public KeyDeserializer createKeyDeserializer(DeserializationConfig config, Annotated member, Class<?> deserClass)
                throws JsonMappingException {
            return null;
        }

        @Override
        public KeyDeserializer findKeyDeserializer(DeserializationConfig config, Annotated member, TypeDeserializer typeDeserializer)
                throws JsonMappingException {
            return null;
        }
    }

    public static class TestDeserializationConfig extends DeserializationConfig {
        // empty configuration
    }

    public static class TestInjectableValues implements InjectableValues {
        // empty injectable values
    }

    public static class MyJsonDeserializer extends JsonDeserializer<Object> {
        // empty deserializer
    }

    public static class MyKeyDeserializer extends KeyDeserializer {
        // empty key deserializer
    }
}