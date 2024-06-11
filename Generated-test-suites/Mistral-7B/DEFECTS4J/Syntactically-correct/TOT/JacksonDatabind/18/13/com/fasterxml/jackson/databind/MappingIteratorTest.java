package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializableObject;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDescriptor;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MappingIteratorTest {

    private ObjectMapper _objectMapper;
    private MappingIterator<String> _iterator;
    private MappingIterator<MyBean> _myBeanIterator;

    @BeforeMethod
    public void setup() {
        _objectMapper = new ObjectMapper();
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<String> iterator = MappingIterator.emptyIterator();
        Assert.assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testBasicUsage() throws IOException {
        String json = "[\"Alice\",\"Bob\",\"Charles\"]";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes());
        _iterator = _objectMapper.readerForListOf(String.class)
                .readValues(jsonStream);
        List<String> expected = new ArrayList<>();
        expected.add("Alice");
        expected.add("Bob");
        expected.add("Charles");
        List<String> actual = _iterator.readAll();
        Assert.assertEquals(expected, actual);
        _iterator.close();
    }

    @Test
    public void testDeserializationFailure() throws IOException {
        String json = "[1, 2, 3]";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes());
        _iterator = _objectMapper.readerForListOf(String.class)
                .readValues(jsonStream);
        try {
            _iterator.nextValue();
            Assert.fail();
        } catch (JsonMappingException e) {
            // expected
        } finally {
            _iterator.close();
        }
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        String json = "[\"Alice\",\"Bob\",\"Charles\"]";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes());
        _iterator = _objectMapper.readerForListOf(String.class)
                .readValues(jsonStream);
        List<String> expected = new ArrayList<>();
        _iterator.readAll(expected);
        List<String> actual = _iterator.readAll();
        Assert.assertEquals(expected, actual);
        _iterator.close();
    }

    @Test
    public void testReadAllWithArrayList() throws IOException {
        String json = "[\"Alice\",\"Bob\",\"Charles\"]";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes());
        _iterator = _objectMapper.readerForListOf(String.class)
                .readValues(jsonStream);
        ArrayList<String> expected = new ArrayList<>();
        _iterator.readAll(expected);
        ArrayList<String> actual = _iterator.readAll();
        Assert.assertEquals(expected, actual);
        _iterator.close();
    }

    @Test
    public void testReadAllWithLinkedList() throws IOException {
        String json = "[\"Alice\",\"Bob\",\"Charles\"]";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes());
        _iterator = _objectMapper.readerForListOf(String.class)
                .readValues(jsonStream);
        List<String> expected = new LinkedList<>();
        _iterator.readAll(expected);
        List<String> actual = _iterator.readAll();
        Assert.assertEquals(expected, actual);
        _iterator.close();
    }

    @Test
    public void testReadAllWithEmptyList() throws IOException {
        String json = "[]";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes());
        _iterator = _objectMapper.readerForListOf(String.class)
                .readValues(jsonStream);
        List<String> expected = Collections.emptyList();
        List<String> actual = _iterator.readAll();
        Assert.assertEquals(expected, actual);
        _iterator.close();
    }

    @Test
    public void testReadAllWithNullList() throws IOException {
        String json = "[null]";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes());
        _iterator = _objectMapper.readerForListOf(String.class)
                .readValues(jsonStream);
        List<String> expected = null;
        List<String> actual = _iterator.readAll();
        Assert.assertEquals(expected, actual);
        _iterator.close();
    }

    @Test
    public void testReadAllWithSubclass() throws IOException {
        class MyBean extends SerializableObject {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        StdDeserializer<MyBean> deserializer = new StdDeserializer<MyBean>(MyBean.class) {
            @Override
            public MyBean deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                MyBean bean = new MyBean();
                if (p.isExpectedStartObjectToken()) {
                    ctxt.findContextualValue("name", String.class, p, bean);
                }
                return bean;
            }
        };

        TypeIdResolver typeIdResolver = new TypeIdResolver() {
            @Override
            public IdType idFromValueAndType(Object value, TypeDescriptor valueType) {
                return null;
            }

            @Override
            public TypeDesc idFromValueAndType(Object value, Class<?> valueType) {
                return null;
            }

            @Override
            public String idFromValue(Object value) {
                return null;
            }
        };

        _objectMapper.setTypeIdResolver(typeIdResolver);

        String json = "[{\"name\":\"Alice\"},{\"name\":\"Bob\"},{\"name\":\"Charles\"}]";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes());
        _myBeanIterator = _objectMapper.readerForListOf(MyBean.class)
                .readValues(jsonStream);
        List<MyBean> expected = new ArrayList<>();
        MyBean bean = new MyBean();
        bean.setName("Alice");
        expected.add(bean);
        MyBean bean2 = new MyBean();
        bean2.setName("Bob");
        expected.add(bean2);
        MyBean bean3 = new MyBean();
        bean3.setName("Charles");
        expected.add(bean3);
        List<MyBean> actual = _myBeanIterator.readAll();
        Assert.assertEquals(expected, actual);
        _myBeanIterator.close();
    }
}