package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TestUtil;

public class InnerClassPropertyTest extends TestBase {

    private static class TestBean {
        private InnerClassInner inner;

        public InnerClassInner getInner() {
            return inner;
        }

        public void setInner(InnerClassInner value) {
            this.inner = value;
        }
    }

    private static class InnerClassInner {
        private int value;

        public InnerClassInner(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private BeanPropertyDefinition innerProperty;
    private InnerClassProperty innerPropertyInstance;

    @Before
    public void setup() throws Exception {
        JsonDeserializer<InnerClassInner> deserializer = new StdDeserializer<InnerClassInner>(InnerClassInner.class) {
            @Override
            public InnerClassInner deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return new InnerClassInner(p.getIntValue());
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        innerProperty = mapper.findProperty("inner", TestBean.class);
        innerPropertyInstance = new InnerClassProperty(null, innerProperty, new AnnotatedConstructor(InnerClassInner.class
                .getDeclaredConstructor(int.class)), deserializer);
    }

    @Test
    public void testConstructor() {
        assertNotNull(innerPropertyInstance);
        assertNotNull(innerPropertyInstance._delegate);
        assertNull(innerPropertyInstance._creator);
        assertNull(innerPropertyInstance._annotated);
    }

    @Test
    public void testSetter() throws IOException {
        TestBean testBean = new TestBean();
        Object value = new Object();

        innerPropertyInstance.setValue(testBean, value);
        assertNotNull(testBean.getInner());

        testBean.setInner(null);
        innerPropertyInstance.set(testBean, null);
        assertNull(testBean.getInner());
    }

    @Test
    public void testDeserialization() throws IOException {
        TestBean testBean = new TestBean();
        ObjectMapper mapper = new ObjectMapper();
        String json = TestUtil.writeValueAsString(new JsonFactory(), testBean);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object readValue = mapper.readValue(jsonParser, TestBean.class);
        InnerClassInner inner = readValue.getClass().getDeclaredField("inner").get(readValue);

        assertNotNull(inner);
        assertEquals(42, inner.getValue());
    }

    @Test
    public void testSerialization() throws IOException {
        TestBean testBean = new TestBean();
        ObjectMapper mapper = new ObjectMapper();

        testBean.setInner(new InnerClassInner(42));
        String json = mapper.writeValueAsString(testBean);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        TestBean readValue = mapper.readValue(jsonParser, TestBean.class);
        InnerClassInner inner = readValue.getClass().getDeclaredField("inner").get(readValue);

        assertNotNull(inner);
        assertEquals(42, inner.getValue());
    }

    @Test
    public void testWriteReplace() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean testBean = new TestBean();
        String json = mapper.writeValueAsString(testBean);

        TestBean deserializedTestBean = mapper.readValue(json, TestBean.class);
        InnerClassProperty testInnerProperty = (InnerClassProperty) deserializedTestBean.getClass()
                .getDeclaredField("innerProperty").get(deserializedTestBean);

        InnerClassProperty innerPropertyWrapper = testInnerProperty.writeReplace();
        TestBean testBeanFromWrapper = mapper.readValue(TestUtil.writeValueAsString(innerPropertyWrapper), TestBean.class);

        InnerClassInner inner = testBeanFromWrapper.getClass()
                .getDeclaredField("inner").get(testBeanFromWrapper);
        assertNotNull(inner);
        assertEquals(42, inner.getValue());
    }
}