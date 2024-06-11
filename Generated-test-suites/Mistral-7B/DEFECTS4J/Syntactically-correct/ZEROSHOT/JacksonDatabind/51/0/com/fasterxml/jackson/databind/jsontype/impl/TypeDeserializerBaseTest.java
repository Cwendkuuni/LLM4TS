package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Unit tests for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest extends TestBase {

    private TypeDeserializerBase _tester;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase tester = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class), null, null, false, null);
        _tester = tester;
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty("testProperty", String.class, null);
        TypeDeserializerBase typeDeserializer = _tester.forProperty(property);
        assertNotNull(typeDeserializer);
        assertEquals(String.class, typeDeserializer.baseType());
        assertEquals(_tester._idResolver, typeDeserializer.getTypeIdResolver());
        assertNull(typeDeserializer.getPropertyName());
        assertEquals(_tester._defaultImpl, typeDeserializer.getDefaultImpl());
        assertFalse(typeDeserializer.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE));
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = _tester.getTypeInclusion();
        assertTrue(typeInclusion == JsonTypeInfo.As.EXISTING_PROPERTY);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals("java.lang.String", _tester.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertNull(_tester.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertNotNull(_tester.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertNull(_tester.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expected = "[TypeDeserializerBase; base-type:java.lang.String; id-resolver:null]";
        assertEquals(expected, _tester.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        DeserializationContext context = new TestContext();
        String typeId = "TestClass";
        JsonDeserializer<Object> deserializer = new TestJsonDeserializer();
        _tester._deserializers.put(typeId, deserializer);

        JsonDeserializer<Object> result = _tester._findDeserializer(context, typeId);
        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext context = new TestContext();
        JsonDeserializer<Object> deserializer = new TestJsonDeserializer();
        _tester._defaultImplDeserializer = deserializer;

        JsonDeserializer<Object> result = _tester._findDefaultImplDeserializer(context);
        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws Exception {
        DeserializationContext context = new TestContext();
        JsonDeserializer<Object> deserializer = new TestJsonDeserializer();
        _tester._defaultImplDeserializer = deserializer;

        JsonDeserializer<Object> result = _tester._findDeserializer(context, null);
        assertNull(result);
    }

    @Test
    public void testFindDeserializerWithUnknownTypeId() throws Exception {
        DeserializationContext context = new TestContext();
        String unknownTypeId = "UnknownType";

        JsonDeserializer<Object> result = _tester._findDeserializer(context, unknownTypeId);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullContext() throws Exception {
        String typeId = "TestClass";
        _tester._findDeserializer(null, typeId);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"typeId\":\"TestClass\"}";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object result = _tester._deserializeWithNativeTypeId(parser, context);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullTypeIdInJson() throws Exception {
        String json = "{\"typeId\":null}";
        JsonParser parser = mapper.getFactory().createParser(json);

        _tester._deserializeWithNativeTypeId(parser, context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithMissingTypeIdInJson() throws Exception {
        String json = "{}";
        JsonParser parser = mapper.getFactory().createParser(json);

        _tester._deserializeWithNativeTypeId(parser, context);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        String unknownTypeId = "UnknownType";

        JavaType result = _tester._handleUnknownTypeId(context, unknownTypeId, null, null);
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JavaType findContextualValueType(BeanProperty property, Class<?> raw) {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findContextualValueDeserializer(JavaType type, BeanProperty property) {
            return null;
        }

        @Override
        public void handleUnexpectedToken(JsonParser p, JavaType type) throws IOException {
            fail("Unexpected token encountered");
        }

        @Override
        public void reportMappingException(Map<String,Object> pMap) throws IOException {
            fail("Unexpected mapping exception");
        }

        @Override
        public void reportMappingException(String msg) throws IOException {
            fail("Unexpected mapping exception: " + msg);
        }

        @Override
        public void reportMappingException(String msg, Throwable t) throws IOException {
            fail("Unexpected mapping exception: " + msg + ", cause: " + t.getMessage());
        }

        @Override
        public void handleMissingValue(JsonParser p, JavaType type) throws IOException {
            fail("Missing value encountered for type: " + type);
        }

        @Override
        public void handleMissingValue(JsonParser p, BeanProperty property) throws IOException {
            fail("Missing value encountered for property: " + property);
        }

        @Override
        public void handleUnexpectedValue(JsonParser p, JsonToken id) throws IOException {
            fail("Unexpected value encountered: " + id.getName());
        }

        @Override
        public void handleUnexpectedValue(JsonParser p, JsonToken id, String propertyName) throws IOException {
            fail("Unexpected value encountered for property: " + propertyName + ", token: " + id.getName());
        }

        @Override
        public boolean canOverride(DeserializationFeature feature) {
            return true;
        }
    }

    @JsonDeserializer(contentType = "application/json")
    static class TestJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return "Test Deserializer Result";
        }
    }
}