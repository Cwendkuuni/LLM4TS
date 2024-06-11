import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(11);
 }

 @Test
 public void testDefaultConstructor() {
 assertEquals(11, charHash.capacity());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 charHash = new TCharHash(5);
 assertEquals(5, charHash.capacity());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 charHash = new TCharHash(7, 0.75f);
 assertEquals(10, charHash.capacity());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 charHash = new TCharHash(13, 0.75f, 'X');
 assertEquals(13, charHash.capacity());
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash.put('A');
 assertEquals('A', charHash.getNoEntryValue());
 charHash.put('B');
 charHash.put('C');
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int capacity = charHash.setUp(17);
 assertEquals(17, capacity);
 }

 @Test
 public void testContains() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 }

 @Test
 public void testForEach() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals(charValue, charHash.get(charValue));
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int initialSize = charHash.size();
 charHash.removeAt(1);
 assertEquals(initialSize - 1, charHash.size());
 }

 @Test
 public void testIndex() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertEquals(0, charHash.index('A'));
 assertEquals(-1, charHash.index('X'));
 }

 @Test
 public void testIndexRehashed() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 int index = charHash.index('B');
 assertEquals(-1, index);
 index = charHash.indexRehashed('B', index, HashFunctions.hash('B'), (byte) 1);
 assertEquals(1, index);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = charHash.insertKey('D');
 assertEquals(4, charHash.size());
 assertEquals(3, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashFailure() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 charHash.put('G');
 }

 @Test
 public void testInsertKeyAt() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.insertKeyAt(3, 'D');
 assertEquals(5, charHash.size());
 assertEquals('D', charHash.get('D'));
 }
}