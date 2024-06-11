package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanPropertyTypeResolver;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Unit tests for {@link ExternalTypeHandler}.
 */
public class ExternalTypeHandlerTest {

    private static class TestBean {
        private String prop1;
        private String prop2;
        private List<Integer> list;

        public TestBean(String prop1, String prop2, List<Integer> list) {
            this.prop1 = prop1;
            this.prop2 = prop2;
            this.list = list;
        }

        @Override
        public String toString() {
            return "TestBean [prop1=" + prop1 + ", prop2=" + prop2 + ", list=" + list + "]";
        }
    }

    private static class TestTypeHandler extends ExternalTypeHandler {

        public TestTypeHandler(TestBean bean) {
            super(new ExternalTypeHandler.Builder()
                    .addExternal(new SettableBeanProperty(TestBean.class, "prop1"), new StringDeserializer())
                    .addExternal(new SettableBeanProperty(TestBean.class, "prop2"), new StringDeserializer())
                    .addExternal(new SettableBeanProperty(TestBean.class, "list"), new IntListDeserializer())
                    .build(), null, null, null, null);
            this.bean = bean;
        }

        private TestBean bean;

        @Override
        public Object complete(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
            Object result = super.complete(p, ctxt, bean);
            this.bean = (TestBean) result;
            assertEquals("expected values set correctly", "expectedProp1", bean.prop1);
            assertEquals("expected values set correctly", "expectedProp2", bean.prop2);
            assertEquals("expected values set correctly", Arrays.asList(1, 2, 3), bean.list);
            return result;
        }
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> cls) {
            super(cls);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class IntListDeserializer extends TypeDeserializer {

        @Override
        public Object deserializeTypedFromObject(Object bean, DeserializationContext ctxt) throws IOException {
            JsonParser p = ctxt.getParser();
            p.nextToken(); // consume [
            List<Integer> result = new ArrayList<Integer>();
            while (p.nextToken() != null && p.getCurrentToken().isValueId()) {
                result.add(p.getValueAsInt());
            }
            p.nextToken(); // consume ]
            return result;
        }

        @Override
        public void resolveType(DeserializationContext ctxt, BeanProperty property) throws IOException {
            // no-op
        }
    }

    @Test
    public void testExternalTypeHandlerWithNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"prop1\":null,\"prop2\":\"value\"}";
        DeserializationContext context = mapper.getDeserializationContext();
        TestTypeHandler handler = new TestTypeHandler(null);
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = handler.complete(parser, context, null);
        assertNull(result);
    }

    @Test
    public void testExternalTypeHandlerWithValidTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"prop1\":\"value1\",\"prop2\":[\"value2\",\"value3\",\"value4\"]}";
        TestBean bean = new TestBean("expectedProp1", "value2", Arrays.asList(1, 2, 3));
        TestTypeHandler handler = new TestTypeHandler(bean);
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = handler.complete(parser, mapper.getDeserializationContext(), null);
        assertEquals(bean, result);
    }

    @Test
    public void testExternalTypeHandlerWithMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"prop1\":\"value1\",\"prop2\":[1,2,3]}";
        TestBean bean = new TestBean("expectedProp1", "expectedProp2", null);
        TestTypeHandler handler = new TestTypeHandler(bean);
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = handler.complete(parser, mapper.getDeserializationContext(), null);
        assertEquals(bean, result);
    }

    @Test
    public void testExternalTypeHandlerWithMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"prop1\":\"value1\",\"missingProp\":[1,2,3]}";
        TestBean bean = new TestBean("expectedProp1", "expectedProp2", Collections.emptyList());
        TestTypeHandler handler = new TestTypeHandler(bean);
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = handler.complete(parser, mapper.getDeserializationContext(), null);
        assertEquals(bean, result);
    }

    @Test
    public void testExternalTypeHandlerWithMultipleProperties() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"prop1\":\"value1\",\"prop2\":\"value2\",\"prop1\":[\"value3\",\"value4\"]}";
        TestBean bean = new TestBean("expectedProp1", "expectedProp2", Arrays.asList("value3", "value4"));
        TestTypeHandler handler = new TestTypeHandler(bean);
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = handler.complete(parser, mapper.getDeserializationContext(), null);
        assertEquals(bean, result);
    }

    @Test
    public void testExternalTypeHandlerWithMissingCreatorProperties() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"prop1\":\"value1\",\"prop2\":\"value2\",\"list\":[1,2,3]}";
        TestBean bean = new TestBean("expectedProp1", "expectedProp2", Arrays.asList(1, 2, 3));
        TestTypeHandler handler = new TestTypeHandler(bean);
        JsonParser parser = mapper.getFactory().createParser(json);
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer pvb) throws IOException {
                return new TestBean("defaultProp1", "defaultProp2", Collections.emptyList());
            }
        };
        Object result = handler.complete(parser, mapper.getDeserializationContext(), buffer, creator);
        assertEquals(bean, result);
    }

    @Test
    public void testExternalTypeHandlerWithCreatorProperties() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"prop1\":\"value1\",\"prop2\":\"value2\",\"list\":[1,2,3],\"creatorProp\":\"creatorValue\"}";
        TestBean bean = new TestBean("expectedProp1", "expectedProp2", Arrays.asList(1, 2, 3));
        TestTypeHandler handler = new TestTypeHandler(bean);
        JsonParser parser = mapper.getFactory().createParser(json);
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer pvb) throws IOException {
                Object creatorBean = pvb.getParameterValue("creatorProp");
                assertEquals("expected creator property value", "creatorValue", creatorBean);
                return new TestBean("createdProp1", "createdProp2", Collections.emptyList());
            }
        };
        Object result = handler.complete(parser, mapper.getDeserializationContext(), buffer, creator);
        assertEquals(bean, result);
    }
}