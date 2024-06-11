package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    public void testFindObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getFactory().createContext();

        // Test with null id
        ObjectIdGenerator<?> gen = mapper.getFactory().getObjectIdGenerator();
        ReadableObjectId id1 = ctxt.findObjectId(null, gen, SimpleObjectIdResolver.instance);
        assertEquals(null, id1.getId());

        // Test with valid id
        Map<String, MyBean> beans = new HashMap<>();
        MyBean bean1 = new MyBean("1");
        beans.put("1", bean1);
        MyBean bean2 = new MyBean("2");
        MyBean bean3 = new MyBean("3");
        MyBean bean4 = new MyBean("4");
        MyBean bean5 = new MyBean("5");
        MyBean bean6 = new MyBean("6");
        beans.putAll( Arrays.asList(bean2, bean3, bean4, bean5, bean6) );

        ObjectIdGenerator<?> gen2 = mapper.getFactory().getObjectIdGenerator(MyBean.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId id2 = ctxt.findObjectId(bean1, gen2, resolver);
        assertEquals("1", id2.getId());
        assertEquals(bean1, id2.getIdValue());

        // Test with duplicate id
        ReadableObjectId id3 = ctxt.findObjectId(bean1, gen2, resolver);
        assertEquals("1", id3.getId());
        assertEquals(bean1, id3.getIdValue());

        // Test with referencing properties
        MyBean refBean = new MyBean("ref");
        bean1.setRef(refBean);
        id2 = ctxt.findObjectId(bean1, gen2, resolver);
        Referring referring = id2.getReferringProperties().next();
        assertEquals(MyBean.class, referring.getBeanType());
        assertEquals("ref", referring.getPropertyName());

        // Test with missing id
        MyBean missingBean = new MyBean("missing");
        id3 = ctxt.findObjectId(missingBean, gen2, resolver);
        assertNull(id3);
    }

    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getFactory().createContext();

        // Test with no unresolved ids
        assertDoesNotThrow(() -> ctxt.checkUnresolvedObjectId());

        // Test with unresolved ids
        ObjectIdGenerator<?> gen = mapper.getFactory().getObjectIdGenerator();
        Map<String, MyBean> beans = new HashMap<>();
        MyBean bean1 = new MyBean("1");
        beans.put("1", bean1);
        MyBean bean2 = new MyBean("2");
        MyBean bean3 = new MyBean("3");
        MyBean bean4 = new MyBean("4");
        MyBean bean5 = new MyBean("5");
        MyBean bean6 = new MyBean("6");
        beans.putAll( Arrays.asList(bean2, bean3, bean4, bean5, bean6) );

        ObjectIdGenerator<?> gen2 = mapper.getFactory().getObjectIdGenerator(MyBean.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId id1 = ctxt.findObjectId(bean1, gen2, resolver);
        ReadableObjectId id2 = ctxt.findObjectId(bean2, gen2, resolver);

        ctxt.checkUnresolvedObjectId();

        UnresolvedForwardReference exception = assertThrows(UnresolvedForwardReference.class, () -> {
            ctxt.checkUnresolvedObjectId();
        });

        assertTrue(exception.hasUnresolvedId("1", MyBean.class, "ref"));
        assertEquals(2, exception.getUnresolvedIds().size());
    }

    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getFactory().createContext();

        // Test with null deserializer definition
        assertNull(ctxt.deserializerInstance(null, null));

        // Test with empty class name
        assertNull(ctxt.deserializerInstance(null, String.class));

        // Test with null deserClass
        assertNull(ctxt.deserializerInstance(null, null));

        // Test with JsonDeserializer class
        JsonDeserializer<MyBean> deserializer = new MyBeanDeserializer();
        JsonDeserializer<MyBean> deserializerInstance = ctxt.deserializerInstance(null, deserializer.getClass());
        assertEquals(deserializer, deserializerInstance);

        // Test with JsonDeserializer instance
        JsonDeserializer<MyBean> deserializerInstance2 = ctxt.deserializerInstance(null, deserializer);
        assertEquals(deserializer, deserializerInstance2);

        // Test with Class<JsonDeserializer>
        Class<JsonDeserializer<MyBean>> deserializerClass = JsonDeserializer.class;
        JsonDeserializer<MyBean> deserializerInstance3 = ctxt.deserializerInstance(null, deserializerClass);
        assertEquals(deserializer, deserializerInstance3);

        // Test with class that is not JsonDeserializer
        Class<MyStringDeserializer> deserializerClass2 = MyStringDeserializer.class;
        assertNull(ctxt.deserializerInstance(null, deserializerClass2));

        // Test with handler instantantiator
        HandlerInstantiator hi = new HandlerInstantiator() {
            @Override
            public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated ann, Class<?> deserClass) {
                return new MyBeanDeserializer();
            }
        };
        JsonDeserializer<MyBean> deserializerInstance4 = ctxt.deserializerInstance(null, null, hi);
        assertEquals(deserializer, deserializerInstance4);
    }

    public static class MyBean {
        private String id;
        private MyBean ref;

        public MyBean(String id) {
            this.id = id;
        }

        public MyBean getRef() {
            return ref;
        }

        public void setRef(MyBean ref) {
            this.ref = ref;
        }
    }

    public static class MyBeanDeserializer extends JsonDeserializer<MyBean> {
        @Override
        public MyBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String id = p.getValueAsString();
            MyBean bean = new MyBean(id);
            return bean;
        }
    }

    public static class MyStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}