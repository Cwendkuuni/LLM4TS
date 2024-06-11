package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * Tests for {@link PropertyBuilder} class.
 */
public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilder() throws Exception {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        MyBean bean = new MyBean("Hello World");
        BeanDescription beanDesc = mapper.createBeanDescription(bean.getClass());

        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);

        assertEquals(JsonInclude.Include.NON_NULL, pb.getDefaultInclusion().getValueInclusion());
        assertFalse(pb.useRealPropertyDefaults);
    }

    @Test
    public void testBuildWriter_defaultSerializationType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        MyBean bean = new MyBean("Hello World");
        BeanDescription beanDesc = mapper.createBeanDescription(bean.getClass());

        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);

        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JavaType javaType = TypeFactory.defaultInstance().constructType(MyBean.class);
        SerializerProvider prov = mapper.getSerializerProvider();

        BeanPropertyWriter bpw = pb.buildWriter(prov, null, javaType, null, null, null, null, false);

        assertEquals(MyBean.class, bpw.getDeclaredType());
        assertEquals("name", bpw.getName());
        assertEquals(String.class, bpw.getType());
        assertEquals(JsonInclude.Include.NON_NULL, bpw.getSerializationInclusion());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());
    }

    @Test
    public void testBuildWriter_customSerializationType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        MyBean bean = new MyBean("Hello World");
        BeanDescription beanDesc = mapper.createBeanDescription(bean.getClass());

        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);

        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JavaType javaType = new TypeReference<MyCustomClass>() {}.getType();
        SerializerProvider prov = mapper.getSerializerProvider();

        JsonSerialize annotation = new MyCustomSerializer();

        BeanPropertyWriter bpw = pb.buildWriter(prov, null, javaType, null, null, null, null, false, annotation);

        assertEquals(MyCustomClass.class, bpw.getDeclaredType());
        assertEquals("name", bpw.getName());
        assertEquals(String.class, bpw.getType());
        assertEquals(JsonInclude.Include.NON_NULL, bpw.getSerializationInclusion());
        assertEquals(MyCustomSerializer.class, bpw.getNullSerializer().getClass());
        assertNull(bpw.getUnwrappingNameTransformer());
    }

    @Test
    public void testBuildWriter_customNullSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        MyBean bean = new MyBean("Hello World");
        BeanDescription beanDesc = mapper.createBeanDescription(bean.getClass());

        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);

        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JavaType javaType = TypeFactory.defaultInstance().constructType(MyBean.class);
        SerializerProvider prov = mapper.getSerializerProvider();

        BeanPropertyWriter bpw = pb.buildWriter(prov, null, javaType, null, null, null, null, false);

        MyNullSerializer myNullSerializer = new MyNullSerializer();

        bpw.assignNullSerializer(prov.serializerInstance(null, MyNullSerializer.class));

        assertEquals(MyBean.class, bpw.getDeclaredType());
        assertEquals("name", bpw.getName());
        assertEquals(String.class, bpw.getType());
        assertEquals(JsonInclude.Include.NON_NULL, bpw.getSerializationInclusion());
        assertEquals(MyNullSerializer.class, bpw.getNullSerializer().getClass());
        assertNull(bpw.getUnwrappingNameTransformer());
    }

    @Test
    public void testBuildWriter_customUnwrappingNameTransformer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        MyBean bean = new MyBean("Hello World");
        BeanDescription beanDesc = mapper.createBeanDescription(bean.getClass());

        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);

        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JavaType javaType = TypeFactory.defaultInstance().constructType(MyBean.class);
        SerializerProvider prov = mapper.getSerializerProvider();

        NameTransformer nameTransformer = new MyNameTransformer();

        BeanPropertyWriter bpw = pb.buildWriter(prov, null, javaType, null, null, null, null, false);

        bpw = bpw.unwrappingWriter(nameTransformer);

        assertEquals(MyBean.class, bpw.getDeclaredType());
        assertEquals("name", bpw.getName());
        assertEquals(String.class, bpw.getType());
        assertEquals(JsonInclude.Include.NON_NULL, bpw.getSerializationInclusion());
        assertNull(bpw.getNullSerializer());
        assertEquals(MyNameTransformer.class, bpw.getUnwrappingNameTransformer().getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriter_invalidSerializationType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        MyBean bean = new MyBean("Hello World");
        BeanDescription beanDesc = mapper.createBeanDescription(bean.getClass());

        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);

        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JavaType javaType = TypeFactory.defaultInstance().constructType(MyBean.class);
        SerializerProvider prov = mapper.getSerializerProvider();

        BeanPropertyWriter bpw = pb.buildWriter(prov, null, javaType, null, null, null, null, false);

        TypeSerializer typeSerializer = mapper.getTypeSerializer(String[].class);

        bpw = bpw.withContentTypeHandler(typeSerializer);

        throw new AssertionError("Expected IllegalArgumentException");
    }

    @Test
    public void testFindSerializationType() throws Exception {
        MyBean bean = new MyBean("Hello World");
        JavaType javaType = TypeFactory.defaultInstance().constructType(MyBean.class);

        AnnotationIntrospector introspector = new AnnotationIntrospector() {
            @Override
            public JsonSerialize<?> findSerializationSerializer(AnnotatedMember member, JavaType typeHint)
                    throws JsonMappingException {
                return new MySerializer();
            }
        };

        PropertyBuilder pb = new PropertyBuilder(new SerializationConfig(), new BeanDescription(MyBean.class));

        JsonSerialize serializationAnnotation = introspector.findSerializationAnnotation(null, javaType);

        JavaType resultType = pb.findSerializationType(null, false, javaType);

        assertEquals(MySerializer.class, resultType.getContentType().getRawClass());
    }

    static class MyBean {
        private String name;

        public MyBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class MySerializer extends JsonSerializer<MyBean> {
        @Override
        public void serialize(MyBean t, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeString(t.getName());
        }

        @Override
        public Typing getTyping() {
            return Typing.STATIC;
        }
    }

    static class MyCustomSerializer extends JsonSerializer<MyBean> {
        @Override
        public void serialize(MyBean t, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeString("custom");
        }

        @Override
        public Typing getTyping() {
            return Typing.STATIC;
        }
    }

    static class MyNameTransformer implements NameTransformer {
        @Override
        public String transformKey(String name) {
            return name.toUpperCase();
        }

        @Override
        public String transformValue(String value) {
            return value.toUpperCase();
        }
    }

    static class MyNullSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object t, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeNull();
        }
    }

    static class MyView extends View {
        // empty
    }
}