package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

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
 * Test cases for {@link SubTypeValidator}.
 *
 * @author <a href="mailto:kostas.skarpelis@gmail.com">Kostas Skarpelis</a>
 */
public class SubTypeValidatorTest {

    private static SubTypeValidator validator;
    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setup() {
        validator = SubTypeValidator.instance();
        objectMapper = new ObjectMapper();
    }

    /**
     * Test valid subtype.
     */
    @Test
    public void testValidSubtype() throws JsonProcessingException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Person.class);
        validator.validateSubType(objectMapper.getDeserializationContext(), javaType);
    }

    /**
     * Test invalid subtype.
     */
    @Test
    public void testInvalidSubtype() {
        String fullClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
        JavaType javaType = TypeFactory.defaultInstance().constructType(fullClassName);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), javaType));
    }

    /**
     * Test invalid subtype starting with Spring prefix.
     */
    @Test
    public void testInvalidSpringSubtype() {
        String fullClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
        JavaType javaType = TypeFactory.defaultInstance().constructType(fullClassName);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), javaType));
    }

    /**
     * Test invalid subtype in interface.
     */
    @Test
    public void testInvalidInterfaceSubtype() {
        String fullClassName = "org.apache.commons.collections.functors.Transformer";
        JavaType javaType = TypeFactory.defaultInstance().constructType(fullClassName.getBytes());

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), javaType));
    }

    /**
     * Test valid Spring sub-type.
     */
    @Test
    public void testValidSpringSubtype() {
        String fullClassName = "com.mycompany.MyApplicationContext";

        Set<String> classNames = new HashSet<String>();
        classNames.add(fullClassName);
        validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = Collections.unmodifiableSet(classNames);
        };

        JavaType javaType = TypeFactory.defaultInstance().constructType(fullClassName);
        validator.validateSubType(objectMapper.getDeserializationContext(), javaType);
    }
}

class Person {}