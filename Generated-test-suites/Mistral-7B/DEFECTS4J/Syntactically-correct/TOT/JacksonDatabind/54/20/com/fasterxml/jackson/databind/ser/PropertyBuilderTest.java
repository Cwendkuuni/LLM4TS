package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilder_DefaultInclusion() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);

        ObjectNode node = mapper(new ObjectMapper(config)).valueToTree(new TestBean());
        assertEquals(node.size(), 3);

        // Testing each property
        assertEquals(node.path("name").asText(), "John Doe");
        assertEquals(node.path("age").asInt(), 30);
        assertEquals(node.path("address").asText(), "123 Main St");
    }

    @Test
    public void testPropertyBuilder_CustomInclusion() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);

        ObjectNode node = mapper(new ObjectMapper(config)).valueToTree(new TestBean("", 0, null));
        assertEquals(node.size(), 2);

        // Testing non-default properties
        assertNull(node.get("name"));
        assertNull(node.get("address"));
    }

    @Test
    public void testPropertyBuilder_NullProperty() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        ObjectNode node = mapper(new ObjectMapper(config)).valueToTree(new TestBean(null, null, null));
        assertNull(node);
    }

    @Test
    public void testPropertyBuilder_MissingDefault() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(TestBean.class);

        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Annotations classAnnotations = builder.getClassAnnotations();
        assertEquals(classAnnotations.size(), 0);

        // Testing default inclusion
        JsonInclude.Value defaultInclusion = builder._defaultInclusion;
        assertEquals(defaultInclusion.getValueInclusion(), JsonInclude.Include.ALWAYS);

        // Testing nullability of _defaultBean
        Object defaultBean = builder._defaultBean;
        assertNull(defaultBean);

        // Testing buildWriter method with no contentTypeSerializer
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter bpw = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping);
        assertNotNull(bpw);
    }

    @Test
    public void testPropertyBuilder_CustomContentTypeSerializer() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);

        ObjectNode node = mapper(new ObjectMapper(config)).valueToTree(new TestBeanWithList("list", new int[] {1, 2, 3}));
        assertEquals(node.size(), 4);

        // Testing each property
        assertEquals(node.path("name").asText(), "TestBeanWithList");
        assertEquals(node.path("list").isArray(), true);
        assertEquals(node.path("list").size(), 3);
        assertEquals(node.path("list[0]").asInt(), 1);
    }

    @Test
    public void testPropertyBuilder_EmptyCollectionSuppression() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
        config.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        ObjectNode node = mapper(new ObjectMapper(config)).valueToTree(new TestBeanWithList(null));
        assertEquals(node.size(), 1);
        assertNull(node.get("list"));
    }

    private static ObjectMapper mapper(ObjectMapper mapper) {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

    static class TestBean {
        private String name;
        private int age;
        private String address;

        public TestBean() {
            this("John Doe", 30, "123 Main St");
        }

        public TestBean(String name, int age, String address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }
    }

    static class TestBeanWithList {
        private String name;
        private int[] list;

        public TestBeanWithList() {
            this("TestBeanWithList", null);
        }

        public TestBeanWithList(String name, int[] list) {
            this.name = name;
            this.list = list;
        }
    }
}