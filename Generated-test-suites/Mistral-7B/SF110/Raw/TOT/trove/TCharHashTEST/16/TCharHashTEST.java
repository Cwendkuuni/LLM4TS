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
 public void testEmptyConstructor() {
 assertEquals(0, charHash.size());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 charHash = new TCharHash(5);
 assertEquals(5, charHash.size());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 charHash = new TCharHash(10, 0.75f);
 assertEquals(10, charHash.size());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 charHash = new TCharHash(10, 0.75f, 'X');
 assertEquals(10, charHash.size());
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash(5);
 charHash.setNoEntryValue('Z');
 assertEquals('Z', charHash.getNoEntryValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 charHash = new TCharHash(-5);
 fail("Expected an IllegalArgumentException");
 }

 @Test
 public void testLoadFactorGreaterThanOne() {
 charHash = new TCharHash(5, 2.0f);
 assertEquals(5, charHash.size());
 }

 @Test
 public void testResize() {
 charHash = new TCharHash(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 charHash.put('G'); // should trigger resize
 charHash.put('H');
 charHash.put('I');
 charHash.put('J');
 assertEquals(11, charHash.size());
 }

 @Test
 public void testContains() {
 charHash = new TCharHash(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 assertTrue(charHash.contains('D'));
 assertTrue(charHash.contains('E'));
 assertTrue(charHash.contains('F'));
 }

 @Test
 public void testForEach() {
 charHash = new TCharHash(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char key) {
 assertEquals('A', key);
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 charHash = new TCharHash(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 int index = charHash.indexOf('B');
 charHash.removeAt(index);
 assertEquals(5, charHash.size());
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testIndex() {
 charHash = new TCharHash(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 assertEquals(5, charHash.indexOf('F'));
 assertEquals(-1, charHash.indexOf('G'));
 }

 @Test
 public void testInsertKey() {
 charHash = new TCharHash(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 charHash.put('G');
 assertEquals(7, charHash.size());
 assertTrue(charHash.contains('G'));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 charHash = new TCharHash(5);
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 charHash.put('G');
 charHash.put('H');
 charHash.put('I');
 charHash.put('J');
 charHash.put('K'); // should throw an IllegalStateException
 }

}