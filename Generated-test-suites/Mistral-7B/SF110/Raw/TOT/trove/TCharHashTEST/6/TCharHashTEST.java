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
 public void testDefaultConstructor() {
 char defaultValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
 assertArrayEquals(new char[5], charHash._set);
 assertEquals(defaultValue, charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 charHash = new TCharHash(15, 0.75f);
 assertEquals(15, charHash.size());
 assertEquals(0.75f, charHash.loadFactor(), 0.01f);
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 charHash = new TCharHash(20, 0.8f, 'X');
 assertEquals(20, charHash.size());
 assertEquals(0.8f, charHash.loadFactor(), 0.01f);
 assertArrayEquals(new char[20], charHash._set);
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 charHash.put('A');
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testContainsWithEmptyHash() {
 assertFalse(charHash.contains('B'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsWithNullValue() {
 charHash.put('C');
 assertFalse(charHash.contains(null));
 }

 @Test
 public void testForEach() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals('A', charValue);
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWithEmptyHash() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = 1;
 char removedValue = charHash.remove(index);
 assertEquals('B', removedValue);
 assertEquals(2, charHash.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithNegativeIndex() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = -1;
 char removedValue = charHash.remove(index);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithIndexGreaterThanSize() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = 5;
 char removedValue = charHash.remove(index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAtWithNullIndex() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 Object index = null;
 char removedValue = charHash.remove(index);
 }

 @Test
 public void testIndex() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertEquals(1, charHash.index('B'));
 assertEquals(-1, charHash.index('D'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexWithNullValue() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertEquals(-1, charHash.index(null));
 }

 @Test
 public void testInsertKey() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 char newValue = 'D';
 int index = charHash.insertKey(newValue);
 assertEquals(4, charHash.size());
 assertEquals('D', charHash.get(index));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNullValue() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 char newValue = null;
 int index = charHash.insertKey(newValue);
 }

 @Test
 public void testInsertKeyWithFullHash() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 char newValue = 'F';
 int index = charHash.insertKey(newValue);
 }
}