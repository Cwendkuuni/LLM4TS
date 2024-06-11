package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Unit tests for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    private static class TestBean {
        private String prop1;
        private int prop2;
        private List<String> list;

        public String getProp1() {
            return prop1;
        }

        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }

        public int getProp2() {
            return prop2;
        }

        public void setProp2(int prop2) {
            this.prop2 = prop2;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> TypeDeserializer createTestTypeDeserializer(Class<T> clazz) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(clazz, new StdDeserializer<T>(clazz) {
            @Override
            public T deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return null;
            }
        });
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        return mapper.getTypeDeserializer(clazz);
    }

    @SuppressWarnings("resource")
    @Test
    public void testExternalTypeHandler() throws IOException {
        // Create test data
        List<SettableBeanProperty> properties = new ArrayList<>();
        properties.add(new SettableBeanProperty("prop1", String.class));
        properties.add(new SettableBeanProperty("prop2", Integer.class));
        SettableBeanProperty listProp = new SettableBeanProperty("list", ArrayList.class);
        properties.add(listProp);

        TypeDeserializer stringDeserializer = createTestTypeDeserializer(String.class);
        TypeDeserializer intDeserializer = createTestTypeDeserializer(Integer.class);
        TypeDeserializer listDeserializer = new ArrayTypeDeserializer(
                ArrayType.constructUnboundedArrayType(String.class), null);

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(properties.get(0), stringDeserializer);
        builder.addExternal(properties.get(1), intDeserializer);
        builder.addExternal(listProp, listDeseralizer);

        ExternalTypeHandler handler = builder.build();

        // Test handleTypePropertyValue method
        JsonParser p = new JsonFactory().createParser("{\"prop1\":\"foo\", \"type\":\"String\", "
                + "\"prop2\":1, \"type2\":\"Integer\"}");
        DeserializationContext ctxt = new TestContext();
        Object bean = new TestBean();

        assertTrue(handler.handleTypePropertyValue(p, ctxt, "prop1", bean));
        assertEquals("foo", properties.get(0).get(bean));

        assertTrue(handler.handleTypePropertyValue(p, ctxt, "prop2", bean));
        assertEquals(1, properties.get(1).get(bean));

        // Test handlePropertyValue method
        p = new JsonFactory().createParser("{\"prop1\":null, \"type\":\"String\", "
                + "\"prop2\":null, \"type2\":\"Integer\", \"list\":[\"foo\", \"bar\"]}");
        handler.handlePropertyValue(p, ctxt, "prop1", bean);
        assertNull(properties.get(0).get(bean));

        handler.handlePropertyValue(p, ctxt, "prop2", bean);
        assertNull(properties.get(1).get(bean));

        handler.handlePropertyValue(p, ctxt, "list", bean);
        List<String> list = properties.get(2).get(bean);
        assertEquals(2, list.size());
        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));

        // Test complete method
        p = new JsonFactory().createParser("{\"prop1\":null, \"type\":\"String\", "
                + "\"prop2\":null, \"type2\":\"Integer\", \"list\":[\"foo\", \"bar\"]}");
        handler.complete(p, ctxt, bean);
        assertEquals("foo", properties.get(0).get(bean));
        assertEquals(1, properties.get(1).get(bean));
        assertEquals(2, list.size());
        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));
    }

    private static class TestContext extends DeserializationContext {
        private Object bean;

        @Override
        public Object findBean(Class<?> beanType, JsonParser p, boolean unwrapSingleDataValueContainer)
                throws JsonMappingException, IOException {
            bean = new TestBean();
            return bean;
        }
    }
}