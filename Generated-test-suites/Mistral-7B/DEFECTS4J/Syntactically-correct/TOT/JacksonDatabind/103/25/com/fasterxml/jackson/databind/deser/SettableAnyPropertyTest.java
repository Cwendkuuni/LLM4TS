package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class SettableAnyPropertyTest {

    private Object testObject;
    private SettableAnyProperty settableAnyProperty;

    @BeforeMethod
    public void setUp() {
        testObject = new Object() {{
            @SuppressWarnings("unchecked")
            Map<String, Object> map = new HashMap<>();
            put("name", "John Doe");
        }};
        settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(getClass(), "map"), null),
                new AnnotatedField(testObject.getClass(), "map"),
                JsonTypeFactory.valueType(Map.class),
                null,
                null,
                null);
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSet() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"name\":\"Jane Doe\",\"age\":30}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        settableAnyProperty.deserializeAndSet(jsonParser, new DeserializationContext(), testObject, "age");

        assertEquals(testObject.getClass().getDeclaredField("map").get(testObject), Map.of("age", 30));
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetWithKeyDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerForType(Map.class);
        String json = "{\"key\":\"age\",\"value\":30}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        KeyDeserializer keyDeserializer = new StandardKeyDeserializer();

        settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(getClass(), "map"), null),
                new AnnotatedField(testObject.getClass(), "map"),
                JsonTypeFactory.valueType(Map.class),
                keyDeserializer,
                null,
                null);

        settableAnyProperty.deserializeAndSet(jsonParser, new DeserializationContext(), testObject, null);

        assertEquals(testObject.getClass().getDeclaredField("map").get(testObject), Map.of("age", 30));
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetWithValueDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"name\":\"John Doe\",\"age\":{\"number\":30,\"string\":\"Thirty\"}}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        JsonDeserializer<Object> valueDeserializer = new SimpleValueDeserializer();
        settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(getClass(), "map"), null),
                new AnnotatedField(testObject.getClass(), "map"),
                JsonTypeFactory.valueType(Map.class),
                null,
                valueDeserializer,
                null);

        settableAnyProperty.deserializeAndSet(jsonParser, new DeserializationContext(), testObject, "age");

        assertEquals(testObject.getClass().getDeclaredField("map").get(testObject), Map.of("age", Map.of("number", 30, "string", "Thirty")));
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetWithTypeDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"name\":\"John Doe\",\"age\":{\"class\":\"java.util.Date\",\"year\":1990,\"month\":1,\"day\":1}}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        TypeDeserializer valueTypeDeserializer = new StdDateDeserializer();
        settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(getClass(), "map"), null),
                new AnnotatedField(testObject.getClass(), "map"),
                JsonTypeFactory.valueType(Map.class),
                null,
                null,
                valueTypeDeserializer);

        settableAnyProperty.deserializeAndSet(jsonParser, new DeserializationContext(), testObject, "age");

        assertEquals(testObject.getClass().getDeclaredField("map").get(testObject), Map.of("age", new Date(1990, 0, 1)));
    }

    @Test
    public void testSettableAnyPropertyDeserializeNullValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"name\":\"John Doe\",\"age\":null}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(getClass(), "map"), null),
                new AnnotatedField(testObject.getClass(), "map"),
                JsonTypeFactory.valueType(Map.class),
                null,
                null,
                null);

        settableAnyProperty.deserializeAndSet(jsonParser, new DeserializationContext(), testObject, "age");

        assertNull(testObject.getClass().getDeclaredField("map").get(testObject));
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeInvalidValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"name\":\"John Doe\",\"age\":[1,2,3]}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(getClass(), "map"), null),
                new AnnotatedField(testObject.getClass(), "map"),
                JsonTypeFactory.valueType(Map.class),
                null,
                null,
                null);

        settableAnyProperty.deserializeAndSet(jsonParser, new DeserializationContext(), testObject, "age");
    }
}