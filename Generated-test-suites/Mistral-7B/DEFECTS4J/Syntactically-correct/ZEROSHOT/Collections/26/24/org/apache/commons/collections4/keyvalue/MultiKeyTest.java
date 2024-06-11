import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstruction() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key2", "locale2");
 MultiKey<String> multiKey3 = new MultiKey<>("key3", "locale3", "locale4");
 MultiKey<String> multiKey4 = new MultiKey<>(new String[]{"key5", "locale5", "locale6"});

 assertEquals(2, multiKey1.size());
 assertEquals(2, multiKey2.size());
 assertEquals(4, multiKey3.size());
 assertEquals(3, multiKey4.size());
 }

 @Test
 public void testGetKeys() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key2", "locale2");
 MultiKey<String> multiKey3 = new MultiKey<>("key3", "locale3", "locale4");
 MultiKey<String> multiKey4 = new MultiKey<>(new String[]{"key5", "locale5", "locale6"});

 K[] keys1 = multiKey1.getKeys();
 K[] keys2 = multiKey2.getKeys();
 K[] keys3 = multiKey3.getKeys();
 K[] keys4 = multiKey4.getKeys();

 assertEquals(2, keys1.length);
 assertEquals("locale1", keys1[1].toString());
 assertEquals("key1", keys1[0].toString());

 assertEquals(2, keys2.length);
 assertEquals("locale2", keys2[1].toString());
 assertEquals("key2", keys2[0].toString());

 assertEquals(4, keys3.length);
 assertEquals("locale4", keys3[2].toString());
 assertEquals("locale3", keys3[1].toString());
 assertEquals("key3", keys3[0].toString());

 assertEquals(3, keys4.length);
 assertEquals("locale6", keys4[2].toString());
 assertEquals("locale5", keys4[1].toString());
 assertEquals("key5", keys4[0].toString());
 }

 @Test
 public void testEquals() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key2", "locale2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey4 = new MultiKey<>("key1", "locale2");

 assertTrue(multiKey1.equals(multiKey3));
 assertFalse(multiKey1.equals(multiKey2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 MultiKey<String> multiKey = new MultiKey(null);
 }
}