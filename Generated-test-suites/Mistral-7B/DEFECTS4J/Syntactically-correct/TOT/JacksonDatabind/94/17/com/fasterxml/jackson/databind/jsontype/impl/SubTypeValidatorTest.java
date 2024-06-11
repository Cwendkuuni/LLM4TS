package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testValidNontSpringClassDeserialization() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(mapper.getDeserializationContext(), javaType);
    }

    @Test
    public void testInvalidSpringClassDeserialization() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), javaType));
    }

    @Test
    public void testInvalidSpringInterfaceDeserialization() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(mapper.getDeserializationContext(), javaType);
    }

    @Test
    public void testInvalidSpringClassHierarchyDeserialization() throws Exception {
        JavaType parentClass = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
        JavaType childClass = mapper.getTypeFactory().constructType(WebApplicationContext.class);
        TypeIdResolver idResolver = mapper.getTypeFactory().getPolymorphicTypeIdResolver();
        idResolver.addTypeIdResolver(ApplicationContext.class, new TypeIdResolverBase() {
            @Override
            public String idFromValueAndType(Object value, JavaType type) {
                return "ApplicationContext";
            }
        });
        JavaType javaType = mapper.getTypeFactory().constructType("com.example.MyClass");
        javaType = mapper.getTypeFactory().constructType(idResolver.resolveValueId(javaType, new Object(), parentClass));
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), javaType));
    }
}