package org.apache.commons.collections4.keyvalue;

import org.apache.commons.collections4.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testTwoKeyMultiKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new K[]{key1, key2}, keys);
        Assert.assertEquals(multiKey.size(), 2);
    }

    @Test
    public void testThreeKeyMultiKey() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new K[]{key1, key2, key3}, keys);
        Assert.assertEquals(multiKey.size(), 3);
    }

    @Test
    public void testFourKeyMultiKey() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new K[]{key1, key2, key3, key4}, keys);
        Assert.assertEquals(multiKey.size(), 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysArray() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        K[] keys = new K[2];
        keys[0] = "key1";
        keys[1] = null;
        MultiKey<K> multiKey = new MultiKey<>(keys);
    }

    @Test
    public void testCloneMultiKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        MultiKey<K> clonedMultiKey = multiKey.clone();
        K[] clonedKeys = clonedMultiKey.getKeys();
        Assert.assertArrayEquals(new K[]{key1, key2}, clonedKeys);
        Assert.assertEquals(clonedMultiKey.size(), 2);
    }

    @Test
    public void testEqualsMultiKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key2");
        MultiKey<K> multiKey4 = new MultiKey<>(new K[] {"key1", "key2"});
        MultiKey<K> multiKey5 = new MultiKey<>(new K[] {"key1", "key3"});

        Assert.assertTrue(multiKey1.equals(multiKey1));
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(multiKey3));
        Assert.assertFalse(multiKey1.equals(multiKey4));
        Assert.assertFalse(multiKey1.equals(multiKey5));
        Assert.assertFalse(multiKey1.equals("Test"));
        Assert.assertFalse(multiKey1.equals(null));
    }

    @Test
    public void testHashCodeMultiKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key2");
        MultiKey<K> multiKey4 = new MultiKey<>(new K[] {"key1", "key2"});
        MultiKey<K> multiKey5 = new MultiKey<>(new K[] {"key1", "key3"});

        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey4.hashCode());
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey5.hashCode());
    }

    @Test
    public void testToStringMultiKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String multiKeyString = multiKey.toString();
        String expectedMultiKeyString = "MultiKey[keys=[key1, key2]]";
        Assert.assertEquals(expectedMultiKeyString, multiKeyString);
    }
}