import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TCharHashTest {

 private TCharHash charHash;
 private char key;

 @Before
 public void setUp() {
 charHash = new TCharHash(11);
 key = 'a';
 }

 @Test
 public void testEmptyHash() {
 assertTrue(charHash.isEmpty());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testSetNoEntryValue() {
 charHash = new TCharHash(11, 'x');
 assertNotNull(charHash.getNoEntryValue());
 assertEquals('x', charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacity() {
 charHash = new TCharHash(13);
 assertEquals(13, charHash.size());
 }

 @Test
 public void testLoadFactor() {
 charHash = new TCharHash(11, 0.5f);
 assertEquals(22, charHash.initialCapacity());
 }

 @Test
 public void testContains() {
 charHash.put(key);
 assertTrue(charHash.contains(key));
 }

 @Test
 public void testDoesNotContain() {
 assertFalse(charHash.contains('b'));
 }

 @Test
 public void testPutAndGet() {
 charHash.put(key, 1);
 assertEquals(Character.valueOf(key), charHash.get(key));
 }

 @Test
 public void testPutNullKey() {
 assertNull(charHash.put(null));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 charHash.put(key, null);
 }

 @Test
 public void testPutAndRemove() {
 charHash.put(key, 1);
 charHash.remove(key);
 assertFalse(charHash.contains(key));
 }

 @Test
 public void testPutAndContainsAll() {
 List<Character> keyList = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd'));
 charHash.putAll(keyList.stream().map(k -> new SimpleEntry<Character, Integer>(k, 1)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue).entrySet().stream().collect(Collectors.toList())));
 for (Character key : keyList) {
 assertTrue(charHash.contains(key));
 }
 }

 @Test
 public void testPutAndKeySet() {
 List<Character> keyList = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd'));
 charHash.keySet().addAll(keyList);
 for (Character key : keyList) {
 assertTrue(charHash.contains(key));
 }
 }

 @Test
 public void testPutAndValues() {
 List<Integer> valueList = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
 charHash.putAll(keyList.stream().map(k -> new SimpleEntry<Character, Integer>(k, valueList.get(kList.indexOf(k)))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue).entrySet().stream().collect(Collectors.toList())));
 for (int value : valueList) {
 assertEquals(Integer.valueOf(value), charHash.getValue(key));
 }
 }

 @Test
 public void testClear() {
 charHash.clear();
 assertTrue(charHash.isEmpty());
 }

 @Test
 public void testSize() {
 charHash.put(key, 1);
 charHash.put('b', 2);
 assertEquals(2, charHash.size());
 }

 @Test
 public void testIsEmpty() {
 charHash.clear();
 assertTrue(charHash.isEmpty());
 }

}