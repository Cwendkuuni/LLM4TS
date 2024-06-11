package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for SetterlessProperty
 */
public class SetterlessPropertyTest {

    /**
     * Test class with a setterless property
     */
    static class TestClass {
        private static final long serialVersionUID = 1L;

        @JsonProperty("testProperty")
        private SetterlessProperty testProperty;

        public Object getTestProperty() {
            return testProperty.getGetter().invoke(this);
        }

        public void setTestProperty(Object value) throws IllegalAccessException, InvocationTargetException {
            testProperty.getSetter().invoke(this, value);
        }
    }

    /**
     * Test method for deserializing a null value
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";

        TestClass instance = mapper.readValue(json, TestClass.class);
        assertNull(instance.getTestProperty());
    }

    /**
     * Test method for deserializing a non-null value
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"testProperty\": [1, 2, 3]}";

        TestClass instance = mapper.readValue(json, TestClass.class);
        Object testProperty = instance.getTestProperty();

        // Check that the deserialized value is correct
        List list = (List) testProperty;
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    /**
     * Test method for setting a value
     *
     * @throws IOException
     */
    @Test
    public void testSetValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass instance = mapper.readValue("{}", TestClass.class);

        List list = new ArrayList();
        list.add("test");

        // Set the value
        instance.setTestProperty(list);

        // Check that the value was set correctly
        Object testProperty = instance.getTestProperty();
        List setTestProperty = (List) testProperty;
        assertEquals(1, setTestProperty.size());
        assertEquals("test", setTestProperty.get(0));
    }

    /**
     * Test method for deserializing a type annotated property
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeTypeAnnotated() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"testProperty\": [1, 2, 3]}";

        TestClass instance = mapper.readValue(json, TestClass.class);
        Object testProperty = instance.getTestProperty();

        // Check that the deserialized value is an instance of the expected type
        List<String> list = (List<String>) testProperty;
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));

        // Check that the deserialized type is correct
        BeanDescription beanDescription = mapper.getSerializationConfig().findBeanDescription(TestClass.class, null);
        BeanPropertyDefinition testPropertyDefinition = beanDescription.findPropertyDefinition("testProperty");
        TypeDeserializer typeDeserializer = testPropertyDefinition.getChildTypeDeserializer();
        assertEquals(String[].class, typeDeserializer.getType(null));
    }

    /**
     * Test method for fixing access
     *
     * @throws IOException
     */
    @Test
    public void testFixAccess() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass instance = mapper.readValue("{@Test(access=PRIVATE) testProperty: [1, 2, 3]}", TestClass.class);

        // Check that the property is accessible
        assertTrue(instance.getClass().isAccessible());
        assertTrue(instance.getClass().getDeclaredField("testProperty").isAccessible());
        assertTrue(instance.getClass().getDeclaredMethod("getTestProperty").isAccessible());
        assertTrue(instance.getClass().getDeclaredMethod("setTestProperty", Object.class).isAccessible());

        // Test deserializing the property
        Object testProperty = instance.getTestProperty();
        List list = (List) testProperty;
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));

        // Test setting a value
        List<String> setValue = new ArrayList<>();
        setValue.add("test");

        AccessibleObject setter = instance.getClass().getDeclaredMethod("setTestProperty", Object.class).getAccessibleObject();
        setter.setAccessible(true);
        setter.invoke(instance, setValue);

        testProperty = instance.getTestProperty();
        List setTestProperty = (List) testProperty;
        assertEquals(1, setTestProperty.size());
        assertEquals("test", setTestProperty.get(0));
    }
}