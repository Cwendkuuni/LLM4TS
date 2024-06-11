package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyBuilderTest {
    private static Object NO_DEFAULT_MARKER = Boolean.FALSE;
    private PropertyBuilder propertyBuilder;
    private Object defaultBean;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDescription = new BeanDescription(MyPOJO.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
        defaultBean = propertyBuilder.getDefaultBean();
    }

    @Test
    public void testBuildWriter_SerializableIntegerProperty() throws IOException {
        SerializerProvider prov = new StandardSerializerProvider();
        BeanPropertyDefinition propDef = propertyBuilder.findPropertyDefinition("intProperty");
        JavaType declaredType = propertyBuilder.getTypeFactory().constructType(int.class);
        JsonSerializer<Integer> ser = new JsonSerializer<Integer>() {};
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedMember() {
            @Override
            public AnnotatedElement getMember() {
                return null;
            }

            @Override
            public String getName() {
                return "intProperty";
            }
        };

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        // Test with default value
        int defaultIntValue = 42;
        Assert.assertEquals("Expected marker for default int value", BeanPropertyWriter.MARKER_FOR_DEFAULT_VALUE, bpw.getValueHandler().getSerializerProvider().getValueHandler(int.class).getValue(defaultIntValue));

        // Test with null value
        int intValue = null;
        Assert.assertNull("Expected null for null int value", bpw.getValueHandler().getSerializerProvider().getValueHandler(int.class).getValue(intValue));

        // Test with non-default value
        int nonDefaultIntValue = 17;
        Assert.assertEquals("Expected non-default int value", nonDefaultIntValue, bpw.getValueHandler().getSerializerProvider().getValueHandler(int.class).getValue(nonDefaultIntValue));

        // Test with annotation that forces type to use declared type
        propDef = propertyBuilder.findPropertyDefinition("intPropertyWithAnnotation");
        declaredType = propertyBuilder.getTypeFactory().constructType(MySerializableInt.class);
        Assert.assertEquals("Expected declared type for int property with annotation", MySerializableInt.class, bpw.getValueHandler().getValueDeserializer().getSerializationConfig().getTypeFactory().constructType(bpw.getValueHandler().getValueDeserializer().getClass()));
    }

    @Test
    public void testBuildWriter_SerializableStringProperty() throws IOException {
        SerializerProvider prov = new StandardSerializerProvider();
        BeanPropertyDefinition propDef = propertyBuilder.findPropertyDefinition("stringProperty");
        JavaType declaredType = propertyBuilder.getTypeFactory().constructType(String.class);
        JsonSerializer<String> ser = new JsonSerializer<String>() {};
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedMember() {
            @Override
            public AnnotatedElement getMember() {
                return null;
            }

            @Override
            public String getName() {
                return "stringProperty";
            }
        };

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        // Test with empty String value
        String emptyStringValue = "";
        Assert.assertEquals("Expected marker for empty String value", BeanPropertyWriter.MARKER_FOR_EMPTY, bpw.getValueHandler().getSerializerProvider().getValueHandler(String.class).getValue(emptyStringValue));

        // Test with null value
        String stringValue = null;
        Assert.assertNull("Expected null for null String value", bpw.getValueHandler().getSerializerProvider().getValueHandler(String.class).getValue(stringValue));

        // Test with non-empty String value
        String nonEmptyStringValue = "test";
        Assert.assertEquals("Expected non-empty String value", nonEmptyStringValue, bpw.getValueHandler().getSerializerProvider().getValueHandler(String.class).getValue(nonEmptyStringValue));

        // Test with annotation that forces type to use super type
        propDef = propertyBuilder.findPropertyDefinition("stringPropertyWithAnnotation");
        declaredType = propertyBuilder.getTypeFactory().constructType(MySerializableString.class);
        Assert.assertEquals("Expected super type for String property with annotation", String.class, bpw.getValueHandler().getValueDeserializer().getSerializationConfig().getTypeFactory().constructType(bpw.getValueHandler().getValueDeserializer().getClass()));
    }

    @Test
    public void testBuildWriter_SerializableListProperty() throws IOException {
        SerializerProvider prov = new StandardSerializerProvider();
        BeanPropertyDefinition propDef = propertyBuilder.findPropertyDefinition("listProperty");
        JavaType declaredType = propertyBuilder.getTypeFactory().constructType(List.class.getTypeParameters()[0].getType());
        JsonSerializer<List> ser = new JsonSerializer<List>() {};
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedMember() {
            @Override
            public AnnotatedElement getMember() {
                return null;
            }

            @Override
            public String getName() {
                return "listProperty";
            }
        };

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        // Test with empty List value
        List<Integer> emptyListValue = Collections.emptyList();
        Assert.assertEquals("Expected marker for empty List value", BeanPropertyWriter.MARKER_FOR_EMPTY, bpw.getValueHandler().getSerializerProvider().getValueHandler(List.class).getValue(emptyListValue));

        // Test with null value
        List<Integer> listValue = null;
        Assert.assertNull("Expected null for null List value", bpw.getValueHandler().getSerializerProvider().getValueHandler(List.class).getValue(listValue));

        // Test with non-empty List value
        List<Integer> nonEmptyListValue = Arrays.asList(1, 2, 3);
        Assert.assertEquals("Expected non-empty List value", nonEmptyListValue, bpw.getValueHandler().getSerializerProvider().getValueHandler(List.class).getValue(nonEmptyListValue));

        // Test with annotation that forces type to use container type
        propDef = propertyBuilder.findPropertyDefinition("listPropertyWithAnnotation");
        declaredType = propertyBuilder.getTypeFactory().constructType(MySerializableList.class);
        Assert.assertEquals("Expected container type for List property with annotation", MySerializableList.class, bpw.getValueHandler().getValueDeserializer().getSerializationConfig().getTypeFactory().constructType(bpw.getValueHandler().getValueDeserializer().getClass()));
    }

    @Test
    public void testGetPropertyDefaultValue() throws IOException {
        SerializerProvider prov = new StandardSerializerProvider();
        BeanPropertyDefinition propDef = propertyBuilder.findPropertyDefinition("intProperty");
        JavaType type = propertyBuilder.getTypeFactory().constructType(int.class);

        // Test with default bean value
        Assert.assertEquals("Expected default int value from default bean value", 42, propertyBuilder.getPropertyDefaultValue("intProperty", null, type));

        // Test with null default bean value
        Assert.assertNull("Expected null for null default bean value", propertyBuilder.getPropertyDefaultValue("intProperty", null, type));

        // Test with non-default bean value
        MyPOJO myPOJO = new MyPOJO();
        myPOJO.setIntProperty(17);
        Assert.assertEquals("Expected int property value from non-default bean value", 17, propertyBuilder.getPropertyDefaultValue("intProperty", myPOJO, type));
    }

    @Test
    public void testGetDefaultValue() throws IOException {
        JavaType type = propertyBuilder.getTypeFactory().constructType(String.class);

        // Test with String value
        Assert.assertEquals("Expected empty String value for default value", "", propertyBuilder.getDefaultValue(type));

        // Test with null value
        Assert.assertNull("Expected null for null value", propertyBuilder.getDefaultValue(type));

        // Test with primitives and wrapper types for primitives
        Assert.assertEquals("Expected 0 for default int value", 0, propertyBuilder.getDefaultValue(propertyBuilder.getTypeFactory().constructType(int.class)));
        Assert.assertEquals("Expected 0 for default int wrapper value", 0, propertyBuilder.getDefaultValue(propertyBuilder.getTypeFactory().constructType(Integer.class)));

        // Test with structured types (Maps, Collections, and arrays) and reference types
        TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {};
        Assert.assertEquals("Expected JsonInclude.Include.NON_EMPTY for default Map value", JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(typeRef));

        TypeReference<List<String>> listTypeRef = new TypeReference<List<String>>() {};
        Assert.assertEquals("Expected JsonInclude.Include.NON_EMPTY for default List value", JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(listTypeRef));

        TypeReference<MySerializableClass[]> arrayTypeRef = new TypeReference<MySerializableClass[]>() {};
        Assert.assertEquals("Expected JsonInclude.Include.NON_EMPTY for default Array value", JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(arrayTypeRef));

        // Test with reference type
        TypeReference<MySerializableClass> referenceTypeRef = new TypeReference<MySerializableClass>() {};
        Assert.assertEquals("Expected JsonInclude.Include.NON_EMPTY for default reference type value", JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(referenceTypeRef));
    }

    @Test
    public void test_ThrowWrapped() throws IOException {
        Throwable e = new IllegalStateException("Test exception");
        String propName = "testProperty";
        Object defaultBean = new Object();

        try {
            propertyBuilder._throwWrapped(e, propName, defaultBean);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Failed to get property 'testProperty' of default Object instance", ex.getMessage());
        }
    }

    private static class MyPOJO {
        private int intProperty;

        public int getIntProperty() {
            return intProperty;
        }

        public void setIntProperty(int intProperty) {
            this.intProperty = intProperty;
        }

        public MyPOJO() {
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public int intPropertyWithAnnotation() {
            return intProperty;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private static class MySerializableInt {
        private int intProperty;

        public int getIntProperty() {
            return intProperty;
        }

        public void setIntProperty(int intProperty) {
            this.intProperty = intProperty;
        }
    }

    private static class MySerializableString {
        private String stringProperty;

        public String getStringProperty() {
            return stringProperty;
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }
    }

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private static class MySerializableList<T> {
        private List<T> listProperty;

        public List<T> getListProperty() {
            return listProperty;
        }

        public void setListProperty(List<T> listProperty) {
            this.listProperty = listProperty;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class MySerializableClass {
        // Empty class for testing
    }
}