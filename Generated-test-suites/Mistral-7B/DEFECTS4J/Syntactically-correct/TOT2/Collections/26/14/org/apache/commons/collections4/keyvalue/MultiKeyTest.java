import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 // Test constructor with two keys
 @Test
 public void testMultiKeyTwoKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 K[] expectedKeys = {key1, key2};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with three keys
 @Test
 public void testMultiKeyThreeKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 K[] expectedKeys = {key1, key2, key3};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with four keys
 @Test
 public void testMultiKeyFourKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 K key4 = "key4";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
 K[] expectedKeys = {key1, key2, key3, key4};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with an array of keys
 @Test
 public void testMultiKeyArrayOfKeys() {
 K[] keys = {"key1", "key2", "key3"};
 MultiKey<String> multiKey = new MultiKey<>(keys);
 assertArrayEquals(keys, multiKey.getKeys());
 }

 // Test equals method
 @Test
 public void testEquals() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey3 = new MultiKey<>(key1, new K() {}); // Different object for key2
 MultiKey<K> multiKey4 = new MultiKey<>(new K[] {key1, key2});
 MultiKey<K> multiKey5 = new MultiKey<>(new K[] {key2, key1});

 assertEquals(multiKey1, multiKey2);
 assertNotEquals(multiKey1, multiKey3);
 assertNotEquals(multiKey1, multiKey4);
 assertNotEquals(multiKey1, multiKey5);
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key2, key1);

 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 }

 // Test getKey method
 @Test
 public void testGetKey() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);

 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 }

 // Test size method
 @Test
 public void testSize() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

 assertEquals(3, multiKey.size());
 }

 // Edge case: Test null keys in constructor
 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullKeys() {
 MultiKey<Object> multiKey = new MultiKey<>(null, null);
 }

 // Error case: Test changing keys after MultiKey creation
 @Test(expected = UnsupportedOperationException.class)
 public void testChangeKeys() {
 K key1 = "key1";
 MultiKey<K> multiKey = new MultiKey<>(key1, key1);
 key1 = new K() {};
 }
}