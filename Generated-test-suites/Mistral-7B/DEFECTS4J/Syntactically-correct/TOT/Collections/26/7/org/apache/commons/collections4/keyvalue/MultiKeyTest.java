import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for MultiKey class.
 */
public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        K[] keys = multiKey.getKeys();
        int size = multiKey.size();

        // Then
        assertArrayEquals("Keys should be correct", new K[] {key1, key2}, keys);
        assertEquals("Size should be correct", 2, size);
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
        int size = multiKey.size();

        // Then
        assertArrayEquals("Keys should be correct", new K[] {key1, key2, key3}, keys);
        assertEquals("Size should be correct", 3, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        // Given
        K[] keys = null;

        // When
        MultiKey<K> multiKey = new MultiKey<>(keys);

        // Then
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKey() {
        // Given
        K[] keys = new K[] {"key1", null};

        // When
        MultiKey<K> multiKey = new MultiKey<>(keys);

        // Then
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testMultiKeyCloneKeys() {
        // Given
        K key1 = new ImmutableK("key1");
        K key2 = new ImmutableK("key2");
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});

        // When
        K[] keys = multiKey.getKeys();

        // Then
        assertNotSame("Keys should not be the same instance", key1, keys[0]);
        assertNotSame("Keys should not be the same instance", key2, keys[1]);
    }

    @Test
    public void testMultiKeyEquals() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);

        // When
        MultiKey<K> multiKey3 = new MultiKey<>(new K[] {"key3", "key4"});

        // Then
        assertTrue("MultiKey1 should be equal to MultiKey2", multiKey1.equals(multiKey2));
        assertFalse("MultiKey1 should not be equal to MultiKey3", multiKey1.equals(multiKey3));
    }

    @Test
    public void testMultiKeyHashCode() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        int hashCode = multiKey.hashCode();

        // Then
        MultiKey<K> multiKeyCopy = new MultiKey<>(multiKey.getKeys());
        int hashCodeCopy = multiKeyCopy.hashCode();

        // Hashcodes should be equal for equal objects
        assertEquals("Hashcodes should be equal", hashCode, hashCodeCopy);
    }
}