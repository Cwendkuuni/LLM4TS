package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.After;
import org.junit.Before;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SerializationUtilsTest {

    private Serializable simpleObject;
    private Serializable complexObject;
    private Serializable complexMapObject;

    @Before
    public void setup() {
        simpleObject = new SimpleSerializable();
        complexObject = new ComplexSerializable();
        complexMapObject = new ComplexMapSerializable();
    }

    @Test
    public void testCloneSimpleSerializable() throws SerializationException {
        Serializable clonedObject = SerializationUtils.clone(simpleObject);
        assertEquals(simpleObject, clonedObject);
    }

    @Test
    public void testSerializeAndDeserializeSimpleSerializable() throws SerializationException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(simpleObject, outputStream);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(outputStream.toByteArray()));
        assertEquals(simpleObject, deserializedObject);
    }

    @Test
    public void testCloneComplexSerializable() throws SerializationException {
        Serializable clonedObject = SerializationUtils.clone(complexObject);
        assertEquals(complexObject, clonedObject);
    }

    @Test
    public void testSerializeAndDeserializeComplexSerializable() throws SerializationException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(complexObject, outputStream);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(outputStream.toByteArray()));
        assertEquals(complexObject, deserializedObject);
    }

    @Test
    public void testCloneComplexMapSerializable() throws SerializationException {
        Serializable clonedObject = SerializationUtils.clone(complexMapObject);
        Map<String, Integer> clonedMap = (Map<String, Integer>) ((Map<?, ?>) clonedObject).clone();
        assertEquals(complexMapObject, clonedObject);
        assertEquals(complexMapObject.getMap(), clonedMap);
    }

    @Test
    public void testSerializeAndDeserializeComplexMapSerializable() throws SerializationException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(complexMapObject, outputStream);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(outputStream.toByteArray()));
        Map<String, Integer> deserializedMap = (Map<String, Integer>) ((Map<?, ?>) deserializedObject).clone();
        assertEquals(complexMapObject, deserializedObject);
        assertEquals(complexMapObject.getMap(), deserializedMap);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNullObject() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, outputStream);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        SerializationUtils.deserialize(inputStream);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeClassNotFoundException() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream(1024);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(new String("TestClass"));
        oos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(os.toByteArray());
        SerializationUtils.deserialize(bais);
    }

    @Test
    public void testDeserializeFinallyClosedInputStream() throws SerializationException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(simpleObject, outputStream);
        Object deserializedObject = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(outputStream.toByteArray())) {
            deserializedObject = SerializationUtils.deserialize(bais);
        }
        assertEquals(simpleObject, deserializedObject);
    }

    @After
    public void teardown() {
        simpleObject = null;
        complexObject = null;
        complexMapObject = null;
    }
}

class SimpleSerializable implements Serializable {
}

class ComplexSerializable implements Serializable {
    private static final long serialVersionUID = 1L;
}

class ComplexMapSerializable implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> map = new HashMap<>();

    public Map<String, Integer> getMap() {
        return map;
    }
}