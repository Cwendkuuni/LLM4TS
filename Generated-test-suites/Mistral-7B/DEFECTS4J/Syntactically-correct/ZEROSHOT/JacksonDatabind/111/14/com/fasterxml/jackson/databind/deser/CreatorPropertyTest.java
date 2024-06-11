package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testCreatorProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Class<?> clazz = TestCreatorProperty.class;
        JsonFactory jsonFactory = mapper.getFactory();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(clazz, new TestCreatorPropertyDeserializer());
        mapper.registerModule(module);

        String json = "{\"name\":\"John\", \"age\":30}";
        TestCreatorProperty obj = mapper.readValue(json, TestCreatorProperty.class);

        // Test basic properties
        JsonDeserializer<?> vd = obj.getValueDeserializer();
        assertNotNull(vd);
        JsonProperty p = obj.getAnnotation(JsonProperty.class);
        assertNotNull(p);
        assertEquals(0, obj.getCreatorIndex());
        assertEquals(TestCreatorProperty.class, obj.getType());
        assertNull(obj._fallbackSetter);
        assertFalse(obj.isIgnorable());

        // Test injection
        Object injectableValueId = new Object() {};
        CreatorProperty creatorProperty = new CreatorProperty(
                PropertyName.construct("name"), new TypeReference<>() {}, null, null,
                Annotations.empty(), null, 0, injectableValueId, null);
        obj = new TestCreatorProperty(clazz, creatorProperty);
        assertNotNull(injectableValueId);
        assertEquals(injectableValueId, obj.getInjectableValueId());

        DeserializationContext context = jsonFactory.createContext();
        Object beanInstance = new Object() {};
        Object injectedValue = context.findInjectableValue(injectableValueId, creatorProperty, beanInstance);
        assertNotNull(injectedValue);
        creatorProperty.inject(context, beanInstance);
        assertEquals(injectedValue, obj.getPropertyValue(beanInstance));

        // Test fallback setter
        creatorProperty._fallbackSetter = new SettableBeanProperty(
                PropertyName.construct("age"), new TypeReference<Integer>() {}, null, null,
                Annotations.empty(), null, 1, null, null);
        obj = new TestCreatorProperty(clazz, creatorProperty);
        settableBeanProperty fallbackSetter = obj.getFallbackSetter();
        assertNotNull(fallbackSetter);
        assertEquals(1, obj.getCreatorIndex());
        assertEquals(Integer.class, fallbackSetter.getType());

        // Test marking as ignorable
        creatorProperty.markAsIgnorable();
        obj = new TestCreatorProperty(clazz, creatorProperty);
        assertTrue(obj.isIgnorable());

        // Test deserialization and set
        JsonParser p = jsonFactory.createParser(json);
        obj.deserializeAndSet(p, context, beanInstance);
        assertEquals("John", obj.getPropertyValue(beanInstance));

        // Test deserialize and return
        Object deserializedValue = obj.deserializeSetAndReturn(p, context, beanInstance);
        assertNotNull(deserializedValue);
        assertEquals("John", deserializedValue);

        // Test set
        Object newValue = new Object() {};
        obj.set(beanInstance, newValue);
        assertEquals(newValue, obj.getPropertyValue(beanInstance));

        // Test set and return
        Object newValueReturn = obj.setAndReturn(beanInstance, newValue);
        assertNotNull(newValueReturn);
        assertEquals(newValue, newValueReturn);
    }

    @JsonCreator
    static class TestCreatorProperty {
        private final String name;
        private int age;

        @JsonProperty
        public String getName() {
            return name;
        }

        @JsonProperty
        public int getAge() {
            return age;
        }

        public TestCreatorProperty(Object id, String name, int age) {
            this.name = name;
            this.age = age;
        }

        public TestCreatorProperty(Class<?> clazz, CreatorProperty creatorProperty) throws IOException {
            this.name = creatorProperty.getName().getShortName();
            this.age = 30;
        }

        public Object getPropertyValue(Object instance) {
            TestCreatorProperty testCreatorProperty = (TestCreatorProperty) instance;
            return testCreatorProperty.name;
        }

        public void set(Object instance, Object value) throws IOException {
            TestCreatorProperty testCreatorProperty = (TestCreatorProperty) instance;
            testCreatorProperty.age = ((Number) value).intValue();
        }
    }

    static class TestCreatorPropertyDeserializer extends JsonDeserializer<TestCreatorProperty> {
        @Override
        public TestCreatorProperty deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            Object id = p.getValueAsToken();
            String name = p.nextTextValue();
            int age = p.readValueAs(Integer.class);
            return new TestCreatorProperty(TestCreatorProperty.class, null, name, age);
        }
    }
}