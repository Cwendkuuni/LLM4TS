package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private static class TestBean {
        private String name;
        private Integer age;
        private List<Person> persons;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public List<Person> getPersons() {
            return persons;
        }

        public void setPersons(List<Person> persons) {
            this.persons = persons;
        }
    }

    private static class Person {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private ExternalTypeHandler handler;
    private Map<String, SettableBeanProperty> properties;
    private ObjectMapper mapper;
    private JsonFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new ObjectMapper().getFactory();
        mapper = new ObjectMapper();

        SettableBeanProperty nameProp = new SettableBeanProperty("name", String.class);
        SettableBeanProperty ageProp = new SettableBeanProperty("age", Integer.class);
        SettableBeanProperty personProp = new SettableBeanProperty("persons", Person[].class);

        properties = new HashMap<>();
        properties.put("name", nameProp);
        properties.put("age", ageProp);
        properties.put("persons", personProp);

        List<SettableBeanProperty> propList = new ArrayList<>(properties.values());
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        for (SettableBeanProperty property : propList) {
            builder.addExternal(property, mapper.getTypeFactory().constructType(property.getType()));
        }
        handler = builder.build();
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        JsonParser jp = factory.createParser("{\"name\":\"John\",\"type\":\"String\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TestBean bean = new TestBean();

        handler.handleTypePropertyValue(jp, ctxt, "name", bean);
        assertEquals("John", bean.getName());
        assertNull(bean.getAge());
        assertNull(bean.getPersons());

        jp = factory.createParser("{\"age\":42,\"type\":\"Integer\"}");
        handler.handleTypePropertyValue(jp, ctxt, "age", bean);
        assertEquals("John", bean.getName());
        assertEquals(42, bean.getAge().intValue());
        assertNull(bean.getPersons());

        jp = factory.createParser("{\"persons\":[{\"name\":\"Alice\"},{\"name\":\"Bob\"}],\"type\":\"List<Person>\"}");
        handler.handleTypePropertyValue(jp, ctxt, "persons", bean);
        List<Person> persons = bean.getPersons();
        assertEquals(2, persons.size());
        assertEquals("Alice", persons.get(0).getName());
        assertEquals("Bob", persons.get(1).getName());
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        JsonParser jp = factory.createParser("{\"name\":\"John\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TestBean bean = new TestBean();

        handler.handlePropertyValue(jp, ctxt, "name", bean);
        assertEquals("John", bean.getName());
        assertNull(bean.getAge());
        assertNull(bean.getPersons());

        jp = factory.createParser("{\"age\":42}");
        handler.handlePropertyValue(jp, ctxt, "age", bean);
        assertEquals("John", bean.getName());
        assertEquals(42, bean.getAge().intValue());
        assertNull(bean.getPersons());

        jp = factory.createParser("{\"persons\":[{\"name\":\"Alice\"},{\"name\":\"Bob\"}]}");
        handler.handlePropertyValue(jp, ctxt, "persons", bean);
        List<Person> persons = bean.getPersons();
        assertEquals(2, persons.size());
        assertEquals("Alice", persons.get(0).getName());
        assertEquals("Bob", persons.get(1).getName());
    }

    @Test
    public void testComplete() throws Exception {
        JsonParser jp = factory.createParser("{\"name\":\"John\",\"type\":\"String\",\"age\":42}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TestBean bean = new TestBean();

        handler.handlePropertyValue(jp, ctxt, "name", bean);
        handler.handlePropertyValue(jp, ctxt, "age", bean);
        Object result = handler.complete(jp, ctxt, bean);
        assertEquals(bean, result);
        assertEquals("John", bean.getName());
        assertEquals(42, bean.getAge().intValue());
        assertNull(bean.getPersons());

        jp = factory.createParser("{\"name\":\"John\",\"type\":\"String\",\"age\":42,\"persons\":[{\"name\":\"Alice\"},{\"name\":\"Bob\"}]}");
        result = handler.complete(jp, ctxt, bean);
        assertEquals(bean, result);
        assertEquals("John", bean.getName());
        assertEquals(42, bean.getAge().intValue());
        List<Person> persons = bean.getPersons();
        assertEquals(2, persons.size());
        assertEquals("Alice", persons.get(0).getName());
        assertEquals("Bob", persons.get(1).getName());
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingTypeIdProperty() throws Exception {
        JsonParser jp = factory.createParser("{\"name\":\"John\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TestBean bean = new TestBean();

        handler.handlePropertyValue(jp, ctxt, "persons", bean);
        handler.complete(jp, ctxt, bean);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingPropertyForTypeId() throws Exception {
        JsonParser jp = factory.createParser("{\"type\":\"List<Person\">\",\"name\":\"John\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TestBean bean = new TestBean();

        handler.handleTypePropertyValue(jp, ctxt, "type", bean);
        handler.handlePropertyValue(jp, ctxt, "name", bean);
        handler.complete(jp, ctxt, bean);
        fail("Expected JsonMappingException");
    }
}