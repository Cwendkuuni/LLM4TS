package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Field;

public class ObjectIdReferencePropertyTest {

    @SuppressWarnings("unchecked")
    private static class TestPojo {
        @JsonDeserialize(using = ObjectIdReferenceProperty.PropertyReferring.class)
        private Object id;
        private String name;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @SuppressWarnings("unchecked")
    private static class TestPojoDeserializer extends JsonDeserializer<TestPojo> {
        @Override
        public TestPojo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectNode node = (ObjectNode) p.readValueAsTree();
            TestPojo pojo = new TestPojo();
            ObjectMapper mapper = new ObjectMapper();
            pojo.setName(mapper.readValue(node.get("name"), String.class));
            pojo.setId(mapper.readValue(node.get("id"), Object.class));
            return pojo;
        }
    }

    @org.junit.Test
    public void testSetAndGetObjectIdReferenceProperty() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<TestPojo> deserializer = new TestPojoDeserializer();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("name", "testName");
        rootNode.put("id", mapper.valueToTree(new ObjectIdReferencePropertyTest.TestIdReference()));
        TestPojo pojo = mapper.reader(TestPojo.class).readValue(rootNode);
        ObjectIdReferenceProperty property = AnnotationIntrospector.findProperty(TestPojo.class, "id");
        assertEquals(TestIdReference.class, property.getAnnotation(ObjectIdReferenceProperty.PropertyReferring.class).getReferencedType());
        assertEquals("testName", pojo.getName());
        assertNull(pojo.getId());
        ObjectIdReferenceProperty referenceProperty = (ObjectIdReferenceProperty) property;
        Object idValue = new ObjectIdReferenceProperty.TestIdValue("testId");
        ObjectIdReferencePropertyTest.TestIdReference idReference = new ObjectIdReferencePropertyTest.TestIdReference();
        referenceProperty.set(pojo, idValue);
        assertEquals("testId", pojo.getId());
        assertEquals(idValue, pojo.getId());
        assertEquals(idReference, referenceProperty.getReferring(pojo));
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testSetObjectIdReferencePropertyWithNullValue() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<TestPojo> deserializer = new TestPojoDeserializer();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("name", "testName");
        rootNode.put("id", null);
        TestPojo pojo = mapper.reader(TestPojo.class).readValue(rootNode);
        ObjectIdReferenceProperty property = AnnotationIntrospector.findProperty(TestPojo.class, "id");
        ObjectIdReferenceProperty referenceProperty = (ObjectIdReferenceProperty) property;
        referenceProperty.set(pojo, null);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testSetObjectIdReferencePropertyWithNonObjectValue() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<TestPojo> deserializer = new TestPojoDeserializer();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("name", "testName");
        rootNode.put("id", mapper.valueToTree(123));
        TestPojo pojo = mapper.reader(TestPojo.class).readValue(rootNode);
        ObjectIdReferenceProperty property = AnnotationIntrospector.findProperty(TestPojo.class, "id");
        ObjectIdReferenceProperty referenceProperty = (ObjectIdReferenceProperty) property;
        referenceProperty.set(pojo, 123);
    }

    @org.junit.Test
    public void testGetObjectIdReferenceProperty() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<TestPojo> deserializer = new TestPojoDeserializer();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("name", "testName");
        rootNode.put("id", mapper.valueToTree(new ObjectIdReferencePropertyTest.TestIdValue("testId")));
        TestPojo pojo = mapper.reader(TestPojo.class).readValue(rootNode);
        ObjectIdReferenceProperty property = AnnotationIntrospector.findProperty(TestPojo.class, "id");
        ObjectIdReferenceProperty referenceProperty = (ObjectIdReferenceProperty) property;
        assertEquals(TestIdValue.class, referenceProperty.getReferencedType());
        assertEquals(referenceProperty, AnnotationIntrospector.findProperty(TestPojo.class, "id"));
    }

    public static class TestIdValue {
        private final String id;

        public TestIdValue(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class TestIdReference {
        private final String name;

        public TestIdReference() {
            this.name = "TestIdReference";
        }

        public String getName() {
            return name;
        }
    }
}