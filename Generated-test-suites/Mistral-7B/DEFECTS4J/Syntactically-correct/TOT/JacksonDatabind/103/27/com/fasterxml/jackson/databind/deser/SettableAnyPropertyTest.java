package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.test.BaseMapTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class SettableAnyPropertyTest extends BaseMapTest {

    private static final String TEST_CLASS_NAME = "com.example.TestClass";
    private static final String TEST_FIELD = "testField";
    private static final String TEST_VALUE = "testValue";

    // Create a test class with a settable any property
    public static class TestClass {
        private Map<String, Object> _testMap;

        @JsonAnySetter
        public void setTestMap(Map<String, Object> testMap) {
            _testMap = testMap;
        }
    }

    @BeforeClass
    public void setup() throws Exception {
        addMixInAnnotations(TestClass.class, SettableAnyProperty.class);
    }

    @Test
    public void testSettableAnyPropertyWithValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Set the value using the settable any property
        TestClass testClass = mapper.readValue(mapper.writeValueAsString(new ObjectNode().put("test", TEST_VALUE)), TestClass.class);
        assertEquals(testClass._testMap.get("test"), TEST_VALUE);
    }

    @Test
    public void testSettableAnyPropertyWithKeyDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("key", "test");

        // Use a custom key deserializer to set the key
        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("value", TEST_VALUE);

        JsonFactory factory = new JsonFactory();
        StandardKeyDeserializer keyDeserializer = new StandardKeyDeserializer();
        keyDeserializer.setContentIntrinsics(true);

        JsonParser keyParser = factory.createParser(testNode);
        JsonParser valueParser = factory.createParser(rootNode);

        SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty("_default", TestClass.class, null, null, keyDeserializer, null);

        settableAnyProperty.deserializeAndSet(valueParser, null, null, "_default");

        TestClass testClass = mapper.readValue(rootNode, TestClass.class);
        assertEquals(testClass._testMap.get("test"), TEST_VALUE);
    }

    @Test
    public void testSettableAnyPropertyWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Use a custom type deserializer to set the value type
        JsonDeserializer<Integer> intDeserializer = new StdSerializer<Integer>(Integer.class) {};
        TypeDeserializer intTypeDeserializer = mapper.findValueDeserializer(Integer.class, null);
        intTypeDeserializer.setAnnotationIntrospector(mapper.getAnnotationIntrospector());
        intTypeDeserializer.setAnnotationProcessor(mapper.getAnnotationProcessor());

        TestClass testClass = mapper.readValue(mapper.writeValueAsString(new ObjectNode().putPOJO("test", new Integer(123))), TestClass.class);
        assertEquals(testClass._testMap.get("test"), 123);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSettableAnyPropertyWithNullKey() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        TestClass testClass = mapper.readValue(mapper.writeValueAsString(new ObjectNode()), TestClass.class);
        testClass.setTestMap(null);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.putNull("test");

        SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty("test", TestClass.class, null, null, null, null);

        try {
            settableAnyProperty.deserializeAndSet(null, null, null, "test");
            fail();
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSettableAnyPropertyWithInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        TestClass testClass = mapper.readValue(mapper.writeValueAsString(new ObjectNode()), TestClass.class);
        testClass.setTestMap(null);

        ObjectNode rootNode = mapper.createObjectNode();

        try {
            SettableAnyProperty settableAnyProperty = mapper.findSettableAnyProperty("test", TestClass.class, null, null, null, null);
            settableAnyProperty.deserializeAndSet(null, null, null, "test");
            fail();
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }
}