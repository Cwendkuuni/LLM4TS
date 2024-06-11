package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Unit tests for {@link SubTypeValidator} class
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

    private static ObjectMapper objectMapper;
    private static SubTypeValidator validator;

    @BeforeClass
    public static void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidType() throws JsonProcessingException {
        JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
        validator.validateSubType(objectMapper.getDeserializationContext(), validType);
    }

    @Test
    public void testInvalidTypeWithSimpleName() throws JsonMappingException {
        JavaType invalidType = TypeFactory.defaultInstance().constructType(com.spring.framework.NastyClass.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), invalidType));
    }

    @Test
    public void testInvalidTypeWithFullName() throws JsonMappingException {
        JavaType invalidType = TypeFactory.defaultInstance().constructType(org.apache.commons.collections4.functors.InstantiateTransformer.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), invalidType));
    }

    @Test
    public void testInvalidTypeWithSpringPrefix() throws JsonMappingException {
        JavaType invalidType = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), invalidType));
    }

    @Test
    public void testValidTypeWithSpringPrefix() throws JsonMappingException {
        JavaType validType = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.BeanDefinition.class);
        validator.validateSubType(objectMapper.getDeserializationContext(), validType);
    }

    @Test
    public void testValidateSet() {
        Set<String> set = SubTypeValidator.DEFAULT_NO_DESER_CLASS_names;
        // Courtesy of [https://github.com/kantega/notsoserial]
        assertTrue(set.contains("org.apache.commons.collections.functors.InvokerTransformer"));
        assertTrue(set.contains("org.apache.commons.collections4.functors.InvokerTransformer"));
        // [databind#1680]
        assertTrue(set.contains("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl"));
        assertTrue(set.contains("org.apache.xalan.xsltc.trax.TemplatesImpl"));
        // [databind#1737]
        assertTrue(set.contains("java.util.logging.FileHandler"));
        assertTrue(set.contains("java.rmi.server.UnicastRemoteObject"));
        // [databind#1855]
        assertTrue(set.contains("org.springframework.beans.factory.config.PropertyPathFactoryBean"));
        assertTrue(set.contains("com.mchange.v2.c3p0.JndiRefForwardingDataSource"));
        assertTrue(set.contains("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource"));
        // [databind#1855] more 3rd party
        assertTrue(set.contains("org.apache.tomcat.dbcp.dbcp2.BasicDataSource"));
        assertTrue(set.contains("com.sun.org.apache.bcel.internal.util.ClassLoader"));
        // custom test
        assertTrue(set.contains("com.mycompany.MyNastyClass"));
    }
}