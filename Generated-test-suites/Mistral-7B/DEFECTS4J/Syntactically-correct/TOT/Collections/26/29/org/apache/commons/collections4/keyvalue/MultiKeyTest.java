import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;

        K[] actualKeys = multiKey.getKeys();

        assertArrayEquals(expectedKeys, actualKeys);
        assertEquals(2, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKey() {
        K key1 = null;
        K key2 = "key2";

        try {
            new MultiKey<>(key1, key2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        K[] expectedKeys = new K[3];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;

        K[] actualKeys = multiKey.getKeys();

        assertArrayEquals(expectedKeys, actualKeys);
        assertEquals(3, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullArray() {
        K[] keys = null;

        try {
            new MultiKey<>(keys);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyEmptyArray() {
        K[] keys = new K[0];

        try {
            new MultiKey<>(keys);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));

        try {
            multiKey.getKey(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }
}