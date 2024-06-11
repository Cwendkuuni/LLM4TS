package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class SerializationUtilsTest {

    private static final Serializable OBJECT_TO_SERIALIZE = new SerializableObject();
    private static final byte[] SERIALIZED_OBJECT = SerializationUtils.serialize(OBJECT_TO_SERIALIZE);

    @Before
    public void setUp() {
        // Empty for now
    }

    @Test
    public void testClone() throws SerializationException, CloneNotSupportedException {
        Serializable clone = SerializationUtils.clone(OBJECT_TO_SERIALIZE);
        assertEquals(OBJECT_TO_SERIALIZE, clone);
    }

    @Test(expected = SerializationException.class)
    public void testCloneWithNullObject() {
        SerializationUtils.clone(null);
    }

    @Test
    public void testSerialize() throws SerializationException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(OBJECT_TO_SERIALIZE, baos);
        byte[] serializedObject = baos.toByteArray();
        assertSame(SERIALIZED_OBJECT, serializedObject);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeWithNullOutputStream() {
        SerializationUtils.serialize(OBJECT_TO_SERIALIZE, null);
    }

    @Test
    public void testDeserialize() throws SerializationException, IOException, ClassNotFoundException {
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(SERIALIZED_OBJECT));
        assertEquals(OBJECT_TO_SERIALIZE, deserializedObject);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeWithNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testDeepClone() {
        Map<Integer, SerializableObject> originalMap = new HashMap<>();
        originalMap.put(1, new SerializableObject());

        Map<Integer, SerializableObject> clonedMap = SerializationUtils.clone(originalMap);
        assertEquals(originalMap, clonedMap);

        originalMap.get(1).field = "originalData";
        clonedMap.get(1).field = "clonedData";
        assertSame(originalMap, clonedMap);
    }

    @Test
    public void testDeserializeWithSerializableHashMap() throws SerializationException, IOException, ClassNotFoundException {
        Map<Integer, SerializableObject> originalMap = new HashMap<>();
        originalMap.put(1, new SerializableObject());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(originalMap);
        out.close();

        Map<Integer, SerializableObject> deserializedMap = (Map<Integer, SerializableObject>) SerializationUtils.deserialize(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(originalMap, deserializedMap);
    }

    @Test
    public void testDeserializeWithNonSerializableHashMap() throws SerializationException, IOException {
        Map<Integer, NonSerializableObject> originalMap = new HashMap<>();
        originalMap.put(1, new NonSerializableObject());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        try {
            out.writeObject(originalMap);
        } catch (IOException ex) {
            // Expected
        }

        assertNull(SerializationUtils.deserialize(new ByteArrayInputStream(baos.toByteArray())));
    }

    private static class SerializableObject implements Serializable {
        private String field;

        @Override
        public String toString() {
            return "SerializableObject{" +
                    "field='" + field + '\'' +
                    '}';
        }
    }

    private static class NonSerializableObject {
    }
}