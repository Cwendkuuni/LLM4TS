import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When

        // Test equals method
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>("key1", "key3");
        MultiKey<K> multiKey4 = new MultiKey<>("key2", "key1");

        // Then
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));

        // Test hashCode method
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());

        // Test getKeys method
        K[] keys = multiKey1.getKeys();
        assertArrayEquals(new Object[]{key1, key2}, keys);

        // Test size method
        assertEquals(2, multiKey1.size());

        // Test getKey method
        assertEquals(key1, multiKey1.getKey(0));
        assertEquals(key2, multiKey1.getKey(1));

        // Test equals method with null
        assertFalse(multiKey1.equals(null));

        // Test hashCode method with null
        assertNotEquals(multiKey1.hashCode(), null);

        // Test toString method
        assertEquals("MultiKey[key1, key2]", multiKey1.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        // Given
        K key1 = null;
        K key2 = null;

        // When
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // Then
        // Illegal argument exception should be thrown
    }

    @Test
    public void testMultiKeyThreeKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        // When

        // Test equals method
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2, key3);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, "key3");
        MultiKey<K> multiKey4 = new MultiKey<>("key1", "key2", "key3");

        // Then
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));

        // Test hashCode method
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());

        // Test getKeys method
        K[] keys = multiKey1.getKeys();
        assertArrayEquals(new Object[]{key1, key2, key3}, keys);

        // Test size method
        assertEquals(3, multiKey1.size());

        // Test getKey method
        assertEquals(key1, multiKey1.getKey(0));
        assertEquals(key2, multiKey1.getKey(1));
        assertEquals(key3, multiKey1.getKey(2));

        // Test equals method with null
        assertFalse(multiKey1.equals(null));

        // Test hashCode method with null
        assertNotEquals(multiKey1.hashCode(), null);

        // Test toString method
        assertEquals("MultiKey[key1, key2, key3]", multiKey1.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullArray() {
        // Given
        K[] keys = null;

        // When
        MultiKey<K> multiKey = new MultiKey<>(keys, false);

        // Then
        // Illegal argument exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysInArray() {
        // Given
        K[] keys = new K[2];
        keys[0] = null;

        // When
        MultiKey<K> multiKey = new MultiKey<>(keys, false);

        // Then
        // Illegal argument exception should be thrown
    }

    @Test
    public void testMultiKeyCloneArray() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        K[] keys = multiKey.getKeys();
        MultiKey<K> multiKeyCloned = new MultiKey<>(keys.clone(), false);

        // Then
        assertNotSame(multiKey, multiKeyCloned);
        assertTrue(multiKey.equals(multiKeyCloned));
    }
}