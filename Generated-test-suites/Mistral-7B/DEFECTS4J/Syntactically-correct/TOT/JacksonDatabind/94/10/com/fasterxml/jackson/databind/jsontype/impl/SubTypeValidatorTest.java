package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidClass() throws JsonMappingException {
 Object carrier = new Object();
 JavaType validType = mapper.getTypeFactory().constructType(Carrier.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), validType);
 }

 @Test
 public void testSpringClass() throws JsonMappingException {
 Object carrier = new Object();
 JavaType springType = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 try {
 validator.validateSubType(mapper.getDeserializationContext(), springType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testNastyClass() throws JsonMappingException {
 Object carrier = new Object();
 JavaType nastyType = mapper.getTypeFactory().constructType(InvokerTransformer.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 try {
 validator.validateSubType(mapper.getDeserializationContext(), nastyType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testInterfaceAsSuperClass() throws JsonMappingException {
 Object carrier = new Object();
 JavaType interfaceType = mapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
 JavaType subType = mapper.getTypeFactory().constructType(ConcretePointcutAdvisor.class);
 JavaType superClassType = mapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 try {
 validator.validateSubType(mapper.getDeserializationContext(), subType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 JavaType validType = mapper.getTypeFactory().constructType(new TypeReference<Carrier>() {}.getType());
 try {
 validator.validateSubType(mapper.getDeserializationContext(), superClassType.withTypeArgs(subType.getTypeParameters()));
 } catch (JsonMappingException e) {
 fail("Unexpected JsonMappingException");
 }
 }

 @Test
 public void testNastyClassWithSuperClassAsInterface() throws JsonMappingException {
 Object carrier = new Object();
 JavaType nastyTypeWithInterface = mapper.getTypeFactory().constructType(ClassWithSpringInterface.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 try {
 validator.validateSubType(mapper.getDeserializationContext(), nastyTypeWithInterface);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}