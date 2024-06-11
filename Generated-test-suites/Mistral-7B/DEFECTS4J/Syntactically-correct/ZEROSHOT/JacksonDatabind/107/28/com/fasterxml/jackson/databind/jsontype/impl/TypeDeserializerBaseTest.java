package com.example.jackson.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        TypeIdResolverBase idResolver = new TypeIdResolverBase();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        BeanProperty property = new BeanProperty("_property", String.class);
        TypeDeserializerBase copiedTypeDeserializerBase = new TypeDeserializerBase(typeDeserializerBase, property);

        assertEquals(baseType, copiedTypeDeserializerBase.getBaseType());
        assertEquals(idResolver, copiedTypeDeserializerBase.getTypeIdResolver());
        assertEquals(typePropertyName, copiedTypeDeserializerBase.getPropertyName());
        assertEquals(typeIdVisible, copiedTypeDeserializerBase.isTypeIdVisible());
        assertEquals(defaultImpl, copiedTypeDeserializerBase.getDefaultImpl());
        assertEquals(copiedTypeDeserializerBase, copiedTypeDeserializerBase.forProperty(property));
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY_TYPES, copiedTypeDeserializerBase.getTypeInclusion());
        assertEquals("baseType:class com.fasterxml.jackson.core.type.TypeReferenceToClass[Ljava/lang/Object;]",
                copiedTypeDeserializerBase.toString());

        assertEquals(baseType, copiedTypeDeserializerBase.baseType());

        String jsonString = "{\"value\":\"test\", \"type\":\"StringType\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        DeserializationContext context = mapper.getDeserializationContext();

        JsonDeserializer<Object> deserializer = copiedTypeDeserializerBase._findDeserializer(context, "StringType");

        Object result = deserializer.deserialize(jsonParser, context);

        assertEquals("test", result);
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        TypeIdResolverBase idResolver = new TypeIdResolverBase();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String jsonString = "{\"value\":\"test\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        DeserializationContext context = mapper.getDeserializationContext();

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);

        Object result = deserializer.deserialize(jsonParser, context);

        fail("Expected IOException but got " + result.getClass().getName());
    }

    @JsonDeserialize(using = FromStringDeserializer.class)
    static class StringType {
        private final String value;

        public StringType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        TypeIdResolverBase idResolver = new TypeIdResolverBase();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        JavaType stringType = mapper.getTypeFactory().constructType(StringType.class);
        JsonDeserializer<StringType> stringTypeDeserializer = mapper.findValueDeserializer(StringType.class);

        typeDeserializerBase._deserializers.put("StringType", stringTypeDeserializer);

        String jsonString = "{\"value\":\"test\", \"type\":\"StringType\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        DeserializationContext context = mapper.getDeserializationContext();

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, "StringType");

        Object result = deserializer.deserialize(jsonParser, context);

        assertEquals(StringType.class, result.getClass());
        assertEquals("test", ((StringType) result).getValue());
    }
}