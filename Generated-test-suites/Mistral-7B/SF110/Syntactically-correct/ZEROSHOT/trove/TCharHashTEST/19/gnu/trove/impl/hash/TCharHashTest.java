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
 assertNotNull(charHash._set);
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 assertFalse(charHash.contains('a'));
 charHash.put('a', 'a');
 assertTrue(charHash.contains('a'));
 }

 @Test
 public void testForEach() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals('a', charValue);
 return true;
 }
 };
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 charHash.remove('a');
 }

 @Test
 public void testIndex() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 assertEquals(0, charHash.index('a'));
 assertEquals(1, charHash.index('b'));
 assertEquals(-1, charHash.index('d'));
 }

 @Test
 public void testInsertKey() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.put('d', 'd');
 assertEquals(4, charHash.size());
 assertEquals(4, charHash.capacity());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 assertTrue(charHash.contains('d'));
 }

 @Test
 public void testInsertKeyRehash() {
 charHash = new TCharHash(3);
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.put('d', 'd');
 charHash.put('e', 'e');
 charHash.put('f', 'f');
 assertEquals(7, charHash.size());
 assertEquals(13, charHash.capacity());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 assertTrue(charHash.contains('d'));
 assertTrue(charHash.contains('e'));
 assertTrue(charHash.contains('f'));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 charHash = new TCharHash(3);
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.put('d', 'd');
 charHash.put('e', 'e');
 charHash.put('f', 'f');
 charHash.put('g', 'g');
 }
}