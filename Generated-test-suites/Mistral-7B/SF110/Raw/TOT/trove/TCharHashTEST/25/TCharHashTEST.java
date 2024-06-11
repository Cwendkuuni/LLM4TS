import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash<Character> charHash;

 @Test
 public void testEmptyTCharHash() {
 charHash = new TCharHash<>(5);
 assertTrue(charHash.isEmpty());
 assertEquals(0, charHash.size());
 }

 @Test
 public void testTCharHashWithInitialCapacityAndLoadFactor() {
 charHash = new TCharHash<>(10, 0.75f);
 assertEquals(10, charHash.initialCapacity());
 assertEquals(0.75f, charHash.loadFactor(), 0.01);
 }

 @Test
 public void testTCharHashWithNoEntryValue() {
 charHash = new TCharHash<>(5, 'N', 0.75f);
 assertEquals('N', charHash.getNoEntryValue());
 }

 @Test
 public void testTCharHashAddCharacter() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTCharHashNegativeInitialCapacity() {
 charHash = new TCharHash<>("-5");
 fail("Expected IllegalArgumentException but got: " + charHash);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTCharHashNegativeLoadFactor() {
 charHash = new TCharHash<>(5, -0.75f);
 fail("Expected IllegalArgumentException but got: " + charHash);
 }

 @Test
 public void testTCharHashRemoveCharacter() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.remove('B');
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testTCharHashContainsCharacter() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 }

 @Test
 public void testTCharHashForEach() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char v) {
 if (v == 'A') {
 return false;
 }
 return true;
 }
 };
 charHash.forEach(procedure);
 assertEquals(2, charHash.size());
 }

 @Test
 public void testTCharHashContainsNull() {
 charHash = new TCharHash<>(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertFalse(charHash.contains(null));
 }

}