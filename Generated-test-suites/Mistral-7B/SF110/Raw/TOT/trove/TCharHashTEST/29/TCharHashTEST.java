import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 @Test
 public void testDefaultConstructor() {
 TCharHash charHash = new TCharHash();
 int capacity = charHash.capacity();
 assertTrue(capacity > 0);
 char noEntryValue = charHash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
 }

 @Test
 public void testInitialCapacityConstructor() {
 TCharHash charHash = new TCharHash(10);
 int capacity = charHash.capacity();
 assertEquals(10, capacity);
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TCharHash charHash = new TCharHash(10, 0.75f);
 int capacity = charHash.capacity();
 assertEquals(13, capacity);
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TCharHash charHash = new TCharHash(10, 0.75f, 'X');
 int capacity = charHash.capacity();
 assertEquals(13, capacity);
 char noEntryValue = charHash.getNoEntryValue();
 assertEquals('X', noEntryValue);
 }

 @Test
 public void testContains() {
 TCharHash charHash = new TCharHash(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');

 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 assertTrue(charHash.contains('D'));
 assertTrue(charHash.contains('E'));

 assertFalse(charHash.contains('F'));
 }

 @Test
 public void testForEach() {
 TCharHash charHash = new TCharHash(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');

 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charToProcess) {
 System.out.println(charToProcess);
 return true;
 }
 };

 charHash.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TCharHash charHash = new TCharHash(-5);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNoEntryValue() {
 TCharHash charHash = new TCharHash(5, 0.75f, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHashSet() {
 TCharHash charHash = new TCharHash(5, 0.75f);
 for (char i = 0; i <= 5; i++) {
 charHash.put((char) i);
 }
 }
}