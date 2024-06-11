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
 public void testEmptyHash() {
 assertTrue(charHash.isEmpty());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testBasicOperations() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');

 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));

 assertEquals('a', charHash.get('a'));
 assertEquals('b', charHash.get('b'));
 assertEquals('c', charHash.get('c'));

 charHash.remove('b');
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('b'));
 assertEquals('a', charHash.get('a'));
 assertEquals('c', charHash.get('c'));
 }

 @Test
 public void testLoadFactor() {
 charHash = new TCharHash(3, 0.75f);
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.put('d', 'd');
 charHash.put('e', 'e');

 assertEquals(5, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 assertTrue(charHash.contains('d'));
 assertTrue(charHash.contains('e'));

 charHash.put('f', 'f'); // should cause rehash
 assertEquals(11, charHash.size());
 assertTrue(charHash.contains('f'));
 }

 @Test
 public void testNoEntryValue() {
 charHash = new TCharHash(5, 'x');
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');

 assertEquals('x', charHash.getNoEntryValue());
 assertNull(charHash.get('a'));
 assertNull(charHash.get('b'));
 assertNull(charHash.get('c'));
 }

 @Test
 public void testProcedure() {
 charHash = new TCharHash(5);
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');

 final TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char element) {
 if (element == 'b') {
 fail("Unexpected 'b' in the hash set");
 }
 return true;
 }
 };

 assertTrue(charHash.forEach(procedure));
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHash() {
 charHash = new TCharHash(5);
 for (char i = 'a'; i <= 'z'; i++) {
 charHash.put(i, i);
 }
 }

}