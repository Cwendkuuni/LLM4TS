package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class MethodPropertyTest {

    private Object targetObject;
    private MethodProperty property;
    private Method setter;
    private Object expectedValue;

    @Before
    public void setup() throws Exception {
        targetObject = new Object();
        setter = ClassUtil.findSetter(targetObject.getClass(), "testProperty");
        property = new MethodProperty(new BeanPropertyDefinition(new PropertyName("testProperty"), null, null),
                ObjectType.StdClass(targetObject.getClass()), null, Annotations.empty(),
                new AnnotatedMethod(setter));
    }

    @Test
    public void testNormalDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"testProperty\": 123}");

        property.deserializeAndSet(parser, new DeserializationContext(), targetObject);

        assertEquals(123, targetObject.getClass().getMethod("getTestProperty").invoke(targetObject));

        parser.close();
    }

    @Test
    public void testNullDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"testProperty\": null}");

        if (property._skipNulls) {
            assertNull(targetObject.getClass().getMethod("getTestProperty").invoke(targetObject));
        } else {
            property.deserializeAndSet(parser, new DeserializationContext(), targetObject);
            assertNull(targetObject.getClass().getMethod("getTestProperty").invoke(targetObject));
        }

        parser.close();
    }

    @Test
    public void testValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerValuableConverter(Integer.class, String.class, new StringToInt());

        property = new MethodProperty(property, mapper.getValueDeserializer(String.class));

        JsonParser parser = mapper.getFactory().createParser("{\"testProperty\": \"456\"}");

        property.deserializeAndSet(parser, new DeserializationContext(), targetObject);

        assertEquals(456, targetObject.getClass().getMethod("getTestProperty").invoke(targetObject));

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetterNotFound() throws Exception {
        property = new MethodProperty(new BeanPropertyDefinition(new PropertyName("testProperty"), null, null),
                ObjectType.StdClass(targetObject.getClass()), null, Annotations.empty(), null);

        property.deserializeAndSet(mockParser(), new DeserializationContext(), targetObject);
    }

    @Test(expected = InvocationTargetException.class)
    public void testExceptionInSetter() throws Exception {
        Method method = ClassUtil.findSetter(targetObject.getClass(), "testProperty");
        method.setAccessible(true);

        property = new MethodProperty(new BeanPropertyDefinition(new PropertyName("testProperty"), null, null),
                ObjectType.StdClass(targetObject.getClass()), null, Annotations.empty(),
                new AnnotatedMethod(method));

        JsonParser parser = mockParser();

        property.deserializeAndSet(parser, new DeserializationContext(), targetObject);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object newValue = new Object();

        Object result = property.setAndReturn(targetObject, newValue);

        assertEquals(newValue, result);
        assertEquals(newValue, targetObject.getClass().getMethod("getTestProperty").invoke(targetObject));
    }

    @Test
    public void testSet() throws Exception {
        property.set(targetObject, expectedValue);
        assertEquals(expectedValue, targetObject.getClass().getMethod("getTestProperty").invoke(targetObject));
    }

    private JsonParser mockParser() {
        return mock(JsonParser.class);
    }

    private class StringToInt extends StdConverter<String, Integer> {
        @Override
        public Integer convert(String text) {
            return Integer.parseInt(text);
        }
    }
}