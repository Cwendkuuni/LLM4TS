import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 @Before
 public void setUp() {
 TCharHash charHash = new TCharHash(5);
 }

 @Test
 public void testConstructor() {
 TCharHash charHash = new TCharHash();
 assertNotNull(charHash);
 assertEquals(0, charHash.size());

 charHash = new TCharHash(10);
 assertEquals(10, charHash.capacity());
 assertEquals(0, charHash.size());

 charHash = new TCharHash(10, 0.75f);
 assertEquals(10, charHash.capacity());
 assertEquals(0, charHash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 TCharHash charHash = new TCharHash();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());

 charHash = new TCharHash('\u0001');
 assertEquals('\u0001', charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TCharHash charHash = new TCharHash(5);
 assertEquals(5, charHash.capacity());
 assertNotNull(charHash._set);
 }

 @Test
 public void testContains() {
 TCharHash charHash = new TCharHash();
 assertFalse(charHash.contains('a'));

 charHash.put('a', 'a');
 assertTrue(charHash.contains('a'));
 }

 @Test
 public void testForEach() {
 TCharHash charHash = new TCharHash();
 char[] expected = new char[0];
 char[] result = new char[10];
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 result[charHash.size()] = charValue;
 return true;
 }
 };
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.forEach(procedure);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testRemoveAt() {
 TCharHash charHash = new TCharHash();
 charHash.put('a', 'a');
 char HashKey = 'a';
 int index = charHash.indexOf(HashKey);
 charHash.removeAt(index);
 assertEquals(-1, index);
 assertFalse(charHash.contains(HashKey));
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtWhenFull() {
 TCharHash charHash = new TCharHash(1);
 char HashKey = 'a';
 charHash.put('a', 'a');
 int index = charHash.indexOf(HashKey);
 charHash.removeAt(index);
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 TCharHash charHash = new TCharHash();
 assertNull(charHash.indexOf('a'));

 charHash.put('a', 'a');
 assertEquals(0, charHash.indexOf('a'));

 charHash.put('b', 'b');
 assertEquals(1, charHash.indexOf('b'));
 }

 @Test
 public void testIndexRehashed() {
 TCharHash charHash = new TCharHash();
 char HashKey = 'a';
 charHash.put('b', 'b');
 charHash.put('a', 'a');
 int index = charHash.indexOf(HashKey);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 TCharHash charHash = new TCharHash();
 char HashKey = 'a';
 char value = 'a';
 int index = charHash.insertKey(HashKey);
 assertEquals(0, index);
 assertTrue(charHash.contains(HashKey));
 }

 @Test
 public void testInsertKeyRehash() {
 TCharHash charHash = new TCharHash(1);
 char HashKey = 'a';
 char value = 'a';
 int index = charHash.insertKey(HashKey);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyWithExistingValue() {
 TCharHash charHash = new TCharHash();
 char HashKey = 'a';
 char existingValue = 'a';
 char newValue = 'b';
 charHash.put(HashKey, existingValue);
 int index = charHash.insertKey(HashKey);
 assertEquals(-1, index);
 assertEquals(existingValue, charHash.get(HashKey));
 }

 @Test
 public void testInsertKeyAt() {
 TCharHash charHash = new TCharHash();
 char HashKey = 'a';
 char value = 'a';
 int index = charHash.insertKey(HashKey);
 assertEquals(0, index);
 assertTrue(charHash.contains(HashKey));

 charHash = new TCharHash(1);
 HashKey = 'a';
 value = 'a';
 index = charHash.insertKeyAt(0, HashKey);
 assertEquals(-1, index);
 charHash.put(HashKey, value);
 assertTrue(charHash.contains(HashKey));
 }
}