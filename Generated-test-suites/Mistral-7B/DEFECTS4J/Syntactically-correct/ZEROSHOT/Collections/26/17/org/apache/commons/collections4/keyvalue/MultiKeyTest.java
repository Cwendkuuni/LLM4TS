import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstruction() {
 MultiKey<String> key1_1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> key1_2 = new MultiKey<>("key1", "locale2");
 MultiKey<String> key2_1 = new MultiKey<>("key2", "locale1");
 MultiKey<String> key2_2 = new MultiKey<>("key2", "locale2");
 MultiKey<String> keys1 = new MultiKey<String>(new String[]{"key1", "locale1"});
 MultiKey<String> keys2 = new MultiKey<String>(new String[]{"key2", "locale1"});
 MultiKey<String> keys3 = new MultiKey<String>(new String[]{"key1", "locale1", "locale2"});

 assertEquals("key1,locale1", key1_1.toString());
 assertEquals("key1,locale2", key1_2.toString());
 assertEquals("key2,locale1", key2_1.toString());
 assertEquals("key2,locale2", key2_2.toString());
 assertEquals("key1,locale1", keys1.toString());
 assertEquals("key2,locale1", keys2.toString());
 assertEquals("key1,locale1,locale2", keys3.toString());
 }

 @Test
 public void testMultiKeyGetKey() {
 MultiKey<String> key1_1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> key1_2 = new MultiKey<>("key1", "locale2");
 MultiKey<String> keys1 = new MultiKey<String>(new String[]{"key1", "locale1"});

 assertEquals("locale1", key1_1.getKey(0).toString());
 assertEquals("locale2", key1_2.getKey(0).toString());
 assertEquals("locale1", keys1.getKey(0).toString());
 }

 @Test
 public void testMultiKeyGetKeys() {
 MultiKey<String> key1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> key2 = new MultiKey<>("key2", "locale2");
 MultiKey<String> keys1 = new MultiKey<String>(new String[]{"key1", "locale1"});
 MultiKey<String> keys2 = new MultiKey<String>(new String[]{"key2", "locale2"});

 assertArrayEquals(new String[]{"key1", "locale1"}, key1.getKeys());
 assertArrayEquals(new String[]{"key2", "locale2"}, key2.getKeys());
 assertArrayEquals(new String[]{"key1", "locale1"}, keys1.getKeys());
 assertArrayEquals(new String[]{"key2", "locale2"}, keys2.getKeys());
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey<String> key1_1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> key1_2 = new MultiKey<>("key1", "locale2");
 MultiKey<String> keys1 = new MultiKey<String>(new String[]{"key1", "locale1"});

 assertEquals(key1_1.hashCode(), key1_1.hashCode());
 assertEquals(key1_1.hashCode(), keys1.hashCode());
 assertNotEquals(key1_1.hashCode(), key1_2.hashCode());
 assertNotEquals(key1_1.hashCode(), keys2.hashCode());
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey<String> key1_1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> key1_2 = new MultiKey<>("key1", "locale2");
 MultiKey<String> keys1 = new MultiKey<String>(new String[]{"key1", "locale1"});
 MultiKey<String> keys2 = new MultiKey<String>(new String[]{"key2", "locale2"});

 assertTrue(key1_1.equals(key1_1));
 assertTrue(key1_1.equals(keys1));
 assertFalse(key1_1.equals(key1_2));
 assertFalse(key1_1.equals(keys2));
 }
}