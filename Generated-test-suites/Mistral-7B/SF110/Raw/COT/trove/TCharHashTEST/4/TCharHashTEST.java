package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import org.junit.Test;

public class TCharHashTest {

 private TCharHash charHash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 charHash = new TCharHash(11);
 }

 @Test
 public void testConstructor() {
 TCharHash charHash = new TCharHash();
 assertNotNull(charHash);
 assertEquals(0, charHash.size());

 charHash = new TCharHash(5);
 assertNotNull(charHash);
 assertEquals(0, charHash.size());

 charHash = new TCharHash(5, 0.75f);
 assertNotNull(charHash);
 assertEquals(0, charHash.size());

 charHash = new TCharHash(5, 0.75f, 'N');
 assertNotNull(charHash);
 assertEquals(0, charHash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 int capacity = charHash.setUp(11);
 assertEquals(11, capacity);
 }

 @Test
 public void testContains() {
 charHash.put('A');
 assertTrue(charHash.contains('A'));

 charHash.put('B');
 assertTrue(charHash.contains('B'));

 assertTrue(charHash.contains('A'));
 assertFalse(charHash.contains('C'));
 }

 @Test
 public void testForEach() {
 int count = 0;
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');

 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 count++;
 return true;
 }
 };

 charHash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');

 int index = 1;
 charHash.removeAt(index);
 assertEquals(2, charHash.size());
 assertEquals('B', charHash.get(index));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtOutOfBounds() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');

 int index = 10;
 charHash.removeAt(index);
 }

 @Test
 public void testIndex() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');

 int index = charHash.index('B');
 assertEquals(-1, index);

 index = charHash.index('A');
 assertEquals(0, index);

 index = charHash.index('C');
 assertEquals(1, index);
 }

 @Test(expected = NoSuchElementException.class)
 public void testIndexNoSuchElement() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');

 int index = charHash.index('D');
 }

 @Test
 public void testInsertKey() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');

 int index = charHash.insertKey('D');
 assertEquals(4, charHash.size());
 assertEquals('D', charHash.get(index));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 }

}