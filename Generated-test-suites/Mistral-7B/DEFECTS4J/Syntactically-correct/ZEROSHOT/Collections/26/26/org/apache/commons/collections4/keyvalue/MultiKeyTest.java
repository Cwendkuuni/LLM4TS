import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstruction() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 MultiKey<String> key123 = new MultiKey<>("key1", "key2", "key3");
 MultiKey<String> key1234 = new MultiKey<>("key1", "key2", "key3", "key4");
 MultiKey<String> keyArray = new MultiKey(new String[]{"key1", "key2"});
 MultiKey<String> keyArrayClone = new MultiKey(new String[]{"key1", "key2"}, true);

 assertEquals("key1, key2", key12.toString());
 assertEquals("key1, key2", key12.toString());
 assertEquals("key1, key2, key3", key123.toString());
 assertEquals("key1, key2, key3, key4", key1234.toString());
 assertEquals("key1, key2", keyArray.toString());
 assertEquals("key1, key2", keyArray.toString());
 assertArrayEquals(new String[]{"key1", "key2"}, keyArrayClone.getKeys());
 }

 @Test
 public void testMultiKeyGetMethods() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

 assertEquals("key1", multiKey.getKey(0));
 assertEquals("key2", multiKey.getKey(1));
 assertEquals(2, multiKey.size());
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "key3");
 MultiKey<String> multiKey4 = new MultiKey<>("key2", "key1");

 assertEquals(multiKey1, multiKey1);
 assertEquals(multiKey1, multiKey2);
 assertNotEquals(multiKey1, multiKey3);
 assertNotEquals(multiKey1, multiKey4);
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");

 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 }

 @Test
 public void testMultiKeyToString() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

 assertEquals("MultiKey[key1, key2]", multiKey.toString());
 }
}