package tests;

import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testTCharHashConstructor() {
 TCharHash charHash = new TCharHash();
 assertEquals(0, charHash.size());
 CharHash emptyCharHash = new TCharHash(0);
 assertEquals(0, emptyCharHash.size());
 TCharHash charHashWithInitialCapacity = new TCharHash(5);
 assertEquals(0, charHashWithInitialCapacity.size());
 TCharHash charHashWithInitialCapacityAndLoadFactor = new TCharHash(5, 0.75f);
 assertEquals(0, charHashWithInitialCapacityAndLoadFactor.size());
 TCharHash charHashWithInitialCapacityLoadFactorAndNoEntryValue = new TCharHash(5, 0.75f, 'X');
 assertEquals(0, charHashWithInitialCapacityLoadFactorAndNoEntryValue.size());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash.no_entry_value = 'A';
 assertEquals('A', charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 int capacity = charHash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 charHash.put('A');
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testContainsWithEmptyHash() {
 assertFalse(charHash.contains('X'));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsWithSetOperationThrown() {
 charHash.put('A');
 try {
 charHash.put('A');
 } catch (UnsupportedOperationException e) {
 assertEquals("Hash table is full", e.getMessage());
 throw e;
 }
 assertFalse(charHash.contains('A'));
 }

 @Test
 public void testForEach() {
 charHash.put('A');
 charHash.put('B');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 if (character == 'A') {
 return false;
 }
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test
 public void testForEachWithEmptyHash() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A');
 charHash.put('B');
 int index = charHash.index('A');
 charHash.removeAt(index);
 assertFalse(charHash.contains('A'));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithNegativeIndex() {
 charHash.put('A');
 charHash.put('B');
 charHash.removeAt(-1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAtWithSetOperationThrown() {
 charHash.put('A');
 charHash.put('B');
 charHash.removeAt(0);
 try {
 charHash.removeAt(0);
 } catch (UnsupportedOperationException e) {
 assertEquals("Hash table is empty", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testIndex() {
 charHash.put('A');
 charHash.put('B');
 int index = charHash.index('A');
 assertEquals(0, index);
 index = charHash.index('B');
 assertEquals(1, index);
 }

 @Test
 public void testIndexWithEmptyHash() {
 int index = charHash.index('X');
 assertEquals(-1, index);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIndexWithSetOperationThrown() {
 charHash.put('A');
 charHash.put('B');
 try {
 int index = charHash.index('C');
 } catch (UnsupportedOperationException e) {
 assertEquals("Hash table is full", e.getMessage());
 throw e;
 }
 assertEquals(-1, index);
 }

 @Test
 public void testIndexRehashed() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 int index = charHash.index('A');
 assertEquals(0, index);
 index = charHash.index('D');
 assertEquals(3, index);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIndexRehashedWithSetOperationThrown() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 try {
 int index = charHash.index('E');
 } catch (UnsupportedOperationException e) {
 assertEquals("Hash table is full", e.getMessage());
 throw e;
 }
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A');
 charHash.put('B');
 int index = charHash.insertKey('C');
 assertEquals(3, index);
 charHash.put('D');
 int index2 = charHash.insertKey('C');
 assertEquals(-1, index2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testInsertKeyWithHashTableFull() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 charHash.put('G');
 try {
 charHash.insertKey('H');
 } catch (UnsupportedOperationException e) {
 assertEquals("Hash table is full", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testInsertKeyRehash() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 charHash.put('G');
 charHash.put('H');
 int index = charHash.insertKey('I');
 assertEquals(8, index);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testInsertKeyRehashWithHashTableFull() {
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
 charHash.put('M');
 charHash.put('N');
 charHash.put('O');
 charHash.put('P');
 charHash.put('Q');
 try {
 charHash.insertKey('R');
 } catch (UnsupportedOperationException e) {
 assertEquals("Hash table is full", e.getMessage());
 throw e;
 }
 }
}