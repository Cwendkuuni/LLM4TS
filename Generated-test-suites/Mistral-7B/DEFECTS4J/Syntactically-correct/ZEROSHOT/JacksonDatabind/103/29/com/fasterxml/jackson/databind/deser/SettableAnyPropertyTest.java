package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseMapTest;

public class SettableAnyPropertyTest extends BaseMapTest {

    private ObjectMapper objectMapper;
    private DeserializationConfig config;
    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        config = objectMapper.getDeserializationConfig();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDescription = objectMapper.findBeanDescription(TestClass.class);
        settableAnyProperty = new SettableAnyProperty(beanDescription.findProperty("anyProperty"),
                beanDescription.findSetter(TestClass.class, "anyProperty"),
                objectMapper.getTypeFactory().constructType(TestClass.class),
                null,
                null,
                null);
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException {
        Object testInstance = new TestClass();
        String propName = "anyProperty";
        settableAnyProperty.deserializeAndSet(jsonParser, config, testInstance, propName);
        assertEquals(null, testInstance.getAnyProperty());
    }

    @Test
    public void testDeserializeAndSetValue() throws IOException {
        Object testInstance = new TestClass();
        String propName = "anyProperty";
        Object expectedValue = "expectedValue";
        settableAnyProperty.deserializeAndSet(jsonParser, config, testInstance, propName);
        assertEquals(expectedValue, testInstance.getAnyProperty());
    }

    @Test
    public void testDeserializeAndSetValueWithType() throws IOException {
        Object testInstance = new TestClass();
        String propName = "anyProperty";
        Object expectedValue = 123;
        TypeDeserializer typeDeserializer = objectMapper.getTypeDeserializerForParametricType(TestClassWithType.class, "anyProperty");
        SettableAnyProperty settableAnyPropertyWithType = new SettableAnyProperty(beanDescription.findProperty("anyProperty"),
                beanDescription.findSetter(TestClassWithType.class, "anyProperty"),
                objectMapper.getTypeFactory().constructType(TestClassWithType.class),
                null,
                null,
                typeDeserializer);
        jsonParser.setValue("123");
        settableAnyPropertyWithType.deserializeAndSet(jsonParser, config, testInstance, propName);
        assertEquals(expectedValue, testInstance.getAnyProperty());
    }

    @Test
    public void testDeserializeWithValueDeserializer() throws IOException {
        Object testInstance = new TestClass();
        String propName = "anyProperty";
        Object expectedValue = new TestClassInner();
        JsonDeserializer<Object> valueDeserializer = objectMapper.findValueDeserializer(TestClassInner.class);
        SettableAnyProperty settableAnyPropertyWithDeserializer = new SettableAnyProperty(beanDescription.findProperty("anyProperty"),
                beanDescription.findSetter(TestClass.class, "anyProperty"),
                objectMapper.getTypeFactory().constructType(TestClass.class),
                null,
                valueDeserializer,
                null);
        jsonParser.setValue("{\"innerProperty\":\"TestInnerClass\"}");
        settableAnyPropertyWithDeserializer.deserializeAndSet(jsonParser, config, testInstance, propName);
        assertEquals(expectedValue, testInstance.getAnyProperty());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnresolvedForwardReference() throws IOException {
        Object testInstance = new TestClass();
        String propName = "anyProperty";
        Object key = new Object();
        JsonDeserializer<Object> valueDeserializer = objectMapper.findValueDeserializer(TestClassInner.class);
        SettableAnyProperty settableAnyPropertyWithDeserializer = new SettableAnyProperty(beanDescription.findProperty("anyProperty"),
                beanDescription.findSetter(TestClass.class, "anyProperty"),
                objectMapper.getTypeFactory().constructType(TestClass.class),
                new KeyDeserializer() {
                    @Override
                    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                        if (key.equals("key")) {
                            Object id = ObjectIdReader.Id.fromString("key-id");
                            ObjectIdReader.Id idRef = new ObjectIdReader.IdImpl(id);
                            ctxt.putContextValue(ObjectIdReader.Id.class, idRef);
                            return key;
                        }
                        throw new IOException("Invalid key: " + key);
                    }
                },
                valueDeserializer,
                null);
        jsonParser.setValue("{\"key\": null, \"innerProperty\":\"TestInnerClass\"}");
        settableAnyPropertyWithDeserializer.deserializeAndSet(jsonParser, config, testInstance, propName);
    }

    @Test
    public void testSet() throws IOException {
        Object testInstance = new TestClass();
        String propName = "anyProperty";
        Object value = new TestClassInner();
        settableAnyProperty.set(testInstance, propName, value);
        assertEquals(value, testInstance.getAnyProperty());
    }

    @Test
    public void testSetWithMap() throws IOException {
        Object testInstance = new TestClass();
        String propName = "anyProperty";
        Object key = "key";
        Object value = new TestClassInner();
        settableAnyProperty._setter = new AnnotatedField(TestClass.class, "mapAnyProperty") {
            @Override
            public Object getValue(Object t) {
                return ((TestClass) t).getMapAnyProperty();
            }
        };
        Map<Object, Object> testMap = new HashMap<>();
        testMap.put(key, value);
        settableAnyProperty.set(testInstance, propName, key, testMap);
        assertEquals(value, testInstance.getAnyProperty());
    }

    @Test
    public void testToString() {
        assertEquals("[any property on class TestClass]", settableAnyProperty.toString());
    }

    static class TestClass {
        private Map<Object, Object> mapAnyProperty;
        private Object anyProperty;

        public Map<Object, Object> getMapAnyProperty() {
            if (mapAnyProperty == null) {
                mapAnyProperty = new HashMap<>();
            }
            return mapAnyProperty;
        }

        public Object getAnyProperty() {
            return anyProperty;
        }

        @JsonProperty("anyProperty")
        public void setAnyProperty(Object value) {
            this.anyProperty = value;
        }
    }

    static class TestClassInner {
        private String innerProperty;

        public String getInnerProperty() {
            return innerProperty;
        }

        public void setInnerProperty(String value) {
            this.innerProperty = value;
        }
    }

    static class TestClassWithType {
        private Map<Object, Object> mapAnyProperty;
        private Object anyProperty;

        public Map<Object, Object> getMapAnyProperty() {
            if (mapAnyProperty == null) {
                mapAnyProperty = new HashMap<>();
            }
            return mapAnyProperty;
        }

        public Object getAnyProperty() {
            return anyProperty;
        }

        @JsonProperty("anyProperty")
        public void setAnyProperty(Object value) {
            this.anyProperty = value;
        }

        @JsonTypeName("TestClassWithType")
        public TestClassWithType() {
        }
    }

}