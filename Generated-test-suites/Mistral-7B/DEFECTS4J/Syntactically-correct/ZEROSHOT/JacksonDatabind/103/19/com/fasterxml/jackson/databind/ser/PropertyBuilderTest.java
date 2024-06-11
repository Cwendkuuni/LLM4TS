package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector.FindSerializationType;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import static com.fasterxml.jackson.core.Version.VERSION_2_11_1;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@SuppressWarnings("unchecked")

public class PropertyBuilderTest {

    private ObjectMapper mapper;

    @BeforeClass
    public void init() throws JsonMappingException {
        SimpleModule module = new SimpleModule() {
            @Override
            public void setupModule(SerializationConfig config) throws Exception {
                config.setVersion(VERSION_2_11_1);
                config.setDefaultPropertyInclusion(JsonInclude.Value.standard());
            }
        };

        mapper = new ObjectMapper(module);
    }

    @Test
    public void testPropertyBuilder() throws IOException {
        // Test simple case
        BeanDescription beanDesc = mapper.constructType(SimpleBean.class);
        SerializationConfig config = new SerializationConfig(mapper.getFactory());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test getClassAnnotations()
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertEquals(annotations.size(), 1);
        assertEquals(annotations.iterator().next().getClass(), SimpleBean.class.getAnnotation(JsonInclude.class));

        // Test buildWriter() with null contentTypeSer
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, false);
        assertEquals(beanPropertyWriter, null);

        // Test buildWriter() with non-null contentTypeSer
        TypeSerializer contentTypeSer = mapper.getTypeSerializer(int[].class);
        BeanPropertyWriter beanPropertyWriterWithContentTypeSer = propertyBuilder.buildWriter(prov, null, null, null, null, contentTypeSer, null, false);
        assertNull(beanPropertyWriterWithContentTypeSer.getSerializer());
        assertEquals(beanPropertyWriterWithContentTypeSer.getContentSerializer(), contentTypeSer);

        // Test buildWriter() with serializer
        JsonSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class) {};
        BeanPropertyWriter beanPropertyWriterWithSerializer = propertyBuilder.buildWriter(prov, null, null, intSerializer, null, null, null, false);
        assertEquals(beanPropertyWriterWithSerializer.getSerializer(), intSerializer);

        // Test buildWriter() with typeSerializer
        TypeSerializer intTypeSerializer = mapper.getTypeSerializer(Integer.TYPE);
        BeanPropertyWriter beanPropertyWriterWithTypeSerializer = propertyBuilder.buildWriter(prov, null, null, null, intTypeSerializer, null, null, false);
        assertEquals(beanPropertyWriterWithTypeSerializer.getTypeSerializer(), intTypeSerializer);

        // Test buildWriter() with defaultUseStaticTyping = true
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder.setDefaultUseStaticTyping(true);
        JavaType javaType = TypeFactory.defaultInstance().constructType(Integer[].class);
        BeanPropertyWriter beanPropertyWriterWithDefaultUseStaticTyping = propertyBuilder.buildWriter(prov, null, javaType, null, null, null, null, false);
        assertEquals(beanPropertyWriterWithDefaultUseStaticTyping.getContentType().getRawClass(), Integer[].class);

        // Test buildWriter() with JavaType
        JavaType intJavaType = TypeFactory.defaultInstance().constructType(Integer.class);
        BeanPropertyWriter beanPropertyWriterWithJavaType = propertyBuilder.buildWriter(prov, null, intJavaType, null, null, null, null, false);
        assertEquals(beanPropertyWriterWithJavaType.getSerializableType().getRawClass(), Integer.class);

        // Test buildWriter() with JsonInclude.Value
        JsonInclude.Value jsonIncludeValue = JsonInclude.Value.builder().includeAsProperty("test").build();
        PropertyBuilder propertyBuilderWithJsonIncludeValue = new PropertyBuilder(config, beanDesc);
        propertyBuilderWithJsonIncludeValue._defaultInclusion = jsonIncludeValue;
        BeanPropertyWriter beanPropertyWriterWithJsonIncludeValue = propertyBuilderWithJsonIncludeValue.buildWriter(prov, null, null, null, null, null, null, false);
        assertEquals(beanPropertyWriterWithJsonIncludeValue.getSerializationConfig().getDefaultPropertyInclusion(), jsonIncludeValue);

        // Test findSerializationType()
        FindSerializationType findSerializationType = propertyBuilder._annotationIntrospector.getClassAnnotations().findAnnotation(JsonSerialize.class);
        JavaType javaTypeWithJsonSerialize = propertyBuilder.findSerializationType(null, false, intJavaType);
        assertEquals(javaTypeWithJsonSerialize, intJavaType);

        AnnotatedElement intSerializerAnnotation = SimpleBean.class.getDeclaredMethod("getIntValue").getAnnotation(JsonSerialize.class);
        JavaType javaTypeWithJsonSerializeAnnotation = propertyBuilder.findSerializationType(intSerializerAnnotation, false, intJavaType);
        assertEquals(javaTypeWithJsonSerializeAnnotation, intJavaType.getRawClass());

        // Test findSerializationType() with JsonSerialize.Typing.STATIC
        JsonSerialize intSerializerStatic = new StdSerializer<Integer>(Integer.class) {
            @Override
            public Typing getContentTypeSerializer() {
                return Typing.STATIC;
            }
        };
        JavaType javaTypeWithJsonSerializeStatic = propertyBuilder.findSerializationType(null, true, intJavaType);
        assertEquals(javaTypeWithJsonSerializeStatic, intJavaType.withStaticTyping());

        // Test findSerializationType() with JsonTypeIdResolver
        JsonTypeIdResolver jsonTypeIdResolver = mapper.getTypeIdResolver();
        JavaType jsonTypeWithJsonTypeIdResolver = propertyBuilder.findSerializationType(null, false, intJavaType, jsonTypeIdResolver);
        assertEquals(jsonTypeWithJsonTypeIdResolver.getRawClass(), Object.class);
        assertEquals(jsonTypeWithJsonTypeIdResolver.getContentType().getRawClass(), int.class);

        // Test getDefaultBean()
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        SimpleBean simpleBean = new SimpleBean();
        propertyBuilder._defaultBean = simpleBean;
        Object defaultBeanWithDefaultBean = propertyBuilder.getDefaultBean();
        assertEquals(defaultBeanWithDefaultBean, simpleBean);

        // Test getPropertyDefaultValue()
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testString", null, String.class);
        assertNull(propertyDefaultValue);

        propertyBuilder._defaultBean = new SimpleBean();
        propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testString", new MethodTestPropertyName("testString"), String.class);
        assertEquals(propertyDefaultValue, "defaultString");

        propertyBuilder._defaultBean = null;
        propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testNumber", new MethodTestPropertyName("testNumber"), Number.class);
        assertNull(propertyDefaultValue);

        propertyBuilder._beanDesc = null;
        try {
            propertyBuilder.getPropertyDefaultValue("testNumber", new MethodTestPropertyName("testNumber"), Number.class);
            throw new RuntimeException("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getDefaultValue()
        propertyDefaultValue = propertyBuilder.getDefaultValue(String.class);
        assertEquals(propertyDefaultValue, "");

        propertyDefaultValue = propertyBuilder.getDefaultValue(List.class);
        assertEquals(propertyDefaultValue, Collections.emptyList());

        propertyDefaultValue = propertyBuilder.getDefaultValue(Map.class);
        assertEquals(propertyDefaultValue, Collections.emptyMap());

        propertyDefaultValue = propertyBuilder.getDefaultValue(Object.class);
        assertNull(propertyDefaultValue);

        propertyBuilder._defaultInclusion = JsonInclude.Value.builder()
                .includeAsProperty("testNull")
                .build();
        propertyDefaultValue = propertyBuilder.getDefaultValue(Integer.class);
        assertNull(propertyDefaultValue);

        propertyBuilder._defaultInclusion = JsonInclude.Value.builder()
                .includeAsProperty("testEmptyList")
                .build();
        propertyDefaultValue = propertyBuilder.getDefaultValue(new TypeReference<List<String>>(){}.getType());
        assertEquals(propertyDefaultValue, Collections.emptyList());

        propertyDefaultValue = propertyBuilder.getDefaultValue(new TypeReference<Map<String, Integer>>(){}.getType());
        assertEquals(propertyDefaultValue, Collections.emptyMap());

        propertyBuilder._defaultInclusion = JsonInclude.Value.builder()
                .includeAsProperty("testNullMapValue")
                .build();
        propertyDefaultValue = propertyBuilder.getDefaultValue(new TypeReference<Map<String, Object>>(){}.getType());
        assertNull(propertyDefaultValue);

        // Test getDefaultValue() with custom getter
        propertyBuilder._beanDesc = new BeanDescription(SimpleCustomBean.class);
        propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testNumberCustomGetter", null, Number.class);
        assertEquals(propertyDefaultValue, 42);

        // Test _throwWrapped()
        try {
            propertyBuilder._throwWrapped(new RuntimeException("Test"), "test", null);
            throw new RuntimeException("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}

class SimpleBean {
    @JsonInclude
    private String testString = "defaultString";

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String testStringNonDefault;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testNumber;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> testEmptyList;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Map<String, Object> testNullMapValue = Collections.singletonMap("test", null);

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Integer> testNullMapValueWithNullValue = Collections.singletonMap("test", null);

    private String testNull;

    @JsonSerialize(using = CustomIntSerializer.class)
    private int testNumberCustomGetter;

    public int getIntValue() {
        return testNumberCustomGetter;
    }

    public static class CustomIntSerializer extends StdSerializer<Integer> {
        public CustomIntSerializer() {
            this(null);
        }

        public CustomIntSerializer(Class<Integer> t) {
            super(t);
        }

        @Override
        public int getContentAsInt(SerializerProvider p, BeanPropertyWriter writer) throws IOException {
            return 42;
        }
    }
}

class MethodTestPropertyName {
    private final String name;

    public MethodTestPropertyName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}