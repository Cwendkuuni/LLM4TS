package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerializerFor;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtils;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

    @Test
    public void testBasicPropertyWriter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleBean bean = new SimpleBean();
        bean.setName("John Doe");
        bean.setAge(25);
        bean.setList(Arrays.asList("Alice", "Bob"));

        SerializationConfig config = mapper.getSerializationConfig();
        BeanSerializerFactory factory = new BeanSerializerFactory(config, BeanDescription.forClass(SimpleBean.class));
        PropertyBuilder builder = factory.createPropertyBuilder();

        ObjectWriter writer = mapper.writer().with(builder);

        String json = writer.writeValueAsString(bean);
        SimpleBean deserializedBean = mapper.readValue(json, SimpleBean.class);

        assertEquals(bean.getName(), deserializedBean.getName());
        assertEquals(bean.getAge(), deserializedBean.getAge());
        assertEquals(bean.getList(), deserializedBean.getList());
    }

    @Test
    public void testDefaultValueSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        SerializationConfig config = mapper.getSerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);

        BeanSerializerFactory factory = new BeanSerializerFactory(config, BeanDescription.forClass(SimpleBean.class));
        PropertyBuilder builder = factory.createPropertyBuilder();

        ObjectWriter writer = mapper.writer().with(builder);

        SimpleBean bean = new SimpleBean();
        String json = writer.writeValueAsString(bean);

        assertNull(json.contains("name"));
        assertNull(json.contains("age"));
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class SimpleBean {
        private String name;
        private int age;
        private List<String> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

    @JsonSerialize(using = SimpleBeanSerializer.class)
    static class SimpleBeanSerializer {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonSerializerFor(SimpleBean.class)
    static class SimpleBeanSerializerModifier extends BeanSerializerModifier {
        @Override
        public PropertyWriter<?> modifyPropertyWriter(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     PropertyWriter<?> pw) throws JsonMappingException {
            if (pw instanceof BeanPropertyWriter) {
                BeanPropertyWriter bpw = (BeanPropertyWriter) pw;
                bpw.assignSerializer(new SimpleBeanSerializer());
            }
            return pw;
        }
    }

    @Test
    public void testCustomSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        SimpleBean bean = new SimpleBean();
        bean.setName("John Doe");

        SerializationConfig config = mapper.getSerializationConfig();
        config.setAnnotationIntrospector(new CustomAnnotationIntrospector());

        BeanSerializerFactory factory = new BeanSerializerFactory(config, BeanDescription.forClass(SimpleBean.class));
        PropertyBuilder builder = factory.createPropertyBuilder();

        ObjectWriter writer = mapper.writer().with(builder);
        String json = writer.writeValueAsString(bean);

        assertEquals("\"John Doe\"", json);
    }

    static class CustomAnnotationIntrospector extends AnnotationIntrospector {
        @Override
        public JsonSerializer<?> findJsonSerializer(AnnotatedMember am, Class<?> type) {
            if (am.isMethod() && SimpleBeanSerializerModifier.class.isAssignableFrom(am.getDeclaringClass())) {
                Method method = (Method) am.getMember();
                if (method.getName().equals("getName")) {
                    return new SimpleBeanNameSerializer();
                }
            }
            return super.findJsonSerializer(am, type);
        }
    }

    static class SimpleBeanNameSerializer extends StdSerializer<String> {
        public SimpleBeanNameSerializer() {
            this(null);
        }

        public SimpleBeanNameSerializer(Class<String> t) {
            this(null);
        }

        public SimpleBeanNameSerializer(Class<String> t, Constructor<SimpleBeanNameSerializer> c, AnnotationMappingParser p) {
            super(t, c, p);
        }

        @Override
        public String serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            return "\"Custom Serialized Value\": " + value;
        }
    }
}