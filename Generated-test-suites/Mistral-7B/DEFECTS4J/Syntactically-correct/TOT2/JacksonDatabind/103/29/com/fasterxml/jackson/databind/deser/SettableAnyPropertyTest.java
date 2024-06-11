package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyConstructor() throws Exception {
        BeanProperty property = new BeanProperty(new AnnotatedClass(Object.class), "property", null);
        AnnotatedMember setter = new AnnotatedMethod(new AnnotatedClass(Object.class), "set", new AnnotatedParameter[]{});
        JavaType type = new ClassType(Map.class);
        KeyDeserializer keyDeserializer = new StubKeyDeserializer();
        JsonDeserializer<Object> valueDeser = new StubJsonDeserializer<>();
        TypeDeserializer typeDeser = new StubTypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                property, setter, type, keyDeserializer, valueDeser, typeDeser);
        assertNotNull(settableAnyProperty);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "key";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(Object.class), "property", null),
                new AnnotatedMethod(new AnnotatedClass(Object.class), "set", new AnnotatedParameter[]{new AnnotatedParameter("key", String.class)}),
                new ClassType(Map.class), null, new StubJsonDeserializer<>(), null);

        settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);

        assertEquals("value", instance.getClass().getMethod("get", String.class).invoke(instance, propName));
        verify(ctxt, times(1)).findBeanProperty("property");
        verifyNoMoreInteractions(ctxt);
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullInstance() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = null;
        String propName = "key";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(Object.class), "property", null),
                new AnnotatedMethod(new AnnotatedClass(Object.class), "set", new AnnotatedParameter[]{new AnnotatedParameter("key", String.class)}),
                new ClassType(Map.class), null, new StubJsonDeserializer<>(), null);

        settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullSetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "key";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(Object.class), "property", null),
                null, new ClassType(Map.class), null, new StubJsonDeserializer<>(), null);

        try {
            settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);
            fail();
        } catch (IllegalArgumentException e) {
        }

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithInvalidPropertyType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "key";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(Object.class), "property", null),
                new AnnotatedMethod(new AnnotatedClass(Object.class), "set", new AnnotatedParameter[]{new AnnotatedParameter("key", Integer.class)}),
                new ClassType(Map.class), null, new StubJsonDeserializer<>(), null);

        try {
            settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);
            fail();
        } catch (IllegalArgumentException e) {
        }

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullJsonParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "key";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(Object.class), "property", null),
                new AnnotatedMethod(new AnnotatedClass(Object.class), "set", new AnnotatedParameter[]{new AnnotatedParameter("key", String.class)}),
                new ClassType(Map.class), null, new StubJsonDeserializer<>(), null);

        try {
            settableAnyProperty.deserializeAndSet(null, ctxt, instance, propName);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullDeserializationContext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        Object instance = new Object();
        String propName = "key";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(Object.class), "property", null),
                new AnnotatedMethod(new AnnotatedClass(Object.class), "set", new AnnotatedParameter[]{new AnnotatedParameter("key", String.class)}),
                new ClassType(Map.class), null, new StubJsonDeserializer<>(), null);

        try {
            settableAnyProperty.deserializeAndSet(parser, null, instance, propName);
            fail();
        } catch (IllegalArgumentException e) {
        }

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "key";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(Object.class), "property", null),
                new AnnotatedMethod(new AnnotatedClass(Object.class), "set", new AnnotatedParameter[]{new AnnotatedParameter("key", String.class)}),
                new ClassType(Map.class), null, new StubJsonDeserializer<>() {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        throw new IOException("IOException");
                    }
                }, null);

        try {
            settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);
            fail();
        } catch (IOException e) {
        }

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithInvalidPropertyName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "invalid_property_name";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedClass(Object.class), "property", null),
                new AnnotatedMethod(new AnnotatedClass(Object.class), "set", new AnnotatedParameter[]{new AnnotatedParameter("key", String.class)}),
                new ClassType(Map.class), null, new StubJsonDeserializer<>(), null);

        try {
            settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);
            fail();
        } catch (IllegalArgumentException e) {
        }

        parser.close();
    }

    private static class StubJsonDeserializer<T> implements JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class StubTypeDeserializer implements TypeDeserializer {
        @Override
        public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromObjectId(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class StubKeyDeserializer implements KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}