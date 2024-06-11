import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash<Character> charHash;

 @Test
 public void testEmptyConstructor() {
 charHash = new TCharHash<>();
 assertEquals(0, charHash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 charHash = new TCharHash(10, 0.75f);
 assertEquals(10, charHash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 charHash = new TCharHash(10, 0.75f, 'X');
 assertEquals(10, charHash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash<>();
 char noEntryValue = charHash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(5);
 charHash.setUp(7);
 assertEquals(7, charHash.size());
 }

 @Test
 public void testContains() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');

 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 assertTrue(charHash.contains('D'));
 assertTrue(charHash.contains('E'));

 assertFalse(charHash.contains('F'));
 }

 @Test
 public void testForEach() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');

 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals('A', charValue);
 return true;
 }
 };

 charHash.forEach(procedure);
 charHash.forEach(procedure); // test for idempotency
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 charHash = new TCharHash(-5);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 charHash = new TCharHash(5, -0.75f);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNoEntryValue() {
 charHash = new TCharHash(5, 0.75f, null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testRemoveAt() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');

 int indexToRemove = 3;
 char removedValue = charHash.removeAt(indexToRemove);
 assertEquals('D', removedValue);
 assertEquals(4, charHash.size());
 }

 @Test
 public void testIndex() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');

 int indexOfA = charHash.index('A');
 int indexOfF = charHash.index('F');

 assertEquals(0, indexOfA);
 assertEquals(-1, indexOfF);
 }

 @Test
 public void testIndexRehashed() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 charHash.put('G');
 charHash.put('H');
 charHash.put('I');
 charHash.put('J');

 int indexOfK = charHash.index('K');
 assertEquals(-1, indexOfK);
 }

 @Test
 public void testInsertKey() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');

 int indexToInsert = 3;
 charHash.put('G');
 assertEquals('G', charHash.get(indexToInsert));
 }

 @Test
 public void testInsertKeyRehash() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 charHash.put('G');
 charHash.put('H');
 charHash.put('I');
 charHash.put('J');
 charHash.put('K');

 int indexToInsert = 11;
 charHash.put('M');
 assertEquals('M', charHash.get(indexToInsert));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWhenFull() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 charHash.put('G');
 charHash.put('H');
 charHash.put('I');
 charHash.put('J');
 charHash.put('K');
 charHash.put('L');

 throw new IllegalStateException("No free or removed slots available. Key set full?!!");
 }
}