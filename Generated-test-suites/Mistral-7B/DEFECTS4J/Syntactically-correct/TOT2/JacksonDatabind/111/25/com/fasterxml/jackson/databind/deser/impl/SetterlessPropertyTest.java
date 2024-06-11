package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SetterlessPropertyTest {

    @org.junit.Test
    public void testSetterlessProperty_BasicUsage() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyTestClass instance = new MyTestClass();

        String json = "{\"someProperty\": [1, 2, 3]}";
        ObjectNode node = mapper.readTree(json);

        SetterlessProperty property = mapper.findDeser(SetterlessProperty.class, MyTestClass.class, "someProperty");
        property.deserializeAndSet(node, mapper.getDeserializationContext(), instance);

        assertEquals(3, instance.getSomeProperty().size());
    }

    @org.junit.Test
    public void testSetterlessProperty_NullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyTestClass instance = new MyTestClass();

        String json = "{\"someProperty\": null}";
        ObjectNode node = mapper.readTree(json);

        SetterlessProperty property = mapper.findDeser(SetterlessProperty.class, MyTestClass.class, "someProperty");
        property.deserializeAndSet(node, mapper.getDeserializationContext(), instance);

        assertNotNull(instance.getSomeProperty());
        assertEquals(0, instance.getSomeProperty().size());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetterlessProperty_SetMethodCall() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyTestClass instance = new MyTestClass();

        String json = "{\"someProperty\": [1, 2, 3]}";
        ObjectNode node = mapper.readTree(json);

        SetterlessProperty property = mapper.findDeser(SetterlessProperty.class, MyTestClass.class, "someProperty");
        property.set(instance, new Object()); // This should not be called
    }

    // Add edge cases and exception handling scenarios here as needed
}

class MyTestClass {
    private List someProperty;

    public List getSomeProperty() {
        return someProperty;
    }
}