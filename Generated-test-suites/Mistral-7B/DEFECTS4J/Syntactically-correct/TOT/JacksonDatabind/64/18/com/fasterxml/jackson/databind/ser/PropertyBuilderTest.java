package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.MarkerForEmpty;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Type;

@Test
public class PropertyBuilderTest {

    private static final Object NO_DEFAULT_MARKER = Boolean.FALSE;

    @SuppressWarnings("unchecked")
    private static final Object NO_DEFAULT_BEAN = new Object() {
        // Empty class used as default bean for testing NON_DEFAULT inclusion
    };

    private static final Object[][] PRIMITIVE_VALUES = {
            {Byte.MIN_VALUE, (byte) 0},
            {Short.MIN_VALUE, (short) 0},
            {Integer.MIN_VALUE, 0},
            {Long.MIN_VALUE, 0L},
            {Float.NaN, 0f},
            {Double.NaN, 0d}
    };

    private static final Object[] STRING_VALUES = {"", "", "test"};

    private static final Object[] ARRAY_VALUES = {new Object[]{}, new Object[]{NO_DEFAULT_BEAN}, new Object[]{1, 2, 3}};

    private static final Object[] MAP_VALUES = {new Object() {
        @SuppressWarnings("unchecked")
        private static final JavaClassMapper MAPPER = new JavaClassMapper();

        private final Map<String, Object> map = new HashMap<>();

        static {
            map.put("key1", 1);
            map.put("key2", "");
            map.put("key3", NO_DEFAULT_BEAN);
        }

        private JavaClassMapper getMapper() {
            return MAPPER;
        }

        private Object getMap() {
            return map;
        }
    }, new HashMap<>(), new LinkedHashMap<>(), new TreeMap<>()};

    @BeforeClass
    public static void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        _config = mapper.getSerializationConfig();
        _annotationIntrospector = mapper.getAnnotationIntrospector();
    }

    @Test
    public void testPrimitiveSerialization() throws JsonProcessingException, IOException {
        for (Object value : PRIMITIVE_VALUES) {
            PropertyBuilder builder = new PropertyBuilder(_config, _annotationIntrospector);
            BeanPropertyWriter writer = builder.buildWriter(_config.getDefaultSerializerProvider(),
                    null, Object.class.getType(), null, null, null, null, false);
            Object defaultValue;
            if (value instanceof Number) {
                defaultValue = 0;
            } else {
                defaultValue = null;
            }

            Assert.assertEquals(writer.getDefaultSerializer().serializeValue(value),
                    writer.getDefaultSerializer().serializeValue(defaultValue), "Serializing primitive values");
        }
    }

    @Test
    public void testStringSerialization() throws JsonProcessingException, IOException {
        PropertyBuilder builder = new PropertyBuilder(_config, _annotationIntrospector);
        BeanPropertyWriter writer = builder.buildWriter(_config.getDefaultSerializerProvider(),
                null, String.class, null, null, null, null, false);

        for (Object value : STRING_VALUES) {
            Assert.assertEquals(writer.getDefaultSerializer().serializeValue(value),
                    writer.getDefaultSerializer().serializeValue(""), "Serializing String values");
        }
    }

    @Test
    public void testContainerSerialization() throws JsonProcessingException, IOException {
        for (Object container : ARRAY_VALUES) {
            PropertyBuilder builder = new PropertyBuilder(_config, _annotationIntrospector);
            BeanPropertyWriter writer = builder.buildWriter(_config.getDefaultSerializerProvider(),
                    null, container.getClass(), null, null, null, null, false);
            Object defaultValue;
            if (container instanceof Object[]) {
                defaultValue = new Object[0];
            } else if (container instanceof Map<?, ?>) {
                defaultValue = new HashMap<>();
            } else {
                defaultValue = new LinkedHashMap<>();
            }

            Assert.assertEquals(writer.getDefaultSerializer().serializeValue(container),
                    writer.getDefaultSerializer().serializeValue(defaultValue),
                    "Serializing container types");
        }
    }

    @Test
    public void testNullSerialization() throws JsonProcessingException, IOException {
        PropertyBuilder builder = new PropertyBuilder(_config, _annotationIntrospector);
        BeanPropertyWriter writer = builder.buildWriter(_config.getDefaultSerializerProvider(),
                null, Object.class, null, null, null, null, false);

        Assert.assertNull(writer.getDefaultSerializer().serializeValue(null), "Serializing null values");
    }

    @Test
    public void testPropertyDefaultValue() throws JsonProcessingException, IOException {
        PropertyBuilder builder = new PropertyBuilder(_config, _annotationIntrospector);

        /* Testing default bean value */
        Object defaultBean = NO_DEFAULT_BEAN;
        BeanPropertyWriter writer = builder.buildWriter(_config.getDefaultSerializerProvider(),
                null, Object.class, null, null, null, null, false);

        Object defaultValue = writer.getDefaultSerializer().getNullValue();
        Assert.assertEquals(defaultValue, NO_DEFAULT_MARKER, "Default bean value test - null");

        defaultBean = new Object();
        defaultValue = writer.getDefaultSerializer().getNullValue();
        Assert.assertNull(defaultValue, "Default bean value test - non-null");

        /* Testing property annotation overrides */
        class TestObject {
            @JsonProperty("testProperty")
            private int testProperty;

            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            private static final int DEFAULT_VALUE = 123;

            public int getTestProperty() {
                return testProperty;
            }

            public void setTestProperty(int testProperty) {
                this.testProperty = testProperty;
            }
        }

        TestObject testObject = new TestObject();
        testObject.setTestProperty(456);
        BeanDescription beanDescription = _annotationIntrospector.findBeanDescription(TestObject.class, _config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _annotationIntrospector);
        BeanPropertyWriter writer2 = propertyBuilder.buildWriter(_config.getDefaultSerializerProvider(),
                null, TestObject.class, null, null, null, null, false);
        Object propertyDefaultValue = writer2.getDefaultSerializer().getNullValue();
        Assert.assertEquals(propertyDefaultValue, TestObject.DEFAULT_VALUE, "Property default value test");
    }

    @AfterClass
    public void teardown() {
        _config = null;
        _annotationIntrospector = null;
    }

    private static ObjectMapper _config;
    private static AnnotationIntrospector _annotationIntrospector;
}