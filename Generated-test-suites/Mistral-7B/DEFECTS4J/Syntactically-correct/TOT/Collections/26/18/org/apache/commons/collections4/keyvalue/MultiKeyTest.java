import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test class for {@link MultiKey} class.
 */
public class MultiKeyTest {

    /**
     * Test constructor with two keys.
     */
    @Test
    public void testTwoKeysConstructor() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertNotNull(multiKey);
        assertEquals(2, multiKey.size());
        assertArrayEquals(new String[] {key1, key2}, multiKey.getKeys());
    }

    /**
     * Test constructor with three keys.
     */
    @Test
    public void testThreeKeysConstructor() {
        final String key1 = "key1";
        final String key2 = "key2";
        final String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        assertNotNull(multiKey);
        assertEquals(3, multiKey.size());
        assertArrayEquals(new String[] {key1, key2, key3}, multiKey.getKeys());
    }

    /**
     * Test constructor with an array of keys.
     */
    @Test
    public void testArrayOfKeysConstructor() {
        final String key1 = "key1";
        final String key2 = "key2";
        final String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(new String[] {key1, key2, key3});
        assertNotNull(multiKey);
        assertEquals(3, multiKey.size());
        assertArrayEquals(new String[] {key1, key2, key3}, multiKey.getKeys());
    }

    /**
     * Test equals method with equal instances.
     */
    @Test
    public void testEqualsWithEqualInstances() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
    }

    /**
     * Test equals method with null instance.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEqualsWithNullInstance() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertNotNull(multiKey);
        assertFalse(multiKey.equals(null));
    }

    /**
     * Test equals method with different types of instances.
     */
    @Test
    public void testEqualsWithDifferentTypes() {
        final String key1 = "key1";
        final int key2 = 1;
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        assertFalse(multiKey1.equals(key2));
    }

    /**
     * Test equals method with different keys.
     */
    @Test
    public void testEqualsWithDifferentKeys() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>("key3", "key4");
        assertFalse(multiKey1.equals(multiKey2));
    }

    /**
     * Test hashCode method with equal instances.
     */
    @Test
    public void testHashCodeWithEqualInstances() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    /**
     * Test hashCode method with different keys.
     */
    @Test
    public void testHashCodeWithDifferentKeys() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>("key3", "key4");
        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}