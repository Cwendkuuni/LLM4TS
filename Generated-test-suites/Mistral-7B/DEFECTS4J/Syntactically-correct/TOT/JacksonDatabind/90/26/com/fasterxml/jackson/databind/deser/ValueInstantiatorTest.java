package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(JUnit3Runner.class)
public class ValueInstantiatorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testEmptyValueInstantiator() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Object.class);
        assertFalse(instantiator.canInstantiate());
        assertNull(instantiator.getDefaultCreator());
        assertEquals("UNKNOWN", instantiator.getValueTypeDesc());
    }

    @Test
    public void testCustomValueInstantiator() throws IOException {
        class MyValue {
            private String name;

            public MyValue(String name) {
                this.name = name;
            }
        }

        class MyValueInstantiator extends ValueInstantiator {
            @Override
            public Class<?> getValueClass() {
                return MyValue.class;
            }

            @Override
            public String getValueTypeDesc() {
                return "MyValue";
            }

            @Override
            public boolean canCreateFromString() {
                return true;
            }

            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return new MyValue(value);
            }
        }

        ValueInstantiator instantiator = new MyValueInstantiator();
        assertTrue(instantiator.canInstantiate());
        assertNotNull(instantiator.getDefaultCreator());
        assertEquals("MyValue", instantiator.getValueTypeDesc());

        String json = "{\"name\":\"John\"}";
        Object result = MAPPER.readValue(json, instantiator.getValueClass());
        assertInstanceOf(MyValue.class, result);
        assertEquals("John", ((MyValue) result).name);
    }

    @Test
    public void testCustomValueInstantiator_CreateFromObjectWith() throws IOException {
        class MyValue {
            private String name;
            private int age;

            public MyValue(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }

        class MyValueInstantiator extends ValueInstantiator {
            @Override
            public Class<?> getValueClass() {
                return MyValue.class;
            }

            @Override
            public String getValueTypeDesc() {
                return "MyValue";
            }

            @Override
            public boolean canCreateFromObjectWith() {
                return true;
            }

            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[] {
                        new SettableBeanProperty() {
                            @Override
                            public BeanPropertyType getBeanPropertyType() {
                                return BeanPropertyType.NORMAL;
                            }

                            @Override
                            public String getName() {
                                return "name";
                            }

                            @Override
                            public PropertyName getNameAsPropertyName() {
                                return new PropertyName("name");
                            }

                            @Override
                            public JsonDeserializer<Object> getValueDeserializer() {
                                return new StdDeserializer<String>() {
                                    @Override
                                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                                        return p.getValueAsString();
                                    }
                                };
                            }
                        },
                        new SettableBeanProperty() {
                            @Override
                            public BeanPropertyType getBeanPropertyType() {
                                return BeanPropertyType.NORMAL;
                            }

                            @Override
                            public String getName() {
                                return "age";
                            }

                            @Override
                            public PropertyName getNameAsPropertyName() {
                                return new PropertyName("age");
                            }

                            @Override
                            public JsonDeserializer<Object> getValueDeserializer() {
                                return new StdDeserializer<Integer>() {
                                    @Override
                                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                                        return p.getValueAsInt();
                                    }
                                };
                            }
                        }
                };
            }

            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                return new MyValue((String) args[0], (Integer) args[1]);
            }
        }

        ValueInstantiator instantiator = new MyValueInstantiator();
        assertTrue(instantiator.canInstantiate());
        assertNotNull(instantiator.getDefaultCreator());
        assertEquals("MyValue", instantiator.getValueTypeDesc());

        String json = "{\"name\":\"John\",\"age\":30}";
        Object result = MAPPER.readValue(json, instantiator.getValueClass());
        assertInstanceOf(MyValue.class, result);
        assertEquals("John", ((MyValue) result).name);
        assertEquals(30, ((MyValue) result).age);
    }

    @Test
    public void testValueInstantiator_CanCreateFromDifferentTypes() throws IOException {
        class MyValue {
            private String name;
            private int age;

            public MyValue(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }

        class MyValueInstantiator extends ValueInstantiator {
            @Override
            public Class<?> getValueClass() {
                return MyValue.class;
            }

            @Override
            public String getValueTypeDesc() {
                return "MyValue";
            }

            @Override
            public boolean canCreateFromInt() {
                return true;
            }

            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return new MyValue("", value);
            }

            @Override
            public boolean canCreateFromLong() {
                return true;
            }

            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return new MyValue("", (int) value);
            }

            @Override
            public boolean canCreateFromDouble() {
                return true;
            }

            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                return new MyValue("", (int) Math.round(value));
            }
        }

        ValueInstantiator instantiator = new MyValueInstantiator();
        assertTrue(instantiator.canInstantiate());
        assertNotNull(instantiator.getDefaultCreator());
        assertEquals("MyValue", instantiator.getValueTypeDesc());

        String jsonString = "{\"name\":\"John\"}";
        Object jsonInt = 30;
        Object jsonLong = 45L;
        Object jsonDouble = 50.5;

        Object resultString = MAPPER.readValue(jsonString, instantiator.getValueClass());
        assertInstanceOf(MyValue.class, resultString);
        assertEquals("John", ((MyValue) resultString).name);
        assertEquals(0, ((MyValue) resultString).age);

        Object resultInt = MAPPER.readValue(jsonIntString(jsonInt), instantiator.getValueClass());
        assertInstanceOf(MyValue.class, resultInt);
        assertEquals("", ((MyValue) resultInt).name);
        assertEquals(30, ((MyValue) resultInt).age);

        Object resultLong = MAPPER.readValue(jsonIntString(jsonLong), instantiator.getValueClass());
        assertInstanceOf(MyValue.class, resultLong);
        assertEquals("", ((MyValue) resultLong).name);
        assertEquals(45, ((MyValue) resultLong).age);

        Object resultDouble = MAPPER.readValue(jsonIntString(jsonDouble), instantiator.getValueClass());
        assertInstanceOf(MyValue.class, resultDouble);
        assertEquals("", ((MyValue) resultDouble).name);
        assertEquals(50, ((MyValue) resultDouble).age);
    }

    private static String jsonIntString(Object value) {
        return "{\"intValue\": " + value + "}";
    }

    @Test(expected = JsonProcessingException.class)
    public void testValueInstantiator_CannotCreateFromUnknownType() throws IOException {
        class MyValueInstantiator extends ValueInstantiator {
            @Override
            public Class<?> getValueClass() {
                return Object.class;
            }

            @Override
            public String getValueTypeDesc() {
                return "MyValue";
            }

            @Override
            public boolean canCreateFromString() {
                return true;
            }

            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return new Object();
            }
        }

        ValueInstantiator instantiator = new MyValueInstantiator();
        assertTrue(instantiator.canInstantiate());
        assertNotNull(instantiator.getDefaultCreator());
        assertEquals("MyValue", instantiator.getValueTypeDesc());

        String json = "{\"name\":\"John\"}";
        MAPPER.readValue(json, instantiator.getValueClass());
        fail("Should throw JsonProcessingException");
    }

    @Test(expected = JsonProcessingException.class)
    public void testValueInstantiator_CannotCreateFromUnknownType_ObjectWith() throws IOException {
        class MyValueInstantiator extends ValueInstantiator {
            @Override
            public Class<?> getValueClass() {
                return Object.class;
            }

            @Override
            public String getValueTypeDesc() {
                return "MyValue";
            }

            @Override
            public boolean canCreateFromObjectWith() {
                return true;
            }

            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[] { new SettableBeanProperty() {
                    @Override
                    public BeanPropertyType getBeanPropertyType() {
                        return BeanPropertyType.NORMAL;
                    }

                    @Override
                    public String getName() {
                        return "name";
                    }

                    @Override
                    public PropertyName getNameAsPropertyName() {
                        return new PropertyName("name");
                    }

                    @Override
                    public JsonDeserializer<Object> getValueDeserializer() {
                        return new StdDeserializer<String>() {
                            @Override
                            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                                return p.getValueAsString();
                            }
                        };
                    }
                } };
            }

            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                throw new JsonProcessingException("Cannot create instance of unknown type");
            }
        }

        ValueInstantiator instantiator = new MyValueInstantiator();
        assertTrue(instantiator.canInstantiate());
        assertNotNull(instantiator.getDefaultCreator());
        assertEquals("MyValue", instantiator.getValueTypeDesc());

        String json = "{\"name\":\"John\"}";
        MAPPER.readValue(json, instantiator.getValueClass());
        fail("Should throw JsonProcessingException");
    }
}