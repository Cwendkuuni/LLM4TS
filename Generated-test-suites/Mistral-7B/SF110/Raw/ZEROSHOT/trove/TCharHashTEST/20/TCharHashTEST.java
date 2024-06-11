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
 public void testConstructor() {
 assertEquals(5, charHash.capacity());
 assertNull(charHash.getNoEntryValue());
 assertArrayEquals(new char[5], charHash._set);
 }

 @Test
 public void testGetNoEntryValue() {
 char initialNoEntryValue = charHash.getNoEntryValue();
 charHash.setNoEntryValue('X');
 assertEquals('X', charHash.getNoEntryValue());
 charHash.setNoEntryValue(initialNoEntryValue);
 }

 @Test
 public void testContains() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertFalse(charHash.contains('C'));
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

 assertTrue(charHash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);

 int indexToRemove = 1;
 charHash.removeAt(indexToRemove);
 assertFalse(charHash.containsKey('B'));
 assertEquals(2, charHash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOutOfBoundsExceptionIndexOutOfBoundsTest() {
 int indexToRemove = charHash.capacity();
 charHash.removeAt(indexToRemove);
 }

 @Test
 public void testIndex() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);

 int indexOfA = charHash.index('A');
 int indexOfC = charHash.index('C');
 int indexOfNonExistent = charHash.index('D');

 assertEquals(0, indexOfA);
 assertEquals(2, indexOfC);
 assertEquals(-1, indexOfNonExistent);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);

 assertEquals(5, charHash.size());
 assertEquals(4, charHash.capacity());

 assertEquals('A', charHash.get('A'));
 assertEquals('B', charHash.get('B'));
 assertEquals('C', charHash.get('C'));
 assertEquals('D', charHash.get('D'));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailableTest() {
 for (int i = 0; i < charHash.capacity(); i++) {
 charHash.put((char) i, i);
 }
 }
}