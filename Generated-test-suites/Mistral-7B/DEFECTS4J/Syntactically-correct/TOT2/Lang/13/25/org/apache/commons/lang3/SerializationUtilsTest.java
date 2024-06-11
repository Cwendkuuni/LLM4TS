import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {
    // Test for clone method
    @Test
    public void testClone() {
        String original = new String("Original String");
        String cloned = SerializationUtils.clone(original);
        assertNotSame(original, cloned);
        assertEquals(original, cloned);

        // Test for serialization and deserialization of simple types
        int originalInt = 10;
        int deserializedInt = SerializationUtils.deserialize(SerializationUtils.serialize(originalInt));
        assertEquals(originalInt, deserializedInt);

        // Test for serialization and deserialization of complex types
        Map<Integer, String> originalMap = new HashMap<>();
        originalMap.put(1, "One");
        originalMap.put(2, "Two");

        Map<Integer, String> deserializedMap = (Map<Integer, String>) SerializationUtils.deserialize(SerializationUtils.serialize(originalMap));
        assertEquals(originalMap, deserializedMap);
    }

    // Test for serialize method with null input
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullInput() {
        SerializationUtils.serialize(null);
    }

    // Test for deserialize method with null input
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInput() {
        SerializationUtils.deserialize(null);
    }

    // Test for serialization and deserialization of custom classes
    @Test
    public void testCustomClasses() {
        MyClass original = new MyClass();
        byte[] serialized = SerializationUtils.serialize(original);
        MyClass deserialized = (MyClass) SerializationUtils.deserialize(serialized);
        assertEquals(original, deserialized);
    }

    // Test for exceptions during serialization and deserialization
    @Test(expected = SerializationException.class)
    public void testSerializationException() {
        try {
            SerializationUtils.serialize(new MyException());
        } catch (IOException e) {
            throw new SerializationException("Failure message", e);
        }
    }

    // Test for deep cloning
    @Test
    public void testDeepClone() {
        MyNestedClass original = new MyNestedClass();
        original.list.add(new MyNestedClass());
        original.list.get(0).list.add(new MyNestedClass());

        MyNestedClass cloned = (MyNestedClass) SerializationUtils.clone(original);
        MyNestedClass nestedCloned = cloned.list.get(0).list.get(0);

        assertEquals(original, cloned);
        assertEquals(original.hashCode(), cloned.hashCode());
        assertEquals(nestedCloned, cloned.list.get(0).list.get(0));
        assertEquals(original.hashCode(), nestedCloned.hashCode());
    }

    
}

class MyClass implements Cloneable, Serializable {
    private int value;
}

class MyNestedClass implements Cloneable, Serializable {
    private List<MyNestedClass> list = new ArrayList<>();
}

class MyException extends Exception implements Serializable {
}