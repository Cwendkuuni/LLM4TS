package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.After;
import org.junit.Assert;import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    private Serializable obj;
    private byte[] objectData;

    @Before
    public void setUp() {
        obj = new HashMap<>();
        putMapData((Map) obj);
        objectData = SerializationUtils.serialize(obj);
    }

    @After
    public void tearDown() {
        obj = null;
        objectData = null;
    }

    @Test
    public void testSerialize() throws IOException, ClassNotFoundException {
        byte[] cloneObjectData = SerializationUtils.serialize(SerializationUtils.clone(obj));
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(cloneObjectData));
        Assert.assertEquals(obj, deserializedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        SerializationUtils.serialize(null, new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }

            @Override
            public void close() throws IOException {
            }
        });
    }

    @Test(expected = SerializationException.class)
    public void testSerializeException() throws IOException {
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }

            @Override
            public void close() throws IOException {
            }
        };
        SerializationUtils.serialize(new Object(), outputStream);
    }

    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(objectData));
        Assert.assertEquals(obj, deserializedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeException() throws IOException {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }

            @Override
            public void close() throws IOException {
            }
        };
        SerializationUtils.deserialize(inputStream);
    }

    private void putMapData(Map map) {
        map.put("key1", "value1");
        map.put("key2", "value2");
    }
}