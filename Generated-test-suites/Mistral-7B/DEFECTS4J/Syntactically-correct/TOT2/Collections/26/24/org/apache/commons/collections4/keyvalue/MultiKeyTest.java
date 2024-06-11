import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Arrays;

public class MultiKeyTest {

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

 @Test
 public void testMultiKeyWithArrayOfKeys() {
 K[] keys = new K[] {"key1", "key2", "key3"};
 MultiKey<K> multiKey = new MultiKey<>(keys);
 K[] keysFromMultiKey = multiKey.getKeys();
 assertEquals(Arrays.toString(keys), Arrays.toString(keysFromMultiKey));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeysArray() {
 MultiKey<K> multiKey = new MultiKey<>(null);
 }

 @Test
 public void testGetKey() {
 MultiKey<Integer> multiKey = new MultiKey<>(new Integer(1), new Integer(2));
 assertEquals(1, multiKey.getKey(0).intValue());
 assertEquals(2, multiKey.getKey(1).intValue());
 }

 @Test
 public void testSize() {
 MultiKey<Integer> multiKey = new MultiKey<>(new Integer(1), new Integer(2));
 assertEquals(2, multiKey.size());
 }

 @Test
 public void testEquals() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "key3");
 assertEquals(multiKey1, multiKey2);
 assertNotSame(multiKey1, multiKey3);
 }

 @Test
 public void testHashCode() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 assertEquals("MultiKey[key1, key2]", multiKey.toString());
 }

}