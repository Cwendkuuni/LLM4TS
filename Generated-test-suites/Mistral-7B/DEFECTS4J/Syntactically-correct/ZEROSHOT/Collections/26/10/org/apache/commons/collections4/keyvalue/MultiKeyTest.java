import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class MultiKeyTest {

    /**
     * Test constructor taking two keys.
     */
    @Test
    public void testMultiKeyTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[]{key1, key2};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    /**
     * Test constructor taking three keys.
     */
    @Test
    public void testMultiKeyThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[]{key1, key2, key3};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    /**
     * Test constructor taking four keys.
     */
    @Test
    public void testMultiKeyFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = new K[]{key1, key2, key3, key4};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    /**
     * Test constructor taking five keys.
     */
    @Test
    public void testMultiKeyFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] expectedKeys = new K[]{key1, key2, key3, key4, key5};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    /**
     * Test constructor taking an array of keys which is cloned.
     */
    @Test
    public void testMultiKeyArrayKeysCloned() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(new String[]{key1, key2, key3});
        K[] expectedKeys = new K[]{key1, key2, key3};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    /**
     * Test constructor taking an array of keys, without cloning.
     */
    @Test
    public void testMultiKeyArrayKeysWithoutCloning() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String[] keys = new String[]{key1, key2, key3};
        MultiKey<String> multiKey = new MultiKey<>(keys, false);
        K[] expectedKeys = new K[]{key1, key2, key3};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
        // Change an element of the array to check that the hashcode changes
        key2 = "newKey2";
        multiKey = new MultiKey<>(keys, false);
        K[] newActualKeys = multiKey.getKeys();
        assertNotSame(actualKeys, newActualKeys);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testMultiKeyEquals() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>(key1, "newKey2");
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey1.equals(multiKey1));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(null));
    }

    /**
     * Test hashcode method.
     */
    @Test
    public void testMultiKeyHashcode() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        int expectedHashcode = Arrays.hashCode(new K[]{key1, key2});
        int actualHashcode = multiKey.hashCode();
        assertEquals(expectedHashcode, actualHashcode);
    }

    /**
     * Test recursive deserialization.
     */
    @Test
    public void testMultiKeySerializable() throws Exception {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        Object output = new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(multiKey);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output.getClass().getBytes());
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
        MultiKey<String> deserializedMultiKey = (MultiKey<String>) inputStream.readObject();
        assertNotNull(deserializedMultiKey);
        K[] deserializedKeys = deserializedMultiKey.getKeys();
        K[] originalKeys = multiKey.getKeys();
        assertArrayEquals(originalKeys, deserializedKeys);
    }

    /**
     * Helper method for asserting arrays are equal.
     */
    private void assertArrayEquals(final K[] expected, final K[] actual) {
        Assert.assertArrayEquals("Expected and actual arrays not equal", expected, actual);
    }
}