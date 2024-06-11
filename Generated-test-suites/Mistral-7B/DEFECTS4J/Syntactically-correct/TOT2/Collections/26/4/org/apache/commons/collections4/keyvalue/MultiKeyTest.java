import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        K[] keys = multiKey.getKeys();
        int size = multiKey.size();

        // Then
        Assert.assertEquals(2, size);
        Assert.assertArrayEquals(new Object[]{key1, key2}, keys);
    }

    @Test
    public void testMultiKeyThreeKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        // When
        K[] keys = multiKey.getKeys();
        int size = multiKey.size();

        // Then
        Assert.assertEquals(3, size);
        Assert.assertArrayEquals(new Object[]{key1, key2, key3}, keys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        // Given
        MultiKey<String> multiKey = new MultiKey<>(new String[0]);

        // Then
        // An exception should be thrown
    }

    @Test
    public void testMultiKeySameKey() {
        // Given
        K key = "key";
        MultiKey<K> multiKey1 = new MultiKey<>(key, key);
        MultiKey<K> multiKey2 = new MultiKey<>(key, key);

        // When
        boolean equals = multiKey1.equals(multiKey2);

        // Then
        Assert.assertTrue(equals);
    }

    @Test
    public void testMultiKeyDifferentKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key3);

        // When
        boolean equals = multiKey1.equals(multiKey2);

        // Then
        Assert.assertFalse(equals);
    }

    @Test
    public void testMultiKeyHashCode() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        int hashCode1 = multiKey.hashCode();
        MultiKey<K> multiKeyNew = new MultiKey<>(key1, key2);
        int hashCode2 = multiKeyNew.hashCode();

        // Then
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testMultiKeyClone() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        K[] keysClone = multiKey.getKeys().clone();

        // Then
        MultiKey<K> multiKeyClone = new MultiKey<>(keysClone);
        Assert.assertArrayEquals(keysClone, multiKeyClone.getKeys());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyOutOfBounds() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        K key = multiKey.getKey(-1);

        // Then
        // An exception should be thrown
    }
}