package com.example.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private SerializationConfig config;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        BeanDescription beanDescription = mapper.getTypeFactory().constructType(Person.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testBuildWriter() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        TypeSerializer typeSerializer = mapper.getTypeSerializer(javaType);

        Person person = new Person("Alice", Arrays.asList(new Person("Bob"), new Person("Charlie")));
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("name");
        BeanPropertyDefinition listPropDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("list");

        JsonSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString());
            }
        };

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef,
                mapper.getTypeFactory().constructType(String.class), serializer, null, typeSerializer, null, true);

        assertEquals("Alice", beanPropertyWriter.getValueAsString());

        JavaType listType = mapper.getTypeFactory().constructType(new TypeReference<List<Person>>(){}.getType());
        TypeSerializer listTypeSerializer = mapper.getTypeSerializer(listType);

        Object listValue = Arrays.asList(new Person("Dave"), new Person("Eve"));
        BeanPropertyWriter listBeanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), listPropDef,
                listType, null, null, listTypeSerializer, null, true);

        assertEquals(listValue, listBeanPropertyWriter.getValue());
    }

    @Test
    public void testFindSerializationType() {
        Class<Person> personClass = Person.class;
        JavaType javaType = mapper.getTypeFactory().constructType(personClass);
        AnnotatedMember member = BeanUtil.findSerializerProperty(personClass, "list");

        JavaType serializationType = propertyBuilder.findSerializationType(member, true, javaType);

        List<Person> listValue = Arrays.asList(new Person("Dave"), new Person("Eve"));
        JavaType listType = mapper.getTypeFactory().constructType(new TypeReference<List<Person>>(){}.getType());
        TypeSerializer listTypeSerializer = mapper.getTypeSerializer(listType);

        assertEquals(listType, serializationType);
        assertEquals(listTypeSerializer, serializationType.getTypeSerializer());
    }

    @Test
    public void testGetDefaultBean() {
        Person person = new Person("John");
        propertyBuilder.setDefaultBean(person);

        Person defaultBean = propertyBuilder.getDefaultBean();
        assertEquals(person, defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws IOException {
        Person person = new Person("John");
        propertyBuilder.setDefaultBean(person);

        String propertyName = "name";
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, mapper.getTypeFactory().constructType(Person.class));

        assertNull(propertyDefaultValue);

        propertyName = "list";
        Object listDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, null, mapper.getTypeFactory().constructType(List.class));

        assertEquals(Collections.emptyList(), listDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);

        Object defaultValue = propertyBuilder.getDefaultValue(javaType);

        assertNull(defaultValue);

        javaType = mapper.getTypeFactory().constructType(List.class);

        Object listDefaultValue = propertyBuilder.getDefaultValue(javaType);

        assertEquals(Collections.emptyList(), listDefaultValue);
    }

    @Test
    public void testThrowWrapped() {
        Exception exception = new IllegalArgumentException("Test exception");
        String propName = "test";
        Object defaultBean = new Object();

        Object thrownObject = propertyBuilder._throwWrapped(exception, propName, defaultBean);

        assertEquals("Test exception", thrownObject.getMessage());
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class Person {
        private String name;
        private List<Person> list;

        public Person() {}

        public Person(String name) {
            this.name = name;
        }

        public Person(String name, List<Person> list) {
            this.name = name;
            this.list = list;
        }

        @JsonSerialize
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonSerialize(using = PersonSerializer.class)
        public List<Person> getList() {
            return list;
        }

        public void setList(List<Person> list) {
            this.list = list;
        }
    }

    static class PersonSerializer extends StdSerializer<Person> {
        public PersonSerializer() {
            this(null);
        }

        public PersonSerializer(Class<Person> t) {
            super(t);
        }

        @Override
        public void serialize(Person person, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeString(person.getName());
            jsonGenerator.writeFieldName("list");
            if (person.getList() != null) {
                JsonSerializer<List<Person>> listSerializer = serializerProvider.findValueSerializer(person.getList().getClass(), null);
                listSerializer.serialize(person.getList(), jsonGenerator, serializerProvider);
            }
            jsonGenerator.writeEndObject();
        }
    }
}