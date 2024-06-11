package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        typeDeserializerBase = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class)
                .getConstructors()
                .get(0)
                .newInstance(
                        JsonTypeFactory.baseJavaType(String.class),
                        mapper.getTypeIdResolver(),
                        "typeProperty",
                        true,
                        JsonTypeFactory.baseJavaType(String.class)
                );
    }

    @Test
    public void testForProperty() throws JsonProcessingException, IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        BeanProperty property = mapper.findProperty("typeProperty", String.class);
        TypeDeserializerBase forProperty = typeDeserializerBase.forProperty(property);
        Assert.assertNotNull(forProperty);
        Assert.assertEquals(javaType, forProperty.baseType());
        Assert.assertEquals(mapper.getTypeIdResolver(), forProperty.getTypeIdResolver());
        Assert.assertEquals("typeProperty", forProperty.getPropertyName());
        Assert.assertEquals(true, forProperty.isTypeIdVisible());
    }

    @Test
    public void testGetTypeInclusion() throws JsonProcessingException, IOException {
        JsonTypeInfo.As inclusion = typeDeserializerBase.getTypeInclusion();
        Assert.assertEquals(JsonTypeInfo.As.WRAP_ONLY, inclusion);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownTypeId() throws JsonProcessingException, IOException {
        String json = "{\"typeId\":\"UnknownType\",\"value\":\"HelloWorld\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        typeDeserializerBase.handleUnknownTypeId(parser.getCurrentName(), parser, mapper.getDeserializationContext());
    }

    @Test
    public void testMissingTypeId() throws JsonProcessingException, IOException {
        String json = "{\"value\":\"HelloWorld\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = typeDeserializerBase.handleMissingTypeId(parser.getCurrentName(), mapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidTypeId() throws JsonProcessingException, IOException {
        String json = "{\"typeId\":\"InvalidTypeId\",\"value\":\"HelloWorld\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        typeDeserializerBase.deserialize(parser, mapper.getDeserializationContext(), null);
    }

    @Test
    public void testDeserializeWithValidTypeId() throws JsonProcessingException, IOException {
        String json = "{\"typeId\":\"StringType\",\"value\":\"HelloWorld\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        String result = (String) typeDeserializerBase.deserialize(parser, mapper.getDeserializationContext(), null);
        Assert.assertEquals("HelloWorld", result);
    }

    @Test
    public void testDeserializeWithNullTypeId() throws JsonProcessingException, IOException {
        String json = "{\"value\":\"HelloWorld\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        String result = (String) typeDeserializerBase.deserialize(parser, mapper.getDeserializationContext(), null);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testFindDeserializer() throws JsonProcessingException, IOException {
        String json = "{\"@class\":\"com.example.MyClass\",\"value\":\"HelloWorld\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        ObjectNode node = (ObjectNode) mapper.readTree(json);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class)
                .getConstructors()
                .get(0)
                .newInstance(
                        JsonTypeFactory.baseJavaType(Object.class),
                        mapper.getTypeIdResolver(),
                        "typeProperty",
                        true,
                        mapper.getTypeFactory()
                                .constructType(MyClass.class)
                );
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(mapper.getDeserializationContext(), node.get("@class").asText());
        Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertEquals(new MyClass(), result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws JsonProcessingException, IOException {
        String json = "null";
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonDeserializer<Object> deserializer = typeDeserializerBase.findDefaultImplDeserializer(mapper.getDeserializationContext());
        Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws JsonProcessingException, IOException {
        JsonFactory factory = new JsonFactory();
        factory.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonParser parser = factory.createParser("[\"NativeTypeId\",1]");
        Object result = typeDeserializerBase.deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
        Assert.assertEquals(1, result);
    }

    private static class MyClass {
    }
}