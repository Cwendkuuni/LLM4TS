package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws Exception {
        SimpleModule module = new SimpleModule();
        ObjectFactory factory = new ObjectFactory();
        module.setSerializerFactory(new JsonSerializerFactory());
        module.addSerializer(String.class, new StringSerializer());
        module.addSerializer(MyBean.class, new MyBeanSerializer());
        module.addSerializer(MyBeanProperty.class, new MyBeanPropertySerializer());
        module.addDeserializer(MyBean.class, new MyBeanDeserializer());
        module.addDeserializer(MyBeanProperty.class, new MyBeanPropertyDeserializer());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        Object defaultBean = factory.createMyBean();
        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(MyBean.class));
        propertyBuilder._defaultBean = defaultBean;
    }

    @Test
    public void testBuildWriter() throws Exception {
        SerializerProvider prov = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findProperty("name");
        JavaType declaredType = TypeFactory.defaultInstance().constructType(MyBeanProperty.class);
        JsonSerializer<?> ser = new MyBeanPropertySerializer();
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);

        // Test with default inclusion
        assertEquals(JsonInclude.Include.ALWAYS, bpw.getInclusion());

        // Test with non-default inclusion
        propertyBuilder._defaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_DEFAULT);
        propertyBuilder._useRealPropertyDefaults = false;
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);
        assertEquals(JsonInclude.Include.NON_DEFAULT, bpw.getInclusion());
        assertNull(bpw.getValueToSuppress());
        assertFalse(bpw.suppressNulls());

        // Test with non-empty inclusion
        propertyBuilder._defaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_EMPTY);
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);
        assertEquals(JsonInclude.Include.NON_EMPTY, bpw.getInclusion());
        assertNull(bpw.getValueToSuppress());
        assertFalse(bpw.suppressNulls());

        // Test with custom inclusion
        JsonInclude.Value value = new JsonInclude.Value(JsonInclude.Include.CUSTOM);
        value.setValueFilter(new PropertyFilter());
        propertyBuilder._defaultInclusion = value;
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);
        assertEquals(JsonInclude.Include.CUSTOM, bpw.getInclusion());
        assertNotNull(bpw.getValueToSuppress());
        assertFalse(bpw.suppressNulls());

        // Test with null inclusion
        propertyBuilder._defaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_NULL);
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);
        assertEquals(JsonInclude.Include.NON_NULL, bpw.getInclusion());
        assertNull(bpw.getValueToSuppress());
        assertFalse(bpw.suppressNulls());
    }

    static class MyBean {

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private MyBeanProperty name;

        public MyBeanProperty getName() {
            return name;
        }

        public void setName(MyBeanProperty name) {
            this.name = name;
        }
    }

    static class MyBeanProperty {

        @JsonSerialize
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    static class StringSerializer extends StdSerializer<String> {

        public StringSerializer() {
            this(null);
        }

        public StringSerializer(Class<String> t) {
            super(t);
        }

        @Override
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {
            jgen.writeString(value);
        }
    }

    static class MyBeanSerializer extends StdSerializer<MyBean> {

        public MyBeanSerializer() {
            this(null);
        }

        public MyBeanSerializer(Class<MyBean> t) {
            super(t);
        }

        @Override
        public void serialize(MyBean value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {
            BeanPropertyWriter bpw = provider.findPropertyWriter("name", MyBeanProperty.class, provider.getSerializationConfig());
            bpw.writeNull(jgen, value.getName());
        }
    }

    static class MyBeanPropertySerializer extends StdSerializer<MyBeanProperty> {

        public MyBeanPropertySerializer() {
            this(null);
        }

        public MyBeanPropertySerializer(Class<MyBeanProperty> t) {
            super(t);
        }

        @Override
        public void serialize(MyBeanProperty value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {
            BeanPropertyWriter bpw = provider.findPropertyWriter("value", String.class, provider.getSerializationConfig());
            bpw.writeValue(jgen, value.getValue());
        }
    }

    static class MyBeanDeserializer extends StdDeserializer<MyBean> {

        public MyBeanDeserializer() {
            this(null);
        }

        public MyBeanDeserializer(Class<MyBean> t) {
            super(t);
        }

        @Override
        public MyBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            MyBean result = new MyBean();
            MyBeanProperty name = ctxt.readValueAs(MyBeanProperty.class, p);
            result.setName(name);
            return result;
        }
    }

    static class MyBeanPropertyDeserializer extends StdDeserializer<MyBeanProperty> {

        public MyBeanPropertyDeserializer() {
            this(null);
        }

        public MyBeanPropertyDeserializer(Class<MyBeanProperty> t) {
            super(t);
        }

        @Override
        public MyBeanProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyBeanProperty();
        }
    }

    static class ObjectFactory {

        public MyBean createMyBean() {
            return new MyBean();
        }
    }

    static class PropertyFilter implements JsonFilter {

        @Override
        public boolean canSerializeValue(SerializerProvider p, TypeSerializer t, Object c) {
            return true;
        }

        @Override
        public void serializeValue(SerializerProvider p, JsonGenerator g, Object c) throws IOException {
            g.writeValue(c);
        }

        @Override
        public boolean canDeserializeValue(DeserializationContext ctxt, JsonParser p) {
            return true;
        }

        @Override
        public Object deserializeValue(DeserializationContext ctxt, JsonParser p, TypeDeserializer vd) throws IOException {
            return p.getValueAsToken().getValueAsString();
        }
    }
}