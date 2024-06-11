package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class ValueInstantiatorTest {

    private ObjectMapper objectMapper;
    private ValueInstantiator valueInstantiator;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        valueInstantiator = new ValueInstantiator.Base(String.class);
    }

    @Test
    public void testGetValueClass() throws Exception {
        Assert.assertEquals(String.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() throws Exception {
        Assert.assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() throws Exception {
        Assert.assertTrue(valueInstantiator.canInstantiate());
    }

    @Test
    public void testGetDelegateType() throws Exception {
        Assert.assertNull(valueInstantiator.getDelegateType(null));
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        DeserializationContext ctxt = new DeserializationContext(objectMapper, config);
        String value = "default test";

        Object result = valueInstantiator.createUsingDefault(ctxt);
        Assert.assertEquals("default test", result.toString());
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        DeserializationContext ctxt = new DeserializationContext(objectMapper, config);
        ObjectNode node = ObjectMapper.createObjectNode();
        node.put("name", "Test Name");

        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        properties[0] = new SettableBeanProperty.Std("name", String.class);
        Object result = valueInstantiator.createFromObjectWith(ctxt, properties, null);
        Assert.assertEquals("Test Name", result.toString());
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        DeserializationContext ctxt = new DeserializationContext(objectMapper, config);
        Object delegate = new Object();

        Assert.assertNull(valueInstantiator.createUsingDelegate(ctxt, delegate));
    }

    @Test(expected = JsonProcessingException.class)
    public void testCreateFromInt() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        DeserializationContext ctxt = new DeserializationContext(objectMapper, config);
        int value = 123;

        valueInstantiator.createFromInt(ctxt, value);
    }

    @Test
    public void testCanCreateFromInt() throws Exception {
        Assert.assertFalse(valueInstantiator.canCreateFromInt());
    }

    @Test(expected = JsonProcessingException.class)
    public void testCreateFromLong() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        DeserializationContext ctxt = new DeserializationContext(objectMapper, config);
        long value = 123L;

        valueInstantiator.createFromLong(ctxt, value);
    }

    @Test
    public void testCanCreateFromLong() throws Exception {
        Assert.assertFalse(valueInstantiator.canCreateFromLong());
    }

    @Test(expected = JsonProcessingException.class)
    public void testCreateFromDouble() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        DeserializationContext ctxt = new DeserializationContext(objectMapper, config);
        double value = 123.45;

        valueInstantiator.createFromDouble(ctxt, value);
    }

    @Test
    public void testCanCreateFromDouble() throws Exception {
        Assert.assertFalse(valueInstantiator.canCreateFromDouble());
    }

    @Test(expected = JsonProcessingException.class)
    public void testCreateFromBoolean() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        DeserializationContext ctxt = new DeserializationContext(objectMapper, config);
        boolean value = true;

        valueInstantiator.createFromBoolean(ctxt, value);
    }

    @Test
    public void testCanCreateFromBoolean() throws Exception {
        Assert.assertFalse(valueInstantiator.canCreateFromBoolean());
    }

    @Test(expected = JsonProcessingException.class)
    public void testCreateFromString() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        DeserializationContext ctxt = new DeserializationContext(objectMapper, config);
        String value = "Test";

        valueInstantiator.createFromString(ctxt, value);
    }

    @Test
    public void testCanCreateFromString() throws Exception {
        Assert.assertFalse(valueInstantiator.canCreateFromString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testCreateUsingArrayDelegate() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        DeserializationContext ctxt = new DeserializationContext(objectMapper, config);
        Object delegate = new Object[1];

        valueInstantiator.createUsingArrayDelegate(ctxt, delegate);
    }

    @Test
    public void testCanCreateUsingArrayDelegate() throws Exception {
        Assert.assertFalse(valueInstantiator.canCreateUsingArrayDelegate());
    }

    @Test
    public void testGetFromObjectArguments() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        DeserializationContext ctxt = new DeserializationContext(objectMapper, config);
        ObjectNode node = ObjectMapper.createObjectNode();
        node.put("name", "Test Name");
        node.put("age", 25);

        SettableBeanProperty[] properties = valueInstantiator.getFromObjectArguments(config);
        Assert.assertNotNull(properties);
        Assert.assertEquals(2, properties.length);
        Assert.assertEquals("name", properties[0].getName());
        Assert.assertEquals(String.class, properties[0].getType());
        Assert.assertEquals("age", properties[1].getName());
        Assert.assertEquals(Integer.class, properties[1].getType());
    }

    @Test
    public void testGetDefaultCreator() throws Exception {
        Class<?> aClass = String[].class;
        ValueInstantiator valueInstantiatorArray = new ValueInstantiator.Base(aClass);
        AnnotatedWithParams defaultCreator = valueInstantiatorArray.getDefaultCreator();

        Assert.assertNotNull(defaultCreator);
        Method method = defaultCreator.getAnnotatedMember().getMember();
        Assert.assertNotNull(method);
        Assert.assertEquals(String[].class, method.getParameterTypes()[0].getClass());
    }
}