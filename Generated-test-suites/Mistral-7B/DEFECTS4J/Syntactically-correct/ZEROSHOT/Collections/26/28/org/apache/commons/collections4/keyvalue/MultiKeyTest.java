import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstruction() {
 // Test construction with two keys
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 assertNotNull(key12);
 assertArrayEquals(new String[]{"key1", "key2"}, key12.getKeys());

 // Test construction with three keys
 MultiKey<String> key123 = new MultiKey<>("key1", "key2", "key3");
 assertNotNull(key123);
 assertArrayEquals(new String[]{"key1", "key2", "key3"}, key123.getKeys());
 }

 @Test
 public void testMultiKeySize() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 assertEquals(2, key12.size());
 }

 @Test
 public void testGetKey() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 assertEquals("key1", key12.getKey(0));
 assertEquals("key2", key12.getKey(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetKeyInvalidIndex() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 key12.getKey(2);
 }

 @Test
 public void testEquals() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 MultiKey<String> key12Equal = new MultiKey<>("key1", "key2");
 MultiKey<String> key13 = new MultiKey<>("key1", "key3");
 MultiKey<String> key32 = new MultiKey<>("key3", "key2");

 assertTrue(key12.equals(key12Equal));
 assertFalse(key12.equals(key13));
 assertFalse(key12.equals(key32));
 assertFalse(key12.equals(new Object()));
 }

 @Test
 public void testHashCode() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 MultiKey<String> key12Equal = new MultiKey<>("key1", "key2");
 MultiKey<String> key13 = new MultiKey<>("key1", "key3");
 MultiKey<String> key32 = new MultiKey<>("key3", "key2");
 MultiKey<String> keyOther = new MultiKey<>("otherKey", "key2");

 assertEquals(key12.hashCode(), key12Equal.hashCode());
 assertNotEquals(key12.hashCode(), key13.hashCode());
 assertNotEquals(key12.hashCode(), key32.hashCode());
 assertNotEquals(key12.hashCode(), keyOther.hashCode());
 }

}