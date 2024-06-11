package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.SimpleType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class SettableAnyPropertyTest {

    private static final String TEST_JSON = "{\"class\":\"TestClass\",\"anyProperty\":{\"name\":\"value\",\"anotherProperty\":123}}";

    @Test
    public void testSettableAnyProperty() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = new TypeFactory().constructType(TestClass.class);
        ObjectMapper objectMapper = new ObjectMapper();

        BeanProperty property = new BeanPropertyConfigurer()
                .getName("anyProperty")
                .get(TestClass.class);

        AnnotatedMember setter = property.getMember();
        JavaType type = SimpleType.constructUnresolved(Map.class, Object.class, Object.class);

        KeyDeserializer keyDeserializer = new FromStringDeserializer();
        JsonDeserializer<Object> valueDeserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object deserializedObject = objectMapper.readValue(TEST_JSON, settableAnyProperty.getType());

        Map<Object, Object> map = (Map<Object, Object>) deserializedObject;
        assertEquals(map.get("name"), "value");
        assertEquals(map.get("anotherProperty"), 123);

        settableAnyProperty.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), deserializedObject, "anyProperty.name");
        assertEquals(map.get("name"), "updatedValue");
    }

    @Test
    public void testSettableAnyPropertyWithTypeDeserializer() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = new TypeFactory().constructType(TestClass.class);

        BeanProperty property = new BeanPropertyConfigurer()
                .getName("anyProperty")
                .get(TestClass.class);

        AnnotatedMember setter = property.getMember();
        JavaType type = SimpleType.constructUnresolved(Map.class, Object.class, SimpleType.constructUnresolved(String.class));

        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new Base64StringDeserializer(Base64Variants.STANDARD);
        TypeDeserializer typeDeserializer = new TestStringTypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object deserializedObject = objectMapper.readValue(TEST_JSON, settableAnyProperty.getType());

        Map<Object, Object> map = (Map<Object, Object>) deserializedObject;
        assertEquals(map.get("name"), "value");
        assertEquals(map.get("anotherProperty"), 123);

        settableAnyProperty.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), deserializedObject, "anyProperty.name");
        assertEquals(map.get("name"), "updatedValue");

        assertEquals(map.get("anotherProperty"), "decodedValue");
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = new TypeFactory().constructType(TestClass.class);

        BeanProperty property = new BeanPropertyConfigurer()
                .getName("anyProperty")
                .get(TestClass.class);

        AnnotatedMember setter = property.getMember();
        JavaType type = SimpleType.constructUnresolved(Map.class, Object.class, Object.class);

        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new FromIntDeserializer();
        TypeDeserializer typeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object deserializedObject = objectMapper.readValue(TEST_JSON, settableAnyProperty.getType());

        Map<Object, Object> map = (Map<Object, Object>) deserializedObject;
        assertEquals(map.get("name"), "value");
        assertEquals(map.get("anotherProperty"), 123);

        settableAnyProperty.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), deserializedObject, "anyProperty.name");
        assertEquals(map.get("name"), 456);
    }

    @Test
    public void testSettableAnyPropertyDependency() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = new TypeFactory().constructType(TestClass.class);

        BeanProperty property1 = new BeanPropertyConfigurer()
                .getName("property1")
                .get(TestClass.class);
        BeanProperty property2 = new BeanPropertyConfigurer()
                .getName("property2")
                .get(TestClass.class);

        AnnotatedMember setter1 = property1.getMember();
        AnnotatedMember setter2 = property2.getMember();
        JavaType type = SimpleType.constructUnresolved(TestClass.class);

        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer typeDeserializer = null;

        SettableAnyProperty settableAnyProperty1 = new SettableAnyProperty(property1, setter1, javaType, keyDeserializer, valueDeserializer, typeDeserializer);
        SettableAnyProperty settableAnyProperty2 = new SettableAnyProperty(property2, setter2, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object deserializedObject = objectMapper.readValue(TEST_JSON, TestClass.class);

        TestClass testClass = (TestClass) deserializedObject;
        assertEquals(testClass.getProperty1(), "value1");
        assertEquals(testClass.getProperty2(), 123);

        settableAnyProperty1.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), deserializedObject, "property1.name");
        settableAnyProperty2.deserializeAndSet(mockJsonParser(), mockDeserializationContext(), deserializedObject, "property2.name");

        assertEquals(testClass.getProperty1(), "updatedValue1");
        assertEquals(testClass.getProperty2(), 456);
    }

    @Test
    public void testSettableAnyPropertyPropertyIsField() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = new TypeFactory().constructType(TestClass.class);

        BeanProperty property = new BeanPropertyConfigurer()
                .getName("anyProperty")
                .get(TestClass.class);

        AnnotatedField setter = (AnnotatedField) property.getFqn().getMember();
        JavaType type = SimpleType.constructUnresolved(Map.class, Object.class, Object.class);

        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object deserializedObject = new TestClass();

        Map<Object, Object> map = (Map<Object, Object>) settableAnyProperty.deserialize(mockJsonParser(), mockDeserializationContext());
        settableAnyProperty.set(deserializedObject, null, map);

        assertEquals(((TestClass) deserializedObject).getAnyProperty(), map);
    }

    @Test
    public void testSettableAnyPropertyPropertyIsMethod() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = new TypeFactory().constructType(TestClass.class);

        BeanProperty property = new BeanPropertyConfigurer()
                .getName("anyProperty")
                .get(TestClass.class);

        AnnotatedMethod setter = (AnnotatedMethod) property.getFqn().getMember();
        JavaType type = SimpleType.constructUnresolved(Map.class, Object.class, Object.class);

        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object deserializedObject = new TestClass();

        Map<Object, Object> map = (Map<Object, Object>) settableAnyProperty.deserialize(mockJsonParser(), mockDeserializationContext());
        settableAnyProperty.set(deserializedObject, "anyProperty", map);

        assertEquals(((TestClass) deserializedObject).getAnyProperty(), map);
    }

    @BeforeMethod
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @AfterMethod
    public void tearDown() {
        // empty
    }

    private JsonParser mockJsonParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext mockDeserializationContext() {
        return mock(DeserializationContext.class);
    }

    private static class TestStringTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromObject(Object beanProperty, JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return p.getValueAsString();
        }
    }

    private static class FromIntDeserializer extends JsonDeserializer<Number> {
        @Override
        public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return (Number) Long.valueOf(p.getValueAsInt());
        }
    }
}