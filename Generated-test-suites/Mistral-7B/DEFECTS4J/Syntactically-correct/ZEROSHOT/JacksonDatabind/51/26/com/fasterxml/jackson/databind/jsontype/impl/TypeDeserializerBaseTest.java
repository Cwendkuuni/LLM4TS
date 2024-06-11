package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase testee;

    @Before
    public void setUp() {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        testee = new TypeDeserializerBase(baseType, idResolver, null, true, null);
    }

    @Test
    public void testForProperty() {
        BeanProperty property = new BeanProperty(Foo.class, "name");
        TypeDeserializer forProperty = testee.forProperty(property);
        assertNotNull(forProperty);
        assertSame(testee.getClass(), forProperty.getClass());
        assertNull(forProperty._property);
        assertSame(testee._idResolver, forProperty._idResolver);
        assertSame(testee._baseType, forProperty._baseType);
        assertNull(forProperty._typePropertyName);
        assertFalse(forProperty._typeIdVisible);
        assertEquals(testee._deserializers.size(), forProperty._deserializers.size());
        assertSame(testee._defaultImplDeserializer, forProperty._defaultImplDeserializer);
        assertSame(testee._defaultImpl, forProperty._defaultImpl);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAP_INCLUDE, testee.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = testee.baseTypeName();
        assertEquals(Foo.class.getName(), baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        assertNull(testee.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertSame(testee._idResolver, testee.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(testee.getDefaultImpl());
    }

    @Test
    public void testToString() {
        String toString = testee.toString();
        assertTrue(toString.startsWith("[TypeDeserializerBase"));
        assertTrue(toString.endsWith("]"));
    }

    @Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        String typeId = "typeId";
        testee._deserializers.put(typeId, deserializer);

        JsonParser parser = mapper.getFactory().createParser("{}");
        parser.nextToken();

        JsonDeserializer<Object> result = testee._findDeserializer(context, typeId);
        assertEquals(deserializer, result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerNotFound() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext context = mapper.getDeserializationContext();
        String typeId = "typeId";

        JsonParser parser = mapper.getFactory().createParser("{}");
        parser.nextToken();

        testee._findDeserializer(context, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext context = mapper.getDeserializationContext();

        JsonDeserializer<Object> result = testee._findDefaultImplDeserializer(context);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext context = mapper.getDeserializationContext();
        String typeId = "typeId";

        JavaType result = testee._handleUnknownTypeId(context, typeId, null, null);
        assertNull(result);
    }

    /**
     * Stub class for {@link JsonDeserializer} used in tests.
     */
    static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    static class BaseTypeIdResolver extends TypeIdResolverBase {
        @Override
        public String getDescForKnownTypeIds() {
            return "known type ids are statically known";
        }
    }

    static class Foo {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}