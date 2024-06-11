package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest extends TestBase {

    public void testSettableAnyProperty() throws Exception {
        BeanProperty property = new BeanProperty(Object.class.getDeclaredField("anyProperty"), null, null);
        AnnotatedMember setter = new AnnotatedMethod(Object.class, "setAnyProperty", new Class[]{Object.class, Object.class});

        JavaType type = JsonTypeFactory.instance.constructSimpleType(Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new FromStringDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object testInstance = new Object() {{
            @SuppressWarnings("unchecked")
            Map<Object, Object> anyProperty = new HashMap<>();
        }};

        JsonParser parser = createParser("{\"key\":\"value\"}");
        settableAnyProperty.deserializeAndSet(parser, context, testInstance, "key");

        Object value = settableAnyProperty.deserialize(parser, context);
        assertEquals("value", value);

        Map<Object, Object> map = (Map<Object, Object>) testInstance.getClass().getDeclaredField("anyProperty")
                .get(testInstance);
        assertEquals("value", map.get("key"));
    }

    public void testSettableAnyPropertyWithValueDeserializer() throws Exception {
        BeanProperty property = new BeanProperty(Object.class.getDeclaredField("anyProperty"), null, null);
        AnnotatedMember setter = new AnnotatedMethod(Object.class, "setAnyProperty", new Class[]{Object.class, Object.class});

        JavaType type = JsonTypeFactory.instance.constructSimpleType(Integer.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>(Integer.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object testInstance = new Object() {{
            @SuppressWarnings("unchecked")
            Map<Object, Object> anyProperty = new HashMap<>();
        }};

        JsonParser parser = createParser("{\"key\":42}");
        settableAnyProperty.deserializeAndSet(parser, context, testInstance, "key");

        Object value = settableAnyProperty.deserialize(parser, context);
        assertEquals(42, value);

        Map<Object, Object> map = (Map<Object, Object>) testInstance.getClass().getDeclaredField("anyProperty")
                .get(testInstance);
        assertEquals(42, map.get("key"));
    }

    public void testSettableAnyPropertyWithTypeDeserializer() throws Exception {
        BeanProperty property = new BeanProperty(Object.class.getDeclaredField("anyProperty"), null, null);
        AnnotatedMember setter = new AnnotatedMethod(Object.class, "setAnyProperty", new Class[]{Object.class, Object.class});

        JavaType type = JsonTypeFactory.instance.constructSimpleType(String[].class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new FromStringDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = new ArrayTypeDeserializer(JsonTypeFactory.instance.constructType(String.class), true);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object testInstance = new Object() {{
            @SuppressWarnings("unchecked")
            Map<Object, Object> anyProperty = new HashMap<>();
        }};

        JsonParser parser = createParser("{\"key\":[\"value1\", \"value2\"]}");
        settableAnyProperty.deserializeAndSet(parser, context, testInstance, "key");

        Object value = settableAnyProperty.deserialize(parser, context);
        assertArrayEquals(new String[]{"value1", "value2"}, (Object[]) value);

        Map<Object, Object> map = (Map<Object, Object>) testInstance.getClass().getDeclaredField("anyProperty")
                .get(testInstance);
        assertArrayEquals(new String[]{"value1", "value2"}, (Object[]) map.get("key"));
    }

    public void testSettableAnyPropertyWithKeyDeserializer() throws Exception {
        BeanProperty property = new BeanProperty(Object.class.getDeclaredField("anyProperty"), null, null);
        AnnotatedMember setter = new AnnotatedMethod(Object.class, "setAnyProperty", new Class[]{Object.class, Object.class});

        JavaType type = JsonTypeFactory.instance.constructSimpleType(String.class);
        KeyDeserializer keyDeserializer = new FromStringDeserializer(Integer.class);
        JsonDeserializer<Object> valueDeserializer = new FromStringDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object testInstance = new Object() {{
            @SuppressWarnings("unchecked")
            Map<Object, Object> anyProperty = new HashMap<>();
        }};

        JsonParser parser = createParser("{\"1\":\"value1\", \"2\":\"value2\"}");
        settableAnyProperty.deserializeAndSet(parser, context, testInstance, null);

        Object value = settableAnyProperty.deserialize(parser, context);
        assertEquals(1, value);

        Map<Object, Object> map = (Map<Object, Object>) testInstance.getClass().getDeclaredField("anyProperty")
                .get(testInstance);
        assertEquals("value1", map.get(1));
        assertEquals("value2", map.get(2));
    }

    public void testSettableAnyPropertyWithNullValue() throws Exception {
        BeanProperty property = new BeanProperty(Object.class.getDeclaredField("anyProperty"), null, null);
        AnnotatedMember setter = new AnnotatedMethod(Object.class, "setAnyProperty", new Class[]{Object.class, Object.class});

        JavaType type = JsonTypeFactory.instance.constructSimpleType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = JsonDeserializer.noOpDeserializer();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object testInstance = new Object() {{
            @SuppressWarnings("unchecked")
            Map<Object, Object> anyProperty = new HashMap<>();
        }};

        JsonParser parser = createParser("{}");
        settableAnyProperty.deserializeAndSet(parser, context, testInstance, null);

        Object value = settableAnyProperty.deserialize(parser, context);
        assertNull(value);

        Map<Object, Object> map = (Map<Object, Object>) testInstance.getClass().getDeclaredField("anyProperty")
                .get(testInstance);
        assertNull(map.get(null));
    }

    public void testSettableAnyPropertyWithMissingSetter() throws Exception {
        BeanProperty property = new BeanProperty(Object.class.getDeclaredField("anyProperty"), null, null);
        AnnotatedMember setter = new AnnotatedField(Object.class.getSuperclass(), "anyProperty", new Class[]{Object.class});

        JavaType type = JsonTypeFactory.instance.constructSimpleType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new FromStringDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object testInstance = new Object() {{
            @SuppressWarnings("unchecked")
            Map<Object, Object> anyProperty = new HashMap<>();
        }};

        JsonParser parser = createParser("{\"key\":\"value\"}");

        try {
            settableAnyProperty.deserializeAndSet(parser, context, testInstance, "key");
            fail();
        } catch (IOException e) {
            // expected
        }

        Object value = settableAnyProperty.deserialize(parser, context);
        assertNull(value);

        Map<Object, Object> map = (Map<Object, Object>) testInstance.getClass().getDeclaredField("anyProperty")
                .get(testInstance);
        assertNull(map.get("key"));
    }

    public void testSettableAnyPropertyWithFieldSetter() throws Exception {
        BeanProperty property = new BeanProperty(Object.class.getDeclaredField("anyProperty"), null, null);
        AnnotatedMember setter = new AnnotatedField(Object.class, "anyProperty", new Class[]{Object.class, Object.class});

        JavaType type = JsonTypeFactory.instance.constructSimpleType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new FromStringDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object testInstance = new Object() {{
            @SuppressWarnings("unchecked")
            Map<Object, Object> anyProperty = new HashMap<>();
        }};

        JsonParser parser = createParser("{\"key\":\"value\"}");
        settableAnyProperty.deserializeAndSet(parser, context, testInstance, "key");

        Object value = settableAnyProperty.deserialize(parser, context);
        assertEquals("value", value);

        Map<Object, Object> map = (Map<Object, Object>) testInstance.getClass().getDeclaredField("anyProperty")
                .get(testInstance);
        assertEquals("value", map.get("key"));
    }

    public void testSettableAnyPropertyWithThrowingException() throws Exception {
        BeanProperty property = new BeanProperty(Object.class.getDeclaredField("anyProperty"), null, null);
        AnnotatedMember setter = new AnnotatedMethod(Object.class, "setAnyProperty", new Class[]{Object.class, Object.class});

        JavaType type = JsonTypeFactory.instance.constructSimpleType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new ThrowingDeserializer();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object testInstance = new Object() {{
            @SuppressWarnings("unchecked")
            Map<Object, Object> anyProperty = new HashMap<>();
        }};

        JsonParser parser = createParser("{\"key\":\"value\"}");

        try {
            settableAnyProperty.deserializeAndSet(parser, context, testInstance, "key");
            fail();
        } catch (IOException e) {
            // expected
        }

        Object value = settableAnyProperty.deserialize(parser, context);
        assertNull(value);

        Map<Object, Object> map = (Map<Object, Object>) testInstance.getClass().getDeclaredField("anyProperty")
                .get(testInstance);
        assertNull(map.get("key"));
    }

    public static class ThrowingDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            throw new IOException("Test exception");
        }
    }
}