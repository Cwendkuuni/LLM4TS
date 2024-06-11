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
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testInitialCapacityAndLoadFactor() {
 charHash = new TCharHash(5, 0.75f);
 assertEquals(5, charHash.size());
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.put('d');
 charHash.put('e');
 assertFalse(charHash.put('f'));
 assertEquals(5, charHash.size());
 }

 @Test
 public void testCustomNoEntryValue() {
 charHash = new TCharHash(10, 'X');
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 charHash = new TCharHash(10);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 }

 @Test
 public void testRemove() {
 charHash = new TCharHash(10);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.remove('b');
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('b'));
 }

 @Test
 public void testPutAndGet() {
 charHash = new TCharHash(10);
 charHash.put('a', (char) 1);
 assertEquals((char) 1, charHash.get('a'));
 }

 @Test
 public void testPutAndGetEdgeCases() {
 charHash = new TCharHash(10);
 char minValue = (char) Short.MIN_VALUE;
 char maxValue = (char) Short.MAX_VALUE;
 charHash.put(minValue, minValue);
 assertEquals(minValue, charHash.get(minValue));
 charHash.put(maxValue, maxValue);
 assertEquals(maxValue, charHash.get(maxValue));
 }

 @Test
 public void testPutAndGetNull() {
 charHash = new TCharHash(10);
 assertNull(charHash.get((char) 0));
 try {
 charHash.put((char) 0, null);
 fail("Should not allow null values.");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testForEach() {
 charHash = new TCharHash(3);
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals(charValue, (char) (charValue - 'a' + 1));
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertionFailure() {
 charHash = new TCharHash(3);
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.put('d');
 }
}