import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        // Test creating a MultiKey with two keys
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // Test getKeys method
        K[] expectedKeys = new K[]{key1, key2};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);

        // Test size method
        int expectedSize = 2;
        int actualSize = multiKey.size();
        assertEquals(expectedSize, actualSize);

        // Test equals method with same MultiKey
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        boolean result = multiKey.equals(multiKey1);
        assertTrue(result);

        // Test equals method with different MultiKey
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key3);
        result = multiKey.equals(multiKey2);
        assertFalse(result);

        // Test getKey method
        int index = 0;
        K actualKey = multiKey.getKey(index);
        assertEquals(key1, actualKey);

        // Test hashCode and equals methods with null object
        Object nullObject = null;
        int actualHashCode = multiKey.hashCode();
        MultiKey<Object> nullMultiKey = new MultiKey<>(null, null);
        assertNotSame(actualHashCode, nullMultiKey.hashCode());
        assertFalse(multiKey.equals(nullObject));
        assertFalse(multiKey.equals(nullMultiKey));

        // Test get method with correct key
        String expectedValue = "testValue";
        multiKey.put("testKey", expectedValue);
        Object actualValue = multiKey.get("testKey");
        assertEquals(expectedValue, actualValue);

        // Test get method with incorrect key
        assertNull(multiKey.get("incorrectKey"));
    }

    @Test
    public void testMultiKeyThreeKeys() {
        // Test creating a MultiKey with three keys
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        // Test getKeys method
        K[] expectedKeys = new K[]{key1, key2, key3};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);

        // Test size method
        int expectedSize = 3;
        int actualSize = multiKey.size();
        assertEquals(expectedSize, actualSize);

        // Test equals method with same MultiKey
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2, key3);
        boolean result = multiKey.equals(multiKey1);
        assertTrue(result);

        // Test equals method with different MultiKey
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, "key4");
        result = multiKey.equals(multiKey2);
        assertFalse(result);

        // Test getKey method
        int index1 = 0;
        K actualKey1 = multiKey.getKey(index1);
        assertEquals(key1, actualKey1);

        int index2 = 1;
        K actualKey2 = multiKey.getKey(index2);
        assertEquals(key2, actualKey2);

        int index3 = 2;
        K actualKey3 = multiKey.getKey(index3);
        assertEquals(key3, actualKey3);

        // Test hashCode and equals methods with null object
        Object nullObject = null;
        int actualHashCode = multiKey.hashCode();
        MultiKey<Object> nullMultiKey = new MultiKey<>(null, null, null);
        assertNotSame(actualHashCode, nullMultiKey.hashCode());
        assertFalse(multiKey.equals(nullObject));
        assertFalse(multiKey.equals(nullMultiKey));

        // Test get method with correct key
        String expectedValue = "testValue";
        multiKey.put("testKey", expectedValue);
        Object actualValue = multiKey.get("testKey");
        assertEquals(expectedValue, actualValue);

        // Test get method with incorrect key
        assertNull(multiKey.get("incorrectKey"));
    }
}