import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructorTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new K[]{key1, key2}, keys);
    }

    @Test
    public void testMultiKeyConstructorThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new K[]{key1, key2, key3}, keys);
    }

    @Test
    public void testMultiKeyConstructorFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new K[]{key1, key2, key3, key4}, keys);
    }

    @Test
    public void testMultiKeyConstructorArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2});
        K[] keys = multiKey.getKeys();
        Assert.assertArrayEquals(new K[]{key1, key2}, keys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructorNullArrayOfKeys() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyInvalidIndex() {
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
        Assert.assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, new K() {});
        Assert.assertEquals(multiKey1, multiKey2);
        Assert.assertNotEquals(multiKey1, multiKey3);
        MultiKey<K> multiKey4 = new MultiKey<>(new K[]{key1, key2});
        Assert.assertEquals(multiKey1, multiKey4);
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int hashCode = multiKey.hashCode();
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        Assert.assertEquals(hashCode, multiKey2.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        Assert.assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}