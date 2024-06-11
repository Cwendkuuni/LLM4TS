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
        int size = multiKey.size();

        // Then
        assertArrayEquals(new K[] {key1, key2}, keys);
        assertEquals(2, size);
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
        int size = multiKey.size();

        // Then
        assertArrayEquals(new K[] {key1, key2, key3}, keys);
        assertEquals(3, size);
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
        int size = multiKey.size();

        // Then
        assertArrayEquals(new K[] {key1, key2, key3, key4}, keys);
        assertEquals(4, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysArray() {
        // Given
        MultiKey<String> multiKey = new MultiKey<>(new String[0]);

        // Then
        // Exception is thrown in the constructor
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        // Given
        K key1 = "key1";
        K key2 = null;
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // Then
        // Exception is thrown in the constructor
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyOutOfBounds() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        int index = 3;

        // Then
        // Exception is thrown when getting a key out of bounds
    }
}