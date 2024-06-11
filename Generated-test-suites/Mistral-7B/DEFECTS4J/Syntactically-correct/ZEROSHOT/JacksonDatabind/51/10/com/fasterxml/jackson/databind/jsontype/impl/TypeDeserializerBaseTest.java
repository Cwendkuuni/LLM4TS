package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class TypeDeserializerBaseTest extends BaseTest {

    private static final String TEST_TYPE_ID = "testTypeId";

    private TypeIdResolver _idResolver;
    private JavaType _baseType;
    private TypeDeserializerBase _testee;

    @Before
    public void setup() throws Exception {
        _idResolver = new SimpleTypeIdResolver();
        _baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        _testee = new TestTypeDeserializerBase(_baseType, _idResolver, null, false, null);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = mapper().getSerializerProvider().findBeanProperty(TypeDeserializerBase.class, "property");
        TypeDeserializer forProperty = _testee.forProperty(property);
        assertEquals(TypeDeserializer.class, forProperty.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = _testee.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String baseTypeName = _testee.baseTypeName();
        assertEquals(TypeDeserializerBase.class.getName(), baseTypeName);
    }

    @Test
    public void testPropertyName() throws Exception {
        String propertyName = _testee.getPropertyName();
        assertEquals("", propertyName);
    }

    @Test
    public void testgetTypeIdResolver() throws Exception {
        TypeIdResolver typeIdResolver = _testee.getTypeIdResolver();
        assertEquals(_idResolver, typeIdResolver);
    }

    @Test
    public void testDefaultImpl() throws Exception {
        Class<?> defaultImpl = _testee.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToString() throws Exception {
        String toString = _testee.toString();
        assertTrue(toString.contains("[TypeDeserializerBase"));
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = TEST_TYPE_ID;
        JsonDeserializer<Object> deserializer = _testee._findDeserializer(mapper().getDeserializationContext(), typeId);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> defaultImplDeserializer = _testee._findDefaultImplDeserializer(mapper().getDeserializationContext());
        assertNull(defaultImplDeserializer);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNull() throws Exception {
        _testee._handleUnknownTypeId(mapper().getDeserializationContext(), null, _idResolver, _baseType);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdInvalid() throws Exception {
        _testee._handleUnknownTypeId(mapper().getDeserializationContext(), "invalidTypeId", _idResolver, _baseType);
    }

    @Test
    public void testHandleUnknownTypeIdValid() throws Exception {
        JavaType javaType = mapper().getTypeFactory().constructType(String.class);
        JsonDeserializer<String> deserializer = mapper().getDeserializationContext().findContextualValueDeserializer(javaType, null);
        _testee._deserializers.put("validTypeId", deserializer);
        JavaType handledType = _testee._handleUnknownTypeId(mapper().getDeserializationContext(), "validTypeId", _idResolver, _baseType);
        assertEquals(javaType, handledType);
    }
}

class TestTypeDeserializerBase extends TypeDeserializerBase {

    public TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
        super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
    }
}