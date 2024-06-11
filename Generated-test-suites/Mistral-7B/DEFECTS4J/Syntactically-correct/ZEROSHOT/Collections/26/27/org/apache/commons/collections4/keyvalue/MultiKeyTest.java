import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        // Test construction with two keys
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);

        // Test construction with three keys
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);

        // Test construction with an array of keys
        K[] keys = new K[]{key1, key2, key3};
        MultiKey<K> multiKey3 = new MultiKey<>(keys);

        // Test that the keys can be retrieved
        Assert.assertArrayEquals(new K[]{key1, key2}, multiKey1.getKeys());
        Assert.assertArrayEquals(new K[]{key1, key2, key3}, multiKey2.getKeys());
        Assert.assertArrayEquals(keys, multiKey3.getKeys());

        // Test size
        Assert.assertEquals(2, multiKey1.size());
        Assert.assertEquals(3, multiKey2.size());
        Assert.assertEquals(3, multiKey3.size());
    }

    @Test
    public void testMultiKeyEquality() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key2");

        // Test equality with same instance
        Assert.assertTrue(multiKey1.equals(multiKey1));

        // Test equality with same keys
        Assert.assertTrue(multiKey1.equals(multiKey2));

        // Test inequality with different keys
        Assert.assertFalse(multiKey1.equals(new MultiKey<>(new K[]{"differentKey1", "differentKey2"})));

        // Test inequality with different number of keys
        Assert.assertFalse(multiKey1.equals(new MultiKey<>(new K[]{"key1", "key2", "differentKey3"})));

        // Test inequality with different keys even if they have the same hashcode
        Assert.assertFalse(multiKey1.equals(new MultiKey<>(Arrays.copyOfRange(multiKey1.getKeys(), 0, 1), new K[]{"key1", "differentKey2"})));

        // Test that hashcodes are equal if keys are equal
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
}