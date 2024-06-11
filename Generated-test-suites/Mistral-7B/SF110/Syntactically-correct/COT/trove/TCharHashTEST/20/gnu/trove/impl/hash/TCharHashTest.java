import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 }

 // Test constructor with no arguments
 @Test
 public void testDefaultConstructor() {
 charHash = new TCharHash();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 assertTrue(charHash._set.length > 0);
 }

 // Test constructor with initialCapacity
 @Test
 public void testConstructorWithInitialCapacity() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.size());
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 assertTrue(charHash._set.length > 10);
 }

 // Test constructor with initialCapacity and loadFactor
 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 charHash = new TCharHash(15, 0.75f);
 assertEquals(15, charHash.capacity());
 assertEquals(11, charHash.size()); // initial 4 empty slots + 7 filled
 assertEquals(0.75f, charHash.loadFactor(), 0.01f);
 }

 // Test constructor with initialCapacity, loadFactor, and noEntryValue
 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 charHash = new TCharHash(15, 0.75f, 'Z');
 assertEquals('Z', charHash.getNoEntryValue());
 assertEquals(15, charHash.capacity());
 assertEquals(11, charHash.size()); // initial 4 empty slots + 7 filled
 Arrays.fill(charHash._set, 'Z');
 }

 // Test getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash(5);
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 charHash = new TCharHash(5, 'X');
 assertEquals('X', charHash.getNoEntryValue());
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 charHash = new TCharHash(3);
 assertEquals(3, charHash.capacity());
 assertNotNull(charHash._set);
 assertEquals(3, charHash._set.length);
 }

 // Test contains method
 @Test
 public void testContains() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);

 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 assertTrue(charHash.contains('D'));

 assertFalse(charHash.contains('E'));
 }

 // Test forEach method
 @Test
 public void testForEach() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);

 final int count = 0;
 charHash.forEach(new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 ++count;
 return true;
 }
 });
 assertEquals(4, count);
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);

 charHash.removeAt(1);
 assertEquals(3, charHash.size());
 assertFalse(charHash.contains('B'));
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('C'));
 assertTrue(charHash.contains('D'));
 }

 // Test index method
 @Test
 public void testIndex() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
charHash.put('E', 5);

 assertEquals(2, charHash.index('B'));
 assertEquals(-1, charHash.index('F'));
 }

 // Test indexRehashed method
 @Test
 public void testIndexRehashed() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
charHash.put('E', 5);
charHash.put('F', 6);
charHash.put('G', 7);

 int index = charHash.indexRehashed('B', 2, 2, (byte) 1);
 assertEquals(2, index);
 }

 // Test insertKey method
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHashTable() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 charHash.put('F', 6);
 charHash.put('G', 7);
 charHash.put('H', 8);
 charHash.put('I', 9);
 charHash.put('J', 10);
 charHash.put('K', 11);
 charHash.put('L', 12);
 charHash.put('M', 13);
 charHash.put('N', 14);
 charHash.put('O', 15);
 charHash.put('P', 16);
 charHash.put('Q', 17);
 charHash.put('R', 18);
 charHash.put('S', 19);
 charHash.put('T', 20);
 charHash.put('U', 21);
 charHash.put('V', 22);
 charHash.put('W', 23);
 charHash.put('X', 24);
 charHash.put('Y', 25);
 charHash.put('Z', 26);
 }

 @Test
 public void testInsertKeyWithEmptyHashTable() {
 charHash = new TCharHash(5);
 charHash.put('A');
 assertEquals(1, charHash.size());
 assertEquals('A', charHash.get(0));
 }

 @Test
 public void testInsertKeyWithExistingKey() {
 charHash = new TCharHash(5);
 charHash.put('A', 1);
 charHash.put('A', 2); // duplicate key
 assertEquals(1, charHash.size());
 assertEquals('A', charHash.get('A'));
 assertEquals(1, charHash.get(0));
 }

 @Test
 public void testInsertKeyWithExistingKeyAndReplace() {
 charHash = new TCharHash(5);
 charHash.put('A', 1);
 charHash.put('A', 2); // duplicate key
 charHash.put('A', 3); // replace existing key
 assertEquals(1, charHash.size());
 assertEquals('A', charHash.get('A'));
 assertEquals(3, charHash.get(0));
 }

 @Test
 public void testInsertKeyRehash() {
 charHash = new TCharHash(5);
 charHash.put('A'); // fill first slot
 charHash.put('B'); // fill second slot
 charHash.put('C'); // fill third slot
 charHash.put('D'); // fill fourth slot
 charHash.put('E'); // trigger rehash
 charHash.put('F'); // new slot
 assertEquals(6, charHash.size());
 assertEquals('F', charHash.get(5));
 }

 // Test insertKeyAt method
 @Test
 public void testInsertKeyAt() {
 charHash = new TCharHash(5);
 charHash.put('A'); // fill first slot
 charHash.put('B'); // fill second slot
 charHash.put('C'); // fill third slot
 charHash.put('D'); // fill fourth slot
 charHash.put('E'); // trigger rehash
 charHash.put('F', 5); // insert at index 5
 assertEquals(6, charHash.size());
 assertEquals('F', charHash.get(5));
 }
}