package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.test.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class SerializationUtilsTest {

    private static final String TEST_OBJECT_NAME = "TestObject";
    private static final String TEST_OBJECT_NAME_2 = "TestObject2";
    private static final String TEST_MAP_KEY = "TestKey";
    private static final String TEST_MAP_VALUE = "TestValue";

    @Parameterized.Parameters(name = "{index}: serialization and deserialization test with {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new TestObject()},
                {new TestObject2()}
        });
    }

    private Serializable testObject;

    public SerializationUtilsTest(Serializable testObject) {
        this.testObject = testObject;
    }

    @Test
    public void testClone() throws IOException, ClassNotFoundException {
        // Serialize and clone original object
        byte[] originalObjectData = SerializationUtils.serialize(testObject);
        Serializable clonedObject = SerializationUtils.clone(testObject);

        // Serialize and deserialize cloned object to check equality
        byte[] clonedObjectData = SerializationUtils.serialize(clonedObject);
        Serializable deserializedClonedObject = SerializationUtils.deserialize(new ByteArrayInputStream(clonedObjectData));

        // Check objects are equal
        assertEquals(testObject, clonedObject);
        assertEquals(testObject, deserializedClonedObject);
    }

    @Test
    public void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
        // Serialize and deserialize object
        byte[] objectData = SerializationUtils.serialize(testObject);
        Serializable deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(objectData));

        // Check objects are equal
        assertEquals(testObject, deserializedObject);
    }

    @Test
    public void testNullInput() {
        assertThrows(NullPointerException.class, () -> SerializationUtils.serialize(null));
        assertThrows(NullPointerException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testSerializationWithHashMap() throws IOException, ClassNotFoundException {
        Map<String, String> map = new HashMap<>();
        map.put(TEST_MAP_KEY, TEST_MAP_VALUE);

        // Serialize and deserialize Map
        byte[] mapData = SerializationUtils.serialize(map);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(new ByteArrayInputStream(mapData));

        // Check maps are equal
        assertEquals(map, deserializedMap);
    }

    private static class TestObject implements Serializable {
        private static final long serialVersionUID = -853606922172622151L;
        private String name;

        public TestObject() {
            this(TEST_OBJECT_NAME);
        }

        public TestObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static class TestObject2 implements Serializable {
        private static final long serialVersionUID = 434122798526224L;
        private String name;
        private int age;

        public TestObject2() {
            this("TestObject2", 25);
        }

        public TestObject2(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}