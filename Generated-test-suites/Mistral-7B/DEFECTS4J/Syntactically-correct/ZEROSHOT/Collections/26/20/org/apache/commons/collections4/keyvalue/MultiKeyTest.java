import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        // Test construction with two keys
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        K[] keys1 = multiKey1.getKeys();
        Assert.assertArrayEquals(new Object[] {key1, key2}, keys1);

        // Test construction with three keys
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        K[] keys2 = multiKey2.getKeys();
        Assert.assertArrayEquals(new Object[] {key1, key2, key3}, keys2);

        // Test construction with an array of keys
        K[] keysArray = new K[] {key1, key2, key3};
        MultiKey<K> multiKey3 = new MultiKey<>(keysArray);
        K[] keys3 = multiKey3.getKeys();
        Assert.assertArrayEquals(keysArray, keys3);
    }

    @Test
    public void testMultiKeySizeAndGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        Assert.assertEquals(2, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "differentKey2");
        Assert.assertEquals(multiKey1, multiKey2);
        Assert.assertNotEquals(multiKey1, multiKey3);
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int hashCode = multiKey.hashCode();
        MultiKey<K> multiKeyCopy = new MultiKey<>(multiKey.getKeys());
        Assert.assertEquals(hashCode, multiKeyCopy.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String toString = multiKey.toString();
        Assert.assertTrue(toString.contains("MultiKey"));
        Assert.assertTrue(toString.contains("[key1, key2]"));
    }
}