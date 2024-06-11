package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for SubTypeValidator.
 */
public class SubTypeValidatorTest {

    @Test
    public void testValidTypes() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType validType = mapper.getTypeFactory().constructType(String.class);
        validator.validateSubType(mapper.getDeserializationContext(), validType);
    }

    @Test
    public void testNastyClasses() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType nastyType1 = mapper.getTypeFactory().constructType(InvokerTransformer.class);
        JavaType nastyType2 = mapper.getTypeFactory().constructType(InstantiateTransformer.class);
        JavaType nastyType3 = mapper.getTypeFactory().constructType(JdbcRowSetImpl.class);
        JavaType nastyType4 = mapper.getTypeFactory().constructType(FileHandler.class);
        JavaType nastyType5 = mapper.getTypeFactory().constructType(UnicastRemoteObject.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType2));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType3));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType4));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType5));
    }

    @Test
    public void testSpringClasses() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType springType1 = mapper.getTypeFactory().constructType(AbstractBeanFactoryPointcutAdvisor.class);
        JavaType springType2 = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
        JavaType springType3 = mapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);

        // SpringApplicationContext is not in the list of nasty classes
        JavaType springType4 = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
        validator.validateSubType(mapper.getDeserializationContext(), springType1);
        validator.validateSubType(mapper.getDeserializationContext(), springType2);
        validator.validateSubType(mapper.getDeserializationContext(), springType3);
        validator.validateSubType(mapper.getDeserializationContext(), springType4);
    }

    @Test
    public void testOtherClasses() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType otherType1 = mapper.getTypeFactory().constructType(BasicDataSource.class);
        JavaType otherType2 = mapper.getTypeFactory().constructType(ClassLoader.class);

        validator.validateSubType(mapper.getDeserializationContext(), otherType1);
        validator.validateSubType(mapper.getDeserializationContext(), otherType2);
    }
}