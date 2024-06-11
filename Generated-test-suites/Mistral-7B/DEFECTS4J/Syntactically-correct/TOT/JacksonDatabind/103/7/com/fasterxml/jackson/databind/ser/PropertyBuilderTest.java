package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.impl.BeanPropertyWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testBasicPropertySerialization() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
        MyClass myClass = new MyClass("Test", 123);
        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializationConfig().getSerializerProvider(), null, MyClass.class, null, null, null, null, true);
        String json = propertyWriter.writeValueAsString(myClass);
        assertEquals("{\"name\":\"Test\",\"number\":123}", json);
    }

    @Test
    public void testNullPropertySerialization() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
        MyClass myClass = new MyClass(null, null);
        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializationConfig().getSerializerProvider(), null, MyClass.class, null, null, null, null, true);
        String json = propertyWriter.writeValueAsString(myClass);
        assertEquals("{}", json);
    }

    @Test
    public void testEmptyArrayPropertySerialization() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
        MyClassWithArray myClass = new MyClassWithArray(new String[0]);
        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializationConfig().getSerializerProvider(), null, MyClassWithArray.class, null, null, null, null, true);
        String json = propertyWriter.writeValueAsString(myClass);
        assertEquals("{\"arrayProperty\":[]}", json);
    }

    @Test
    public void testNonDefaultInclusion() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
        MyClassWithNonDefault myClass = new MyClassWithNonDefault();
        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializationConfig().getSerializerProvider(), null, MyClassWithNonDefault.class, null, null, null, null, true);
        String json = propertyWriter.writeValueAsString(myClass);
        assertEquals("{}", json);

        JsonInclude.Value inclusion = propertyBuilder._defaultInclusion;
        assertTrue(inclusion.getValueInclusion() == JsonInclude.Include.NON_DEFAULT || inclusion.getValueInclusion() == JsonInclude.Include.NON_EMPTY);
    }

    @Test(expected = JsonMappingException.class)
    public void testFailedToFindSerializationType() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
        ObjectMapper.SerializeValueProvider serProvider = objectMapper.getSerializerProvider();
        AnnotatedMember am = null;
        BeanPropertyWriter buildWriter = propertyBuilder.buildWriter(serProvider, null, Object.class, null, null, null, am, true);
    }

    @Test
    public void testSuppressedNulls() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
        MyClassWithSuppressedNulls myClass = new MyClassWithSuppressedNulls(null);
        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializationConfig().getSerializerProvider(), null, MyClassWithSuppressedNulls.class, null, null, null, null, true);
        String json = propertyWriter.writeValueAsString(myClass);
        assertEquals("{}", json);
    }

    @Test
    public void testEmptyClassPropertySerialization() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
        MyClassWithEmptyClass myClass = new MyClassWithEmptyClass();
        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializationConfig().getSerializerProvider(), null, MyClassWithEmptyClass.class, null, null, null, null, true);
        String json = propertyWriter.writeValueAsString(myClass);
        assertEquals("{}", json);
    }

    @Test
    public void testTypeSerializer() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
        MyClassWithTypeSerializer myClass = new MyClassWithTypeSerializer();
        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializationConfig().getSerializerProvider(), null, MyClassWithTypeSerializer.class, null, null, null, null, true);
        ObjectMapper serializerProvider = propertyWriter.getSerializerProvider();
        TypeSerializer typeSerializer = serializerProvider.findTypeSerializer(MyClassWithTypeSerializer.class, null);
        String json = propertyWriter.writeValueAsString(myClass);
        assertEquals("{\"typeSerializer\":\"[SimpleTypeSerializer]\"}", json);
    }

    @Test
    public void testFindSerializationTypeWithAnnotations() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
        MyClassWithAnnotation myClass = new MyClassWithAnnotation();
        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializationConfig().getSerializerProvider(), null, MyClassWithAnnotation.class, null, null, null, null, true);
        JavaType serializationType = propertyWriter.getSerializationType();
        assertEquals(String.class, serializationType.getRawClass());
    }

    @Test
    public void testCustiomNullFilter() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
        MyClassWithCustomNullFilter myClass = new MyClassWithCustomNullFilter();
        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializationConfig().getSerializerProvider(), null, MyClassWithCustomNullFilter.class, null, null, null, null, true);
        String json = propertyWriter.writeValueAsString(myClass);
        assertTrue(json.equals("{}") || json.equals("{\"customNullFilter\":null}"));
    }

    static class MyClass {
        @JsonProperty("name")
        private String name;
        private int number;

        public MyClass(String name, int number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    static class MyClassWithArray {
        private String[] arrayProperty;

        public MyClassWithArray(String[] arrayProperty) {
            this.arrayProperty = arrayProperty;
        }

        public String[] getArrayProperty() {
            return arrayProperty;
        }

        public void setArrayProperty(String[] arrayProperty) {
            this.arrayProperty = arrayProperty;
        }
    }

    static class MyClassWithNonDefault {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int number;

        public MyClassWithNonDefault() {
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    static class MyClassWithSuppressedNulls {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String nullableString;

        public MyClassWithSuppressedNulls(String nullableString) {
            this.nullableString = nullableString;
        }

        public String getNullableString() {
            return nullableString;
        }

        public void setNullableString(String nullableString) {
            this.nullableString = nullableString;
        }
    }

    static class MyClassWithCustomNullFilter {
        @JsonInclude(JsonInclude.Include.CUSTOM)
        @JsonSerialize(using = CustomNullSerializer.class)
        private Object customNullFilter;

        public MyClassWithCustomNullFilter() {
        }

        public Object getCustomNullFilter() {
            return customNullFilter;
        }

        public void setCustomNullFilter(Object customNullFilter) {
            this.customNullFilter = customNullFilter;
        }
    }

    static class CustomNullSerializer extends StdSerializer<Object> {
        public CustomNullSerializer() {
            this(null);
        }

        public CustomNullSerializer(Class<Object> t) {
            this(null);
        }

        public CustomNullSerializer(Class<Object> t, BeanPropertyWriter.SerializerProvider p) {
            this(null);
        }

        public CustomNullSerializer(Class<Object> t, BeanPropertyWriter.SerializerProvider p, AnnotationIntrospector i, JsonSerializer<Object> s, DefaultSerializerProvider p1) {
            super(t, p);
        }

        @Override
        public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException {
            if (value == null) {
                jg.writeNull();
            } else {
                jg.writeValue(value);
            }
        }
    }
}