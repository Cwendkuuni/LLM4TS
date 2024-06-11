package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class PropertyBuilderTest {

    @SuppressWarnings("unchecked")
    private Object createTestBean() {
        return new TestBean();
    }

    private SerializationConfig createTestConfig() {
        return new SerializationConfig();
    }

    private BeanDescription createTestBeanDescription(Object bean) throws IOException {
        AnnotationIntrospector introspector = new StandardAnnotationIntrospector();
        return introspector.findBeanDescription(Object.class, bean.getClass());
    }

    private PropertyBuilder createPropertyBuilder(Object bean, SerializationConfig config) {
        try {
            BeanDescription beanDescription = createTestBeanDescription(bean);
            return new PropertyBuilder(config, beanDescription);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBuildWriter_Default() throws Exception {
        Object testBean = createTestBean();
        SerializationConfig config = createTestConfig();
        PropertyBuilder propertyBuilder = createPropertyBuilder(testBean, config);

        ObjectWriter objectWriter = new DefaultSerializerProvider().getWriter(propertyBuilder);

        String json = objectWriter.writeValueAsString(testBean);
        assertEquals("{}", json);
    }

    @Test
    public void testBuildWriter_CustomSerializationType() throws Exception {
        Object testBean = createTestBean();
        SerializationConfig config = createTestConfig();

        JavaType javaType = new TypeReference<>() {}.getType();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null) {
            @Override
            protected JavaType findSerializationType(Annotated a, boolean useStaticTyping, JavaType declaredType) {
                return javaType;
            }
        };

        ObjectWriter objectWriter = new DefaultSerializerProvider().getWriter(propertyBuilder);

        String json = objectWriter.writeValueAsString(testBean);
        assertEquals("{}", json);
    }

    @Test
    public void testBuildWriter_NullSuppression() throws Exception {
        Object testBean = createTestBean();
        SerializationConfig config = createTestConfig();

        PropertyBuilder propertyBuilder = createPropertyBuilder(testBean, config);

        ObjectWriter objectWriter = new DefaultSerializerProvider().getWriter(propertyBuilder);

        String json = objectWriter.writeValueAsString(null);
        assertNull(json);

        propertyBuilder = new PropertyBuilder(config, null) {
            @Override
            protected Object getPropertyDefaultValue(String name, AnnotatedMember member, JavaType type) {
                return "default";
            }
        };

        objectWriter = new DefaultSerializerProvider().getWriter(propertyBuilder);

        json = objectWriter.writeValueAsString(null);
        assertEquals("\"default\"", json);

        propertyBuilder = new PropertyBuilder(config, null) {
            @Override
            protected Object getPropertyDefaultValue(String name, AnnotatedMember member, JavaType type) {
                return "default";
            }

            @Override
            protected boolean suppressNulls() {
                return true;
            }
        };

        objectWriter = new DefaultSerializerProvider().getWriter(propertyBuilder);

        json = objectWriter.writeValueAsString(null);
        assertNull(json);
    }

    @Test
    public void testBuildWriter_JsonInclude() throws Exception {
        Object testBean = createTestBean();
        SerializationConfig config = createTestConfig();

        JsonInclude.Value defaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_DEFAULT);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null) {
            @Override
            protected JsonInclude.Value _defaultInclusion() {
                return defaultInclusion;
            }
        };

        ObjectWriter objectWriter = new DefaultSerializerProvider().getWriter(propertyBuilder);

        String json = objectWriter.writeValueAsString(testBean);
        assertNotNull(json);
        assertTrue(json.isEmpty());

        TestBean testBeanWithValue = new TestBean("value");
        json = objectWriter.writeValueAsString(testBeanWithValue);
        assertEquals("\"value\"", json);
    }

    @Test
    public void testBuildWriter_JsonSerialize() throws Exception {
        Object testBean = new TestBeanWithJsonSerialize();
        SerializationConfig config = createTestConfig();

        PropertyBuilder propertyBuilder = createPropertyBuilder(testBean, config);

        ObjectWriter objectWriter = new DefaultSerializerProvider().getWriter(propertyBuilder);

        String json = objectWriter.writeValueAsString(testBean);
        assertEquals("{\"value\":\"serializedValue\"}", json);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        Object testBean = new TestBean();
        SerializationConfig config = createTestConfig();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);

        JsonTypeSerializer typeSerializer = new JsonTypeSerializer(new TypeSerializer.Impl(), null, null);
        JavaType javaType = new TypeReference<>() {}.getType();

        TypeSerializer contentTypeSer = new TypeSerializer.Std(Object.class, new TypeSerializer.NullValueSerializer());
        AnnotatedMember am = new AnnotatedMethod() {
            @Override
            public AnnotatedElement getMember() {
                return null;
            }

            @Override
            public int getMemberIndex() {
                return 0;
            }

            @Override
            public Method getMemberMethod() {
                return null;
            }

            @Override
            public Class<?> getDeclaringClass() {
                return Object.class;
            }
        };

        JavaType serializationType = propertyBuilder.findSerializationType(am, false, javaType);
        assertEquals(null, serializationType);

        propertyBuilder = new PropertyBuilder(config, null) {
            @Override
            protected JavaType findSerializationType(Annotated a, boolean useStaticTyping, JavaType declaredType) {
                return javaType;
            }
        };

        serializationType = propertyBuilder.findSerializationType(am, false, javaType);
        assertEquals(javaType, serializationType);

        JavaType serializationTypeWithContentType = propertyBuilder.findSerializationType(am, false, javaType, typeSerializer, contentTypeSer);
        assertEquals(javaType.withContentTypeHandler(contentTypeSer), serializationTypeWithContentType);
    }

    static class TestBean {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public Object value;
    }

    static class TestBeanWithJsonSerialize {
        @JsonSerialize(using = TestSerializer.class)
        public String value = "serializedValue";
    }

    static class TestSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString("serializedValue");
        }
    }
}