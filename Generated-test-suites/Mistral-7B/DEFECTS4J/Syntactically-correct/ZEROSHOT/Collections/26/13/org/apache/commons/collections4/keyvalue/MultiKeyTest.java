import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        // Test construction with two keys
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        assertArrayEquals(new K[] {key1, key2}, multiKey1.getKeys());

        // Test construction with three keys
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        assertArrayEquals(new K[] {key1, key2, key3}, multiKey2.getKeys());

        // Test construction with an array of keys
        K[] keys = new K[] {key1, key2, key3};
        MultiKey<K> multiKey3 = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey3.getKeys());
    }

    @Test
    public void testMultiKeyGetKey() {
        // Test getting a key by index
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testMultiKeySize() {
        // Test getting the size of the keys
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyEquals() {
        // Test equality with same instance
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = multiKey1;
        assertTrue(multiKey1.equals(multiKey2));

        // Test equality with different instance but same keys
        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key2");
        assertTrue(multiKey1.equals(multiKey3));

        // Test inequality with different keys
        MultiKey<String> multiKey4 = new MultiKey<>("key1", "key3");
        assertFalse(multiKey1.equals(multiKey4));

        // Test inequality with different types
        Object multiKey5 = new MultiKey<>(1, 2);
        assertFalse(multiKey1.equals(multiKey5));
    }

    @Test
    public void testMultiKeyHashCode() {
        // Test hash code consistency
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        int hashCode1 = multiKey1.hashCode();

        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        int hashCode2 = multiKey2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testMultiKeyToString() {
        // Test toString method
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}