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
        K[] keys = multiKey.getKeys();

        // Then
        assertArrayEquals(new K[] {key1, key2}, keys);

        // Test equals method
        MultiKey<K> multiKeyEqual = new MultiKey<>(key1, key2);
        assertTrue(multiKey.equals(multiKeyEqual));

        // Test hashCode method
        int hashCode1 = multiKey.hashCode();
        MultiKey<K> multiKeyEqualHashCode = new MultiKey<>(key1, key2);
        assertEquals(hashCode1, multiKeyEqual.hashCode());

        // Test size method
        int size = multiKey.size();
        assertEquals(2, size);

        // Test getKey method
        K key = multiKey.getKey(0);
        assertEquals(key1, key);

        // Test getKey method with out of bounds index
        try {
            key = multiKey.getKey(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Test equals method with null object
        assertFalse(multiKey.equals(null));

        // Test equals method with different type of object
        assertFalse(multiKey.equals(new Object()));

        // Test equals method with different number of keys
        MultiKey<K> multiKeyDifferentSize = new MultiKey<>(key1, key2, "key3");
        assertFalse(multiKey.equals(multiKeyDifferentSize));

        // Test equals method with same keys in different order
        MultiKey<K> multiKeyDifferentOrder = new MultiKey<>(key2, key1);
        assertFalse(multiKey.equals(multiKeyDifferentOrder));

        // Test equals method with null keys
        MultiKey<String> multiKeyNullKey = new MultiKey<>(null, key2);
        assertFalse(multiKey.equals(multiKeyNullKey));

        // Test hashCode method with null keys
        MultiKey<String> multiKeyHashNullKey = new MultiKey<>(null, key2);
        assertNotEquals(multiKey.hashCode(), multiKeyHashNullKey.hashCode());

        // Test toString method
        String toString = multiKey.toString();
        assertTrue(toString.contains("MultiKey"));
        assertTrue(toString.contains("[key1, key2]"));
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

        // Then
        assertArrayEquals(new K[] {key1, key2, key3}, keys);

        // Test equals method
        MultiKey<K> multiKeyEqual = new MultiKey<>(key1, key2, key3);
        assertTrue(multiKey.equals(multiKeyEqual));

        // Test hashCode method
        int hashCode1 = multiKey.hashCode();
        MultiKey<K> multiKeyEqualHashCode = new MultiKey<>(key1, key2, key3);
        assertEquals(hashCode1, multiKeyEqual.hashCode());

        // Test size method
        int size = multiKey.size();
        assertEquals(3, size);

        // Test getKey method
        K key = multiKey.getKey(0);
        assertEquals(key1, key);

        // Test getKey method with out of bounds index
        try {
            key = multiKey.getKey(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Test equals method with null object
        assertFalse(multiKey.equals(null));

        // Test equals method with different type of object
        assertFalse(multiKey.equals(new Object()));

        // Test equals method with different number of keys
        MultiKey<K> multiKeyDifferentSize = new MultiKey<>(key1, key2, key3, "key4");
        assertFalse(multiKey.equals(multiKeyDifferentSize));

        // Test equals method with keys in different order
        MultiKey<K> multiKeyDifferentOrder = new MultiKey<>(key2, key1, key3);
        assertFalse(multiKey.equals(multiKeyDifferentOrder));

        // Test equals method with null keys
        MultiKey<String> multiKeyNullKey = new MultiKey<>(null, null, key3);
        assertFalse(multiKey.equals(multiKeyNullKey));

        // Test hashCode method with null keys
        MultiKey<String> multiKeyHashNullKey = new MultiKey<>(null, null, key3);
        assertNotEquals(multiKey.hashCode(), multiKeyHashNullKey.hashCode());

        // Test toString method
        String toString = multiKey.toString();
        assertTrue(toString.contains("MultiKey"));
        assertTrue(toString.contains("[key1, key2, key3]"));
    }
}