package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TestBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class MethodPropertyTest extends TestBase {

    static class TestBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class TestJsonDeserializer extends JsonDeserializer<TestBean> {
        @Override
        public TestBean deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            TestBean bean = mapper.readValue(p, TestBean.class);
            return bean;
        }
    }

    @org.junit.Test
    public void testMethodProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        MethodProperty property = mapper.findProperty("name", TestBean.class);

        // Test setting value using setter method
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\"}");
        property.deserializeAndSet(p, new TestContext(), bean);
        assertEquals("John", bean.getName());

        // Test setting null value
        p = mapper.getFactory().createParser("{\"name\": null}");
        property.deserializeAndSet(p, new TestContext(), bean);
        assertNull(bean.getName());

        // Test empty string value
        p = mapper.getFactory().createParser("{\"name\": \"\"}");
        property.deserializeAndSet(p, new TestContext(), bean);
        assertEquals("", bean.getName());

        // Test setting value using deserializer
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForProperty("name", TestBean.class);
        Converter<String, TestBean> converter = new StdConverter.StdConverterImpl<String, TestBean>() {
            @Override
            public TestBean convert(String text) {
                TestBean bean = new TestBean();
                bean.setName(text);
                return bean;
            }
        };
        property = mapper.findProperty("name", TestBean.class, typeDeserializer, converter);

        p = mapper.getFactory().createParser("{\"name\": \"Jane\"}");
        TestBean deserializedBean = property.deserializeSetAndReturn(p, new TestContext(), bean);
        assertEquals("Jane", deserializedBean.getName());

        // Test deserializing null value
        p = mapper.getFactory().createParser("{\"name\": null}");
        deserializedBean = property.deserializeSetAndReturn(p, new TestContext(), bean);
        assertNull(deserializedBean.getName());
    }

    static class TestContext extends DeserializationContext {
        @Override
        public SerializerProvider getSerializerProvider() {
            return new SerializerProvider() {};
        }

        @Override
        public ObjectMapper getObjectMapper() {
            return new ObjectMapper();
        }

        @Override
        public BeanDescription findBeanDescription(Class<?> beanClass, MapperConfig<?> config, JavaType type) {
            return null;
        }

        @Override
        public Object findValueHolder(PropertyName propName, JavaType type) {
            return null;
        }
    }
}