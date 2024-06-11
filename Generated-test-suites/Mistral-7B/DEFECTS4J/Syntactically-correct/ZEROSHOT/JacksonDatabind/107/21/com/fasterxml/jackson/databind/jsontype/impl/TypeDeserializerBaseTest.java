package com.example.jackson.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

public class TypeDeserializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase INSTANCE = new TypeDeserializerBaseBase() {
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TestTypeDeserializer();
        }
    };

    private static class TestTypeDeserializer extends TypeDeserializerBase {
        private static final long serialVersionUID = 1;

        public TestTypeDeserializer() {
            super(null, null, null, false, null);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TestTypeDeserializer();
        }
    };

    @org.junit.Test
    public void testForProperty() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer base = INSTANCE.forProperty(property);
        assertEquals(TestTypeDeserializer.class, base.getClass());
    }

    @org.junit.Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY, INSTANCE.getTypeInclusion());
    }

    @org.junit.Test
    public void testBaseTypeName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertEquals(TypeDeserializerBase.class.getName(), INSTANCE.baseTypeName());
        assertEquals(baseType.getRawClass().getName(), INSTANCE.baseTypeName());
    }

    @org.junit.Test
    public void testGetPropertyName() throws Exception {
        assertEquals(TypeDeserializerBase.DEFAULT_TYPE_PROPERTY_NAME, INSTANCE.getPropertyName());
    }

    @org.junit.Test
    public void testGetTypeIdResolver() throws Exception {
        assertNull(INSTANCE.getTypeIdResolver());
    }

    @org.junit.Test
    public void testGetBaseType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertEquals(baseType, INSTANCE.baseType());
    }

    @org.junit.Test
    public void testToString() throws Exception {
        assertEquals("[TypeDeserializerBase; base-type:class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase, id-resolver:null]",
                INSTANCE.toString());
    }

    @org.junit.Test
    public void testFindDeserializer() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));

        when(context.getTypeFactory().constructType(String.class)).thenReturn(new TypeReference<String>() {}.getType());

        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);

        JsonDeserializer<Object> deserializer = INSTANCE._findDeserializer(context, "StringTypeId");
        assertEquals(JsonDeserializer.class, deserializer.getClass());
        assertEquals(javaType, deserializer.getType());
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));

        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);

        JsonDeserializer<Object> deserializer = INSTANCE._findDefaultImplDeserializer(context);
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getTypeFactory()).thenReturn(new ObjectMapper().getTypeFactory());

        JavaType baseType = TEST_BASE_TYPE;
        String typeId = "UnknownTypeId";

        JavaType result = INSTANCE._handleUnknownTypeId(context, typeId);
        assertEquals(JsonTypeDeserializer.NULL_TYPE, result);
    }

    @org.junit.Test
    public void testHandleMissingTypeId() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getTypeFactory()).thenReturn(new ObjectMapper().getTypeFactory());

        JavaType baseType = TEST_BASE_TYPE;

        JavaType result = INSTANCE._handleMissingTypeId(context, "MissingTypeId");
        assertEquals(JsonTypeDeserializer.NULL_TYPE, result);
    }

    private static final JavaType TEST_BASE_TYPE = new TypeReference<Map<String, Object>>() {}.getType();
}