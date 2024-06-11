package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JacksonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setActiveTypeId(null);
        ctxt.setInputFilter(new com.fasterxml.jackson.core.BaseFilter() {
            @Override
            public boolean canDeserialize(Class<?> cls) {
                return cls.isAssignableFrom(TypeDeserializerBase.class);
            }
        });

        String json = "{\"typeId\":\"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\",\"value\":42}";
        JsonParser jp = mapper.getFactory().createParser(json);

        TypeDeserializerBase deserializedObject = typeDeserializer.deserialize(jp, ctxt);
        assertEquals(TypeDeserializerBase.class, deserializedObject.getClass());
        assertNull(deserializedObject._property);
        assertEquals(42, deserializedObject._idResolver.getIdValue(ctxt, deserializedObject, null));
    }

    @Test
    public void testDefaultImplDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setActiveTypeId(null);
        ctxt.setInputFilter(new com.fasterxml.jackson.core.BaseFilter() {
            @Override
            public boolean canDeserialize(Class<?> cls) {
                return cls.isAssignableFrom(TypeDeserializerBase.class);
            }
        });

        String json = "{\"typeId\":\"null\"}";
        JsonParser jp = mapper.getFactory().createParser(json);

        TypeDeserializerBase deserializedObject = typeDeserializer.deserialize(jp, ctxt);
        assertEquals(TypeDeserializerBase.class, deserializedObject.getClass());
        assertNull(deserializedObject._property);
        assertNull(deserializedObject._idResolver);
        assertNull(deserializedObject._baseType);
        assertNull(deserializedObject._defaultImpl);
        assertNull(deserializedObject._typePropertyName);
        assertNull(deserializedObject._typeIdVisible);
        assertNull(deserializedObject._deserializers);
        assertNull(deserializedObject._defaultImplDeserializer);
    }

    @Test
    public void testPropertyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        BeanProperty property = mapper.findBeanProperty("propertyName", String.class);
        TypeDeserializerBase deserializedObject = typeDeserializer.forProperty(property);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setInputFilter(new com.fasterxml.jackson.core.BaseFilter() {
            @Override
            public boolean canDeserialize(Class<?> cls) {
                return cls.isAssignableFrom(TypeDeserializerBase.class);
            }
        });

        String json = "{\"typeId\":\"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\",\"propertyName\":\"test\"}";
        JsonParser jp = mapper.getFactory().createParser(json);

        TypeDeserializerBase deserializedPropertyObject = deserializedObject.deserialize(jp, ctxt);
        assertEquals(TypeDeserializerBase.class, deserializedPropertyObject.getClass());
        assertEquals("propertyName", deserializedPropertyObject._property.getName());
        assertEquals("test", deserializedPropertyObject._idResolver.getIdValue(ctxt, deserializedPropertyObject, null));
    }

    @Test
    public void testTypeIdVisibleTrue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        String json = "{\"typeId\":\"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\",\"_typeIdVisible\":true}";
        JsonParser jp = mapper.getFactory().createParser(json);

        TypeDeserializerBase deserializedObject = typeDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(true, deserializedObject._typeIdVisible);
    }

    @Test
    public void testTypeIdVisibleFalse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        String json = "{\"typeId\":\"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\",\"_typeIdVisible\":false}";
        JsonParser jp = mapper.getFactory().createParser(json);

        TypeDeserializerBase deserializedObject = typeDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(false, deserializedObject._typeIdVisible);
    }

    @Test(expected = JsonProcessingException.class)
    public void testUnknownTypeId() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        String json = "{\"typeId\":\"com.unknown.TypeDeserializerBase\"}";
        JsonParser jp = mapper.getFactory().createParser(json);

        typeDeserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test
    public void testFindDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        String json = "{\"typeId\":\"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\",\"value\":42}";
        JsonParser jp = mapper.getFactory().createParser(json);

        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(mapper.getDeserializationContext(), "com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase");
        assertEquals(TypeDeserializerBase.class, deserializer.getBindingClass());

        JsonDeserializer<Object> nullDeserializer = typeDeserializer._findDeserializer(mapper.getDeserializationContext(), "null");
        assertNull(nullDeserializer);
    }
}