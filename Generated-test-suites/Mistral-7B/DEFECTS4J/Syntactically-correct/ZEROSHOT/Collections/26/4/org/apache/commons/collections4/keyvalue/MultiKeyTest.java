import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructorWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeyConstructorWithThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[3];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructorWithNullKeyArray() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K expectedKey1 = key1;
        K expectedKey2 = key2;
        K key1Retrieved = multiKey.getKey(0);
        K key2Retrieved = multiKey.getKey(1);
        Assert.assertEquals(expectedKey1, key1Retrieved);
        Assert.assertEquals(expectedKey2, key2Retrieved);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyWithInvalidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2);
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedSize = 2;
        int actualSize = multiKey.size();
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        Object other = new Object();
        MultiKey<String> multiKey3 = new MultiKey<>("key3", "key4");
        Assert.assertTrue(multiKey1.equals(multiKey1));
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(other));
        Assert.assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedHashCode = 32 + key1.hashCode() + key2.hashCode();
        int actualHashCode = multiKey.hashCode();
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedString = "MultiKey[key1, key2]";
        String actualString = multiKey.toString();
        Assert.assertEquals(expectedString, actualString);
    }
}