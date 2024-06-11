import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Test
 public void testEmptyTCharHash() {
 charHash = new TCharHash(5);
 assertTrue(charHash.isEmpty());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testTCharHashWithInitialCapacityAndDefaultLoadFactor() {
 charHash = new TCharHash(10);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 }

 @Test
 public void testTCharHashWithInitialCapacityAndCustomLoadFactor() {
 charHash = new TCharHash(10, 0.75f);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 assertEquals(4, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 }

 @Test
 public void testTCharHashWithInitialCapacityCustomLoadFactorAndCustomNoEntryValue() {
 charHash = new TCharHash(10, 0.75f, 'X');
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 assertEquals(4, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testTCharHashWithPutNullValue() {
 charHash = new TCharHash(5);
 assertFalse(charHash.contains(null));
 charHash.put('a');
 charHash.put(null);
 charHash.put('b');
 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertFalse(charHash.contains(null));
 }

 @Test
 public void testTCharHashWithPutSameValue() {
 charHash = new TCharHash(5);
 charHash.put('a');
 charHash.put('a');
 assertEquals(1, charHash.size());
 assertTrue(charHash.contains('a'));
 }

 @Test
 public void testTCharHashWithPutDifferentValues() {
 charHash = new TCharHash(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 assertEquals(3, charHash.size());
 assertFalse(charHash.contains('d'));
 }

 @Test
 public void testTCharHashWithRemoveValue() {
 charHash = new TCharHash(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.remove('b');
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('b'));
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('c'));
 }

 @Test
 public void testTCharHashWithReplaceValue() {
 charHash = new TCharHash(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.put('d', 'e');
 assertEquals(4, charHash.size());
 assertFalse(charHash.contains('b'));
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('c'));
 assertTrue(charHash.contains('d'));
 assertEquals('e', charHash.get('d'));
 }

 @Test
 public void testTCharHashWithContainsAllValues() {
 charHash = new TCharHash(5);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 assertTrue(charHash.containsAll(Arrays.asList('a', 'b', 'c')));
 }

 @Test
 public void testTCharHashWithContainsNullValue() {
 charHash = new TCharHash(5);
 assertFalse(charHash.contains(null));
 }

 @Test
 public void testTCharHashWithCustomProcedure() {
 charHash = new TCharHash(5);charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 final TCharProcedure charProcedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 return character == 'c';
 }
 };
 assertTrue(charHash.forEach(charProcedure));
 }
}