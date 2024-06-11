package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireTypeIdResolver;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        mapper = JsonMapperFactory.builder()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        typeDeserializerBase = new BaseTypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                new LaissezFaireTypeIdResolver(),
                "typeName",
                true,
                mapper.getTypeFactory().constructType(Integer.class)
        );
    }

    @Test
    public void testForProperty() throws JsonMappingException, JsonProcessingException {
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializer deserializer = typeDeserializerBase.forProperty(property);
        assertNotNull(deserializer);
        assertEquals(String.class, deserializer.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testForPropertyTwice() throws JsonMappingException, JsonProcessingException {
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializer deserializer = typeDeserializerBase.forProperty(property);
        deserializer = typeDeserializerBase.forProperty(property);
    }

    @Test
    public void testFindDeserializer() throws JsonMappingException, JsonProcessingException {
        String typeId = "java.lang.Integer";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(
                mapper.getDeserializationContext(), typeId
        );
        assertNotNull(deserializer);
        assertEquals(Integer.class, deserializer.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerUnknownTypeId() throws JsonMappingException, JsonProcessingException {
        String typeId = "java.lang.UnknownType";
        typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws JsonMappingException, JsonProcessingException {
        Class<?> raw = Integer.class;
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(
                mapper.getDeserializationContext()
        );
        assertNotNull(deserializer);
        assertEquals(Integer.class, deserializer.getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws JsonMappingException, JsonProcessingException {
        String typeId = "java.lang.UnknownType";
        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(
                mapper.getDeserializationContext(), typeId
        );
        assertNotNull(javaType);
        assertEquals(mapper.getTypeFactory().constructType(Object.class), javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMissingTypeId() throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase._handleMissingTypeId(mapper.getDeserializationContext(), "");
    }

    private static class BaseTypeDeserializerBase extends TypeDeserializerBase {
    }
}