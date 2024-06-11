package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstruction() {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        assertNotNull(typeDeserializerBase);
    }

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializerBase forProperty = typeDeserializerBase.forProperty(property);
        assertNotNull(forProperty);
        assertEquals(String.class, forProperty.getPropertyName());
        assertEquals(defaultImpl, forProperty.getDefaultImpl());
    }

    @Test
    public void testGetTypeInclusion() {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertTrue(typeInclusion.isExplicit());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(Foo.class.getName(), typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(idRes, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(Bar.class, typeDeserializerBase.getDefaultImpl().getRawClass());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = ObjectMapper.getDefaultInstance();
        String typeId = "com.fasterxml.jackson.databind.jsontype.impl.Baz";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNotNull(deserializer);
        assertEquals(BazDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = ObjectMapper.getDefaultInstance();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
        assertEquals(NullifyingDeserializer.class, deserializer.getClass());
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullTypeId() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = ObjectMapper.getDefaultInstance();
        typeDeserializerBase._handleUnknownTypeId(ctxt, null);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdInvalidTypeId() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = ObjectMapper.getDefaultInstance();
        typeDeserializerBase._handleUnknownTypeId(ctxt, "invalidTypeId");
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeId() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = ObjectMapper.getDefaultInstance();
        typeDeserializerBase._handleMissingTypeId(ctxt, "missingTypeId");
    }

    private static class Foo {
        String testProperty;
        int testInt;
    }

    private static class Bar {
        String testProperty;
        int testInt;
        String testString;

        static class BazDeserializer extends JsonDeserializer<Bar> {
            @Override
            public Bar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                Bar bar = new Bar();
                bar.testString = p.getValueAsString();
                return bar;
            }
        }
    }

    private static class SimpleTypeIdResolver implements TypeIdResolver {
        @Override
        public String idFromValue(Object value) {
            if (value instanceof String) {
                return (String) value;
            }
            return null;
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) {
            if ("com.fasterxml.jackson.databind.jsontype.impl.Baz".equals(typeId)) {
                return ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructType(Baz.class);
            }
            return null;
        }
    }
}