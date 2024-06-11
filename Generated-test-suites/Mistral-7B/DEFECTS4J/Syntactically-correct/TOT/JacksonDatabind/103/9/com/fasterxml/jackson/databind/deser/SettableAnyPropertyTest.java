package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.core.util.TestUtils.convertInputSource;
import static org.testng.Assert.*;

public class SettableAnyPropertyTest {

    private ObjectMapper mapper;

    @BeforeMethod
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSettableAnyProperty_SimpleType() throws IOException {
        BeanProperty property = new BeanProperty(null, "property", String.class, null);
        AnnotatedField setter = new AnnotatedField(MyPojo.class, "setProperty");
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        MyPojo pojo = new MyPojo();
        ObjectMapper originalMapper = new ObjectMapper();
        originalMapper.findAndRegisterModules();
        ObjectWriter writer = originalMapper.writerFor(MyPojo.class);
        String json = writer.writeValueAsString(pojo);

        JsonParser parser = mapper.getFactory().createParser(convertInputSource(json));
        SettableAnyProperty.DeserializationContext context = mapper.getDeserializationContext();
        SettableAnyProperty settable = settableAnyProperty.deserializeAndSet(parser, context, pojo, "property");
        assertEquals("expected", pojo.getProperty(), settable.getProperty().getValue(pojo));
    }

    @Test
    public void testSettableAnyProperty_MapType() throws IOException {
        BeanProperty property = new BeanProperty(null, "property", Map.class, null);
        AnnotatedField setter = new AnnotatedField(MyPojo.class, "setProperty");
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        MyPojo pojo = new MyPojo();
        ObjectMapper originalMapper = new ObjectMapper();
        originalMapper.findAndRegisterModules();
        ObjectWriter writer = originalMapper.writerFor(MyPojo.class);
        String json = writer.writeValueAsString(pojo);

        JsonParser parser = mapper.getFactory().createParser(convertInputSource(json));
        SettableAnyProperty.DeserializationContext context = mapper.getDeserializationContext();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        SettableAnyProperty settable = settableAnyProperty.deserializeAndSet(parser, context, pojo, "property");
        assertEquals(map, pojo.getProperty());
    }

    @Test
    public void testSettableAnyProperty_NullValue() throws IOException {
        BeanProperty property = new BeanProperty(null, "property", String.class, null);
        AnnotatedField setter = new AnnotatedField(MyPojo.class, "setProperty");
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        MyPojo pojo = new MyPojo();
        ObjectMapper originalMapper = new ObjectMapper();
        originalMapper.findAndRegisterModules();
        ObjectWriter writer = originalMapper.writerFor(MyPojo.class);
        String json = writer.writeValueAsString(pojo);

        JsonParser parser = mapper.getFactory().createParser(convertInputSource(json));
        SettableAnyProperty.DeserializationContext context = mapper.getDeserializationContext();
        SettableAnyProperty settable = settableAnyProperty.deserializeAndSet(parser, context, pojo, "property");
        assertNull(pojo.getProperty());
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSettableAnyProperty_InvalidType() throws IOException {
        BeanProperty property = new BeanProperty(null, "property", Integer.class, null);
        AnnotatedField setter = new AnnotatedField(MyPojo.class, "setProperty");
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        MyPojo pojo = new MyPojo();
        ObjectMapper originalMapper = new ObjectMapper();
        originalMapper.findAndRegisterModules();
        ObjectWriter writer = originalMapper.writerFor(MyPojo.class);
        String json = writer.writeValueAsString(pojo);

        JsonParser parser = mapper.getFactory().createParser(convertInputSource(json));
        SettableAnyProperty.DeserializationContext context = mapper.getDeserializationContext();
        Object value = mapper.readTree(parser);

        SettableAnyProperty settable = settableAnyProperty.deserializeAndSet(parser, context, pojo, "property");
        fail("Expected JsonMappingException but got " + settable);
    }

    @Test
    public void testSettableAnyProperty_ValueDeserializer() throws IOException {
        BeanProperty property = new BeanProperty(null, "property", String.class, null);
        AnnotatedField setter = new AnnotatedField(MyPojo.class, "setProperty");
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        MyPojo pojo = new MyPojo();
        ObjectMapper originalMapper = new ObjectMapper();
        originalMapper.findAndRegisterModules();
        ObjectWriter writer = originalMapper.writerFor(MyPojo.class);
        String json = writer.writeValueAsString(pojo);

        JsonParser parser = mapper.getFactory().createParser(convertInputSource(json));
        SettableAnyProperty.DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
        SettableAnyProperty settable = settableAnyProperty.withValueDeserializer(deserializer).deserializeAndSet(parser, context, pojo, "property");
        assertEquals("expected", "expected", settable.getProperty().getValue(pojo));
    }

    @Test
    public void testSettableAnyProperty_ValueDeserializerWithType() throws IOException {
        BeanProperty property = new BeanProperty(null, "property", String.class, null);
        AnnotatedField setter = new AnnotatedField(MyPojo.class, "setProperty");
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        MyPojo pojo = new MyPojo();
        ObjectMapper originalMapper = new ObjectMapper();
        originalMapper.findAndRegisterModules();
        ObjectWriter writer = originalMapper.writerFor(MyPojo.class);
        String json = writer.writeValueAsString(pojo);

        JsonParser parser = mapper.getFactory().createParser(convertInputSource(json));
        SettableAnyProperty.DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new StandardDeserializer<Integer>(Integer.class);
        TypeDeserializer typeDeserializer = new StdTypeDeserializer();
        SettableAnyProperty settable = settableAnyProperty.withValueDeserializer(deserializer, typeDeserializer).deserializeAndSet(parser, context, pojo, "property");
        assertEquals(42, ((Map) settable.getProperty().getValue(pojo)).get("value"));
    }

    @Test
    public void testSettableAnyProperty_KeyDeserializer() throws IOException {
        BeanProperty property = new BeanProperty(null, "property", String.class, null);
        AnnotatedField setter = new AnnotatedField(MyPojo.class, "setProperty");
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        MyPojo pojo = new MyPojo();
        ObjectMapper originalMapper = new ObjectMapper();
        originalMapper.findAndRegisterModules();
        ObjectWriter writer = originalMapper.writerFor(MyPojo.class);
        String json = writer.writeValueAsString(pojo);

        JsonParser parser = mapper.getFactory().createParser(convertInputSource(json));
        SettableAnyProperty.DeserializationContext context = mapper.getDeserializationContext();
        KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
        SettableAnyProperty settable = settableAnyProperty.withKeyDeserializer(keyDeserializer).deserializeAndSet(parser, context, pojo, "property");
        assertEquals("key1", ((Map) settable.getProperty().getValue(pojo)).keySet().iterator().next());
    }

    @Test
    public void testSettableAnyProperty_JsonMappingException() throws IOException {
        BeanProperty property = new BeanProperty(null, "property", String.class, null);
        AnnotatedField setter = new AnnotatedField(MyPojo.class, "setProperty");
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        MyPojo pojo = new MyPojo();
        ObjectMapper originalMapper = new ObjectMapper();
        originalMapper.findAndRegisterModules();
        ObjectWriter writer = originalMapper.writerFor(MyPojo.class);
        String json = writer.writeValueAsString(pojo);

        JsonParser parser = mapper.getFactory().createParser(convertInputSource(json));
        SettableAnyProperty.DeserializationContext context = mapper.getDeserializationContext();
        context.putAnnotationIntrospector(new StandardAnnotationIntrospector());
        context.findValueDeserializer(MyPojo.class, "setProperty");
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume "property"
        parser.nextToken(); // consume VALUE_STRING
        parser.nextToken(); // consume END_OBJECT

        SettableAnyProperty settable = settableAnyProperty.deserializeAndSet(parser, context, pojo, "property");
        fail("Expected JsonMappingException but got " + settable);
    }
}

class MyPojo {
    private Map<String, Object> property;

    public Map<String, Object> getProperty() {
        return property;
    }

    public void setProperty(Map<String, Object> property) {
        this.property = property;
    }
}