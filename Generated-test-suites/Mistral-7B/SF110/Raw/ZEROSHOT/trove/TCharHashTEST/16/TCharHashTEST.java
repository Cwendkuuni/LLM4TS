package test;

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
 public void testEmptyTCharHash() {
 assertEquals(0, charHash.size());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(5);
 charHash.setUp(5);
 assertEquals(5, charHash.capacity());
 assertArrayEquals(new char[5], charHash._set);
 }

 @Test
 public void testNoEntryValue() {
 charHash = new TCharHash();
 char defaultNoEntryValue = charHash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultNoEntryValue);
 }

 @Test
 public void testContains() {
 charHash.put('a', 'a');
 assertTrue(charHash.contains('a'));
 }

 @Test
 public void testDoesNotContain() {
 charHash.put('a', 'a');
 assertFalse(charHash.contains('b'));
 }

 @Test
 public void testForEach() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');

 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 assertEquals('a', character);
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.removeAt(0);
 }

 @Test
 public void testIndex() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');

 int indexA = charHash.index('a');
 int indexB = charHash.index('b');
 int indexC = charHash.index('c');

 assertEquals(0, indexA);
 assertEquals(1, indexB);
 assertEquals(2, indexC);
 }

 @Test
 public void testIndexDoesNotExist() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');

 int indexD = charHash.index('d');
 assertEquals(-1, indexD);
 }

 @Test
 public void testInsertKey() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');

 char newKey = 'd';
 int index = charHash.insertKey(newKey);
 assertEquals(4, charHash.size());
 assertEquals(3, index);
 assertEquals('d', charHash._set[index]);
 }

 @Test
 public void testInsertKeyRehashes() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.put('d', 'd');
 charHash.put('e', 'e');
 charHash.put('f', 'f');
 charHash.put('g', 'g');

 char newKey = 'h';
 int index = charHash.insertKey(newKey);
 assertEquals(16, charHash.capacity());
 assertEquals(14, index);
 assertEquals('h', charHash._set[index]);
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.put('d', 'd');
 charHash.put('e', 'e');
 charHash.put('f', 'f');
 charHash.put('g', 'g');
 charHash.put('h', 'h');
 charHash.put('i', 'i');
 charHash.put('j', 'j');
 charHash.put('k', 'k');
 charHash.put('l', 'l');
 charHash.put('m', 'm');
 charHash.put('n', 'n');
 charHash.put('o', 'o');
 charHash.put('p', 'p');
 charHash.put('q', 'q');
 charHash.put('r', 'r');
 charHash.put('s', 's');
 charHash.put('t', 't');
 charHash.put('u', 'u');
 charHash.put('v', 'v');
 charHash.put('w', 'w');
 charHash.put('x', 'x');
 charHash.put('y', 'y');
 charHash.put('z', 'z');
 }
}