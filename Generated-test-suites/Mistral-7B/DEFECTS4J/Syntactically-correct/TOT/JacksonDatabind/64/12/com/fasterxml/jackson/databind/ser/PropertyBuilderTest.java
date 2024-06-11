package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = mapper.findBeanDescription(Person.class);
    }

    @Test
    public void testSimplePropertySerialization() throws JsonProcessingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializationConfig newConfig = new SerializationConfig(config);
        newConfig.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(),
                beanDesc.findPropertyDefinition("name"), Person.class, null, null, null, null, true);

        Person person = new Person("John Doe");
        writer.writeValueAsString(person);

        String expected = "\"John Doe\"";
        String actual = config.getDefaultSerializerProvider().textualValueOf(writer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNullPropertySerialization() throws JsonProcessingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(),
                beanDesc.findPropertyDefinition("age"), Person.class, null, null, null, null, true);

        Person person = new Person(null);
        writer.writeValueAsString(person);

        String expected = "null";
        String actual = config.getDefaultSerializerProvider().textualValueOf(writer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEmptyCollectionSerialization() throws JsonProcessingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(),
                beanDesc.findPropertyDefinition("list"), Person[].class, null, null, null, null, true);

        writer.writeValueAsString(new Person[0]);

        String expected = "[]";
        String actual = config.getDefaultSerializerProvider().textualValueOf(writer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNonEmptyCollectionSerialization() throws JsonProcessingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(),
                beanDesc.findPropertyDefinition("list"), Person[].class, null, null, null, null, true);

        writer.writeValueAsString(new Person[]{new Person("Alice"), new Person("Bob")});

        String expected = "[{\"name\":\"Alice\"},{\"name\":\"Bob\"}]";
        String actual = config.getDefaultSerializerProvider().textualValueOf(writer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTransientPropertySerialization() throws JsonProcessingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(),
                beanDesc.findPropertyDefinition("transientProperty"), Person.class, null, null, null, null, true);

        Person person = new Person("John Doe");
        writer.writeValueAsString(person);

        String expected = "{}";
        String actual = config.getDefaultSerializerProvider().textualValueOf(writer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteEmptyJsonArray() throws JsonProcessingException {
        SerializationConfig newConfig = new SerializationConfig(config);
        newConfig.setSerializationFeature(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);

        PropertyBuilder propertyBuilder = new PropertyBuilder(newConfig, beanDesc);

        PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(),
                beanDesc.findPropertyDefinition("list"), Person[].class, null, null, null, null, true);

        writer.writeValueAsString(new Person[0]);

        String expected = null;
        try {
            config.getDefaultSerializerProvider().textualValueOf(writer);
        } catch (JsonProcessingException e) {
            expected = e.getMessage();
        }
        Assert.assertEquals("Cannot construct instance of [simple type, class com.fasterxml.jackson.databind.ser.PropertyBuilderTest$Person], not null", expected);
    }

    @Test
    public void testNullSerializer() throws JsonProcessingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(),
                beanDesc.findPropertyDefinition("name"), Person.class, null, null, null, null, true);

        AnnotationIntrospector annotationIntrospector = config.getAnnotationIntrospector();
        NameTransformer nameTransformer = annotationIntrospector.findUnwrappingNameTransformer(writer.getMember());
        JsonSerializer<String> nullSerializer = annotationIntrospector.findNullSerializer(writer.getMember());
        writer = writer.unwrappingWriter(nameTransformer).assignNullSerializer(mapper.getSerializerProvider().serializerInstance(writer.getMember(), nullSerializer));

        Person person = new Person(null);
        writer.writeValueAsString(person);

        String expected = "null";
        String actual = config.getDefaultSerializerProvider().textualValueOf(writer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCustomSerialization() throws JsonProcessingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotationIntrospector annotationIntrospector = config.getAnnotationIntrospector();
        JsonSerialize.Typing serializationTyping = annotationIntrospector.findSerializationTyping(beanDesc.findPropertyDefinition("age"));

        PropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(),
                beanDesc.findPropertyDefinition("age"), Person.class, null, null, null, null, true);

        if (serializationTyping != JsonSerialize.Typing.DEFAULT_TYPING) {
            writer = writer.withTypeSerializer(mapper.getTypeSerializer(Person.class, Object.class, null, null, serializationTyping));
        }

        Person person = new Person(25);
        writer.writeValueAsString(person);

        String expected = "25";
        String actual = config.getDefaultSerializerProvider().textualValueOf(writer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPropertyBuilderWithNullBeanDesc() {
        try {
            new PropertyBuilder(config, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPropertyBuilderWithNullBeanConfig() {
        try {
            new PropertyBuilder(null, beanDesc);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    static class Person {
        private String name;
        private Integer age;
        private transient String transientProperty;

        public Person(String name) {
            this.name = name;
        }

        public Person(Integer age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        public String getTransientProperty() {
            return transientProperty;
        }
    }
}