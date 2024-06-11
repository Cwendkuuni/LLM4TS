package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to test {@link SettableAnyProperty} functionality.
 */
public class SettableAnyPropertyTest {

    private Object testInstance;
    private Map<String, Object> testMap;

    @Before
    public void setUp() {
        testInstance = new Object();
        testMap = new HashMap<>();
    }

    @Test
    public void testSettableAnyPropertyConstruction() {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedField setter = new AnnotatedField(TestClass.class, "testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        Assert.assertNotNull(settableAnyProperty);
        Assert.assertEquals(property, settableAnyProperty.getProperty());
        Assert.assertEquals(setter, settableAnyProperty._setter);
        Assert.assertEquals(type, settableAnyProperty.getType());
        Assert.assertNull(settableAnyProperty._valueDeserializer);
        Assert.assertNull(settableAnyProperty._valueTypeDeserializer);
        Assert.assertNull(settableAnyProperty._keyDeserializer);
        Assert.assertFalse(settableAnyProperty._setterIsField);
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedField setter = new AnnotatedField(TestClass.class, "testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(new IntDeserializer());

        Assert.assertNotNull(settableAnyProperty);
        Assert.assertEquals(property, settableAnyProperty.getProperty());
        Assert.assertEquals(setter, settableAnyProperty._setter);
        Assert.assertEquals(type, settableAnyProperty.getType());
        Assert.assertNull(settableAnyProperty._valueDeserializer);
        Assert.assertNull(settableAnyProperty._valueTypeDeserializer);
        Assert.assertNull(settableAnyProperty._keyDeserializer);
        Assert.assertFalse(settableAnyProperty._setterIsField);

        Assert.assertNotNull(settableAnyPropertyWithValueDeserializer);
        Assert.assertEquals(property, settableAnyPropertyWithValueDeserializer.getProperty());
        Assert.assertEquals(setter, settableAnyPropertyWithValueDeserializer._setter);
        Assert.assertEquals(type, settableAnyPropertyWithValueDeserializer.getType());
        Assert.assertNotNull(settableAnyPropertyWithValueDeserializer._valueDeserializer);
        Assert.assertNull(settableAnyPropertyWithValueDeserializer._valueTypeDeserializer);
        Assert.assertNull(settableAnyPropertyWithValueDeserializer._keyDeserializer);
        Assert.assertFalse(settableAnyPropertyWithValueDeserializer._setterIsField);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedField setter = new AnnotatedField(TestClass.class, "testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": \"testValue\"}");

        settableAnyProperty.deserializeAndSet(jsonParser, null, testInstance, "testProperty");

        Assert.assertEquals("testValue", testMap.get("testProperty"));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithValueDeserializer() throws IOException {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedField setter = new AnnotatedField(TestClass.class, "testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = new NumberDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": 42}");

        settableAnyProperty.deserializeAndSet(jsonParser, null, testInstance, "testProperty");

        // The test should fail because the value is an integer, but the setter expects a String.
    }

    @Test
    public void testReadResolve() {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedField setter = new AnnotatedField(TestClass.class, "testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object objectToSerialize = settableAnyProperty;
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        objectMapper.writeValue(jsonGenerator, objectToSerialize);
        jsonGenerator.close();

        Object deserializedObject = objectMapper.readValue(new ByteArrayInputStream(objectMapper.writeValueAsBytes(objectToSerialize)), TestClass.class);

        SettableAnyProperty deserializedSettableAnyProperty = (SettableAnyProperty) deserializedObject;
        Assert.assertEquals(settableAnyProperty.getProperty(), deserializedSettableAnyProperty.getProperty());
        Assert.assertEquals(settableAnyProperty._setter, deserializedSettableAnyProperty._setter);
        Assert.assertEquals(settableAnyProperty.getType(), deserializedSettableAnyProperty.getType());
        Assert.assertEquals(settableAnyProperty._valueDeserializer, deserializedSettableAnyProperty._valueDeserializer);
        Assert.assertEquals(settableAnyProperty._valueTypeDeserializer, deserializedSettableAnyProperty._valueTypeDeserializer);
        Assert.assertEquals(settableAnyProperty._keyDeserializer, deserializedSettableAnyProperty._keyDeserializer);
        Assert.assertEquals(settableAnyProperty._setterIsField, deserializedSettableAnyProperty._setterIsField);
    }

    @Test
    public void testToString() {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedField setter = new AnnotatedField(TestClass.class, "testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Assert.assertEquals("[any property on class com.fasterxml.jackson.databind.deser.test.TestClass]", settableAnyProperty.toString());
    }

    private static class TestClass {

        @JsonProperty("testProperty")
        private Map<String, Object> testMap;

        public Map<String, Object> getTestMap() {
            return testMap;
        }

        public void setTestMap(Map<String, Object> testMap) {
            this.testMap = testMap;
        }
    }

    private static class StringDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class IntDeserializer extends JsonDeserializer<Integer> {

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }
    }

    private static class NumberDeserializer extends JsonDeserializer<Number> {

        @Override
        public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsNumber();
        }
    }
}