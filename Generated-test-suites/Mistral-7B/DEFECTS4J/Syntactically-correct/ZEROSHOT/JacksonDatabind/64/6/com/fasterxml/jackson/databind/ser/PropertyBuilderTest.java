package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.PropertyWriterBuilder;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilder() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig(null);
        BeanDescription beanDesc = new BeanDescription(Object.class, null, null);
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test getClassAnnotations()
        assertEquals(Arrays.asList(), propertyBuilder.getClassAnnotations().getAnnotations());

        // Test buildWriter() with null arguments
        assertNull(propertyBuilder.buildWriter(null, null, null, null, null, null, null, false));

        // Test buildWriter() with valid arguments
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        Object property = new HashMap<>();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", javaType, null, null, null, null);
        JsonSerializer<?> serializer = new JsonStringSerializer();
        TypeSerializer typeSerializer = TypeSerializers.statelessStringSerializer();
        TypeSerializer contentTypeSerializer = null;
        AnnotatedMember am = new AnnotatedClassMember("test", property);
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(new SerializerProvider(), propDef, javaType, serializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping);
        assertEquals("test", beanPropertyWriter.getName());
        assertEquals(javaType, beanPropertyWriter.getType());
        assertNull(beanPropertyWriter.getNullSerializer());
        assertNull(beanPropertyWriter.getUnwrappingNameTransformer());

        // Test findSerializationType() with valid arguments
        Annotated a = new AnnotatedClassMember("test", property);
        JavaType secondaryType = TypeFactory.defaultInstance().constructType(String.class, String.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType type = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(type);

        a = new AnnotatedClassMember("test", property);
        secondaryType = TypeFactory.defaultInstance().constructType(String.class, String.class);
        declaredType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        type = propertyBuilder.findSerializationType(a, true, declaredType);
        assertEquals(secondaryType, type);

        // Test getDefaultBean() with valid arguments
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._defaultBean = new HashMap<>();
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertEquals(HashMap.class, defaultBean.getClass());
        assertNull(propertyBuilder.getDefaultBean());

        // Test getPropertyDefaultValue() with valid arguments
        String name = "test";
        AnnotatedMember member = new AnnotatedField("test", property);
        JavaType type1 = TypeFactory.defaultInstance().constructType(String.class);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type1);
        assertEquals("", defaultValue);

        // Test getDefaultValue() with valid arguments
        JavaType type2 = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        Object defaultValue2 = propertyBuilder.getDefaultValue(type2);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue2);

        // Test _throwWrapped() with valid arguments
        Throwable throwable = new RuntimeException("Test exception");
        Object obj = new Object();
        Object thrown = propertyBuilder._throwWrapped(throwable, "test", obj);
        assertEquals("Failed to get property 'test' of default Object@5e8fc2b7 class [B instance", thrown.getClass().getName());
    }

    static class AnnotatedClassMember implements AnnotatedMember {

        private final String name;
        private final Object value;

        public AnnotatedClassMember(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public JavaType getGenericType() {
            return null;
        }

        @Override
        public JavaType getType() {
            return null;
        }

        @Override
        public JavaType getRawType() {
            return null;
        }

        @Override
        public Object getValue(Object object) {
            return BeanUtil.findValue(value);
        }

        @Override
        public AnnotatedMember setValue(Object value) {
            return this;
        }

        @Override
        public boolean isStatic() {
            return false;
        }

        @Override
        public boolean isSynthetic() {
            return false;
        }

        @Override
        public Class<?> getDeclaringClass() {
            return Object.class;
        }

        @Override
        public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }

        @Override
        public String getNameDescription() {
            return null;
        }

        @Override
        public String getSimpleName() {
            return null;
        }
    }

    static class AnnotatedField extends AnnotatedClassMember {

        public AnnotatedField(String name, Object value) {
            super(name, value);
        }

        @Override
        public JavaType getType() {
            return TypeFactory.defaultInstance().constructType(getValue().getClass());
        }

        @Override
        public Object getValue(Object object) {
            Converter<Object, Object> converter = new Converter.Std(Object.class, getValue().getClass()) {
                @Override
                public Object convert(Object value, Class<?> destType) {
                    return getValue();
                }
            };
            return converter.convertValue(getValue(), getType());
        }

        @Override
        public JavaType getDeclaredType() {
            return TypeFactory.defaultInstance().constructType(getValue().getClass());
        }

        @Override
        public AnnotatedMember setValue(Object value) {
            throw new UnsupportedOperationException();
        }
    }
}