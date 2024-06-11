import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 @Test
 public void testEmptyTCharHash() {
 TCharHash charHash = new TCharHash(5);
 assertTrue(charHash.isEmpty());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testBasicFunctionality() {
 TCharHash charHash = new TCharHash(5);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);

 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));

 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 return charValue != 'b';
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test
 public void testLoadFactor() {
 TCharHash charHash = new TCharHash(5);
 for (int i = 0; i < 6; i++) {
 charHash.put((char) i, i);
 }
 assertEquals(6, charHash.size());
 assertTrue(charHash.contains((char) 0)); // due to hash collision
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 new TCharHash(-5);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNoEntryValue() {
 new TCharHash(5, null);
 }

 @Test
 public void testCustomNoEntryValue() {
 TCharHash charHash = new TCharHash(5, 'X');
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);

 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testRemove() {
 TCharHash charHash = new TCharHash(5);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 charHash.remove('b');

 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('b'));
 }
}