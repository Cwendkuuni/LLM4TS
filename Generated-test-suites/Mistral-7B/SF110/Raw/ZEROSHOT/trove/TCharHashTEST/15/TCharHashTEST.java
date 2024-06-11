import org.junit.After;
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
 public void testDefaultConstructor() {
 assertEquals(5, charHash.capacity());
 assertNull(charHash.getNoEntryValue());
 assertArrayEquals(new char[5], charHash._set);
 }

 @Test
 public void testInitialCapacityConstructor() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.capacity());
 assertNull(charHash.getNoEntryValue());
 assertArrayEquals(new char[10], charHash._set);
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 charHash = new TCharHash(15, 0.75f);
 assertEquals(15, charHash.capacity());
 assertNull(charHash.getNoEntryValue());
 assertArrayEquals(new char[11], charHash._set); // 11 due to load factor
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 charHash = new TCharHash(20, 0.75f, 'X');
 assertEquals(20, charHash.capacity());
 assertEquals('X', charHash.getNoEntryValue());
 assertArrayEquals(new char[20], charHash._set); // 20 due to initialCapacity
 }

 @Test
 public void testGetNoEntryValue() {
 charHash.setNoEntryValue('A');
 assertEquals('A', charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 int capacity = charHash.setUp(7);
 assertEquals(7, capacity);
 assertArrayEquals(new char[7], charHash._set);
 }

 @Test
 public void testContains() {
 charHash.put('a', 'A');
 assertTrue(charHash.contains('a'));
 }

 @Test
 public void testForEach() {
 charHash.put('a', 'A');
 charHash.put('b', 'B');
 charHash.put('c', 'C');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals('A', charValue);
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 charHash.put('a', 'A');
 charHash.put('b', 'B');
 charHash.put('c', 'C');
 int index = charHash.indexOf('b');
 charHash.removeAt(index);
 assertFalse(charHash.contains('b'));
 }

 @Test
 public void testIndex() {
 charHash.put('a', 'A');
 charHash.put('b', 'B');
 charHash.put('c', 'C');
 int index = charHash.indexOf('b');
 assertEquals(index, 1);
 }

 @Test
 public void testIndexRehashed() {
 charHash.put('a', 'A');
 charHash.put('b', 'B');
 charHash.put('c', 'C');
 charHash.put('d', 'D');
 charHash.put('e', 'E');
 int index = charHash.indexOf('e');
 assertEquals(index, 4);
 }

 @Test
 public void testInsertKey() {
 charHash.put('a', 'A');
 charHash.put('b', 'B');
 charHash.put('c', 'C');
 charHash.put('d', 'D');
 charHash.put('e', 'E');
 charHash.put('f', 'F');
 int index = charHash.indexOf('f');
 assertEquals(index, 5);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 charHash.put('a', 'A');
 charHash.put('b', 'B');
 charHash.put('c', 'C');
 charHash.put('d', 'D');
 charHash.put('e', 'E');
 charHash.put('f', 'F');
 charHash.put('g', 'G'); // should throw an exception
 }

 @After
 public void tearDown() {
 charHash = null;
 }
}