package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest extends TestBase {

    private ObjectMapper mapper;
    private AnnotatedClass annotatedClass;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        annotatedClass = mapper.getTypeFactory().constructType(TestClass.class);
    }

    @Test
    public void testCreatorPropertyDefault() throws Exception {
        CreatorProperty property = findCreatorProperty(annotatedClass, "stringProp");
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertFalse(property.isIgnorable());
    }

    @Test
    public void testInjectableValueId() throws Exception {
        Object injectableValue = new Object();
        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        when(parameter.getId()).thenReturn("injectableValueId");
        CreatorProperty property = new CreatorProperty(
                null, "stringProp", null, null, null, parameter, 0, injectableValue, null);
        assertEquals("injectableValueId", property.getInjectableValueId());
    }

    @Test
    public void testFallbackSetter() throws Exception {
        AnnotatedField field = mock(AnnotatedField.class);
        when(field.getName()).thenReturn("fallbackSetter");
        CreatorProperty property = new CreatorProperty(
                null, "stringProp", null, null, null, null, 0, null, field);
        assertSame(field, property._fallbackSetter);
    }

    @Test
    public void testIgnorable() throws Exception {
        CreatorProperty property = new CreatorProperty(
                null, "stringProp", null, null, null, null, 0, null, null);
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetFallbackSetterAfterConstruction() throws Exception {
        CreatorProperty property = new CreatorProperty(
                null, "stringProp", null, null, null, null, 0, null, null);
        property.setFallbackSetter(null);
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        Object bean = new TestClass();
        CreatorProperty property = findCreatorProperty(annotatedClass, "intProp");
        Object injectedValue = new Object();
        when(mapper.findInjectableValue(eq(property.getInjectableValueId()), eq(bean), anyObject()))
                .thenReturn(injectedValue);
        assertEquals(injectedValue, property.findInjectableValue(mapper, bean));
    }

    @Test
    public void testInject() throws Exception {
        Object bean = new TestClass();
        CreatorProperty property = findCreatorProperty(annotatedClass, "intProp");
        Object injectedValue = new Object();
        when(mapper.findInjectableValue(eq(property.getInjectableValueId()), eq(bean), anyObject()))
                .thenReturn(injectedValue);
        property.inject(mapper, bean);
        assertEquals(injectedValue, property.get(bean));
    }

    @Test(expected = IOException.class)
    public void testInjectFailure() throws Exception {
        Object bean = new TestClass();
        CreatorProperty property = findCreatorProperty(annotatedClass, "intProp");
        when(mapper.findInjectableValue(eq(property.getInjectableValueId()), eq(bean), anyObject()))
                .thenThrow(new IOException("Test error"));
        property.inject(mapper, bean);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object bean = new TestClass();
        CreatorProperty property = findCreatorProperty(annotatedClass, "intProp");
        String json = "{\"intProp\":1}";
        Object deserializedValue = mapper.readValue(json, Integer.class);
        property.deserializeAndSet(mapper.getFactory().createParser(json), mapper, bean);
        assertEquals(deserializedValue, property.get(bean));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object bean = new TestClass();
        CreatorProperty property = findCreatorProperty(annotatedClass, "intProp");
        String json = "{\"intProp\":1}";
        Object deserializedValue = mapper.readValue(json, Integer.class);
        assertEquals(deserializedValue, property.deserializeSetAndReturn(mapper.getFactory().createParser(json), mapper, bean));
    }

    @Test
    public void testSet() throws Exception {
        Object bean = new TestClass();
        CreatorProperty property = findCreatorProperty(annotatedClass, "intProp");
        int value = 1;
        property.set(bean, value);
        assertEquals(value, property.get(bean));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object bean = new TestClass();
        CreatorProperty property = findCreatorProperty(annotatedClass, "intProp");
        int value = 1;
        assertEquals(value, property.setAndReturn(bean, value));
        assertEquals(value, property.get(bean));
    }

    @Test
    public void testToString() throws Exception {
        CreatorProperty property = findCreatorProperty(annotatedClass, "stringProp");
        String expected = "[creator property, name 'stringProp'; inject id 'null']";
        assertEquals(expected, property.toString());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws Exception {
        CreatorProperty property = findCreatorProperty(annotatedClass, "missingSetter");
        property._verifySetter();
    }

    private CreatorProperty findCreatorProperty(AnnotatedClass ac, String name) throws Exception {
        return (CreatorProperty) ac.findProperty(name);
    }

    @JsonCreator
    static class TestClass {

        @JsonProperty
        CreatorProperty intProp;

        @JsonProperty
        CreatorProperty stringProp;

        @JsonProperty(value = "missingSetter")
        CreatorProperty missingSetter;

        @JsonProperty(value = "ignoredProp")
        @JsonIgnore
        CreatorProperty ignoredProp;

        @JsonProperty
        static CreatorProperty creatorProp;

        public TestClass() {
            creatorProp = new CreatorProperty(
                    null, "creatorProp", null, null, null, null, 0, null, null);
        }

    }

}