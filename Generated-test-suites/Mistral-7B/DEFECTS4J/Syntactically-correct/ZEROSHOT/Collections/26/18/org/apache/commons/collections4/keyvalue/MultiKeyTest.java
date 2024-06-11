import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";

        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey123 = new MultiKey<>(key1, key2, key3);

        K[] keys12 = {key1, key2};
        MultiKey<K> multiKey12Clone = new MultiKey<>(keys12);

        K[] keys123 = {key1, key2, key3};
        MultiKey<K> multiKey123Clone = new MultiKey<>(keys123, false);

        K[] keys3 = {key3};
        MultiKey<K> multiKey3 = new MultiKey<>(keys3);

        // Test equals
        Assert.assertEquals(multiKey12, multiKey12);
        Assert.assertEquals(multiKey12, multiKey12);
        Assert.assertEquals(multiKey12, multiKey12Clone);
        Assert.assertEquals(multiKey12, multiKey123);
        Assert.assertEquals(multiKey12, multiKey123Clone);
        Assert.assertNotEqual(multiKey12, multiKey3);

        // Test hashCode
        Assert.assertEquals(multiKey12.hashCode(), multiKey12.hashCode());
        Assert.assertEquals(multiKey12.hashCode(), multiKey12Clone.hashCode());
        Assert.assertEquals(multiKey12.hashCode(), multiKey123.hashCode());
        Assert.assertEquals(multiKey12.hashCode(), multiKey123Clone.hashCode());
        Assert.assertNotEqual(multiKey12.hashCode(), multiKey3.hashCode());

        // Test getKeys and getKey
        K[] keysMultiKey12 = multiKey12.getKeys();
        Assert.assertArrayEquals(keys12, keysMultiKey12);
        K keyMultiKey12 = multiKey12.getKey(0);
        Assert.assertEquals(key1, keyMultiKey12);
        K keyMultiKey12_1 = multiKey12.getKey(1);
        Assert.assertEquals(key2, keyMultiKey12_1);
        Assert.assertEquals(2, multiKey12.size());
    }
}