package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.test.BaseTest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertyBuilderTest extends BaseTest {

    public void testPropertyBuilder() throws Exception {
        SerializationConfig config = new SerializationConfig();
        config.setRootValueConstructor(true);

        BeanDescription beanDesc = BeanDescription.forDeserialization(MyPojo.class);
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);

        // Test with null value
        ObjectNode node = mapper().writeValueAsTree(pb.buildWriter(null, null, null, null, null, null, null, false));
        assertNull(node.toString());

        // Test with simple value
        MyPojo simple = new MyPojo("Simple", 1);
        node = mapper().writeValueAsTree(pb.buildWriter(null, null, simple.getClass(), null, null, null, null, false).writeValueAsString(simple.getName()));
        assertEquals("{\"name\":\"Simple\"}", node.toString());

        // Test with array value
        MyPojoWithArray pojoWithArray = new MyPojoWithArray(new String[]{"element1", "element2"});
        node = mapper().writeValueAsTree(pb.buildWriter(null, null, pojoWithArray.getClass(), null, null, null, null, false).writeValueAsString(pojoWithArray.getStringArray()));
        assertEquals("{\"stringArray\":[\"element1\",\"element2\"]}", node.toString());

        // Test with custom serializer
        MyPojoWithCustomSerializer pojoWithCustomSerializer = new MyPojoWithCustomSerializer();
        node = mapper().writeValueAsTree(pb.buildWriter(null, null, pojoWithCustomSerializer.getClass(), StdSerializer.instance(pojoWithCustomSerializer.getClass()), null, null, null, false).writeValueAsString(pojoWithCustomSerializer));
        assertEquals("\"CustomSerializerSerialized\"", node.toString());

        // Test with JsonInclude.Include.NON_DEFAULT
        MyPojoWithInclude nonDefault = new MyPojoWithInclude(5);
        node = mapper().writeValueAsTree(pb.buildWriter(null, null, nonDefault.getClass(), null, null, null, null, false).writeValueAsString(nonDefault.getValue()));
        assertNull(node.toString());

        // Test with JsonInclude.Include.NON_NULL
        MyPojoWithNull pojoWithNull = new MyPojoWithNull(null);
        node = mapper().writeValueAsTree(pb.buildWriter(null, null, pojoWithNull.getClass(), null, null, null, null, false).writeValueAsString(pojoWithNull));
        assertNull(node.toString());

        // Test with JsonInclude.Include.NON_EMPTY
        MyPojoWithEmpty pojoWithEmpty = new MyPojoWithEmpty();
        node = mapper().writeValueAsTree(pb.buildWriter(null, null, pojoWithEmpty.getClass(), null, null, null, null, false).writeValueAsString(pojoWithEmpty));
        assertNull(node.toString());

        // Test with JsonInclude.Include.ALWAYS
        MyPojoWithAlways pojoWithAlways = new MyPojoWithAlways();
        node = mapper().writeValueAsTree(pb.buildWriter(null, null, pojoWithAlways.getClass(), null, null, null, null, false).writeValueAsString(pojoWithAlways));
        assertEquals("{\"value\":{}}", node.toString());

        // Test with Map type
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);

        node = mapper().writeValueAsTree(pb.buildWriter(null, null, map.getClass(), null, null, null, null, false));
        assertEquals("{\"key1\":\"value1\"}", node.toString());

        // Test with LinkedHashMap type
        LinkedHashMap<String, Object> linkedMap = new LinkedHashMap<>();
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", null);

        node = mapper().writeValueAsTree(pb.buildWriter(null, null, linkedMap.getClass(), null, null, null, null, false));
        assertEquals("{\"key1\":\"value1\"}", node.toString());
    }

    public static class MyPojo {
        private final String name;

        public MyPojo(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class MyPojoWithArray {
        private final String[] stringArray;

        public MyPojoWithArray(String[] stringArray) {
            this.stringArray = stringArray;
        }

        public String[] getStringArray() {
            return stringArray;
        }
    }

    public static class MyPojoWithCustomSerializer extends StdSerializer<MyPojoWithCustomSerializer> {
        public MyPojoWithCustomSerializer() {
            this(null);
        }

        public MyPojoWithCustomSerializer(Class<MyPojoWithCustomSerializer> t) {
            this(null);
        }

        public MyPojoWithCustomSerializer(Class<MyPojoWithCustomSerializer> t, boolean quickSerializer) {
            this();
        }

        public MyPojoWithCustomSerializer() {
            this(null);
        }

        public MyPojoWithCustomSerializer(Class<MyPojoWithCustomSerializer> t, boolean quickSerializer, AnnotationIntrospector a, SerializerProvider p) {
            super(t, quickSerializer, a, p);
        }

        @Override
        public void serialize(MyPojoWithCustomSerializer myPojoWithCustomSerializer, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString("CustomSerializerSerialized");
        }
    }

    public static class MyPojoWithInclude {
        private int value;

        public MyPojoWithInclude(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public int getDefaultValue() {
            return 5;
        }
    }

    public static class MyPojoWithNull {
        private Object value;

        public MyPojoWithNull(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }

    public static class MyPojoWithEmpty {
        private Map<String, Object> value;

        public MyPojoWithEmpty() {
            this.value = new HashMap<>();
        }

        public Map<String, Object> getValue() {
            return value;
        }
    }

    public static class MyPojoWithAlways {
        private Map<String, Object> value = new HashMap<>();

        public MyPojoWithAlways() {
            value.put("key", new Object());
        }

        public Map<String, Object> getValue() {
            return value;
        }
    }
}