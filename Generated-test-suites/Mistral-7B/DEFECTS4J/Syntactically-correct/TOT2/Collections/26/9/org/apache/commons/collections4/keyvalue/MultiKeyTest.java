import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.keyvalue.MultiKeyTest; // Assuming this is a utility class for test data
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 // Test for constructor taking two keys
 @Test
 public void testMultiKeyTwoKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 assertArrayEquals(new K[] {key1, key2}, multiKey.getKeys());
 }

 // Test for constructor taking three keys
 @Test
 public void testMultiKeyThreeKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 assertArrayEquals(new K[] {key1, key2, key3}, multiKey.getKeys());
 }

 // Test for constructor taking an array of keys
 @Test
 public void testMultiKeyArrayOfKeys() {
 K[] keys = new K[] {"key1", "key2", "key3"};
 MultiKey<K> multiKey = new MultiKey<>(keys);
 assertArrayEquals(keys, multiKey.getKeys());
 }

 // Test for equals method
 @Test
 public void testMultiKeyEquals() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);
 MultiKey<K> multiKey4 = new MultiKey<>(Arrays.copyOfRange(multiKey1.getKeys(), 1, multiKey1.size()));

 assertTrue(multiKey1.equals(multiKey2));
 assertFalse(multiKey1.equals(multiKey3));
 assertFalse(multiKey1.equals(multiKey4));
 }

 // Test for hashCode method
 @Test
 public void testMultiKeyHashCode() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key2, key1);

 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 }

 // Edge case 1: empty array
 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyEmptyArray() {
 new MultiKey<>(new K[] {});
 }

 // Edge case 2: null key
 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullKey() {
 K[] keys = new K[] {"key1", null};
 new MultiKey<>(keys);
 }

 // Error scenario: changing keys after MultiKey is created
 @Test
 public void testMultiKeyChangeKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 key1 = "changedKey1";
 key2 = "changedKey2";
 fail("MultiKey keys should not be changed once created");
 }

}