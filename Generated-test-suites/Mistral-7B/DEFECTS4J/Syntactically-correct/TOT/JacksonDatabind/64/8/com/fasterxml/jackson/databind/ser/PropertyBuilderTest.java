package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private AnnotationIntrospector annotationIntrospector;
    private ObjectMapper objectMapper;

    @BeforeMethod
    public void setUp() throws Exception {
        config = new SerializationConfig();
        config.setAnnotationIntrospector(new AnnotationIntrospector());
        ObjectMapper objectMapper = new ObjectMapper(config);
        this.annotationIntrospector = objectMapper.getSerializationConfig().getAnnotationIntrospector();
        BeanDescription beanDescription = objectMapper.findBeanDescription(DummyClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testGetClassAnnotations() throws JsonMappingException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> expectedAnnotations = new HashMap<>();
        expectedAnnotations.put("@JsonInclude", JsonInclude.Value.builder().addInclude(JsonInclude.Include.NON_NULL).build());
        Map<String, Object> actualAnnotations = propertyBuilder.getClassAnnotations();
        Assert.assertEquals(actualAnnotations, expectedAnnotations);
    }

    @Test
    public void testBuildWriter_Empty() throws JsonMappingException, IOException {
        SerializerProvider serializerProvider = new SerializerProvider() {
            @Override
            public SerializerProvider getAllSerializerProviders() {
                return this;
            }

            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer) {
                return null;
            }

            @Override
            public TypeSerializer findContentTypeSerializer(JavaType type, TypeSerializer serializer) {
                return null;
            }

            @Override
            public JsonSerializer<?> findSerializerByType(Class<?> type) {
                return null;
            }

            @Override
            public JsonSerializer<?> findSerializer(PropertyName propertyName, JsonSerializer<?> serializer) {
                return null;
            }

            @Override
            public JsonSerializer<?> findSerializer(Method method, JsonSerializer<?> serializer) {
                return null;
            }

            @Override
            public JsonSerializer<?> findSerializer(AnnotatedMember member, JsonSerializer<?> serializer) {
                return null;
            }

            @Override
            public JsonSerializer<?> findNullSerializer(AnnotatedMember member, JsonSerializer<?> serializer) {
                return null;
            }

            @Override
            public Object findValueHandler(Class<?> type) {
                return null;
            }

            @Override
            public Object findValueHandler(Method method) {
                return null;
            }

            @Override
            public Object findValueHandler(AnnotatedMember member) {
                return null;
            }
        };

        BeanPropertyWriter writer = propertyBuilder.buildWriter(serializerProvider, null, JavaType.valueOf(String.class), null, null, null, null, false);
        Assert.assertNull(writer);
    }

    @Test
    public void testBuildWriter_WithoutAnnotation() throws JsonMappingException, IOException {
        SerializerProvider serializerProvider = new SerializerProvider() {
            // ...
        };
        JavaType javaType = JavaType.valueOf(String.class);
        JsonSerializer<?> jsonSerializer = new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(value);
            }
        };
        TypeSerializer typeSerializer = new TypeSerializer() {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString("String");
            }
        };
        TypeSerializer contentTypeSerializer = new TypeSerializer() {
            // ...
        };
        AnnotatedMember annotatedMember = new AnnotatedMethod();
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(serializerProvider, null, javaType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, defaultUseStaticTyping);
        Assert.assertNotNull(writer);
    }

    @Test
    public void testBuildWriter_WithAnnotation() throws JsonMappingException, IOException {
        SerializerProvider serializerProvider = new SerializerProvider() {
            // ...
        };
        JavaType javaType = JavaType.valueOf(String.class);
        JsonSerializer<String> jsonSerializer = new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(value);
            }
        };
        TypeSerializer typeSerializer = new TypeSerializer() {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString("String");
            }
        };
        TypeSerializer contentTypeSerializer = new TypeSerializer() {
            // ...
        };
        AnnotatedMember annotatedMember = new AnnotatedField();
        boolean defaultUseStaticTyping = false;
        JsonInclude.Value jsonInclude = JsonInclude.Value.builder().addInclude(JsonInclude.Include.NON_EMPTY).addInclude(JsonInclude.Include.NON_DEFAULT).build();
        AnnotationIntrospector.BeanProperty annotation = new AnnotationIntrospector.BeanProperty() {{
            setAnnotation(new JsonInclude(jsonInclude));
        }};

        BeanPropertyWriter writer = propertyBuilder.buildWriter(serializerProvider, null, javaType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, defaultUseStaticTyping);
        Assert.assertNotNull(writer);

        String testValue = "test";
        writer.writeValue(testValue, new StringWriter(new StringBuffer()), null);
        String result = writer.asString();
        Assert.assertEquals(testValue, result);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        AnnotatedMember annotatedMember = new AnnotatedField();
        boolean useStaticTyping = true;
        JavaType javaType = JavaType.valueOf(String[].class);

        JavaType actualType = propertyBuilder.findSerializationType(annotatedMember, useStaticTyping, javaType);
        JavaType expectedType = JavaType.arrayOf(JavaType.of(JavaType.valueOf(String.class)));
        Assert.assertEquals(actualType, expectedType);
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException, IllegalAccessException, InvocationTargetException {
        AnnotatedMember annotatedMember = new AnnotatedField();
        JavaType javaType = JavaType.valueOf(String.class);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("testField", annotatedMember, javaType);
        Assert.assertNull(defaultValue);

        Object defaultValue2 = propertyBuilder.getPropertyDefaultValue("testField", annotatedMember, javaType);
        Assert.assertNull(defaultValue2);
    }

    @Test
    public void testGetPropertyDefaultValue_NonDefaultAnnotation() throws JsonMappingException, IllegalAccessException, InvocationTargetException {
        AnnotatedMember annotatedMember = new AnnotatedField();
        JavaType javaType = JavaType.valueOf(DummyClass.class);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("testField", annotatedMember, javaType);
        Object expectedDefaultValue = new DummyClass();

        Assert.assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_Null() throws JsonMappingException, IllegalAccessException, InvocationTargetException {
        AnnotatedMember annotatedMember = new AnnotatedField();
        JavaType javaType = JavaType.valueOf(String.class);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("testField", annotatedMember, javaType);
        Assert.assertNull(defaultValue);

        Object defaultValue2 = propertyBuilder.getPropertyDefaultValue("testField", annotatedMember, javaType);
        Assert.assertNull(defaultValue2);
    }

    @Test
    public void testGetPropertyDefaultValue_NonDefaultBean() throws JsonMappingException, IllegalAccessException, InvocationTargetException {
        AnnotatedMember annotatedMember = new AnnotatedField();
        JavaType javaType = JavaType.valueOf(DummyClass.class);

        Object defaultBean = new DummyClass();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("testField", annotatedMember, javaType);
        Object expectedDefaultValue = null;

        Assert.assertEquals(expectedDefaultValue, defaultValue);
    }

    private static class DummyClass {
        private String testField;

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public String getTestField() {
            return testField;
        }

        public void setTestField(String testField) {
            this.testField = testField;
        }
    }

    private static class AnnotatedField extends AnnotatedMember {
        AnnotatedField() {
            setName("testField");
            setValue("testValue");
        }

        @Override
        public Field getMember() {
            try {
                Field field = DummyClass.class.getDeclaredField("testField");
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class AnnotatedMethod extends AnnotatedMember {
        AnnotatedMethod() {
            setName("testMethod");
            setValue(new MethodTest());
        }

        @Override
        public Method getMember() {
            return new MethodTest();
        }
    }

    private static class MethodTest {
        public String testMethod() {
            return "test";
        }
    }

    private static class StringWriter extends StringBufferWriter {
        private static final long serialVersionUID = 1L;

        public StringWriter(StringBuffer buffer) {
            super(buffer);
        }

        @Override
        public String toString() {
            return getBuffer().toString();
        }
    }
}