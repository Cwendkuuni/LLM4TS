package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyBasedCreator;
import com.fasterxml.jackson.databind.PropertyValueBuffer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializationContext;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExternalTypeHandlerTest {

    private static final ObjectMapper objectMapper;
    private static final TypeDeserializer stringTypeDeserializer;
    private static final TypeDeserializer numberTypeDeserializer;
    private static final TypeDeserializer booleanTypeDeserializer;
    private static final TypeDeserializer customTypeDeserializer;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        stringTypeDeserializer = mock(TypeDeserializer.class);
        numberTypeDeserializer = mock(TypeDeserializer.class);
        booleanTypeDeserializer = mock(TypeDeserializer.class);
        customTypeDeserializer = mock(TypeDeserializer.class);
        when(stringTypeDeserializer.getPropertyName()).thenReturn("string");
        when(numberTypeDeserializer.getPropertyName()).thenReturn("number");
        when(booleanTypeDeserializer.getPropertyName()).thenReturn("boolean");
        when(customTypeDeserializer.getPropertyName()).thenReturn("custom");
    }

    private ExternalTypeHandler externalTypeHandler;
    private PropertyBasedCreator creator;

    @BeforeMethod
    public void setUp() throws Exception {
        List<BeanPropertyDefinition> properties = Arrays.asList(
                new BeanPropertyDefinition("stringProperty", String.class, stringTypeDeserializer),
                new BeanPropertyDefinition("numberProperty", Integer.class, numberTypeDeserializer),
                new BeanPropertyDefinition("booleanProperty", Boolean.class, booleanTypeDeserializer),
                new BeanPropertyDefinition("customProperty", MyCustomClass.class, customTypeDeserializer)
        );
        externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(
                new AnnotatedMember(new AnnotatedClass(MyClass.class), "stringProperty", new AnnotatedMember.StdGetter(), null),
                stringTypeDeserializer)
                .addExternal(
                        new AnnotatedMember(new AnnotatedClass(MyClass.class), "numberProperty", new AnnotatedMember.StdGetter(), null),
                        numberTypeDeserializer)
                .addExternal(
                        new AnnotatedMember(new AnnotatedClass(MyClass.class), "booleanProperty", new AnnotatedMember.StdGetter(), null),
                        booleanTypeDeserializer)
                .addExternal(
                        new AnnotatedMember(new AnnotatedClass(MyClass.class), "customProperty", new AnnotatedMember.StdGetter(), null),
                        customTypeDeserializer)
                .build());

        Map<String, Object> creatorMap = new HashMap<>();
        creatorMap.put("MyClass", new MyClassCreator());
        creator = new PropertyBasedCreator(MyClass.class, creatorMap);
    }

    @Test
    public void testHandleTypePropertyValue() throws JsonProcessingException {
        String json = "{\"typeProperty\":\"string\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        MyClass myClass = new MyClass();
        Assert.assertTrue(externalTypeHandler.handleTypePropertyValue(jsonParser, context, "typeProperty", myClass));
        Assert.assertEquals(jsonParser.getValueAsString(), "string", myClass.getStringProperty());
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testHandleTypePropertyValueWithMissingProperty() throws JsonProcessingException {
        String json = "{\"missingProperty\":\"string\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        MyClass myClass = new MyClass();
        externalTypeHandler.handleTypePropertyValue(jsonParser, context, "missingProperty", myClass);
    }

    @Test
    public void testHandleTypePropertyValueWithMissingTypeId() throws JsonProcessingException {
        String json = "{\"typeProperty\":null}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        MyClass myClass = new MyClass();
        Assert.assertFalse(externalTypeHandler.handleTypePropertyValue(jsonParser, context, "typeProperty", myClass));
    }

    @Test
    public void testHandleTypePropertyValueWithWrongTypeId() throws JsonProcessingException {
        String json = "{\"typeProperty\":\"number\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        MyClass myClass = new MyClass();
        Assert.assertFalse(externalTypeHandler.handleTypePropertyValue(jsonParser, context, "typeProperty", myClass));
    }

    @Test
    public void testHandlePropertyValue() throws JsonProcessingException {
        String json = "{\"stringProperty\":\"hello\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        MyClass myClass = new MyClass();
        Assert.assertTrue(externalTypeHandler.handlePropertyValue(jsonParser, context, "stringProperty", myClass));
        Assert.assertEquals("hello", myClass.getStringProperty());
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testHandlePropertyValueWithMissingProperty() throws JsonProcessingException {
        String json = "{\"missingProperty\":\"hello\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        MyClass myClass = new MyClass();
        externalTypeHandler.handlePropertyValue(jsonParser, context, "missingProperty", myClass);
    }

    @Test
    public void testHandlePropertyValueWithMissingTypeId() throws JsonProcessingException {
        String json = "{\"stringProperty\":null}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        MyClass myClass = new MyClass();
        externalTypeHandler.handlePropertyValue(jsonParser, context, "stringProperty", myClass);
        Assert.assertNull(myClass.getStringProperty());
    }

    @Test
    public void testHandlePropertyValueWithWrongTypeId() throws JsonProcessingException {
        String json = "{\"numberProperty\":\"42\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        MyClass myClass = new MyClass();
        externalTypeHandler.handlePropertyValue(jsonParser, context, "numberProperty", myClass);
        Assert.assertEquals(42, myClass.getNumberProperty());
    }

    @Test
    public void testComplete() throws JsonProcessingException {
        String json = "{\"stringProperty\":\"hello\",\"numberProperty\":42,\"booleanProperty\":true}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        MyClass myClass = externalTypeHandler.start().complete(jsonParser, context, null);
        Assert.assertEquals("hello", myClass.getStringProperty());
        Assert.assertEquals(42, myClass.getNumberProperty());
        Assert.assertTrue(myClass.getBooleanProperty());
    }

    @Test
    public void testCompleteWithCreator() throws JsonProcessingException {
        String json = "[{\"name\":\"John\",\"age\":30,\"myCustomProperty\": \"customValue\"},{\"name\":\"Mary\",\"age\":25}]";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        MyClass[] result = externalTypeHandler.start().complete(jsonParser, context, buffer, creator);
        Assert.assertEquals(2, result.length);
        for (MyClass myClass : result) {
            Assert.assertNotNull(myClass);
            Assert.assertNotNull(myClass.getStringProperty());
            Assert.assertNotNull(myClass.getNumberProperty());
            Assert.assertNotNull(myClass.getBooleanProperty());
            Assert.assertNotNull(myClass.getCustomProperty());
        }
    }

    @Test
    public void testCompleteWithMissingTypeId() throws JsonProcessingException {
        String json = "{\"name\":\"John\",\"age\":30}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        MyClass myClass = externalTypeHandler.start().complete(jsonParser, context, buffer, creator);
        Assert.assertNotNull(myClass);
        Assert.assertNotNull(myClass.getStringProperty());
        Assert.assertEquals(30, myClass.getNumberProperty());
        Assert.assertTrue(myClass.getBooleanProperty());
        // customProperty is null because there was no typeId for it
        Assert.assertNull(myClass.getCustomProperty());
    }

    @Test
    public void testCompleteWithMissingProperty() throws JsonProcessingException {
        String json = "{\"age\":30}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = new StdDeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        MyClass myClass = externalTypeHandler.start().complete(jsonParser, context, buffer, creator);
        // missing "name" property, should still create instance with default values
        Assert.assertNotNull(myClass);
        Assert.assertEquals(30, myClass.getNumberProperty());
        Assert.assertTrue(myClass.getBooleanProperty());
        Assert.assertNull(myClass.getStringProperty());
        Assert.assertNull(myClass.getCustomProperty());
    }

    private static class MyClass {
        private String stringProperty;
        private Integer numberProperty;
        private Boolean booleanProperty;
        private MyCustomClass customProperty;

        public String getStringProperty() {
            return stringProperty;
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        public Integer getNumberProperty() {
            return numberProperty;
        }

        public void setNumberProperty(Integer numberProperty) {
            this.numberProperty = numberProperty;
        }

        public Boolean getBooleanProperty() {
            return booleanProperty;
        }

        public void setBooleanProperty(Boolean booleanProperty) {
            this.booleanProperty = booleanProperty;
        }

        public MyCustomClass getCustomProperty() {
            return customProperty;
        }

        public void setCustomProperty(MyCustomClass customProperty) {
            this.customProperty = customProperty;
        }
    }

    private static class MyClassCreator implements PropertyBasedCreator {
        @Override
        public Object createFromProperties(DeserializationContext ctxt,
                                            PropertyValueBuffer pvb) throws JsonMappingException {
            Object result = new MyClass();
            pvb.assignParameter(result::setStringProperty, pvb.getNextValue());
            pvb.assignParameter(result::setNumberProperty, pvb.getNextValue());
            pvb.assignParameter(result::setBooleanProperty, pvb.getNextValue());
            pvb.assignParameter(result::setCustomProperty, pvb.getNextValue());
            return result;
        }
    }

    private static class MyCustomClass {
        private String customProperty;

        public String getCustomProperty() {
            return customProperty;
        }

        public void setCustomProperty(String customProperty) {
            this.customProperty = customProperty;
        }
    }
}