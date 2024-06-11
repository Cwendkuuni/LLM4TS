package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CreatorPropertyTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    private CreatorProperty property;
    private BeanDescription description;

    @Before
    public void setUp() throws Exception {
        Class<?> testClass = TestClass.class;
        description = mapper.findClassDescriptors(testClass, null)[0];
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        Object instance = testClass.newInstance();

        property = introspect(description, "value", null, null, 0, null, null);
    }

    @Test
    public void testBasicProperties() {
        assertEquals("value", property.getName().getShortName());
        assertEquals(String.class, property.getType().getType());
        assertNull(property._annotated);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(-1, property._creatorIndex);
        assertFalse(property.isIgnorable());
    }

    @Test
    public void testConstructorProperties() throws IOException {
        Object constructorInstance = TestClass.createWithValue("constructorValue");

        property = introspect(description, "value", null, null, 0, null, null, constructorInstance);

        assertSame(constructorInstance, property._annotated.getExecutable());
        assertEquals(0, property._creatorIndex);
        assertTrue(property.isIgnorable());
    }

    @Test
    public void testInjectableProperties() throws IOException {
        TestClass testClass = new TestClass("injectValue");
        Object injectInstance = testClass;

        property = introspect(description, "injectValue", null, null, 0, null, null, injectInstance);
        Object injectedValue = new Object();

        DeserializationContext ctxt = mapper.getDeserializationContext();
        property.inject(ctxt, injectInstance);

        assertEquals(injectedValue, testClass.getInjectValue());
        assertNull(property._annotated);
        assertNotNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(-1, property._creatorIndex);
        assertTrue(property.isIgnorable());
    }

    @Test
    public void testSetterProperties() throws IOException, IllegalAccessException, InvocationTargetException {
        TestClass testClass = new TestClass("setterValue");

        Method setterMethod = TestClass.class.getMethod("setSetterValue", String.class);
        PropertyName wrapperName = PropertyName.construct("setterValueWrapper", null);
        TypeDeserializer typeDeser = null;

        property = new CreatorProperty(
                PropertyName.construct("setterValue"),
                String.class,
                wrapperName,
                typeDeser,
                Annotations.empty(),
                new AnnotatedParameter(setterMethod, null),
                0,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        Object instance = testClass.newInstance();
        String testValue = "setterValue";

        property.setFallbackSetter(
                new CreatorProperty(
                        PropertyName.construct("setterValue"),
                        String.class,
                        wrapperName,
                        typeDeser,
                        Annotations.empty(),
                        new AnnotatedParameter(setterMethod, new Object[]{testValue}),
                        0,
                        null,
                        PropertyMetadata.STANDARD_EMPTY)
        );

        JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString(testValue));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        property.deserializeAndSet(p, ctxt, instance);

        assertEquals(testValue, testClass.getSetterValue());
        assertNotNull(property._annotated);
        assertNull(property._injectableValueId);
        assertNotNull(property._fallbackSetter);
        assertEquals(0, property._creatorIndex);
        assertFalse(property.isIgnorable());
    }
}

class TestClass {

    @JsonProperty
    private String value;

    @JsonProperty
    private String injectValue;

    @JsonCreator
    public TestClass(@JsonProperty("value") String value) {
        this.value = value;
    }

    private String setterValue;

    public void setSetterValue(String value) {
        this.setterValue = value;
    }

    public String getValue() {
        return value;
    }

    public String getInjectValue() {
        return injectValue;
    }

    public String getSetterValue() {
        return setterValue;
    }

    public TestClass(String injectValue) {
        this.injectValue = injectValue;
    }

    public TestClass() {
    }

    static TestClass createWithValue(String value) {
        return new TestClass(value);
    }
}