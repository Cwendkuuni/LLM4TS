package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseMapTest;

import java.io.IOException;
import java.util.Map;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest extends BaseMapTest {

    private static final String TYPE_PROPERTY_NAME = "type";

    @SuppressWarnings("unchecked")
    private static <T> JavaType getJavaType(Class<T> clazz) {
        return mapper.getTypeFactory().constructType(clazz);
    }

    @SuppressWarnings("rawtypes")
    private static TypeIdResolver getTypeIdResolver() {
        return mapper.getPolymorphicTypeIdResolver();
    }

    @SuppressWarnings("rawtypes")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = TYPE_PROPERTY_NAME)
    static class TestClass {
        private String typeProperty;
    }

    @SuppressWarnings("rawtypes")
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = TYPE_PROPERTY_NAME)
    static class TestClassWithClassAnnotation {
        private String typeProperty;
    }

    @SuppressWarnings("unchecked")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.PROPERTY, property = TYPE_PROPERTY_NAME)
    static class TestClassWithCustomAnnotation {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NONE, as = TestClassCustom.class)
        private TestClassCustom typeProperty;

        static class TestClassCustom {
            private String customTypeId;
        }
    }

    @Test
    public void testBaseType() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        assertEquals(TestClass.class, tdb.baseType());
    }

    @Test
    public void testTypeIdResolver() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        TypeIdResolver idResolver = tdb.getTypeIdResolver();
        assertNotNull(idResolver);
    }

    @Test
    public void testPropertyName() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        assertEquals(TYPE_PROPERTY_NAME, tdb.getPropertyName());
    }

    @Test
    public void testDefaultImpl() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        assertNull(tdb.getDefaultImpl());
    }

    @Test
    public void testToString() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        String expected = "[TypeDeserializerBase; base-type:class com.fasterxml.jackson.databind.test.TypeDeserializerBaseTest$TestClass; id-resolver=com.fasterxml.jackson.databind.JsonMapping$PolymorphicTypeIdResolver@3c02eb7a; ]";
        assertEquals(expected, tdb.toString());
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = new BeanProperty("typeProperty", TestClass.class, null, null);
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        TypeDeserializerBase forProperty = tdb.forProperty(beanProperty);
        assertEquals(TestClass.class, forProperty.baseType());
        assertEquals(TestClass.class, forProperty.getDefaultImpl());
    }

    @Test
    public void testGetTypeInclusion() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        JsonTypeInfo.As typeInclusion = tdb.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeInclusion);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        DeserializationContext context = mapper.getDeserializationContext();
        String typeId = "TestClass";
        JsonDeserializer<Object> deserializer = tdb._findDeserializer(context, typeId);
        assertEquals(TestClass.class, deserializer.getTargetValueType());

        typeId = "com.fasterxml.jackson.databind.test.TypeDeserializerBaseTest$TestClassWithClassAnnotation";
        deserializer = tdb._findDeserializer(context, typeId);
        assertEquals(TestClassWithClassAnnotation.class, deserializer.getTargetValueType());

        typeId = "somethingElse";
        deserializer = tdb._findDeserializer(context, typeId);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = tdb._findDefaultImplDeserializer(context);
        assertNull(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        JsonParser jp = mapper.getFactory().createParser(mapper.createObjectNode().setText("[]"));
        Object result = tdb._deserializeWithNativeTypeId(jp, context);
        assertNull(result);

        jp = mapper.getFactory().createParser(mapper.createObjectNode().put("type", "TestClassWithClassAnnotation"));
        result = tdb._deserializeWithNativeTypeId(jp, context);
        assertEquals(TestClassWithClassAnnotation.class, result.getClass());
    }

    @Test
    public void testDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        DeserializationContext context = mapper.getDeserializationContext();
        context.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object result = tdb._findDefaultImplDeserializer(context).deserialize(jp, context);
        assertNull(result);

        context.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true);
        result = tdb._findDefaultImplDeserializer(context).deserialize(jp, context);
        assertTrue(result instanceof NullifyingDeserializer);

        TypeDeserializerBase customTypeDeserializerBase = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClassWithCustomAnnotation.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        JsonDeserializer<TestClassCustom> customDeserializer = context.findValueDeserializer(TestClassCustom.class);
        customTypeDeserializerBase._deserializers.put("TestClassCustom", customDeserializer);
        result = customTypeDeserializerBase._findDefaultImplDeserializer(context).deserialize(jp, context);
        assertNotNull(result);
        TestClassCustom testClassCustom = (TestClassCustom) result;
        assertNotNull(testClassCustom.getTypeId());
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase tdb = new TypeDeserializerBase.TypeDeserializerBaseBuilder<>(
                getJavaType(TestClass.class), getTypeIdResolver(), TYPE_PROPERTY_NAME, false, null)
                .build();
        DeserializationContext context = mapper.getDeserializationContext();
        context.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        tdb._handleUnknownTypeId(context, "unknownTypeId", getTypeIdResolver(), null);
        fail("Expected JsonMappingException to be thrown");
    }
}