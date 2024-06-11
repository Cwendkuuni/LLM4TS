import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setup() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testEmptyTCharHash() {
 assertTrue(charHash.isEmpty());
 assertFalse(charHash.contains('a'));
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testTCharHashWithInitialCapacity() {
 charHash = new TCharHash(5, 'e');
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
 assertFalse(charHash.contains('f'));
 assertEquals('e', charHash.getNoEntryValue());

 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 return charValue == 'a' || charValue == 'e';
 }
 };

 assertTrue(charHash.forEach(procedure));
 }

 @Test
 public void testTCharHashWithLoadFactor() {
 charHash = new TCharHash(5, 0.75f);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.put('d');
 charHash.put('e');
 charHash.put('f');
 charHash.put('g');
 charHash.put('h');

 assertEquals(8, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 assertTrue(charHash.contains('d'));
 assertTrue(charHash.contains('e'));
 assertTrue(charHash.contains('f'));
 assertTrue(charHash.contains('g'));
 assertTrue(charHash.contains('h'));
 assertFalse(charHash.contains('i'));
 assertEquals('h', charHash.getNoEntryValue());
 }

 @Test
 public void testTCharHashWithInitialCapacityAndLoadFactor() {
 charHash = new TCharHash(10, 0.75f, 'i');
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.put('d');
 charHash.put('e');
 charHash.put('f');
 charHash.put('g');
 charHash.put('h');
 charHash.put('i');
 charHash.put('j');
 charHash.put('k');
 charHash.put('l');
 charHash.put('m');
 charHash.put('n');
 charHash.put('o');
 charHash.put('p');
 charHash.put('q');
 charHash.put('r');
 charHash.put('s');
 charHash.put('t');
 charHash.put('u');
 charHash.put('v');
 charHash.put('w');
 charHash.put('x');
 charHash.put('y');
 charHash.put('z');

 assertEquals(26, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 //... (all other characters should be present as well)
 assertFalse(charHash.contains('\u0000')); // null character
 assertFalse(charHash.contains('\u007F')); // delete character
 assertNull(charHash.getNoEntryValue());
 }
}