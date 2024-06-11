package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test for {@link MethodProperty} class
 */
public class MethodPropertyTest {

    private static final Object TEST_OBJECT = new Object() {
        private int _field;
        private String _methodProperty;

        @JsonProperty(value = "field")
        public int getField() {
            return _field;
        }

        @JsonProperty(value = "methodProperty")
        public void setMethodProperty(String value) {
            _methodProperty = value;
        }
    };

    private ObjectMapper _objectMapper;
    private MethodProperty _methodProperty;

    @Before
    public void setUp() throws Exception {
        _objectMapper = new ObjectMapper();
        MethodProperty.MethodPropertyBuilder builder = MethodProperty.StdBuilder
                .stdBuilder(new BeanPropertyDefinition(null, "_methodProperty",
                        IntType.getInstance(), null), IntType.getInstance(),
                        null, new Annotation[0],
                        findSetterMethod(MethodPropertyTest.class.getDeclaredMethods(),
                                "setMethodProperty", int.class));
        _methodProperty = builder.build();
    }

    private Method findSetterMethod(Method[] methods, String methodName,
            Class<?>... parameterTypes) {
        for (Method method : methods) {
            if (method.getName().equals(methodName)
                    && ClassUtil.matchTypes(method.getParameterTypes(),
                            parameterTypes)) {
                return method;
            }
        }
        return null;
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String json = "{\"methodProperty\":\"test\"}";
        JsonParser parser = _objectMapper.getFactory()
                .createParser(new ByteArrayInputStream(json.getBytes()));
        DeserializationContext ctxt = _objectMapper.getDeserializationContext();

        _methodProperty.deserializeAndSet(parser, ctxt, TEST_OBJECT);
        assertEquals("test", TEST_OBJECT._methodProperty);
        parser.close();
    }

    @Test
    public void testDeserializeAndSetNull() throws Exception {
        String json = "{\"methodProperty\":null}";
        JsonParser parser = _objectMapper.getFactory()
                .createParser(new ByteArrayInputStream(json.getBytes()));
        DeserializationContext ctxt = _objectMapper.getDeserializationContext();

        _methodProperty.deserializeAndSet(parser, ctxt, TEST_OBJECT);
        assertNull(TEST_OBJECT._methodProperty);
        parser.close();
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        String json = "{\"methodProperty\":\"test\"}";
        JsonParser parser = _objectMapper.getFactory()
                .createParser(new ByteArrayInputStream(json.getBytes()));
        Object result = _methodProperty.deserializeSetAndReturn(parser,
                _objectMapper.getDeserializationContext(), TEST_OBJECT);
        assertEquals(TEST_OBJECT, result);
        assertEquals("test", TEST_OBJECT._methodProperty);
        parser.close();
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws Exception {
        String json = "{\"methodProperty\":null}";
        JsonParser parser = _objectMapper.getFactory()
                .createParser(new ByteArrayInputStream(json.getBytes()));
        Object result = _methodProperty.deserializeSetAndReturn(parser,
                _objectMapper.getDeserializationContext(), TEST_OBJECT);
        assertNull(TEST_OBJECT._methodProperty);
        assertEquals(TEST_OBJECT, result);
        parser.close();
    }

    @Test
    public void testSet() throws Exception {
        _methodProperty.set(TEST_OBJECT, "test");
        assertEquals("test", TEST_OBJECT._methodProperty);
    }

    @Test
    public void testSetNull() throws Exception {
        _methodProperty.set(TEST_OBJECT, null);
        assertNull(TEST_OBJECT._methodProperty);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        _methodProperty.setAndReturn(TEST_OBJECT, "test");
        assertEquals(TEST_OBJECT, _methodProperty.getBean());
        assertEquals("test", TEST_OBJECT._methodProperty);
    }

    @Test
    public void testSetAndReturnNull() throws Exception {
        _methodProperty.setAndReturn(TEST_OBJECT, null);
        assertEquals(TEST_OBJECT, _methodProperty.getBean());
        assertNull(TEST_OBJECT._methodProperty);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        String json = "{\"methodProperty\":42}";
        JsonParser parser = _objectMapper.getFactory()
                .createParser(new ByteArrayInputStream(json.getBytes()));

        Object result = _methodProperty.deserializeWithType(parser,
                _objectMapper.getDeserializationContext(),
                IntType.getInstance());
        assertEquals(TEST_OBJECT, result);
        assertEquals(42, TEST_OBJECT._field);
        parser.close();
    }

    @Test
    public void testDeserializeWithTypeNull() throws Exception {
        String json = "{\"methodProperty\":null}";
        JsonParser parser = _objectMapper.getFactory()
                .createParser(new ByteArrayInputStream(json.getBytes()));

        Object result = _methodProperty.deserializeWithType(parser,
                _objectMapper.getDeserializationContext(),
                IntType.getInstance());
        assertNull(TEST_OBJECT._methodProperty);
        assertEquals(TEST_OBJECT, result);
        parser.close();
    }

    @Test
    public void testGetAnnotation() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, IntType.getInstance(), null,
                findSetterMethod(MethodPropertyTest.class.getDeclaredMethods(),
                        "setMethodProperty", int.class), null, null, null);
        JsonProperty annotation = methodProperty.getAnnotation(JsonProperty.class);
        assertEquals("methodProperty", annotation.value());
    }

    @Test
    public void testGetAnnotationNull() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, IntType.getInstance(), null,
                null, null, null, null, null);
        JsonProperty annotation = methodProperty.getAnnotation(JsonProperty.class);
        assertNull(annotation);
    }

    @Test
    public void testFixAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS, false);
        MethodProperty methodProperty = new MethodProperty(
                new BeanPropertyDefinition(null, "_methodProperty", IntType.getInstance(), null),
                IntType.getInstance(), null, null,
                findSetterMethod(MethodPropertyTest.class.getDeclaredMethods(),
                        "setMethodProperty", int.class), null, null, null);
        methodProperty.fixAccess(mapper.getDeserializationConfig());
        assertEquals(Modifier.PUBLIC, methodProperty.getBean().getClass().getMethod("setMethodProperty", int.class).getModifiers());
    }

    @Test
    public void testFixAccessPublic() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MethodProperty methodProperty = new MethodProperty(
                new BeanPropertyDefinition(null, "_methodProperty", IntType.getInstance(), null),
                IntType.getInstance(), null, null,
                findSetterMethod(MethodPropertyTest.class.getDeclaredMethods(),
                        "setMethodProperty", int.class), null, null, null);
        methodProperty.fixAccess(mapper.getDeserializationConfig());
        assertEquals(Modifier.PUBLIC, methodProperty.getBean().getClass().getMethod("setMethodProperty", int.class).getModifiers());
    }
}