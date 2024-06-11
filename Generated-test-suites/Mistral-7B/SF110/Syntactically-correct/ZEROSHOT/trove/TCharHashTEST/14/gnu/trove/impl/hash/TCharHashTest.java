import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setup() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 TCharHash charHash = new TCharHash();
 assertNotNull(charHash);
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TCharHash charHash = new TCharHash(5);
 assertNotNull(charHash);
 assertEquals(5, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TCharHash charHash = new TCharHash(5, 0.75f);
 assertNotNull(charHash);
 assertEquals(5, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 char entryValue = 'E';
 TCharHash charHash = new TCharHash(5, 0.75f, entryValue);
 assertNotNull(charHash);
 assertEquals(5, charHash.size());
 assertEquals(entryValue, charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash.put('A', 'A');
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 charHash.put('A', 'A');
 int capacity = charHash.capacity();
 char[] set = charHash._set;
 assertNotNull(set);
 assertEquals(capacity, set.length);
 Arrays.fill(set, Constants.DEFAULT_CHAR_NO_ENTRY_VALUE);
 }

 @Test
 public void testContains() {
 charHash.put('A', 'A');
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testDoesNotContain() {
 charHash.put('A', 'A');
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testForEachProcedure() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 assertEquals('A', character);
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 charHash.put('A', 'A');
 charHash.removeAt(0);
 }

 @Test
 public void testIndex() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 int indexA = charHash.index('A');
 int indexB = charHash.index('B');
 assertEquals(indexA, 0);
 assertEquals(indexB, 1);
 }

 @Test
 public void testIndexRehashed() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 int indexA = charHash.indexRehashed('A', 1, 1, (byte) 1);
 int indexB = charHash.indexRehashed('B', 1, 1, (byte) 1);
 int indexC = charHash.indexRehashed('C', 1, 1, (byte) 1);
 assertEquals(indexA, -1);
 assertEquals(indexB, 1);
 assertEquals(indexC, 2);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 int indexA = charHash.insertKey('A');
 int indexB = charHash.insertKey('B');
 assertEquals(indexA, 0);
 assertEquals(indexB, 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHashTable() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.put('E', 'E');
 charHash.put('F', 'F');
 charHash.put('G', 'G');
 charHash.put('H', 'H');
 charHash.put('I', 'I');
 charHash.put('J', 'J');
 charHash.put('K', 'K');
 charHash.put('L', 'L');
 charHash.put('M', 'M');
 charHash.put('N', 'N');
 charHash.put('O', 'O');
 charHash.put('P', 'P');
 charHash.put('Q', 'Q');
 charHash.put('R', 'R');
 charHash.put('S', 'S');
 charHash.put('T', 'T');
 charHash.put('U', 'U');
 charHash.put('V', 'V');
 charHash.put('W', 'W');
 charHash.put('X', 'X');
 charHash.put('Y', 'Y');
 charHash.put('Z', 'Z');
 }

}