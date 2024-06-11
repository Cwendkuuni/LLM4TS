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
        assertEquals(2, multiKey.size());
        assertNotNull(multiKey.hashCode());
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
        assertEquals(3, multiKey.size());
        assertNotNull(multiKey.hashCode());
    }

    @Test
    public void testMultiKeyFourKeys() {
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
        assertNotNull(multiKey.hashCode());
    }

    @Test
    public void testMultiKeyArrayOfKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        K[] keys = new K[] {key1, key2, key3, key4, key5};
        MultiKey<K> multiKey = new MultiKey<>(keys);

        // When
        K[] keysFromMultiKey = multiKey.getKeys();

        // Then
        assertArrayEquals(keys, keysFromMultiKey);
        assertEquals(5, multiKey.size());
        assertNotNull(multiKey.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        // Given
        K[] keys = new K[] {null};

        // When
        MultiKey<K> multiKey = new MultiKey<>(keys);

        // Then
        fail("Expected IllegalArgumentException");
    }
}