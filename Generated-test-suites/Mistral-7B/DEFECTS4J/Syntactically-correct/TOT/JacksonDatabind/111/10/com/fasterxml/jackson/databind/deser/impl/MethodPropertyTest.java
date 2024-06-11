package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @After
    public void teardown() throws Exception {
        objectMapper = null;
    }

    @Test
    public void testMethodPropertyNormalUsage() throws JsonMappingException, JsonProcessingException {
        // Given
        class TestClass {
            @JsonProperty("name")
            private String _name;

            public void setName(String name) {
                _name = name;
            }

            public String getName() {
                return _name;
            }
        }

        BeanPropertyDefinition propDef = AnnotatedClass.findProperties(TestClass.class, objectMapper)
                .get(0);

        JavaType type = objectMapper.getTypeFactory().constructType(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.findAnnotations(TestClass.class, objectMapper);
        AnnotatedMethod method = TestClass.class.getMethod("setName", String.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // When
        String json = "{\"name\": \"John Doe\"}";
        Object instance = new TestClass();
        Object result = methodProperty.deserializeAndSet(objectMapper.readValue(json, JsonParser.class), null, instance);

        // Then
        assertEquals("John Doe", instance.getName());
        assertNull(result);
    }

    @Test
    public void testMethodPropertyNullValue() throws JsonMappingException, JsonProcessingException {
        // Given
        class TestClass {
            @JsonProperty("name")
            private String _name;

            public void setName(String name) {
                _name = name;
            }

            public String getName() {
                return _name;
            }
        }

        BeanPropertyDefinition propDef = AnnotatedClass.findProperties(TestClass.class, objectMapper)
                .get(0);

        JavaType type = objectMapper.getTypeFactory().constructType(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.findAnnotations(TestClass.class, objectMapper);
        AnnotatedMethod method = TestClass.class.getMethod("setName", String.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // When
        String json = "{\"name\": null}";
        Object instance = new TestClass();
        Object result = methodProperty.deserializeAndSet(objectMapper.readValue(json, JsonParser.class), null, instance);

        // Then
        assertNull(instance.getName());
        assertNull(result);
    }

    @Test
    public void testMethodPropertyNullValueProvider() throws JsonMappingException, JsonProcessingException {
        // Given
        class TestClass {
            @JsonProperty("name")
            private String _name;

            public void setName(String name) {
                _name = name;
            }

            public String getName() {
                return _name;
            }
        }

        BeanPropertyDefinition propDef = AnnotatedClass.findProperties(TestClass.class, objectMapper)
                .get(0);

        JavaType type = objectMapper.getTypeFactory().constructType(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.findAnnotations(TestClass.class, objectMapper);
        AnnotatedMethod method = TestClass.class.getMethod("setName", String.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // When
        ObjectMapper objectMapperWithNullProvider = new ObjectMapper();
        ObjectMapper.NullValueProvider nullValueProvider = objectMapperWithNullProvider.getConfig().getNullValueProvider();
        methodProperty = new MethodProperty(methodProperty, nullValueProvider);

        String json = "{\"name\": null}";
        Object instance = new TestClass();
        Object result = methodProperty.deserializeAndSet(objectMapperWithNullProvider.readValue(json, JsonParser.class), null, instance);

        // Then
        assertNull(instance.getName());
        assertNotNull(result);
        assertEquals(instance, result);
    }

    @Test
    public void testMethodPropertyEmptyString() throws JsonMappingException, JsonProcessingException {
        // Given
        class TestClass {
            @JsonProperty("name")
            private String _name;

            public void setName(String name) {
                _name = name;
            }

            public String getName() {
                return _name;
            }
        }

        BeanPropertyDefinition propDef = AnnotatedClass.findProperties(TestClass.class, objectMapper)
                .get(0);

        JavaType type = objectMapper.getTypeFactory().constructType(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.findAnnotations(TestClass.class, objectMapper);
        AnnotatedMethod method = TestClass.class.getMethod("setName", String.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // When
        String json = "{\"name\": \"\"}";
        Object instance = new TestClass();
        Object result = methodProperty.deserializeAndSet(objectMapper.readValue(json, JsonParser.class), null, instance);

        // Then
        assertEquals("", instance.getName());
        assertNull(result);
    }

    @Test
    public void testMethodPropertyNotSettable() throws JsonMappingException, JsonProcessingException {
        // Given
        class TestClass {
            @JsonProperty("name")
            private final String _name;

            public TestClass(String name) {
                _name = name;
            }

            public String getName() {
                return _name;
            }
        }

        BeanPropertyDefinition propDef = AnnotatedClass.findProperties(TestClass.class, objectMapper)
                .get(0);

        JavaType type = objectMapper.getTypeFactory().constructType(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.findAnnotations(TestClass.class, objectMapper);
        AnnotatedMethod method = TestClass.class.getMethod("setName", String.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // When
        String json = "{\"name\": \"John Doe\"}";
        Object instance = new TestClass("Initial name");
        Object result = methodProperty.deserializeAndSet(objectMapper.readValue(json, JsonParser.class), null, instance);

        // Then
        assertEquals("Initial name", instance.getName());
        assertNull(result);
    }

    @Test(expected = IllegalAccessException.class)
    public void testMethodPropertyIllegalAccess() throws JsonMappingException, JsonProcessingException {
        // Given
        class TestClass {
            private String _name;

            public String getName() {
                return _name;
            }

            public void setName(String name) {
                _name = name;
            }
        }

        BeanPropertyDefinition propDef = AnnotatedClass.findProperties(TestClass.class, objectMapper)
                .get(0);

        JavaType type = objectMapper.getTypeFactory().constructType(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.findAnnotations(TestClass.class, objectMapper);
        AnnotatedMethod method = TestClass.class.getMethod("setName", String.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // When
        String json = "{\"name\": \"John Doe\"}";
        Object instance = new TestClass();
        methodProperty.deserializeAndSet(objectMapper.readValue(json, JsonParser.class), null, instance);

        // Then
        fail("IllegalAccessException should have been thrown");
    }

    @Test(expected = InvocationTargetException.class)
    public void testMethodPropertyRuntimeException() throws JsonMappingException, JsonProcessingException {
        // Given
        class TestClass {
            private String _name;

            public String getName() {
                return _name;
            }

            public void setName(String name) throws RuntimeException {
                _name = name;
                if ("Exception".equals(name)) {
                    throw new RuntimeException();
                }
            }
        }

        BeanPropertyDefinition propDef = AnnotatedClass.findProperties(TestClass.class, objectMapper)
                .get(0);

        JavaType type = objectMapper.getTypeFactory().constructType(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.findAnnotations(TestClass.class, objectMapper);
        AnnotatedMethod method = TestClass.class.getMethod("setName", String.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // When
        String json = "{\"name\": \"Exception\"}";
        Object instance = new TestClass();
        methodProperty.deserializeAndSet(objectMapper.readValue(json, JsonParser.class), null, instance);

        // Then
        fail("RuntimeException should have been thrown");
    }
}