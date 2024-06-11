package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);
        originalMap.put("three", 3);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertNotNull(clonedMap);
        Assert.assertEquals(originalMap, clonedMap);
        Assert.assertNotSame(originalMap, clonedMap);
    }

    @Test
    public void testSerializeAndDeserializeObject() {
        Person originalPerson = new Person("John Doe", 30);
        byte[] objectData = SerializationUtils.serialize(originalPerson);
        Person clonedPerson = (Person) SerializationUtils.deserialize(objectData);

        Assert.assertNotNull(clonedPerson);
        Assert.assertEquals(originalPerson, clonedPerson);
        Assert.assertNotSame(originalPerson, clonedPerson);
    }

    @Test
    public void testSerializeAndDeserializeByteArray() {
        Person originalPerson = new Person("John Doe", 30);
        byte[] objectData = SerializationUtils.serialize(originalPerson);
        byte[] deserializedData = SerializationUtils.serialize(originalPerson);
        Person clonedPerson = (Person) SerializationUtils.deserialize(objectData);

        Assert.assertArrayEquals(deserializedData, objectData);
        Assert.assertEquals(originalPerson, clonedPerson);
        Assert.assertNotSame(originalPerson, clonedPerson);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() {
        Person person = new Person("John Doe", 30);
        ObjectOutputStream outputStream = new ObjectOutputStream(new ByteArrayOutputStream());
        try {
            outputStream.writeObject(person);
        } catch (IOException e) {
            throw new SerializationException("Unexpected IOException", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                // ignore close exception
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testClassNotFoundException() {
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(new byte[0]));
        ClassLoaderAwareObjectInputStream clonedInputStream = new ClassLoaderAwareObjectInputStream(inputStream, getClass().getClassLoader());
        try {
            clonedInputStream.readObject();
        } catch (IOException e) {
            throw new SerializationException("Unexpected IOException", e);
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Unexpected ClassNotFoundException", e);
        } finally {
            try {
                clonedInputStream.close();
            } catch (IOException e) {
                // ignore close exception
            }
        }
    }

    @Test(expected = IOException.class)
    public void testIOException() {
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(new byte[0]));
        try {
            inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Unexpected ClassNotFoundException", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new SerializationException("Unexpected IOException", e);
            }
        }
    }

    private static class Person implements Serializable {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}