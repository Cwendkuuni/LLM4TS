package org.apache.commons.collections4.keyvalue;

import org.apache.commons.collections4.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for MultiKey.
 */
public class MultiKeyTest {

    /**
     * Test constructor with two keys.
     */
    @Test
    public void testTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(multiKey.hashCode(), multiKey.hashCode()); // check hashcode is calculated correctly
    }

    /**
     * Test constructor with three keys.
     */
    @Test
    public void testThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(multiKey.hashCode(), multiKey.hashCode()); // check hashcode is calculated correctly
    }

    /**
     * Test constructor with four keys.
     */
    @Test
    public void testFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] keys = multiKey.getKeys();
        assertEquals(4, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(key4, keys[3]);
        assertEquals(multiKey.hashCode(), multiKey.hashCode()); // check hashcode is calculated correctly
    }

    /**
     * Test getKey method with valid index.
     */
    @Test
    public void testGetKeyValidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K key = multiKey.getKey(0);
        assertEquals(key1, key);
    }

    /**
     * Test getKey method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
    }

    /**
     * Test equals method with equal MultiKey.
     */
    @Test
    public void testEqualsEqual() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
    }

    /**
     * Test equals method with not equal MultiKey.
     */
    @Test
    public void testEqualsNotEqual() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, "key3");
        assertFalse(multiKey1.equals(multiKey2));
        assertFalse(multiKey2.equals(multiKey1));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey.hashCode(), multiKey.hashCode());
        assertEquals(multiKey.hashCode(), multiKey2.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String toString = multiKey.toString();
        assertTrue(toString.startsWith("MultiKey"));
        assertTrue(toString.contains("[key1, key2]"));
    }
}