package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSettableAnyProperty() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
        BeanProperty property = mapper.findBeanProperty("testSettableAnyProperty", javaType, null);
        AnnotatedMember setter = PropertyName.standard("testSettableAnyProperty").get(javaType);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);

        Object pojo = mapper.readValue(
                "{ \"testSettableAnyProperty\": { \"key1\": \"value1\", \"key2\": null } }", javaType);
        Map<String, String> map = (Map<String, String>) pojo;

        // set a value using testSettableAnyProperty
        SettableAnyProperty testSettableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);
        testSettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser("{ \"key3\": \"value3\" }"), mapper.getDeserializationContext(), pojo, "key3");
        assertEquals("value3", map.get("key3"));

        // test setting a null value
        testSettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser("{ \"key4\": null }"), mapper.getDeserializationContext(), pojo, "key4");
        assertNull(map.get("key4"));

        Object id = mapper.readTree("{ \"testId\": {} }").get("testId").getObjectId();
        SettableAnyProperty referencingSettableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null).withValueDeserializer(mapper.getFactory().constructValueDeserializer(String.class, mapper.getBeanFactory().findValueDeserializer(String.class, null)));
        AnySetterReferring anySetterReferring = new AnySetterReferring(referencingSettableAnyProperty, (UnresolvedForwardReference) id, javaType.getRawClass(), pojo, "key5");
        id.appendValue("value5");
        referencingSettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser("{ \"key5\": \"value5\" }"), mapper.getDeserializationContext(), pojo, "key5");
        assertEquals("value5", map.get("key5"));
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyInvalidType() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class);
        BeanProperty property = mapper.findBeanProperty("testSettableAnyProperty", javaType, null);
        AnnotatedMember setter = PropertyName.standard("testSettableAnyProperty").get(javaType);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);

        Object pojo = mapper.readValue(
                "{ \"testSettableAnyProperty\": { \"key1\": \"value1\", \"key2\": 1 } }", javaType);
        Map<String, Object> map = (Map<String, Object>) pojo;

        // try to set a value of wrong type
        try {
            settableAnyProperty.deserializeAndSet(mapper.getFactory().createParser("{ \"key3\": 3 }"), mapper.getDeserializationContext(), pojo, "key3");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Expected type: class java.lang.String"));
            throw e;
        }
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettableAnyPropertyNullValue() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
        BeanProperty property = mapper.findBeanProperty("testSettableAnyProperty", javaType, null);
        AnnotatedMember setter = PropertyName.standard("testSettableAnyProperty").get(javaType);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);

        Object pojo = mapper.readValue(
                "{ \"testSettableAnyProperty\": null }", javaType);
        Map<String, String> map = (Map<String, String>) pojo;

        try {
            settableAnyProperty.deserializeAndSet(mapper.getFactory().createParser("{ \"key\": null }"), mapper.getDeserializationContext(), pojo, "key");
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to set null value", e);
        }
        fail();
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyMissingValue() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
        BeanProperty property = mapper.findBeanProperty("testSettableAnyProperty", javaType, null);
        AnnotatedMember setter = PropertyName.standard("testSettableAnyProperty").get(javaType);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);

        Object pojo = mapper.readValue(
                "{ \"testSettableAnyProperty\": {} }", javaType);
        Map<String, String> map = (Map<String, String>) pojo;

        try {
            settableAnyProperty.deserializeAndSet(mapper.getFactory().createParser("{}"), mapper.getDeserializationContext(), pojo, "key");
        } catch (IOException e) {
            throw new JsonMappingException("Expected a JSON object to find a value to set", e);
        }
        fail();
    }

    @Test
    public void testSettableAnyPropertyEmptyValue() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
        BeanProperty property = mapper.findBeanProperty("testSettableAnyProperty", javaType, null);
        AnnotatedMember setter = PropertyName.standard("testSettableAnyProperty").get(javaType);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);

        Object pojo = mapper.readValue(
                "{ \"testSettableAnyProperty\": {} }", javaType);
        Map<String, String> map = (Map<String, String>) pojo;

        String emptyValue = "";
        settableAnyProperty.deserializeAndSet(mapper.getFactory().createParser("{ \"key\": \""+emptyValue+"\" }"), mapper.getDeserializationContext(), pojo, "key");
        assertEquals(emptyValue, map.get("key"));
    }

    @Test
    public void testSettableAnyPropertyValueDeserializer() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
        BeanProperty property = mapper.findBeanProperty("testSettableAnyProperty", javaType, null);
        AnnotatedMember setter = PropertyName.standard("testSettableAnyProperty").get(javaType);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, mapper.getFactory().constructValueDeserializer(String[].class, mapper.getBeanFactory().findValueDeserializer(String[].class, null)));

        Object pojo = mapper.readValue(
                "{ \"testSettableAnyProperty\": [\"value1\", \"value2\"] }", javaType);
        Map<String, Object> map = (Map<String, Object>) pojo;

        String[] expectedValue = new String[]{"value1", "value2"};
        settableAnyProperty.deserializeAndSet(mapper.getFactory().createParser("{ \"key\": ["+String.join("", Arrays.asList(expectedValue))+" ] }"), mapper.getDeserializationContext(), pojo, "key");
        assertArrayEquals(expectedValue, (String[])map.get("key"));
    }

    @Test
    public void testSettableAnyPropertyTypeDeserializer() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, MyType.class);
        BeanProperty property = mapper.findBeanProperty("testSettableAnyProperty", javaType, null);
        AnnotatedMember setter = PropertyName.standard("testSettableAnyProperty").get(javaType);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, mapper.getFactory().constructTypeDeserializer(MyType.class), null);

        Object pojo = mapper.readValue(
                "{ \"testSettableAnyProperty\": { \"key\": 1 } }", javaType);
        Map<String, Object> map = (Map<String, Object>) pojo;

        MyType expectedValue = new MyType();
        settableAnyProperty.deserializeAndSet(mapper.getFactory().createParser("{ \"key\": 1 }"), mapper.getDeserializationContext(), pojo, "key");
        assertEquals(expectedValue.getValue(), map.get("key"));
    }

    @Test
    public void testSettableAnyPropertyKeyDeserializer() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructMapType(HashMap.class, MyType.class, String.class);
        BeanProperty property = mapper.findBeanProperty("testSettableAnyProperty", javaType, null);
        AnnotatedMember setter = PropertyName.standard("testSettableAnyProperty").get(javaType);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, mapper.getFactory().constructKeyDeserializer(MyType.class), null, null);

        Object pojo = mapper.readValue(
                "{ \"testSettableAnyProperty\": { \"key\": {"+MyType.class.getName()+": \"value1\" } } }", javaType);
        Map<Object, Object> map = (Map<Object, Object>) pojo;

        MyType key = new MyType();
        settableAnyProperty.deserializeAndSet(mapper.getFactory().createParser("{ \"key\": {"+key.getClass().getName()+": \"value1\" } }"), mapper.getDeserializationContext(), pojo, key);
        assertEquals(key, map.get(key));
    }

    private static class MyType {
        private String value;

        public MyType() {}

        public MyType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}