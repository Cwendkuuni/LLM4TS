import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.keyvalue.MultiKeyTest; // Assuming this is a utility class for test data
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 // Test constructor with two keys
 @Test
 public void testMultiKeyConstructorTwoKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 assertArrayEquals(new String[]{"key1", "key2"}, multiKey.getKeys());
 }

 // Test constructor with three keys
 @Test
 public void testMultiKeyConstructorThreeKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
 assertArrayEquals(new String[]{"key1", "key2", "key3"}, multiKey.getKeys());
 }

 // Test constructor with four keys
 @Test
 public void testMultiKeyConstructorFourKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
 assertArrayEquals(new String[]{"key1", "key2", "key3", "key4"}, multiKey.getKeys());
 }

 // Test getKeys() method
 @Test
 public void testGetKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 assertArrayEquals(new String[]{"key1", "key2"}, multiKey.getKeys());
 }

 // Test getKey(int index) method
 @Test
 public void testGetKey() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 assertEquals("key1", multiKey.getKey(0));
 assertEquals("key2", multiKey.getKey(1));
 }

 // Test size() method
 @Test
 public void testSize() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 assertEquals(2, multiKey.size());
 }

 // Test equals() method with identical MultiKey instance
 @Test
 public void testEqualsIdenticalMultiKey() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
 assertTrue(multiKey1.equals(multiKey2));
 }

 // Test equals() method with different number of keys
 @Test(expected = AssertionError.class)
 public void testEqualsDifferentNumberOfKeys() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String, Integer> multiKey2 = new MultiKey<>("key1");
 assertFalse(multiKey1.equals(multiKey2));
 }

 // Test equals() method with different keys
 @Test(expected = AssertionError.class)
 public void testEqualsDifferentKeys() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key2", "key1");
 assertFalse(multiKey1.equals(multiKey2));
 }

 // Test equals() method with null
 @Test(expected = AssertionError.class)
 public void testEqualsNull() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 assertFalse(multiKey.equals(null));
 }

 // Test hashCode() method with identical MultiKey instance
 @Test
 public void testHashCodeIdenticalMultiKey() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 }

 // Test hashCode() method with different keys
 @Test
 public void testHashCodeDifferentKeys() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key3", "key4");
 assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
 }

 // Test toString() method
 @Test
 public void testToString() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 assertEquals("MultiKey[key1, key2]", multiKey.toString());
 }
}