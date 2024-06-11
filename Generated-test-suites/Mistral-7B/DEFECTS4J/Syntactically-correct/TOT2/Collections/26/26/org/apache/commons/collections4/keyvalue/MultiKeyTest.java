import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 // Test method for MultiKey constructor taking two keys
 @Test
 public void testMultiKeyTwoKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 K[] expectedKeys = new K[2];
 expectedKeys[0] = key1;
 expectedKeys[1] = key2;
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test method for MultiKey constructor taking three keys
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
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test method for MultiKey constructor taking an array of keys which is cloned
 @Test
 public void testMultiKeyArrayOfKeysCloned() {
 K[] keys = new K[2];
 keys[0] = "key1";
 keys[1] = "key2";
 MultiKey<K> multiKey = new MultiKey<>(keys);
 K[] expectedKeys = new K[2];
 expectedKeys[0] = "key1";
 expectedKeys[1] = "key2";
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test method for MultiKey constructor taking an array of keys, optionally choosing whether to clone
 @Test
 public void testMultiKeyArrayOfKeysNotCloned() {
 K[] keys = new K[2];
 keys[0] = "key1";
 keys[1] = "key2";
 MultiKey<K> multiKey = new MultiKey<>(keys, false);
 K[] expectedKeys = new K[2];
 expectedKeys[0] = "key1";
 expectedKeys[1] = "key2";
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 // This would cause a bug, as the hashcode is calculated here and not changed
 // when the array is modified
 // keys[0] = "changedKey";
 // assertNotEquals(expectedKeys, multiKey.getKeys());
 }

 // Test method for MultiKey equals method
 @Test
 public void testMultiKeyEquals() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
 assertTrue(multiKey1.equals(multiKey2));
 }

 // Test method for MultiKey hashCode method
 @Test
 public void testMultiKeyHashCode() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 int expectedHashCode = key1.hashCode() ^ key2.hashCode();
 int actualHashCode = multiKey.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 // Test method for MultiKey size method
 @Test
 public void testMultiKeySize() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 int expectedSize = 2;
 int actualSize = multiKey.size();
 assertEquals(expectedSize, actualSize);
 }

 // Test method for MultiKey getKey method
 @Test (expected = IndexOutOfBoundsException.class)
 public void testMultiKeyGetKeyOutOfBounds() {
 K[] keys = new K[2];
 keys[0] = "key1";
 keys[1] = "key2";
 MultiKey<K> multiKey = new MultiKey<>(keys);
 multiKey.getKey(2);
 }

 // Test method for MultiKey getKeys method
 @Test
 public void testMultiKeyGetKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 K[] expectedKeys = new K[2];
 expectedKeys[0] = key1;
 expectedKeys[1] = key2;
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

}