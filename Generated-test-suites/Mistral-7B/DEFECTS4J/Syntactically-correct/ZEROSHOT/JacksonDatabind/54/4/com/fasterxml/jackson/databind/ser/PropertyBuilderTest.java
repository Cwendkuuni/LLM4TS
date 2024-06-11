package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private SerializationConfig serializationConfig;
    private BeanDescription beanDescription;

    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        objectMapper = new ObjectMapper();
        serializationConfig = objectMapper.getSerializationConfig();
        beanDescription = new BeanDescription(MyClass.class);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = beanDescription.findPropertyDefinition("myProperty");
        JavaType declaredType = propDef.getType();

        JsonSerializer<?> jsonSerializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString("Serialized value");
            }
        };

        TypeSerializer typeSerializer = null;
        AnnotatedMember am = new AnnotatedMember(MyClass.class.getField("myProperty")) {
            @JsonSerialize(using = JsonSerializer.None.class)
            public Object getValue(Object t) {
                return null;
            }
        };

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, null, am, false);
        Assert.assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

        Annotated a = new AnnotatedMember(MyClass.class.getField("myProperty")) {
            @JsonSerialize(using = MyJsonSerializer.class)
            public Class<?> getAnnotatedClass() {
                return MyJsonSerializer.class;
            }
        };

        JavaType declaredType = propDescription.getType();
        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertEquals(MyJsonSerializer.class, result.getRawClass());

        MyJsonSerializer myJsonSerializer = new MyJsonSerializer();
        result = propertyBuilder.findSerializationType(a, true, declaredType);
        Assert.assertEquals(MyJsonSerializer.class, result.getRawClass());
    }

    @Test
    public void testGetDefaultBean() throws InstantiationException, IllegalAccessException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);

        MyClass myClass = (MyClass) propertyBuilder.getDefaultBean();
        Assert.assertNotNull(myClass);
        Assert.assertEquals("Default value", myClass.getDefaultValue());
    }

    @Test
    public void testGetPropertyDefaultValue() throws InstantiationException, IllegalAccessException, IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

        Object defaultBean = propertyBuilder.getDefaultBean();
        MyClass myClass = (MyClass) defaultBean;

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("myProperty", new AnnotatedMember(MyClass.class.getField("myProperty")) {
        }, propDescription);

        Assert.assertEquals("Default value", propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        Object defaultValue = propertyBuilder.getDefaultValue(javaType);

        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultValueIllegalArgumentException() throws InstantiationException, IllegalAccessException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

        JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
        propertyBuilder.getDefaultValue(javaType);
    }

    private static class MyClass {
        private static int myStaticInt = 1;
        private int myInt;
        private String myString;
        private MyOtherClass myOtherClass;
        private MyArray myArray;

        private MyClass() {
        }

        public MyClass(int myInt, String myString, MyOtherClass myOtherClass, MyArray myArray) {
            this.myInt = myInt;
            this.myString = myString;
            this.myOtherClass = myOtherClass;
            this.myArray = myArray;
        }

        public int getMyInt() {
            return myInt;
        }

        public String getMyString() {
            return myString;
        }

        public MyOtherClass getMyOtherClass() {
            return myOtherClass;
        }

        public MyArray getMyArray() {
            return myArray;
        }

        public static class MyOtherClass {
        }

        public static class MyArray {
        }

        private static String defaultValue = "Default value";

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    @JsonSerialize(using = JsonSerializer.None.class)
    private static class MyJsonSerializer {
    }
}

class PropDescription {
    JavaType type;

    PropDescription(JavaType type) {
        this.type = type;
    }

    BeanPropertyDefinition getBeanPropertyDefinition() {
        return new BeanPropertyDefinition() {};
    }
}