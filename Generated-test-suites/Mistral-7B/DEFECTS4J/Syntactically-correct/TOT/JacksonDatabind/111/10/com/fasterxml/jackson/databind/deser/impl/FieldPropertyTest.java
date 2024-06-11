package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;

public class FieldPropertyTest {

    static class TestClass {
        @JsonProperty("name")
        private String name;

        @JsonProperty("age")
        private int age;

        @JsonProperty("booleanFlag")
        private boolean booleanFlag;

        @JsonProperty("fieldProperty")
        private FieldProperty fieldProperty;
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass testClass = new TestClass();
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "John Doe");
        node.putNull("age");
        node.putBoolean("booleanFlag", false);
        node.putField("fieldProperty", mapper.createValueFactory().valueElement("value", new Object() {}));

        mapper.readValue(node, TestClass.class, TestClass.class);

        Assert.assertEquals("John Doe", testClass.getName());
        Assert.assertNull(testClass.getAge());
        Assert.assertFalse(testClass.isBooleanFlag());

        // Test setting null value
        JsonParser parser = mapper.getFactory().createParser(node);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FieldProperty property = testClass.getClass().getDeclaredField("fieldProperty").getAnnotation(FieldProperty.class);
        property.setNullProvider(ctxt.constructorArg(NullValueProvider.StandardProvider.instance()));
        testClass.setFieldProperty(null);
        testClass.deserializeAndSet(parser, ctxt, testClass);
        Assert.assertNull(testClass.getFieldProperty());

        // Test setting non-null value
        Object value = new Object();
        parser.nextValue();
        testClass.deserializeAndSet(parser, ctxt, testClass);
        Assert.assertNotNull(testClass.getFieldProperty());
        Assert.assertEquals(value, testClass.getFieldProperty().getValue());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass testClass = new TestClass();
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "John Doe");
        node.putNull("age");
        node.putBoolean("booleanFlag", false);
        node.putField("fieldProperty", mapper.createValueFactory().valueElement("value", new Object() {}));

        Object result = mapper.readValue(node, TestClass.class);

        Assert.assertEquals("John Doe", ((TestClass) result).getName());
        Assert.assertNull(((TestClass) result).getAge());
        Assert.assertFalse(((TestClass) result).isBooleanFlag());

        // Test setting null value
        JsonParser parser = mapper.getFactory().createParser(node);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FieldProperty property = TestClass.class.getDeclaredField("fieldProperty").getAnnotation(FieldProperty.class);
        property.setNullProvider(ctxt.constructorArg(NullValueProvider.StandardProvider.instance()));
        Object result2 = ((TestClass) result).setFieldProperty(null);
        Assert.assertNull(((TestClass) result2).getFieldProperty());

        // Test setting non-null value
        Object value = new Object();
        parser.nextValue();
        result2 = ((TestClass) result).setFieldProperty(value);
        Assert.assertNotNull(((TestClass) result2).getFieldProperty());
        Assert.assertEquals(value, ((TestClass) result2).getFieldProperty().getValue());
        Assert.assertEquals(result, result2);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass testClass = new TestClass();
        testClass.setName("John Doe");
        testClass.setAge(30);
        testClass.setBooleanFlag(true);
        testClass.setFieldProperty(new Object() {});

        ObjectNode node = mapper.writeValueAsObject(testClass);
        Assert.assertEquals("\"name\":\"John Doe\",\"age\":30,\"booleanFlag\":true,\"fieldProperty\":{}}", node.toString());
    }

    @Test
    public void testSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass testClass = new TestClass();
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "John Doe");
        node.putNull("age");
        node.putBoolean("booleanFlag", false);
        node.putField("fieldProperty", mapper.createValueFactory().valueElement("value", new Object() {}));

        TestClass result = mapper.readValue(node, TestClass.class);

        Object value = new Object();
        Field field = TestClass.class.getDeclaredField("fieldProperty");
        field.setAccessible(true);
        field.set(result, value);

        Assert.assertEquals(value, result.getFieldProperty().getValue());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass testClass = new TestClass();
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "John Doe");
        node.putNull("age");
        node.putBoolean("booleanFlag", false);
        node.putField("fieldProperty", mapper.createValueFactory().valueElement("value", new Object() {}));

        TestClass result = mapper.readValue(node, TestClass.class);

        Object value = new Object();
        Field field = TestClass.class.getDeclaredField("fieldProperty");
        field.setAccessible(true);
        Object result2 = field.set(result, value);

        Assert.assertEquals(value, result2);
        Assert.assertEquals(value, result.getFieldProperty().getValue());
        Assert.assertEquals(result, result2);
    }
}