package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testDefaultImplDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MapperFeature.DEFAULT_INT_AS_JAVA_INT = false; // to test int serialization
        String json = "{\"name\":\"John\",\"type\":\"ClassA\"}";
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeDeserializer();
        JavaType javaType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        BeanProperty property = new BeanProperty.Std("name", String.class);

        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        TypeDeserializerBase deserializer = typeDeserializerBase.forProperty(property);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        Object result = deserializer.deserialize(jp, ctxt);

        Assert.assertEquals("John", result);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MapperFeature.DEFAULT_INT_AS_JAVA_INT = false; // to test int serialization
        String json = "{\"name\":\"John\",\"type\":\"ClassB\"}";

        TypeDeserializerBase typeDeserializerBase = mapper.getTypeDeserializer();
        JavaType javaType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        BeanProperty property = new BeanProperty.Std("name", String.class);

        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        TypeDeserializerBase deserializer = typeDeserializerBase.forProperty(property);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        JsonDeserializer<Object> deserializerForTypeB = new MyJsonDeserializerForClassB();
        idResolver.setValueIdResolver(new MyCustomTypeIdResolver(deserializerForTypeB));

        Object result = deserializer.deserialize(jp, ctxt);

        Assert.assertEquals("John", result);
        Assert.assertTrue(result.getClass() == ClassB.class);
    }

    @Test
    public void testFindDeserializer_UnknownTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MapperFeature.DEFAULT_INT_AS_JAVA_INT = false; // to test int serialization
        String json = "{\"name\":\"John\",\"type\":\"UnknownType\"}";

        TypeDeserializerBase typeDeserializerBase = mapper.getTypeDeserializer();
        JavaType javaType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        BeanProperty property = new BeanProperty.Std("name", String.class);

        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        TypeDeserializerBase deserializer = typeDeserializerBase.forProperty(property);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        Object result = deserializer.deserialize(jp, ctxt);

        Assert.assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MapperFeature.DEFAULT_INT_AS_JAVA_INT = false; // to test int serialization
        String json = "{\"name\":\"John\",\"type\":\"[java.lang.Object]\"}";

        TypeDeserializerBase typeDeserializerBase = mapper.getTypeDeserializer();
        JavaType javaType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        BeanProperty property = new BeanProperty.Std("name", String.class);

        TypeIdResolver idResolver = new IdResolverForUnknownTypeId();
        TypeDeserializerBase deserializer = typeDeserializerBase.forProperty(property);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        Object result = deserializer.deserialize(jp, ctxt);

        Assert.assertNull(result);
    }

    @Test
    public void testNullifyingDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MapperFeature.DEFAULT_INT_AS_JAVA_INT = false; // to test int serialization
        String json = "null";

        TypeDeserializerBase typeDeserializerBase = mapper.getTypeDeserializer();
        JavaType javaType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        BeanProperty property = new BeanProperty.Std("name", String.class);

        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        TypeDeserializerBase deserializer = typeDeserializerBase.forProperty(property);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(json);

        Object result = deserializer.deserialize(jp, ctxt);

        Assert.assertEquals(null, result);
    }

    static class MyJsonDeserializerForClassB extends StdDeserializer<ClassB> {
        public MyJsonDeserializerForClassB() {
            this(null);
        }

        public MyJsonDeserializerForClassB(Class<?> vc) {
            super(ClassB.class, vc);
        }

        @Override
        public ClassB deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectNode node = p.getCodec().readTree(p);
            String name = node.get("name").asText();
            return new ClassB(name);
        }
    }

    static class MyCustomTypeIdResolver implements TypeIdResolver {
        private final JsonDeserializer<Object> _deserializer;

        public MyCustomTypeIdResolver(JsonDeserializer<Object> deserializer) {
            _deserializer = deserializer;
        }

        @Override
        public String idFromValue(Object value) {
            if (value.getClass() == ClassB.class) {
                return "ClassB";
            }
            return null;
        }

        @Override
        public Object valueFromId(String id, DeserializationContext ctxt) throws IOException {
            return _deserializer.deserialize(ctxt.getParser(), ctxt);
        }
    }

    static class IdResolverForUnknownTypeId implements TypeIdResolver {
        @Override
        public String idFromValue(Object value) {
            if (value instanceof String) {
                return "[java.lang.Object]";
            }
            return null;
        }

        @Override
        public Object valueFromId(String id, DeserializationContext ctxt) throws IOException {
            throw new RuntimeException("Unknown type id: " + id);
        }
    }

    static class ClassB {
        private final String name;

        public ClassB(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "ClassB{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}