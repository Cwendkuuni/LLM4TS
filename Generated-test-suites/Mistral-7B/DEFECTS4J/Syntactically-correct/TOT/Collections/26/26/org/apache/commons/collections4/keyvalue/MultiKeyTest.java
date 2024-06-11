import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyWithTwoKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        K[] keys = multiKey.getKeys();

        // Then
        assertArrayEquals(new K[] {key1, key2}, keys);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        // When
        K[] keys = multiKey.getKeys();

        // Then
        assertArrayEquals(new K[] {key1, key2, key3}, keys);
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testMultiKeyWithFourKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);

        // When
        K[] keys = multiKey.getKeys();

        // Then
        assertArrayEquals(new K[] {key1, key2, key3, key4}, keys);
        assertEquals(4, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysArray() {
        // Given
        MultiKey<K> multiKey = new MultiKey<>(null);

        // Then
        fail("Should throw IllegalArgumentException for null keys array");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        // Given
        K[] keys = new K[] {"key1", null};
        MultiKey<K> multiKey = new MultiKey<>(keys);

        // Then
        fail("Should throw IllegalArgumentException for null key in keys array");
    }

    @Test
    public void testEquals() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);

        // When
        boolean equals = multiKey1.equals(multiKey2);

        // Then
        assertTrue(equals);
    }

    @Test
    public void testNotEquals() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");

        // When
        boolean equals = multiKey1.equals(multiKey2);

        // Then
        assertFalse(equals);
    }

    @Test
    public void testHashCode() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        int hashCode = multiKey.hashCode();

        // Then
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        int hashCode2 = multiKey2.hashCode();

        // Assert hash codes are equal
        assertEquals(hashCode, hashCode2);
    }
}