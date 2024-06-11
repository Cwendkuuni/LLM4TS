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
 K[] keys = multiKey.getKeys();
 assertEquals(2, keys.length);
 assertEquals(key1, keys[0]);
 assertEquals(key2, keys[1]);
 }

 // Test constructor with three keys
 @Test
 public void testMultiKeyThreeKeys() {
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
 public void testMultiKeyFourKeys() {
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
 public void testMultiKeyFiveKeys() {
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

 // Test constructor with an array of keys
 @Test
 public void testMultiKeyArrayOfKeys() {
 K[] keys = new K[]{new K() { @Override public String toString() { return "key1"; }},
 new K() { @Override public String toString() { return "key2"; }},
 new K() { @Override public String toString() { return "key3"; }}};
 MultiKey<K> multiKey = new MultiKey<>(keys);
 K[] keysTest = multiKey.getKeys();
 assertArrayEquals(keys, keysTest);
 }

 // Test equals method
 @Test
 public void testEquals() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey3 = new MultiKey<>(key1, "key3");
 MultiKey<K> multiKey4 = new MultiKey<>((K)new Object(){@Override public String toString() {return "key1";}}, (K)new Object(){@Override public String toString() {return "key2";}});
 MultiKey<K> multiKey5 = new MultiKey<>(new K[] {key1, key2});
 assertEquals(multiKey1, multiKey1);
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
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey3 = new MultiKey<>(key1, "key3");
 MultiKey<K> multiKey4 = new MultiKey<>((K)new Object(){@Override public String toString() {return "key1";}}, (K)new Object(){@Override public String toString() {return "key2";}});
 MultiKey<K> multiKey5 = new MultiKey<>(new K[] {key1, key2});
 assertEquals(multiKey1.hashCode(), multiKey1.hashCode());
 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey4.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey5.hashCode());
 }

 // Test size method
 @Test
 public void testSize() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 assertEquals(2, multiKey.size());
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

 // Test toString method
 @Test
 public void testToString() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 assertEquals("MultiKey[key1, key2]", multiKey.toString());
 }

 // Edge cases and exception handling
 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 new MultiKey<>(null);
 }

 @Test
 public void testCloneable() {
 MultiKey<K> multiKey = new MultiKey<>(new K[] {new K() { @Override public String toString() { return "key1"; }},
 new K() { @Override public String toString() { return "key2"; }},
 new K() { @Override public String toString() { return "key3"; }}});
 MultiKey<K> clonedMultiKey = (MultiKey<K>) multiKey.clone();
 MultiKey<K> multiKeyFromArray = new MultiKey<>(multiKey.getKeys().clone());
 assertEquals(multiKey, multiKeyFromArray);
 assertEquals(multiKey, clonedMultiKey);
 }
}