package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.ClassCastException;
import java.lang.reflect.Type;

public class StdTypeResolverBuilderTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MapperFactory mapperFactory = new MapperFactory();
        objectMapper = mapperFactory.build();
    }

    @Test
    public void testStdTypeResolverBuilder_Property_Deserialization() throws IOException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .typeProperty("typeProperty");

        JavaType javaType = objectMapper.getTypeFactory()
                .constructType(stdClassWithTypeProperty.class);

        String json = "{\"data\": {\"typeProperty\": \"com.fasterxml.jackson.databind.jsontype.impl.MyClass1\"}}";
        MyClass1 myClass1 = objectMapper.readValue(json, javaType);

        Assert.assertEquals("Expected MyClass1", myClass1.getClass().getName(), MyClass1.class.getName());
    }

    @Test
    public void testStdTypeResolverBuilder_WrapperObject_Deserialization() throws IOException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .inclusion(JsonTypeInfo.As.WRAPPER_OBJECT)
                .typeProperty("typeProperty");

        JavaType javaType = objectMapper.getTypeFactory()
                .constructType(wrapperObjectClass.class);

        String json = "{\"@type\": \"com.fasterxml.jackson.databind.jsontype.impl.MyClass2\" , \"data\": \"Data\"}";
        MyClass2 myClass2 = objectMapper.readValue(json, javaType);

        Assert.assertEquals("Expected MyClass2", myClass2.getClass().getName(), MyClass2.class.getName());
    }

    @Test(expected = JsonMappingException.class)
    public void testStdTypeResolverBuilder_Invalid_Deserialization() throws IOException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .typeProperty("typeProperty");

        JavaType javaType = objectMapper.getTypeFactory()
                .constructType(stdClassWithTypeProperty.class);

        String json = "{\"data\": 123}"; // Invalid JSON, no 'typeProperty'

        objectMapper.readValue(json, javaType);
    }

    @Test
    public void testStdTypeResolverBuilder_Property_Serialization() throws IOException {
        MyClass1 myClass1 = new MyClass1();

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, myClass1.getClass(), null);

        String json = objectMapper.writerWithTypeSerializer(typeSerializer)
                .writeValueAsString(myClass1);

        MyClass1 deserializedMyClass1 = objectMapper.readValue(json, MyClass1.class);

        Assert.assertEquals("Expected MyClass1", myClass1, deserializedMyClass1);
    }

    @Test
    public void testStdTypeResolverBuilder_WrapperArray_Serialization() throws IOException {
        MyClass3[] myClass3Array = new MyClass3[2];
        myClass3Array[0] = new MyClass3(1);
        myClass3Array[1] = new MyClass3(2);

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.WRAPPER_ARRAY)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = builder.buildTypeSerializer(null,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MyClass3.class), null);

        String json = objectMapper.writerWithTypeSerializer(typeSerializer)
                .writeValueAsString(Arrays.asList(myClass3Array));

        List<MyClass3> deserializedMyClass3List = objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MyClass3.class));

        Assert.assertEquals("Expected MyClass3[2]", myClass3Array, deserializedMyClass3List.toArray());
    }

    @Test(expected = JsonMappingException.class)
    public void testStdTypeResolverBuilder_Invalid_Serialization() throws IOException {
        MyClass1 myClass1 = new MyClass1();

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, myClass1.getClass(), null);

        String json = objectMapper.writerWithTypeSerializer(typeSerializer)
                .writeValueAsString(new Object()); // Invalid type

        try {
            objectMapper.readValue(json, MyClass1.class);
        } catch (JsonMappingException e) {
            throw e;
        } catch (IOException e) {
            throw new AssertionError("Unexpected IOException", e);
        }
    }

    @Test
    public void testStdTypeResolverBuilder_NoTypeInfoBuilder() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory()
                .constructType(stdClassWithoutTypeInfo.class);

        String json = objectMapper.writeValueAsString(new MyClass4());
        MyClass4 myClass4 = objectMapper.readValue(json, javaType);

        Assert.assertEquals("Expected MyClass4", myClass4.getClass().getName(), MyClass4.class.getName());
    }

    private static class MyClass1 {
        private String data;

        public String getTypeProperty() {
            return "com.fasterxml.jackson.databind.jsontype.impl.MyClass1";
        }
    }

    private static class MyClass2 {
        private String data;

        public String getTypeName() {
            return "com.fasterxml.jackson.databind.jsontype.impl.MyClass2";
        }
    }

    private static class MyClass3 {
        private int data;

        public MyClass3(int data) {
            this.data = data;
        }

        public String getTypeProperty() {
            return "com.fasterxml.jackson.databind.jsontype.impl.MyClass3";
        }
    }

    private static class stdClassWithTypeProperty {
        private String typeProperty;
    }

    private static class wrapperObjectClass {
        public String data;
    }

    private static class stdClassWithoutTypeInfo {
    }

    private static class MyClass4 {
        private String data;
    }
}