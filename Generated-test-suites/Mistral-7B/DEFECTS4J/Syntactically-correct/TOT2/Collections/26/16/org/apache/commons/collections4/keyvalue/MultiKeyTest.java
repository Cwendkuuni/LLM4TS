import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

 // Test constructor with two keys
 @Test
 public void testMultiKeyWithTwoKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 K[] expectedKeys = {key1, key2};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with three keys
 @Test
 public void testMultiKeyWithThreeKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 K[] expectedKeys = {key1, key2, key3};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with four keys
 @Test
 public void testMultiKeyWithFourKeys() {
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
 public void testMultiKeyWithArrayOfKeys() {
 K[] keys = {"key1", "key2"};
 MultiKey<K> multiKey = new MultiKey<>(keys);
 K[] expectedKeys = {"key1", "key2"};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test getting key at specified index
 @Test
 public void testGetKey() {
 K[] keys = {"key1", "key2"};
 MultiKey<String> multiKey = new MultiKey<>(keys);
 String key1 = multiKey.getKey(0);
 String key2 = multiKey.getKey(1);
 assertEquals("key1", key1);
 assertEquals("key2", key2);
 }

 // Test size
 @Test
 public void testSize() {
 K[] keys = {"key1", "key2"};
 MultiKey<String> multiKey = new MultiKey<>(keys);
 assertEquals(2, multiKey.size());
 }

 // Test equals
 @Test
 public void testEquals() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey3 = new MultiKey<>(key1, "key3");
 MultiKey<K> multiKey4 = new MultiKey(new Object[] {"key1", "key2"});
 MultiKey<K> multiKey5 = new MultiKey<>(new Object[] {"key3", "key1"});

 // same instance
 Assert.assertEquals(multiKey1, multiKey1);

 // equals with same keys
 Assert.assertEquals(multiKey1, multiKey2);

 // not equals with different keys
 Assert.assertNotEquals(multiKey1, multiKey3);

 // not equals with different keys order
 Assert.assertNotEquals(multiKey1, multiKey5);

 // not equals with different type
 MultiKey<Integer> multiKeyInt = new MultiKey<>(1, 2, 3, 4);
 Assert.assertNotEquals(multiKey1, multiKeyInt);

 // not equals with null
 MultiKey<K> multiKeyNull = null;
 Assert.assertNotEquals(multiKey1, multiKeyNull);
 }

 // Test hashCode
 @Test
 public void testHashCode() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 int hashCode = multiKey.hashCode();
 K[] keys = {key1, key2};
 MultiKey<K> multiKey2 = new MultiKey<>(keys);
 int hashCode2 = multiKey2.hashCode();
 Assert.assertEquals(hashCode, hashCode2);
 }

 // Test toString
 @Test
 public void testToString() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 String expected = "MultiKey[MultiKey[key1, key2]]";
 String actual = multiKey.toString();
 assertEquals(expected, actual);
 }
}