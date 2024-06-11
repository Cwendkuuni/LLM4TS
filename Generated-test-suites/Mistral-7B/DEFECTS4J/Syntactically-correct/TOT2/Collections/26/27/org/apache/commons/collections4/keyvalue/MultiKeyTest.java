import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.keyvalue.MultiKeyTestCase;
import org.junit.Test;

public class MultiKeyTest extends MultiKeyTestCase {

    @Test
    public void testMultiKeyConstructorWithTwoKeys() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertMultiKeyEquals(multiKey, key1, key2);
    }

    @Test
    public void testMultiKeyConstructorWithThreeKeys() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        K key3 = createKey("key3");
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        assertMultiKeyEquals(multiKey, key1, key2, key3);
    }

    @Test
    public void testMultiKeyConstructorWithFourKeys() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        K key3 = createKey("key3");
        K key4 = createKey("key4");
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertMultiKeyEquals(multiKey, key1, key2, key3, key4);
    }

    @Test
    public void testMultiKeyConstructorWithArrayOfKeys() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        K key3 = createKey("key3");
        K[] keys = new K[]{key1, key2, key3};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        assertMultiKeyEquals(multiKey, key1, key2, key3);
    }

    @Test
    public void testMultiKeyConstructorWithClonedArrayOfKeys() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        K key3 = createKey("key3");
        K[] keys = new K[]{key1, key2, key3};
        MultiKey<K> multiKey = new MultiKey<>(keys, false);
        assertMultiKeyEquals(multiKey, key1, key2, key3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructorWithNullArrayOfKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyInvalidKeyTypes() {
        String key1 = "key1";
        int key2 = 1;
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
    }

    @Test
    public void testMultiKeyGetKeys() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = {key1, key2};
        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K expectedKey = key1;
        K actualKey = multiKey.getKey(0);
        assertEquals(expectedKey, actualKey);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyOutOfBounds() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(1);
    }

    @Test
    public void testMultiKeySize() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedSize = 2;
        int actualSize = multiKey.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, createKey("key3"));
        MultiKey<Integer> multiKey4 = new MultiKey<>(createKey("key1"), createKey("key2"));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedHashCode = 2 * key1.hashCode() + 3 * key2.hashCode();
        int actualHashCode = multiKey.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = createKey("key1");
        K key2 = createKey("key2");
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedString = "MultiKey[keys=[key1, key2]]";
        String actualString = multiKey.toString();
        assertEquals(expectedString, actualString);
    }
}