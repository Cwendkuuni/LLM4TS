import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructorTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[]{key1, key2};
        assertArrayEquals("Incorrect number of keys in MultiKey", expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[]{key1, key2, key3};
        assertArrayEquals("Incorrect number of keys in MultiKey", expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedToString = "MultiKey[MultiKey, MultiKey]";
        String actualToString = multiKey.toString();
        assertEquals("Incorrect toString representation", expectedToString, actualToString);
    }

    @Test
    public void testMultiKeyHashCodeEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals("MultiKey1 and MultiKey2 should have the same hashcode", multiKey1.hashCode(), multiKey2.hashCode());
        assertEquals("MultiKey1 and MultiKey2 should be equal", multiKey1, multiKey2);
    }

    @Test
    public void testMultiKeyHashCodeDifferentObjects() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        Object otherObject = new Object();
        assertNotSame("MultiKey1 and otherObject should not have the same hashcode", multiKey1.hashCode(), otherObject.hashCode());
        assertNotEquals("MultiKey1 and otherObject should not be equal", multiKey1, otherObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        K[] keys = new K[]{null};
        MultiKey<K> multiKey = new MultiKey<>(keys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullClone() {
        K[] keys = new K[]{null};
        MultiKey<K> multiKey = new MultiKey<>(keys, false);
    }

    @Test
    public void testMultiKeyClone() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        MultiKey<K> clonedMultiKey = multiKey.clone();
        assertNotSame("MultiKey and clonedMultiKey should not be the same instance", multiKey, clonedMultiKey);
        assertArrayEquals("ClonedMultiKey and MultiKey should have the same keys", multiKey.getKeys(), clonedMultiKey.getKeys());
    }
}