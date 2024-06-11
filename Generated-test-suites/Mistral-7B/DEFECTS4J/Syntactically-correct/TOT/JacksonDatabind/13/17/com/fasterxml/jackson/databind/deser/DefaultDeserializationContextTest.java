package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    private ObjectMapper objectMapper;
    private XmlMapper xmlMapper;

    @BeforeClass
    public void setup() {
        objectMapper = new ObjectMapper();
        xmlMapper = new XmlMapper();
    }

    @Test
    public void testObjectIdGeneration() throws IOException {
        // Test normal object id generation
        Person person = objectMapper.readValue(
                "[{\"name\":\"Alice\",\"id\":null}]", Person[].class);
        assertEquals(person[0].getId(), null);

        Person person2 = objectMapper.readValue(
                "[{\"name\":\"Bob\",\"id\":1}]", Person[].class);
        assertEquals(person2[0].getId(), new ReadableObjectId(IdKey.ID, 1));

        // Test object id resolution
        PersonContainer personContainer = objectMapper.readValue(
                "[{\"persons\":[{\"name\":\"Alice\",\"id\":null},{\"name\":\"Bob\",\"id\":1}],\"id\":null}]",
                PersonContainer.class);
        assertEquals(personContainer.getId(), null);
        assertEquals(personContainer.getPersons()[0].getId(), null);
        assertEquals(personContainer.getPersons()[1].getId(), new ReadableObjectId(IdKey.ID, 1));

        // Test error handling for unresolved object ids
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
            PersonContainer container = objectMapper.readValue(
                    "[{\"persons\":[{\"name\":\"Alice\",\"id\":null},{\"name\":\"Bob\",\"id\":1},{\"name\":\"Charlie\",\"id\":null}],\"id\":null}]",
                    PersonContainer.class);
        } catch (UnresolvedForwardReference e) {
            // Expected
        }

        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true);
            PersonContainer container = objectMapper.readValue(
                    "[{\"persons\":[{\"name\":\"Alice\",\"id\":null},{\"name\":\"Bob\",\"id\":1},{\"name\":\"Charlie\",\"id\":null}],\"id\":null}]",
                    PersonContainer.class);
            fail();
        } catch (UnresolvedForwardReference e) {
            // Expected, but not called if failOnUnresolvedObjectIds is false
            assertEquals(e.getUnresolvedIds().size(), 1);
            assertEquals(e.getUnresolvedIds().get(0).getIdClass(), IdKey.class);
            assertEquals(e.getUnresolvedIds().get(0).getBeanType(), PersonContainer.class);
            assertEquals(e.getUnresolvedIds().get(0).getLocation(), "id");
        }

        // Test XML deserialization
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true);
        PersonContainer xmlContainer = xmlMapper.readValue(
                "<container><persons><person name=\"Alice\" /><person name=\"Bob\" id=\"1\" /><person name=\"Charlie\" /></persons><id /></container>",
                PersonContainer.class);
        try {
            xmlMapper.readValue(
                    "<container><persons><person name=\"Alice\" /><person name=\"Bob\" id=\"1\" /><person name=\"Charlie\" /></persons></container>",
                    PersonContainer.class);
            fail();
        } catch (UnresolvedForwardReference e) {
            // Expected
            assertEquals(e.getUnresolvedIds().size(), 1);
            assertEquals(e.getUnresolvedIds().get(0).getIdClass(), IdKey.class);
            assertEquals(e.getUnresolvedIds().get(0).getBeanType(), PersonContainer.class);
            assertEquals(e.getUnresolvedIds().get(0).getLocation(), null);
        }

        // Test null ObjectIdResolver
        JsonDeserializer<Person> nullResolver = new JsonDeserializer<Person>() {
            @Override
            public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new RuntimeException("Should not reach here");
            }
        };
        ObjectMapper nullObjectMapper = new ObjectMapper();
        nullObjectMapper.setDefaultUseWrapper(false);
        nullObjectMapper.setSerializationInclusion(FindNulls.NON_NULL);
        nullObjectMapper.addDeserializer(Person.class, nullResolver);
        try {
            nullObjectMapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true);
            nullObjectMapper.readValue("[{\"id\":null}]", Person.class);
            fail();
        } catch (UnresolvedForwardReference e) {
            // Expected, but should not reach here as nullObjectMapper does not have ObjectIdResolver
            fail();
        }
    }

    static class Person {
        @JsonAutoDetect
        private Integer id;
        private String name;

        public Person() {
        }

        public Person(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        @JsonIgnore
        public Integer get$id() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return ToStringSerializer.instance.serializeValues(this, null);
        }
    }

    static class PersonContainer {
        @JsonAutoDetect
        private List<Person> persons;
        @JsonIgnore
        private Integer id;

        public PersonContainer() {
        }

        public PersonContainer(List<Person> persons) {
            this.persons = persons;
        }

        public List<Person> getPersons() {
            return persons;
        }

        public void setPersons(List<Person> persons) {
            this.persons = persons;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}