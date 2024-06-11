import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 // Test constructor with two keys
 @Test
 public void testMultiKeyWithTwoKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 K[] keys = multiKey.getKeys();
 assertEquals(2, keys.length);
 assertEquals(key1, keys[0]);
 assertEquals(key2, keys[1]);
 }

 // Test constructor with three keys
 @Test
 public void testMultiKeyWithThreeKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 K[] keys = multiKey.getKeys();
 assertEquals(3, keys.length);
 assertEquals(key1, keys[0]);
 assertEquals(key2, keys[1]);
 assertEquals(key3, keys[2]);
 }

 // Test constructor with four keys
 @Test
 public void testMultiKeyWithFourKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 K key4 = "key4";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
 K[] keys = multiKey.getKeys();
 assertEquals(4, keys.length);
 assertEquals(key1, keys[0]);
 assertEquals(key2, keys[1]);
 assertEquals(key3, keys[2]);
 assertEquals(key4, keys[3]);
 }

 // Test constructor with five keys
 @Test
 public void testMultiKeyWithFiveKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 K key4 = "key4";
 K key5 = "key5";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 K[] keys = multiKey.getKeys();
 assertEquals(5, keys.length);
 assertEquals(key1, keys[0]);
 assertEquals(key2, keys[1]);
 assertEquals(key3, keys[2]);
 assertEquals(key4, keys[3]);
 assertEquals(key5, keys[4]);
 }

 // Test constructor with array of keys
 @Test
 public void testMultiKeyWithArrayOfKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey = new MultiKey<>((K[]) new Object[] {key1, key2, key3});
 K[] keys = multiKey.getKeys();
 assertEquals(3, keys.length);
 assertEquals(key1, keys[0]);
 assertEquals(key2, keys[1]);
 assertEquals(key3, keys[2]);
 }

 // Test equals method
 @Test
 public void testEquals() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key2, key1);
 MultiKey<K> multiKey3 = new MultiKey<>((K[]) new Object[] {key1, key2});
 MultiKey<K> multiKey4 = new MultiKey<>((K[]) new Object[] {key2, key1});
 MultiKey<K> multiKey5 = new MultiKey<>(key1);
 MultiKey<K> multiKey6 = new MultiKey<>((K[]) new Object[] {key1, key2, key3});
 MultiKey<K> multiKey7 = new MultiKey<>((K[]) new Object[] {key1, key2, key3, null});

 assertTrue(multiKey1.equals(multiKey1));
 assertTrue(multiKey1.equals(multiKey2));
 assertTrue(multiKey1.equals(multiKey3));
 assertTrue(multiKey1.equals(multiKey4));
 assertFalse(multiKey1.equals(multiKey5));
 assertFalse(multiKey1.equals(multiKey6));
 assertFalse(multiKey1.equals(multiKey7));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 int hashCode = multiKey.hashCode();
 MultiKey<K> multiKey2 = new MultiKey<>(key2, key1);
 int hashCode2 = multiKey2.hashCode();
 assertEquals(hashCode, hashCode2);
 }

 // Test size method
 @Test
 public void testSize() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 int size = multiKey.size();
 assertEquals(2, size);
 }

 // Test getKey method
 @Test
 public void testGetKey() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 K key = multiKey.getKey(0);
 assertEquals(key1, key);
 }

 // Test getting keys array
 @Test
 public void testGetKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 K[] keys = multiKey.getKeys();
 assertEquals(2, keys.length);
 assertEquals(key1, keys[0]);
 assertEquals(key2, keys[1]);
 }
}