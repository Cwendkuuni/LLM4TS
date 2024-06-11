package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PropertyBuilderTest {

    @SuppressWarnings("unchecked")
    private static final Object NO_DEFAULT_MARKER = Boolean.FALSE;

    @org.junit.Test
    public void testPropertyBuilder() throws Exception {
        SerializationConfig config = new SerializationConfig();
        config.setDefaultSerializationInclusion(JsonInclude.Value.always());
        config.setDefaultPropertyInclusion(JsonInclude.Value.always());
        config.setFeatureEnabled(SerializationFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        config.setFeatureEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);

        // Test with no per-type or property annotations
        BeanDescription beanDesc = BeanDescription.createDescForClass(Pojo1.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = builder.getDefaultBean();
        assertNull(defaultBean);

        // Test with empty POJO
        Pojo1 emptyBean = new Pojo1();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo1.class));
        Object defaultBean1 = builder.getDefaultBean();
        assertNull(defaultBean1);

        // Test with POJO having default constructor
        Pojo2 defaultPojo = new Pojo2();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo2.class));
        Object defaultBean2 = builder.getDefaultBean();
        assertEquals(defaultPojo, defaultBean2);

        // Test with POJO having non-default constructor
        Pojo3 nonDefaultPojo = new Pojo3();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo3.class));
        Object defaultBean3 = builder.getDefaultBean();
        assertNull(defaultBean3);

        // Test POJO with static typing enabled
        config.setDefaultSerializationInclusion(JsonInclude.Value.always());
        config.setDefaultPropertyInclusion(JsonInclude.Value.always());
        config.setFeatureEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        config.setFeatureEnabled(SerializationFeature.FAIL_ON_EMPTY_OR_NULL_STRING_SERIALIZATION);
        config.setFeatureEnabled(SerializationFeature.WRITE_DATABIND_XML_NAMES);
        Pojo4 pojo4 = new Pojo4();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo4.class));
        Object defaultBean4 = builder.getDefaultBean();
        assertEquals(pojo4, defaultBean4);

        // Test with null POJO
        Pojo5 pojo5 = null;
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo5.class));
        Object defaultBean5 = builder.getDefaultBean();
        assertNull(defaultBean5);

        // Test with POJO having custom value filter
        Pojo6 pojo6 = new Pojo6();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo6.class));
        Object defaultBean6 = builder.getDefaultBean();
        assertSame(pojo6, defaultBean6);

        // Test with per-type and property annotations
        Pojo7 pojo7 = new Pojo7();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo7.class));
        Object defaultBean7 = builder.getDefaultBean();
        assertNull(defaultBean7);

        // Test with property annotation for null inclusion
        Pojo8 pojo8 = new Pojo8();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo8.class));
        Object defaultBean8 = builder.getDefaultBean();
        assertNull(defaultBean8);

        // Test with property annotation for non-null inclusion
        Pojo9 pojo9 = new Pojo9();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo9.class));
        Object defaultBean9 = builder.getDefaultBean();
        assertNotNull(defaultBean9);

        // Test with property annotation for non-empty inclusion
        Pojo10 pojo10 = new Pojo10();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo10.class));
        Object defaultBean10 = builder.getDefaultBean();
        assertNotNull(defaultBean10);
        assertNotSame(pojo10, defaultBean10);

        // Test with property annotation for custom inclusion
        Pojo11 pojo11 = new Pojo11();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo11.class));
        Object defaultBean11 = builder.getDefaultBean();
        assertNull(defaultBean11);

        // Test with property annotation for non-default inclusion
        Pojo12 pojo12 = new Pojo12();
        builder = new PropertyBuilder(config, BeanDescription.createDescForClass(Pojo12.class));
        Object defaultBean12 = builder.getDefaultBean();
        assertEquals(pojo12, defaultBean12);
    }

    // Getters and setters for testing

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class Pojo1 {
        // Empty class
    }

    static class Pojo2 {
        Pojo2() {
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class Pojo3 {
        Pojo3(int i) {
        }
    }

    static class Pojo4 {
        Pojo4() {
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class Pojo5 {
        Pojo5() {
        }

        @JsonSerialize(using = EmptySerializer.class)
        public Object getEmptyProp() {
            return null;
        }
    }

    static class Pojo6 {
        Pojo6() {
        }

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public Object getEmptyProp() {
            return null;
        }

        @JsonSerialize(using = EmptySerializer.class)
        public Object getProp() {
            return "Value";
        }

        public boolean getValueFilter() {
            return true;
        }
    }

    static class Pojo7 {
        Pojo7() {
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Object getProp() {
            return null;
        }

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public Object[] getArrayProp() {
            return new Object[0];
        }
    }

    static class Pojo8 {
        Pojo8() {
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Object getProp() {
            return null;
        }
    }

    static class Pojo9 {
        Pojo9() {
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Object getProp() {
            return new Object();
        }
    }

    static class Pojo10 {
        Pojo10() {
        }

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public Object getProp() {
            return null;
        }
    }

    static class Pojo11 {
        Pojo11() {
        }

        @JsonInclude(JsonInclude.Include.CUSTOM)
        public Object getProp() {
            return null;
        }
    }

    static class Pojo12 {
        Pojo12() {
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public Object getProp() {
            return new Object();
        }
    }

    static class EmptySerializer extends StdSerializer<Object> {
        public EmptySerializer() {
            this(null);
        }

        public EmptySerializer(Class<Object> t) {
            this(null);
        }

        public EmptySerializer(Class<Object> t, AnnotationIntrospector ais) {
            this();
        }

        public EmptySerializer() {
            this(null);
        }

        public EmptySerializer(Class<Object> t, AnnotationIntrospector ais, BeanDescription beanDesc) {
            super(Object.class, ais, beanDesc);
        }

        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider prov) throws IOException {
            jgen.writeNull();
        }
    }
}