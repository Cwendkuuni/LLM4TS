package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;

/**
 * JUnit 4 test file to test all methods in the SubTypeValidator class.
 */
public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithLegalClass() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        validator.validateSubType(new DeserializationContext(mapper.getConfig()), javaType);
    }

    @Test
    public void testValidateSubTypeWithIllegalClassStartingWithPrefixSpring() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(mapper.getConfig()), javaType));
    }

    @Test
    public void testValidateSubTypeWithIllegalClassNotStartingWithPrefixSpringButInSpringPackage() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(org.springframework.beans.factory.support.DefaultListableBeanFactory.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(mapper.getConfig()), javaType));
    }

    @Test
    public void testValidateSubTypeWithIllegalClassNotInSpringPackageOrStartingWithPrefixSpring() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(java.util.logging.FileHandler.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(mapper.getConfig()), javaType));
    }

    @Test
    public void testValidateSubTypeWithInterface() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(java.io.Serializable.class.getClassLoader().getInterfaceTypes()[0]);

        validator.validateSubType(new DeserializationContext(mapper.getConfig()), javaType);
    }

    @Test
    public void testValidateSubTypeWithIllegalClassInHashSet() throws Exception {
        Set<String> illegalClassNames = new HashSet<>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
        illegalClassNames.add(org.apache.commons.collections.functors.InvokerTransformer.class.getName());

        SubTypeValidator validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(org.apache.commons.collections.functors.InvokerTransformer.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(mapper.getConfig()), javaType));
    }

    @Test
    public void testValidateSubTypeWithIllegalClassInCollectionsUnmodifiableSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(JsonMappingException.class);

        assertThrows(JsonMappingException.class, () -> SubTypeValidator.instance().validateSubType(new DeserializationContext(mapper.getConfig()), javaType));
    }
}