import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstructorWithTwoKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 String[] expectedKeys = new String[]{"key1", "key2"};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testMultiKeyConstructorWithThreeKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
 String[] expectedKeys = new String[]{"key1", "key2", "key3"};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testMultiKeyConstructorWithFourKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
 String[] expectedKeys = new String[]{"key1", "key2", "key3", "key4"};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testMultiKeyConstructorWithArrayOfKeys() {
 MultiKey<String> multiKey = new MultiKey(new String[]{"key1", "key2"});
 String[] expectedKeys = new String[]{"key1", "key2"};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "key3");
 assertTrue(multiKey1.equals(multiKey2));
 assertFalse(multiKey1.equals(multiKey3));
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "key3");
 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 assertNotSame(multiKey1.hashCode(), multiKey3.hashCode());
 }

 @Test
 public void testMultiKeyToString() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 assertEquals("MultiKey[key1, key2]", multiKey.toString());
 }
}