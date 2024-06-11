package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.fail;

public class SubTypeValidatorTest {

private ObjectMapper mapper = new ObjectMapper();

@Test
public void testValidType() throws Exception {
JavaType validType = mapper.getTypeFactory().constructType(String.class);
SubTypeValidator validator = SubTypeValidator.instance();
validator.validateSubType(mapper.getDeserializationContext(), validType);
}

@Test
public void testNastyClass() throws Exception {
JavaType nastyType = mapper.getTypeFactory().constructType(new TypeReference<com.mchange.v2.c3p0.JndiRefForwardingDataSource>() {});
SubTypeValidator validator = SubTypeValidator.instance();
try {
validator.validateSubType(mapper.getDeserializationContext(), nastyType);
fail("Should have thrown an exception");
} catch (JsonMappingException e) {
// Expected
}
}

@Test
public void testSpringClass() throws Exception {
JavaType springType = mapper.getTypeFactory().constructType(new TypeReference<org.springframework.beans.factory.config.PropertyPathFactoryBean>() {});
SubTypeValidator validator = SubTypeValidator.instance();
try {
validator.validateSubType(mapper.getDeserializationContext(), springType);
fail("Should have thrown an exception");
} catch (JsonMappingException e) {
// Expected
}
}

@Test
public void testValidClassWithPrefix() throws Exception {
Class<?> validClassWithPrefix = Class.forName("org.apache.commons.logging.LogFactory");
JavaType validTypeWithPrefix = mapper.getTypeFactory().constructType(validClassWithPrefix.getName());
SubTypeValidator validator = SubTypeValidator.instance();
validator.validateSubType(mapper.getDeserializationContext(), validTypeWithPrefix);
}

@Test(expected = JsonMappingException.class)
public void testNastyClassWithPrefix() throws Exception {
Class<?> nastyClassWithPrefix = Class.forName("org.apache.commons.collections4.functors.InstantiateTransformer");
JavaType nastyTypeWithPrefix = mapper.getTypeFactory().constructType(nastyClassWithPrefix.getName());
SubTypeValidator validator = SubTypeValidator.instance();
validator.validateSubType(mapper.getDeserializationContext(), nastyTypeWithPrefix);
}
}