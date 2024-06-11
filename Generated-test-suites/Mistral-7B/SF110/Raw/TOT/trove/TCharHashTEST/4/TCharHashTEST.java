import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testInitialization() {
 // Test empty charHash
 assertNotNull(charHash);
 assertEquals(5, charHash.size());
 assertNull(charHash.getNoEntryValue());

 // Test charHash with no_entry_value
 charHash = new TCharHash(5, '\u0001');
 assertNotNull(charHash.getNoEntryValue());
 assertEquals('\u0001', charHash.getNoEntryValue());

 // Test charHash with initialCapacity and loadFactor
 charHash = new TCharHash(10, 0.75f);
 assertEquals(10, charHash.capacity());
 assertEquals(7, charHash.loadFactor());
 }

 @Test
 public void testSetUp() {
 // Test _set initialization
 charHash = new TCharHash(5);
 assertNotNull(charHash._set);
 assertEquals(5, charHash._set.length);

 // Test _set fill with no_entry_value
 charHash = new TCharHash(5);
 if (charHash.no_entry_value != '\0') {
 for (char c : charHash._set) {
 assertEquals(charHash.no_entry_value, c);
 }
 }
 }

 @Test
 public void testContains() {
 // Test empty charHash
 assertFalse(charHash.contains('a'));

 // Test charHash with added elements
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));

 // Test charHash with removed element
 charHash.remove('b');
 assertFalse(charHash.contains('b'));
 }

 @Test
 public void testForEach() {
 // Test empty charHash
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertFalse(true);
 return false;
 }
 };
 charHash = new TCharHash();
 charHash.forEach(procedure);

 // Test charHash with added elements
 charHash = new TCharHash(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals(charValue, charHash.get(charValue));
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 // Test empty charHash
 charHash = new TCharHash();
 assertFalse(charHash.containsKey('a'));
 assertFalse(charHash.removeKey('a') > -1);

 // Test charHash with added elements
 charHash = new TCharHash(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 int index = charHash.indexOf('b');
 assertEquals(2, index);
 charHash.removeAt(index);
 assertFalse(charHash.containsKey('b'));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehash_NoFreeOrRemovedSlotsAvailable() {
 // Test charHash with maximum size
 charHash = new TCharHash(5);
 for (char c = 'a'; c <= 'z'; c++) {
 charHash.put(c);
 }
 }

 @Test
 public void testInsertKeyRehash() {
 // Test charHash with added elements
 charHash = new TCharHash(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');

 // Test inserting 'd'
 assertFalse(charHash.containsKey('d'));
 charHash.put('d');
 assertEquals(6, charHash.size());
 assertEquals('d', charHash.get('d'));
 assertEquals(2, charHash.indexOf('d'));

 // Test inserting 'e'
 assertFalse(charHash.containsKey('e'));
 charHash.put('e');
 assertEquals(7, charHash.size());
 assertEquals('e', charHash.get('e'));
 assertEquals(-1, charHash.indexOf('a')); // Index 'a' should be rehashed
 }

 @Test
 public void testGetNoEntryValue() {
 // Test empty charHash
 assertNull(charHash.getNoEntryValue());

 // Test charHash with no_entry_value
 charHash = new TCharHash(5, '\u0001');
 assertEquals('\u0001', charHash.getNoEntryValue());

 // Test charHash without no_entry_value
 charHash = new TCharHash(5);
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testHashFunctions() {
 // Test hash function for non-negative integers
 assertEquals(1, HashFunctions.hash((short) 1));
 assertEquals(1, HashFunctions.hash((byte) 1));
 assertEquals(1, HashFunctions.hash(0));
 assertEquals(1, HashFunctions.hash(1));
 assertEquals(1, HashFunctions.hash(127));
 assertEquals(1, HashFunctions.hash(128));
 assertEquals(1, HashFunctions.hash(Integer.MAX_VALUE));

 // Test hash function for negative integers
 assertEquals(-1, HashFunctions.hash((short) -1));
 assertEquals(-1, HashFunctions.hash((byte) -1));
 assertEquals(-1, HashFunctions.hash(-1));
 assertEquals(-1, HashFunctions.hash(-128));
 assertEquals(-1, HashFunctions.hash(-129));
 assertEquals(-1, HashFunctions.hash(-Integer.MAX_VALUE));
 }
}