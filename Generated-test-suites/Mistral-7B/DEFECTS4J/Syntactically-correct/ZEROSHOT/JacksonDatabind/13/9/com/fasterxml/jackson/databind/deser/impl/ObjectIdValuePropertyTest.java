package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    @JsonDeserialize(using = ObjectIdReader.class)
    static class TestPojo {
        private Object id;

        @JsonProperty("idProperty")
        private String idProperty;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getIdProperty() {
            return idProperty;
        }

        public void setIdProperty(String idProperty) {
            this.idProperty = idProperty;
        }
    }

    private static ObjectIdReader createObjectIdReader() {
        return new ObjectIdReader() {
            private static final long serialVersionUID = 1L;

            @Override
            public PropertyName getPropertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> getIdType() {
                return Object.class;
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator getIdGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getIdResolver() {
                return null;
            }

            @Override
            public String getIdPropertyName() {
                return "idProperty";
            }

            @Override
            public SettableBeanProperty getIdProperty() {
                return null;
            }
        };
    }

    @Test
    public void testBasicDeserialization() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(createObjectIdReader(), null);

        ObjectMapper mapper = new ObjectMapper();
        TestPojo pojo = mapper.readValue(ObjectNode.parse("{\"id\":\"123\"}"), TestPojo.class);
        assertEquals("123", pojo.getId().toString());
        assertEquals("123", pojo.getIdProperty());
    }

    @Test
    public void testDeserializationWithIdProperty() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(createObjectIdReader(), null);
        ObjectIdValueProperty idPropertyProperty = property.withName("idProperty");

        ObjectMapper mapper = new ObjectMapper();
        TestPojo pojo = mapper.readValue(ObjectNode.parse("{\"idProperty\":\"456\"}"), TestPojo.class);
        assertEquals("456", pojo.getId().toString());
        assertEquals("456", pojo.getIdProperty());
    }

    @Test
    public void testSetMethod() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(createObjectIdReader(), null);
        TestPojo pojo = new TestPojo();

        ObjectIdValueProperty idPropertyProperty = property.withName("idProperty");

        // set using setter method
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("id", "789");
        pojo = new ObjectMapper().readValue(node, TestPojo.class);
        idPropertyProperty.setAndReturn(pojo, "789");
        assertEquals("789", pojo.getId().toString());
        assertEquals("789", pojo.getIdProperty());

        // set using deserialization
        pojo = new ObjectMapper().readValue(node, TestPojo.class);
        idPropertyProperty.deserializationSetter.set(pojo, "789");
        assertEquals("789", pojo.getId().toString());
        assertEquals("789", pojo.getIdProperty());
    }

    @Test
    public void testAnnotation() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(createObjectIdReader(), null);
        TestPojo pojo = new TestPojo();
        AnnotationIntrospector introspector = new AnnotationIntrospector();

        ObjectIdValueProperty idPropertyProperty = property.withName("idProperty");

        // test annotation on property
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("idProperty", "111");
        introspector.findProperties(node, TestPojo.class, null);
        ObjectIdValueProperty annotatedProperty = (ObjectIdValueProperty) introspector.findProperty("idProperty", TestPojo.class);
        assertEquals("idProperty", annotatedProperty.getName());

        // test annotation on getter
        node.put("id", "222");
        introspector.findProperties(node, TestPojo.class, null);
        ObjectIdValueProperty annotatedGetter = (ObjectIdValueProperty) introspector.findGetter("getId", TestPojo.class);
        assertEquals("id", annotatedGetter.getName());

        // test annotation on setter
        node.put("id", "333");
        introspector.findProperties(node, TestPojo.class, null);
        ObjectIdValueProperty annotatedSetter = (ObjectIdValueProperty) introspector.findSetter("setId", TestPojo.class);
        assertEquals("id", annotatedSetter.getName());
    }
}