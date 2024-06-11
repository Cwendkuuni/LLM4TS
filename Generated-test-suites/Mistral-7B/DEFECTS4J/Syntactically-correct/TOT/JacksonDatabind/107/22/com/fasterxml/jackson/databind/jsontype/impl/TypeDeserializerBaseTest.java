package com.fasterxml.jackson.databind.test.typedeserializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Class<TypeDeserializerBase> TYPE_DESERIALIZER_BASE = TypeDeserializerBase.class;

    private static final JavaType BASE_TYPE = MAPPER.getTypeFactory().constructType(TypeDeserializerBase.class);

    @Before
    public void setUp() throws Exception {
        MAPPER.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @After
    public void tearDown() throws Exception {
        // no cleanup needed
    }

    /**
     * Test for {@link TypeDeserializerBase#TypeDeserializerBase(JavaType, TypeIdResolver, String, boolean, JavaType)}
     * constructor.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBaseConstructor() throws Exception {
        // Test empty constructor
        TypeDeserializerBase deserializer = new TypeDeserializerBase(BASE_TYPE, null, "", false, null);
        assertNotNull(deserializer);

        // Test constructor with arguments
        TypeIdResolver idResolver = new TestTypeIdResolver();
        JavaType baseType = MAPPER.getTypeFactory().constructType(TypeDeserializerBase.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = false;
        JavaType defaultImpl = MAPPER.getTypeFactory().constructType(TestDefaultImpl.class);

        TypeDeserializerBase deserializer2 = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(baseType, deserializer2.getBaseType());
        assertEquals(idResolver, deserializer2.getTypeIdResolver());
        assertEquals(typePropertyName, deserializer2.getPropertyName());
        assertFalse(deserializer2.isTypeIdVisible());
        assertEquals(defaultImpl, deserializer2.getDefaultImpl());
    }

    /**
     * Test for {@link TypeDeserializerBase#forProperty(BeanProperty)} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBaseForProperty() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        BeanProperty property = getTestProperty();

        // Test null property
        try {
            deserializer.forProperty(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test valid property
        TypeDeserializer typeDeserializer = deserializer.forProperty(property);
        assertEquals(TYPE_DESERIALIZER_BASE, typeDeserializer.getClass());
        assertEquals(deserializer, typeDeserializer.getTypeDeserializerBase());
    }

    /**
     * Test for {@link TypeDeserializerBase#getTypeInclusion()} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBasegetTypeInclusion() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        JsonTypeInfo.As inclusion = deserializer.getTypeInclusion();

        assertNotNull(inclusion);
    }

    /**
     * Test for {@link TypeDeserializerBase#baseTypeName()} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBasebaseTypeName() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        String baseTypeName = deserializer.baseTypeName();

        assertEquals(BASE_TYPE.getRawClass().getName(), baseTypeName);
    }

    /**
     * Test for {@link TypeDeserializerBase#getPropertyName()} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBasegetPropertyName() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        String propertyName = deserializer.getPropertyName();

        assertNull(propertyName);
    }

    /**
     * Test for {@link TypeDeserializerBase#getTypeIdResolver()} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBasegetTypeIdResolver() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        TypeIdResolver idResolver = deserializer.getTypeIdResolver();

        assertNotNull(idResolver);
    }

    /**
     * Test for {@link TypeDeserializerBase#getDefaultImpl()} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBasegetDefaultImpl() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        JavaType defaultImpl = deserializer.getDefaultImpl();

        assertNull(defaultImpl);
    }

    /**
     * Test for {@link TypeDeserializerBase#toString()} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBasetoString() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        String toString = deserializer.toString();

        assertTrue(toString.startsWith("[TypeDeserializerBase"));
        assertTrue(toString.endsWith("]"));
    }

    /**
     * Test for {@link TypeDeserializerBase#findDeserializer(DeserializationContext, String)} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBasefindDeserializer() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        String typeId = "testTypeId";

        // Test null context
        try {
            deserializer.findDeserializer(null, typeId);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null typeId
        try {
            deserializer.findDeserializer(ctxt, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test valid typeId
        JsonDeserializer<Object> deserializer2 = deserializer.findDeserializer(ctxt, typeId);
        assertNotNull(deserializer2);
    }

    /**
     * Test for {@link TypeDeserializerBase#findDefaultImplDeserializer(DeserializationContext)} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBasefindDefaultImplDeserializer() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        DeserializationContext ctxt = MAPPER.getDeserializationContext();

        // Test with no default impl
        assertNull(deserializer.findDefaultImplDeserializer(ctxt));

        // Test with default impl
        JavaType defaultImplType = MAPPER.getTypeFactory().constructType(TestDefaultImpl.class);
        deserializer.setDefaultImpl(defaultImplType);
        JsonDeserializer<Object> deserializer2 = deserializer.findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer2);
        assertEquals(TestDefaultImpl.class, deserializer2.getClass());
    }

    /**
     * Test for {@link TypeDeserializerBase#deserializeWithNativeTypeId(JsonParser, DeserializationContext)} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBasedeserializeWithNativeTypeId() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        JsonParser jsonParser = MAPPER.getFactory().createParser("{\"typeId\":\"TestType\"}");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();

        Object result = deserializer.deserializeWithNativeTypeId(jsonParser, ctxt);
        assertNull(result);
    }

    /**
     * Test for {@link TypeDeserializerBase#handleUnknownTypeId(DeserializationContext, String)} method.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testTypeDeserializerBasehandleUnknownTypeId() throws Exception {
        TypeDeserializerBase deserializer = getTestDeserializer();
        DeserializationContext ctxt = MAPPER.getDeserializationContext();

        JavaType type = deserializer.handleUnknownTypeId(ctxt, "TestType");
        assertNull(type);

        try {
            deserializer.handleMissingTypeId(ctxt, "TestType");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private TypeDeserializerBase getTestDeserializer() throws IOException {
        String typeId = "testTypeId";
        JavaType baseType = MAPPER.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new TestTypeIdResolver();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = false;
        JavaType defaultImpl = MAPPER.getTypeFactory().constructType(TestDefaultImpl.class);

        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        return deserializer;
    }

    private BeanProperty getTestProperty() {
        return new BeanProperty("testProperty", TestClass.class, null);
    }

    private static class TestClass {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY)
        private String type;
    }

    private static class TestDefaultImpl {
        // empty class
    }

    private static class TestTypeIdResolver implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
            if (id.equals("TestType")) {
                return MAPPER.getTypeFactory().constructType(TestClass.class);
            }
            return null;
        }

        @Override
        public String idFromValue(DeserializationContext ctxt, Object value) throws IOException {
            if (value instanceof TestClass) {
                return "TestType";
            }
            return null;
        }

        @Override
        public void initValues(Map<String, Object> initValues) {
            // empty
        }

        @Override
        public Converter<String, String> findValueConverter(DeserializationContext ctxt, JavaType type) throws IOException {
            return null;
        }

        @Override
        public JavaType resolveTypeId(DeserializationContext ctxt, Object value, JavaType typeHint) throws IOException {
            if (value instanceof TestClass) {
                return MAPPER.getTypeFactory().constructType(TestClass.class);
            }
            return null;
        }
    }
}