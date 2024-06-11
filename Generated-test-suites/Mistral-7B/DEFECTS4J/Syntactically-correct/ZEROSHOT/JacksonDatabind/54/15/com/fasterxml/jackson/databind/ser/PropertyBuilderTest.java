package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private SerializationConfig config;
    private PropertyBuilder propertyBuilder;

    @BeforeEach
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        config = objectMapper.getSerializationConfig();
        BeanDescription beanDescription = new BeanDescription(TestBean.class, BeanDescription.StandardBuilder.stanдарtBuilder());
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testClassAnnotations() throws IOException {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.size());
        JsonInclude annotation = (JsonInclude) annotations.findAnnotation(JsonInclude.class);
        Assertions.assertEquals(JsonInclude.Include.NON_NULL, annotation.value());
    }

    @Test
    public void testBuildWriter() throws IOException {
        BeanPropertyDefinition propDef = beanDescription.findPropertyDefinition("name");
        SerializerProvider serializerProvider = config.getSerializerProvider();
        TypeSerializer typeSerializer = null;
        Object ser = new IdentitySerializer(TestBean.class);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef,
                new TypeReference<>() {}.getType(), ser, typeSerializer, null, null, false);

        Assertions.assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() throws IOException {
        Annotated annotated = Mockito.mock(Annotated.class);
        JavaType declaredType = new TypeReference<>() {}.getType();

        when(annotated.getType()).thenReturn(declaredType);

        JavaType result = propertyBuilder.findSerializationType(annotated, false, declaredType);

        Assertions.assertEquals(declaredType, result);

        JavaType secondaryType = new TypeReference<List<String>>() {}.getType();
        when(annotated.getType()).thenReturn(secondaryType);

        result = propertyBuilder.findSerializationType(annotated, false, declaredType);

        Assertions.assertEquals(null, result);

        when(annotated.getType()).thenReturn(secondaryType);
        when(annotated.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerialize());

        result = propertyBuilder.findSerializationType(annotated, false, declaredType);

        Assertions.assertEquals(secondaryType, result);
    }

    @Test
    public void testGetDefaultBean() throws IOException {
        Object defaultBean = propertyBuilder.getDefaultBean();

        Assertions.assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws IOException {
        Object name = propertyBuilder.getPropertyDefaultValue("name", null, new TypeReference<TestBean>() {}.getType());

        Assertions.assertNull(name);

        TestBean testBean = new TestBean();
        testBean.setName("Test");
        Object nameDefault = propertyBuilder.getPropertyDefaultValue("name", null, new TypeReference<TestBean>() {}.getType());
        testBean.setName(null);

        Assertions.assertNull(nameDefault);

        TestBean testBean1 = new TestBean();
        testBean1.setName("Test");
        Object nameDefault1 = propertyBuilder.getPropertyDefaultValue("age", null, new TypeReference<TestBean>() {}.getType());

        Assertions.assertNull(nameDefault1);
    }

    @Test
    public void testGetDefaultValue() throws IOException {
        Object result = propertyBuilder.getDefaultValue(new TypeReference<>() {}.getType());

        Assertions.assertNull(result);

        result = propertyBuilder.getDefaultValue(String.class);

        Assertions.assertEquals("", result);

        result = propertyBuilder.getDefaultValue(new TypeReference<List<String>>() {}.getType());

        Assertions.assertEquals(JsonInclude.Include.NON_EMPTY, result);

        result = propertyBuilder.getDefaultValue(Map.class);

        Assertions.assertEquals(JsonInclude.Include.NON_EMPTY, result);

        result = propertyBuilder.getDefaultValue(TestBean[].class);

        Assertions.assertEquals(JsonInclude.Include.NON_EMPTY, result);
    }

    @Test
    public void test_throwWrapped() throws IOException {
        Throwable exception = new IOException("Test IOException");

        Object result = propertyBuilder._throwWrapped(exception, "test", new Object());

        Assertions.assertEquals("Failed to get property 'test' of default Object@15db9742 class [com.fasterxml.jackson.databind.ser.PropertyBuilderTest$TestBean]", result.toString());
    }

    @Test
    public void testFindSerializationTypeWithAnnotation() throws IOException {
        Annotated annotated = Mockito.mock(Annotated.class);
        JavaType declaredType = new TypeReference<TestBean>() {}.getType();

        when(annotated.getType()).thenReturn(declaredType);

        when(annotated.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerialize() {{
            @JsonSerialize.Typing(value = JsonSerialize.Typing.STATIC)
            String serializesAs = "Test";
        }});

        JavaType result = propertyBuilder.findSerializationType(annotated, false, declaredType);

        Assertions.assertEquals(new TypeReference<>("Test") {}.getType(), result);
    }

    private static class TestBean {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}

class IdentitySerializer extends JsonSerializer<Object> {
    private Class<Object> clazz;

    public IdentitySerializer(Class<Object> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void serialize(Object o, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(o.toString());
    }

    @Override
    public Class<Object> handledType() {
        return clazz;
    }
}