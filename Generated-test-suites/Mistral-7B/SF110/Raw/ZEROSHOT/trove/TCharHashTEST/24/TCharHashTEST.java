import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 char defaultValue = TCharHash.class.getFields()[1].get(charHash);
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultValue);
 }

 @Test
 public void testInitialCapacityConstructor() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 charHash = new TCharHash(20, 0.75f);
 assertEquals(20, charHash.capacity());
 assertEquals(0.75f, charHash.loadFactor(), 0.01f);
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 charHash = new TCharHash(15, 0.8f, 'X');
 assertEquals(15, charHash.capacity());
 assertEquals(0.8f, charHash.loadFactor(), 0.01f);
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 char defaultValue = TCharHash.class.getFields()[1].get(charHash);
 char hashInstanceNoEntryValue = charHash.getNoEntryValue();
 assertEquals(defaultValue, hashInstanceNoEntryValue);
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.capacity());
 char[] set = charHash._set;
 assertNotNull(set);
 }

 @Test
 public void testContains() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 }

 @Test
 public void testContainsWithEmptyHash() {
 assertFalse(charHash.contains('A'));
 }

 @Test
 public void testForEach() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 if (character == 'A') {
 return false;
 }
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWithModifyingProcedure() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 if (character == 'A') {
 charHash.put('D', 'D');
 return false;
 }
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 int indexToRemove = 1;
 char removedValue = charHash.removeAt(indexToRemove);
 assertEquals('B', removedValue);
 }

 @Test
 public void testRemoveAtWithEmptyHash() {
 assertNull(charHash.removeAt(0));
 }

 @Test
 public void testIndex() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 int indexOfA = charHash.index('A');
 int indexOfB = charHash.index('B');
 int indexOfC = charHash.index('C');
 assertEquals(0, indexOfA);
 assertEquals(1, indexOfB);
 assertEquals(2, indexOfC);
 }

 @Test
 public void testIndexWithEmptyHash() {
 assertEquals(-1, charHash.index('A'));
 }

 @Test
 public void testIndexRehashed() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.put('E', 'E');
 charHash.put('F', 'F');
 int indexOfA = charHash.index('A');
 int indexOfB = charHash.index('B');
 int indexOfC = charHash.index('C');
 int indexOfD = charHash.index('D');
 int indexOfE = charHash.index('E');
 int indexOfF = charHash.index('F');
 int indexOfG = charHash.index('G');
 assertEquals(0, indexOfA);
 assertEquals(1, indexOfB);
 assertEquals(2, indexOfC);
 assertEquals(3, indexOfD);
 assertEquals(4, indexOfE);
 assertEquals(5, indexOfF);
 assertEquals(-1, indexOfG);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A', 'A');
 char newKey = 'B';
 int index = charHash.insertKey(newKey);
 assertEquals(1, index);
 assertEquals('B', charHash.get(index));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHash() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.put('E', 'E');
 charHash.put('F', 'F');
 charHash.put('G', 'G');
 char newKey = 'H';
 int index = charHash.insertKey(newKey);
 }

 @Test
 public void testInsertKeyRehash() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.put('E', 'E');
 charHash.put('F', 'F');
 char newKey = 'G';
 int index = charHash.insertKeyRehash(newKey, 0, 1, 1);
 assertEquals(1, index);
 assertEquals('G', charHash.get(index));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithFullHash() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.put('E', 'E');
 charHash.put('F', 'F');
 charHash.put('G', 'G');
 charHash.put('H', 'H');
 char newKey = 'I';
 int index = charHash.insertKeyRehash(newKey, 0, 1, 1);
 }
}