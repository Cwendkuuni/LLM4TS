package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InnerClassPropertyTest {

    private InnerClassProperty property;
    private JsonDeserializer<Object> valueDeserializer;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;

    @Before
    public void setUp() throws Exception {
        Class<MyClass> clazz = MyClass.class;

        Constructor<MyInnerClass> innerConstructor = MyInnerClass.class.getDeclaredConstructor();
        innerConstructor.setAccessible(true);

        valueDeserializer = Mockito.mock(JsonDeserializer.class);
        AnnotatedConstructor<MyInnerClass> annotatedInnerConstructor = Mockito.mock(AnnotatedConstructor.class);
        Mockito.when(annotatedInnerConstructor.getAnnotated()).thenReturn(innerConstructor);

        property = new InnerClassProperty(new SettableBeanProperty(new AnnotatedField(clazz, "innerField")), annotatedInnerConstructor);

        objectMapper = new ObjectMapper();
        jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object arg = invocation.getArgument(0);
                if (arg instanceof MyClass) {
                    return new MyClass();
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }).when(objectMapper).readValueAs(clazz, JsonParser.class);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object arg = invocation.getArgument(0);
                if (arg instanceof MyClass) {
                    return property;
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }).when(objectMapper).findProperty(ArgumentMatchers.any(PropertyName.class));

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object arg = invocation.getArgument(0);
                if (arg instanceof MyClass) {
                    return new MyInnerClass();
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }).when(jsonParser).getValueAs(MyInnerClass.class);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object arg = invocation.getArgument(0);
                if (arg instanceof MyInnerClass) {
                    MyInnerClass inner = (MyInnerClass) arg;
                    inner.setName("Test Name");
                    return inner;
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }).when(valueDeserializer).deserialize(ArgumentMatchers.any(JsonParser.class), ArgumentMatchers.any(DeserializationContext.class), ArgumentMatchers.eq(MyInnerClass.class));

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object arg = invocation.getArgument(0);
                if (arg instanceof MyInnerClass) {
                    MyInnerClass inner = (MyInnerClass) arg;
                    property.set(arg, inner);
                    return inner;
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }).when(property).set(ArgumentMatchers.any(Object.class), ArgumentMatchers.any(Object.class));

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object arg = invocation.getArgument(0);
                if (arg instanceof MyInnerClass) {
                    MyInnerClass inner = (MyInnerClass) arg;
                    return inner.getName();
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }).when(property).get(ArgumentMatchers.any(Object.class));

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return property;
            }
        }).when(objectMapper).findProperty("innerField");

        Mockito.doAnswer(new Answer<JsonDeserializer<?>>() {
            @Override
            public JsonDeserializer<?> answer(InvocationOnMock invocation) throws Throwable {
                return valueDeserializer;
            }
        }).when(objectMapper).findValueDeserializer(MyInnerClass.class);
    }

    @Test
    public void testSerializationAndDeserialization() throws Exception {
        MyClass myClass = new MyClass();
        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(myClass);

        MyClass deserializedMyClass = objectMapper.readValue(json, MyClass.class);
        assertEquals(myClass, deserializedMyClass);

        assertEquals(property.get(myClass), property.get(deserializedMyClass));
    }

    @Test
    public void testNullValue() throws Exception {
        MyClass myClass = new MyClass();

        property.deserializeAndSet(jsonParser, context, myClass);
        assertNull(property.get(myClass));
    }

    @Test
    public void testTypeDeserializer() throws Exception {
        MyClass myClass = new MyClass();

        JsonDeserializer<MyInnerClass> myInnerClassDeserializer = Mockito.mock(JsonDeserializer.class);
        Mockito.when(valueDeserializer.deserializeWithType(jsonParser, context, myInnerClassDeserializer)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                Object arg = invocation.getArgument(0);
                MyInnerClass myInnerClass = new MyInnerClass();
                myInnerClassDeserializer.deserialize(jsonParser, context, myInnerClass);
                return myInnerClass;
            }
        });

        property.deserializeAndSet(jsonParser, context, myClass);

        MyInnerClass inner = property.get(myClass);
        assertEquals("Test Name", inner.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingConstructor() throws Exception {
        MyClass myClass = new MyClass();
        property = new InnerClassProperty(new SettableBeanProperty(new AnnotatedField(MyClass.class, "innerField")), null);

        property.deserializeAndSet(jsonParser, context, myClass);
    }

    @Test(expected = IOException.class)
    public void testDeserializationFailure() throws Exception {
        MyClass myClass = new MyClass();

        Mockito.doThrow(new IOException("Test Error")).when(jsonParser).getValueAs(MyInnerClass.class);

        property.deserializeAndSet(jsonParser, context, myClass);
    }

    static class MyClass {
        private MyInnerClass innerField;

        @JsonDeserialize(using = MyInnerClassDeserializer.class)
        public MyInnerClass getInnerField() {
            return innerField;
        }

        public void setInnerField(MyInnerClass value) {
            this.innerField = value;
        }
    }

    static class MyInnerClass {
        private String name;

        public MyInnerClass() {}

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class MyInnerClassDeserializer extends JsonDeserializer<MyInnerClass> {
        @Override
        public MyInnerClass deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            Converter.ValueConverter<String, MyInnerClass> converter = new Converter.StdConverter<String, MyInnerClass>() {
                @Override
                public MyInnerClass convert(String text) {
                    MyInnerClass myInnerClass = new MyInnerClass();
                    myInnerClass.setName(text);
                    return myInnerClass;
                }
            };
            return converter.convertValue(jsonParser.getValueAsString(), ctxt);
        }
    }
}