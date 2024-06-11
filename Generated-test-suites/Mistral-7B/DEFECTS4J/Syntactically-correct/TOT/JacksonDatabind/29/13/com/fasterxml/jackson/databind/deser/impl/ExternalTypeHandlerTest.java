package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Test case for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    /**
     * Test method for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)}
     * when property name matches the external type id.
     */
    @Test
    public void testHandleTypePropertyValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"name\": \"John\", \"type\": \"Person\"}";
        JsonParser jp = factory.createParser(json);
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new Builder().build());
        handler.start();
        handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "type", bean);
        assertEquals("Person", handler.complete(jp, mapper.getDeserializationContext(), bean).getClass().getSimpleName());
    }

    /**
     * Test method for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)}
     * when property name does not match the external type id.
     */
    @Test
    public void testHandleTypePropertyValue_wrongPropertyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"name\": \"John\", \"type\": \"Person\"}";
        JsonParser jp = factory.createParser(json);
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new Builder().build());
        handler.start();
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "wrongName", bean);
        assertNull(handler.complete(jp, mapper.getDeserializationContext(), bean));
    }

    /**
     * Test method for {@link ExternalTypeHandler#handlePropertyValue(JsonParser, DeserializationContext, String, Object)}
     * when property name matches the property name of the deserializer.
     */
    @Test
    public void testHandlePropertyValue_propertyNameMatchesDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"name\": \"John\", \"age\": 30}";
        JsonParser jp = factory.createParser(json);
        Object bean = new Person();
        ExternalTypeHandler handler = new ExternalTypeHandler(new Builder().build());
        handler.start();
        handler.addExternal(new SettableBeanProperty("age", AgeDeserializer.class), new AgeDeserializer());
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "age", bean);
        assertEquals(30, handler.complete(jp, mapper.getDeserializationContext(), bean).getAge());
    }

    /**
     * Test method for {@link ExternalTypeHandler#handlePropertyValue(JsonParser, DeserializationContext, String, Object)}
     * when property name does not match the property name of the deserializer.
     */
    @Test
    public void testHandlePropertyValue_propertyNameDoesNotMatchDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"name\": \"John\", \"age\": 30}";
        JsonParser jp = factory.createParser(json);
        Object bean = new Person();
        ExternalTypeHandler handler = new ExternalTypeHandler(new Builder().build());
        handler.start();
        handler.addExternal(new SettableBeanProperty("name", NameDeserializer.class), new NameDeserializer());
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "age", bean);
        assertNull(handler.complete(jp, mapper.getDeserializationContext(), bean));
    }

    /**
     * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)}
     * when all properties have been handled.
     */
    @Test
    public void testComplete() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"name\": \"John\", \"type\": \"Person\", \"age\": 30, \"address\": {\"street\": \"Main St\", \"city\": \"Anytown\"}}";
        JsonParser jp = factory.createParser(json);
        Object bean = new Person();
        ExternalTypeHandler handler = new ExternalTypeHandler
                (new Builder()
                        .addExternal(new SettableBeanProperty("type", TypeDeserializer.class), new TypeDeserializer())
                        .addExternal(new SettableBeanProperty("age", AgeDeserializer.class), new AgeDeserializer())
                        .addExternal(new SettableBeanProperty("address.city"), CityDeserializer.class)
                        .build());
        handler.start();
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "type", bean);
        handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "type", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "age", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "address.city", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "address.street", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "name", bean);
        assertEquals("John", bean.getName());
        assertEquals(30, handler.complete(jp, mapper.getDeserializationContext(), bean).getAge());
        assertEquals("Anytown", handler.complete(jp, mapper.getDeserializationContext(), bean).getAddress().getCity());
        assertEquals("Main St", handler.complete(jp, mapper.getDeserializationContext(), bean).getAddress().getStreet());
        assertEquals("Person", handler.complete(jp, mapper.getDeserializationContext(), bean).getClass().getSimpleName());
    }

    /**
     * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)}
     * when some properties have not been handled.
     */
    @Test
    public void testComplete_unhandledProperties() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"name\": \"John\", \"type\": \"Person\", \"age\": 30, \"address\": {\"street\": \"Main St\", \"city\": \"Anytown\"}, \"unknown\": \"Unknown\"}";
        JsonParser jp = factory.createParser(json);
        Object bean = new Person();
        ExternalTypeHandler handler = new ExternalTypeHandler
                (new Builder()
                        .addExternal(new SettableBeanProperty("type", TypeDeserializer.class), new TypeDeserializer())
                        .addExternal(new SettableBeanProperty("age", AgeDeserializer.class), new AgeDeserializer())
                        .addExternal(new SettableBeanProperty("address.city"), CityDeserializer.class)
                        .build());
        handler.start();
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "type", bean);
        handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "type", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "age", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "address.city", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "address.street", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "name", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "unknown", bean); // should be ignored
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "name", bean); // should be replaced
        assertNull(handler.complete(jp, mapper.getDeserializationContext(), bean));
    }

    /**
     * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object, PropertyBasedCreator, PropertyValueBuffer)}
     * when all properties have been handled, including creator properties.
     */
    @Test
    public void testComplete_creatorProperties() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"name\": \"John\", \"type\": \"Person\", \"age\": 30, \"address\": {\"street\": \"Main St\", \"city\": \"Anytown\"}, \"creator\": {\"creatorName\": \" creator\"}}";
        JsonParser jp = factory.createParser(json);
        Object bean = new CreatorPerson();
        ExternalTypeHandler handler = new ExternalTypeHandler
                (new Builder()
                        .addExternal(new SettableBeanProperty("type", TypeDeserializer.class), new TypeDeserializer())
                        .addExternal(new SettableBeanProperty("age", AgeDeserializer.class), new AgeDeserializer())
                        .addExternal(new SettableBeanProperty("address.city"), CityDeserializer.class)
                        .addExternal(new SettableBeanProperty("creator.creatorName"), CreatorNameDeserializer.class)
                        .build());
        handler.start();
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "type", bean);
        handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "type", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "age", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "address.city", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "address.street", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "name", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "creator.creatorName", bean);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "creator.creatorName", bean); // should be replaced
        Object result = handler.complete(jp, mapper.getDeserializationContext(), bean, new CreatorPerson.Creator(), new PropertyValueBuffer());
        CreatorPerson expected = new CreatorPerson(" creator");
        expected.setName("John");
        expected.setAge(30);
        expected.setAddress(new Address("Anytown", "Main St"));
        assertEquals(expected, result);
    }

    /**
     * Test helper class for {@link ExternalTypeHandlerTest}.
     */
    static class Person {
        private String name;
        private int age;
        private Address address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    static class Address {
        private String city;
        private String street;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }

    static class CreatorPerson {
        private String name;
        private int age;
        private Address address;
        private Creator creator;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Creator getCreator() {
            return creator;
        }

        public void setCreator(Creator creator) {
            this.creator = creator;
        }

        public CreatorPerson(String creatorName) {
            this.creator = new Creator(creatorName);
        }

        static class Creator {
            private String creatorName;

            public Creator(String creatorName) {
                this.creatorName = creatorName;
            }

            public String getCreatorName() {
                return creatorName;
            }
        }
    }

    static class AgeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromObject(Object ctxt, JsonParser p, TypeDeserializer rhd) throws IOException {
            return p.getValueAsInt();
        }
    }

    static class CityDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromObject(Object ctxt, JsonParser p, TypeDeserializer rhd) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public void resolve(DeserializationContext ctxt, TypeDeserializerPropertyConfig config) throws JsonMappingException {
            config.setNullValueHandler(null);
        }
    }

    static class CreatorNameDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromObject(Object ctxt, JsonParser p, TypeDeserializer rhd) throws IOException {
            return p.getValueAsString();
        }
    }
}