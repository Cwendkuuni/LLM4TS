import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash<Character> charHash;

 @Test
 public void testEmptyCharHash() {
 charHash = new TCharHash<>(5);
 assertTrue(charHash.isEmpty());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testInitialCapacityCharHash() {
 charHash = new TCharHash<>(5, 'a');
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.put('d');
 charHash.put('e');
 assertEquals(5, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 assertTrue(charHash.contains('d'));
 assertTrue(charHash.contains('e'));
 }

 @Test
 public void testLoadFactorCharHash() {
 charHash = new TCharHash<>(5, 0.75f, 'a');
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.put('d');
 charHash.put('e');
 charHash.put('f');
 charHash.put('g');
 assertEquals(10, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 assertTrue(charHash.contains('d'));
 assertTrue(charHash.contains('e'));
 assertTrue(charHash.contains('f'));
 assertTrue(charHash.contains('g'));
 }

 @Test
 public void testNoEntryValueCharHash() {
 charHash = new TCharHash<>(5, 'x');
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.put('d');
 charHash.put('e');
 assertEquals(5, charHash.size());
 assertFalse(charHash.contains('x'));
 }

 @Test
 public void testPutAndGetCharHash() {
 charHash = new TCharHash<>(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 assertEquals('c', charHash.get('c'));
 assertEquals('b', charHash.get('b'));
 assertEquals('a', charHash.get('a'));
 }

 @Test
 public void testRemoveCharHash() {
 charHash = new TCharHash<>(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.remove('b');
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('b'));
 }

 @Test
 public void testContainsCharHash() {
 charHash = new TCharHash<>(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 }

 @Test
 public void testIterateCharHash() {
 charHash = new TCharHash<>(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 final TCharProcedure charProcedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 assertEquals(character, charHash.get(character));
 return true;
 }
 };
 charHash.forEach(charProcedure);
 }

 @Test(expected = IllegalStateException.class)
 public void testFullCharHash() {
 charHash = new TCharHash<>(5);
 for (char i = 'a'; i <= 'z'; i++) {
 charHash.put(i);
 }
 }
}