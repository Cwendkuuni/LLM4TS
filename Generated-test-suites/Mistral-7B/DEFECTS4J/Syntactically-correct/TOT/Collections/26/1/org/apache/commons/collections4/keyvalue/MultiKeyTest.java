import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // When
        final String[] expectedKeys = new String[]{"key1", "key2"};
        final String[] actualKeys = multiKey.getKeys();

        // Then
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeyThreeKeys() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final String key3 = "key3";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        // When
        final String[] expectedKeys = new String[]{"key1", "key2", "key3"};
        final String[] actualKeys = multiKey.getKeys();

        // Then
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        // Given
        final MultiKey<String> multiKey = new MultiKey<>(null);

        // Then
        // The constructor should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKey() {
        // Given
        final K[] keys = new K[]{null};
        final MultiKey<String> multiKey = new MultiKey<>(keys);

        // Then
        // The constructor should throw an IllegalArgumentException
    }

    @Test
    public void testMultiKeyEquals() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        final MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        // When
        // Then
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
    }

    @Test
    public void testMultiKeyNotEquals() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final String key3 = "key3";
        final MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        final MultiKey<String> multiKey2 = new MultiKey<>(key1, key3);

        // When
        // Then
        assertFalse(multiKey1.equals(multiKey2));
        assertFalse(multiKey2.equals(multiKey1));
    }

    @Test
    public void testMultiKeyHashCode() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // When
        final int expectedHashCode = (31 * key1.hashCode()) ^ key2.hashCode();
        final int actualHashCode = multiKey.hashCode();

        // Then
        assertEquals(expectedHashCode, actualHashCode);
    }
}