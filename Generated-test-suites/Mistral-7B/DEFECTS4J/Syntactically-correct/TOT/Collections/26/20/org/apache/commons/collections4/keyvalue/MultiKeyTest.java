import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
         prejudgeEquals(new MultiKey<>(key1, key2), new MultiKey<>(key1, key2));
         prejudgeEquals(new MultiKey<>(key1, key2), new MultiKey<>(key2, key1));
         prejudgeHashCode(new MultiKey<>(key1, key2), key1, key2);
         testGetKey(new MultiKey<>(key1, key2), 0, key1);
         testGetKey(new MultiKey<>(key1, key2), 1, key2);
         testGetKeys(new MultiKey<>(key1, key2));
         testSize(new MultiKey<>(key1, key2));
         testHashCode(new MultiKey<>(key1, key2));
         testEquals(new MultiKey<>(key1, key2), new MultiKey<>(key1, key2));
         testEquals(new MultiKey<>(key1, key2), new MultiKey<>(key2, key1));
         testNotEquals(new MultiKey<>(key1, key2), new MultiKey<>(key1, "key3"));
         testNotEquals(new MultiKey<>(key1, key2), new Object());
         testNullKey(new MultiKey<>(key1, key2));
    }

    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
         prejudgeEquals(new MultiKey<>(key1, key2, key3), new MultiKey<>(key1, key2, key3));
         prejudgeEquals(new MultiKey<>(key1, key2, key3), new MultiKey<>(key2, key1, key3));
         prejudgeEquals(new MultiKey<>(key1, key2, key3), new MultiKey<>(key2, key3, key1));
         prejudgeEquals(new MultiKey<>(key1, key2, key3), new MultiKey<>(key3, key1, key2));
         prejudgeHashCode(new MultiKey<>(key1, key2, key3), key1, key2, key3);
         testGetKey(new MultiKey<>(key1, key2, key3), 0, key1);
         testGetKey(new MultiKey<>(key1, key2, key3), 1, key2);
         testGetKey(new MultiKey<>(key1, key2, key3), 2, key3);
         testGetKeys(new MultiKey<>(key1, key2, key3));
         testSize(new MultiKey<>(key1, key2, key3));
         testHashCode(new MultiKey<>(key1, key2, key3));
         testEquals(new MultiKey<>(key1, key2, key3), new MultiKey<>(key1, key2, key3));
         testEquals(new MultiKey<>(key1, key2, key3), new MultiKey<>(key2, key1, key3));
         testEquals(new MultiKey<>(key1, key2, key3), new MultiKey<>(key2, key3, key1));
         testEquals(new MultiKey<>(key1, key2, key3), new MultiKey<>(key3, key1, key2));
         testNotEquals(new MultiKey<>(key1, key2, key3), new MultiKey<>(key1, key2, "key4"));
         testNotEquals(new MultiKey<>(key1, key2, key3), new Object());
         testNullKey(new MultiKey<>(key1, key2, key3));
    }

    @Test
    public void testMultiKeyFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key2, key3, key4));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key2, key4, key3));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key3, key2, key4));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key3, key4, key2));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key4, key2, key3));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key4, key3, key2));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key2, key1, key3, key4));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key2, key1, key4, key3));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key2, key3, key1, key4));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key2, key3, key4, key1));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key3, key1, key2, key4));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key3, key1, key4, key2));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key3, key2, key1, key4));
         prejudgeEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key3, key2, key4, key1));
         prejudgeHashCode(new MultiKey<>(key1, key2, key3, key4), key1, key2, key3, key4);
         testGetKey(new MultiKey<>(key1, key2, key3, key4), 0, key1);
         testGetKey(new MultiKey<>(key1, key2, key3, key4), 1, key2);
         testGetKey(new MultiKey<>(key1, key2, key3, key4), 2, key3);
         testGetKey(new MultiKey<>(key1, key2, key3, key4), 3, key4);
         testGetKeys(new MultiKey<>(key1, key2, key3, key4));
         testSize(new MultiKey<>(key1, key2, key3, key4));
         testHashCode(new MultiKey<>(key1, key2, key3, key4));
         testEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key2, key3, key4));
         testEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key2, key4, key3));
         testEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key3, key2, key4));
         testEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key3, key4, key2));
         testEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key4, key2, key3));
         testEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key4, key3, key2));
         testEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key2, key1, key3, key4));
         testEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key2, key1, key4, key3));
         testEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key2, key3, key1, key4));
         testEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key2, key3, key4, key1));
         testNotEquals(new MultiKey<>(key1, key2, key3, key4), new MultiKey<>(key1, key2, "key5"));
         testNotEquals(new MultiKey<>(key1, key2, key3, key4), new Object());
         testNullKey(new MultiKey<>(key1, key2, key3, key4));
    }

    private void prejudgeEquals(MultiKey<?> multiKey, Object... keys) {
        MultiKey<?> other = new MultiKey<>(keys);
        assertEquals(multiKey, other);
    }

    private void prejudgeHashCode(MultiKey<?> multiKey, Object... keys) {
        int hashCode = 0;
        for (Object key : keys) {
            if (key != null) {
                hashCode ^= key.hashCode();
            }
        }
        assertEquals(multiKey.hashCode(), hashCode);
    }

    private void testGetKey(MultiKey<?> multiKey, int index, K expectedKey) {
        K actualKey = multiKey.getKey(index);
        assertEquals(expectedKey, actualKey);
    }

    private void testGetKeys(MultiKey<?> multiKey) {
        K[] keys = multiKey.getKeys();
        assertArrayEquals(multiKey.keys, keys);
    }

    private void testSize(MultiKey<?> multiKey) {
        int size = multiKey.size();
        assertEquals(multiKey.keys.length, size);
    }

    private void testHashCode(MultiKey<?> multiKey) {
        int hashCode = multiKey.hashCode();
        assertNotSame(0, hashCode);
    }

    private void testEquals(MultiKey<?> multiKey1, MultiKey<?> multiKey2) {
        assertTrue(multiKey1.equals(multiKey2));
    }

    private void testNotEquals(MultiKey<?> multiKey, Object other) {
        assertFalse(multiKey.equals(other));
    }

    private void testNullKey(MultiKey<?> multiKey) {
        try {
            multiKey.put(null, "value");
            fail("Should not accept a null key");
        } catch (IllegalArgumentException expected) {
        }
    }
}