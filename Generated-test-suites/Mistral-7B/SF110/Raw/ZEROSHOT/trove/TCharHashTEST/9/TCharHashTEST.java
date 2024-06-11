import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 @Test
 public void testEmptyTCharHash() {
 TCharHash charHash = new TCharHash();
 assertEquals(0, charHash.size());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testBasicFunctionality() {
 TCharHash charHash = new TCharHash(11);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);

 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));

 assertEquals(1, charHash.get('a'));
 assertEquals(2, charHash.get('b'));
 assertEquals(3, charHash.get('c'));

 charHash.remove('a');
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('a'));
 assertFalse(charHash.containsKey('a'));
 }

 @Test
 public void testLoadFactor() {
 TCharHash charHash = new TCharHash(3, 0.75f);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 charHash.put('d', 4);
 charHash.put('e', 5);
 charHash.put('f', 6);
 charHash.put('g', 7);

 assertEquals(7, charHash.size());

 // Test if hash table was resized
 assertTrue(charHash._set.length > 3);

 // Test if elements are present in the hash table
 for (char key : new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'}) {
 assertTrue(charHash.contains(key));
 }
 }

 @Test
 public void testCustomNoEntryValue() {
 TCharHash charHash = new TCharHash(11, 'X');
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);

 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));

 assertEquals('X', charHash.get('d'));
 assertFalse(charHash.contains('d'));
 }

 @Test
 public void testForEach() {
 TCharHash charHash = new TCharHash(5);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 charHash.put('d', 4);
 charHash.put('e', 5);

 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 System.out.println("Value: " + charValue);
 return true;
 }
 };

 charHash.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TCharHash charHash = new TCharHash(-5);
 }
}