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
 assertEquals(5, charHash.capacity());
 assertNotNull(charHash._set);
 assertNotNull(charHash._states);
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testInitializationWithInitialCapacityAndLoadFactor() {
 charHash = new TCharHash(5, 0.75f);
 assertEquals(5, charHash.capacity());
 assertEquals(0.75f, charHash.loadFactor(), 0.01f);
 }

 @Test
 public void testInitializationWithInitialCapacityLoadFactorAndNoEntryValue() {
 charHash = new TCharHash(5, 0.75f, 'X');
 assertEquals(5, charHash.capacity());
 assertEquals(0.75f, charHash.loadFactor(), 0.01f);
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash();
 charHash.setNoEntryValue('Z');
 assertEquals('Z', charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 int capacity = charHash.setUp(10);
 assertEquals(10, capacity);
 assertArrayEquals(new char[10], charHash._set);
 }

 @Test
 public void testContains() {
 charHash.put('A', 1);
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testContainsNullValue() {
 charHash.put('A', 1);
 assertFalse(charHash.contains(null));
 }

 @Test
 public void testContainsNonExistentValue() {
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testForEach() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char value) {
 assertEquals(value, charHash.get(value));
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWithConcurrentModification() {
 charHash.put('A', 1);
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char value) {
 charHash.remove('A');
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A', 1);
 int index = charHash.indexOf('A');
 charHash.removeAt(index);
 assertFalse(charHash.contains('A'));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtNegativeIndex() {
 charHash.put('A', 1);
 charHash.removeAt(-1);
 }

 @Test
 public void testRemoveAtIndexOutOfBounds() {
 charHash.put('A', 1);
 int index = charHash.capacity();
 charHash.removeAt(index);
 }

 @Test
 public void testIndex() {
 charHash.put('A', 1);
 int index = charHash.indexOf('A');
 assertEquals(index, charHash.indexOf('A'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexNegativeValue() {
 charHash.put('A', 1);
 charHash.indexOf((char) Integer.MIN_VALUE);
 }

 @Test
 public void testIndexNonExistentValue() {
 assertNull(charHash.indexOf('B'));
 }

 @Test
 public void testIndexRehashed() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 int index = charHash.indexOf('D');
 assertEquals(index, charHash.indexOf('D'));
 }

 @Test
 public void testInsertKey() {
 charHash.put('A', 1);
 char Hash = 'B';
 int index = charHash.insertKey(Hash);
 assertEquals(Hash, charHash.get(Hash));
 assertEquals(1, charHash.size());
 assertEquals(index, charHash.indexOf(Hash));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullCharHash() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 char Hash = 'F';
 int index = charHash.insertKey(Hash);
 fail("This test should throw an IllegalStateException");
 }
}