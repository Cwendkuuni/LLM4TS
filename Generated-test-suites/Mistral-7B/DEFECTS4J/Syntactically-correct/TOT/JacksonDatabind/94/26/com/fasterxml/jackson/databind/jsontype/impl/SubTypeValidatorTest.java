package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private static final SubTypeValidator instance = SubTypeValidator.instance();

 @Test
 public void testValidType() throws JsonMappingException, IOException {
 final ObjectMapper mapper = new ObjectMapper();
 final JavaType validType = mapper.getTypeFactory().constructType(String.class);
 instance.validateSubType(mapper.getContext(), validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyType() throws JsonMappingException, IOException {
 final ObjectMapper mapper = new ObjectMapper();
 final JavaType nastyType = mapper.getTypeFactory().constructType(InvokerTransformer.class);
 instance.validateSubType(mapper.getContext(), nastyType);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringType() throws JsonMappingException, IOException {
 final ObjectMapper mapper = new ObjectMapper();
 final JavaType springType = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
 instance.validateSubType(mapper.getContext(), springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownPrefixType() throws JsonMappingException, IOException {
 final ObjectMapper mapper = new ObjectMapper();
 final JavaType unknownPrefixType = mapper.getTypeFactory().constructType(UnknownPrefixType.class);
 instance.validateSubType(mapper.getContext(), unknownPrefixType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInterfaceType() throws JsonMappingException, IOException {
 final ObjectMapper mapper = new ObjectMapper();
 final JavaType interfaceType = mapper.getTypeFactory().constructType(Serializable.class);
 instance.validateSubType(mapper.getContext(), interfaceType);
 }

 @Test
 public void testValidSubTypeHierarchy() throws JsonMappingException, IOException {
 Set<String> names = new HashSet<>();
 names.add("com.example.MyClassOne");
 names.add("com.example.MyClassTwo");
 names.add("com.example.MyClassThree");
 names.add("com.example.MyClassFour");
 final JavaType rootType = mapper.getTypeFactory().constructType(MyBaseClass.class);
 final JavaType subType = mapper.getTypeFactory().constructType(new TypeReference<Class<?>>() {}.getType());
 final TypeReference<Set<String>> typeRef = new TypeReference<Set<String>>() {}.getType();
 final JavaType javaType = mapper.getTypeFactory().constructType(typeRef);
 final ObjectMapper objectMapper = new ObjectMapper();
 final String json = "{\"root\":{\"subType\":{\"names\":[" + String.join(",", Arrays.asList(names.toArray(new String[0]))) + "]}}}";
 final Object rootObject = objectMapper.readValue(json, rootType);
 final Object subObject = ((Map<?, ?>) rootObject).get("subType");
 final Object subTypeObject = ((Map<?, ?>) subObject).get("names");
 final Set<String> actualNames = (Set<String>) subTypeObject;
 assertEquals(names, actualNames);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubTypeHierarchy() throws JsonMappingException, IOException {
 setUpNastyTypes();
 final ObjectMapper mapper = new ObjectMapper();
 final JavaType rootType = mapper.getTypeFactory().constructType(MyBaseClass.class);
 final JavaType subType = mapper.getTypeFactory().constructType(new TypeReference<Class<?>>() {}.getType());
 final TypeReference<Set<String>> typeRef = new TypeReference<Set<String>>() {}.getType();
 final JavaType javaType = mapper.getTypeFactory().constructType(typeRef);
 final ObjectMapper objectMapper = new ObjectMapper();
 final String json = "{\"root\":{\"subType\":{" + "\"names\":" + "[" // NOSONAR
 + String.join(",", Arrays.asList(NastyTypeOne.class.getName(), NastyTypeTwo.class.getName())) // NOSONAR
 + "]}}}";
 final Object rootObject = objectMapper.readValue(json, rootType);
 }

 private void setUpNastyTypes() {
 final Class<?> nastyTypeOne = Class.forName("org.apache.commons.collections.functors.InvokerTransformer"); // NOSONAR
 final Class<?> nastyTypeTwo = Class.forName("com.example.NastyTypeTwo"); // NOSONAR
 }
}

class MyBaseClass {
 Set<String> names;
 Set<String> getNames() { return names; }
}