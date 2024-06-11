package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class SettableAnyPropertyTest {

    private ObjectMapper mapper;

    @BeforeClass
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSettableAnyProperty_StringField() throws Exception {
        BeanProperty property = mapper.findBeanProperty("com.test.Person", "name");
        AnnotatedMember setter = property.getMember();
        JavaType type = mapper.getTypeFactory().constructType(Person.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        // Test setting a string value
        Person person = new Person();
        anyProperty.deserializeAndSet(createParser("{\"name\":\"John\"}"), mapper.getConfiguration(), person, "name");
        assertEquals(person.getName(), "John");

        // Test setting a null value
        JsonParser parser = createParser("{\"name\":null}");
        anyProperty.deserializeAndSet(parser, mapper.getConfiguration(), person, "name");
        assertNull(person.getName());

        // Test setting an invalid value (non-string)
        parser = createParser("{\"name\":123}");
        try {
            anyProperty.deserializeAndSet(parser, mapper.getConfiguration(), person, "name");
            assert false; // should throw an exception
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testSettableAnyProperty_MapField() throws Exception {
        BeanProperty property = mapper.findBeanProperty(Person.class, "properties");
        AnnotatedMember setter = property.getMember();
        JavaType type = mapper.getTypeFactory().constructType(Person.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        // Test setting a string key, string value
        Person person = new Person();
        anyProperty.deserializeAndSet(createParser("{\"properties\":{\"test\":\"John\"}}"), mapper.getConfiguration(), person, "properties");
        assertEquals(person.getProperties().get("test"), "John");

        // Test setting a null value
        JsonParser parser = createParser("{\"properties\":null}");
        anyProperty.deserializeAndSet(parser, mapper.getConfiguration(), person, "properties");
        assertNull(person.getProperties());

        // Test setting a non-string key
        parser = createParser("{\"properties\":{123:\"John\"}}");
        try {
            anyProperty.deserializeAndSet(parser, mapper.getConfiguration(), person, "properties");
            assert false; // should throw an exception
        } catch (JsonMappingException e) {
            // expected
        }
    }

    // Add more test cases as needed

    private JsonParser createParser(String content) throws IOException {
        JsonFactory factory = mapper.getFactory();
        return factory.createParser(new StringReader(content));
    }

    private static class Person {
        private String name;
        private Map<Object, Object> properties;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<Object, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<Object, Object> properties) {
            this.properties = properties;
        }
    }
}