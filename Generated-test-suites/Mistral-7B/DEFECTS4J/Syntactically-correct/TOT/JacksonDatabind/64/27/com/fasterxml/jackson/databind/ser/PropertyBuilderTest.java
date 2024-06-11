package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private ObjectMapper _objectMapper;

    @Before
    public void setup() throws IOException {
        _config = new SerializationConfig();
        _config.setSerializationInclusion(JsonInclude.Value.ALWAYS);
        _objectMapper = new ObjectMapper(_config);
    }

    @Test
    public void testEmptyListSerialization() throws IOException {
        PropertyBuilder builder = new PropertyBuilder(_config, new BeanDescription(List.class));
        ObjectWriter writer = _objectMapper.writerWith(builder);
        List<Integer> emptyList = new ArrayList<>();

        String json = writer.writeValueAsString(emptyList);
        Assert.assertEquals("[]", json);
    }

    @Test
    public void testNullListSerialization() throws IOException {
        PropertyBuilder builder = new PropertyBuilder(_config, new BeanDescription(List.class));
        ObjectWriter writer = _objectMapper.writerWith(builder);
        List<Integer> nullList = null;

        String json = writer.writeValueAsString(nullList);
        Assert.assertEquals("null", json);
    }

    @Test
    public void testEmptyMapSerialization() throws IOException {
        PropertyBuilder builder = new PropertyBuilder(_config, new BeanDescription(HashMap.class));
        ObjectWriter writer = _objectMapper.writerWith(builder);
        Map<String, Integer> emptyMap = new HashMap<>();

        String json = writer.writeValueAsString(emptyMap);
        Assert.assertEquals("{}", json);
    }

    @Test
    public void testNullMapSerialization() throws IOException {
        PropertyBuilder builder = new PropertyBuilder(_config, new BeanDescription(HashMap.class));
        ObjectWriter writer = _objectMapper.writerWith(builder);
        Map<String, Integer> nullMap = null;

        String json = writer.writeValueAsString(nullMap);
        Assert.assertEquals("null", json);
    }

    @Test
    public void testIntegerDefaultValueSerialization() throws IOException {
        JsonInclude.Include inclusion = JsonInclude.Include.NON_DEFAULT;
        PropertyBuilder builder = new PropertyBuilder(_config, new BeanDescription(MyClass.class));
        builder._useRealPropertyDefaults = true;

        ObjectWriter writer = _objectMapper.writerWith(builder);
        MyClass obj = new MyClass();
        obj.intValue = 10;

        String json = writer.writeValueAsString(obj);
        Assert.assertEquals("{\"intValue\":10}", json);

        MyClass defaultObj = builder.getDefaultBean();
        if (defaultObj instanceof MyClass) {
            obj = (MyClass) defaultObj;
            obj.intValue = null;
        }

        json = writer.writeValueAsString(obj);
        Assert.assertEquals("{\"intValue\":0}", json);
    }

    @Test
    public void testStringDefaultValueSerialization() throws IOException {
        JsonInclude.Include inclusion = JsonInclude.Include.NON_DEFAULT;
        PropertyBuilder builder = new PropertyBuilder(_config, new BeanDescription(MyClass.class));
        builder._useRealPropertyDefaults = true;

        ObjectWriter writer = _objectMapper.writerWith(builder);
        MyClass obj = new MyClass();
        obj.stringValue = "test";

        String json = writer.writeValueAsString(obj);
        Assert.assertEquals("{\"stringValue\":\"test\"}", json);

        MyClass defaultObj = builder.getDefaultBean();
        if (defaultObj instanceof MyClass) {
            obj = (MyClass) defaultObj;
            obj.stringValue = null;
        }

        json = writer.writeValueAsString(obj);
        Assert.assertEquals("{\"stringValue\":\"\"}", json);
    }

    @Test
    public void testListDefaultValueSerialization() throws IOException {
        JsonInclude.Include inclusion = JsonInclude.Include.NON_DEFAULT;
        PropertyBuilder builder = new PropertyBuilder(_config, new BeanDescription(MyClassList.class));
        builder._useRealPropertyDefaults = true;

        ObjectWriter writer = _objectMapper.writerWith(builder);
        MyClassList obj = new MyClassList();
        obj.intList = Arrays.asList(1, 2, 3);

        String json = writer.writeValueAsString(obj);
        Assert.assertEquals("[1, 2, 3]", json);

        MyClassList defaultObj = builder.getDefaultBean();
        if (defaultObj instanceof MyClassList) {
            obj = (MyClassList) defaultObj;
            obj.intList = null;
        }

        json = writer.writeValueAsString(obj);
        Assert.assertEquals("[0, 0, 0]", json);
    }

    @Test
    public void testMapDefaultValueSerialization() throws IOException {
        JsonInclude.Include inclusion = JsonInclude.Include.NON_DEFAULT;
        PropertyBuilder builder = new PropertyBuilder(_config, new BeanDescription(MyClassMap.class));
        builder._useRealPropertyDefaults = true;

        ObjectWriter writer = _objectMapper.writerWith(builder);
        MyClassMap obj = new MyClassMap();
        obj.map = new HashMap<>();
        obj.map.put("key1", new MyClass());
        obj.map.put("key2", new MyClass());

        String json = writer.writeValueAsString(obj);
        Assert.assertEquals("{" +
                "\"map\":{" +
                "\"key1\":{}, " +
                "\"key2\":{}" +
                "}" +
                "}", json);

        MyClassMap defaultObj = builder.getDefaultBean();
        if (defaultObj instanceof MyClassMap) {
            obj = (MyClassMap) defaultObj;
            obj.map.clear();
        }

        json = writer.writeValueAsString(obj);
        Assert.assertEquals("{" +
                "\"map\":{}" +
                "}", json);
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class MyClass {
        int intValue;
        String stringValue;
    }

    static class MyClassList extends List<Integer> {
    }

    static class MyClassMap extends HashMap<String, Object> {
    }
}