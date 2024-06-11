package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ValueInstantiatorTest {

    private ValueInstantiator instantiator;
    private ObjectMapper mapper;

    @BeforeClass
    public void setup() throws Exception {
        instantiator = new StdValueInstantiator(ValueInstantiator.Base.class);
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultInstance() throws IOException {
        Object result = instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals(result.getClass(), Object.class);
    }

    @Test(expectedExceptions = JsonProcessingException.class)
    public void testCannotCreateFromObjectWithNull() throws IOException {
        instantiator = new MyValueInstantiator();
        SettableBeanProperty[] properties = new SettableBeanProperty[]{new SettableBeanProperty.Std("name")};
        instantiator.createFromObjectWith(mapper.getDeserializationContext(), properties, null);
        fail("Expected JsonProcessingException");
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        instantiator = new MyValueInstantiator();
        Object value = new Object();
        SettableBeanProperty[] properties = new SettableBeanProperty[]{new SettableBeanProperty.Std("name", String.class), new SettableBeanProperty.Std("age", Integer.class)};
        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), properties, value);
        assertNotNull(result);
        assertEquals(result.getClass(), MyValue.class);
        assertEquals(result.getProperty("name"), "value");
        assertEquals(result.getProperty("age"), 42);
    }

    @Test(expectedExceptions = JsonProcessingException.class)
    public void testCreateFromStringEmpty() throws IOException {
        instantiator = new MyValueInstantiator();
        instantiator.createFromString(mapper.getDeserializationContext(), "");
        fail("Expected JsonProcessingException");
    }

    @Test
    public void testCreateFromString() throws IOException {
        instantiator = new MyValueInstantiator();
        String jsonString = "{\"name\":\"value\",\"age\":42}";
        Object result = instantiator.createFromString(mapper.getDeserializationContext(), jsonString);
        assertNotNull(result);
        assertEquals(result.getClass(), MyValue.class);
        assertEquals(result.getProperty("name"), "value");
        assertEquals(result.getProperty("age"), 42);
    }

    @Test
    public void testCreateFromInt() throws IOException {
        instantiator = new MyValueInstantiator();
        int value = 42;
        Object result = instantiator.createFromInt(mapper.getDeserializationContext(), value);
        assertNotNull(result);
        assertEquals(result.getClass(), MyValue.class);
        assertEquals(result.getProperty("name"), "valueFromInt");
        assertEquals(result.getProperty("age"), value);
    }

    @Test
    public void testCreateFromLong() throws IOException {
        instantiator = new MyValueInstantiator();
        long value = 42L;
        Object result = instantiator.createFromLong(mapper.getDeserializationContext(), value);
        assertNotNull(result);
        assertEquals(result.getClass(), MyValue.class);
        assertEquals(result.getProperty("name"), "valueFromLong");
        assertEquals(result.getProperty("age"), value);
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        instantiator = new MyValueInstantiator();
        double value = 42.0;
        Object result = instantiator.createFromDouble(mapper.getDeserializationContext(), value);
        assertNotNull(result);
        assertEquals(result.getClass(), MyValue.class);
        assertEquals(result.getProperty("name"), "valueFromDouble");
        assertEquals(result.getProperty("age"), value);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        instantiator = new MyValueInstantiator();
        boolean value = true;
        Object result = instantiator.createFromBoolean(mapper.getDeserializationContext(), value);
        assertNotNull(result);
        assertEquals(result.getClass(), MyValue.class);
        assertEquals(result.getProperty("name"), "valueFromBoolean");
        assertEquals(result.getProperty("age"), value);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        instantiator = new MyValueInstantiatorWithDelegate();
        Object result = instantiator.createUsingDelegate(mapper.getDeserializationContext(), new Object());
        assertNotNull(result);
        assertEquals(result.getClass(), MyValue.class);
        assertEquals(result.getProperty("name"), "delegatedName");
        assertEquals(result.getProperty("age"), 42);
    }

    static class MyValue {
        private String name;
        private int age;

        public MyValue(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Object getProperty(String name) {
            switch (name) {
                case "name":
                    return name;
                case "age":
                    return age;
                default:
                    throw new IllegalArgumentException("Unknown property: " + name);
            }
        }
    }

    static class MyValueInstantiator extends ValueInstantiator.Std {
        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[]{new SettableBeanProperty.Std("name", String.class), new SettableBeanProperty.Std("age", Integer.class)};
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            AnnotatedClass ac = AnnotatedClass.construct(getValueClass(), ctxt.getAnnotationIntrospector());
            TypeSerializer ts = mapper.getTypeSerializer();
            SettableBeanProperty[] props = getFromObjectArguments(ctxt);
            if (props.length == 0) {
                return createUsingDefault(ctxt);
            }
            Object[] values = mapper.readValue(ctxt.getParser(), ValueInstantiator.ValueType.VALUE_ARRAY_TYPE);
            for (int i = 0; i < props.length; i++) {
                PropertyValueBuffer buffer = ctxt.allocateProperties();
                SettableBeanProperty prop = props[i];
                buffer.setAndBind(prop.getMember(), values[i]);
            }
            Object result = createFromObjectWith(ctxt, buffer.getProperties());
            if (ac.hasMarkerAnnotation(JsonCreator.class)) {
                MyValue creator = ac.getAnnotation(JsonCreator.class).value();
                if (creator != null) {
                    try {
                        result = creator.createInstance(ctxt, this, result);
                    } catch (Exception e) {
                        throw ctxt.weirdValueException(String.format("Creator method '%s#%s()' threw exception", ac.getRawClass(), creator.getClass().getName()), e);
                    }
                }
            }
            return result;
        }
    }

    static class MyValueInstantiatorWithDelegate extends MyValueInstantiator {
        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return new MyValue("delegatedName", 42);
        }
    }
}