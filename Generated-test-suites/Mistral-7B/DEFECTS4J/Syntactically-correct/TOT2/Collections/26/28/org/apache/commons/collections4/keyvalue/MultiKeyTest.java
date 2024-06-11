import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.keyvalue.MultiKeyTest; // Assuming a test utility class named MultiKeyTest is provided
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 // Test constructor with two keys
 @Test
 public void testMultiKeyTwoKeys() {
 K key1 = "testKey1";
 K key2 = "testKey2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 assertNotNull(multiKey);
 assertEquals(2, multiKey.size());
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 }

 // Test constructor with three keys
 @Test
 public void testMultiKeyThreeKeys() {
 K key1 = "testKey1";
 K key2 = "testKey2";
 K key3 = "testKey3";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 assertNotNull(multiKey);
 assertEquals(3, multiKey.size());
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 assertEquals(key3, multiKey.getKey(2));
 }

 // Test constructor with an array of keys
 @Test
 public void testMultiKeyArrayOfKeys() {
 K[] keys = {"testKey1", "testKey2", "testKey3"};
 MultiKey<String> multiKey = new MultiKey<>(keys);
 assertNotNull(multiKey);
 assertEquals(3, multiKey.size());
 assertArrayEquals(keys, multiKey.getKeys());
 }

 // Test equals method
 @Test
 public void testMultiKeyEquals() {
 K key1 = "testKey1";
 K key2 = "testKey2";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey3 = new MultiKey<>(key1, "testKey2");
 MultiKey<K> multiKey4 = new MultiKey<>("testKey1", "testKey2"); // Note: different instantiation order

 assertTrue(multiKey1.equals(multiKey2));
 assertTrue(multiKey1.equals(multiKey4));
 assertFalse(multiKey1.equals(multiKey3));
 }

 // Test hashCode method
 @Test
 public void testMultiKeyHashCode() {
 K key1 = "testKey1";
 K key2 = "testKey2";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key2, key1); // Different order of keys

 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 }

 // Test getKeys method
 @Test
 public void testMultiKeyGetKeys() {
 K key1 = "testKey1";
 K key2 = "testKey2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 K[] keys = multiKey.getKeys();
 assertArrayEquals(new Object[]{key1, key2}, keys);
 }

 // Test size method
 @Test
 public void testMultiKeySize() {
 K key1 = "testKey1";
 K key2 = "testKey2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 assertEquals(2, multiKey.size());
 }

 // Test exception handling when keys are null
 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullKeys() {
 MultiKey<Object> multiKey = new MultiKey<>(null, null);
 }

 // Test exception handling when making a clone without cloning the keys array
 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyCloneWithoutCloningKeys() {
 MultiKey<String> multiKey = new MultiKey<>(new String[]{"testKey1", "testKey2"});
 MultiKey<String> multiKeyClone = new MultiKey<>(multiKey.keys, false);
 }

 // Edge case testing for large number of keys
 @Test
 public void testMultiKeyLargeNumberOfKeys() {
 final int numberOfKeys = 100;
 K[] keys = new K[numberOfKeys];
 for (int i = 0; i < numberOfKeys; i++) {
 keys[i] = "testKey" + i;
 }
 MultiKey<K> multiKey = new MultiKey<>(keys);
 assertEquals(numberOfKeys, multiKey.size());
 }
}