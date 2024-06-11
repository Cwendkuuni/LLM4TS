package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
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

    @Test
    public void testClone() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);

        Serializable serializableMap = SerializationUtils.clone(map);

        @SuppressWarnings("unchecked")
        Map<String, Integer> clonedMap = (Map<String, Integer>) SerializationUtils.clone(map);

        Assert.assertEquals(map, clonedMap);
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        String input = "test string";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(input, byteArrayOutputStream);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        String deserializedInput = (String) SerializationUtils.deserialize(byteArrayInputStream);

        Assert.assertEquals(input, deserializedInput);
    }

    @Test
    public void testSerializationException() {
        SerializationUtils serializationUtils = new SerializationUtils();
        try {
            serializationUtils.serialize(null, new ByteArrayOutputStream());
            Assert.fail();
        } catch (SerializationException e) {
            Assert.assertEquals("Null input object", e.getMessage());
        }

        try {
            SerializationUtils.deserialize(null);
            Assert.fail();
        } catch (SerializationException e) {
            Assert.assertEquals("Null input stream", e.getMessage());
        }

        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (SerializationException e) {
            Assert.assertEquals("Failed to deserialize object", e.getMessage());
        }

        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Unexpected end of file", e.getMessage());
        }

        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (ClassNotFoundException e) {
            Assert.assertEquals("ClassNotFoundException while reading cloned object data", e.getMessage());
        }

        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("IOException while reading cloned object data", e.getMessage());
        }
    }
}