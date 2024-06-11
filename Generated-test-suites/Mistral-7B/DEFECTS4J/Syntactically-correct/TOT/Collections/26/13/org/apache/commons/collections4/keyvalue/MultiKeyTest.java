package org.apache.commons.collections4.keyvalue;

import org.apache.commons.collections4.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the MultiKey class.
 */
public class MultiKeyTest {

    /**
     * Test constructing a MultiKey with two keys.
     */
    @Test
    public void testTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] {key1, key2}, keys);
    }

    /**
     * Test constructing a MultiKey with three keys.
     */
    @Test
    public void testThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] {key1, key2, key3}, keys);
    }

    /**
     * Test constructing a MultiKey with an array of keys.
     */
    @Test
    public void testArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2, key3});
        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] {key1, key2, key3}, keys);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key2");
        MultiKey<K> multiKey4 = new MultiKey<>(new K[] {key1, key2});

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int hashCode = multiKey.hashCode();
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(hashCode, multiKey2.hashCode());
    }

    /**
     * Test clone constructor.
     */
    @Test
    public void testCloneConstructor() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        MultiKey<K> clonedMultiKey = new MultiKey<>(multiKey);
        K[] keys = clonedMultiKey.getKeys();
        assertArrayEquals(multiKey.getKeys(), keys);
    }
}