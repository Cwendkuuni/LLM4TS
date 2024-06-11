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
 charHash = new TCharHash(11);
 }

 @Test
 public void testDefaultConstructor() {
 charHash = new TCharHash();
 assertEquals(0, charHash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 charHash = new TCharHash(11);
 assertEquals(11, charHash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 charHash = new TCharHash(11, 0.75f);
 assertEquals(11, charHash.size());
 assertEquals(0.75f, charHash.getLoadFactor(), 0.01f);
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 charHash = new TCharHash(11, 0.75f, 'X');
 assertEquals(11, charHash.size());
 assertEquals(0.75f, charHash.getLoadFactor(), 0.01f);
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash(11);
 charHash.setNoEntryValue('Z');
 assertEquals('Z', charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(11);
 assertEquals(11, charHash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TCharHash charHash = new TCharHash(-5);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialCapacityGreaterThanMaxValue() {
 TCharHash charHash = new TCharHash(Integer.MAX_VALUE + 1);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testContains() {
 charHash.put('A', 1);
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testDoesNotContain() {
 charHash.put('A', 1);
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testForEach() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals(charValue, (char) (charValue + 1));
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 int index = charHash.indexOf('B');
 charHash.removeAt(index);
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testIndex() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 int index = charHash.indexOf('B');
 assertEquals(index, 1);
 }

 @Test
 public void testIndexDoesNotExist() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 int index = charHash.indexOf('D');
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 int index = charHash.insertKey('D');
 assertEquals(4, charHash.size());
 assertEquals(index, 3);
 }

 @Test
 public void testInsertKeyExistingValue() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('B', 4);
 int index = charHash.insertKey('B');
 assertEquals(5, charHash.size());
 assertEquals(index, 4);
 }

 @Test
 public void testInsertKeyRehashing() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 charHash.put('F', 6);
 charHash.put('G', 7);
 charHash.put('H', 8);
 charHash.put('I', 9);
 charHash.put('J', 10);
 charHash.put('K', 11);
 charHash.put('L', 12);
 charHash.put('M', 13);
 charHash.put('N', 14);
 charHash.put('O', 15);
 charHash.put('P', 16);
 charHash.put('Q', 17);
 charHash.put('R', 18);
 charHash.put('S', 19);
 charHash.put('T', 20);
 charHash.put('U', 21);
 charHash.put('V', 22);
 charHash.put('W', 23);
 charHash.put('X', 24);
 charHash.put('Y', 25);
 charHash.put('Z', 26);
 charHash.put('Z', 27); // Duplicate key
 int index = charHash.insertKey('Z');
 assertEquals(28, charHash.size());
 assertEquals(-1, index); // Duplicate key should not be inserted
 }

 @After
 public void tearDown() {
 charHash = null;
 }
}