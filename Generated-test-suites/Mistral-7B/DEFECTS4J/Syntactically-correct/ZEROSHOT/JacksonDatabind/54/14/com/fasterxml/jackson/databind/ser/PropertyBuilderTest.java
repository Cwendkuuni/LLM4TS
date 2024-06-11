package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Unit tests for {@link PropertyBuilder}.
 */
public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = mapper.findBeanDescription(MyBean.class);
    }

    /**
     * Test creation of {@link PropertyBuilder}.
     */
    @Test
    public void testCreation() {
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
        assertNotNull(pb);
    }

    /**
     * Test getting property annotations.
     */
    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
        Annotations annotations = pb.getClassAnnotations();
        assertEquals(1, annotations.size());
        JsonSerialize annotation = annotations.findValue(JsonSerialize.class);
        assertNotNull(annotation);
    }

    /**
     * Test building property writer.
     */
    @Test
    public void testBuildWriter() throws JsonMappingException {
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);

        ObjectWriter writer = config.builder()
                .forType(MyBean.class)
                .with(new DefaultSerializerProvider())
                .build();
        JsonSerializer<MyBean> ser = writer.findValueSerializer(MyBean.class);
        TypeSerializer typeSer = config.getTypeSerializers().findValueSerializer(MyBean.class, null);
        BeanPropertyWriter bpw = pb.buildWriter(new DefaultSerializerProvider(),
                beanDesc.findPropertyDefinition("name"), MyBean.class.getDeclaredField("name"), ser,
                typeSer, null, null, false);
        assertNotNull(bpw);
    }

    /**
     * Test finding serialization type.
     */
    @Test
    public void testFindSerializationType() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
        JavaType declaredType = MyBean.class.getDeclaredField("list").getType();
        JavaType serializationType = pb.findSerializationType(beanDesc.findPropertyDefinition("list"), false, declaredType);
        assertEquals(List.class, serializationType.getRawClass());
    }

    /**
     * Test handling of null serializer.
     */
    @JsonSerialize(nulls = JsonSerialize.Null.IGNORED)
    static class MyBean {
        private String name;
        private List<Integer> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonSerialize(nullValue = MyIntSerializer.class)
        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }

        public static class MyIntSerializer extends JsonSerializer<Integer> {
            @Override
            public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString("null");
            }
        }
    }

    /**
     * Test handling of custom null serializer.
     */
    @Test
    public void testHandleNullSerializer() throws JsonMappingException {
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
        ObjectWriter writer = config.builder()
                .forType(MyBean.class)
                .with(new DefaultSerializerProvider())
                .build();
        JsonSerializer<MyBean> ser = writer.findValueSerializer(MyBean.class);
        TypeSerializer typeSer = config.getTypeSerializers().findValueSerializer(MyBean.class, null);
        BeanPropertyWriter bpw = pb.buildWriter(new DefaultSerializerProvider(),
                beanDesc.findPropertyDefinition("list"), MyBean.class.getDeclaredField("list"), ser,
                typeSer, null, null, false);
        Object nullSerializer = pb.findNullSerializer(beanDesc.findPropertyDefinition("list"), null);
        assertNotNull(nullSerializer);
        assertEquals(MyBean.MyIntSerializer.class, nullSerializer.getClass());
    }

    /**
     * Test handling of unwrapping.
     */
    @Test
    public void testHandleUnwrapping() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
        ObjectWriter writer = config.builder()
                .forType(MyBean.class)
                .with(new DefaultSerializerProvider())
                .build();
        JsonSerializer<MyBean> ser = writer.findValueSerializer(MyBean.class);
        TypeSerializer typeSer = config.getTypeSerializers().findValueSerializer(MyBean.class, null);
        NameTransformer unwrapper = new NameTransformer() {
            @Override
            public String transformKey(String name) {
                return "inner_" + name;
            }
        };
        BeanPropertyWriter bpw = pb.buildWriter(new DefaultSerializerProvider(),
                beanDesc.findPropertyDefinition("innerList"), MyBean.class.getDeclaredField("innerList"), ser,
                typeSer, null, null, false);
        bpw = bpw.unwrappingWriter(unwrapper);
        assertEquals("inner_list", bpw.getName());
    }
}