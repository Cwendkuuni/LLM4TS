import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testTwoKeysConstructor() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[]{key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedSize = 2;
        assertEquals(expectedSize, multiKey.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyOutOfBounds() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int index = 3;
        multiKey.getKey(index);
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testNotEqualsDifferentSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1);
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testNotEqualsDifferentKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, "key3");
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = new K[]{key1, key2};
        int expectedHashCode = keys[0].hashCode() ^ keys[1].hashCode();
        assertEquals(expectedHashCode, multiKey.hashCode());
    }

    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedToString = "MultiKey[MultiKey@" + hashCode(multiKey) + ", " + key1 + ", " + key2 + "]";
        assertEquals(expectedToString, multiKey.toString());
    }

    private int hashCode(Object obj) {
        return obj.hashCode();
    }
}