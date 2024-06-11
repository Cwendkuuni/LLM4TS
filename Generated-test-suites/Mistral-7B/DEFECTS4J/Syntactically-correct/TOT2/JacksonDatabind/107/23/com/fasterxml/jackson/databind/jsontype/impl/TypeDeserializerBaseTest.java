package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TypeDeserializerBaseTest {

    private DeserializationContext ctxt;
    private JsonFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new JsonFactory();
        ctxt = factory.createContext();
    }

    @Test
    public void testForProperty() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializer.class, forProperty.getClass());
    }

    @Test
    public void testGetTypeInclusion() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        assertEquals(JsonTypeInfo.As.WRAP_IN_ARRAY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        assertEquals(Object.class.getName(), typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testToString() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        String expected = String.format("[%s; base-type:%s; id-resolver: %s]",
                TypeDeserializerBase.class.getName(), baseType.getRawClass().getName(), idResolver);
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializerNull() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        assertNull(typeDeserializerBase._findDeserializer(ctxt, null));
    }

    @Test
    public void testFindDeserializerEmptyTypeId() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        assertNull(typeDeserializerBase._findDeserializer(ctxt, ""));
    }

    @Test
    public void testFindDeserializerUnknownTypeId() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        Object typeId = "unknownTypeId";
        assertThrows(IOException.class, () -> typeDeserializerBase._findDeserializer(ctxt, typeId));
    }

    @Test
    public void testFindDeserializerTypeId() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        String typeId = "com.fasterxml.jackson.databind.jsontype.impl.MyClass";
        JsonDeserializer<Object> deserializer = new MyClassDeserializer();
        idResolver.setValueHandler(typeId, ctxt, deserializer);
        JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertEquals(deserializer, foundDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(ctxt));
    }

    @Test
    public void testFindDefaultImplDeserializerNull() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        TypeDeserializerBase.NullifyingDeserializer nullifyingDeserializer = TypeDeserializerBase.NullifyingDeserializer.instance;
        typeDeserializerBase._defaultImplDeserializer = nullifyingDeserializer;
        assertSame(nullifyingDeserializer, typeDeserializerBase._findDefaultImplDeserializer(ctxt));
    }

    @Test
    public void testHandleUnknownTypeId() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        String unknownTypeId = "unknownTypeId";
        assertThrows(IOException.class, () -> typeDeserializerBase._handleUnknownTypeId(ctxt, unknownTypeId));
    }

    @Test
    public void testHandleMissingTypeId() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        assertThrows(IOException.class, () -> typeDeserializerBase._handleMissingTypeId(ctxt, ""));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws JsonMappingException, JsonProcessingException {
        JavaType baseType = factory.constructType(Object.class);
        TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        String typeId = "MyClass";
        JsonParser parser = factory.createParser("{\"typeId\":\"" + typeId + "\"}");
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, ctxt);
        // Assuming 'MyClassDeserializer' is defined for 'MyClass'
        assertInstanceOf(MyClassDeserializer.class, result);
    }

    static class MyClassDeserializer extends JsonDeserializer<MyClass> {
        @Override
        public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyClass();
        }
    }

    static class MyClass {
        // Empty class for testing.
    }
}