import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 @Test
 public void testEmptyTCharHash() {
 TCharHash charHash = new TCharHash(10);
 assertTrue(charHash.isEmpty());
 assertFalse(charHash.contains('\0'));
 assertEquals(0, charHash.size());
 }

 @Test
 public void testBasicFunctionality() {
 TCharHash charHash = new TCharHash(10);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 }

 @Test
 public void testPutWithNoEntryValue() {
 TCharHash charHash = new TCharHash(10, '\u0001');
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 assertFalse(charHash.contains('\u0001'));
 }

 @Test
 public void testPutWithCustomNoEntryValue() {
 TCharHash charHash = new TCharHash(10, 'X', 'Z');
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 assertFalse(charHash.contains('X'));
 }

 @Test
 public void testPutWithCollisionHandling() {
 TCharHash charHash = new TCharHash(5);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('a', 3); // Should rehash
 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 }

 @Test
 public void testForEachProcedure() {
 TCharHash charHash = new TCharHash(5);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 final int count = 0;
 charHash.forEach(new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 count++;
 return true;
 }
 });
 assertEquals(3, count);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TCharHash charHash = new TCharHash(-5);
 }

 @Test
 public void testPutWithLoadFactor() {
 TCharHash charHash = new TCharHash(5, 0.75f);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 charHash.put('d', 4);
 assertEquals(6, charHash.size());
 }

 @Test
 public void testRemove() {
 TCharHash charHash = new TCharHash(10);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 charHash.remove('a');
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 }
}