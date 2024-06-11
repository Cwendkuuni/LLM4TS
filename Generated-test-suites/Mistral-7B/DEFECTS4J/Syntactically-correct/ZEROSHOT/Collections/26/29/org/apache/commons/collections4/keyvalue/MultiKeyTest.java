import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyCreation() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        // Test MultiKey creation with two keys
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        assertArrayEquals(new K[] {key1, key2}, multiKey1.getKeys());

        // Test MultiKey creation with three keys
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        assertArrayEquals(new K[] {key1, key2, key3}, multiKey2.getKeys());

        // Test MultiKey creation with four keys
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4);
        assertArrayEquals(new K[] {key1, key2, key3, key4}, multiKey3.getKeys());

        // Test MultiKey creation with five keys
        MultiKey<K> multiKey4 = new MultiKey<>(key1, key2, key3, key4, key5);
        assertArrayEquals(new K[] {key1, key2, key3, key4, key5}, multiKey4.getKeys());

        // Test MultiKey creation with an array of keys
        K[] keys = new K[] {key1, key2, key3, key4, key5};
        MultiKey<K> multiKey5 = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey5.getKeys());
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        // Test getting key by index
        for (int i = 0; i < multiKey.size(); i++) {
            assertEquals(multiKey.getKey(i), multiKey.getKeys()[i]);
        }
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals(multiKey.size(), 5);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<String> multiKey4 = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        MultiKey<String> multiKey5 = new MultiKey<>("key1", "key2", "key3", "key4", "key5");

        // Test equals with same instance
        assertEquals(multiKey1, multiKey1);

        // Test equals with same keys
        assertEquals(multiKey1, multiKey2);
        assertEquals(multiKey1, multiKey3);
        assertEquals(multiKey1, multiKey4);
        assertEquals(multiKey1, multiKey5);

        // Test not equals with different keys
        MultiKey<K> multiKey6 = new MultiKey<>(key1, key2, key3, "key4", key5);
        assertNotEquals(multiKey1, multiKey6);

        // Test not equals with different instance and different keys
        MultiKey<K> multiKey7 = new MultiKey<>(key1, key2, key3, key4, "key5");
        assertNotEquals(multiKey1, multiKey7);
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4, key5);

        // Test same instance has the same hash code
        assertEquals(multiKey1.hashCode(), multiKey1.hashCode());

        // Test equal instances have the same hash code
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertEquals(multiKey1.hashCode(), multiKey3.hashCode());

        // Test different instances have different hash codes
        MultiKey<K> multiKey4 = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertNotEquals(multiKey1.hashCode(), multiKey4.hashCode());

        // Test modifying the keys changes the hash code
        MultiKey<K> multiKey5 = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<K> multiKey6 = new MultiKey<>(key1, key2, key3, "key4", key5);
        assertNotEquals(multiKey5.hashCode(), multiKey6.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals("MultiKey[key1, key2, key3, key4, key5]", multiKey.toString());
    }
}