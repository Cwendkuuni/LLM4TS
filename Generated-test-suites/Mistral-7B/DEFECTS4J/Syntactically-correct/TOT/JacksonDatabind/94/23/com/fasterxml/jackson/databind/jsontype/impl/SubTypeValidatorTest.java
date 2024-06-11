package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidateSubType_ValidType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(setupContext(), validType);
 }

 @Test
 public void testValidateSubType_InvalidType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(setupContext(), invalidType));
 }

 @Test
 public void testValidateSubType_Interface() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class.getInterfaces()[0]);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(setupContext(), invalidType));
 }

 @Test
 public void testValidateSubType_SpringClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springClass = TypeFactory.defaultInstance().constructType(Object[].class);
 JavaType springSubClass = TypeFactory.defaultInstance().constructType(Arrays.asList(new Object[] {new Object() {}}).getClass());
 JavaType type = TypeFactory.defaultInstance().constructType("com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator$1");
 JavaType contextType = TypeFactory.defaultInstance().constructType(DeserializationContext.class);
 JavaType typeWithContext = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, contextType, type);
 JavaType springType = TypeFactory.defaultInstance().constructType("org.springframework.beans.factory.BeanFactory");
 JavaType subSpringType = TypeFactory.defaultInstance().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 JavaType typeWithSpring = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, contextType, type, springType, subSpringType);

 // Tests for "AbstractApplicationContext"
 JavaType applicationContextType = TypeFactory.defaultInstance().constructType("org.springframework.context.ApplicationContext");
 JavaType applicationContextSubType = TypeFactory.defaultInstance().constructType("org.springframework.context.support.FileSystemXmlApplicationContext");
 JavaType typeWithApplicationContext = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, contextType, type, applicationContextType, applicationContextSubType);
 validator.validateSubType(setupContext(), typeWithApplicationContext);

 // Tests for "AbstractBeanFactoryPointcutAdvisor"
 JavaType advisorType = TypeFactory.defaultInstance().constructType("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");
 JavaType typeWithAdvisor = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, contextType, type, advisorType);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(setupContext(), typeWithAdvisor));

 // Tests for "PropertyPathFactoryBean"
 JavaType beanFactoryType = TypeFactory.defaultInstance().constructType("org.springframework.beans.factory.BeanFactory");
 JavaType factoryBeanType = TypeFactory.defaultInstance().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 JavaType typeWithFactoryBean = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, contextType, type, beanFactoryType, factoryBeanType);
 validator.validateSubType(setupContext(), typeWithFactoryBean);

 // Tests for "com.mchange.v2.c3p0.ComboPooledDataSource" and "com.mchange.v2.c3p0.debug.AfterCloseLoggingComboPooledDataSource"
 JavaType c3p0DataSourceType = TypeFactory.defaultInstance().constructType("com.mchange.v2.c3p0.ComboPooledDataSource");
 JavaType loggingDataSourceType = TypeFactory.defaultInstance().constructType("com.mchange.v2.c3p0.debug.AfterCloseLoggingComboPooledDataSource");
 JavaType typeWithC3P0DataSource = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, contextType, type, c3p0DataSourceType);
 JavaType typeWithLoggingDataSource = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, contextType, type, loggingDataSourceType);
 validator.validateSubType(setupContext(), typeWithC3P0DataSource);
 validator.validateSubType(setupContext(), typeWithLoggingDataSource);
 }

 private DeserializationContext setupContext() {
 return MAPPER.getDeserializationContext();
 }

 @Test
 public void testValidateSubType_CustomListType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType listType = TypeFactory.defaultInstance().constructType(Arrays.asList(Integer.class).getClass());
 JavaType typeWithListType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, JavaTypeFactory.instance().constructType(DeserializationContext.class), listType);
 validator.validateSubType(setupContext(), typeWithListType);
 }

}