package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class InnerClassPropertyTest {

    private static class TestBean {
        private InnerClass innerClass;

        public InnerClass getInnerClass() {
            return innerClass;
        }

        public void setInnerClass(InnerClass innerClass) {
            this.innerClass = innerClass;
        }

        public static class InnerClass {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Constructor<InnerClass> getInnerClassConstructor(Class<?> clazz) throws NoSuchMethodException {
        return (Constructor<InnerClass>) clazz.getDeclaredConstructor();
    }

    @SuppressWarnings("unchecked")
    private static AnnotatedConstructor<InnerClass> getAnnotatedConstructor(Class<?> clazz) throws NoSuchMethodException {
        return (AnnotatedConstructor<InnerClass>) clazz.getDeclaredConstructor().getAnnotation(Constructor.class);
    }

    @org.junit.Test
    public void testDefaultConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(
                "{ \"innerClass\": null}",
                TestBean.class);
        assertEquals(null, bean.getInnerClass());

        InnerClassProperty property = mapper.findProperty("innerClass", TestBean.class);
        assertEquals(SettableBeanProperty.NULL, property.getValue());

        ObjectNode node = mapper.createObjectNode();
        node.put("innerClass", null);
        bean = mapper.readValue(node, TestBean.class);
        assertEquals(null, bean.getInnerClass());
    }

    @org.junit.Test
    public void testConstructorWithAnnotations() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(
                "{ \"innerClass\": { \"class\": \"com.fasterxml.jackson.databind.deser.impl.InnerClassProperty$InnerClass\" } }",
                TestBean.class);
        InnerClassProperty property = mapper.findProperty("innerClass", TestBean.class);
        assertNotNull(property);

        AnnotatedConstructor<InnerClass> constructor = getAnnotatedConstructor(property.getClass().getDeclaredField("_creator").getType());
        assertEquals(constructor, property._annotated);
        assertSame(property._creator.getDeclaringClass(), constructor.getDeclaringClass());

        InnerClass innerClass = property.deserializeAndSet(
                mapper.parserForObjectMappings(),
                new DefaultSerializerProvider(),
                bean);
        assertNotNull(innerClass);
        assertEquals("InnerClassProperty$InnerClass", innerClass.getClass().getName());
    }

    @org.junit.Test
    public void testConstructorWithoutAnnotations() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(
                "{ \"innerClass\": { \"class\": \"com.fasterxml.jackson.databind.deser.impl.InnerClassProperty$InnerClass\" } }",
                TestBean.class);

        InnerClassProperty property = mapper.findProperty("innerClass", TestBean.class);
        assertNotNull(property);
        assertNull(property._annotated);
        assertNull(property._creator);

        Constructor<InnerClass> constructor = getInnerClassConstructor(property.getClass().getDeclaredField("_delegate").getType().getComponentType().getClass());
        InnerClass innerClass = constructor.newInstance(bean.getInnerClass());
        assertEquals("InnerClassProperty$InnerClass", innerClass.getClass().getName());

        property.deserializeAndSet(
                mapper.parserForObjectMappings(),
                new DefaultSerializerProvider(),
                bean);
        assertEquals("InnerClassProperty$InnerClass", bean.getInnerClass().getClass().getName());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMissingConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(
                "{ \"innerClass\": null}",
                TestBean.class);

        InnerClassProperty property = mapper.findProperty("innerClass", TestBean.class);
        assertNotNull(property);

        property.deserializeAndSet(
                mapper.parserForObjectMappings(),
                new DefaultSerializerProvider(),
                bean);
    }

    @org.junit.Test
    public void testSetValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(
                "{ \"innerClass\": { \"name\": \"TestValueDeserializer\" } }",
                TestBean.class);

        InnerClassProperty property = mapper.findProperty("innerClass", TestBean.class);
        assertNotNull(property);
        assertNull(property._annotated);
        assertNull(property._creator);

        JsonDeserializer<String> valueDeserializer = new StubValueDeserializer();
        property = new InnerClassProperty(property, valueDeserializer);

        InnerClass innerClass = property.deserializeAndSet(
                mapper.parserForObjectMappings(),
                new DefaultSerializerProvider(),
                bean);
        assertNotNull(innerClass);
        assertEquals("TestValueDeserializer", innerClass.getClass().getName());

        assertEquals("TestValueDeserializer", bean.getInnerClass().getClass().getName());
    }

    @org.junit.Test
    public void testSetName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(
                "{ \"innerClass\": { \"name\": \"TestNameProperty\" } }",
                TestBean.class);

        InnerClassProperty property = mapper.findProperty("innerClass", TestBean.class);
        assertNotNull(property);
        assertNull(property._annotated);
        assertNull(property._creator);

        PropertyName newName = PropertyName.construct("newName");
        property = new InnerClassProperty(property, newName);

        assertEquals("newName", property.getName().asString());
        assertEquals("innerClass", property.getOriginalName());
    }

    @org.junit.Test
    public void testTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(
                "{ \"innerClass\": { \"class\": \"com.fasterxml.jackson.databind.deser.impl.StubClassDeserializer\" } }",
                TestBean.class);

        InnerClassProperty property = mapper.findProperty("innerClass", TestBean.class);
        assertNotNull(property);
        assertNull(property._annotated);
        assertNull(property._creator);

        JsonDeserializer<StubClassDeserializer> typeDeserializer = new StubClassDeserializer();
        property = new InnerClassProperty(property, typeDeserializer);

        InnerClass innerClass = property.deserializeAndSet(
                mapper.parserForObjectMappings(),
                new DefaultSerializerProvider(),
                bean);
        assertNotNull(innerClass);
        assertEquals("StubClassDeserializer", innerClass.getClass().getName());

        assertEquals("StubClassDeserializer", bean.getInnerClass().getClass().getName());
    }

    private static class StubValueDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "TestValueDeserializer";
        }
    }

    private static class StubClassDeserializer extends StdDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }
}