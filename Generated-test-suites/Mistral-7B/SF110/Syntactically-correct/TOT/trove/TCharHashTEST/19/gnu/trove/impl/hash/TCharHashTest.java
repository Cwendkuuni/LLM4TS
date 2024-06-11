import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Test
 public void testEmptyTCharHash() {
 charHash = new TCharHash(10);
 assertTrue(charHash.isEmpty());
 assertEquals(0, charHash.size());
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(5);
 assertEquals(5, charHash.capacity());
 charHash.setUp(10);
 assertEquals(10, charHash.capacity());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testContainsChar() {
 charHash = new TCharHash(5);
 charHash.put('a', 1);
 charHash.put('b', 2);
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertFalse(charHash.contains('c'));
 }

 @Test
 public void testForEachProcedure() {
 charHash = new TCharHash(5);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 if (character == 'a') {
 fail("'a' should not be present in the hash");
 }
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 charHash = new TCharHash(-5);
 }

 @Test
 public void testFullLoadFactor() {
 charHash = new TCharHash(5, 1.0f);
 for (int i = 0; i < 6; i++) {
 charHash.put((char) i, i);
 }
 assertFalse(charHash.put((char) 6, 6));
 }

 @Test
 public void testRehash() {
 charHash = new TCharHash(5);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 charHash.put('d', 4);
 charHash.put('e', 5);
 charHash.setUp(16);
 charHash.put('f', 6);
 assertEquals(7, charHash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 charHash = new TCharHash(5);
 for (int i = 0; i < 5; i++) {
 charHash.put((char) i, i);
 }
 for (int i = 5; i < 10; i++) {
 charHash.put((char) i, i);
 }
 }
}